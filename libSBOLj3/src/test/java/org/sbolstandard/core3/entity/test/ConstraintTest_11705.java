package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import org.sbolstandard.core3.vocabulary.RestrictionType.OrientationRestriction;

import junit.framework.TestCase;

public class ConstraintTest_11705 extends TestCase {
	
	public void testConstraintReference() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component multicellularSystem=SBOLAPI.createComponent(doc, "MulticellularSystem", ComponentType.FunctionalEntity.getUri(), "MulticellularSystem", "Multicellular System", Role.FunctionalCompartment);
        Component senderSystem=SBOLAPI.createComponent(doc, "SenderSystem", ComponentType.FunctionalEntity.getUri(), "SenderSystem", "Sender System", Role.FunctionalCompartment);
        Component receiverSystem=SBOLAPI.createComponent(doc, "ReceiverSystem", ComponentType.FunctionalEntity.getUri(), "ReceiverSystem", "Receiver System", Role.FunctionalCompartment);
        Component senderCell=SBOLAPI.createComponent(doc, "OrganismA", ComponentType.OptionalComponentType.Cell.getUri(), "OrganismA","Organism A", Role.PhysicalCompartment);
        Component receiverCell=SBOLAPI.createComponent(doc, "OrganismB", ComponentType.OptionalComponentType.Cell.getUri(), "OrganismB", "Organism B", Role.PhysicalCompartment);
        Component ahl=SBOLAPI.createComponent(doc, "AHL", ComponentType.SimpleChemical.getUri(), "AHL", "AHL", Role.Effector);
        Component nonAHL=SBOLAPI.createComponent(doc, "nonAHL", ComponentType.SimpleChemical.getUri(), "nonAHL", "nonAHL", Role.Effector);
        
        SBOLAPI.createConstraint(senderSystem, senderCell, ahl, RestrictionType.TopologyRestriction.contains.getUri());
        SBOLAPI.createConstraint(receiverSystem, receiverCell, ahl, RestrictionType.TopologyRestriction.contains.getUri());       
        
        SubComponent senderSubComponent=SBOLAPI.addSubComponent(multicellularSystem, senderSystem);
        SubComponent receiverSubComponent=SBOLAPI.addSubComponent(multicellularSystem, receiverSystem);
        
        int errorNo=0;
        SBOLAPI.mapTo(multicellularSystem, senderSystem, ahl, receiverSystem,ahl);        
        TestUtil.validateDocument(doc, errorNo, null, null);
       
        SBOLAPI.createConstraint(receiverSystem, receiverCell, nonAHL, RestrictionType.TopologyRestriction.contains.getUri());        
        SBOLAPI.mapTo(multicellularSystem, senderSystem, ahl, receiverSystem,nonAHL);        
        
        //CompRef -->SubComponent test
        TestUtil.validateDocument(doc, errorNo, null, null);
 
        
        //CompRef-->CompRef--> SubComponent test
        Component IPTG=SBOLAPI.createComponent(doc, "IPTG", ComponentType.SimpleChemical.getUri(), "IPTG", "IPTG", Role.Effector);
        
        Component iptgSenderL2=SBOLAPI.createComponent(doc, "senderIPTGSubSystem", ComponentType.FunctionalEntity.getUri(), "SenderIPTGSubSystem", "Sender IPTG Sub System", Role.FunctionalCompartment);
        Component iptgSenderL3=SBOLAPI.createComponent(doc, "senderIPTGSubSubSystem", ComponentType.FunctionalEntity.getUri(), "SenderIPTGSubSubSystem", "Sender IPTG SubSub System", Role.FunctionalCompartment);
        SubComponent scIPTG_SenderL3= iptgSenderL3.createSubComponent(IPTG);
        SubComponent scIPTGSenderL3_L2= iptgSenderL2.createSubComponent(iptgSenderL3);
        SubComponent scIPTGSenderL2_L1= multicellularSystem.createSubComponent(iptgSenderL2);
        ComponentReference csIPTG_SenderL2=iptgSenderL2.createComponentReference(scIPTG_SenderL3, scIPTGSenderL3_L2);
        ComponentReference csIPTG_SenderL1=multicellularSystem.createComponentReference(csIPTG_SenderL2, scIPTGSenderL2_L1);
        
        Component iptgReceiverL2=SBOLAPI.createComponent(doc, "ReceiverIPTGSubSystem", ComponentType.FunctionalEntity.getUri(), "ReceiverIPTGSubSystem", "Sender IPTG Sub System", Role.FunctionalCompartment);
        Component iptgReceiverL3=SBOLAPI.createComponent(doc, "ReceiverIPTGSubSubSystem", ComponentType.FunctionalEntity.getUri(), "ReceiverIPTGSubSubSystem", "Sender IPTG SubSub System", Role.FunctionalCompartment);
        SubComponent scIPTG_ReceiverL3= iptgReceiverL3.createSubComponent(IPTG);
        SubComponent scIPTGReceiverL3_L2= iptgReceiverL2.createSubComponent(iptgReceiverL3);
        SubComponent scIPTGReceiverL2_L1= multicellularSystem.createSubComponent(iptgReceiverL2);
        ComponentReference csIPTG_ReceiverL2=iptgReceiverL2.createComponentReference(scIPTG_ReceiverL3, scIPTGReceiverL3_L2);
        ComponentReference csIPTG_ReceiverL1=multicellularSystem.createComponentReference(csIPTG_ReceiverL2, scIPTGReceiverL2_L1);
        
