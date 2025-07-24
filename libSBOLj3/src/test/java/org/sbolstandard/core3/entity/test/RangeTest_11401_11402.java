package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class RangeTest_11401_11402 extends TestCase {
	
	public void testRange() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUri())); 
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		
		SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, term_na);
		
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=device.createSubComponent(term);
		termSubComponent.setOrientation(Orientation.inline);
		
		TestUtil.validateIdentified(device, 0);
		
		//Sequence seqStart = SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, "cca");
		
		Range startRange=termSubComponent.createSourceRange(1, 3, term.getSequences().get(0));
		TestUtil.validateIdentified(device, 0);
		
		//RANGE_START_VALID_FOR_SEQUENCE
		Range startRange2=termSubComponent.createSourceRange(100, 3, term.getSequences().get(0));
		TestUtil.validateIdentified(device, 2);
		
		//RANGE_END_VALID_FOR_SEQUENCE
		startRange2.setEnd(Optional.of(200));
		TestUtil.validateIdentified(device, 2);
		
		startRange2.setStart(Optional.of(1));
		startRange2.setEnd(Optional.of(20));
		TestUtil.validateIdentified(device, 0);
		
		//Test invalid range start and end
		Component device2=doc.createComponent("i135042", Arrays.asList(ComponentType.DNA.getUri()));
		device2.setRoles(Arrays.asList(Role.EngineeredGene));		
		SBOLAPI.addSequence(doc, device2, Encoding.NucleicAcid, term_na);
		SequenceFeature sf=device2.createSequenceFeature(200, 300, device2.getSequences().get(0));
		TestUtil.validateIdentified(device2, 2, "sbol3-11401,sbol3-11402");
		String elements=device2.getSequences().get(0).getElements();
		device2.getSequences().get(0).setElements(null);
		Configuration.getInstance().setCompleteDocument(false);
		TestUtil.validateIdentified(device2, 0);
		Configuration.getInstance().setCompleteDocument(true);
		TestUtil.validateIdentified(device2, 2,"sbol3-11401,sbol3-11402");
		device2.getSequences().get(0).setElements(elements);
		((Range)sf.getLocations().get(0)).setStart(Optional.of(1));
        ((Range)sf.getLocations().get(0)).setEnd(Optional.of(3));		
		TestUtil.validateIdentified(device2, 0);
		Configuration.getInstance().setCompleteDocument(false);
		
		
		
		
		
		
		
		
		

    }
}
