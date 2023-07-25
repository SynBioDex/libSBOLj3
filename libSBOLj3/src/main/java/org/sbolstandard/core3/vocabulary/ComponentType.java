package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sbolstandard.core3.util.URINameSpace;

interface HasURI{
    URI getUri();
    
    /*static Map<URI, HasURI> lookup = new HashMap<>();
    static <T extends HasURI> T get(URI uri)
    {
    	return (T) lookup.get(uri);
    }*/
}

/**
 * 
 * Represents a component type in the SBOL data model.
 *
 */
public enum ComponentType implements HasURI{
	DNA(URINameSpace.SBO.local("0000251")), 
	RNA(URINameSpace.SBO.local("0000250")), 
	Protein(URINameSpace.SBO.local("0000252")), 
	SimpleChemical(URINameSpace.SBO.local("0000247")), 
	NoncovalentComplex(URINameSpace.SBO.local("0000253")),
	FunctionalEntity(URINameSpace.SBO.local("0000241"));

	private URI uri;

	ComponentType(URI uri) {
		this.uri = uri;
	}
	
	/**
	 * Returns the URI for the associated ComponentType object.
	 */
	public URI getUri() {
		return uri;
	}

	private static final Map<URI, ComponentType> lookup = new HashMap<>();

	static {
		for (ComponentType value : ComponentType.values()) {
			lookup.put(value.getUri(), value);
		}
	}
	
	/**
	 * Returns a ComponentType object for the supplied URL
	 * @param url The URL identifying the component type.
	 * @return A ComponentType object for the specified URL.
	 */
	public static ComponentType get(URI url) {
		return lookup.get(url);
	}

	// hardcoded list of matches from table 2 to table 1
	/**
	 * Checks the component type and retrieves it's associated encoding.
	 * @param type The type to be matched.
	 * @return A list object containing the encoding data for the associated ComponentType object.
	 */
	public static List<Encoding> checkComponentTypeMatch(ComponentType type) {
		switch (type) {
		case DNA:
			return Arrays.asList(Encoding.NucleicAcid);
		case RNA:
			return Arrays.asList(Encoding.NucleicAcid);
		case Protein:
			return Arrays.asList(Encoding.AminoAcid);
		case SimpleChemical:
			return Arrays.asList(Encoding.INCHI, Encoding.SMILES);
		default:
			return null;
		}
	}
	
	/**
	 * 
	 * Represents an optional component type in the SBOL data model.
	 *
	 */
	public enum OptionalComponentType implements HasURI {
		Cell(URINameSpace.GO.local("0005623"));

		private URI uri;

		OptionalComponentType(URI uri) {
			this.uri = uri;
		}
		
		/**
		 * Returns the URI for the associated OptionalComponentType object.
		 */
		public URI getUri() {
			return uri;
		}
	}
	
	/**
	 * 
	 * Represents an optional component type in the SBOL data model.
	 *
	 */
	public enum TopologyType implements HasURI {
		Circular(URINameSpace.SO.local("0000988")),
		Linear(URINameSpace.SO.local("0000987"));
		
		private URI uri;

		TopologyType(URI uri) {
			this.uri = uri;
		}
		
		/**
		 * Returns the URI for the associated TopologyType object.
		 */
		public URI getUri() {
			return uri;
		}
		
		private static final Map<URI, TopologyType> lookup = new HashMap<>();

		static {
			for (TopologyType value : TopologyType.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		/**
		 * Returns the topology type from supplied URI
		 * @param uri The URI of the topology type.
		 * @return The associated TopologyType object.
		 */
		public static TopologyType get(URI uri) {
			return lookup.get(uri);
		}
	}
	
	/**
	 * 
	 * Represents a strand type in the SBOL data model.
	 *
	 */
	public enum StrandType implements HasURI {
		Single(URINameSpace.SO.local("0000984")),
		Double(URINameSpace.SO.local("0000985"));
		
		private URI uri;

		StrandType(URI uri) {
			this.uri = uri;
		}
		
		/**
		 * Returns the URI for the associated StrandType object.
		 */
		public URI getUri() {
			return uri;
		}
		
		private static final Map<URI, StrandType> lookup = new HashMap<>();

		static {
			for (StrandType value : StrandType.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		/**
		 * Returns a StrandType 
		 * @param uri The URI of the strand type.
		 * @return The associated StrandType object.
		 */
		public static StrandType get(URI uri) {
			return lookup.get(uri);
		}
	}
}
