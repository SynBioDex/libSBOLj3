package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotNull;

/**
 * 
 * Represents unit multiplication operations in the SBOL data model.
 *
 */
public class UnitMultiplication extends CompoundUnit{
	/*private URI term1;
	private URI term2;*/
	protected  UnitMultiplication(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitMultiplication(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Creates a new object for the unit multiplication operation.
	 * @param sbolDocument A valid SBOL document.
	 * @param uri The URI associated with this operation.
	 * @param namespace The namespace of this operation.
	 * @return A newly constructed UnitMultiplication object.
	 * @throws SBOLGraphException
	 */
	public static UnitMultiplication create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		UnitMultiplication identified = new UnitMultiplication(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
	/**
	 * Gets the first term associated with the operation.
	 * @return The unit expressing the first term.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{UNITMULTIPLICATION_TERM1_NOT_NULL}")	
	public Unit getTerm1() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term1);	
		return contsructIdentified(MeasureDataModel.UnitMultiplication.term1, Unit.getSubClassTypes());	
	}
	
	/**
	 * Sets the first term in the multiplication.
	 * @param term1 The unit expressing the first term.
	 * @throws SBOLGraphException
	 */
	public void setTerm1(@NotNull(message = "UNITMULTIPLICATION_TERM1_NOT_NULL}") Unit term1) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTerm1", new Object[] {term1}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term1, SBOLUtil.toURI(term1));
	}
	
	/**
	 * Gets the second term associated with the operation.
	 * @return The unit expressing the second term.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "UNITMULTIPLICATION_TERM2_NOT_NULL}")	
	public Unit getTerm2() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term2);
		return contsructIdentified(MeasureDataModel.UnitMultiplication.term2, Unit.getSubClassTypes());	
	}
	
	/**
	 * Sets the second term in the multiplication.
	 * @param term2 The unit expressing the second term.
	 * @throws SBOLGraphException
	 */
	public void setTerm2(@NotNull(message = "UNITMULTIPLICATION_TERM2_NOT_NULL}") Unit term2) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTerm2", new Object[] {term2}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term2, SBOLUtil.toURI(term2));
	}
	
	/**
	 * Gets the resource type associated with the unit multiplication operation.
	 * @return The corresponding URI.
	 */
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitMultiplication.uri;
	}
	
	/**
	 * Gets the validation messages associated with this unit multiplication operation.
	 * @return A list of corresponding validation messages.
	 */
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitMultiplication.term1, this.resource, getTerm1(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitMultiplication.term2, this.resource, getTerm2(), validationMessages);
		return validationMessages;
	}
			
}