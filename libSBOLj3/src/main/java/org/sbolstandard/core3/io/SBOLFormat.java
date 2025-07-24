package org.sbolstandard.core3.io;

import org.apache.jena.riot.RDFFormat;

/**
 * 
 * Represents the SBOL formatting of RDF resources in the SBOL data model.
 *
 */
public enum SBOLFormat
{
	 TURTLE(RDFFormat.TURTLE),
	 NTRIPLES (RDFFormat.NTRIPLES),
	 RDFXML(RDFFormat.RDFXML_ABBREV),
	 JSONLD (RDFFormat.JSONLD),
//	 JSONLD_FLAT("JSONLD_FLAT"),
	 JSONLD_EXPAND (RDFFormat.JSONLD_FLAT);
//	 JSONLD_EXPAND_FLAT ("JSONLD_EXPAND_FLAT");
		
	 private RDFFormat format;
	 
	    SBOLFormat(RDFFormat format) {
	        this.format = format;
	    }
	    
	    /**
	     * Gets the format associated with SBOL.
	     * @return An RDFFormat object of the corresponding type.
	     */
	    public RDFFormat getFormat() {
	        return format;
	    }
	   
}

