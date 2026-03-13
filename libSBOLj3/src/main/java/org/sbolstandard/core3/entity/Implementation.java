package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.Valid;

public class Implementation extends TopLevel{
	//private URI component=null;
	
	protected  Implementation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Implementation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public Component getComponent() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Implementation.built);
		return contsructIdentified(DataModel.Implementation.built, Component.class, DataModel.Component.uri);
	}
	
	@Valid
	public URI getComponentURI() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Implementation.built);
		//return contsructIdentified(DataModel.Implementation.built, Component.class, DataModel.Component.uri);
	}

	public void setComponent(Component component) {
		RDFUtil.setProperty(resource, DataModel.Implementation.built, SBOLUtil.toURI(component));
	}
	
	public void setComponent(URI componentURI) {
		RDFUtil.setProperty(resource, DataModel.Implementation.built, componentURI);
	}

	@Override
	public URI getResourceType() {
		return DataModel.Implementation.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Implementation.built, this.resource, this.getComponent(), validationMessages, this.getComponentURI());
		return validationMessages;
	}

	@Override
	public Map<URI, List<? extends Identified>> getReferencedChildEntitiesWithEdgeURIs() throws SBOLGraphException {
		Map<URI, List<? extends Identified>> identifieds = super.getReferencedChildEntitiesWithEdgeURIs();
		identifieds = addToMap(identifieds, DataModel.Implementation.built, Arrays.asList(getComponent()));
		return identifieds;
	}

	@Override
	public Map<URI, List<? extends Identified>> getReferencedTopLevelsWithEdgeURIs() throws SBOLGraphException {
		Map<URI, List<? extends Identified>> identifieds=super.getReferencedTopLevelsWithEdgeURIs();
		identifieds = addToMap(identifieds, DataModel.Implementation.built, Arrays.asList(getComponent()));
		return identifieds;
	}

	@Override
	public Map<URI, List<URI>> getReferencedTopLevelURIsWithEdgeURIs() throws SBOLGraphException{
		Map<URI, List<URI>> identifieds=super.getReferencedTopLevelURIsWithEdgeURIs();			
		identifieds = addToURIMap(identifieds, DataModel.Implementation.built, Arrays.asList(getComponentURI()));
		return identifieds;
	}

}