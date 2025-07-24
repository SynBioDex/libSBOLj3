package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

/**
 * 
 * Represents a binary prefix in the SBOL data model.
 *
 */
public class BinaryPrefix extends Prefix{
	
	protected  BinaryPrefix(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  BinaryPrefix(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Gets the URI of the resource type.
	 * @return The corresponding URI.
	 */
	@Override
	public URI getResourceType() {
		return MeasureDataModel.BinaryPrefix.uri;
	}
	
	/**
	 * 
	 * @param sbolDocument A valid SBOL document.
	 * @param uri The URI for the component.
	 * @param namespace The compnent's namespace.
	 * @return The completed binary prefix.
	 * @throws SBOLGraphException
	 */
	public static BinaryPrefix create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		BinaryPrefix prefix = new BinaryPrefix(sbolDocument.getRDFModel(), uri);
		prefix.setNamespace(namespace);
		return prefix;
	}
	
}