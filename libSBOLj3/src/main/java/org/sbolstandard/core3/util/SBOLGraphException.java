package org.sbolstandard.core3.util;

/**
 * 
 * Represents the exception thrown as a result of a graphing error in the SBOL data model.
 *
 */
public class SBOLGraphException extends Exception {

	private static final long serialVersionUID = -523239991112483227L;
	
	/**
	 * Generates an exception object.
	 * @param message The message to be included in the thrown exception.
	 */
	public SBOLGraphException(String message) {
		super(message);
	}
	
	/**
	 * Generates an exception object.
	 * @param message The message to be included in the thrown exception.
	 * @param e The exception object thrown.
	 */
	public SBOLGraphException(String message, Exception e) {
		super(message,e);
	}

}
