package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.*;
import org.sbolstandard.core3.vocabulary.DataModel;
import jakarta.validation.Valid;

public abstract class FeatureWithLocation extends Feature{
	/*private List<URI> types=new ArrayList<URI>();
	private List<Location> locations=null;*/

	protected  FeatureWithLocation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  FeatureWithLocation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	
	public List<Location> getLocations(URI property) throws SBOLGraphException {		
		/*List<Location> locations=null;
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty (resource, DataModel.SubComponent.location);
			if (resources!=null)
			{
				for (Resource res:resources)
				{
					if (locations==null)
					{
						locations=new ArrayList<Location>();
					}
					Location location= LocationFactory.create(res);	
					locations.add(location);			
				}
			}
		}
		return locations;*/
		return addToList(property, Location.getSubClassTypes());
	}
	
	@Valid
	public List<Location> getLocations() throws SBOLGraphException {		
		return addToList(getDefaultLocationProperty(), Location.getSubClassTypes());
	}
	
	protected List<Cut> getCuts(URI property) throws SBOLGraphException {
		return addToList(property, Cut.class, DataModel.Cut.uri);
	}
	
	protected List<Cut> getCuts() throws SBOLGraphException {
		return getCuts(getDefaultLocationProperty());
	}
	
	private <T extends Location> List<T> getAllLocations(Class<T> locationClass, URI classURI) throws SBOLGraphException {
		List<T> locations= addToList(getDefaultLocationProperty(), locationClass, classURI);
		if (getAdditionalLocationProperties()!=null)
		{
			for (URI uri: getAdditionalLocationProperties())
			{
				List<T> additionalLocations= addToList(uri, locationClass, classURI);
						
				locations=merge(locations, additionalLocations);
			}
		}
		return locations;
	}
	
	private <T extends Location> List<T> merge(List<T> list1, List<T> list2) throws SBOLGraphException
	{
		List<T> allLocations=new ArrayList<T>();
		if (list1!=null)
		{
			allLocations.addAll(list1);
		}
		if (list2!=null)
		{
			allLocations.addAll(list2);
		}
		return allLocations;
	}
	
	protected List<Range> getRanges(URI property) throws SBOLGraphException {
		return addToList(property, Range.class, DataModel.Range.uri);
	}

	
	protected List<Range> getRanges() throws SBOLGraphException {
		return getRanges(getDefaultLocationProperty());
	}
	
	protected List<EntireSequence> getEntireSequences(URI property) throws SBOLGraphException {
		return addToList(property, EntireSequence.class, DataModel.EntireSequenceLocation.uri);
	}

	protected List<EntireSequence> getEntireSequences() throws SBOLGraphException {
		return getEntireSequences(getDefaultLocationProperty());
	}
	
	protected Cut createCut(URI uri,  int at, Sequence sequence, URI property) throws SBOLGraphException {
		Cut cut= new Cut(this.resource.getModel(), uri);
		cut.setSequence(sequence);
		cut.setAt(Optional.of(at));
		addToList(cut, property);
		return cut;
	}
	
	public Cut createCut(URI uri,  int at, Sequence sequence) throws SBOLGraphException {
		return createCut(uri, at, sequence, getDefaultLocationProperty());
	}
	
	protected Cut createCut(String displayId, int at, Sequence sequence, URI property) throws SBOLGraphException {
		return createCut(SBOLAPI.append(this.getUri(), displayId), at, sequence, property);
	}

	public Cut createCut(String displayId, int at, Sequence sequence) throws SBOLGraphException {
		return createCut(displayId, at, sequence, getDefaultLocationProperty());
	}

	protected Cut createCut(int at, Sequence sequence, URI property) throws SBOLGraphException {
		List<Cut> locations=getAllLocations(Cut.class, DataModel.Cut.uri);
		String displayId=SBOLAPI.createLocalName(DataModel.Cut.uri, locations);	
		return createCut(displayId, at, sequence, property);
	}
	public Cut createCut(int at, Sequence sequence) throws SBOLGraphException {
		return createCut(at, sequence, getDefaultLocationProperty());
	}
	
	protected Range createRange(URI uri, int start, int end, Sequence sequence, URI property) throws SBOLGraphException {
		Range range= new Range(this.resource.getModel(), uri);
		range.setSequence(sequence);
		range.setStart(Optional.of(start));
		range.setEnd(Optional.of(end));
		addToList(range, property);
		return range;
	}
	
	public Range createRange(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(uri, start, end, sequence, getDefaultLocationProperty());
	}
	
	protected Range createRange(String displayId, int start, int end, Sequence sequence, URI property) throws SBOLGraphException {
		return createRange(SBOLAPI.append(this.getUri(), displayId), start, end,  sequence, property);
	}
	
	public Range createRange(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(displayId, start, end, sequence, getDefaultLocationProperty());
	}
	
	protected Range createRange(int start, int end, Sequence sequence, URI property) throws SBOLGraphException {
		List<Range> locations=getAllLocations(Range.class, DataModel.Range.uri);
		String displayId=SBOLAPI.createLocalName(DataModel.Range.uri, locations);	
		return createRange(displayId, start, end,  sequence, property);
	}

