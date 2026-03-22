package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class SubComponentTest_10807_3 extends TestCase {
	
	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        String plasmid_na="aacgatgatgctcactctcgggtaccagcattttcggaggttctctaacagtatggataaccgtgttttcactgtgctgcggttacccatcgcctgaaatccagttggtgtcaagccattccctgtctaggacgccgcatgtagtaaaacatatacattgctcgggttcggtctcgggagttgacggctagctcagtcctaggtacagtgctagctacttgagacctataaacgccaggttgtatccgcatttgatgctaccatggatgagtcagcgtcgagcacgcggcatttattgcatgagtagggttgactaagaaccgttagatgcctcgctgtactaataattgtcaacagatcgtcaagattagaaaatagggtttagtccggcaatacttccggcaaaaaagggcaaggtgtcaccaccctgccctttttctttaaaaccgaaaagattacttcgcgttatgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatacggttatccacagaatcaggggataacgcaggaaagaacatgtgagcaaaaggccagcaaaaggccaggaaccgtaaaaaggccgcgttgctggcgtttttccacaggctccgcccccctgacgagcatcacaaaaatcgacgctcaagtcagaggtggcgaaacccgacaggactataaagataccaggcgtttccccctggaagctccctcgtgcgctctcctgttccgaccctgccgcttaccggatacctgtccgcctttctcccttcgggaagcgtggcgctttctcatagctcacgctgtaggtatctcagttcggtgtaggtcgttcgctccaagctgggctgtgtgcacgaaccccccgttcagcccgaccgctgcgccttatccggtaactatcgtcttgagtccaacccggtaagacacgacttatcgccactggcagcagccactggtaacaggattagcagagcgaggtatgtaggcggtgctacagagttcttgaagtggtggcctaactacggctacactagaagaacagtatttggtatctgcgctctgctgaagccagttaccttcggaaaaagagttggtagctcttgatccggcaaacaaaccaccgctggtagcggtggtttttttgtttgcaagcagcagattacgcgcagaaaaaaaggatctcaagaagatcctttgatcttttctacggggtctgacgctcagtggaacgaaaactcacgttaagggattttggtcatgagattatcaaaaaggatcttcacctagatccttttaaattaaaaatgaagttttaaatcaatctaaagtatatatgagtaaacttggtctgacagctcgaggcttggattctcaccaataaaaaacgcccggcggcaaccgagcgttctgaacaaatccagatggagttctgaggtcattactggatctatcaacaggagtccaagcgagctcgatatcaaattacgccccgccctgccactcatcgcagtactgttgtaattcattaagcattctgccgacatggaagccatcacaaacggcatgatgaacctgaatcgccagcggcatcagcaccttgtcgccttgcgtataatatttgcccatggtgaaaacgggggcgaagaagttgtccatattggccacgtttaaatcaaaactggtgaaactcacccagggattggctgagacgaaaaacatattctcaataaaccctttagggaaataggccaggttttcaccgtaacacgccacatcttgcgaatatatgtgtagaaactgccggaaatcgtcgtggtattcactccagagcgatgaaaacgtttcagtttgctcatggaaaacggtgtaacaagggtgaacactatcccatatcaccagctcaccgtctttcattgccatacgaaattccggatgagcattcatcaggcgggcaagaatgtgaataaaggccggataaaacttgtgcttatttttctttacggtctttaaaaaggccgtaatatccagctgaacggtctggttataggtacattgagcaactgactgaaatgcctcaaaatgttctttacgatgccattgggatatatcaacggtggtatatccagtgatttttttctccattttagcttccttagctcctgaaaatctcgataactcaaaaaatacgcccggtagtgatcttatttcattatggtgaaagttggaacctcttacgtgcccgatcaactcgagtgccacctgacgtctaagaaaccattattatcatgacattaacctataaaaataggcgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctg";
        Component plasmid= SBOLAPI.createDnaComponent(doc, "plasmid", null, null, Role.EngineeredGene, plasmid_na);
		
        Component RNAPbinding= SBOLAPI.createDnaComponent(doc, "BBa_J23100_RNAPbinding", null, null, Role.Promoter, "ttgacctagc");
		
	    SubComponent RNAPbindingSC = plasmid.createSubComponent(RNAPbinding);
	    RNAPbindingSC.createRange(181,185, plasmid.getSequences().get(0));
	    Range range2=RNAPbindingSC.createRange(210,213, plasmid.getSequences().get(0));
	    
	    TestUtil.validateIdentifiedAndDocument(RNAPbindingSC,doc,1,"sbol3-10807", "Invalid");
	    range2.setEnd(Optional.of(214));
	    TestUtil.validateIdentifiedAndDocument(RNAPbindingSC,doc,0, null, null);
	    String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
	    System.out.println(output);
	   	    
	    range2.setSequence(RNAPbinding.getSequences().get(0));
	    TestUtil.validateIdentifiedAndDocument(RNAPbindingSC,doc,2,3, "sbol3-11302,sbol3-11402", "RangeSequenceNotFromComponent");	   
    }

}
