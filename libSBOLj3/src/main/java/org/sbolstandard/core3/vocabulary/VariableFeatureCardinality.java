package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents variable feature cardinalities in the SBOL data model.
 *
 */
public enum VariableFeatureCardinality {
	
	ZeroOrOne(URINameSpace.SBOL.local("zeroOrOne")), 
	One(URINameSpace.SBOL.local("one")),
	ZeroOrMore(URINameSpace.SBOL.local("zeroOrMore")), 
	OneOrMore(URINameSpace.SBOL.local("oneOrMore"));

	private URI uri;

	VariableFeatureCardinality(URI envUrl) {
		this.uri = envUrl;
	}
	
	/**
	 * Gets the URI associated with the VariableFeatureCardinality object.
	 * @return The relevant URI.
	 */
	public URI getUri() {
		return uri;
	}
	
	private static final Map<URI, VariableFeatureCardinality> lookup = new HashMap<>();

	static
	{
	    for(VariableFeatureCardinality encoding: VariableFeatureCardinality.values())
	    {
	        lookup.put(encoding.getUri(), encoding);
	    }
	}
	
	/**
	 * Gets the variable feature cardinality from a supplied URI.
	 * @param uri The URI to be accessed.
	 * @return A VariableFeatureCardinality object associated with the URI.
	 */
	public static VariableFeatureCardinality get(URI uri) 
	{
	    return lookup.get(uri);
	}
}
