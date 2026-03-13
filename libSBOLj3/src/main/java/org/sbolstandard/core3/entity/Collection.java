package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Collection extends TopLevel{
	//private List<URI> members=null;
	
	protected  Collection(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Collection(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	

	public List<URI> getMembers() throws SBOLGraphException {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Collection.member);	
	}

	public List<TopLevel> getMemberTopLevels() throws SBOLGraphException {
		Map<Class<? extends TopLevel>, URI> topLevelClassURITypeMappings = SBOLUtil.getTopLevelClassURITypeMappings();
		Map<URI, List<? extends TopLevel>> memberTopLevels = this.getConnectedEntitiesWithThePropertyHavingType(topLevelClassURITypeMappings, DataModel.Collection.member);		
		List<TopLevel> topLevels = new ArrayList<>();
		memberTopLevels.values().forEach(topLevels::addAll);
		return topLevels;
	}

	public void setMembers(List<URI> members) {
		RDFUtil.setProperty(resource, DataModel.Collection.member, members);
	}
	
	public void addMember(TopLevel member) {
		RDFUtil.addProperty(resource, DataModel.Collection.member, member.getUri());
	}
	
	
	public void addMembers(List<TopLevel> members) {
		RDFUtil.addProperty(resource, DataModel.Collection.member, SBOLUtil.getURIs(members));
	}
	
	public void addMember(URI member) {
		RDFUtil.addProperty(resource, DataModel.Collection.member, member);
	}
	
	
	public void addUriMembers(List<URI> members) {
		RDFUtil.addProperty(resource, DataModel.Collection.member, members);
	}
	
	
	/*
	public List<TopLevel> getMembers() throws SBOLGraphException {
		List<URI> members = RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Collection.member);	
		
	}

	public void setMembers(List<URI> members) {
		RDFUtil.setProperty(resource, DataModel.Collection.member, members);
	}	
	 */
	
	@Override
	public Map<URI, List<? extends Identified>> getReferencedTopLevelsWithEdgeURIs() throws SBOLGraphException {
		Map<URI, List<? extends Identified>> identifieds=super.getReferencedTopLevelsWithEdgeURIs();
		identifieds = addToMap(identifieds, DataModel.Collection.member, this.getMemberTopLevels());
		//identifieds.putAll(getConnectedEntitiesHavingTypeProperty(TopLevel.class, DataModel.Collection.member));
		return identifieds;
	}

	@Override
	public Map<URI, List<URI>> getReferencedTopLevelURIsWithEdgeURIs() throws SBOLGraphException{
		Map<URI, List<URI>> identifieds=super.getReferencedTopLevelURIsWithEdgeURIs();			
		identifieds = addToURIMap(identifieds, DataModel.Collection.member, this.getMembers());
		return identifieds;
	}

	@Override
	public URI getResourceType() {
		return DataModel.Collection.uri;
	}
	

	
}