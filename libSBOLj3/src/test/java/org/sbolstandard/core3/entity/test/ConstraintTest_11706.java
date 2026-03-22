package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import org.sbolstandard.core3.vocabulary.RestrictionType.SequentialRestriction;

import junit.framework.TestCase;

public class ConstraintTest_11706 extends TestCase {
	
	public void testConstraintReference() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        String gfp_na="atgcgtaaaggagaagaacttttca";
		Component gfp=SBOLAPI.createDnaComponent(doc, "parent", "parent", null, Role.CDS, gfp_na);
		Sequence seq= gfp.getSequences().get(0);
		
		Component child13=SBOLAPI.createDnaComponent(doc, "child13", "child13", "child13", Role.EngineeredRegion, "atg");//atgaaagga
		Component child57=SBOLAPI.createDnaComponent(doc, "child57", "child57", "child57", Role.EngineeredRegion, "gta");//atgaaagga
		Component child24=SBOLAPI.createDnaComponent(doc, "child24", "child24", "child24", Role.EngineeredRegion, "tgc");//atgaaagga
		Component child46=SBOLAPI.createDnaComponent(doc, "child46", "child46", "child46", Role.EngineeredRegion, "cgt");//atgaaagga
		Component child14=SBOLAPI.createDnaComponent(doc, "child14", "child14", "child14", Role.EngineeredRegion, "atgc");//atgaaagga
		Component child15=SBOLAPI.createDnaComponent(doc, "child15", "child15", "child15", Role.EngineeredRegion, "atgcg");//atgaaagga
		
		Component child1357=SBOLAPI.createDnaComponent(doc, "child1357", "child1357", "child1357", Role.EngineeredRegion, "atggta");//atgaaagga
		Component child1457=SBOLAPI.createDnaComponent(doc, "child1457", "child1457", "child1457", Role.EngineeredRegion, "atgcgta");//atgaaagga
		Component child1458=SBOLAPI.createDnaComponent(doc, "child1458", "child1458", "child1458", Role.EngineeredRegion, "aggaaaga");//atgaaagga
		
		Component child89=SBOLAPI.createDnaComponent(doc, "child89", "child89", "child89", Role.EngineeredRegion, "aa");//atgaaagga
		
		Component child9_12__14_17__19_23=SBOLAPI.createDnaComponent(doc, "child9_12__14_17_19_23", "child9_12__14_17_19_23", "child9_12__14_17_19_23", Role.EngineeredRegion, "aaggaagactttt");//atgaaagga
		Component child10_11__15_16__20_22=SBOLAPI.createDnaComponent(doc, "child10_11__15_16__20_22", "child10_11__15_16__20_22", "child10_11__15_16__20_22", Role.EngineeredRegion, "agagttt");//atgaaagga
		Component child10_11__15_16=SBOLAPI.createDnaComponent(doc, "child10_11__15_16", "child10_11__15_16", "child10_11__15_16", Role.EngineeredRegion, "agag");//atgaaagga
		Component child9_12__14_17=SBOLAPI.createDnaComponent(doc, "child9_12__14_17", "child9_12__14_17", "child9_12__14_17", Role.EngineeredRegion, "aaggaaga");//atgaaagga
		
		
		SubComponent feature13=gfp.createSubComponent(child13);
		SubComponent feature57=gfp.createSubComponent(child57);
		SubComponent feature24=gfp.createSubComponent(child24);
		SubComponent feature46=gfp.createSubComponent(child46);
		SubComponent feature14=gfp.createSubComponent(child14);
		SubComponent feature15=gfp.createSubComponent(child15);
		SubComponent feature13_v2=gfp.createSubComponent(child13);
		SubComponent feature1357=gfp.createSubComponent(child1357);
		SubComponent feature1457=gfp.createSubComponent(child1457);
		SubComponent feature1458=gfp.createSubComponent(child1458);
		SubComponent feature9_12__14_17__19_23=gfp.createSubComponent(child9_12__14_17__19_23);
		SubComponent feature89=gfp.createSubComponent(child89);
		SubComponent feature10_11__15_16__20_22=gfp.createSubComponent(child10_11__15_16__20_22);
		SubComponent feature10_11__15_16=gfp.createSubComponent(child10_11__15_16);
		SubComponent feature9_12__14_17=gfp.createSubComponent(child9_12__14_17);
		
		
		feature13.createRange(1, 3, seq);   
		feature57.createRange(5, 7, seq);
		feature24.createRange(2, 4, seq);
		feature46.createRange(4, 6, seq);
		feature14.createRange(1, 4, seq);
		feature15.createRange(1, 5, seq);
		feature13_v2.createRange(1, 3, seq); 
		
