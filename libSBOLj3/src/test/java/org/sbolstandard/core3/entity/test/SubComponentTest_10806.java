package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class SubComponentTest_10806 extends TestCase {
	
	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        String gfp_na="atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagaccacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa";
		Component gfp=SBOLAPI.createDnaComponent(doc, "E0040", "E0040", null, Role.CDS, gfp_na);
		Sequence seq= gfp.getSequences().get(0);
		
		Component region=SBOLAPI.createDnaComponent(doc, "region", "region", "region", Role.EngineeredRegion, "atgcgtaaagga");
		Sequence seqRegion= region.getSequences().get(0);
		
		SubComponent feature=gfp.createSubComponent(region);
		feature.setOrientation(Orientation.inline);
		
		
		TestUtil.validateIdentified(feature,doc,0);

		/*Component test=SBOLAPI.createDnaComponent(doc, "test", "test", null, Role.CDS, "aaaaaa");
		feature.createRange(1, 3, test.getSequences().get(0));   
		 TestUtil.validateIdentified(feature,doc,1);*/
		 
		 
		
	    feature.createRange(1, 3, seq);   
	    feature.createRange(7, 9, seq);
	    feature.createRange(10, 12, seq);
	    
	    feature.createSourceRange(1, 3, seqRegion);
	    feature.createSourceRange(7, 12, seqRegion);
	    
	    //TestUtil.validateIdentified(feature,doc,1);
	    
	    //feature.createSourceRange(12, 12, seq);
	    TestUtil.validateIdentified(feature,doc,0);
	    
    }

}
