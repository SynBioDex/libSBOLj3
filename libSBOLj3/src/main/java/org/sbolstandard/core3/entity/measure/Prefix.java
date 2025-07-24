package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * Represents a unit prefix in the SBOL data model.
 *
 */
public abstract class Prefix extends Unit{
	
	//private float factor=Float.NaN;
	protected  Prefix(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Prefix(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Sets the factor of the prefix.
	 * @param factor The factor to be applied.
	 * @throws SBOLGraphException
	 */
	public void setFactor(@NotNull (message = "{PREFIX_FACTOR_NOT_NULL}") Optional<@NotNull (message = "{PREFIX_FACTOR_NOT_NULL}") Float> factor) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setFactor", new Object[] {factor}, Optional.class);
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.Prefix.factor, factor);		
	}
	
	/**
	 * Gets the factor of the prefix.
	 * @return The associated factor.
	 * @throws SBOLGraphException
	 */
	@NotNull (message = "{PREFIX_FACTOR_NOT_NULL}") 
	public Optional<@NotNull (message = "{PREFIX_FACTOR_NOT_NULL}") Float> getFactor() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalFloat(this.resource, MeasureDataModel.Prefix.factor);
	}
	
	/**
	 * Gets the URI of the prefix.
	 * @return The corresponding URI
	 */
	@Override
	public URI getResourceType() {
		return MeasureDataModel.Prefix.uri;
	}	
	
	/**
	 * Get the subclass types of the prefix.
	 * @param <T>
	 * @return The corresponding subclasses.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Identified> HashMap<URI, Class<T>> getSubClassTypes()
	{
		HashMap<URI, Class<T>> subclasses=new HashMap<URI, Class<T>>();
		subclasses.put(MeasureDataModel.BinaryPrefix.uri, (Class<T>) BinaryPrefix.class);
		subclasses.put(MeasureDataModel.SIPrefix.uri, (Class<T>) SIPrefix.class);
		return subclasses;
	}
	
	@Override
	protected String getNameLabelMessage()
	{
		return "{PREFIX_NAME_LABEL_EQUAL}";
	}
	
	@Override
	protected String getDescriptionCommentMessage()
	{
		return "{PREFIX_DESCRIPTION_COMMENT_EQUAL}";
	}
		
}