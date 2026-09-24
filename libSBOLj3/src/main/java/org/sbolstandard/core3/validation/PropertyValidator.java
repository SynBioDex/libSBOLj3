package org.sbolstandard.core3.validation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;

public class PropertyValidator {
	//private static PropertyValidator propertyValidator = null;
	private ExecutableValidator validator;
	
	private PropertyValidator() throws SBOLGraphException
	{	
		try
		{
			jakarta.validation.Configuration<?> config = Validation.byDefaultProvider().configure();
			ValidatorFactory factory = config
					.parameterNameProvider(new AnnotationBasedParameterNameProvider(config.getDefaultParameterNameProvider()))
					.buildValidatorFactory();
			
			//propertyValidator.validator = factory.getValidator().forExecutables();	
			this.setExecutableValidator(factory.getValidator().forExecutables());	
		}
		catch (Exception exception)
		{
			throw new SBOLGraphException("Could not initialize the property validator. " + exception.getMessage(), exception);
		}
	}
	
	private void setExecutableValidator(ExecutableValidator executableValidator) throws SBOLGraphException	
	{
		if (executableValidator==null)
		{
			throw new SBOLGraphException("Unable to create an ExecutableValidator");
		}
			
		this.validator=executableValidator;
	}
	
	/*public static PropertyValidator getValidator() throws SBOLGraphException
	{
		if (propertyValidator == null)
		{
			try
			{
				propertyValidator=new PropertyValidator();
				ValidatorFactory factory = Validation.byDefaultProvider()
		 	            .configure()
		 	            .buildValidatorFactory();
				
				//propertyValidator.validator = factory.getValidator().forExecutables();	
				propertyValidator.setExecutableValidator(factory.getValidator().forExecutables());	
			}
			catch (Exception exception)
			{
				throw new SBOLGraphException("Could not initialize the property validator. " + exception.getMessage(), exception);
			}
		}
		return propertyValidator;
	}*/

	public static PropertyValidator getValidator() throws SBOLGraphException {
		return SingletonHelper.INSTANCE;
	}

	private static class SingletonHelper {
        private static final PropertyValidator INSTANCE ;
        static {
            try {
                INSTANCE = new PropertyValidator();
            } catch (SBOLGraphException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }
	
	
	public void validateReturnValue(Identified identified, String methodName, Object returnValue, Class<?>... parameterTypes) throws SBOLGraphException
	{
		Method method;
		try {
			method = identified.getClass().getMethod(methodName, parameterTypes);
		} 
		catch (NoSuchMethodException | SecurityException e) {
			throw new SBOLGraphException(e.getMessage(),e);
		}
		
		Set<ConstraintViolation<Identified>> violations = this.validator.validateReturnValue(identified, method,returnValue);
		processViolations(violations);
	}
	
	public static String  getViolotionMessage(ConstraintViolation<?> violation)
	{
		List<String> fragments=new ArrayList<String>();
    	fragments.add(violation.getMessage());
    	fragments.add(String.format("Property: %s",getPropertyPathString(violation)));
		//fragments.add(String.format("Property: %s",violation.getPropertyPath().toString()));
    	if (violation.getLeafBean()!=null && violation.getLeafBean() instanceof Identified ){
    	    Identified identifiedLeaf= (Identified) violation.getLeafBean();
    	    fragments.add(String.format("Entity URI: %s",identifiedLeaf.getUri().toString()));
    	    fragments.add(String.format("Entity Type: %s",identifiedLeaf.getClass().getSimpleName()));    
    	}
    	if (violation.getInvalidValue()!=null && !(violation.getInvalidValue() instanceof Identified) && !(violation.getInvalidValue() instanceof SBOLDocument)){    	
    		fragments.add("Value: " + violation.getInvalidValue().toString());
    	}
    	String message=StringUtils.join(fragments, "," + System.lineSeparator()  + "\t");
    	return message;
	}
	
	private static String getPropertyPathString(ConstraintViolation<?> violation) {
		Object leafBean = violation.getLeafBean();
		if (leafBean != null) {
			for (jakarta.validation.Path.Node node : violation.getPropertyPath()) {
				if (node.getKind() == jakarta.validation.ElementKind.METHOD) {
					// Return-value / parameter validation: look up @PropertyName on the method
					PropertyName ann = findMethodAnnotation(leafBean.getClass(), node.getName());
					if (ann != null) {
						return ann.value();
					}
				} else if (node.getKind() == jakarta.validation.ElementKind.PROPERTY) {
					// Getter property constraint: derive getter name and look for @PropertyName
					String name = node.getName();
					String getter = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
					PropertyName ann = findMethodAnnotation(leafBean.getClass(), getter);
					if (ann != null) {
						return ann.value();
					}
				}
			}
		}
		String path = violation.getPropertyPath().toString();
		if (path.startsWith("set")) {
			int dot = path.indexOf('.');
			return dot >= 0 ? path.substring(dot + 1) : path;
		}
		return path;
		
	}

	private static PropertyName findMethodAnnotation(Class<?> clazz, String methodName) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(methodName)) {
				return m.getAnnotation(PropertyName.class);
			}
		}
		return null;
	}

	public static List<String>  getViolotionMessages(Set<ConstraintViolation<Identified>> violations)
	{
		List<String> messages=null;
		if (violations!=null && violations.size()>0){
			messages=new ArrayList<String>();
			for (ConstraintViolation<Identified> violation : violations) {
		    	messages.add(getViolotionMessage(violation));
			}
		}
		return messages;
	}
	private void processViolations(Set<ConstraintViolation<Identified>> violations) throws SBOLGraphException
	{
		List<String> messages=PropertyValidator.getViolotionMessages(violations);
		if (messages!=null && messages.size()>0)
		{	
			String errorMessage=StringUtils.join(messages, "," + System.lineSeparator()  + "\t");	
			throw new SBOLGraphException(errorMessage);
		
		}
	}
	
	public void validate(Identified identified, String methodName, Object[] parameterValues, Class<?>... parameterTypes) throws SBOLGraphException
	{
		if (Configuration.getInstance().isValidateAfterSettingProperties())
		{
			Method method;
			try {
				method = identified.getClass().getMethod(methodName, parameterTypes);
			} 
			catch (NoSuchMethodException | SecurityException e) {
				throw new SBOLGraphException(e.getMessage(),e);
			}
			
			Set<ConstraintViolation<Identified>> violations = this.validator.validateParameters(identified, method,parameterValues);
			processViolations(violations);
		}
	}
	
}
