package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.OptionalLong;
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

public class TopLevelReferenceTest extends TestCase {
	
	public void testTopLevel() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
       
	  	Component compWithSeqURI=SBOLAPI.createDnaComponent(doc, "pLacI", "pLacI", "LacI repressible promoter2", Role.Promoter, null);
	  	compWithSeqURI.addSequence(URI.create("https://sbolstandard.org/sequences/sequence1"));
	  	TestUtil.validateDocument(doc,1);
	  				
    }

}
