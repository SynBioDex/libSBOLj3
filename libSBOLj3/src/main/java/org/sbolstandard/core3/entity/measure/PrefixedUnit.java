package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * Represents a prefixed unit in the SBOL data model.
 *
 */
public class PrefixedUnit extends Unit{
	
	/*private URI prefix;
	private URI unit;*/
	
	protected  PrefixedUnit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  PrefixedUnit(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Creates a new prefixed unit.
	 * @param sbolDocument A valid SBOL document.
	 * @param uri The URI of the prefixed unit.
	 * @param namespace The namespace of the unit.
	 * @return A new prefixed unit object.
	 * @throws SBOLGraphException
	 */
	public static PrefixedUnit create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		PrefixedUnit identified = new PrefixedUnit(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
	/**
	 * Gets the prefix for the prefixed unit.
	 * @return The corresponding prefix.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{PREFIXEDUNIT_PREFIX_NOT_NULL}")	
	public Prefix getPrefix() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.prefix);	
		return contsructIdentified(MeasureDataModel.PrefixedUnit.prefix, Prefix.getSubClassTypes());	
	}

	/**
	 * Sets the prefix for the prefixed unit.
	 * @param prefix The prefix to be applied.
	 * @throws SBOLGraphException
	 */
	public void setPrefix(@NotNull(message = "{PREFIXEDUNIT_PREFIX_NOT_NULL}") Prefix prefix) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setPrefix", new Object[] {prefix}, Prefix.class);
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.prefix, SBOLUtil.toURI(prefix));
	}
	
	/**
	 * Get the unit of the prefixed unit.
	 * @return The corresponding unit.
	 * @throws SBOLGraphException
	 */
	//@NotNull(message = "{PREFIXEDUNIT_UNIT_NOT_NULL}")	
	public Unit getUnit() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.unit);
		return contsructIdentified(MeasureDataModel.PrefixedUnit.unit, Unit.getSubClassTypes());	
	}

	/**
	 * Get the encoding type for the sequence.
	 * @return An object with the corresponding encoding type.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{PREFIXEDUNIT_UNIT_NOT_NULL}")	
	public URI getUnitURI() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.unit);		
	}
	
	/**
	 * Set the unit of the prefixed unit.
	 * @param unit The unit to be applied.
	 * @throws SBOLGraphException
	 */
	public void setUnit(@NotNull(message = "{PREFIXEDUNIT_UNIT_NOT_NULL}") Unit unit) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setUnit", new Object[] {unit}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.unit, SBOLUtil.toURI(unit));
	}

	/**
	 * Sets the unit of the prefixed unit.
	 * @param unit The unit to be applied.
	 * @throws SBOLGraphException
	 */
	public void setUnitURI(@NotNull(message = "{PREFIXEDUNIT_UNIT_NOT_NULL}") URI unit) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setUnit", new Object[] {unit}, URI.class);
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.unit, unit);
	}
	
	/**
	 * Get the resource type of the prefixed unit.
	 * @return The corresponding URI.
	 */
	@Override
	public URI getResourceType() {
		return MeasureDataModel.PrefixedUnit.uri;
	}
	
	/**
	 * Gets the validation messages associated with this prefixed unit.
	 * @return The corresponding validation messages.
	 */
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.PrefixedUnit.unit, this.resource, getUnit(), validationMessages, this.getUnitURI());
		return validationMessages;
	}

	@Override
	public Map<URI, List<? extends Identified>> getReferencedTopLevelsWithEdgeURIs() throws SBOLGraphException {
		Map<URI, List<? extends Identified>> identifieds=super.getReferencedTopLevelsWithEdgeURIs();
		identifieds = addToMap(identifieds, MeasureDataModel.PrefixedUnit.unit, Arrays.asList(getUnit()));
		identifieds = addToMap(identifieds, MeasureDataModel.PrefixedUnit.prefix, Arrays.asList(getPrefix()));		
		return identifieds;
	}
	
	@Override
	public Map<URI, List<URI>> getReferencedTopLevelURIsWithEdgeURIs() throws SBOLGraphException{
		Map<URI, List<URI>> identifieds=super.getReferencedTopLevelURIsWithEdgeURIs();			
		identifieds = addToURIMap(identifieds, MeasureDataModel.PrefixedUnit.unit, Arrays.asList(getUnitURI()));
		identifieds = addToURIMap(identifieds, MeasureDataModel.PrefixedUnit.prefix, Arrays.asList(getPrefix().getUri()));
		return identifieds;
	}
    

}