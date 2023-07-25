package org.sbolstandard.core3.vocabulary;

import java.net.URI;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents the provenance data model in the SBOL data model.
 *
 */
public class ProvenanceDataModel {
	
	/**
	 * 
	 * Constructs an object for an Agent with the corresponding URI.
	 *
	 */
	public static final class Agent
	{
		public static URI uri=URINameSpace.PROV.local("Agent");
	}
	
	/**
	 * 
	 * Constructs an object for an Association with the corresponding URIs.
	 *
	 */
	public static final class Association 
	{	
		public static URI uri=URINameSpace.PROV.local("Association");
		public static URI role=URINameSpace.PROV.local("hadRole");
		public static URI agent=URINameSpace.PROV.local("agent");
		public static URI plan=URINameSpace.PROV.local("hadPlan");
	}
	
	/**
	 * 
	 * Constructs an object for a Usage with it's corresponding URIs.
	 *
	 */
	public static final class Usage
	{
		public static URI uri=URINameSpace.PROV.local("Usage");
		public static URI role=URINameSpace.PROV.local("hadRole");
		public static URI entity=URINameSpace.PROV.local("entity");
		
	}
	
	/**
	 * 
	 * Constructs an object for an Activity with it's corresponding URIs.
	 *
	 */
	public static final class Activity
	{
		public static URI uri=URINameSpace.PROV.local("Activity");
		public static URI startedAtTime=URINameSpace.PROV.local("startedAtTime");
		public static URI endedAtTime=URINameSpace.PROV.local("endedAtTime");
		public static URI type=URINameSpace.SBOL.local("type");
		public static URI qualifiedUsage=URINameSpace.PROV.local("qualifiedUsage");
		public static URI qualifiedAssociation=URINameSpace.PROV.local("qualifiedAssociation");
		public static URI wasInformedBy=URINameSpace.PROV.local("wasInformedBy");
	}
	
	/**
	 * 
	 * Constructs an object for a Plan with it's corresponding URIs.
	 *
	 */
	public static final class Plan
	{
		public static URI uri=URINameSpace.PROV.local("Plan");
	}
		
}
