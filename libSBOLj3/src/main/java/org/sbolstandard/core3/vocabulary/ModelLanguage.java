package org.sbolstandard.core3.vocabulary;

import java.net.URI;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents the model language used in the SBOL data model.
 *
 */
public class ModelLanguage {
	public static URI SBML = URINameSpace.EDAM.local("format_2585");
	public static URI CellML = URINameSpace.EDAM.local("format_3240");
	public static URI BioPAX = URINameSpace.EDAM.local("format_3156");
}