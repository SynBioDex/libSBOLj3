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
 * Represents unit division operations in the SBOL data model.
 *
 */
public class UnitDivision extends CompoundUnit{
	/*private URI numerator;
	private URI denominator;*/
	
	protected  UnitDivision(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitDivision(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Creates a new UnitDivision object.
	 * @param sbolDocument A valid SBOL document.
	 * @param uri The URI of the operation.
	 * @param namespace The namespace of the operation.
	 * @return A newly constructed UnitDivision object.
	 * @throws SBOLGraphException
	 */
	public static UnitDivision create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		UnitDivision identified = new UnitDivision(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
	/**
	 * Gets the numerator associated with this unit division operation.
	 * @return A Unit object with the newly applied numerator.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{UNITDIVISION_NUMERATOR_NOT_NULL}")	
	public Unit getNumerator() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.numerator);	
		return contsructIdentified(MeasureDataModel.UnitDivision.numerator, Unit.getSubClassTypes());	
	}
	
	/**
	 * Sets the numerator for this unit division operation.
	 * @param numerator The numerator to be applied.
	 * @throws SBOLGraphException
	 */
	public void setNumerator(@NotNull(message = "{UNITDIVISION_NUMERATOR_NOT_NULL}") Unit numerator) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setNumerator", new Object[] {numerator}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.numerator, SBOLUtil.toURI(numerator));
	}
	
	/**
	 * Gets the denominator associated with this unit division operation.
	 * @return The updated operation with the denominator applied.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{UNITDIVISION_DENOMINATOR__NOT_NULL}")	
	public Unit getDenominator() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.denominator);	
		return contsructIdentified(MeasureDataModel.UnitDivision.denominator, Unit.getSubClassTypes());	
	}
	
	/**
	 * Sets the denominator of the unit division operation.
	 * @param denominator The denominator to be used in this operation.
	 * @throws SBOLGraphException
	 */
	public void setDenominator(@NotNull(message = "{UNITDIVISION_DENOMINATOR__NOT_NULL}") Unit denominator) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setDenominator", new Object[] {denominator}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.denominator, SBOLUtil.toURI(denominator));
	}
	
	/**
	 * Gets the resource type associated with this unit division operation.
	 * @return The corresponding URI.
	 */
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitDivision.uri;
	}	
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitDivision.denominator, this.resource, getDenominator(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitDivision.numerator, this.resource, getNumerator(), validationMessages);
		return validationMessages;
	}
}