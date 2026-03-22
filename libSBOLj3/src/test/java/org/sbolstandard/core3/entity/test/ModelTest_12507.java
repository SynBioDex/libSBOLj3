package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class ModelTest_12507 extends TestCase {
	
	public void testInterface() throws SBOLGraphException, IOException, Exception
    {
		String namespace="https://sbolstandard.org";
		String baseUri=namespace + "/examples/";
        
		SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        Component toggleSwitch=SBOLAPI.createComponent(doc, "toggle_switch", ComponentType.FunctionalEntity.getUri(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        Model model=doc.createModel("model1", URI.create("http://virtualparts.org"), ModelFramework.Continuous,ModelLanguage.SBML);
        model.setNamespace(URI.create(namespace));
        toggleSwitch.setModels(Arrays.asList(model));
                        
        TestUtil.validateIdentifiedAndDocument(model,doc,0, null, null);
        
        model.setFramework(URI.create("http://invalidmodelframework.org"));		
     	TestUtil.validateIdentifiedAndDocument(model,doc,1,"sbol3-12507", "InvalidFramework");
     	
     	model.setFramework(ModelFramework.Logical);		
     	TestUtil.validateIdentifiedAndDocument(model,doc,0, null, null);     	
    }

}