		feature1357.createRange(1, 3, seq);   
		feature1357.createRange(5, 7, seq);   
		
		feature1457.createRange(1, 4, seq);   
		feature1457.createRange(5, 7, seq); 
		
		feature1458.createRange(1, 4, seq); 
		feature1458.createRange(5, 8, seq); 
		
		feature9_12__14_17__19_23.createRange(9, 12, seq);
		feature9_12__14_17__19_23.createRange(14, 17, seq);
		feature9_12__14_17__19_23.createRange(19, 23, seq);
		
		feature89.createRange(8, 9, seq); 
		
		feature10_11__15_16__20_22.createRange(10, 11, seq);
		feature10_11__15_16__20_22.createRange(15, 16, seq);
		feature10_11__15_16__20_22.createRange(20, 22, seq);
		
		
		feature10_11__15_16.createRange(10, 11, seq);
		feature10_11__15_16.createRange(15, 16, seq);
		
		feature9_12__14_17.createRange(9, 12, seq);
		feature9_12__14_17.createRange(14, 17, seq);
		
		
		SequenceFeature cut1=gfp.createSequenceFeature(1, seq);
		SequenceFeature entire=gfp.createSequenceFeature(seq);
		
		TestUtil.validateDocument(doc,0, null, null);
			
		//Precedes - No error
		Constraint const_1357=gfp.createConstraint(SequentialRestriction.precedes, feature13, feature57);
	    TestUtil.validateIdentifiedOnly(doc, const_1357,0, null, null);
	    
	    //Precedes No error
	    Constraint const_1324=gfp.createConstraint(SequentialRestriction.precedes, feature13, feature24);
	    TestUtil.validateIdentifiedOnly(doc, const_1324,0, null, null);  
	    
	    //Precedes - Error
	    Constraint const_5713=gfp.createConstraint(SequentialRestriction.precedes, feature57, feature13);
	    TestUtil.validateIdentifiedOnly(doc, const_5713,1,"sbol3-11706", "Constraint_const_5713");
	    
	    
	    //Strictly precedes - No error
	    Constraint const_1346_sp=gfp.createConstraint(SequentialRestriction.strictlyPrecedes, feature13, feature46);
	    TestUtil.validateIdentifiedOnly(doc, const_1346_sp,0, null, null);
	
	    //Strictly precedes - Error
	    Constraint const_1324_sp=gfp.createConstraint(SequentialRestriction.strictlyPrecedes, feature13, feature24);	   
	    TestUtil.validateIdentifiedOnly(doc, const_1324_sp, 1, "sbol3-11706", "Constraint_const_1324_sp");
	  
	    
	    //Meets -  No Error   
	    Constraint const_2446_meets=gfp.createConstraint(SequentialRestriction.meets, feature24, feature46);
	    TestUtil.validateIdentifiedOnly(doc, const_2446_meets,0, null, null);
	  
	    //Meets -  Error
	    Constraint const_1346_meets=gfp.createConstraint(SequentialRestriction.meets, feature13, feature46);
	    TestUtil.validateIdentifiedOnly(doc, const_1346_meets,1,"sbol3-11706", "Constraint_const_1346_meets");
	
	    //Meets - Error
	    Constraint const_1324_meets=gfp.createConstraint(SequentialRestriction.meets, feature13, feature24);
	    TestUtil.validateIdentifiedOnly(doc, const_1324_meets,1,"sbol3-11706", "Constraint_const_1324_meets");
	    
	   
	    //Starts -  No Error   
	    Constraint const_1314_starts=gfp.createConstraint(SequentialRestriction.starts, feature13, feature14);
	    TestUtil.validateIdentifiedOnly(doc, const_1314_starts,0, null, null);
	  
	    //Starts -  Error
	    Constraint const_1324_starts=gfp.createConstraint(SequentialRestriction.starts, feature13, feature24);
	    TestUtil.validateIdentifiedOnly(doc, const_1324_starts,1,"sbol3-11706", "Constraint_const_1324_starts");
	    
