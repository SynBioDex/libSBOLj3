package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledTopLevel;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;

/**
 * 
 * Represents an agent in the SBOL data model.
 *
 */
public class Agent extends ControlledTopLevel{
	
	protected  Agent(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Agent(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Gets the URI associated with this agent.
	 * @return The URI associated with this agent.
	 */
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Agent.uri;
	}
	
	/**
	 * Creates a new agent object.
	 * @param sbolDocument The SBOL document associated with the agent.
	 * @param uri The URI associated with the agent.
	 * @param namespace The namespace of the associated agent.
	 * @return The newly created agent object.
	 * @throws SBOLGraphException
	 */
	public static Agent create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		Agent identified = new Agent(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
}