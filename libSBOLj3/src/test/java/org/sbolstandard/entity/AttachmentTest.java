package org.sbolstandard.entity;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class AttachmentTest extends TestCase {
	
	public void testImplementation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "TetR_protein"), ComponentType.Protein.getUrl(), "TetR", "TetR_protein", "TetR protein", Role.TF);
        Attachment attachment=doc.createAttachment(SBOLAPI.append(baseUri, "attachment1"), URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(1000);
        attachment.setHash("aaa");
        attachment.setHashAlgorithm("Alg1");
        
        
        Implementation impl=doc.createImplementation(SBOLAPI.append(baseUri,"impl1"));
        impl.setComponent(TetR_protein.getUri());
        impl.setAttachments(Arrays.asList(attachment.getUri()));
        
        TestUtil.serialise(doc, "entity/implementation", "implementation");
      
        System.out.println(SBOLWriter.write(doc, "Turtle"));
    }

}