	    //Starts -  Error
	    Constraint const_4613_starts=gfp.createConstraint(SequentialRestriction.starts, feature46, feature13);
	    TestUtil.validateIdentifiedOnly(doc, const_4613_starts,1,"sbol3-11706", "Constraint_const_4613_starts");
	    
	    
	    //Finishes -  No Error   
	    Constraint const_2414_finishes=gfp.createConstraint(SequentialRestriction.finishes, feature24, feature14);
	    TestUtil.validateIdentifiedOnly(doc, const_2414_finishes,0, null, null);
	  
	    //Finishes -  Error
	    Constraint const_1324_finishes=gfp.createConstraint(SequentialRestriction.finishes, feature13, feature24);
	    TestUtil.validateIdentifiedOnly(doc, const_1324_finishes,1,"sbol3-11706", "Constraint_const_1324_finishes");
	    
	  
	    //Overlaps -  No Error   
	    Constraint const_2414_overlaps=gfp.createConstraint(SequentialRestriction.overlaps, feature24, feature14);
	    TestUtil.validateIdentifiedOnly(doc, const_2414_overlaps,0, null, null);
	  
	    //Overlaps -  No Error   
	    Constraint const_2446_overlaps=gfp.createConstraint(SequentialRestriction.overlaps, feature24, feature46);
	    TestUtil.validateIdentifiedOnly(doc, const_2446_overlaps,0, null, null);
	
	    //Overlaps -  Error
	    Constraint const_1357_overlaps=gfp.createConstraint(SequentialRestriction.overlaps, feature13, feature57);
	    TestUtil.validateIdentifiedOnly(doc, const_1357_overlaps,1,"sbol3-11706", "Constraint_const_1357_overlaps");
	
	  
	    //Contains  -  No Error
	    Constraint const_1413_contains=gfp.createConstraint(SequentialRestriction.contains, feature14, feature13);
	    TestUtil.validateIdentifiedOnly(doc, const_1413_contains,0, null, null);
	    
	    //Contains  -  Error
	    Constraint const_1314_contains=gfp.createConstraint(SequentialRestriction.contains, feature13, feature14);
	    TestUtil.validateIdentifiedOnly(doc, const_1314_contains,1,"sbol3-11706", "Constraint_const_1314_contains");
	    
	    
	    //Strictly Contains -  No Error   
	    Constraint const_1524_sc=gfp.createConstraint(SequentialRestriction.strictlyContains, feature15, feature24);
	    TestUtil.validateIdentifiedOnly(doc, const_1524_sc,0, null, null);
	
	    //Strictly Contains  -  Error
	    Constraint const_1413_sc=gfp.createConstraint(SequentialRestriction.strictlyContains, feature14, feature13);
	    TestUtil.validateIdentifiedOnly(doc, const_1413_sc,1,"sbol3-11706", "Constraint_const_1413_sc");
	
	
	    //Equals -  No Error   
	    Constraint const_1313_equals=gfp.createConstraint(SequentialRestriction.equals, feature13, feature13_v2);
	    TestUtil.validateIdentifiedOnly(doc, const_1313_equals,0, null, null);
	
	    //Equals  -  Error
	    Constraint const_1314_equals=gfp.createConstraint(SequentialRestriction.equals, feature13, feature14);
	    TestUtil.validateIdentifiedOnly(doc, const_1314_equals,1,"sbol3-11706", "Constraint_const_1314_equals");
	
	    
	    //Range vs cut
	    //Starts -  No Error   
	    Constraint const_1_13_starts=gfp.createConstraint(SequentialRestriction.starts, cut1, feature13);
	    TestUtil.validateIdentifiedOnly(doc, const_1_13_starts,0, null, null);
	    
	    //Starts -  No Error   
	    Constraint const_1_24_starts=gfp.createConstraint(SequentialRestriction.starts, cut1, feature24);
	    TestUtil.validateIdentifiedOnly(doc, const_1_24_starts,1,"sbol3-11706", "Constraint_const_1_24_starts");
	    
	    
	    //Range vs entire sequence
	    //Starts -  No Error   
	    Constraint const_13_entire_starts=gfp.createConstraint(SequentialRestriction.starts, feature13, entire);
	    TestUtil.validateIdentifiedOnly(doc, const_13_entire_starts,0, null, null);
	    
	    //Starts -  No Error   
	    Constraint const_entire_13_starts=gfp.createConstraint(SequentialRestriction.starts, entire, feature13);
	    TestUtil.validateIdentifiedOnly(doc, const_entire_13_starts,1,"sbol3-11706", "Constraint_const_entire_13_starts");
	    
