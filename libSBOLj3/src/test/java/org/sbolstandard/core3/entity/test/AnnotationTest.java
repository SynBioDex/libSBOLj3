package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.provenance.Plan;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class AnnotationTest extends TestCase {
	
	public void testAnnotation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        URINameSpace igem=new URINameSpace(URI.create("http://parts.igem.org/"), "igem");
        doc.addNameSpacePrefixes(igem);
        
        Component part=SBOLAPI.createComponent(doc, "BBa_J23119", ComponentType.DNA.getUri(), "BBa_J23119 part", "Parts J23100 through J23119 are a family of constitutive promoter parts isolated from a small combinatorial library.", Role.Promoter);
        part.addAnnotation(igem.local("group"), "iGEM2006_Berkeley");
        part.addAnnotation(igem.local("experienceURL"), URI.create("http://parts.igem.org/Part:BBa_J23119:Experience"));
        
        //Internal metadata
        Metadata igemInf=part.createMetadata("information1", igem.local("Information"), igem.local("hasInformation"));
        igemInf.setName("BBa_J23119_experience");
        igemInf.setDescription("The experience page captures users' experience using the BBa_J23119 part");
        igemInf.addAnnotation(igem.local("sigmaFactor"), "//rnap/prokaryote/ecoli/sigma70");
        igemInf.addAnnotation(igem.local("regulation"), "//regulation/constitutive");
        igemInf.addAnnotation(igem.local("regulation"), "//regulation/second_regulation");
        Assert.assertTrue(igemInf.getType().contains( igem.local("Information")));

		 //Internal metadata
        Metadata igemInf_2=part.createMetadata("information2", igem.local("A_Information"), igem.local("hasInformation"));
        igemInf_2.setName("BBa_J23119_experience2");
		Metadata igemInf_3=part.createMetadata("information3", igem.local("Z_Information"), igem.local("hasInformation"));
        igemInf_3.setName("BBa_J23119_experience3");
		
        
        igemInf.addAnnotation(igem.local("source"), part.getUri());
        List<Object> uris= igemInf.getAnnotation(igem.local("source"));
        Assert.assertTrue(uris!=null && uris.size()>0 && ((URI)uris.get(0)).equals(part.getUri()));
        		
        
        
        
        Metadata igemInf2=part.createMetadata("usage1", igem.local("iGEMUsage"), igem.local("hasUsage"));
        igemInf2.setName("BBa_J23119_usage");
        igemInf2.setDescription("BBa_J23119 usage statistics");
        igemInf2.addAnnotation(igem.local("inStock"), "true");
        igemInf2.addAnnotation(igem.local("registryStar"), "1");
        igemInf2.addAnnotation(igem.local("uses"), "442");
        igemInf2.addAnnotation(igem.local("uses2"), 442);
        igemInf2.addAnnotation(igem.local("twins"), "7");
		
		Metadata igemInf2_2=part.createMetadata("usage2", igem.local("A_iGEMUsage"), igem.local("hasUsage"));
        igemInf2_2.setName("BBa_J23119_usage_2");
		igemInf2_2.addAnnotation(igem.local("twins"), "7");
		Metadata igemInf2_3=part.createMetadata("usage3", igem.local("Z_iGEMUsage"), igem.local("hasUsage"));
        igemInf2_3.setName("BBa_J23119_usage_3");
		

       
        //Metadata within metadata
        Identified igemInfInternal=igemInf2.createMetadata("twinParts", igem.local("TwinPartUsage"), igem.local("twinURLs"));
        igemInfInternal.setName("twin parts");
        igemInfInternal.addAnnotation(igem.local("twinURL"), URI.create("http://parts.igem.org/wiki/index.php?title=Part:BBa_J72073"));
        igemInfInternal.addAnnotation(igem.local("twinURL"), URI.create("http://parts.igem.org/wiki/index.php?title=Part:BBa_M1638"));
        igemInfInternal.addAnnotation(igem.local("twinURL"), URI.create("http://parts.igem.org/wiki/index.php?title=Part:BBa_M36800"));

        //TopLevel metadata
        TopLevel igemInf4=doc.createMetadata("iGEMRepository", igem.local("Repository"));
        igemInf4.setName("iGEM Registry");
        igemInf4.setDescription("Registry of Standard Biological Parts");
        igemInf4.addAnnotation(igem.local("website"), URI.create("http://parts.igem.org/Main_Page"));
        
        TopLevel igemInf5=doc.createMetadata("SynBioHubRepository", igem.local("Repository"));
        igemInf5.setName("SynBioHub");
       
        
        part.addAnnotation(igem.local("belongsTo"), igemInf5);
        part.addAnnotation(igem.local("belongsTo"), igemInf4);
        
        TestUtil.serialise(doc, "entity/annotation", "annotation");
   
        List<TopLevelMetadata> repositoryAnnotations=doc.getTopLevelMetadataList(igem.local("Repository"));
        assertEquals(repositoryAnnotations.size(), 2);
        TopLevelMetadata repAnnot1=repositoryAnnotations.get(0);
        
        List<URI> searchResult=part.filterIdentifieds(Arrays.asList(igemInf4.getUri(), igemInf5.getUri()), DataModel.Identified.name, "iGEM Registry");
        assertEquals(searchResult.get(0), igemInf4.getUri());
        
        List<TopLevelMetadata> allTopLevelAnnotations=doc.getTopLevelMetadataList();
        assertEquals(allTopLevelAnnotations.size(), 2);
        
		Plan plan=doc.createPlan("myPlan");
		plan.setName("myPlanName");
		plan.setDescription("myPlanDescription");
		
		allTopLevelAnnotations=doc.getTopLevelMetadataList();
        assertEquals(allTopLevelAnnotations.size(), 2);
       
        
        List<Pair<URI,Object>> annotations=part.getAnnotations();
        if (annotations!=null)
        {
        	for (Pair<URI,Object> annotation: annotations)
        	{
        		URI property= annotation.getKey();
        		Object value= annotation.getValue();
        		if (value instanceof TopLevelMetadata){
        			TopLevelMetadata tlm=(TopLevelMetadata) value;
        			System.out.println(property.toString() + " : " +  tlm.getUri());
        		}
        		else if (value instanceof Metadata){
        			Metadata tlm=(Metadata) value;
        			System.out.println(property.toString() + " : " +  tlm.getUri());
        		}
        		else if (value instanceof Metadata){        			
        			System.out.println(property.toString() + " : " +  value.toString());
        		}        		        		
        	}
        }
        
        
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.println(output);
        List<Metadata> metedataEntities=part.getMetadataEntites();
        
        assertEquals(metedataEntities.get(0).getDisplayId(), igemInf.getDisplayId());
        
        SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
            
        output=SBOLIO.write(doc2, SBOLFormat.TURTLE);
        System.out.println(output);
        System.out.println(SBOLIO.write(doc2, SBOLFormat.RDFXML));     
        printMetadata(doc2.getComponents().get(0));
        
        TestUtil.assertReadWrite(doc);
        
        List<TopLevelMetadata> list=doc.getTopLevelMetadataList();
        
        
    }
	
	public void printMetadata(Identified identified) throws SBOLGraphException
	{
		 URINameSpace igem=new URINameSpace(URI.create("http://parts.igem.org/"), "igem");  
		 System.out.println("group:" + identified.getAnnotation(igem.local("group")));
		 System.out.println("experienceURL:" + identified.getAnnotation(igem.local("experienceURL")));
		 List<Object> informationMetadata=identified.getAnnotation(igem.local("hasInformation"));
		 if (informationMetadata!=null)
		 {
			 Metadata metadata=(Metadata) informationMetadata.get(0);
			 System.out.println("hasInformation");
			 printMetadata(metadata,3);
			 System.out.println("   SigmaFactor:" + metadata.getAnnotation(igem.local("sigmaFactor")));
			 System.out.println("   Regulation:" + metadata.getAnnotation(igem.local("regulation")));
		 }
		 List<Object> usage=identified.getAnnotation(igem.local("hasUsage"));
		 if (usage!=null)
		 {
			 Metadata metadata=(Metadata) usage.get(0);
			 System.out.println("hasUsage");
			 printMetadata(metadata,3);
			 System.out.println("   Uses:" + metadata.getAnnotation(igem.local("uses")));
			 System.out.println("   Uses:" + metadata.getAnnotation(igem.local("uses2")));			 
			 System.out.println("   Twins:" + metadata.getAnnotation(igem.local("twins")));
			 List<Object> twinsURLs=metadata.getAnnotation(igem.local("twinURLs"));
			 if (twinsURLs!=null){
				Metadata twinPartUsageMetadata=(Metadata) twinsURLs.get(0);
				System.out.println("   TwinPartUsage:");
				printMetadata(metadata,6);
				System.out.println("      twinURL:" + twinPartUsageMetadata.getAnnotation(igem.local("twinURL")));
			 }
		 }
		 
		 List<Object> repositoryMetadataList=identified.getAnnotation(igem.local("belongsTo"));
		 System.out.println("belongsTo:");
		 if (repositoryMetadataList!=null)
		 {
			 for (Object o: repositoryMetadataList)
			 {
				 Identified repositoryMetadata=(Identified) o;
				 System.out.println("  " + repositoryMetadata.getName());
				 
			 }
		 }
	}

	 private void printMetadata(Metadata metadata, int count) throws SBOLGraphException
	 {
		 String space=String.format("%"+count+"s", "") ;
		 System.out.println(space + "uri:" + metadata.getUri());
		 System.out.println(space + "name:" + metadata.getName());
		 System.out.println(space + "desc:" + metadata.getDescription());
		 System.out.println(space + "displayId:" + metadata.getDisplayId());
		 
	 }
	/*
	public class IgemInformation extends Metadata
	{
		public IgemInformation (SBOLDocument doc, URI uri) throws SBOLGraphException
		{
			super(doc, uri);
		}
		
		@Override
		public URI getResourceType() {
			return URI.create("http://parts.igem.org/IgemInformation");
		}
	}*/

}
