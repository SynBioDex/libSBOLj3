package org.sbolstandard.core3.vocabulary;

import java.net.URI;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents roles in the SBOL data model.
 *
 */
public class Role {
	
		public static URI Promoter = URINameSpace.SO.local("0000167");
		public static URI RBS = URINameSpace.SO.local("0000139");
		public static URI CDS = URINameSpace.SO.local("0000316");
		public static URI Terminator = URINameSpace.SO.local("0000141");
		public static URI Gene = URINameSpace.SO.local("0000704");
		public static URI Operator = URINameSpace.SO.local("0000057");
		public static URI EngineeredGene = URINameSpace.SO.local("0000704");
		public static URI EngineeredRegion = URINameSpace.SO.local("0000804");
		public static URI SequenceFeature = URINameSpace.SO.local("0000110");
		
		public static URI mRNA = URINameSpace.SO.local("0000234");
		public static URI Effector = URINameSpace.CHEBI.local("35224");
		public static URI TF = URINameSpace.GO.local("0003700");
		public static URI FunctionalCompartment = URINameSpace.SBO.local("0000289");
		public static URI PhysicalCompartment = URINameSpace.SBO.local("0000290");
		
		public static URI[] SequenceFeatures= {Promoter, RBS, CDS, Terminator, Gene, Operator, EngineeredGene, EngineeredRegion, SequenceFeature, mRNA};
		
		
		
		
		
}

