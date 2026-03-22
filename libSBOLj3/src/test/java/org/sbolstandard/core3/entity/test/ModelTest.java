package org.sbolstandard.core3.entity.test;

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

public class ModelTest extends TestCase {
	
	public void testInterface() throws SBOLGraphException, IOException, Exception
    {
		String namespace="https://sbolstandard.org";
		String baseUri=namespace + "/examples/";
        
		SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        Component toggleSwitch=SBOLAPI.createComponent(doc, "toggle_switch", ComponentType.FunctionalEntity.getUri(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        Model model=doc.createModel("model1", URI.create("http://virtualparts.org"), ModelFramework.Continuous,ModelLanguage.SBML);
        model.setNamespace(URI.create(namespace));
        toggleSwitch.setModels(Arrays.asList(model));
        
        TestUtil.serialise(doc, "entity/model", "model");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
        TestUtil.validateIdentifiedAndDocument(model,doc,0, null, null);
		
        TestUtil.validateProperty(model, "setSource", new Object[] {null}, URI.class);
        model.setSource(null);
		TestUtil.validateIdentifiedAndDocument(model,doc,1, "sbol3-12501", "InvalidSource");
		
		TestUtil.validateProperty(model, "setFramework", new Object[] {null}, URI.class);
		model.setFramework(null);
		TestUtil.validateIdentifiedAndDocument(model,doc,2, "sbol3-12501,sbol3-12505", "InvalidFramework");
		
		TestUtil.validateProperty(model, "setLanguage", new Object[] {null}, URI.class);        
		model.setLanguage(null);
		TestUtil.validateIdentifiedAndDocument(model,doc,3, "sbol3-12501,sbol3-12502,sbol3-12505", "InvalidLanguage");
    }

}
