package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.Model;
import org.sbolstandard.core3.entity.Participation;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.Role;
import junit.framework.TestCase;

public class ComponentTest extends TestCase {
	
	public void testComponent() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
	    TestUtil.serialise(doc, "entity/component", "component");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
        
		//Component.hasSequence can have zero values
		TestUtil.validateIdentifiedAndDocument(popsReceiver,doc,0, null, null);
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		
		//Component.hasSequence can have multiple values
		List<Sequence> tempSequences=pTetR.getSequences();
		Sequence pTetRSequence=SBOLAPI.addSequence(doc, pTetR, Encoding.NucleicAcid, "aaaa");
		Sequence retrieved=doc.getIdentified(pTetRSequence.getUri(), Sequence.class);
		String elementsRetrieved=retrieved.getElements();
		
		// disable testing option requirements so it doesn't match against COMPONENT_TYPE_SEQUENCE_LENGTH_MATCH
		Configuration.getInstance().setValidateRecommendedRules(false);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		Configuration.getInstance().setValidateRecommendedRules(true);
		pTetR.setSequences(tempSequences);
		
		// COMPONENT_TYPE_SEQUENCE_LENGTH_MATCH
		SBOLAPI.addSequence(doc, pTetR, Encoding.NucleicAcid, "tttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		pTetR.getSequences().get(0).setElements("aaa");
		pTetR.getSequenceURIs().get(0);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10617", "ComponentTest_sequenceLengthMismatch");
		Configuration.getInstance().setValidateRecommendedRules(false);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);

		Configuration.getInstance().setValidateRecommendedRules(true);
		pTetR.setSequences(tempSequences); //use previously saved vales above
		
		List<Sequence> nullSequences= null;
		
		// COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
		pTetR.getSequences().get(0).setEncoding(Encoding.INCHI);
		TestUtil.validateIdentifiedAndDocument(pTetR, doc, 1, "sbol3-10616", "ComponentTest");				
		pTetR.getSequences().get(0).setEncoding(Encoding.NucleicAcid);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		pTetR.setSequences(tempSequences);
		
