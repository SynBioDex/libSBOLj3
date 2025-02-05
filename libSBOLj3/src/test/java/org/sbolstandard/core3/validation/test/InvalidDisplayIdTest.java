package org.sbolstandard.core3.validation.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;

import junit.framework.TestCase;

public class InvalidDisplayIdTest extends TestCase {

	public void testCreateInvalidDisplayId() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
       
        Configuration.getInstance().setValidateBeforeSaving(false);
        Configuration.getInstance().setValidateAfterSettingProperties(false);
 	    
        Component TetR_protein=SBOLAPI.createComponent(doc, "TetR_protein", ComponentType.Protein.getUri(), "TetR", "TetR protein", Role.TF);
        TetR_protein.setDisplayId("1TetR");
        //Component LacI_protein=SBOLAPI.createComponent(doc, "LacI_protein", ComponentType.Protein.getUrl(), "LacI", "LacI protein", Role.TF);
        TetR_protein.setNamespace(URI.create("https://sbolstandard.org/examples/"));
        
        Component rbs=doc.createComponent(URI.create("https://sbolstandard.org/examples/rbs"), URI.create("https://sbolstandard.org/examples"), Arrays.asList(ComponentType.DNA.getUri())); 
		rbs.setName("B0034");
		rbs.setDisplayId("B0034 rbs");
		rbs.setDescription("RBS (Elowitz 1999)");
		rbs.setRoles(Arrays.asList(Role.RBS));
		TestUtil.serialise(doc, "invalid/displayid", "displayid");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));  
    }
}

	