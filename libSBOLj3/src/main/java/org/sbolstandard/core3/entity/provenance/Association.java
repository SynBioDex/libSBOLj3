package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * Represents an association in the SBOL data model.
 *
 */
public class Association extends ControlledIdentified{
	
	/*private List<URI> roles=null;
	private URI plan=null;
	private URI agent=null;*/
	protected  Association(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Association(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Gets the roles associated with this association.
	 * @return A list object containing the roles associated with this association.
	 */
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Association.role);
	}
	
	/**
	 * Sets the roles associated with this association.
	 * @param roles A list object containing the roles associated with this association.
	 */
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.role, roles);
	}
	
	/**
	 * Adds a role to the roles associated with this association.
	 * @param role The role to be appended.
	 */
	public void addRole(URI role) {
		RDFUtil.addProperty(resource, ProvenanceDataModel.Association.role, role);
	}
	
	/**
	 * Gets the plan associated with the association.
	 * @return An object of the plan associated with the association.
	 * @throws SBOLGraphException
	 */
	public Plan getPlan() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Association.plan);
		return contsructIdentified(ProvenanceDataModel.Association.plan, Plan.class, ProvenanceDataModel.Plan.uri);
	}
	
	/**
	 * Sets the plan associated with the association.
	 * @param plan The plan associated with the association.
	 */
	public void setPlan(Plan plan) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.plan, SBOLUtil.toURI(plan));
	}
	
	/**
	 * Gets the agent associated with this association.
	 * @return The agent object associated with the association.
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{ASSOCIATION_AGENT_NOT_NULL}")
	public Agent getAgent() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Association.agent);
		return contsructIdentified(ProvenanceDataModel.Association.agent, Agent.class, ProvenanceDataModel.Agent.uri);

	}
	
	/**
	 * Sets the agent associated with this association.
	 * @param agent The agent object associated with the association.
	 * @throws SBOLGraphException
	 */
	public void setAgent(@NotNull(message = "{ASSOCIATION_AGENT_NOT_NULL}") Agent agent) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setAgent", new Object[] {agent}, Agent.class);
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.agent, SBOLUtil.toURI(agent));
	}
	
	/**
	 * Gets the URI associated with this association.
	 * @return The URI associated with this association.
	 */
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Association.uri;
	}
	
	/**
	 * Gets the validation messages associated with this association.
	 * @return The validation messages associated with this association.
	 * @throws SBOLGraphException
	 */
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, ProvenanceDataModel.Association.agent, this.resource, getAgent(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, ProvenanceDataModel.Association.plan, this.resource, getPlan(), validationMessages);
		return validationMessages;
	}
}