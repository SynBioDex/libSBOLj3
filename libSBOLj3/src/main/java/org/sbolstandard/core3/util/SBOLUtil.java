package org.sbolstandard.core3.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.CombinatorialDerivation;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.EntireSequence;
import org.sbolstandard.core3.entity.ExperimentalData;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.Location;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.TopLevel;
import org.sbolstandard.core3.entity.measure.BinaryPrefix;
import org.sbolstandard.core3.entity.measure.PrefixedUnit;
import org.sbolstandard.core3.entity.measure.SIPrefix;
import org.sbolstandard.core3.entity.measure.SingularUnit;
import org.sbolstandard.core3.entity.measure.UnitDivision;
import org.sbolstandard.core3.entity.measure.UnitExponentiation;
import org.sbolstandard.core3.entity.measure.UnitMultiplication;
import org.sbolstandard.core3.entity.provenance.Activity;
import org.sbolstandard.core3.entity.provenance.Agent;
import org.sbolstandard.core3.entity.provenance.Plan;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;


public class SBOLUtil {

	 public static boolean isURL(String s){
	        try{
	            URL url = new URL(s);
	            url.toURI();
	            return true;
	        }catch(MalformedURLException e){
	            return false;
	        } catch (URISyntaxException e) {
	            return false;
	        }
	    }
	 
	 public static void sort(File inputFile, File outputFile, Charset encoding) throws IOException
	 {
		 FileInputStream inputStream=new FileInputStream(inputFile);
		 FileOutputStream outputStream=new FileOutputStream(outputFile);
		sort(inputStream, encoding, outputStream);
			
		if (outputStream!=null){
			outputStream.close();
		}
	 }
	 
	 public static String sort(String input, Charset encoding) throws IOException
	 {
			ByteArrayInputStream inputStream=new ByteArrayInputStream(input.getBytes());
			ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
			
			sort(inputStream, encoding, outputStream);
			String output = new String(outputStream.toString());
			
			if (outputStream!=null)
		 	{
		 		outputStream.close();
		 	}
		 	return output;
		 	
	 }
	 
	 public static void sort(InputStream inputStream, Charset encoding, OutputStream outputStream) throws IOException
	 {
			List<String> lineList=IOUtils.readLines(inputStream,encoding);
		 	Collections.sort(lineList);
			IOUtils.writeLines(lineList, null, outputStream, encoding);
			if (inputStream!=null)
			{
				inputStream.close();
			}
	 }
	 
	 public static List<URI> filterItems2(SBOLDocument document, List<URI> identifieds, URI property, URI value)
	 {
		return RDFUtil.filterItems(document.getRDFModel(), identifieds, property, value.toString());
	 }
	 
	 public static <T extends Identified> boolean exists(URI uri, List<T> identifieds)
		{
			boolean exists=false;
			if (uri!=null && identifieds!=null && identifieds.size()>0)	
			{
				for (Identified identified:identifieds)
				{
					if (uri.equals(identified.getUri()))
					{
						exists=true;
						break;
					}
				}
			}
			return exists;
		}
	 
	 
	 public static boolean includesMultipleRootComponentTypes(List<URI> types)
	 {
		 boolean result=false;
		 if (types!=null && types.size()>0){
			int counter=0;
			ComponentType[] typeValues=ComponentType.values();
			for (int i=0;i<typeValues.length;i++){
				if (types.contains(typeValues[i].getUri())){
					counter++;
				}
				if (counter==2) {
					result=true;
					break;
				}		
			}
		}
	 	return result;
	 }
	 
	 public static String toQualifiedString(URI uri)
		{
			for (URI key:lookup.keySet())
			{
				String uriString=uri.toString().toLowerCase();
				String keyString=key.toString().toLowerCase();
				int index=uriString.indexOf(keyString);
				if (index>-1)
				{
					String subUri=uri.toString().substring(index + keyString.length());
					//return (lookup.get(key)).prefix + ":" + subUri; 
					return subUri; 
				}
			}
			return uri.toString();
		}
	 
	 private static final Map<URI, URINameSpace> lookup = new HashMap<>();
	  
	    static
	    {
	    	lookup.put(URINameSpace.SBOL.uri, URINameSpace.SBOL);
	        lookup.put(URINameSpace.SO.uri, URINameSpace.SO);
	        lookup.put(URINameSpace.SBO.uri, URINameSpace.SBO);
	        lookup.put(URINameSpace.CHEBI.uri, URINameSpace.CHEBI);
	        lookup.put(URINameSpace.GO.uri, URINameSpace.GO);
	        lookup.put(URINameSpace.EDAM.uri, URINameSpace.EDAM);
	        lookup.put(URINameSpace.PROV.uri, URINameSpace.PROV);
	        lookup.put(URINameSpace.OM.uri, URINameSpace.OM);
	        lookup.put(URINameSpace.RDFS.uri, URINameSpace.RDFS); 
	    }
	    
