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

public class SubComponentTest_10807_2 extends TestCase {
	
	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        String plasmid_na="aacgatgatgctcactctcgggtaccagcattttcggaggttctctaacagtatggataaccgtgttttcactgtgctgcggttacccatcgcctgaaatccagttggtgtcaagccattccctgtctaggacgccgcatgtagtaaaacatatacattgctcgggttcggtctcgggagttgacggctagctcagtcctaggtacagtgctagctacttgagacctataaacgccaggttgtatccgcatttgatgctaccatggatgagtcagcgtcgagcacgcggcatttattgcatgagtagggttgactaagaaccgttagatgcctcgctgtactaataattgtcaacagatcgtcaagattagaaaatagggtttagtccggcaatacttccggcaaaaaagggcaaggtgtcaccaccctgccctttttctttaaaaccgaaaagattacttcgcgttatgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatacggttatccacagaatcaggggataacgcaggaaagaacatgtgagcaaaaggccagcaaaaggccaggaaccgtaaaaaggccgcgttgctggcgtttttccacaggctccgcccccctgacgagcatcacaaaaatcgacgctcaagtcagaggtggcgaaacccgacaggactataaagataccaggcgtttccccctggaagctccctcgtgcgctctcctgttccgaccctgccgcttaccggatacctgtccgcctttctcccttcgggaagcgtggcgctttctcatagctcacgctgtaggtatctcagttcggtgtaggtcgttcgctccaagctgggctgtgtgcacgaaccccccgttcagcccgaccgctgcgccttatccggtaactatcgtcttgagtccaacccggtaagacacgacttatcgccactggcagcagccactggtaacaggattagcagagcgaggtatgtaggcggtgctacagagttcttgaagtggtggcctaactacggctacactagaagaacagtatttggtatctgcgctctgctgaagccagttaccttcggaaaaagagttggtagctcttgatccggcaaacaaaccaccgctggtagcggtggtttttttgtttgcaagcagcagattacgcgcagaaaaaaaggatctcaagaagatcctttgatcttttctacggggtctgacgctcagtggaacgaaaactcacgttaagggattttggtcatgagattatcaaaaaggatcttcacctagatccttttaaattaaaaatgaagttttaaatcaatctaaagtatatatgagtaaacttggtctgacagctcgaggcttggattctcaccaataaaaaacgcccggcggcaaccgagcgttctgaacaaatccagatggagttctgaggtcattactggatctatcaacaggagtccaagcgagctcgatatcaaattacgccccgccctgccactcatcgcagtactgttgtaattcattaagcattctgccgacatggaagccatcacaaacggcatgatgaacctgaatcgccagcggcatcagcaccttgtcgccttgcgtataatatttgcccatggtgaaaacgggggcgaagaagttgtccatattggccacgtttaaatcaaaactggtgaaactcacccagggattggctgagacgaaaaacatattctcaataaaccctttagggaaataggccaggttttcaccgtaacacgccacatcttgcgaatatatgtgtagaaactgccggaaatcgtcgtggtattcactccagagcgatgaaaacgtttcagtttgctcatggaaaacggtgtaacaagggtgaacactatcccatatcaccagctcaccgtctttcattgccatacgaaattccggatgagcattcatcaggcgggcaagaatgtgaataaaggccggataaaacttgtgcttatttttctttacggtctttaaaaaggccgtaatatccagctgaacggtctggttataggtacattgagcaactgactgaaatgcctcaaaatgttctttacgatgccattgggatatatcaacggtggtatatccagtgatttttttctccattttagcttccttagctcctgaaaatctcgataactcaaaaaatacgcccggtagtgatcttatttcattatggtgaaagttggaacctcttacgtgcccgatcaactcgagtgccacctgacgtctaagaaaccattattatcatgacattaacctataaaaataggcgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctg";
        Component plasmid= SBOLAPI.createDnaComponent(doc, "plasmid", null, null, Role.EngineeredGene, plasmid_na);
		
        String insert_na="ttgacggctagctcagtcctaggtacagtgctagc";
        Component insert= SBOLAPI.createDnaComponent(doc, "insert", null, null, Role.Promoter, insert_na);
	     
        SubComponent insertSC = plasmid.createSubComponent(insert);
        Range range=insertSC.createRange(181,214, plasmid.getSequences().get(0));
        
        TestUtil.validateIdentifiedAndDocument(insertSC,doc,1, "sbol3-10807", "SubComponentTest_10807_2");
        
        range.setEnd(Optional.of(215));
        TestUtil.validateIdentifiedAndDocument(insertSC,doc,0, null, null);
         
        insert.getSequences().get(0).setElements(null);
        range.setEnd(Optional.of(214));
        TestUtil.validateIdentifiedAndDocument(insertSC,doc,0, null, null);
        
        
	   // String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
	   // System.out.println(output);
	   
	    
	    
	    //Anderson_Promoters_in_vector_ins_BBa_J23100/SubComponent5/Range1
	    
	    //<https://synbiohub.org/public/igem/BBa_J23100
	    //Anderson_Promoters_in_vector_ins_BBa_J23100/
    }

}
