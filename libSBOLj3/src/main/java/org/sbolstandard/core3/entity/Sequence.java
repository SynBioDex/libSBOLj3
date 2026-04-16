package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;

/**
 * This class represents an encoded sequence.
 */
public class Sequence extends TopLevel {
	/*private String elements;
	private Encoding encoding;*/

	protected  Sequence(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Sequence(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/**
	 * Gets a list validation messages corresponding to errors and best practices.
	 */
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		if (this.getElements()!=null && this.getEncoding()==null)
		{
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{SEQUENCE_MUST_HAVE_ENCODING}", DataModel.Sequence.encoding));      	   
		}
		validationMessages = checkEncodingType(validationMessages);
		
		if (Configuration.getInstance().isValidateRecommendedRules()){
			URI encodingValue=this.getEncoding();
			
			if (encodingValue!=null && !Configuration.getInstance().getEdamEncodingTerms().contains(encodingValue.toString()))
			{
				ValidationMessage message = new ValidationMessage("{SEQUENCE_ENCODING_VALID_SUBTERM}", DataModel.Sequence.encoding, encodingValue);
				validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
			}
		}
		//SEQUENCE_ENCODING_VALID_SUBTERM
		/*		if (Configuration.getInstance().isValidateRecommendedRules()){
					List<Sequence> sequences=this.getSequences();
					if (sequences!=null){
						Map<URI,Boolean> validity=new HashMap<URI, Boolean>();
						for (Sequence sequence:sequences){
							URI encodingValue=sequence.getEncoding();
							if (encodingValue!=null && Encoding.get(encodingValue)==null){//Check for uncontrolled URIs only.
								Boolean valid=validity.get(encodingValue);
								if (valid==null){
									valid=RDFUtil.hasParentRecursively(Configuration.getInstance().getEDAMOntology(), encodingValue.toString() , Encoding.PARENT_TERM.toString());
							    	validity.put(encodingValue, valid);
								}
								if (!valid){
									ValidationMessage message = new ValidationMessage("{SEQUENCE_ENCODING_VALID_SUBTERM}", DataModel.Sequence.uri, sequence, encodingValue);
									message.childPath(DataModel.Sequence.encoding, null);
									messages=IdentifiedValidator.addToValidations(messages, message);

								}
							}
						}
					}
				}
*/
    	return validationMessages;
	}
	
	private List<ValidationMessage> checkEncodingType(List<ValidationMessage> validationMessages) throws SBOLGraphException {

		String elements = this.getElements();
		Encoding enc = Encoding.get(this.getEncoding());
		if (elements!=null && !elements.isEmpty() && enc != null) {
			if (enc.equals(Encoding.AminoAcid)) {
				//Pattern patternAA = Pattern.compile("^[ARNDCQEGHILKMFPSTWYVX-]+$", Pattern.CASE_INSENSITIVE); // compiled from list of characters at https://iupac.qmul.ac.uk/AminoAcid/AA1n2.html
				  Pattern patternAA = Pattern.compile("^[ABCDEFGHIJKLMNOPQRSTUVWXYZ]+$", Pattern.CASE_INSENSITIVE); // compiled from list of characters at https://iupac.qmul.ac.uk/AminoAcid/AA1n2.html
				Matcher matcherAA = patternAA.matcher(elements);
				if (!matcherAA.find()) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage(
							"{SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING}", DataModel.Sequence.elements, elements));
				}
			} else if (enc.equals(Encoding.INCHI)) {
				Pattern patternINCHI = Pattern.compile("^((InChI=)?[^J][0-9a-z+\\-\\(\\)\\\\\\/,]+)$",
						Pattern.CASE_INSENSITIVE); // general regex used from
													// https://gist.github.com/lsauer/1312860/264ae813c2bd2c27a769d261c8c6b38da34e22fb
				Matcher matcherINCHI = patternINCHI.matcher(elements);
				if (!matcherINCHI.find()) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage(
							"{SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING}", DataModel.Sequence.elements, elements));
				}
			} else if (enc.equals(Encoding.NucleicAcid)) {
				//Pattern patternNA = Pattern.compile("^[ATGCIUXQRYN]+$", Pattern.CASE_INSENSITIVE); // compiled from list at https://iupac.qmul.ac.uk/misc/naabb.html#p3
				Pattern patternNA = Pattern.compile("^[ACGTURYSWKMBDHVN.-]+$", Pattern.CASE_INSENSITIVE); // compiled from list at https://iupac.qmul.ac.uk/misc/naabb.html#p3
				Matcher matcherNA = patternNA.matcher(elements);
				if (!matcherNA.find()) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage(
							"{SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING}", DataModel.Sequence.elements, elements));
				}
			} else if (enc.equals(Encoding.SMILES)) {
				Pattern patternSMILES = Pattern.compile("^([^J][A-Za-z0-9@+\\-\\[\\]\\(\\)\\\\\\/%=#$]+)$",
						Pattern.CASE_INSENSITIVE); // general regex taken from https://www.biostars.org/p/13468/
				Matcher matcherSMILES = patternSMILES.matcher(elements);
				if (!matcherSMILES.find()) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage(
							"{SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING}", DataModel.Sequence.elements, elements));
				}
			}

		}

		return validationMessages;
	}

	/**
	 * Get the elements associated with the sequence. 
	 * @return An object containing the relevant elements.
	 * @throws SBOLGraphException
	 */
	public String getElements() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Sequence.elements);
	}
	
	/**
	 * Set the elements of the corresponding sequence.
	 * @param elements The elements to be applied.
	 */
	public void setElements(String elements) {
		RDFUtil.setProperty(this.resource, DataModel.Sequence.elements, elements);
	}
	
	/**
	 * Get the encoding type for the sequence.
	 * @return An object with the corresponding encoding type.
	 * @throws SBOLGraphException
	 */
	public URI getEncoding() throws SBOLGraphException {
		//Encoding encoding=null;
		URI encodingValue=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Sequence.encoding);
		/*if (encodingValue!=null){
			encoding=Encoding.get(encodingValue); 
		}*/
		return encodingValue;
	}
	
	/**
	 * Set the encoding type for the sequence.
	 * @param encoding The encoding type to be applied.
	 */
	public void setEncoding(Encoding encoding) {
		URI encodingURI=null;
		if (encoding!=null)
		{
			encodingURI=encoding.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.Sequence.encoding, encodingURI);
	}
	
	/**
	 * Set the encoding type for the sequence.
	 * @param encoding The encoding type to be applied.
	 */
	public void setEncoding(URI encoding) {
		RDFUtil.setProperty(this.resource, DataModel.Sequence.encoding, encoding);
	}
	
	/**
	 * Gets the resource type for the sequence.
	 * @return A URI object representing the corresponding resource.
	 */
	public URI getResourceType()
	{
		return DataModel.Sequence.uri;
	}
	
	public static String getReverseComplement(String sequence) {
        StringBuilder revComp = new StringBuilder();
        for (int i = sequence.length() - 1; i >= 0; i--) {
            char base = sequence.charAt(i);
            switch (base) {
                //Default bases
				case 'A': revComp.append('T'); break;
                case 'T': revComp.append('A'); break;
                case 'C': revComp.append('G'); break;
                case 'G': revComp.append('C'); break;
                case 'U': revComp.append('A'); break; 
                
				// Others
                case 'R': revComp.append('Y'); break;  // AG -> TC
                case 'Y': revComp.append('R'); break;  // CT -> GA
                case 'S': revComp.append('S'); break;  // GC -> CG (self)
                case 'W': revComp.append('W'); break;  // AT -> TA (self)
                case 'K': revComp.append('M'); break;  // GT -> CA
                case 'M': revComp.append('K'); break;  // AC -> TG
                case 'B': revComp.append('V'); break;  // CGT -> GCA
                case 'D': revComp.append('H'); break;  // AGT -> TCA
                case 'H': revComp.append('D'); break;  // ACT -> TGA
                case 'V': revComp.append('B'); break;  // ACG -> TGC
                case 'N': revComp.append('N'); break;  // any -> any
                
				// Lowercase
                case 'a': revComp.append('t'); break;
                case 't': revComp.append('a'); break;
                case 'c': revComp.append('g'); break;
                case 'g': revComp.append('c'); break;
                case 'u': revComp.append('a'); break;
                case 'r': revComp.append('y'); break;
                case 'y': revComp.append('r'); break;
                case 's': revComp.append('s'); break;
                case 'w': revComp.append('w'); break;
                case 'k': revComp.append('m'); break;
                case 'm': revComp.append('k'); break;
                case 'b': revComp.append('v'); break;
                case 'd': revComp.append('h'); break;
                case 'h': revComp.append('d'); break;
                case 'v': revComp.append('b'); break;
                case 'n': revComp.append('n'); break;
                // Gap characters and any other, // '-', '.', ...
                default:  revComp.append(base); break;  
            }
        }
        return revComp.toString();
}
}