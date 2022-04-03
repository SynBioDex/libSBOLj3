package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

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
	
	public URI getPrefix() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.prefix);	
	}

	public void setPrefix(URI prefix) {
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.prefix, prefix);
	}
	
	
	public URI getUnit() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.unit);
	}

	public void setUnit(URI unit) {
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.unit, unit);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.PrefixedUnit.uri;
	}
}