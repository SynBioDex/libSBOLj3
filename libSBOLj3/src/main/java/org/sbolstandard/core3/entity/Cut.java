package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * This class represents a cut of a sequence.
 */
public class Cut extends Location {
	//private int at=Integer.MIN_VALUE;

	protected Cut(Model model, URI uri) throws SBOLGraphException {
		super(model, uri);
	}
	
	protected  Cut(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Gets the location of the cut.
	 * @return An object containing the position of the cut.
	 */
	@NotNull(message = "{CUT_AT_NOT_NULL}")
	public Optional<@NotNull(message = "{CUT_AT_NOT_NULL}") @PositiveOrZero(message="{CUT_AT_POSITIVE_OR_ZERO}") Integer> getAt() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalInteger(this.resource, DataModel.Cut.at);
	}
	
	/**
	 * Sets the location of the cut.
	 * @param at The location of the cut.
	 */
	public void setAt(Optional<@NotNull(message = "{CUT_AT_NOT_NULL}")  @PositiveOrZero(message="{CUT_AT_POSITIVE_OR_ZERO}")Integer> at) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setAt", new Object[] {at}, Optional.class);
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, DataModel.Cut.at, at);
	}
	
	/**
	 * Gets the resource type for the sequence cut.
	 * @return A URI object representing the corresponding resource.
	 */
	public URI getResourceType()
	{
		return DataModel.Cut.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		Optional<Integer> at=getAt();
		Sequence sequence=this.getSequence();
		
		//CUT_AT_VALID_FOR_SEQUENCE
		validationMessages= assertLocationNotBiggerThanSequence(validationMessages, sequence, at, "{CUT_AT_VALID_FOR_SEQUENCE}", DataModel.Cut.at);
	
		return validationMessages;	
	}
	
}
