package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;

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
	
	public URI getEntity() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Usage.entity);
	}
	
	public void setEntity(URI entity) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Usage.entity, entity);
	}
	
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Usage.role);
	}
	
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Usage.role, roles);
	}
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Usage.uri;
	}
	
}