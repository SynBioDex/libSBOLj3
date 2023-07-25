package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents role integrations in the SBOL data model.
 *
 */
public enum RoleIntegration {
	overrideRoles(URINameSpace.SBOL.local("overrideRoles")), 
	mergeRoles(URINameSpace.SBOL.local("mergeRoles"));

	private URI uri;

	RoleIntegration(URI uri) {
		this.uri = uri;
	}
	
	/**
	 * Gets the URI associated with the RoleIntegration object.
	 * @return The relevant URI.
	 */
	public URI getUri() {
		return uri;
	}
	
	private static final Map<URI, RoleIntegration> lookup = new HashMap<>();
	  
    static
    {
        for(RoleIntegration value : RoleIntegration.values())
        {
            lookup.put(value.getUri(), value);
        }
    }
    
    /**
     * Gets a role integration from a supplied URI.
     * @param uri The URI to be accessed.
     * @return The RoleIntegration object associated with the URI.
     */
    public static RoleIntegration get(URI uri) 
    {
        return lookup.get(uri);
    }

}
