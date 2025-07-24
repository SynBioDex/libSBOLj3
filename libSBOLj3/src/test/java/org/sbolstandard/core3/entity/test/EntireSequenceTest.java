package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Cut;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Role;
import junit.framework.TestCase;

public class EntireSequenceTest extends TestCase {
	
	public void testEntireSequence() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component device=SBOLAPI.createDnaComponent(doc, "Device", "Device", "Device", Role.EngineeredGene, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcacgggaaaccc");
	    
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    //Sequence sequence=doc.getSequences().get(0);
		
	    SubComponent pTetRSC=device.createSubComponent(pTetR);
	    pTetR.getSequences().get(0).setElements(null);
	    pTetRSC.createEntireSequence(pTetR.getSequences().get(0));
	    TestUtil.validateIdentified(pTetRSC, 0);
	    
	    
    	
    }
}
