package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class RangeTest_11403 extends TestCase {
	
	public void testRange() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="aggcat";
		String device_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUri())); 
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		
		SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, device_na);
		
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=device.createSubComponent(term);
		termSubComponent.setOrientation(Orientation.inline);
		
		TestUtil.validateIdentified(device, 0);
		
		//Sequence seqStart = SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, "cca");
		
		Range startRange=termSubComponent.createSourceRange(3, 6, term.getSequences().get(0));
		TestUtil.validateIdentified(device, 0);
		
		startRange.setEnd(Optional.of(3));
		TestUtil.validateIdentified(device, 0);
		
		startRange.setEnd(Optional.of(2));
		TestUtil.validateIdentified(device, 1);
		
		startRange.setEnd(Optional.of(4));
		TestUtil.validateIdentified(device, 0);
		
		
		
		
		
		
		
		
		
		
			

		
		
		
    }
}
