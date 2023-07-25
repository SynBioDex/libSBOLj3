package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import java.util.Optional;
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
 * Represent unit exponentiation operations in the SBOL data model.
 *
 */
public class UnitExponentiation extends CompoundUnit{
	
	/*private URI base;
	private int exponent=Integer.MIN_VALUE;*/
	
	
	protected  UnitExponentiation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitExponentiation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Creates a new unit exponentiation.
	 * @param sbolDocument A valid SBOL document.
	 * @param uri The URI associated with this operation.
	 * @param namespace The namespace of this operation.
	 * @return A newly constructed UnitExponentiation object.
	 * @throws SBOLGraphException
	 */
	public static UnitExponentiation create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		UnitExponentiation identified = new UnitExponentiation(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
	/**
	 * Gets the base associated with this operation.
	 * @return A unit representing the base of the exponentiation operation.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{UNITEXPONENTIATION_BASE_NOT_NULL}")	
	public Unit getBase() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitExponentiation.base);
		return contsructIdentified(MeasureDataModel.UnitExponentiation.base, Unit.getSubClassTypes());	
	}
	
	/**
	 * Sets the base of this operation.
	 * @param base The unit value to be applied.
	 * @throws SBOLGraphException
	 */
	public void setBase(@NotNull(message = "{UNITEXPONENTIATION_BASE_NOT_NULL}") Unit base) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setBase", new Object[] {base}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitExponentiation.base, SBOLUtil.toURI(base));
	}
	
	/**
	 * Gets the exponent associated with this operation.
	 * @return An updated UnitExponentiation object with the exponent applied.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}")	
	public Optional<@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Integer> getExponent() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalInteger(this.resource, MeasureDataModel.UnitExponentiation.exponent);
	}
	
	/**
	 * Sets the exponent of this operation.
	 * @param exponent The exponent to be applied.
	 * @throws SBOLGraphException
	 */
	public void setExponent(@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Optional<@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Integer> exponent) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setExponent", new Object[] {exponent}, Optional.class);
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.UnitExponentiation.exponent, exponent);
	}
	
	/**
	 * Gets the corresponding resource type of this unit exponentiation operation.
	 * @return The associated URI.
	 */
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitExponentiation.uri;
	}
	
	/**
	 * Gets all the validation messages associated with this unit exponentiation operation.
	 * @return A list of corresponding validation messages.
	 */
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitExponentiation.base, this.resource, getBase(), validationMessages);
		return validationMessages;
	}
	
}