package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents constraint restrictions in the SBOL data model.
 *
 */
public class RestrictionType {
	
	/**
	 * Interface for constraint restriction, extending a class to have base URI properties.
	 */
	public interface ConstraintRestriction extends HasURI
	{
		
	}
	
	/**
	 * Represents an orientation restriction, itself a type of constraint restriction.
	 */
	public enum OrientationRestriction implements ConstraintRestriction
	{
		sameOrientationAs(URINameSpace.SBOL.local("sameOrientationAs")),
		oppositeOrientationAs(URINameSpace.SBOL.local("oppositeOrientationAs"));
		
		private URI uri;

		OrientationRestriction(URI uri) {
			this.uri = uri;
		}
		
		/**
		 * Gets the URI for the corresponding OrientationRestriction object.
		 * @return The URI that identifies the orientation restriction.
		 */
		public URI getUri() {
			return uri;
		}
		
		/**
		 * Gets the URIs associated with orientation restrictions.
		 * @return A set object containing all relevant URIs.
		 */
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, OrientationRestriction> lookup = new HashMap<>();

		static {
			for (OrientationRestriction value : OrientationRestriction.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		/**
		 * Gets the orientation restriction from a supplied URL.
		 * @param url The URL to be accessed.
		 * @return An OrientationRestriction object identified by the URL.
		 */
		public static OrientationRestriction get(URI url) {
			return lookup.get(url);
		}
	}
	
	/**
	 * Represents an identity restriction, itself a type of constraint restriction.
	 */
	public enum IdentityRestriction implements ConstraintRestriction
	{
		verifyIdentical(URINameSpace.SBOL.local("verifyIdentical")),
		differentFrom(URINameSpace.SBOL.local("differentFrom")),
		replaces(URINameSpace.SBOL.local("replaces"));
		
		private URI uri;

		IdentityRestriction(URI uri) {
			this.uri = uri;
		}
		
		/**
		 * Gets the URI for the corresponding IdentityRestriction object.
		 * @return The URI that identifies the identity restriction.
		 */
		public URI getUri() {
			return uri;
		}
		
		/**
		 * Gets the URIs associated with orientation restrictions.
		 * @return A set object containing all relevant URIs.
		 */
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, IdentityRestriction> lookup = new HashMap<>();

		static {
			for (IdentityRestriction value : IdentityRestriction.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		/**
		 * Gets the identity restriction from the supplied URL.
		 * @param url The URL to be accessed.
		 * @return An IdentityRestriction object identified by the URL.
		 */
		public static IdentityRestriction get(URI url) {
			return lookup.get(url);
		}
	}
	
	/**
	 * 
	 */
	public enum TopologyRestriction implements ConstraintRestriction
	{
		isDisjointFrom(URINameSpace.SBOL.local("isDisjointFrom")),
		strictlyContains(URINameSpace.SBOL.local("strictlyContains")),
		contains(URINameSpace.SBOL.local("contains")),
		equals(URINameSpace.SBOL.local("equals")),
		meets(URINameSpace.SBOL.local("meets")),
		covers(URINameSpace.SBOL.local("covers")),
		overlaps(URINameSpace.SBOL.local("overlaps"));
		
		private URI uri;

		TopologyRestriction(URI uri) {
			this.uri = uri;
		}

		public URI getUri() {
			return uri;
		}
		
		/**
		 * Gets a list of all URIs associated with topology restrictions.
		 * @return A set object containing the relevant URIs
		 */
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, TopologyRestriction> lookup = new HashMap<>();

		static {
			for (TopologyRestriction value : TopologyRestriction.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		/**
		 * Gets a topology restriction from the supplied URL.
		 * @param url The URL to be accessed.
		 * @return A TopologyRestriction object identified by the URL.
		 */
		public static TopologyRestriction get(URI url) {
			return lookup.get(url);
		}
	}
	
	/**
	 * 
	 * Represents a sequential restriction, itself a form of constraint restriction.
	 *
	 */
	public enum SequentialRestriction implements ConstraintRestriction
	{
		precedes(URINameSpace.SBOL.local("precedes")),
		strictlyPrecedes(URINameSpace.SBOL.local("strictlyPrecedes")),
		meets(URINameSpace.SBOL.local("meets")),
		overlaps(URINameSpace.SBOL.local("overlaps")),
		contains(URINameSpace.SBOL.local("contains")),
		strictlyContains(URINameSpace.SBOL.local("strictlyContains")),
		equals(URINameSpace.SBOL.local("equals")),
		finishes(URINameSpace.SBOL.local("finishes")),	
		starts(URINameSpace.SBOL.local("starts"));	
		
		private URI uri;

		SequentialRestriction(URI uri) {
			this.uri = uri;
		}
		
		/**
		 * Gets the URI associated with this sequential restriction.
		 * @return The URI that identifies this sequential restriction.
		 */
		public URI getUri() {
			return uri;
		}
		
		/**
		 * Gets all URIs associated with sequential restrictions.
		 * @return A set object containing all URIs associated with sequential restrictions.
		 */
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, SequentialRestriction> lookup = new HashMap<>();

		static {
			for (SequentialRestriction value : SequentialRestriction.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		/**
		 * Gets the sequential restriction associated with the supplied URL.
		 * @param url The URL to be accessed.
		 * @return A SequentialRestriction object identified by the URL.
		 */
		public static SequentialRestriction get(URI url) {
			return lookup.get(url);
		}
		
		
		/**
		 * Checks to see if the supplied subject precedes the object.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the subject precedes the object.
		 */
		public static boolean checkPrecedes(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			return subject.getLeft() < object.getLeft();
		}
		
		/**
		 * Checks to see if the supplied subject strictly precedes the object.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the subject precedes the object.
		 */
		public static boolean checkStrictlyPrecedes(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			return subject.getRight() < object.getLeft();
		}
		
		/**
		 * Checks to see if the supplied subject meets the object.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the subject meets the object.
		 */
		public static boolean checkMeets(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			return subject.getRight() == object.getLeft();
		}
		
		/**
		 * Checks to see if the supplied subject starts a sequence.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the left side of the subject and object match and the subjects right side ends sooner the the object.
		 */
		public static boolean checkStarts(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			return subject.getLeft() == object.getLeft() && subject.getRight() < object.getRight();
		}
		
		/**
		 * Checks to see if the supplied subject ends a sequence.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the right side of the subject and object match and the subjects left side ends after the the object.
		 */
		public static boolean checkFinishes(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			return subject.getRight() == object.getRight() && subject.getLeft() > object.getLeft();
		}
		
		/**
		 * Checks to see if the supplied subject overlaps with the object.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the subject range overlaps with the object range.
		 */
		public static boolean checkOverlaps(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			//return existsBetween(subject.getLeft(), object.getLeft(), object.getRight()) || existsBetween(subject.getRight(), object.getLeft(), object.getRight()); 
			return existsBetween(subject.getLeft(), object.getLeft(), object.getRight()) || existsBetween(object.getLeft(), subject.getLeft(), subject.getRight()); 
		}
		
		/**
		 * Checks to see if the supplied subject contains the object.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the object range is within the range of the subject range.
		 */
		public static boolean checkContains(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			return subject.getLeft() <= object.getLeft() &&  subject.getRight() >= object.getRight();
		}
		
		/**
		 * Checks to see if the supplied subject strictly contains the object.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the object range is within the range of the subject range, but not the start and end points.
		 */
		public static boolean checkStrictlyContains(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			return subject.getLeft() < object.getLeft() &&  subject.getRight() > object.getRight();
		}
		
		/**
		 * Checks to see if the supplied subject and object ranges match.
		 * @param subject The subject to be compared.
		 * @param object The object to be compared.
		 * @return A boolean value, true if the left and right side of the ranges are identical.
		 */
		public static boolean checkEquals(Pair<Integer, Integer> subject, Pair<Integer, Integer> object)
		{
			return subject.getLeft() == object.getLeft() &&  subject.getRight() == object.getRight();
		}
		
		/**
		 * Checks to see if a value exists between a supplied start and end point.
		 * @param location The location to be tested.
		 * @param start The start of the accepted range.
		 * @param end The end of the accepted range.
		 * @return A boolean value, true if location is between the start and end points, inclusive.
		 */
		private static boolean existsBetween (int location, int start, int end)
		{
			return (location>=start && location<=end);
		}
	}	
	
	/**
	 * Gets all URIs associated with the different types of restrictions.
	 * @return A set object containing all relevant URIs.
	 */
	public static Set<URI> getUris()
	{
		Set<URI> all=new HashSet<URI>();
		all.addAll(OrientationRestriction.getUris());
		all.addAll(IdentityRestriction.getUris());
		all.addAll(TopologyRestriction.getUris());
		all.addAll(SequentialRestriction.getUris());
		return all;	
	}	
	
}

