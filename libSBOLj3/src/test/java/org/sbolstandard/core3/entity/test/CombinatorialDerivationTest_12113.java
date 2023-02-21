package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class CombinatorialDerivationTest_12113 extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		Collection col=doc.createCollection("promoterlib");
		
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    Component pTetR2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_2", "pTetR2", "TetR repressible promoter", Role.Promoter, "accctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    
	    	    
	    Component start=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start", "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");	
	    SubComponent sc_start=pTetR.createSubComponent(start);	    
	    SequenceFeature pTetRSequenceFeature=pTetR.createSequenceFeature(pTetR.getSequences().get(0));
	    
	    Component start2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start2", "pTetR_start2", "promoter_start2", Role.EngineeredRegion, "accctat");	
	    SubComponent sc_start2=pTetR2.createSubComponent(start2);	    
	    SequenceFeature pTetRSequenceFeature2=pTetR2.createSequenceFeature(pTetR2.getSequences().get(0));
	    
	    CombinatorialDerivation cd=doc.createCombinatorialDerivation("cd1", pTetR);	   	    
	    pTetR2.setWasDerivedFrom(Arrays.asList(cd.getUri()));
	   
	    VariableFeature varFeature=cd.createVariableFeature(VariableFeatureCardinality.One, sc_start);
	    varFeature.setVariants(Arrays.asList(start2));
	    sc_start2.setWasDerivedFrom(Arrays.asList(sc_start.getUri()));
	    pTetRSequenceFeature2.setWasDerivedFrom(Arrays.asList(pTetRSequenceFeature.getUri()));
	    		  	    
	    TestUtil.validateDocument(doc, 0);
	    pTetR.createConstraint(RestrictionType.SequentialRestriction.contains, pTetRSequenceFeature, sc_start);
	    TestUtil.validateDocument(doc, 2, "sbol3-12113, sbol3-12113"); //Two errors due to a single missing constraint with a missing subject and missing object 
	    pTetR2.createConstraint(RestrictionType.SequentialRestriction.contains, sc_start2, pTetRSequenceFeature2 );//Subject and object are switched.
	    TestUtil.validateDocument(doc, 2, "sbol3-12113, sbol3-12113"); 
	    pTetR2.createConstraint(RestrictionType.SequentialRestriction.contains, pTetRSequenceFeature2, sc_start2 );
	    TestUtil.validateDocument(doc, 0); 
	    	     
	    
    }
}
