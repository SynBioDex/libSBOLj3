package org.sbolstandard.core3.vocabulary;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents a combinational derivation strategy in the SBOL data model.
 *
 */
public enum CombinatorialDerivationStrategy
{
	 	Enumerate(URINameSpace.SBOL.local("enumerate")), 
	 	Sample(URINameSpace.SBOL.local("sample"));
	 	
	    private URI url;
	 
	    CombinatorialDerivationStrategy(URI envUrl) {
	        this.url = envUrl;
	    }
	    
	    /**
	     * Gets the URI associated with this combinational derivation strategy.
	     * @return The URI object associated with this combinational derivation strategy.
	     */
	    public URI getUri() {
	        return url;
	    }
	    
	    private static final Map<URI, CombinatorialDerivationStrategy> lookup = new HashMap<>();
		  
	    static
	    {
	        for(CombinatorialDerivationStrategy strategy : CombinatorialDerivationStrategy.values())
	        {
	            lookup.put(strategy.getUri(), strategy);
	        }
	    }
	    
	    /**
	     * Gets the combinational derivation strategy from the supplied URI.
	     * @param uri The URI associated with the combinational derivation strategy.
	     * @return The combinational derivation strategy object associated with the supplied URI.
	     */
	    public static CombinatorialDerivationStrategy get(URI uri) 
	    {
	        return lookup.get(uri);
	    }
}


