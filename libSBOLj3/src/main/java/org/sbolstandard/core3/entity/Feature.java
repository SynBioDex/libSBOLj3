package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Orientation;
import jakarta.validation.constraints.NotNull;

public abstract class Feature extends Identified{
	/*private List<URI> roles=null;
	private Orientation orientation=null;*/
	
	protected  Feature(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Feature(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	/**
	 * Get the roles for the feature.
	 * @return A list of roles for the corresponding feature.
	 */
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
	}
	
	/**
	 * Set the roles for this feature.
	 * @param roles A list of resources identifying the roles to be applied.
	 */
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	/**
	 * Appends a role to this feature.
	 * @param role The role to be appended.
	 */
	public void addRole(URI role) {
		RDFUtil.addProperty(resource, DataModel.role, role);
	}
	
	/**
	 * Gets the orientation of the feature.
	 * @return An object representing the orientation of the feature.
	 * @throws SBOLGraphException
	 */
	public Orientation getOrientation() throws SBOLGraphException {
		Orientation orientation=null;
		
		URI value=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.orientation);
		if (value!=null){
			
			orientation=toOrientation(value); 	
			PropertyValidator.getValidator().validateReturnValue(this, "toOrientation", orientation, URI.class);
		}
		return orientation;
	}
	
	/**
	 * Extrapolates the orientation from a resource identifier.
	 * @param uri The resource identifier to be processed.
	 * @return An object with the orientation values.
	 */
	@NotNull(message = "{FEATURE_ORIENTATION_VALID_IF_NOT_NULL}")   
	public Orientation toOrientation (URI uri)
	{
		return Orientation.get(uri); 
	}
	
	/**
	 * Apply an orientation to the feature.
	 * @param orientation The orientation to be applied.
	 */
	public void setOrientation(Orientation orientation) {
		URI orientationURI=null;
		if (orientation!=null)
		{
			orientationURI=orientation.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.orientation, orientationURI);
	}
	
	/**
	 * Gets the types for the corresponding subclasses of the feature.
	 * @param <T> A list containing the resources and class references for the subclasses.
	 * @return A hashmap containing the identifiers and class references for the subclasses.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Identified> HashMap<URI, Class<T>> getSubClassTypes()
	{
		HashMap<URI, Class<T>> subclasses=new HashMap<URI, Class<T>>();
		subclasses.put(DataModel.ComponentReference.uri, (Class<T>) ComponentReference.class);
		subclasses.put(DataModel.SubComponent.uri,  (Class<T>)SubComponent.class);
		subclasses.put(DataModel.LocalSubComponent.uri,(Class<T>)  LocalSubComponent.class);
		subclasses.put(DataModel.ExternalyDefined.uri, (Class<T>)  ExternallyDefined.class);
		subclasses.put(DataModel.SequenceFeature.uri,(Class<T>)  SequenceFeature.class);
		return subclasses;
	}
	
	/*public static HashMap<URI, Class> getSubClassTypes()
	{
		HashMap<URI, Class> subclasses=new HashMap<URI, Class>();
		subclasses.put(DataModel.ComponentReference.uri,  ComponentReference.class);
		subclasses.put(DataModel.SubComponent.uri,  SubComponent.class);
		subclasses.put(DataModel.LocalSubComponent.uri,  LocalSubComponent.class);
		subclasses.put(DataModel.ExternalyDefined.uri,  ExternallyDefined.class);
		subclasses.put(DataModel.SequenceFeature.uri,  SequenceFeature.class);
		return subclasses;
	}*/
}