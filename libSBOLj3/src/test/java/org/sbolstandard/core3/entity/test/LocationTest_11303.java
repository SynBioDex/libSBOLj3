package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class LocationTest_11303 extends TestCase {
	
	public void testRange() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="ccaggcatca";
		//String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		
		String term_na2="catcaa";
		String finalseq="ccaggatca";
		
		Component device=SBOLAPI.createDnaComponent(doc,"i13504", "device", "i13504 engineered gene", Role.EngineeredGene, finalseq); 
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		
		//Sequence seq_term_na=SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, term_na);
		
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=device.createSubComponent(term);
		termSubComponent.setOrientation(Orientation.inline);
		
		Component term2=SBOLAPI.createDnaComponent(doc, "term2", "terminator", " Term 2", Role.Terminator,term_na2);
		SubComponent termSubComponent2=device.createSubComponent(term2);
		termSubComponent2.setOrientation(Orientation.inline);
		
		TestUtil.validateIdentified(device, 0);
		Range term1Frag=termSubComponent.createSourceRange(2, 7, device.getSequences().get(0));
		
		//Range term1Frag=termSubComponent.createSourceRange(2, 7, term.getSequences().get(0));
		//Range term1Annotation=termSubComponent.createRange(1, 5, device.getSequences().get(0));
		TestUtil.validateIdentified(device, doc, 1, "sbol3-11303");
		term1Frag.setSequence(term.getSequences().get(0));
		TestUtil.validateIdentified(device, 0);
		Range term1Annotation=termSubComponent.createRange(1, 5, device.getSequences().get(0));
		TestUtil.validateIdentified(device, doc, 1, "sbol3-10806");
		term1Frag.setEnd(Optional.of(6));
		TestUtil.validateIdentified(device, 0);
		
		
		
		
		
		//Range term2Frag=termSubComponent2.createSourceRange(2, 4, term2.getSequences().get(0));
		
		
		//System.out.println(SBOLIO.write(doc, SBOLFormat.RDFXML));
		
		/*
		Sequence seqStart=doc.createSequence("start");
		seqStart.setElements("cca");
		seqStart.setEncoding(Encoding.NucleicAcid);
		
		Sequence seqStartSourceFromLongSequence=doc.createSequence("startSource");
		seqStart.setElements(term_na2);
		seqStart.setEncoding(Encoding.NucleicAcid);
		
		Range startRange=termSubComponent.createSourceRange(1, 10, seq_term_na);
		TestUtil.validateIdentified(device, 0);
		startRange.setSequence(device.getSequences().get(0));
		TestUtil.validateIdentified(device, 0);
		
		startRange.setSequence(seqStart);
		TestUtil.validateIdentified(device, 1);
		
		Sequence secondTermSeq=doc.createSequence("term_na2");
		secondTermSeq.setElements("ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgtt");
		secondTermSeq.setEncoding(Encoding.NucleicAcid);
		SequenceFeature seqFeature=device.createSequenceFeature(secondTermSeq);
		//Use a sequence that has been added to the parent (device) as an EntireSequence entity.
		startRange.setSequence(secondTermSeq);
		TestUtil.validateIdentified(device, 0);
		*/
		
		
	}
}
