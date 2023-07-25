package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledTopLevel;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
import jakarta.validation.constraints.NotEmpty;

/**
 * 
 * Represents a unit in the SBOL data model.
 *
 */
public abstract class Unit extends ControlledTopLevel{
	
	
	/*private String symbol;
	private List<String> alternativeSymbols;
	private String label;
	private List<String> alternativeLabels;
	private String comment;
	private String longComment;*/
		
	protected  Unit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Unit(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Gets the primary symbol of the unit.
	 * @return The associated symbol string.
	 * @throws SBOLGraphException
	 */
	@NotEmpty(message = "{UNIT_SYMBOL_NOT_EMPTY}")	
	public String getSymbol() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.symbol);
	}
	
	/**
	 * Sets the primary symbol of the unit.
	 * @param symbol The symbol string to be applied.
	 * @throws SBOLGraphException
	 */
	public void setSymbol(@NotEmpty(message = "{UNIT_SYMBOL_NOT_EMPTY}") String symbol) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setSymbol", new Object[] {symbol}, String.class);
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.symbol, symbol);	
	}
	
	/**
	 * Gets the alternative symbols associated with the unit.
	 * @return A list of corresponding alternative symbol strings.
	 */
	public List<String> getAlternativeSymbols() {
		return RDFUtil.getLiteralPropertiesAsStrings(this.resource, MeasureDataModel.Unit.alternativeSymbol);
	}
	
	/**
	 * Sets the alternative symbols for the unit.
	 * @param alternativeSymbols A list of alternative symbol strings to be applied.
	 */
	public void setAlternativeSymbols(List<String> alternativeSymbols) {
		RDFUtil.setPropertyAsStrings(resource, MeasureDataModel.Unit.alternativeSymbol, alternativeSymbols);	
	}
	
	/**
	 * Gets the label from the unit.
	 * @return The associated label string.
	 * @throws SBOLGraphException
	 */
	@NotEmpty(message = "{UNIT_LABEL_NOT_EMPTY}")	
	public String getLabel() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.label);
	}
	
	/**
	 * Sets a label to the unit.
	 * @param label The label string to be applied.
	 * @throws SBOLGraphException
	 */
	public void setLabel(@NotEmpty(message = "{UNIT_LABEL_NOT_EMPTY}") String label) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setLabel", new Object[] {label}, String.class);
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.label, label);
		
		if (label!=null && !label.isEmpty() && !label.equals(getName())){
			setName(label);
		}
	}
	
	/**
	 * Gets the alternative labels from the unit.
	 * @return A list of associated alternative labels.
	 */
	public List<String> getAlternativeLabels() {
		return RDFUtil.getLiteralPropertiesAsStrings(this.resource, MeasureDataModel.Unit.alternativeLabel);
	}
	
	/**
	 * Sets the alternative labels for the unit.
	 * @param alternativeLabels A list of alternative label strings to apply.
	 */
	public void setAlternativeLabels(List<String> alternativeLabels) {
		RDFUtil.setPropertyAsStrings(resource, MeasureDataModel.Unit.alternativeLabel, alternativeLabels);
	}
	
	/**
	 * Sets the name of the unit.
	 * @param name The name to be applied.
	 */
	@Override 
	public void setName(String name) throws SBOLGraphException
	{
		super.setName(name);
		
		if (name!=null && !name.equals(getLabel()))
		{
			setLabel(name);
		}
	}
	
	/**
	 * Sets the description of the unit.
	 * @param description The description to be applied.
	 */
	@Override 
	public void setDescription(String description) throws SBOLGraphException
	{
		super.setDescription(description);
		
		if (description!=null && !description.equals(getComment()))
		{
			setComment(description);
		}
	}
	
	/**
	 * Gets the comment associated with this unit.
	 * @return A string containing the comment.
	 * @throws SBOLGraphException
	 */
	public String getComment() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.comment);
	}
	
	/**
	 * Sets the comment to the unit.
	 * @param comment The comment string to be applied.
	 * @throws SBOLGraphException
	 */
	public void setComment(String comment) throws SBOLGraphException {
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.comment, comment);
		if (comment!=null && !comment.equals(getDescription()))
		{
			setDescription(comment);
		}	
	}
	
	/**
	 * Gets the long comment from the unit.
	 * @return The associated long comment string.
	 * @throws SBOLGraphException
	 */
	public String getLongComment() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.longComment);
	}
	
	/**
	 * Sets a long comment to the unit.
	 * @param longComment The long comment string to be applied.
	 */
	public void setLongComment(String longComment) {
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.longComment, longComment);		
	}
	
	/**
	 * Gets the resource type associated with the unit.
	 * @return The corresponding URI identifying the resource type.
	 */
	@Override
	public URI getResourceType() {
		return MeasureDataModel.Unit.uri;
	}
	
	/**
	 * Gets the subclass types associated with the unit.
	 * @param <T>
	 * @return A list object containing associated subclasses.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Identified> HashMap<URI, Class<T>> getSubClassTypes()
	{
		HashMap<URI, Class<T>> subclasses=new HashMap<URI, Class<T>>();
		subclasses.put(MeasureDataModel.BinaryPrefix.uri, (Class<T>) BinaryPrefix.class);
		subclasses.put(MeasureDataModel.PrefixedUnit.uri, (Class<T>) PrefixedUnit.class);
		subclasses.put(MeasureDataModel.SingularUnit.uri, (Class<T>) SingularUnit.class);
		subclasses.put(MeasureDataModel.SIPrefix.uri, (Class<T>) SIPrefix.class);
		subclasses.put(MeasureDataModel.UnitDivision.uri, (Class<T>) UnitDivision.class);
		subclasses.put(MeasureDataModel.UnitExponentiation.uri, (Class<T>) UnitExponentiation.class);
		subclasses.put(MeasureDataModel.UnitMultiplication.uri, (Class<T>) UnitMultiplication.class);	
		return subclasses;
	}
	
	/**
	 * Gets the validation messages associated with the unit.
	 * @return A list of associated validation messages.
	 */
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages=assertNameLabelEqual(validationMessages);
		validationMessages=assertDescriptionCommentEqual(validationMessages);		
		return validationMessages;
	}
	
	/**
	 * Asserts a validation message if the label and name are the same.
	 * @param validationMessages A list of validation messages to be appended to.
	 * @return An updated ValidationMessages object.
	 * @throws SBOLGraphException
	 */
	public List<ValidationMessage> assertNameLabelEqual(List<ValidationMessage> validationMessages) throws SBOLGraphException{
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			String label=this.getLabel();
			String name=this.getName();
			validationMessages=IdentifiedValidator.assertTwoPropertyValueIdenticalEqual (validationMessages, getNameLabelMessage(), name, label, DataModel.Identified.name, MeasureDataModel.Unit.label); 
			/*if (label!=null && !label.isEmpty() && name!=null && !name.isEmpty()) {
				if (!label.equals(name)){
					String message=String.format("{UNIT_NAME_LABEL_EQUAL}%sLabel: %s",ValidationMessage.INFORMATION_SEPARATOR, label);				
					ValidationMessage valMessage=new ValidationMessage(message, DataModel.Identified.name, name);  				
					validationMessages= IdentifiedValidator.addToValidations(validationMessages,valMessage);											
				}
			}*/
		}
		return validationMessages;
	}
	
	/**
	 * Asserts a validation message if the description and comment are the same.
	 * @param validationMessages A list of validation messages to be appended to.
	 * @return An updated ValidationMessages object.
	 * @throws SBOLGraphException
	 */
	public List<ValidationMessage> assertDescriptionCommentEqual(List<ValidationMessage> validationMessages) throws SBOLGraphException {		
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			String desc=this.getDescription();
			String comment=this.getComment();
			validationMessages=IdentifiedValidator.assertTwoPropertyValueIdenticalEqual (validationMessages,getDescriptionCommentMessage() , desc, comment, DataModel.Identified.description, MeasureDataModel.Unit.comment); 
		}
		return validationMessages;
	}
	
	protected String getNameLabelMessage()
	{
		return "{UNIT_NAME_LABEL_EQUAL}";
	}
	
	protected String getDescriptionCommentMessage()
	{
		return "{UNIT_DESCRIPTION_COMMENT_EQUAL}";
	}
	
	
}