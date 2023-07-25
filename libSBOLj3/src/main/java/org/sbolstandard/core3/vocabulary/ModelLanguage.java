package org.sbolstandard.core3.vocabulary;

import java.net.URI;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents the model languages used in the SBOL data model.
 *
 */
public class ModelLanguage {
	/**
	 * The URI identifying the SMBL model language.
	 */
	public static URI SBML = URINameSpace.EDAM.local("format_2585");
	/**
	 * The URI identifying the CellML model language.
	 */
	public static URI CellML = URINameSpace.EDAM.local("format_3240");
	/**
	 * The URI identifying the BioPAX model language.
	 */
	public static URI BioPAX = URINameSpace.EDAM.local("format_3156");
}