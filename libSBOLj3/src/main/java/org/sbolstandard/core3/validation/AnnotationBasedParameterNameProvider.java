package org.sbolstandard.core3.validation;

import jakarta.validation.ParameterNameProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link ParameterNameProvider} that checks each parameter for a
 * {@link PropertyName} annotation and uses its value as the parameter name.
 * Falls back to the delegate provider for parameters without the annotation.
 */
public class AnnotationBasedParameterNameProvider implements ParameterNameProvider {

    private final ParameterNameProvider delegate;

    public AnnotationBasedParameterNameProvider(ParameterNameProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getParameterNames(Constructor<?> constructor) {
        return delegate.getParameterNames(constructor);
    }

    @Override
    public List<String> getParameterNames(Method method) {
        List<String> names = new ArrayList<>(delegate.getParameterNames(method));
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation ann : paramAnnotations[i]) {
                if (ann instanceof PropertyName) {
                    names.set(i, ((PropertyName) ann).value());
                    break;
                }
            }
        }
        return names;
    }
}
