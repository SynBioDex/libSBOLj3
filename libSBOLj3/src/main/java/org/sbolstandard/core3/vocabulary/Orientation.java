package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents orientations in the SBOL data model.
 *
 */
public enum Orientation {
	inline(URINameSpace.SO.local("0001030")), 
	reverseComplement(URINameSpace.SO.local("0001031"));

	private URI uri;

	Orientation(URI uri) {
		this.uri = uri;
	}
	
	/**
	 * Gets the URI for the associated Orientation object.
	 * @return A URI object identifying the orientation.
	 */
	public URI getUri() {
		return uri;
	}
	
	private static final Map<URI, Orientation> lookup = new HashMap<>();
	  
    static
    {
        for(Orientation orientation : Orientation.values())
        {
            lookup.put(orientation.getUri(), orientation);
        }
        lookup.put(URINameSpace.SBOL.local("inline"), inline);
        lookup.put(URINameSpace.SBOL.local("reverseComplement"), reverseComplement);
        
    }
    
    /**
     * Gets the orientation from the supplied URI
     * @param uri The URI to be inspected.
     * @return An Orientation object associated with the URI.
     */
    public static Orientation get(URI uri) 
    {
        return lookup.get(uri);
    }

}