		/*********************
	     * Multiple locations
	    *********************/
	    //Starts -  No Error   
	    Constraint const_1357_entire_starts=gfp.createConstraint(SequentialRestriction.starts, feature1357, entire);
	    TestUtil.validateIdentifiedOnly(doc, const_1357_entire_starts,0, null, null);
	  
	    //Starts -  Error   
	    Constraint const_1357_1457_starts=gfp.createConstraint(SequentialRestriction.starts, feature1357, feature1457);
	    TestUtil.validateIdentifiedOnly(doc, const_1357_1457_starts,1,"sbol3-11706", "Constraint_const_1357_1457_starts");
	    
	    //Starts -  No Error   
	    Constraint const_1357_1458_starts=gfp.createConstraint(SequentialRestriction.starts, feature1357, feature1458);
	    TestUtil.validateIdentifiedOnly(doc, const_1357_1458_starts,0, null, null);
	    
	    //Overlaps - No error
	    Constraint const_1357_1457_overlaps=gfp.createConstraint(SequentialRestriction.overlaps, feature1357, feature1457);
	    TestUtil.validateIdentifiedOnly(doc, const_1357_1457_overlaps,0, null, null);
	    
	    //Overlaps - No Error
	    Constraint const_13_1457_overlaps=gfp.createConstraint(SequentialRestriction.overlaps, feature13, feature1457);
	    TestUtil.validateIdentifiedOnly(doc, const_13_1457_overlaps,0, null, null);
	    
	    //Overlaps - Error
	    Constraint const_9_12__14_17__19_23_overlaps_1457=gfp.createConstraint(SequentialRestriction.overlaps, feature9_12__14_17__19_23, feature1457);
	    TestUtil.validateIdentifiedOnly(doc, const_9_12__14_17__19_23_overlaps_1457,1,"sbol3-11706", "Constraint_const_9_12__14_17__19_23_overlaps_1457");
	    
	    //Overlaps - No Error
	    Constraint const_9_12__14_17__19_23_overlaps_89=gfp.createConstraint(SequentialRestriction.overlaps, feature9_12__14_17__19_23, feature89);
	    TestUtil.validateIdentifiedOnly(doc, const_9_12__14_17__19_23_overlaps_89,0, null, null);
	    
	    //Overlaps - No Error
	    Constraint const_9_12__14_17__19_23_overlaps_10_11__15_16__20_22=gfp.createConstraint(SequentialRestriction.overlaps, feature9_12__14_17__19_23, feature10_11__15_16__20_22);
	    TestUtil.validateIdentifiedOnly(doc, const_9_12__14_17__19_23_overlaps_10_11__15_16__20_22,0, null, null);
	    
	    
	    //Strictly Contains - No Error
	    Constraint const_9_12__14_17__19_23_sc_10_11__15_16__20_22=gfp.createConstraint(SequentialRestriction.strictlyContains, feature9_12__14_17__19_23, feature10_11__15_16__20_22);
	    TestUtil.validateIdentifiedOnly(doc, const_9_12__14_17__19_23_sc_10_11__15_16__20_22,0, null, null);
	    
	    //Strictly Contains - Error
	    Constraint const_9_12__14_17__19_23_sc_10_11__15_16=gfp.createConstraint(SequentialRestriction.strictlyContains, feature9_12__14_17__19_23, feature10_11__15_16);
	    TestUtil.validateIdentifiedOnly(doc, const_9_12__14_17__19_23_sc_10_11__15_16,0,null, null);
	    
	    //Strictly Contains - Error
	    Constraint const_9_12__14_17_sc_10_11__15_16__20_22=gfp.createConstraint(SequentialRestriction.strictlyContains, feature9_12__14_17, feature10_11__15_16__20_22);
	    TestUtil.validateIdentifiedOnly(doc, const_9_12__14_17_sc_10_11__15_16__20_22,1,"sbol3-11706", "Constraint_const_9_12__14_17_sc_10_11__15_16__20_22");
	  
	    //Strictly Contains - Error
	    Constraint const_9_12__14_17__19_23_equals_9_12__14_17=gfp.createConstraint(SequentialRestriction.equals, feature9_12__14_17__19_23, feature9_12__14_17);
	    TestUtil.validateIdentifiedOnly(doc, const_9_12__14_17__19_23_equals_9_12__14_17,1,"sbol3-11706", "Constraint_const_9_12__14_17__19_23_equals_9_12__14_17");
	  
    }
}