	public Range createRange(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(start, end, sequence, getDefaultLocationProperty());
	}

	protected EntireSequence createEntireSequence(URI uri, Sequence sequence, URI property) throws SBOLGraphException {
		EntireSequence entireSequence= new EntireSequence(this.resource.getModel(), uri);
		entireSequence.setSequence(sequence);
		addToList(entireSequence, property);
		return entireSequence;
	}
	
	public EntireSequence createEntireSequence(URI uri, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(uri, sequence, getDefaultLocationProperty());
	}
	
	protected EntireSequence createEntireSequence(String displayId, Sequence sequence, URI property) throws SBOLGraphException {
		return createEntireSequence(SBOLAPI.append(this.getUri(), displayId), sequence, property);
	}
	
	public EntireSequence createEntireSequence(String displayId, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(displayId, sequence, getDefaultLocationProperty());
	}
	
	protected EntireSequence createEntireSequence(Sequence sequence, URI property) throws SBOLGraphException {
		List<EntireSequence> locations=getAllLocations(EntireSequence.class, DataModel.EntireSequenceLocation.uri);
		String displayId=SBOLAPI.createLocalName(DataModel.EntireSequenceLocation.uri, locations);	
		return createEntireSequence(displayId, sequence, property);
	}
	
	public EntireSequence createEntireSequence(Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(sequence, getDefaultLocationProperty());
	}
	
	/*public Location createCutLocation(int at, Sequence sequence) throws SBOLGraphException {
		Location location=builder.build(this.resource.getModel(),this.getUri());
		addToList(location, DataModel.LocalSubComponent.location);
		return location;
	}*/
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertExists(this, getDefaultLocationProperty(), this.resource, getLocations(), validationMessages);
		return validationMessages;
	}
	
	abstract public URI getDefaultLocationProperty();
	
	abstract public List<URI> getAdditionalLocationProperties();
	
	@Override
	public URI getResourceType() {
		return DataModel.Feature.uri;
	}
	
	protected List<ValidationMessage> assertDoNotHaveOverlappingRegions(List<ValidationMessage>  validationMessages, String message) throws SBOLGraphException
	{
		List<Location> locations = getLocations(); 
		if (locations != null) {
			for (int i = 0; i < locations.size() - 1; i++) {
				Location location = locations.get(i);
				if (location instanceof Range) {
					Range range1 = (Range) location;
					for (int j = i + 1; j < locations.size(); j++) {	
						Location location2 = locations.get(j);
						if (location2 instanceof Range) {
							Range range2 = (Range) location2;
							validationMessages=assertDoNotOverlap(validationMessages, range1, range2, message);
						} 
						else {		
							continue;
						}
					}
					
				} 
				else {		
					continue;
				}
			}
		}
		return  validationMessages;
	}
	
	private List<ValidationMessage> assertDoNotOverlap(List<ValidationMessage>  validationMessages, Range range1, Range range2, String message) throws SBOLGraphException
	{
		if (range1!=null && range2!=null && !range1.getUri().equals(range2.getUri())){
			ValidationMessage validationMessage=null;
			Optional<Integer> start1Optional=range1.getStart();
			Optional<Integer> start2Optional=range2.getStart();
			Optional<Integer> end1Optional=range1.getEnd();
			Optional<Integer> end2Optional=range2.getEnd();
			
			if (!SBOLUtil.isNullOrEmpty(start1Optional) && !SBOLUtil.isNullOrEmpty(start2Optional) 
					&& !SBOLUtil.isNullOrEmpty(end1Optional) && !SBOLUtil.isNullOrEmpty(end2Optional)){
				int start1=start1Optional.get();
				int end1=end1Optional.get();
				int start2=start2Optional.get();
				int end2=end2Optional.get();
				
				if (existsBetween(end2, start1, end1)) //range2 overlaps with range 1start
				{
					validationMessage=new ValidationMessage(message, DataModel.FeatureWithLocation.location, range2, end2);
					validationMessage.childPath(DataModel.Range.end);
				}
				else if (existsBetween(end1, start2, end2))
				{
					validationMessage=new ValidationMessage(message, DataModel.FeatureWithLocation.location, range1, end1);
					validationMessage.childPath(DataModel.Range.end);
				}
				else if (existsBetween(start2, start1, end1)) //range2 overlaps with range1 end		
				{
					validationMessage=new ValidationMessage(message, DataModel.FeatureWithLocation.location, range2, start2);
					validationMessage.childPath(DataModel.Range.start);
				}
				else if (existsBetween(start1, start2, end2)) 	
				{
					validationMessage=new ValidationMessage(message, DataModel.FeatureWithLocation.location, range1, start1);
					validationMessage.childPath(DataModel.Range.start);
				}
				
				if (validationMessage!=null)
				{
					validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
				}
			}
		}
		return validationMessages;	
	}
	
	public static boolean existsBetween (int location, int start, int end)
	{
		return (location>=start && location<=end);
	}
	
}