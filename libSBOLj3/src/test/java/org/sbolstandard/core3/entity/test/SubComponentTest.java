package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

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

public class SubComponentTest extends TestCase {
	
	public void testSubComponent() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUri())); 
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		
		SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, "");
		
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=device.createSubComponent(term.getUri());
		termSubComponent.setOrientation(Orientation.inline);
		
		termSubComponent.getRoleIntegration();
		TestUtil.validateReturnValue(termSubComponent, "toRoleIntegration", new Object[] {URI.create("http://invalidroleintegration.org")}, URI.class);
		
		Sequence i13504Sequence= device.getSequences().get(0);
		
		int start=i13504Sequence.getElements().length() + 1;
		int end=start + term_na.length()-1;
    	
		i13504Sequence.setElements(i13504Sequence.getElements() + term_na);
		Range range=(Range)termSubComponent.createRange(start, end,i13504Sequence);
		range.setOrientation(Orientation.inline);
		
		//Range range2=(Range)termSubComponent.createRange(start, end,i13504Sequence);
		
		TestUtil.serialise(doc, "entity/subcomponent", "subcomponent");
	    System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
	    TestUtil.assertReadWrite(doc); 
	    
	    Configuration.getInstance().setValidateAfterSettingProperties(false);
	     
	    TestUtil.validateIdentifiedAndDocument(termSubComponent,doc,0, null, null);
	    
	    TestUtil.validateProperty(termSubComponent, "setInstanceOf", new Object[] {null}, Component.class);
	    URI nullURI=null;
	    termSubComponent.setInstanceOf(nullURI);	    
	    range.setEnd(Optional.empty());
	    //range2.setEnd(Optional.empty());
	    TestUtil.validateIdentifiedAndDocument(termSubComponent,doc,2,null, "SubComponent_instanceOfNull");
	    termSubComponent.setRoleIntegration(null);
	    TestUtil.validateIdentifiedAndDocument(termSubComponent,doc,2,null, "SubComponent_roleIntegrationNull");
	    termSubComponent.setRoleIntegration(RoleIntegration.mergeRoles);
	    TestUtil.validateIdentifiedAndDocument(termSubComponent,doc,2, null, "SubComponent_roleIntegrationMergeRoles");
	    
	    //Roles must be provided if roleIntegration is not nulls
	    termSubComponent.setRoleIntegration(null);
	    termSubComponent.setRoles(Arrays.asList(URI.create("http://testrole.org")));
	    TestUtil.validateIdentifiedAndDocument(termSubComponent,doc,3, "sbol3-10802", "SubComponent_roleIntegrationNullRolesNotNull");
	    
	    termSubComponent.setInstanceOf(term);
	    TestUtil.validateIdentifiedOnly(doc, device, 3, "sbol3-10802, sbol3-10807", "SubComponent_instanceOfInvalid");   
	    termSubComponent.setInstanceOf(device);
	    TestUtil.validateIdentifiedOnly(doc, device, 5, "sbol3-10802,sbol3-10803,sbol3-10804,sbol3-10807", "SubComponent_instanceOfInvalid2");
	    
	    //Clean the errors
	    termSubComponent.setInstanceOf(term);
	    TestUtil.validateIdentifiedOnly(doc, device, 3, "sbol3-10802, sbol3-10807", "SubComponent_instanceOfInvalid3");   
	    range.setEnd(Optional.of(end));
	    //range2.setEnd(Optional.of(end));
	    termSubComponent.setRoleIntegration(RoleIntegration.mergeRoles);
	    TestUtil.validateIdentifiedOnly(doc, device, 0, null, null);
	    
	    
	    Resource resource = TestUtil.getResource(termSubComponent);
		
	    //SBOL_VALID_ENTITY_TYPES - SubComponent.instanceOf
	    Component instanceOf=termSubComponent.getInstanceOf();
	  	RDFUtil.setProperty(resource, DataModel.SubComponent.instanceOf, Arrays.asList(range.getUri()));
	  	TestUtil.validateIdentifiedAndDocument(termSubComponent,doc,1, "sbol3-10111", "SubComponent_instanceOfInvalid");
	  	termSubComponent.setInstanceOf(term);
	  	TestUtil.validateIdentifiedAndDocument(termSubComponent,doc,0, null, null);
	  		
	    //SBOL_VALID_ENTITY_TYPES - SubComponent.roleIntegration
	    RoleIntegration roleIntegration=termSubComponent.getRoleIntegration();
	  	
	  	TestUtil.assertReadWrite(doc);	  		  
    }
}
