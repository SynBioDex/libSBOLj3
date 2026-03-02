package org.sbolstandard.core3.entity.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Collection;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class SBOLFileTypeTest extends TestCase {
	
	public void testFileType() throws SBOLGraphException, IOException
    {
		String testOutput = "testoutput"  + File.separator + "filetypetest";
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, "TetR_protein", ComponentType.Protein.getUri(), "TetR", "TetR protein", Role.TF);
        Component LacI_protein=SBOLAPI.createComponent(doc, "LacI_protein", ComponentType.Protein.getUri(), "LacI", "LacI protein", Role.TF);
      
        Collection col=doc.createCollection("col1");
                
        col.addMember(LacI_protein);
        col.addMember(TetR_protein);        
        SBOLIO.write(doc, new File(testOutput + "RDF.txt"), SBOLFormat.RDFXML); 
        SBOLIO.write(doc, new File(testOutput + "JSON.txt"), SBOLFormat.JSONLD); 
        SBOLIO.write(doc, new File(testOutput + "TURTLE.txt"), SBOLFormat.TURTLE); 
        SBOLIO.write(doc, new File(testOutput + "NTRIPLES.txt"), SBOLFormat.NTRIPLES); 
        
        //Read without the correct extension. This will use content based RDF type detection.
        doc=SBOLIO.read(new File(testOutput + "RDF.txt"));
        SBOLIO.write(doc, new File(testOutput + "RDF.txt.xml"), SBOLFormat.RDFXML); 
        //Read with the correct extension. This will use file extension based RDF type detection.
        doc=SBOLIO.read(new File(testOutput + "RDF.txt.xml"));

        SBOLIO.write(doc, new File(testOutput + ".sbol"), SBOLFormat.RDFXML);
        doc=SBOLIO.read(new File(testOutput + ".sbol"));
        
        SBOLIO.write(doc, new File(testOutput + "_no_extension"), SBOLFormat.RDFXML);
        doc=SBOLIO.read(new File(testOutput + "_no_extension"));

        SBOLIO.write(doc, new File(testOutput + ".randomextension"), SBOLFormat.RDFXML);
        doc=SBOLIO.read(new File(testOutput + ".randomextension"));

        doc=SBOLIO.read(new File(testOutput + "JSON.txt"));
        SBOLIO.write(doc, new File(testOutput + "JSON.txt.jsonld"), SBOLFormat.JSONLD); 
        doc=SBOLIO.read(new File(testOutput + "JSON.txt.jsonld"));
       
        doc=SBOLIO.read(new File(testOutput + "TURTLE.txt"));
        SBOLIO.write(doc, new File(testOutput + "TURTLE.txt.ttl"), SBOLFormat.TURTLE);
        doc=SBOLIO.read(new File(testOutput + "TURTLE.txt.ttl"));
       

        doc=SBOLIO.read(new File(testOutput + "NTRIPLES.txt"));
        SBOLIO.write(doc, new File(testOutput + "NTRIPLES.txt.nt"), SBOLFormat.NTRIPLES);
        doc=SBOLIO.read(new File(testOutput + "NTRIPLES.txt.nt"));    
        
        String output=SBOLIO.writeToString(doc, SBOLFormat.RDFXML);
        doc=SBOLIO.read(output);
        output=SBOLIO.writeToString(doc, SBOLFormat.TURTLE);
        System.out.println(output);        


    }
}
