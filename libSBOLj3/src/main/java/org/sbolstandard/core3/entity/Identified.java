package org.sbolstandard.core3.entity;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.measure.Measure;
import org.sbolstandard.core3.entity.measure.Unit;
import org.sbolstandard.core3.entity.provenance.*;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidSBOLEntity;
import org.sbolstandard.core3.validation.ValidatableSBOLEntity;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ActivityType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;

@ValidSBOLEntity
public abstract class Identified implements ValidatableSBOLEntity {
	protected Resource resource=null;
	
	/*private String displayId;
	private String name;
	private String description;
	private List<URI> wasDerivedFrom;
	private List<URI> wasGeneratedBy;
	private List<Measure> measures;
	//private List<Metadata> metadataList;
	private URI uri;*/
	
	protected Identified()
	{}
	
	protected Identified(Model model, URI uri) throws SBOLGraphException
	{
		//this.uri=uri;
		this.resource=RDFUtil.createResource(model, uri,this.getResourceType());
		inferDisplayId(uri);
	}
	
	/*protected Identified(Model model, URI uri, URI resourceType) throws SBOLGraphException
	{
		this.uri=uri;
		this.resource=RDFUtil.createResource(model, this.uri,resourceType);
		inferDisplayId(uri);
	}*/
	
	protected Identified(Resource resource) throws SBOLGraphException
	{
		//this.uri=URI.create(resource.getURI());
		this.resource=resource;
		inferDisplayId(URI.create(resource.getURI()));
	}
			
