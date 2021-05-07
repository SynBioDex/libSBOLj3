package org.sbolstandard.usecase;

import java.io.IOException;
import java.net.URI;
import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.SubComponent;
import org.sbolstandard.io.SBOLFormat;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.RestrictionType;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class MulticellularSimple extends TestCase {

	public void testApp() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component multicellularSystem=SBOLAPI.createComponent(doc, "MulticellularSystem", ComponentType.FunctionalEntity.getUrl(), "MulticellularSystem", "Multicellular System", Role.FunctionalCompartment);
        Component senderSystem=SBOLAPI.createComponent(doc, "SenderSystem", ComponentType.FunctionalEntity.getUrl(), "SenderSystem", "Sender System", Role.FunctionalCompartment);
        Component receiverSystem=SBOLAPI.createComponent(doc, "ReceiverSystem", ComponentType.FunctionalEntity.getUrl(), "ReceiverSystem", "Receiver System", Role.FunctionalCompartment);
        Component senderCell=SBOLAPI.createComponent(doc, "OrganismA", ComponentType.Cell.getUrl(), "OrganismA","Organism A", Role.PhysicalCompartment);
        Component receiverCell=SBOLAPI.createComponent(doc, "OrganismB", ComponentType.Cell.getUrl(), "OrganismB", "Organism B", Role.PhysicalCompartment);
        Component ahl=SBOLAPI.createComponent(doc, "AHL", ComponentType.SimpleChemical.getUrl(), "AHL", "AHL", Role.Effector);
       
        SBOLAPI.createConstraint(senderSystem, senderCell, ahl, RestrictionType.Topology.contains);
        SBOLAPI.createConstraint(receiverSystem, receiverCell, ahl, RestrictionType.Topology.contains);       
        
        SubComponent senderSubComponent=SBOLAPI.addSubComponent(multicellularSystem, senderSystem);
        SubComponent receiverSubComponent=SBOLAPI.addSubComponent(multicellularSystem, receiverSystem);
        SBOLAPI.mapTo(multicellularSystem, senderSystem, ahl, receiverSystem,ahl);
                
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.print(output);
        
        SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
        output=SBOLIO.write(doc2, SBOLFormat.RDFXML);
        System.out.print(output);
        
        TestUtil.serialise(doc2, "multicellular_simple", "multicellular_simple");     
        System.out.println("done");   
        TestUtil.assertReadWrite(doc);
    }
	 
	
	
	
	
	
    
}
