package org.sbolstandard.core3.vocabulary;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.validation.ValidationMessage;

/**
 * 
 * Represents the encoding types in the SBOL data model.
 *
 */
public enum Encoding
{
	 	NucleicAcid(URINameSpace.EDAM.local("format_1207")), 
	    AminoAcid(URINameSpace.EDAM.local("format_1208")),
	    SMILES(URINameSpace.EDAM.local("format_1196")),
    	INCHI(URINameSpace.EDAM.local("format_1197"));
	 
	    private URI uri;
	 
	    Encoding(URI uri) {
	        this.uri = uri;
	    }
	    
	    /**
	     * Gets the URI associated with the Encoding object.
	     * @return The relevant URI.
	     */
	    public URI getUri() {
	        return uri;
	    }
	    
	    private static final Map<URI, Encoding> lookup = new HashMap<>();
	    
	    static
	    {
	        for(Encoding encoding: Encoding.values())
	        {
	            lookup.put(encoding.getUri(), encoding);
	        }
	    }
	    
	    /**
	     * Gets the encoding identified by the supplied URI.
	     * @param uri The URI to be accessed.
	     * @return The corresponding Encoding object.
	     */
	    public static Encoding get(URI uri) 
	    {
	        return lookup.get(uri);
	    }
	    
	    public static final URI PARENT_TERM= URINameSpace.EDAM.local("format_2330");

		//TODO: Refactor the checkEncodingType code in Sequence.java using this method to avoid code duplication.
		public boolean isValidSequence(String elements) {
			Matcher matcher = null;
			if (this.equals(Encoding.AminoAcid)) {
				//Pattern patternAA = Pattern.compile("^[ARNDCQEGHILKMFPSTWYVX-]+$", Pattern.CASE_INSENSITIVE); // compiled from list of characters at https://iupac.qmul.ac.uk/AminoAcid/AA1n2.html
				Pattern patternAA = Pattern.compile("^[ABCDEFGHIJKLMNOPQRSTUVWXYZ]+$", Pattern.CASE_INSENSITIVE); // compiled from list of characters at https://iupac.qmul.ac.uk/AminoAcid/AA1n2.html
				matcher = patternAA.matcher(elements);
			} 
			else if (this.equals(Encoding.INCHI)) {
				Pattern patternINCHI = Pattern.compile("^((InChI=)?[^J][0-9a-z+\\-\\(\\)\\\\\\/,]+)$",
						Pattern.CASE_INSENSITIVE); // general regex used from
													// https://gist.github.com/lsauer/1312860/264ae813c2bd2c27a769d261c8c6b38da34e22fb
				matcher = patternINCHI.matcher(elements);
			} 
			else if (this.equals(Encoding.NucleicAcid)) {
				//Pattern patternNA = Pattern.compile("^[ATGCIUXQRYN]+$", Pattern.CASE_INSENSITIVE); // compiled from list at https://iupac.qmul.ac.uk/misc/naabb.html#p3
				Pattern patternNA = Pattern.compile("^[ACGTURYSWKMBDHVN.-]+$", Pattern.CASE_INSENSITIVE); // compiled from list at https://iupac.qmul.ac.uk/misc/naabb.html#p3
				matcher = patternNA.matcher(elements);
			} 
			else if (this.equals(Encoding.SMILES)) {
				Pattern patternSMILES = Pattern.compile("^([^J][A-Za-z0-9@+\\-\\[\\]\\(\\)\\\\\\/%=#$]+)$",
						Pattern.CASE_INSENSITIVE); // general regex taken from https://www.biostars.org/p/13468/
				matcher = patternSMILES.matcher(elements);
			}
			return matcher != null && matcher.find();
		}
					    
}