        //No error
        multicellularSystem.createConstraint(RestrictionType.IdentityRestriction.verifyIdentical.getUri(), csIPTG_SenderL1, csIPTG_ReceiverL1);
        TestUtil.validateDocument(doc, errorNo, null, null );
       
        //Error since they are the same
        multicellularSystem.createConstraint(RestrictionType.IdentityRestriction.differentFrom.getUri(), csIPTG_SenderL1, csIPTG_ReceiverL1);
        TestUtil.validateDocument(doc, ++errorNo, "sbol3-11705", "differentFrom");
     
        
        Component araSenderL2=SBOLAPI.createComponent(doc, "senderAraSubSystem", ComponentType.FunctionalEntity.getUri(), "SenderAraSubSystem", "Sender Ara Sub System", Role.FunctionalCompartment);
        Component araSenderL3=SBOLAPI.createComponent(doc, "senderAraSubSubSystem", ComponentType.FunctionalEntity.getUri(), "SenderAraSubSubSystem", "Sender Ara SubSub System", Role.FunctionalCompartment);
        ExternallyDefined exAra_SenderL3= araSenderL3.createExternallyDefined(Arrays.asList(ComponentType.SimpleChemical.getUri()), URI.create("http://uniprot.com/arabinose"));
        SubComponent scAraSenderL3_L2= araSenderL2.createSubComponent(araSenderL3);
        SubComponent scAraSenderL2_L1= multicellularSystem.createSubComponent(araSenderL2);
        ComponentReference csAra_SenderL2=araSenderL2.createComponentReference(exAra_SenderL3, scAraSenderL3_L2);
        ComponentReference csAra_SenderL1=multicellularSystem.createComponentReference(csAra_SenderL2, scAraSenderL2_L1);
        
        Component araReceiverL2=SBOLAPI.createComponent(doc, "ReceiverAraSubSystem", ComponentType.FunctionalEntity.getUri(), "ReceiverAraSubSystem", "Receiver Ara Sub System", Role.FunctionalCompartment);
        Component araReceiverL3=SBOLAPI.createComponent(doc, "ReceiverAraSubSubSystem", ComponentType.FunctionalEntity.getUri(), "ReceiverAraSubSubSystem", "Receiver Ara SubSub System", Role.FunctionalCompartment);
        ExternallyDefined exAra_ReceiverL3= araReceiverL3.createExternallyDefined(Arrays.asList(ComponentType.SimpleChemical.getUri()), URI.create("http://uniprot.com/arabinose"));
        SubComponent scAraReceiverL3_L2= araReceiverL2.createSubComponent(araReceiverL3);
        SubComponent scAraReceiverL2_L1= multicellularSystem.createSubComponent(araReceiverL2);
        ComponentReference csAra_ReceiverL2=araReceiverL2.createComponentReference(exAra_ReceiverL3, scAraReceiverL3_L2);
        ComponentReference csAra_ReceiverL1=multicellularSystem.createComponentReference(csAra_ReceiverL2, scAraReceiverL2_L1);
       
        //No error
        multicellularSystem.createConstraint(RestrictionType.IdentityRestriction.verifyIdentical.getUri(), csAra_SenderL1, csAra_ReceiverL1);
        TestUtil.validateDocument(doc, errorNo, null, null);
        
        //Error since they are the same
        multicellularSystem.createConstraint(RestrictionType.IdentityRestriction.differentFrom.getUri(), csAra_SenderL1, csAra_ReceiverL1);
        TestUtil.validateDocument(doc, ++errorNo, "sbol3-11705", "differentFrom_2");
        
        //Error:
        //Change receiver.Ara (ExternallyDefined) --> receiver.Ara (SubComponent). 
        //Now sender has compref.ExternalllyDefined and receiver has compRef.SubComponent as two different types.
        Component Ara=SBOLAPI.createComponent(doc, "Ara", ComponentType.SimpleChemical.getUri(), "Ara", "Ara", Role.Effector);
        SubComponent scAra_ReceiverL3=araReceiverL3.createSubComponent(Ara);
        csAra_ReceiverL2.setRefersTo(scAra_ReceiverL3);
        TestUtil.validateDocument(doc, ++errorNo, "sbol3-11705", "differentTypes");
        
       
        senderSubComponent.setOrientation(Orientation.inline);
        receiverSubComponent.setOrientation(Orientation.inline);
        Constraint orientationConstraint=multicellularSystem.createConstraint(OrientationRestriction.sameOrientationAs, senderSubComponent, receiverSubComponent);
        TestUtil.validateDocument(doc, errorNo, null, null);
       
        receiverSubComponent.setOrientation(Orientation.reverseComplement);
        TestUtil.validateDocument(doc, ++errorNo, "sbol3-11705", "orientationMismatch");
        
        orientationConstraint.setRestriction(OrientationRestriction.oppositeOrientationAs);
        TestUtil.validateDocument(doc, --errorNo,null, null);
                 
    }

}
