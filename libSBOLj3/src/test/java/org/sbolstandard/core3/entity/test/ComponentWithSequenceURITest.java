package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.Role;
import junit.framework.TestCase;

public class ComponentWithSequenceURITest extends TestCase {
	
	public void testComponent() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
	    popsReceiver.setSequences(URI.create("http://example.org/sequence1"));
	    		
        TestUtil.validateIdentifiedOnly(doc,popsReceiver, 0, null, null);
        
    }
}
