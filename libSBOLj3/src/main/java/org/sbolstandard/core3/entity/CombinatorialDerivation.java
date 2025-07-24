package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.CombinatorialDerivationStrategy;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.VariableFeatureCardinality;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CombinatorialDerivation extends TopLevel{
	/*private URI template=null;
	private URI strategy=null;
	private List<VariableFeature> variableFeatures=new ArrayList<VariableFeature>();*/
	
	protected  CombinatorialDerivation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  CombinatorialDerivation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		
			List<VariableFeature> variableFeatures=this.getVariableFeatures();
			if (variableFeatures!=null){
				Map<URI, Integer> variableCount=new HashMap<>();
				for (VariableFeature variableFeature: variableFeatures){
					
					if (this.getStrategy()==CombinatorialDerivationStrategy.Enumerate){
						//COMBINATORIALDERIVATION_VARIABLEFEATURE_VALID_IF_STRATEGY_ENUMERATE = sbol3-12102 - If the strategy property of a CombinatorialDerivation contains the URI http://sbols.org/v3#enumerate, then its hasVariableFeature property MUST NOT contain a VariableFeature with an cardinality property that contains the URI http://sbols.org/v3#zeroOrMore or the URI http://sbols.org/v3#oneOrMore.
						VariableFeatureCardinality cardinality=variableFeature.getCardinality();
						if (cardinality.equals(VariableFeatureCardinality.OneOrMore) || cardinality.equals(VariableFeatureCardinality.ZeroOrMore))
						{
							ValidationMessage message = new ValidationMessage("{COMBINATORIALDERIVATION_VARIABLEFEATURE_VALID_IF_STRATEGY_ENUMERATE}", DataModel.CombinatorialDerivation.variableFeature,variableFeature,variableFeature.getCardinality().getUri());
							message.childPath(DataModel.VariableFeature.cardinality);
							validationMessages= addToValidations(validationMessages,message);
						}
					}
					
					//COMBINATORIALDERIVATION_VARIABLEFEATURE_UNIQUE = sbol3-12103 - A CombinatorialDerivation MUST NOT contain two or more hasVariableFeature properties that refer to VariableFeature objects with a variable property that contain the same URI.
					if (variableFeature.getVariable()!=null){
						Integer count=variableCount.get(variableFeature.getVariable().getUri());
						if (count==null){
							variableCount.put(variableFeature.getVariable().getUri(), 1);
						}
						else {
							if (count==1){
								ValidationMessage message = new ValidationMessage("{COMBINATORIALDERIVATION_VARIABLEFEATURE_UNIQUE}", DataModel.CombinatorialDerivation.variableFeature,variableFeature,variableFeature.getVariable());
								message.childPath(DataModel.VariableFeature.variable);
								validationMessages= addToValidations(validationMessages,message);
							}
							variableCount.put(variableFeature.getVariable().getUri(), count++);
						}	
					}
					
					//COMBINATORIALDERIVATION_VARIABLEFEATURE_CONTAINED_BY_TEMPLATE
					validationMessages= assertVariableFeatureContainedByTemplate(validationMessages, variableFeature);					
				}
			}			
		
		
		if (Configuration.getInstance().isValidateRecommendedRules())
		{
			validationMessages= assertTemplateHasAtLeastOneFeature(validationMessages);
		}
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.CombinatorialDerivation.variableFeature, this.resource, getVariableFeatures(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.CombinatorialDerivation.template, this.resource, getTemplate(), validationMessages);
			
		return validationMessages;
	}
	
	//COMBINATORIALDERIVATION_VARIABLEFEATURE_CONTAINED_BY_TEMPLATE = sbol3-12202 - The Feature referenced by the variable property of a VariableFeature MUST be contained by the template Component of the CombinatorialDerivation that contains the VariableFeature.
	private List<ValidationMessage> assertVariableFeatureContainedByTemplate(List<ValidationMessage> validationMessages, VariableFeature variableFeature) throws SBOLGraphException{
		boolean found=false;
		if (variableFeature!=null) {
			Feature feature=variableFeature.getVariable();			
			Component template= this.getTemplate();
			if(template!=null){
				List<Feature> templateFeatures=template.getFeatures();
				if (feature!=null && templateFeatures!=null){
					if (SBOLUtil.getURIs(templateFeatures).contains(feature.getUri())){
						found = true;
					}
				}				
			}			
		
			if (!found){
				ValidationMessage message = new ValidationMessage("{COMBINATORIALDERIVATION_VARIABLEFEATURE_CONTAINED_BY_TEMPLATE}", DataModel.CombinatorialDerivation.variableFeature,variableFeature,feature);
				message.childPath(DataModel.VariableFeature.variable);
				validationMessages= addToValidations(validationMessages,message);
			}
		}			
		return validationMessages;
	}
	
	private List<ValidationMessage> assertTemplateHasAtLeastOneFeature(List<ValidationMessage> messages) throws SBOLGraphException
	{
		Component templateComponent = this.getTemplate();
		if (templateComponent!=null)
		{
			List<Feature> features=templateComponent.getFeatures();
			if (features==null || features.size()==0)
			{
				ValidationMessage message = new ValidationMessage("{COMBINATORIALDERIVATION_TEMPLATECOMPONENT_HAS_ATLEAST_ONE_FEATURE}", DataModel.CombinatorialDerivation.template,templateComponent, null);
				message.childPath(DataModel.Component.feature);
				messages= addToValidations(messages,message);	
			}
		}
		return messages;
	}
	
	/*@NotNull(message = "{COMBINATORIALDERIVATION_TEMPLATE_NOT_NULL}")
	public URI getTemplate() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.template);
	}

	/*public void setTemplate(@NotNull(message = "{COMBINATORIALDERIVATION_TEMPLATE_NOT_NULL}") URI template) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTemplate", new Object[] {template}, URI.class);
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, template);
	}*/
	
	@NotNull(message = "{COMBINATORIALDERIVATION_TEMPLATE_NOT_NULL}")
	public Component getTemplate() throws SBOLGraphException {
		return contsructIdentified(DataModel.CombinatorialDerivation.template, Component.class, DataModel.Component.uri);
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.template);
	}
	
	/*public URI getTemplateURI() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.template);
	}*/
	
	public void setTemplate(@NotNull(message = "{COMBINATORIALDERIVATION_TEMPLATE_NOT_NULL}") Component template) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTemplate", new Object[] {template}, Component.class);
		URI uri=null;
		if (template!=null)
		{
			uri=template.getUri();
		}
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, uri);
	}
	
	public void setTemplate(@NotNull(message = "{COMBINATORIALDERIVATION_TEMPLATE_NOT_NULL}") URI template) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTemplate", new Object[] {template}, URI.class);		
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, template);
	}
	
	public CombinatorialDerivationStrategy getStrategy() throws SBOLGraphException {
		CombinatorialDerivationStrategy strategy=null;
		
		URI value=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.strategy);
		if (value!=null){
			
			strategy=toStrategy(value); 	
			PropertyValidator.getValidator().validateReturnValue(this, "toStrategy", strategy, URI.class);
		}
		return strategy;		
	}

	@NotNull(message = "{COMBINATORIALDERIVATION_STRATEGY_VALID_IF_NOT_NULL}")   
	public CombinatorialDerivationStrategy toStrategy(URI uri)
	{
		return CombinatorialDerivationStrategy.get(uri); 
	}

	
	public void setStrategy(CombinatorialDerivationStrategy strategy) {
		URI uri=null;
		if (strategy!=null)
		{
			uri=strategy.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.CombinatorialDerivation.strategy, uri);		
	}

	@Valid
	public List<VariableFeature> getVariableFeatures() throws SBOLGraphException {
		return addToList(DataModel.CombinatorialDerivation.variableFeature, VariableFeature.class, DataModel.VariableFeature.uri);
	}
	
	public VariableFeature createVariableFeature(URI uri, VariableFeatureCardinality cardinality, Feature variable) throws SBOLGraphException
	{
		VariableFeature variableComponent= new VariableFeature(this.resource.getModel(), uri);
		variableComponent.setCardinality(cardinality);
		variableComponent.setVariable(variable);
		addToList(variableComponent, DataModel.CombinatorialDerivation.variableFeature);
		return variableComponent;	
	}
	
	private VariableFeature createVariableFeature(String displayId, VariableFeatureCardinality cardinality, Feature variable) throws SBOLGraphException
	{
		return createVariableFeature(SBOLAPI.append(this.getUri(), displayId), cardinality, variable);	
	}
	
	public VariableFeature createVariableFeature(VariableFeatureCardinality cardinality, Feature variable) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.VariableFeature.uri, getVariableFeatures());	
		return createVariableFeature(displayId, cardinality, variable);	
	}

	@Override
	public URI getResourceType() {
		return DataModel.CombinatorialDerivation.uri;
	}

	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getVariableFeatures());
		return identifieds;
	}
	
	
}