	//@Pattern(regexp = "^[a-zA-Z_]+[a-zA-Z0-9_]*$", message = "{IDENTIFIED_DISPLAYID}")	
	//@Pattern(regexp = "^[\\p{L}_]+[\\p{L}0-9_]*$", message = "{IDENTIFIED_DISPLAYID}")
	public String getDisplayId() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Identified.displayId);
	}
	
	/*@Valid
	public String getDisplayId() throws SBOLGraphException{
		String displayId=IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Identified.displayId);
		boolean valid=isValidDisplayId(displayId); 
		PropertyValidator.getValidator().validateReturnValue(this, "isValidDisplayId", valid, String.class);
		return displayId;		
	}*/
	
		
	
	public void setDisplayId(String displayId) throws SBOLGraphException {
		boolean valid=isValidDisplayId(displayId); 
		if (Configuration.getInstance().isValidateAfterSettingProperties()){
			PropertyValidator.getValidator().validateReturnValue(this, "isValidDisplayId", valid, String.class);
		}			
		RDFUtil.setProperty(resource, DataModel.Identified.displayId, displayId);			
	}
	
	@AssertTrue(message = "{IDENTIFIED_DISPLAYID}")   	
	public boolean isValidDisplayId(String displayId)
	{
		/*String pattern="^[\\p{L}_]+[\\p{L}0-9_]*$";
		//String pattern="[\\p{L}]*";		
		//String pattern="[\\p{Alpha}]*";
		boolean valid=true;
		if (displayId!=null) {
			valid=displayId.matches(pattern);
		}*/
		boolean valid=true;		
		if (displayId!=null)
		{
			Matcher matcher=Configuration.getInstance().getDisplayIdPattern().matcher(displayId);
			valid=matcher.find();
		}
		return valid;		
	}
	
	public String getName() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Identified.name);
	}
	
	public void setName(String name) throws SBOLGraphException {
		RDFUtil.setProperty(resource, DataModel.Identified.name, name);	
		
	}
	
	public String getDescription() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Identified.description);
	}
	
	public void setDescription(String description) throws SBOLGraphException {
		RDFUtil.setProperty(resource, DataModel.Identified.description, description);
	}
	
	
	public List<URI> getWasDerivedFrom() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Identified.wasDerivedFrom);
	}
	
	public void setWasDerivedFrom(@Valid List<URI> wasDerivedFrom) {
		RDFUtil.setProperty(resource, DataModel.Identified.wasDerivedFrom, wasDerivedFrom);
	}
	
	public void addWasDerivedFrom(@Valid URI wasDerivedFrom) {
		RDFUtil.addProperty(resource, DataModel.Identified.wasDerivedFrom, wasDerivedFrom);
	}
	
	public void addWasDerivedFrom(Identified wasDerivedFrom) {
		if (wasDerivedFrom!=null){
			addWasDerivedFrom(wasDerivedFrom.getUri());
		}
	}
	
	public List<Activity> getWasGeneratedBy() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Identified.wasGeneratedBy);
		return addToList(DataModel.Identified.wasGeneratedBy, Activity.class, ProvenanceDataModel.Activity.uri);
	}
	
	public void setWasGeneratedBy(List<Activity> wasGeneratedBy) {
		RDFUtil.setProperty(resource, DataModel.Identified.wasGeneratedBy, SBOLUtil.getURIs(wasGeneratedBy));
	}
	
	public void addWasGeneratedBy(Activity wasGeneratedBy) {
		if (wasGeneratedBy!=null)
		{
			RDFUtil.addProperty(resource, DataModel.Identified.wasGeneratedBy, wasGeneratedBy.getUri());
		}
	}
	
	@Valid
	public List<Measure> getMeasures()throws SBOLGraphException  {
		return addToList(DataModel.Identified.measure, Measure.class, MeasureDataModel.Measure.uri);
	}
	
	public Measure createMeasure(URI uri, float value, Unit unit) throws SBOLGraphException
	{
		Measure measure = new Measure(this.resource.getModel(), uri) {};
		measure.setValue(Optional.of(value));
		measure.setUnit(unit);		
		addToList(measure, DataModel.Identified.measure);
		return measure;	
	}
	
	public Measure createMeasure(String displayId, float value, Unit unit) throws SBOLGraphException
	{
		return createMeasure(SBOLAPI.append(this.getUri(), displayId), value, unit);
	}
	
	/*public List<Metadata> getMetadata(URI property)throws SBOLGraphException  {
		this.metadataList=addToList(this.metadataList, property, Metadata.class, DataModel.Identified.uri);
		return metadataList;
	}*/
	
	public Metadata createMetadata(URI uri, URI dataType, URI property) throws SBOLGraphException
	{
		if (dataType==null)
		{
			throw new SBOLGraphException("Application specific types MUST have a datatype property specified. " + "Metadata URI:" + uri);
		}
		Metadata metadata=new Metadata(this.resource.getModel(), uri);
		metadata.addAnnotationType(dataType);
		this.addAnnotion(property, metadata);
		return metadata;
	}
	
	public Metadata createMetadata(String displayId, URI dataType, URI property) throws SBOLGraphException
	{
		return createMetadata(SBOLAPI.append(this.getUri(), displayId), dataType, property);
	}
	
	public URI getUri() {
		return URI.create(this.resource.getURI());
	}
	
	abstract public URI getResourceType();
	public List<Identified> getChildren() throws SBOLGraphException
	{
		List<Identified> identifieds=null;
		identifieds=addToList(identifieds, this.getMeasures());
		return identifieds;
	}
	

	public List<ValidationMessage> addToValidations(List<ValidationMessage> messages,ValidationMessage message)
	{
		return IdentifiedValidator.addToValidations(messages, message);
	}
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=null;
		List<URI> wasDerivedFroms=this.getWasDerivedFrom();		
		
		if (!isValidDisplayId(this.getDisplayId())){
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{IDENTIFIED_DISPLAYID}", DataModel.Identified.displayId));      	 	   
		}
		
    	if (wasDerivedFroms!=null && wasDerivedFroms.contains(this.getUri()))
    	{
    		validationMessages= addToValidations(validationMessages,new ValidationMessage("{IDENTIFIED_CANNOT_BE_REFERREDBY_WASDERIVEDFROM}", DataModel.Identified.wasDerivedFrom));      
    	}
    	
    	List<Identified> children=this.getChildren();
    	if (children!=null)
    	{
    		validationMessages=IdentifiedValidator.assertURIStartsWith(this, validationMessages, children);
    	}
    	
    	validationMessages=IdentifiedValidator.getValidator().assertOneSBOLEntityType(this, this.resource, validationMessages);
    	validationMessages= IdentifiedValidator.assertExists(this, DataModel.Identified.wasGeneratedBy, this.resource, this.getWasGeneratedBy(), validationMessages);
    	validationMessages= IdentifiedValidator.assertExists(this, DataModel.Identified.measure, this.resource, this.getMeasures(), validationMessages);
        
    	if (Configuration.getInstance().isValidateRecommendedRules())
    	{
    		validationMessages=checkActivityTypes(validationMessages);
    	}
    	
    	return validationMessages;
	}
	
	private List<ValidationMessage> checkActivityTypes(List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		List<Activity> activities=this.getWasGeneratedBy();
		if (activities!=null){
			for (Activity activity:activities){
				List<Association> associations=activity.getAssociations();
				if (associations!=null){
					for (Association association:associations){
						List<URI> roles=association.getRoles();
						if (roles!=null){
							for (URI role:roles){
								ActivityType activityType= ActivityType.get(role);
								if (activityType!=null){	
									boolean validReferredType=false;
									if (activityType.getUri().equals(ActivityType.Design.getUri())){
										if (!(this instanceof Implementation) && (this instanceof TopLevel)){
											validReferredType=true;
										}
									}
									else if (activityType.getUri().equals(ActivityType.Build.getUri())){
										if (this instanceof Implementation){
											validReferredType=true;
										}
									}
									else if (activityType.getUri().equals(ActivityType.Test.getUri())){
										if (this instanceof ExperimentalData){
											validReferredType=true;
										}
									}
									else if (activityType.getUri().equals(ActivityType.Learn.getUri())){
										if (!(this instanceof Implementation)){
											validReferredType=true;
										}
									}
									
									if (!validReferredType){
										ValidationMessage message=new ValidationMessage("{IDENTIFIED_ACTIVITY_TYPE_MATCHES_WITH_ASSOCIATION_ROLE}", DataModel.Identified.wasDerivedFrom, activity, role);      
										message.childPath(ProvenanceDataModel.Activity.qualifiedAssociation, association);
										validationMessages= addToValidations(validationMessages,message);									
									}
								}
								
							}
						}
						
					}
				}	
			}
		}
		return validationMessages;
	}
	
	protected <T extends Identified> Identified createIdentified(Resource res, Class<T> identified) throws SBOLGraphException
	{
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			if (!constructor.isAccessible())
			{
				constructor.setAccessible(true);
			}
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}
	}
	
	
	/*protected <T extends Identified>  List<T> addToList(List<T> items, URI property, Class<T> identifiedClass) throws SBOLGraphException
	{
		if (items==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
			if (resources!=null && resources.size()>0)
			{
				items=new ArrayList<T>();
				for (Resource res:resources)
				{
					Identified identified=createIdentified(res, identifiedClass);
					items.add((T)identified);
				}
			}
			
		}
		return items;
	}*/
	
	
	/*protected <T extends Identified>  T contsructIdentified2(URI property, Class<T> identifiedClass) throws SBOLGraphException
	{
		T identified=null;
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
		if (resources!=null)
		{
			if (resources.size()==1)
			{
				 identified=(T)createIdentified(resources.get(0), identifiedClass);
			}
			else
			{
				String message=String.format("Multiple property values exist for the property %s. The entity URI:%s", property.toString(),this.resource.getURI());
				throw new SBOLGraphException(message);
			}
		}		
		return identified;
	}*/
	
	protected <T extends Identified>  T contsructIdentified(URI property, Class<T> identifiedClass, URI identifiedResourceType ) throws SBOLGraphException
	{
		T identified=null;
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property,identifiedResourceType);
		if (resources!=null)
		{
			if (resources.size()==1)
			{
				 identified=(T)createIdentified(resources.get(0), identifiedClass);
			}
			else
			{
				String message=String.format("Multiple property values exist for the property %s. The entity URI:%s", property.toString(),this.resource.getURI());
				throw new SBOLGraphException(message);
			}
		}		
		return identified;
	}
	
	/*protected <T extends Identified>  T contsructIdentified(URI property, HashMap<URI, Class<T>> identifiedClassOptions) throws SBOLGraphException
	{
		T identified=null;
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
		if (resources!=null)
		{
			if (resources.size()==1)
			{
				Resource targetRes=resources.get(0);
				if (identifiedClassOptions!=null) 
				{
					for (Entry<URI, Class<T>> entry: identifiedClassOptions.entrySet())
					{
						if (RDFUtil.hasType(targetRes.getModel(), targetRes, entry.getKey()))
						{
							identified=(T)createIdentified(targetRes, entry.getValue());
						}
					}
				}
			}
			else
			{
				String message=String.format("Multiple property values exist for the property %s. The entity URI:%s", property.toString(),this.resource.getURI());
				throw new SBOLGraphException(message);
			}
		}		
		return identified;
	}*/
	
	protected <T extends Identified>  T contsructIdentified(URI property, HashMap<URI, Class<T>> identifiedClassOptions) throws SBOLGraphException
	{
		T identified=null;
		int numberOfMatchingResources=0;
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
		if (resources!=null && identifiedClassOptions!=null){
			for (Resource targetRes: resources) {
				for (Entry<URI, Class<T>> entry: identifiedClassOptions.entrySet()){
					if (RDFUtil.hasType(targetRes.getModel(), targetRes, entry.getKey())){
						identified=(T)createIdentified(targetRes, entry.getValue());
						numberOfMatchingResources++;
						break;
					}
				}
			}
			if (numberOfMatchingResources>1){
				String message=String.format("Multiple property values exist for the property %s. The entity URI:%s", property.toString(),this.resource.getURI());
				throw new SBOLGraphException(message);
			}
		}		
		return identified;
	}
		
	
	/***
	 * Serialises an array of objects into RDF resources.
	 * @param items
	 * @param identified
	 * @param property
	 * @return
	 */
	protected <T extends Identified> void addToList(Identified identified, URI property)
	{
		RDFUtil.addProperty(this.resource,property, identified.getUri());
	}
	
	
	protected <T extends Identified> List<Identified> addToList(List<Identified> listA, List<T> listB)
	{
		if (listB!=null && listB.size()>0)
		{
			if (listA==null)
			{
				listA=new ArrayList<Identified>();
			}
			listA.addAll(listB);
		}
		return listA;
	}
	
	protected <T extends Identified> List<Identified> addToList(List<Identified> listA, T identified)
	{
		if (identified!=null)
		{
			if (listA==null)
			{
				listA=new ArrayList<Identified>();
			}
			listA.add(identified);
		}
		return listA;
	}
	/*protected <T extends Identified> List<T> addToList(List<T> items, Identified identified, URI property)
	{
		RDFUtil.addProperty(this.resource,property, identified.getUri());
		
		if (items==null)
		{
			items=new ArrayList<T>();
		}
		items.add((T)identified);
		return items;
	}*/
	
	
	
	/*protected <T extends Identified> List<T> addToList(List<T> items, URI property, Class<T> identifiedClass, URI identifiedResourceType) throws SBOLGraphException
	{
		if (items==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property, identifiedResourceType);
			if (resources!=null && resources.size()>0)
			{
				items=new ArrayList<T>();
				for (Resource res:resources)
				{
					Identified identified=createIdentified(res, identifiedClass);
					items.add((T)identified);
				}
			}
			
		}
		return items;
	}*/
	
	protected <T extends Identified> List<T> addToList(URI property, Class<T> identifiedClass, URI identifiedResourceType) throws SBOLGraphException
	{
		List<T> items=null;
		
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property, identifiedResourceType);
		if (resources!=null && resources.size()>0){
			items=new ArrayList<T>();
			for (Resource res:resources){
				Identified identified=createIdentified(res, identifiedClass);
				items.add((T)identified);
			}
		}
		return items;
	}
	
	protected <T extends Identified> List<T> addToList(URI property, HashMap<URI, Class<T>> identifiedClassOptions) throws SBOLGraphException
	{
		List<T> items=null;
		
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
		if (resources!=null && resources.size()>0){
			for (Resource targetRes:resources){
				if (identifiedClassOptions!=null) {
					for (Entry<URI, Class<T>> entry: identifiedClassOptions.entrySet()){
						if (RDFUtil.hasType(targetRes.getModel(), targetRes, entry.getKey())){
							if (items==null){
								items=new ArrayList<T>();
							}
							Identified identified=createIdentified(targetRes, entry.getValue());
							items.add((T)identified);
						}
					}
				}
			}
		}
		return items;
	}
	
	/*protected <T extends Identified>  T contsructIdentifiedDEL(URI property, HashMap<URI, Class> identifiedClassOptions) throws SBOLGraphException
	{
		T identified=null;
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
		if (resources!=null)
		{
			if (resources.size()==1)
			{
				Resource targetRes=resources.get(0);
				if (identifiedClassOptions!=null) 
				{
					for (Entry<URI, Class> entry: identifiedClassOptions.entrySet())
					{
						if (RDFUtil.hasType(targetRes.getModel(), targetRes, entry.getKey()))
						{
							identified=(T)createIdentified(targetRes, entry.getValue());
							break;
						}
					}
				}
			}
			else
			{
				String message=String.format("Multiple property values exist for the property %s. The entity URI:%s", property.toString(),this.resource.getURI());
				throw new SBOLGraphException(message);
			}
		}		
		return identified;
	}
	*/
	
	
	/***
	 * Deserialises an array of objects from RDF resources
	 * @param items
	 * @param property
	 * @param identifiedClass
	 * @return
	 * @throws SBOLGraphException
	 */
	/*protected <T extends Identified>  List<T> addToList2(URI property, Class<T> identifiedClass) throws SBOLGraphException
	{
		List<T> items=null;
	
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
		if (resources!=null && resources.size()>0){
			items=new ArrayList<T>();
			for (Resource res:resources){
				Identified identified=createIdentified(res, identifiedClass);
				items.add((T)identified);
			}
		}
		return items;
	}*/
	
	public void addAnnotion(URI property, String value)
	{
		RDFUtil.addProperty(resource, property, value);
	}
	
	public void addAnnotion(URI property, Integer value)
	{
		RDFUtil.addProperty(resource, property, value);
	}
	
	public void addAnnotion(URI property, Boolean value)
	{
		RDFUtil.addProperty(resource, property, value);
	}
	
	public void addAnnotion(URI property, Double value)
	{
		RDFUtil.addProperty(resource, property, value);
	}
	
	public void addAnnotion(URI property, URI value)
	{
		RDFUtil.addProperty(resource, property, value);
	}
	
	public void addAnnotion(URI property, Identified value)
	{
		RDFUtil.addProperty(resource, property, value.getUri());
	}
	
	public void addAnnotion(URI property, TopLevel value)
	{
		RDFUtil.addProperty(resource, property, value.getUri());
	}
	
	public void addAnnotationType(URI typeURI)
	{
		RDFUtil.addType(resource, typeURI);
	}
	
	/**
	 * Returns a TopLevel or a MetaData entity, if such an entity exist. Other URI and Literal values matching the property are also returned.
	 * @param propertyURI
	 * @return
	 * @throws SBOLGraphException
	 */
	public List<Object> getAnnotion(URI propertyURI) throws SBOLGraphException
	{
		ArrayList<Object> values=null;
        Property property=resource.getModel().getProperty(propertyURI.toString());
        for (StmtIterator iterator=resource.listProperties(property);iterator.hasNext();)
        {
        	if (values==null)
        	{
        		values=new ArrayList<Object>();
        	}
        	Statement stmt=iterator.next();
        	RDFNode object=stmt.getObject();
        	
        	if (object.isResource()) 
        	{
        		if (object.asResource().listProperties().hasNext()==false)
        		{
        			values.add(object.asResource().getURI());
        		}
        		else
        		{
        			Resource metadataResource=object.asResource();
        			Identified metadata=null;
        			if (RDFUtil.hasType(metadataResource.getModel(), metadataResource, DataModel.TopLevel.uri))
        			{
        				metadata=new TopLevelMetadata(metadataResource);
        			}
        			else
        			{
        				metadata=new Metadata(metadataResource);
        			}
        			values.add(metadata);
        		}
        	}
        	else
        	{
        		values.add(object.asLiteral().getValue());
        	}
        }
        return values;		
	}
	
	private boolean isAnnotationProperty(String property)
	{
		boolean value=true;
		if (property.toLowerCase().startsWith(URINameSpace.SBOL.getUri().toString().toLowerCase())){
			value= false;
		}
		else if (property.toLowerCase().startsWith(URINameSpace.PROV.getUri().toString().toLowerCase())){
			value=false;
		}
		else if (property.toLowerCase().equals(RDF.type.getURI().toLowerCase())){
			value=false;
		}				
		return value;
	}
	
	/**
	 * Extracts property-value pairs. Values can be URIs, literal values, Metadata entities or TopLevelMetadata entities.
	 * @return
	 * @throws SBOLGraphException
	 */
	public List<Pair<URI, Object>> getAnnotations() throws SBOLGraphException
	{
		List<Pair<URI, Object>> values=null;
		for (StmtIterator iterator=resource.listProperties();iterator.hasNext();){        	
        	Statement stmt=iterator.next();
        	RDFNode object=stmt.getObject();
        	String property=stmt.getPredicate().getURI();  
        	//Extract non-SBOL properties
        	if (isAnnotationProperty(property)){
        		URI propertyURI=URI.create(property);
            	
	        	if (values==null){
	        		values=new ArrayList<Pair<URI, Object>>();
	        	}
	        	if (object.isResource()) {
	        		if (object.asResource().listProperties().hasNext()==false){
	        			values.add(Pair.of(propertyURI, URI.create(object.asResource().getURI())));
	        		}
	        		else{
	        			Resource metadataResource=object.asResource();
	        			if (RDFUtil.hasType(metadataResource.getModel(), metadataResource, DataModel.TopLevel.uri)){
	        				values.add(Pair.of(propertyURI,  new TopLevelMetadata(metadataResource)));
	        			}
	        			else if (RDFUtil.hasType(metadataResource.getModel(), metadataResource, DataModel.Identified.uri)){
	        				values.add(Pair.of(propertyURI,  new Metadata(metadataResource)));	    	        			        				
	        			}	  
	        			else { //Value is an SBOL entity, add as a URI
	        				values.add(Pair.of(propertyURI, URI.create(object.asResource().getURI())));
	        			}
	        		}
	        	}
	        	else{
	        		values.add(Pair.of(propertyURI, object.asLiteral().getValue()));
	        	}
        	}        	        		
        }
        return values;		
	}
	
	public List<Metadata> getMetadataEntites() throws SBOLGraphException
	{
		ArrayList<Metadata> values=null;
        for (StmtIterator iterator=resource.listProperties();iterator.hasNext();){        	
        	Statement stmt=iterator.next();
        	RDFNode object=stmt.getObject();
        	
        	if (object.isResource()) {
        		Resource metadataResource=object.asResource();
        		Metadata metadata=null;
        		if (RDFUtil.hasType(metadataResource.getModel(), metadataResource, DataModel.Identified.uri)){        				
        			metadata=new Metadata(metadataResource);
        			if (values==null){
                    	values=new ArrayList<Metadata>();
                    }
            		values.add(metadata); 
        		}        			        		       		
        	}        	
        }
        return values;		
	}
	
	
	/*
	public List<Metadata> getAnnotations() throws SBOLGraphException {
		return addToList(resource,null, DataModel.Identified.uri,Metadata.class);
	}
	
	private <T extends Identified>  List<T> addToList(Resource resource, List<T> items, URI entityType, Class<T> identifiedClass) throws SBOLGraphException
	{
		if (items==null)
		{
			List<Resource> resources= RDFUtil.getResourcesWithProperty(resource, URI.create(RDF.type.getURI()), entityType);
			//List<Resource> resources=RDFUtil.getResourcesOfType(resource, entityType);
			if (resources!=null && resources.size()>0)
			{
				items=new ArrayList<T>();
				for (Resource res:resources)
				{
					Identified identified=createIdentified(res, identifiedClass) ;
					items.add((T)identified);
				}
			}
		}
		return items;
	}*/
	
	 
	/*private boolean hasSBOLType (List<URI> types){
		boolean result=false;
		if (types!=null)
		{
			for (URI rdfType:types)
			{
				if (rdfType.toString().toLowerCase().startsWith(URINameSpace.SBOL.getUri().toString().toLowerCase()))
				{
					result=true;
					break;
				}
			}
		}
		return result;
	}*/
	/*Commented on 24 June 2025
	private void inferDisplayId(URI uri) throws SBOLGraphException {
		String displayId = getDisplayId();
		if (StringUtils.isEmpty(displayId)) {
			String result = null;
			String uriString = uri.toString();

			if (SBOLUtil.isURL(uriString))// .contains("://"))
			{
				//String path=uriString;// uri.getPath();
				String path= uri.getPath();
				
				int index = path.lastIndexOf("/");
				if (path.length() > index + 1) {
					result = path.substring(index + 1);
				} else {
					result = null;
				}
				if (result != null && uriString.endsWith(result)) {
					setDisplayId(result);
				}
				else
				{
					throw new SBOLGraphException("An SBOL URI MUST include the display id fragment. URI:" + uri);
				}
			}	
		}
	}
	*/
	private void inferDisplayId(URI uri) throws SBOLGraphException {
		String displayId = getDisplayId();
		if (StringUtils.isEmpty(displayId)) {
			String result = SBOLAPI.inferDisplayId(uri);
			if (!StringUtils.isEmpty(result)){
				setDisplayId(result);
			}			
		}
	}
	
		
	public List<URI> filterIdentifieds(List<URI> identifieds, URI property, String value)
	{
		return RDFUtil.filterItems(this.resource.getModel(), identifieds, property, value);
	}	
	
}