		/*
		//If the sequence is empty then there is no need to validate the encoding
		pTetR.getSequences().get(0).setElements(null);
		TestUtil.validateIdentified(pTetR,doc,0);		
		Configuration.getInstance().setCompleteDocument(true);
		TestUtil.validateIdentified(pTetR,doc,1, "sbol3-10616");		
		Configuration.getInstance().setCompleteDocument(false);		
		pTetR.setSequences(tempSequences);
		TestUtil.validateIdentified(pTetR,doc,0);
		*/
		
		
		//COMPONENT_TYPE_AT_MOST_ONE_TOPOLOGY_TYPE
		pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.TopologyType.Circular.getUri()));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.TopologyType.Circular.getUri(), ComponentType.TopologyType.Linear.getUri()));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10607", "ComponentTest.InconsistentTopologyTypes");
		pTetR.setTypes(Arrays.asList(ComponentType.Protein.getUri(), ComponentType.TopologyType.Circular.getUri(), ComponentType.TopologyType.Linear.getUri()));
		tempSequences=pTetR.getSequences();
		
		pTetR.setSequences(nullSequences);
		pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
		pTetR.setSequences(tempSequences);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		
		//COMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_STRAND_OR_TOPOLOGY
		pTetR.setTypes(Arrays.asList(ComponentType.TopologyType.Linear.getUri(), ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,2, "sbol3-10608,sbol3-10612", "ComponentTest_ComponentTypeOnlyDNAOrRNAIncludeStrandOrTopology");
	    pTetR.setTypes(Arrays.asList(ComponentType.Protein.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,3, "sbol3-10608,sbol3-10612,sbol3-10616", "ComponentTest_ComponentTypeOnlyDNAOrRNAIncludeStrandOrTopology_2");
	    pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10607", "ComponentTest_ComponentTypeOnlyDNAOrRNAIncludeStrandOrTopology_3");
	    pTetR.setTypes(Arrays.asList(ComponentType.RNA.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10607", "ComponentTest_ComponentTypeOnlyDNAOrRNAIncludeStrandOrTopology_4");
	    pTetR.setTypes(Arrays.asList(ComponentType.RNA.getUri(), ComponentType.TopologyType.Linear.getUri()));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
	    pTetR.setTypes(Arrays.asList(ComponentType.StrandType.Double.getUri(), ComponentType.Protein.getUri()));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,3, "sbol3-10608,sbol3-10612,sbol3-10616", "ComponentTest_ComponentTypeOnlyDNAOrRNAIncludeStrandOrTopology_5");
	    pTetR.setTypes(Arrays.asList(ComponentType.StrandType.Double.getUri(), ComponentType.DNA.getUri()));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		
		
		Resource resource = TestUtil.getResource(pTetR);
		
		//SBOL_VALID_ENTITY_TYPES - Component.Sequences
		RDFUtil.setProperty(resource, DataModel.Component.sequence, popsReceiver.getUri());
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10111", "ComponentTest_sequenceInvalid");
		RDFUtil.setProperty(resource, DataModel.Component.sequence, Arrays.asList(popsReceiver.getUri(), pTetR.getUri(), tempSequences.get(0).getUri()));	
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,2, "sbol3-10111", "ComponentTest_sequenceInvalid_2");
		pTetR.setSequences(tempSequences);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		
		//SBOL_VALID_ENTITY_TYPES - Component.Models
		List<Model> tempModels=pTetR.getModels();
		RDFUtil.setProperty(resource, DataModel.Component.model, SBOLUtil.getURIs(tempSequences));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10111", "ComponentTest_modelInvalid");
		pTetR.setModels(tempModels);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		
		
		//SBOL_VALID_ENTITY_TYPES - Component.Features
		List<URI> tempURIs=SBOLUtil.getURIs(pTetR.getFeatures());
		RDFUtil.setProperty(resource, DataModel.Component.feature, SBOLUtil.getURIs(pTetR.getSequences()));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10111", "ComponentTest_featureInvalid");
		RDFUtil.setProperty(resource, DataModel.Component.feature, tempURIs);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		
		//SBOL_VALID_ENTITY_TYPES - Component.Constraints
		tempURIs=SBOLUtil.getURIs(pTetR.getConstraints());
		RDFUtil.setProperty(resource, DataModel.Component.constraint, SBOLUtil.getURIs(pTetR.getSequences()));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10111", "ComponentTest_constraintInvalid");
		RDFUtil.setProperty(resource, DataModel.Component.constraint, tempURIs);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);

		//SBOL_VALID_ENTITY_TYPES - Component.Interactions
		tempURIs=SBOLUtil.getURIs(pTetR.getInteractions());
		RDFUtil.setProperty(resource, DataModel.Component.interaction, SBOLUtil.getURIs(pTetR.getSequences()));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, "sbol3-10111", "ComponentTest_interactionInvalid");
		RDFUtil.setProperty(resource, DataModel.Component.interaction, tempURIs);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);		
				
		//Component.type is required
		TestUtil.validateProperty(pTetR, "setTypes", new Object[] {null}, List.class);
		List<URI> tempList=pTetR.getTypes();
		pTetR.setTypes(null);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, null, "ComponentTest_typeRequired");
		pTetR.setTypes(new ArrayList<URI>());
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,1, null, "ComponentTest_typeRequired_2");		
		pTetR.setTypes(tempList);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);		

        // COMPONENT_TYPE_MATCH_PROPERTY
		/* Removed the validation for sbol3-10604 ⋆ A Component SHOULD have a type property from Table 2.
		 * pTetR.setTypes(Arrays.asList(URI.create("http://invalidtype.org")));
		TestUtil.validateIdentified(pTetR,doc,1); 
		pTetR.setTypes(Arrays.asList(URI.create("http://invalidtype.org"),URI.create("http://invalidtype2.org")));
		TestUtil.validateIdentified(pTetR,doc,1);*/
		
		pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null); 
		
		//also check that the configuration option properly disables the check and allows an invalid type
		Configuration.getInstance().setValidateRecommendedRules(false);
		pTetR.setTypes(Arrays.asList(URI.create("http://invalidtype.org")));
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
		//Reset the values
		pTetR.setTypes(tempList);
		Configuration.getInstance().setValidateRecommendedRules(true);
		TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);	
		
		Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
	    attachment.setFormat(ModelLanguage.SBML);
	    attachment.setSize(OptionalLong.of(1000));
	    
	    pTetR.setAttachments(Arrays.asList(attachment));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
	    attachment.setSize(OptionalLong.of(-1));
	    TestUtil.validateDocument(doc,1, " sbol3-12804", "ComponentTest_attachmentSizeNegative");
	    attachment.setSize(OptionalLong.of(100));
	    
	    
	    //Encoding must be provided if elements are set
	    Sequence seq=doc.getSequences().get(0);
	    URI encodingValue=null;
	    seq.setEncoding(encodingValue);
	    TestUtil.validateIdentifiedAndDocument(seq,doc,1,2, "sbol3-10501", "ComponentTest.SequenceEncodingMissing"); // will also error against COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
		
	    //One main component type must be provided.
	    pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.Protein.getUri() ));
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,3,4, "sbol3-10501,sbol3-10601,sbol3-10616", "ComponentTest.MultipleInconsistentComponentTypes"); // will also error against COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
	    pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
	    seq.setEncoding(Encoding.NucleicAcid);
	    TestUtil.validateIdentifiedAndDocument(pTetR,doc,0, null, null);
	    
		
	   //IDENTIFIED_URI_MUST_BE_USED_AS_A_PREFIX_FOR_CHILDREN
        Interaction interaction= popsReceiver.createInteraction(SBOLAPI.append(base, "protein_production"), Arrays.asList(InteractionType.GeneticProduction.getUri()));
        TestUtil.validateIdentifiedAndDocument(popsReceiver,doc,1, "sbol3-10104", "ComponentTest.Interaction.ChildURIshouldUseParentPrefix");
        
        Interaction interaction2= popsReceiver.createInteraction(Arrays.asList(InteractionType.Inhibition.getUri()));
        Component TetR=SBOLAPI.createComponent(doc, URI.create("https://synbiohub.org/public/igem/TetR"),ComponentType.Protein.getUri(), "TetR", "TetR repressor", Role.TF);
        SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(popsReceiver, TetR);
        Participation participation= interaction2.createParticipation(SBOLAPI.append(base, "inhibitor_participation"), Arrays.asList(ParticipationRole.Inhibitor.getUri()), gfpProteinSubComponent);
        TestUtil.validateIdentifiedAndDocument(popsReceiver,doc,2, "sbol3-10104", "ComponentTest.Interaction2.ChildURIshouldUseParentPrefix_2");
        TestUtil.validateIdentifiedOnly(doc, interaction2,1, "sbol3-10104", "ComponentTest.Interaction2", false);
        
        //Introduce two more errors. 
        Interaction interaction3= popsReceiver.createInteraction(SBOLAPI.append(base, "protein_production3"), Arrays.asList(InteractionType.Inhibition.getUri()));
        Participation participation3= interaction3.createParticipation(SBOLAPI.append(base, "inhibitor_participation3"), Arrays.asList(ParticipationRole.Inhibitor.getUri()), gfpProteinSubComponent);
        TestUtil.validateIdentifiedAndDocument(popsReceiver, doc,4, "sbol3-10104", "ComponentTest.Interaction.InconsistentParticipationRoles");
        TestUtil.validateIdentifiedOnly(doc, interaction3, 1, "sbol3-10104", "ComponentTest.Interaction3", false);

        //SUBCOMPONENT_OBJECTS_CIRCULAR_REFERENCE_CHAIN
        Component TetRbindingDomain=SBOLAPI.createComponent(doc, URI.create("https://synbiohub.org/public/igem/TetR_binding"),ComponentType.Protein.getUri(), "TetRbinding", "TetR binding domain", Role.TF);
        SubComponent tetRProteinSubComponent=SBOLAPI.addSubComponent(popsReceiver, TetR);//Valid
	    TestUtil.validateIdentifiedAndDocument(TetR,doc, 0, 4,"sbol3-10104", "ComponentTest.InvalidURIsInSubComponents");
        SubComponent tetRBindingProteinSubComponent=SBOLAPI.addSubComponent(TetR, TetRbindingDomain);//Valid
	    TestUtil.validateIdentifiedAndDocument(TetR,doc, 0, 4,"sbol3-10104", "ComponentTest.InvalidURIsInSubComponents_2");
        SubComponent tetRBindingProteinSubComponent2=SBOLAPI.addSubComponent(TetR, TetR);//InValid
        TestUtil.validateIdentifiedAndDocument(TetR,doc, 2, 6,"sbol3-10104,sbol3-10803", "ComponentTest.InvalidURIsInSubComponents_3");
        
        SubComponent tetRBindingProteinSubComponent3=SBOLAPI.addSubComponent(TetRbindingDomain, TetR); //InValid
	    TestUtil.validateIdentifiedAndDocument(TetR,doc, 3, 8,"sbol3-10104,sbol3-10803,sbol3-10804", "ComponentTest.InvalidURIsInSubComponents_4");
	    TestUtil.validateIdentifiedOnly(doc, TetRbindingDomain,1,"sbol3-10804", "ComponentTest.TetRbindingDomain");
	    
        SubComponent tetRBindingProteinSubComponent4=SBOLAPI.addSubComponent(TetRbindingDomain, popsReceiver); //InValid
        
       // Configuration.getConfiguration().setValidateBeforeSaving(false);
       // System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        
        
        TestUtil.validateIdentifiedAndDocument(TetR,doc, 5, 11, "sbol3-10104,sbol3-10803,sbol3-10804", "ComponentTest.InvalidURIsInSubComponents_5");
	    TestUtil.validateIdentifiedOnly(doc, popsReceiver,  5,  "sbol3-10104", "ComponentTest.popsReceiver", false);

           
     	//SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING
        Sequence seqAA = doc.createSequence("seqAA");
        seqAA.setEncoding(Encoding.AminoAcid);
        seqAA.setElements("ATgz");
        TestUtil.validateIdentifiedOnly(doc, seqAA, 0, null, null);
        seqAA.setElements("ATgd");
        TestUtil.validateIdentifiedOnly(doc, seqAA, 0, null, null);
        seqAA.setElements("AT.");
        TestUtil.validateIdentifiedOnly(doc, seqAA, 1, "sbol3-10503", "ComponentTest.seqAA.dot");
        seqAA.setElements("AT-");
        TestUtil.validateIdentifiedOnly(doc, seqAA, 1, "sbol3-10503",	"ComponentTest.seqAA.hyphen");
        
        

        Sequence seqNA = doc.createSequence("seqNA");
        seqNA.setEncoding(Encoding.NucleicAcid);
        seqNA.setElements("ZAtc");
        TestUtil.validateIdentifiedOnly(doc, seqNA, 1, "sbol3-10503", "ComponentTest.seqNA.Z");
        seqNA.setElements("ATGcn");
        TestUtil.validateIdentifiedOnly(doc, seqNA, 0, null , null);

        // examples taken from https://archive.epa.gov/med/med_archive_03/web/html/smiles.html
        Sequence seqSMILES = doc.createSequence("seqSMILES");
        seqSMILES.setEncoding(Encoding.SMILES);
        seqSMILES.setElements("Hydrogen Dioxode"); //pretty much anything is allowed other than a space
        TestUtil.validateIdentifiedOnly(doc, seqSMILES, 1, "sbol3-10503", "ComponentTest.seqSMILES.invalid");
        seqSMILES.setElements("c1c(N(=O)=O)cccc1"); // nitrobenzene
        TestUtil.validateIdentifiedOnly(doc, seqSMILES, 0, null, null);
        seqSMILES.setElements("C=1CCCCC1"); // Cyclohexene
        TestUtil.validateIdentifiedOnly(doc, seqSMILES, 0, null, null);

        // examples taken from https://en.wikipedia.org/wiki/International_Chemical_Identifier
        Sequence seqINCHI = doc.createSequence("seqINCHI");
        seqINCHI.setEncoding(Encoding.INCHI);
        seqINCHI.setElements("InChI=JS/C2H6O/c1-2-3/h3H,2H2,1H3"); //ethanol but starting with a J
        TestUtil.validateIdentifiedOnly(doc, seqINCHI, 1, "sbol3-10503", "ComponentTest.seqINCHI.invalid");
        seqINCHI.setElements("InChI=1S/C2H6O/c1-2-3/h3H,2H2,1H3"); //ethanol
        TestUtil.validateIdentifiedOnly(doc, seqINCHI, 0, null, null);
        seqINCHI.setElements("InChI=1S/C6H8O6/c7-1-2(8)5-3(9)4(10)6(11)12-5/h2,5,7-10H,1H2/t2-,5+/m0/s1"); //L-ascorbic acid with InChI
        TestUtil.validateIdentifiedOnly(doc, seqINCHI, 0, null, null); 
        String elements = seqINCHI.getElements();
        
        seqINCHI.setElements(null);
        TestUtil.validateIdentifiedOnly(doc, seqINCHI, 0, null, null);
        
        
	    
        /*ComponentType[] values=ComponentType.values();
        System.out.println(values.length);
        OptionalComponentType[] optionalValues2=ComponentType.OptionalComponentType.values();
        System.out.println(optionalValues2.length);*/
        
        
    }
}
