package org.sbolstandard.core3.entity.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.provenance.Plan;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import junit.framework.TestCase;

public class TempTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		/*String baseUri="https://sbolstandard.org/examples/";
        Configuration.getInstance().setValidateRecommendedRules(false);
        SBOLDocument doc = SBOLIO.read(new File("/Users/goksel/Downloads/Provenance_SpecifyCutOperation2.xml_sbol3.xml"), SBOLFormat.RDFXML);
        String stringRep=SBOLIO.write(doc, SBOLFormat.RDFXML);
        System.out.println(stringRep);
        */
        
        //TestUtil.assertReadWrite(doc);
    }

}