/*private void inferDisplayId(URI uri) {
	if ((uri.getPath() != null && uri.getPath().length() > 0)|| (uri.getFragment() != null && uri.getFragment().length() > 0)) {
		displayId = getDisplayId();
		if (StringUtils.isEmpty(displayId)) {
			String result = null;
			String uriString = uri.toString();

			if (SBOLUtil.isURL(uriString))// .contains("://"))
			{
				int index = uriString.lastIndexOf("#");
				int index2 = uriString.lastIndexOf("/");
				if (index2 > index) {
					index = index2;
				}
				if (uriString.length() > index + 1) {
					result = uriString.substring(index + 1);
				} else {
					result = null;
				}
			}
			if (result != null) {
				setDisplayId(result);
			}
		}
	}
}
// }
}
*/

/*protected <T extends Identified>  void addToList(Resource res, List<T> items, URI property, URI entityType) throws SBOLException, SBOLGraphException
{
	List<Resource> resources=RDFUtil.getResourcesWithProperty(res, property);
	for (Resource subResource:resources)
	{
		Identified identified=SBOLEntityFactory.create(subResource, entityType) ;
		items.add((T)identified);
	}
}
*/

/*protected Identified get_del(Resource res, URI property, URI entityType) throws SBOLGraphException, SBOLException
{
	Identified identified=null;
	List<Resource> resources=RDFUtil.getResourcesWithProperty(res, property);
	if (resources!=null && resources.size()>1)
	{
		String message=String.format("Found multiple versiond of %s entity", entityType.toString());
		throw new SBOLGraphException(message);
	}
	else
	{
		identified=SBOLEntityFactory.create(resources.get(0), entityType) ;
	}
	return identified;
}

public Resource toResource(Model model)
	{
		URI resourceType=this.getResourceType();
		
		Resource resource=RDFHandler.createResource(model, this.getUri(), resourceType);
		
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#displayId"), this.displayId);
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#name"), this.name);
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#description"), this.description);		
		RDFHandler.addProperty(model, resource, URI.create("https://www.w3.org/TR/prov-o/wasGeneratedBy"), this.wasGeneratedBy);
		RDFHandler.addProperty(model, resource, URI.create("https://www.w3.org/TR/prov-o/wasDerivedFrom"), this.wasDerivedFrom);
		
		this.addEntitySpecificProperties(model, resource);
		
		return resource;
	}
	
*
*/

