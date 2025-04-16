package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.RoleIntegration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * This class represents the structural hierarchy of Features within a given Component.
 * 
 *
 */
public class SubComponent extends FeatureWithLocation{
	/*private URI roleIntegration=null;
	private URI isInstanceOf=null;
	private List<Location> locations=null;
	private List<Location> sourceLocations=null;*/
	

	protected  SubComponent(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SubComponent(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		List<URI> roles=this.getRoles();
		if (roles!=null && roles.size()>0){
			RoleIntegration roleIntegration=this.getRoleIntegration();
			if (roleIntegration==null){
				validationMessages= addToValidations(validationMessages,new ValidationMessage("{SUBCOMPONENT_ROLEINTEGRATION_NOT_NULL_IF_ROLES_EXIST}", DataModel.SubComponent.roleIntegration));      	
			}
		}
		
		validationMessages=assertDoNotHaveOverlappingRegions(validationMessages, "{SUBCOMPONENT_LOCATIONS_REGIONS_NOT_OVERLAPPING}");
				
		List<Location> sourceLocations= getSourceLocations();
		List<Location> locations= getLocations();
		if (!CollectionUtils.isEmpty(locations))
		{
			if (!CollectionUtils.isEmpty(sourceLocations))
			{ //SUBCOMPONENT_LOCATIONS_AND_SOURCE_LOCATION_LENGTHS_MATCH
				
				int sourceLength= getLocationLength(sourceLocations);
				int locationLength= getLocationLength(locations);
				if (sourceLength!=locationLength)
				{
					String message=String.format("%s ADDITIONAL INFORMATION: Calculated source location length:%s, Calculated location length:%s", 
							"{SUBCOMPONENT_LOCATIONS_AND_SOURCE_LOCATION_LENGTHS_MATCH}", sourceLength, locationLength);
					
					validationMessages= addToValidations(validationMessages,new ValidationMessage(message, DataModel.SubComponent.location, SBOLUtil.getURIs(locations)));      		
				}
			}
			else
			{//SUBCOMPONENT_LOCATIONS_AND_NO_SOURCE_LOCATION_LENGTHS_MATCH = 
			//sbol3-10807 - If a SubComponent object has at least one hasLocation and zero sourceLocation properties, and the Component linked by its instanceOf has precisely one hasSequence property whose Sequence has a value for its elements property, then the sum of the lengths of the Location objects referred to by the hasLocation properties MUST equal the length of the elements value of the Sequence.
				Component instanceOfComponent = this.getInstanceOf();
				List<Sequence> seqsOfInstanceOfComponent= instanceOfComponent.getSequences();
				if (seqsOfInstanceOfComponent!=null && seqsOfInstanceOfComponent.size()==1)
				{
					String elements=seqsOfInstanceOfComponent.get(0).getElements();
					if (elements!=null)
					{
						int locationLength= getLocationLength(locations);
						int elementsLength=elements.length();
						if (elementsLength!=locationLength)
						{
							String message=String.format("%s ADDITIONAL INFORMATION: Sequence length:%s, Calculated location length:%s, Sequence: %s", 
									"{SUBCOMPONENT_LOCATIONS_AND_NO_SOURCE_LOCATION_LENGTHS_MATCH}", elementsLength, locationLength, seqsOfInstanceOfComponent.get(0).getUri());
							validationMessages= addToValidations(validationMessages,new ValidationMessage(message, DataModel.SubComponent.location, SBOLUtil.getURIs(locations)));      		
						}
					}
				}
			}
		}
		
		
		
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.SubComponent.instanceOf, this.resource, this.getInstanceOf(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.SubComponent.sourceLocation, this.resource, sourceLocations, validationMessages);

		return validationMessages;
	}
	
	private int getLocationLength(List<Location> locations) throws SBOLGraphException
	{
		int length = 0;
		for (Location location: locations)
		{
			if (location instanceof Range)
			{
				Range range= (Range) location;
				Optional<Integer> start=range.getStart();
				Optional<Integer> end=range.getEnd();
				
				if (!SBOLUtil.isNullOrEmpty(start) && !SBOLUtil.isNullOrEmpty(end))
				{
					int rangeLength=Math.abs(end.get() - start.get()) + 1;
					length= length + rangeLength;
				}
			}
		}
		return length;
	}
	
	/**
	 * Gets the role integration associated with the SubComponent.
	 * @return A RoleIntegration object with the corresponding values.
	 * @throws SBOLGraphException
	 */
	public RoleIntegration getRoleIntegration() throws SBOLGraphException {		
		RoleIntegration roleIntegration=null;
		URI value=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.roleIntegration);
		//value=URI.create("http://test.org");
		if (value!=null){
			roleIntegration=toRoleIntegration(value);
			PropertyValidator.getValidator().validateReturnValue(this, "toRoleIntegration", roleIntegration, URI.class);
		}
		return roleIntegration;
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	@NotNull(message = "{SUBCOMPONENT_ROLEINTEGRATION_VALID_IF_NOT_NULL}")   
	public RoleIntegration toRoleIntegration(URI uri)
	{
		return RoleIntegration.get(uri); 
	}
	
	/**
	 * 
	 * @param roleIntegration
	 */
	public void setRoleIntegration(RoleIntegration roleIntegration) {
		URI roleIntegrationURI=null;
		if (roleIntegration!=null)
		{
			roleIntegrationURI=roleIntegration.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.roleIntegration, roleIntegrationURI);
	}
	
	/**
	 * 
	 * @return
	 * @throws SBOLGraphException
	 */
	@NotNull(message = "{SUBCOMPONENT_ISINSTANCEOF_NOT_NULL}")
	public Component getInstanceOf() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.instanceOf);
		return contsructIdentified(DataModel.SubComponent.instanceOf, Component.class, DataModel.Component.uri);
	}
	
	/**
	 * 
	 * @param isInstanceOf
	 * @throws SBOLGraphException
	 */
	public void setInstanceOf(@NotNull(message = "{SUBCOMPONENT_ISINSTANCEOF_NOT_NULL}") Component isInstanceOf) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setInstanceOf", new Object[] {isInstanceOf}, Component.class);
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.instanceOf, SBOLUtil.toURI(isInstanceOf));	
	}
	
	/**
	 * 
	 * @param isInstanceOf
	 * @throws SBOLGraphException
	 */
	public void setInstanceOf(@NotNull(message = "{SUBCOMPONENT_ISINSTANCEOF_NOT_NULL}") URI isInstanceOf) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setInstanceOf", new Object[] {isInstanceOf}, Component.class);
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.instanceOf, isInstanceOf);	
	}
	
	
	/*private <T extends Location> List<T> merge2(List<T> list1, List<T> list2) throws SBOLGraphException
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
	}*/
	
	/**
	 * Gets the locations for this subcomponent. 
	 * @return A list of locations for the sources.
	 * @throws SBOLGraphException
	 */
	@Valid
	public List<Location> getSourceLocations() throws SBOLGraphException {
		return getLocations(DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Gets the cuts for this subcomponent.
	 * @return A list of cuts for this subcomponent.
	 * @throws SBOLGraphException
	 */
	public List<Cut> getSourceCuts() throws SBOLGraphException {
		return getCuts(DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Gets the ranges for this subcomponent.
	 * @return A list of ranges for this subcomponent.
	 * @throws SBOLGraphException
	 */
	public List<Range> getSourceRanges() throws SBOLGraphException {
		return getRanges(DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Gets the full sequences pertaining to the subcomponent.
	 * @return A list of EntireSequences for this subcomponent.
	 * @throws SBOLGraphException
	 */
	public List<EntireSequence> getSourceEntireSequences() throws SBOLGraphException {
		return getEntireSequences(DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Create a cut for the source of this subcomponent.
	 * @param uri The resource of the cut.
	 * @param at The location of the cut.
	 * @param sequence The sequence the cut is to be applied to.
	 * @return An object with the corresponding cut.
	 * @throws SBOLGraphException
	 */
	public Cut createSourceCut(URI uri,  int at, Sequence sequence) throws SBOLGraphException {
		return createCut(uri, at, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Create a cut for the source of this subcomponent.
	 * @param displayId The identifier for the cut.
	 * @param at The location of the cut.
	 * @param sequence The sequence the cut is to be applied to.
	 * @return An object with the corresponding cut.
	 * @throws SBOLGraphException
	 */
	public Cut createSourceCut(String displayId, int at, Sequence sequence) throws SBOLGraphException {
		return createCut(displayId, at, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Create a cut for the source of this subcomponent.
	 * @param at The location of the cut.
	 * @param sequence The sequence the cut is to be applied to.
	 * @return An object with the corresponding cut.
	 * @throws SBOLGraphException
	 */
	public Cut createSourceCut(int at, Sequence sequence) throws SBOLGraphException {
		return createCut(at, sequence,  DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Create a range for the source of this subcomponent.
	 * @param uri The resource of the range.
	 * @param start The start position of the range.
	 * @param end The end position of the range.
	 * @param sequence The sequence the range relates to.
	 * @return An object containing the corresponding range.
	 * @throws SBOLGraphException
	 */
	public Range createSourceRange(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(uri, start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Create a range for the source of this subcomponent.
	 * @param displayId The identifier for the range.
	 * @param start The start position of the range.
	 * @param end The end position of the range.
	 * @param sequence The sequence the range relates to.
	 * @return An object containing the corresponding range.
	 * @throws SBOLGraphException
	 */
	public Range createSourceRange(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(displayId, start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Create a range for the source of this subcomponent.
	 * @param start The start position of the range.
	 * @param endThe end position of the range.
	 * @param sequence The sequence the range relates to.
	 * @return An object containing the corresponding range.
	 * @throws SBOLGraphException
	 */
	public Range createSourceRange(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Create a sequence for the source of this subcomponent.
	 * @param uri The resource of the sequence.
	 * @param sequence The sequence to be applied to this subcomponent.
	 * @return An object containing the corresponding sequence.
	 * @throws SBOLGraphException
	 */
	public EntireSequence createSourceEntireSequence(URI uri, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(uri, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * Create a sequence for the source of this subcomponent.
	 * @param displayId The identifier of the sequence.
	 * @param sequence The sequence to be applied to this subcomponent.
	 * @return An object containing the corresponding sequence.
	 * @throws SBOLGraphException
	 */
	public EntireSequence createSourceEntireSequence(String displayId,Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(displayId, sequence, DataModel.SubComponent.sourceLocation);	
	}
	
	/**
	 * Create a sequence for the source of this subcomponent.
	 * @param sequence The sequence to be applied to the subcomponent.
	 * @return An object containing the corresponding sequence.
	 * @throws SBOLGraphException
	 */
	public EntireSequence createSourceEntireSequence(Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(sequence, DataModel.SubComponent.sourceLocation);
	}
	
	/**
	 * 
	 */
	@Override
	public URI getResourceType() {
		return DataModel.SubComponent.uri;
	}
	
	/**
	 * 
	 */
	@Override
	public URI getDefaultLocationProperty() {
		return DataModel.SubComponent.location;
	}
	
	/**
	 * 
	 */
	@Override
	public List<URI> getAdditionalLocationProperties() {
		return new ArrayList<URI>(Arrays.asList(DataModel.SubComponent.sourceLocation));
	}
	
	/**
	 * @return
	 */
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getLocations());
		identifieds=addToList(identifieds, this.getSourceLocations());
		
		return identifieds;
	}
	
}