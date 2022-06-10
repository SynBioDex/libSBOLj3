package org.sbolstandard.core3.vocabulary;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.core3.util.URINameSpace;
public enum ComponentType
{
	 	DNA(URINameSpace.SBO.local("0000251")), 
	 	RNA(URINameSpace.SBO.local("0000250")),
	 	Protein(URINameSpace.SBO.local("0000252")),
	 	SimpleChemical(URINameSpace.SBO.local("0000247")),
	 	NoncovalentComplex(URINameSpace.SBO.local("0000253")),
	 	FunctionalEntity(URINameSpace.SBO.local("0000241")),
		Cell(URINameSpace.GO.local("0005623"));
	
	 
	    private URI url;
	 
	    ComponentType(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
	    
	    private static final Map<URI, ComponentType> lookup = new HashMap<>();
	    
		static {
			for(ComponentType value : ComponentType.values())
	        {
	            lookup.put(value.getUrl(), value);
	        }
		}
		  
	    public static ComponentType get(URI url) 
	    {
	        return lookup.get(url);
	    }
}


