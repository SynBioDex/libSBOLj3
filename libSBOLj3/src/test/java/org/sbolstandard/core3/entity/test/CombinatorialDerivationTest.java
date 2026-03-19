package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class CombinatorialDerivationTest extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		Component start=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start", "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");
		
        SubComponent startFeature=pTetR.createSubComponent(start);
        SubComponent startCodonFeature=pTetR.createSubComponent(start);
        SubComponent endCodonFeature=pTetR.createSubComponent(start);
        
		CombinatorialDerivation cd=doc.createCombinatorialDerivation("cs1", pTetR);
		
		
		TestUtil.serialise(doc, "entity/combinatorialderivation", "combinatorialderivation");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
        TestUtil.validateIdentifiedAndDocument(cd, doc, 0);
        
        //template is required.
        Component tmpURI=cd.getTemplate();
        TestUtil.validateProperty(cd, "setTemplate", new Object[] {null}, Component.class);
        Component nullValue=null;
        cd.setTemplate(nullValue);
        TestUtil.validateIdentifiedAndDocument(cd, doc, 1);
        URI nullURI=null;
        cd.setTemplate(nullURI);
        TestUtil.validateIdentifiedAndDocument(cd, doc, 1);
        cd.setTemplate(tmpURI);
        TestUtil.validateIdentifiedAndDocument(cd,doc,0);
		
        
        //SBOL_VALID_ENTITY_TYPES CombinatorialDerivation.template
        Resource resource = TestUtil.getResource(cd);
        RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, Arrays.asList(cd.getUri()));
		TestUtil.validateIdentifiedAndDocument(cd,doc,1);
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, pTetR.getUri());
		TestUtil.validateIdentifiedAndDocument(cd,doc,0);
		
        
        
        VariableFeature vf=cd.createVariableFeature(VariableFeatureCardinality.One,startFeature);
        
        TestUtil.validateProperty(vf, "setCardinality", new Object[] {null}, VariableFeatureCardinality.class);
        vf.setCardinality(null);
        TestUtil.validateIdentifiedAndDocument(vf, doc, 1);
        
        TestUtil.validateProperty(vf, "setVariable", new Object[] {null}, Feature.class);
        vf.setVariable(null);
        TestUtil.validateDocument(doc, 3, "sbol3-12202", "CombinatorialDerivationTest_varFeatureVariableNull");
        
        vf.setCardinality(VariableFeatureCardinality.One);
        vf.setVariable(startFeature);
        TestUtil.validateIdentifiedAndDocument(vf, doc, 0);
        
        cd.setStrategy(CombinatorialDerivationStrategy.Enumerate);
        TestUtil.validateIdentifiedAndDocument(vf, doc, 0);
        
        TestUtil.validateReturnValue(cd, "toStrategy", new Object[] {URI.create("http://sbols.org/v3#InvalidStrategy")}, URI.class);
        
        cd.setStrategy(CombinatorialDerivationStrategy.Enumerate);
        vf.setCardinality(VariableFeatureCardinality.OneOrMore);
        TestUtil.validateIdentifiedAndDocument(cd, doc, 1);
        vf.setCardinality(VariableFeatureCardinality.One);
        TestUtil.validateIdentifiedAndDocument(cd, doc, 0);
        
        VariableFeature vf2=cd.createVariableFeature(VariableFeatureCardinality.One,startFeature);
        TestUtil.validateIdentifiedAndDocument(cd, doc, 1);
        VariableFeature vf4=cd.createVariableFeature(VariableFeatureCardinality.One,startCodonFeature);
        
        //SBOL_VALID_ENTITY_TYPES - VariableFeature.Variable
        //VARIABLEFEATURE_FEATURE_NOT_NULL
        Resource resvf4= TestUtil.getResource(vf4);
        RDFUtil.setProperty(resvf4, DataModel.VariableFeature.variable, startFeature.getUri());
        TestUtil.validateIdentifiedAndDocument(cd, doc, 2);
        
        //Clear the errors
        vf4.setVariable(startCodonFeature);
        vf2.setVariable(endCodonFeature);
        TestUtil.validateIdentifiedAndDocument(cd, doc, 0);
        
        TestUtil.validateReturnValue(vf4, "toCardinality", new Object[] {URI.create("http://invalidcardinality.org")}, URI.class);
        TestUtil.validateReturnValue(false, vf4, "toCardinality", new Object[] {VariableFeatureCardinality.One.getUri()}, URI.class);
        
        //SBOL_VALID_ENTITY_TYPES - VariableFeature.variantCollection
        Resource resvf= TestUtil.getResource(vf4);
        RDFUtil.setProperty(resvf, DataModel.VariableFeature.variantCollection, startCodonFeature.getUri());
        RDFUtil.setProperty(resvf, DataModel.VariableFeature.variantDerivation, startCodonFeature.getUri());
        RDFUtil.setProperty(resvf, DataModel.VariableFeature.variant, startCodonFeature.getUri());
        RDFUtil.setProperty(resvf, DataModel.VariableFeature.variantMeasure, startCodonFeature.getUri());
          
        
        TestUtil.validateIdentifiedAndDocument(cd, doc, 4);
        URI tmp=null;
        RDFUtil.setProperty(resvf, DataModel.VariableFeature.variantCollection, tmp);
        RDFUtil.setProperty(resvf, DataModel.VariableFeature.variantDerivation, tmp);
        RDFUtil.setProperty(resvf, DataModel.VariableFeature.variant, tmp);
        RDFUtil.setProperty(resvf, DataModel.VariableFeature.variantMeasure, tmp);
        
        TestUtil.validateIdentifiedAndDocument(cd, doc, 0);
    
    }

}
