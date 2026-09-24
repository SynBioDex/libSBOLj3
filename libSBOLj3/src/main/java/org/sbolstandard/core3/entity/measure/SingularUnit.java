package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.Valid;

/**
 * 
 * Represents a singular unit in the SBOL data model.
 *
 */
public class SingularUnit extends Unit{
	
	//private float factor=Float.NaN;
	//private URI unit;
	
	protected  SingularUnit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SingularUnit(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Creates a new SingularUnit object.
	 * @param sbolDocument A valid SBOL document.
	 * @param uri The URI to be applied.
	 * @param namespace The namespace to be applied.
	 * @return A new SingularUnit object.
	 * @throws SBOLGraphException
	 */
	public static SingularUnit create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		SingularUnit identified = new SingularUnit(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
	/**
	 * Sets the factor of the prefixed unit.
	 * @param factor The factor to be applied.
	 * @throws SBOLGraphException
	 */
	public void setFactor(Optional<Float>factor) throws SBOLGraphException {
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.SingularUnit.factor, factor);
	}
	
	/**
	 * Gets the factor of the prefixed unit.
	 * @return The associated factor.
	 * @throws SBOLGraphException
	 */
	public Optional<Float> getFactor() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalFloat(this.resource, MeasureDataModel.SingularUnit.factor);
	}
	
	/**
	 * Gets the unit of the prefixed unit.
	 * @return The associated unit.
	 * @throws SBOLGraphException
	 */
	public Unit getUnit() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.SingularUnit.unit);	
		return contsructIdentified(MeasureDataModel.SingularUnit.unit, Unit.getSubClassTypes());
	}
	
	/**
	 * Gets the unit of this entity.
	 * @return The associated unit URI.
	 * @throws SBOLGraphException
	 */
	@Valid
	public URI getUnitURI() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.SingularUnit.unit);	
		//return contsructIdentified(MeasureDataModel.SingularUnit.unit, Unit.getSubClassTypes());
	}
	
	/**
	 * Sets the unit of the prefixed unit.
	 * @param unit The unit to be applied.
	 * @throws SBOLGraphException
	 */
	public void setUnit(Unit unit) {
		URI unitURI=null;
		if (unit != null) {
			unitURI = unit.getUri();
		}
		RDFUtil.setProperty(resource, MeasureDataModel.SingularUnit.unit, unitURI);
	}
	
	/**
	 * Sets the unit of the prefixed unit.
	 * @param unit The unit to be applied.
	 * @throws SBOLGraphException
	 */
	public void setUnitURI(URI unit) {
		RDFUtil.setProperty(resource, MeasureDataModel.SingularUnit.unit, unit);
	}
	
	/**
	 * Gets the associated URI of the prefixed unit.
	 * @return The corresponding URI.
	 */
	@Override
	public URI getResourceType() {
		return MeasureDataModel.SingularUnit.uri;
	}
	
	/**
	 * Gets the validation messages associated with the prefixed unit.
	 * @return The corresponding validation messages.
	 */
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.SingularUnit.unit, this.resource, getUnit(), validationMessages, this.getUnitURI());
		return validationMessages;
	}

	@Override
	public Map<URI, List<? extends Identified>> getReferencedTopLevelsWithEdgeURIs() throws SBOLGraphException {
		Map<URI, List<? extends Identified>> identifieds=super.getReferencedTopLevelsWithEdgeURIs();
		identifieds = addToMap(identifieds, MeasureDataModel.SingularUnit.unit, Arrays.asList(getUnit()));
		return identifieds;
	}
	
	@Override
	public Map<URI, List<URI>> getReferencedTopLevelURIsWithEdgeURIs() throws SBOLGraphException{
		Map<URI, List<URI>> identifieds=super.getReferencedTopLevelURIsWithEdgeURIs();			
		identifieds = addToURIMap(identifieds, MeasureDataModel.SingularUnit.unit, Arrays.asList(getUnitURI()));
		return identifieds;
	}
	
}