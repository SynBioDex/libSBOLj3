package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.OptionalLong;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class IdentifiedTest extends TestCase {
	
	public void testIdentified() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(OptionalLong.of(1000));
        attachment.setHashAlgorithm(HashAlgorithm.sha3_256);
        attachment.setHash("aaa");
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
        attachment.setDisplayId("test");
        TestUtil.validateIdentifiedAndDocument(attachment,doc,0, null, null);
        attachment.setDisplayId("1test");
        TestUtil.validateIdentifiedAndDocument(attachment,doc,1, "sbol3-10201", "attachmentInvalidDisplayId");
        attachment.setDisplayId("_test");
        TestUtil.validateIdentifiedAndDocument(attachment,doc,0, null, null);
        TestUtil.validateProperty(attachment, "setDisplayId", new Object[] {"!qq"}, String.class);
        
        Attachment attachment2=doc.createAttachment("2attachment", URI.create("https://sbolstandard.org/attachment2_source"));
        TestUtil.validateIdentifiedAndDocument(attachment2,doc,1, "sbol3-10201", "attachment2InvalidDisplayId");
        attachment2.setDisplayId("attachment2");
        TestUtil.validateIdentifiedAndDocument(attachment2,doc,0, null, null);
      
        //IDENTIFIED_CANNOT_BE_REFERREDBY_WASDERIVEDFROM
        attachment.setWasDerivedFrom(null);
        TestUtil.validateIdentifiedAndDocument(attachment,doc,0, null, null);
        attachment.setWasDerivedFrom(Arrays.asList(attachment.getUri()));
        TestUtil.validateIdentifiedAndDocument(attachment, doc, 1, "sbol3-10202", "attachmentWasDerivedFromInvalid");
        attachment.setWasDerivedFrom(null);
        TestUtil.validateIdentifiedAndDocument(attachment, doc, 0, null, null);       
        
        Resource resource = TestUtil.getResource(attachment);
        
		//SBOL_VALID_ENTITY_TYPES - Identified.wasGeenratedBy
		RDFUtil.setProperty(resource, DataModel.Identified.wasGeneratedBy, attachment.getUri());
		TestUtil.validateIdentifiedAndDocument(attachment, doc, 1, "sbol3-10111", "attachmentWasGeneratedByInvalid");
		attachment.setWasGeneratedBy(null);
		TestUtil.validateIdentifiedAndDocument(attachment, doc, 0, null, null);
		
		RDFUtil.setProperty(resource, DataModel.Identified.measure, attachment.getUri());
		TestUtil.validateIdentifiedAndDocument(attachment, doc, 1, "sbol3-10111", "attachmentMeasureInvalid");
		URI tmp=null;
		RDFUtil.setProperty(resource, DataModel.Identified.measure, tmp);
		TestUtil.validateIdentifiedAndDocument(attachment, doc, 0, null, null);
									       		
		//IDENTIFIED_SUITABLE_SBOL_ENTITY_TYPES
        //This will cause an invalid attachment and will create an invalid sequence. Two document errors - one attachment error.
        attachment.addAnnotationType(DataModel.Sequence.uri);
        TestUtil.validateIdentifiedAndDocument(attachment, doc, 1,2, "sbol3-10106", "attachmentInvalidType");
        
    }
}
