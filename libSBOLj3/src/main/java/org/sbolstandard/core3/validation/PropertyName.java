package org.sbolstandard.core3.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Overrides the property name reported in a {@link jakarta.validation.ConstraintViolation}
 * property path when Hibernate Validator validates method parameters via
 * {@link jakarta.validation.executable.ExecutableValidator#validateParameters}.
 *
 * <p>Place this annotation on a setter parameter to control the name that
 * appears in {@code violation.getPropertyPath()} instead of the default
 * name derived from the parameter variable or the method name.
 *
 * <pre>{@code
 * public void setRoles(@PropertyName("roles") @NotEmpty(...) List<URI> roles) { ... }
 * }</pre>
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyName {
    /** The property name to use in the constraint violation path. */
    String value();
}