	 /*public static List<URI> filterItems(Identified identified, List<URI> identifieds, URI property, URI value)
	 {
		return RDFUtil.filterItems(identified.resource.getRDFModel(), identifieds, property, value.toString());
	 }*/
	    
	    public static URI toNameSpace(URI uri)
		{
			String uriString=uri.toString();
			if (uriString.endsWith("/"))
			{
				uriString=uriString.substring(0, uriString.length()-1);
			}
			return URI.create(uriString);
		}
	   
	    
	    public static <T extends Identified>  boolean contains(Collection<T> identifieds, T identified)
	    {
	    	boolean contains=false;
	    	if (identifieds!=null && identifieds.size()>0 && identified!=null)
	    	{
	    		contains=SBOLUtil.getURIs(identifieds).contains(identified.getUri());
	    	}
	    	return contains;
	    }
		
	    
	    public static <T extends Identified>  List<URI> getURIs(Collection<T> identifieds)
		{
			ArrayList<URI> uris=null;
			if (identifieds!=null && identifieds.size()>0 )
			{
				uris=new ArrayList<URI>();
				for (Identified identified:identifieds)
				{
					uris.add(identified.getUri());
				}
			}
			return uris;
		}
		
	    public static URI toURI(Identified identified)
	    {
	    	URI uri=null;
			if (identified!=null)
			{
				uri=identified.getUri();
			}
			return uri;
	    }
	    
	    
	    /*private static String readFromFileResource(String fileResource) throws IOException
		{
			try
			{
				File file = new File(SBOLAPI.class.getClassLoader().getResource(fileResource).getFile());
				return IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
			}
			catch (Exception e)
			{
				throw new IOException("Could not load the file " + fileResource,e);
			}
		}*/
	    
	    public static Model getModelFromFileResourceOld(String fileResource, Lang lang) {
	        File file = new File(SBOLAPI.class.getClassLoader().getResource(fileResource).getFile());
	        return RDFDataMgr.loadModel(file.getPath(), lang);
	    }
	    
	    
	    public static Model getModelFromFileResource(String fileResource,RDFFormat format) throws FileNotFoundException {
	    	InputStream is = SBOLAPI.class.getClassLoader().getResourceAsStream(fileResource);
	    	Model model= RDFUtil.read(is, format);
	    	//RDFDataMgr.read(null, is, lang);
	    	return model;
	    	
	    }
	    
	    
	    public static boolean isNullOrEmpty(Optional<?> optional)
	    {
	    	return (optional==null || optional.isEmpty());
	    }
	   
	    
	    public static void printValidationMessages(List<String> messages)
		{	 
	    	if (messages!=null && messages.size()>0)
	        {
				for (String message: messages){
		        	System.out.println(message);
		        } 
	        }
	        else
	        {
	        	System.out.println("\tNo errors!");
	        }
	        System.out.println("--------------------");
		}
	    
	    
	    public static Set<URI> addToSet(Set<URI> current, URI item){
			if (item!=null){
				if (current==null){
					current=new HashSet<URI>();
				}
				current.add(item);
			}
			return current;
		}
	    
	    
		public static String getDateTimeString (int year, int month, int day, int hour, int min, int sec) throws SBOLGraphException
	    {
	    	String dateTimeString = null;			
	    	if (year>=1900 && day>=1 && day<=31 && month>=1 && month<=12 && hour>=0 && hour<=23 && min>=0 && min<=59 && sec>=0 && sec<=59){
				 Calendar calendar=Calendar.getInstance();
			     TimeZone timeZone=calendar.getTimeZone();		     
				 calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
			     calendar.set(year,month-1,day,hour,min,sec);
			     calendar.set(Calendar.MILLISECOND,0);   
			     XSDDateTime dateTime= new XSDDateTime(calendar);
			     dateTimeString= dateTime.toString();
			     calendar.setTimeZone(timeZone);		     
			}
	    	else{
	    		String message=String.format("Invalid datetime. Year:%d, Month:%d, Day:%d, hour: %d, min:%d, sec:%d", year, month, day, hour, min, sec);
	    		throw new SBOLGraphException(message);
	    	}
	    	return dateTimeString;
	    }

