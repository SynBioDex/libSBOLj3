package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Represents participation roles in the SBOL data model.
 *
 */
public enum ParticipationRole implements HasURI {
	Inhibitor(URINameSpace.SBO.local("0000020")),
	Inhibited(URINameSpace.SBO.local("0000642")),
	Stimulator(URINameSpace.SBO.local("0000459")),
	Stimulated(URINameSpace.SBO.local("0000643")),
	Reactant (URINameSpace.SBO.local("0000010")),
	Product(URINameSpace.SBO.local("0000011")),
	Promoter(URINameSpace.SBO.local("0000598")),
	Modifier(URINameSpace.SBO.local("0000019")),
	Modified(URINameSpace.SBO.local("0000644")),
	Template(URINameSpace.SBO.local("0000645"));
	
	private URI uri;

	ParticipationRole(URI uri) {
		this.uri = uri;
	}
	
	/**
	 * Gets the URI for the associated participation role.
	 * @return URI The URI identifying the ParticipationRole object.
	 */
	public URI getUri() {
		return uri;
	}
	
	/**
	 * Gets a set of all URIs associated with participation roles.
	 * @return A set object containing all associated URIs.
	 */
	public static Set<URI> getUris() {
		return lookup.keySet();
	}
	
	private static final Map<URI, ParticipationRole> lookup = new HashMap<>();

	static {
		for (ParticipationRole value : ParticipationRole.values()) {
			lookup.put(value.getUri(), value);
		}
	}
	
	/**
	 * Gets the participation role associated with a supplied URL.
	 * @param url The URL associated with the participation role.
	 * @return A ParticiationRole object identified by the supplied URI.
	 */
	public static ParticipationRole get(URI url) {
		return lookup.get(url);
	}
}