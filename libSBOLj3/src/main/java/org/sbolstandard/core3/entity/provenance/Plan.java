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
 * Represents a plan in the SBOL data model.
 *
 */
public class Plan extends ControlledTopLevel{
	
	protected  Plan(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Plan(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Gets the URI associated with this plan.
	 * @return The URI associated with this plan.
	 */
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Plan.uri;
	}
	
	/**
	 * Creates a new plan with the supplied parameters.
	 * @param doc An SBOL document object associated with this plan.
	 * @param uri The URI associated with this plan.
	 * @param namespace The namespace of the associated plan.
	 * @return The constructed plan object.
	 * @throws SBOLGraphException
	 */
	public static Plan create(SBOLDocument doc, URI uri, URI namespace) throws SBOLGraphException
	{
		Plan plan = new Plan(doc.getRDFModel(), uri);
		plan.setNamespace(namespace);
		return plan;		
	}
	
}