		public static String getDateTimeString (int year, int month, int day, int hour, int min, int sec, int millisecond) throws SBOLGraphException
	    {
	    	String dateTimeString = null;			
	    	if (year>=1900 && day>=1 && day<=31 && month>=1 && month<=12 && hour>=0 && hour<=23 && min>=0 && min<=59 && sec>=0 && sec<=59){
				 Calendar calendar=Calendar.getInstance();
			     TimeZone timeZone=calendar.getTimeZone();		     
				 calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
			     calendar.set(year,month-1,day,hour,min,sec);
			     calendar.set(Calendar.MILLISECOND,millisecond);   
			     XSDDateTime dateTime= new XSDDateTime(calendar);
			     dateTimeString= dateTime.toString();
			    // calendar.setTimeZone(timeZone);		     
			}
	    	else{
	    		String message=String.format("Invalid datetime. Year:%d, Month:%d, Day:%d, hour: %d, min:%d, sec:%d", year, month, day, hour, min, sec);
	    		throw new SBOLGraphException(message);
	    	}
	    	return dateTimeString;
	    }
		
		public static boolean hasEmptyEntireSequence(List<Location> locations) throws SBOLGraphException
		{
			boolean result= false;
			if (locations!=null){
				for (Location location: locations){
					if (location instanceof EntireSequence){
						EntireSequence entireSeqLoc=(EntireSequence) location;
						String elements= entireSeqLoc.getSequence().getElements();
						if (elements==null || elements.isEmpty()){
							result= true;
							break;
						}
						
					}
				}
			}
			return result;		
		}


	public static Set<URI> getTopLevelResourceTypes(){

			List<URI> types = Arrays.asList(DataModel.Component.uri,
					DataModel.Sequence.uri,
					DataModel.Model.uri,
					DataModel.Implementation.uri,
					DataModel.ExperimentalData.uri,
					DataModel.Attachment.uri,
					DataModel.Collection.uri,
					DataModel.CombinatorialDerivation.uri,
					DataModel.TopLevel.uri,
					ProvenanceDataModel.Agent.uri,
					ProvenanceDataModel.Plan.uri,
					ProvenanceDataModel.Activity.uri,
					MeasureDataModel.Measure.uri,
					MeasureDataModel.SIPrefix.uri,
					MeasureDataModel.BinaryPrefix.uri,
					MeasureDataModel.SingularUnit.uri,
					MeasureDataModel.UnitMultiplication.uri,
					MeasureDataModel.UnitDivision.uri,
					MeasureDataModel.UnitExponentiation.uri,
					MeasureDataModel.PrefixedUnit.uri
					);
			Set<URI> topLevelResourceTypes=new HashSet<URI>(types);			
		return topLevelResourceTypes;
	}

	public static Map<Class<? extends TopLevel>, URI> getTopLevelClassURITypeMappings(){
			Map<Class<? extends TopLevel>, URI> types = new HashMap<>();
			types.put(Component.class, DataModel.Component.uri);
			types.put(Sequence.class, DataModel.Sequence.uri);
			types.put(org.sbolstandard.core3.entity.Model.class, DataModel.Model.uri);
			types.put(Implementation.class, DataModel.Implementation.uri);
			types.put(ExperimentalData.class, DataModel.ExperimentalData.uri);
			types.put(Attachment.class, DataModel.Attachment.uri);
			types.put(org.sbolstandard.core3.entity.Collection.class, DataModel.Collection.uri);
			types.put(CombinatorialDerivation.class, DataModel.CombinatorialDerivation.uri);
			types.put(TopLevel.class, DataModel.TopLevel.uri);
			types.put(Agent.class, ProvenanceDataModel.Agent.uri);
			types.put(Plan.class, ProvenanceDataModel.Plan.uri);
			types.put(Activity.class, ProvenanceDataModel.Activity.uri);
			types.put(SIPrefix.class, MeasureDataModel.SIPrefix.uri);
			types.put(BinaryPrefix.class, MeasureDataModel.BinaryPrefix.uri);
			types.put(SingularUnit.class, MeasureDataModel.SingularUnit.uri);
			types.put(UnitMultiplication.class, MeasureDataModel.UnitMultiplication.uri);
			types.put(UnitDivision.class, MeasureDataModel.UnitDivision.uri);
			types.put(UnitExponentiation.class, MeasureDataModel.UnitExponentiation.uri);
			types.put(PrefixedUnit.class, MeasureDataModel.PrefixedUnit.uri);
			return types;
	}
	
}