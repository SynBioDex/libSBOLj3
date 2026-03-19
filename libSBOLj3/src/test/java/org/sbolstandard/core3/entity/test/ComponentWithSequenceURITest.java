package org.sbolstandard.core3.entity.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.SBOLValidator;
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
        /*
        
        SBOLDocument doc=SBOLIO.read(new File("/Users/goksel/Downloads/PoPSReceiver.ttl"), SBOLFormat.TURTLE);
        List<String> messages=SBOLValidator.getValidator().validate(doc);
        if (messages!=null && messages.size()>0)
        {
        	System.out.print(messages.size());
        	System.out.print(messages.get(0));
        }
        TestUtil.validateDocument(doc,0);
		*/
        /*ComponentType[] values=ComponentType.values();
        System.out.println(values.length);
        OptionalComponentType[] optionalValues2=ComponentType.OptionalComponentType.values();
        System.out.println(optionalValues2.length);*/
        
        
    }
}
