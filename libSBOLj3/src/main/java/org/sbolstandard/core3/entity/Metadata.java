package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;

/**
 * This class represents the metadata for a given entity.
 */
public class Metadata extends Identified{
	
	
	protected  Metadata(org.apache.jena.rdf.model.Model model,URI uri) throws SBOLGraphException
	{
		//super(model, uri, dataType);
		super(model, uri);
	}
	
	protected  Metadata(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/*protected Metadata(SBOLDocument doc, URI uri, URI dataType) throws SBOLGraphException
	{
		this(doc, uri,dataType,false);
		if (dataType==null)
		{
			throw new SBOLGraphException("Application specific types MUST have a datatype property specified. " + "Metadata URI:" + uri);
		}	
	}
	
	protected Metadata(SBOLDocument doc, URI uri, URI dataType, boolean isTopLevel) throws SBOLGraphException
	{
		super(doc.model,uri,dataType);
		if (isTopLevel)
		{
			//TODO GMGM
			//doc.addTopLevelResourceType(dataType);
			RDFUtil.addType(resource, DataModel.TopLevel.uri);
		}
		else
		{
			RDFUtil.addType(resource, DataModel.Identified.uri);
		}
	}*/
	
	/**
	 * Gets the resource type for the metadata.
	 * @return A URI object representing the corresponding resource.
	 */
	@Override
	public URI getResourceType() {
		return DataModel.Identified.uri;
	}
	
	
}