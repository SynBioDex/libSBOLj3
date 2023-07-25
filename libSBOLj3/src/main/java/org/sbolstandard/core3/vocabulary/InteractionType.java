package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents interaction types in the SBOL data model.
 *
 */
public enum InteractionType implements HasURI{
	
		Inhibition(URINameSpace.SBO.local("0000169")),
		Stimulation(URINameSpace.SBO.local("0000170")),
		BiochemicalReaction(URINameSpace.SBO.local("0000176")),
		NonCovalentBinding(URINameSpace.SBO.local("0000177")),
		Degradation(URINameSpace.SBO.local("0000179")),
		GeneticProduction(URINameSpace.SBO.local("0000589")),
		Control(URINameSpace.SBO.local("0000168"));

		private URI uri;

		InteractionType(URI uri) {
			this.uri = uri;
		}
		
		/**
		 * Returns the URI for the associated InteractionType object.
		 */
		public URI getUri() {
			return uri;
		}
		
		/**
		 * Returns the URIs associated with interaction types.
		 * @return A set object containing all URIs associated with interaction types.
		 */
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, InteractionType> lookup = new HashMap<>();

		static {
			for (InteractionType value : InteractionType.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		/**
		 * Returns an interaction type from the supplied URL.
		 * @param url The URL identifying the interaction type.
		 * @return An InteractionType object associated with the supplied URL.
		 */
		public static InteractionType get(URI url) {
			return lookup.get(url);
		}
		
		/**
		 * Maps participation roles associated with an interaction type to a list.
		 * @param type The interaction type object to be inspected.
		 * @return A list object containing the participation roles associated with the interaction type.
		 */
		public static List<ParticipationRole> mapParticipationRoles(InteractionType type) {
			switch (type) {
			case Inhibition:
				return Arrays.asList(ParticipationRole.Inhibited, ParticipationRole.Inhibitor, ParticipationRole.Promoter);
			case BiochemicalReaction:
				return Arrays.asList(ParticipationRole.Reactant, ParticipationRole.Product, ParticipationRole.Modifier, ParticipationRole.Modified);
			case Control:
				return Arrays.asList(ParticipationRole.Modifier, ParticipationRole.Modified);
			case Degradation:
				return Arrays.asList(ParticipationRole.Reactant);
			case GeneticProduction:
				return Arrays.asList(ParticipationRole.Product, ParticipationRole.Promoter, ParticipationRole.Template);
			case NonCovalentBinding:
				return Arrays.asList(ParticipationRole.Reactant, ParticipationRole.Product);
			case Stimulation:
				return Arrays.asList(ParticipationRole.Stimulator, ParticipationRole.Stimulated, ParticipationRole.Promoter);
			default:
				return null;
			}
		}
}
