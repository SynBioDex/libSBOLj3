package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * Represents a usage in the SBOL data model.
 *
 */
public class Usage extends ControlledIdentified{
	/*private URI entity=null;
	private List<URI> roles=null;*/
		
	protected  Usage(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Usage(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Gets the entity associated with this usage.
	 * @return An object representing the entity associated with this usage.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{USAGE_ENTITY_NOT_NULL}")
	public URI getEntity() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Usage.entity);
	}
	
	/**
	 * Set the entity associated with this usage.
	 * @param entity The entity associated with this usage.
	 * @throws SBOLGraphException
	 */
	public void setEntity(@NotNull(message = "{USAGE_ENTITY_NOT_NULL}") URI entity) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setEntity", new Object[] {entity}, URI.class);
		RDFUtil.setProperty(resource, ProvenanceDataModel.Usage.entity, entity);
	}
	
	/**
	 * Gets the roles associated with this usage.
	 * @return A list object containing the roles associated with this usage.
	 */
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Usage.role);
	}
	
	/**
	 * Sets the roles associated with this usage.
	 * @param roles A list object containing the roles associated with this usage.
	 */
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Usage.role, roles);
	}
	
	/**
	 * Adds a role to this usage.
	 * @param role The role to be added to this usage.
	 */
	public void addRole(URI role) {
		RDFUtil.addProperty(resource, ProvenanceDataModel.Usage.role, role);
	}
	
	/**
	 * Gets the URI of the associated with this usage.
	 * @return A URI object associated with this usage.
	 */
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Usage.uri;
	}			
	
}