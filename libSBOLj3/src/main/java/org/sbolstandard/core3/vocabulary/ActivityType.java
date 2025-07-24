package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents an activity type in the SBOL data model.
 *
 */
public enum ActivityType {
	Design(URINameSpace.SBOL.local("design")), 
	Build(URINameSpace.SBOL.local("build")), 
	Test(URINameSpace.SBOL.local("test")), 
	Learn(URINameSpace.SBOL.local("learn")); 
	
	private URI url;

	ActivityType(URI envUrl) {
		this.url = envUrl;
	}
	
	/**
	 * Gets the URI associated with this activity type.
	 * @return A URI object associated with this activity type.
	 */
	public URI getUri() {
		return url;
	}
	
	private static final Map<URI, ActivityType> lookup = new HashMap<>();
	private static final Map<ActivityType, ActivityType> lookupPreceding = new HashMap<>();
	
	    
	static{
		for(ActivityType activity: ActivityType.values()){
			lookup.put(activity.getUri(), activity);
		}

		lookupPreceding.put(Design, Learn);//Design's preceding type is Learn
		lookupPreceding.put(Build, Design);
		lookupPreceding.put(Test, Build);
		lookupPreceding.put(Learn, Test);
		

	}

	/**
	 * Gets the activity type from a URI.
	 * @param uri The URI associated with this activity type.
	 * @return The activity type associated with the supplied URI.
	 */
	public static ActivityType get(URI uri) {
		return lookup.get(uri);
	}
	
	/**
	 * Gets the preceding activity to this activity type.
	 * @param activity The activity to to be inspected.
	 * @return An activity type object representing the preceding activity.
	 */
	public static ActivityType getPreceding(ActivityType activity) {
		return lookupPreceding.get(activity);
	}
	
	/**
	 * Gets the URIs associated with this activity type.
	 * @return A set object containing the URIs associated with this activity type.
	 */
	public static Set<URI> getURIs() {
		return lookup.keySet();
	}
	    
}
