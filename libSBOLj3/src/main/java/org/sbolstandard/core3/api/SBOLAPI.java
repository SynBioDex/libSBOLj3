package org.sbolstandard.core3.api;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Constraint;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.Participation;
import org.sbolstandard.core3.entity.Range;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.entity.TopLevel;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.RestrictionType;

/**
 * 
 * @author gokselmisirli
 *
 */
public class SBOLAPI {

	public static List<Interaction> createInteraction(List<URI> interactionTypes, Component parent,
			Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles)
			throws SBOLGraphException {
		List<SubComponent> features1 = getOrCreateSubComponents(parent, participant1);
		List<SubComponent> features2 = getOrCreateSubComponents(parent, participant2);
		return createInteraction(interactionTypes, parent, (List<Feature>)(List<?>)features1, participant1Roles, (List<Feature>)(List<?>)features2, participant2Roles);
	}


	public static List<Interaction> createInteractionFromFeatures(List<URI> interactionTypes, Component parent,
			Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles)
			throws SBOLGraphException {
		List<SubComponent> features1 = getOrCreateSubComponents(parent, participant1);
		List<SubComponent> features2 = getOrCreateSubComponents(parent, participant2);
		return createInteraction(interactionTypes, parent, (List<Feature>)(List<?>)features1, participant1Roles, (List<Feature>)(List<?>)features2, participant2Roles);
	}

	/*
		DEL
		public static List<Interaction> createInteraction(List<URI> interactionTypes, Component parent,
			Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles)
			throws SBOLGraphException {
		List<Interaction> interactions = new ArrayList<Interaction>();
		List<SubComponent> features1 = getOrCreateSubComponents(parent, participant1);
		List<SubComponent> features2 = getOrCreateSubComponents(parent, participant2);
		if (features1 != null && features2 != null) {
			for (Feature feature1 : features1) {
				for (Feature feature2 : features2) {
					Interaction interaction = createInteraction(interactionTypes, parent, feature1, participant1Roles,
							feature2, participant2Roles);
					interactions.add(interaction);
				}
			}
		}
		return interactions;
	}
	*/

	public static List<Interaction> createInteraction(List<URI> interactionTypes, Component parent, List<Feature> participant1Features,
			 List<URI> participant1Roles, List<Feature> participant2Features, List<URI> participant2Roles)
			throws SBOLGraphException {
		List<Interaction> interactions = new ArrayList<Interaction>();
		if (participant1Features != null && participant2Features != null) {
			for (Feature feature1 : participant1Features) {
				for (Feature feature2 : participant2Features) {
					Interaction interaction = createInteraction(interactionTypes, parent, feature1, participant1Roles, feature2, participant2Roles);
					interactions.add(interaction);
				}
			}
		}
		return interactions;
	}

	
	//A + B --> C + D + E
	/* 
	A1,A2, B1, B2 --> C1, D1, D2, E1, E2, E3
	A1 + B1 --> C1 + D1 + E1
	A1 + B1 --> C1 + D1 + E2
	A1 + B1 --> C1 + D1 + E3
	A1 + B1 --> C1 + D2 + E1
	A1 + B1 --> C1 + D2 + E2
	A1 + B1 --> C1 + D2 + E3	
	A1 + B2 --> C1 + D1 + E1
	A1 + B2 --> C1 + D1 + E2
	A1 + B2 --> C1 + D1 + E3
	A1 + B2 --> C1 + D2 + E1
	A1 + B2 --> C1 + D2 + E2
	A1 + B2 --> C1 + D2 + E3	
	A2 + B1 --> C1 + D1 + E1
	A2 + B1 --> C1 + D1 + E2
	A2 + B1 --> C1 + D1 + E3
	A2 + B1 --> C1 + D2 + E1
	A2 + B1 --> C1 + D2 + E2
	A2 + B1 --> C1 + D2 + E3	
	A2 + B2 --> C1 + D1 + E1
	A2 + B2 --> C1 + D1 + E2
	A2 + B2 --> C1 + D1 + E3
	A2 + B2 --> C1 + D2 + E1
	A2 + B2 --> C1 + D2 + E2
	A2 + B2 --> C1 + D2 + E3	
	*/

	// A + B --> C + D + E [mod M]
	// where A, B, C, D, E, M are components and A1/A2, B1/B2, … are their subcomponents.
	// One interaction is created per combination of (one subcomponent per source) x
	// (one subcomponent per target) x (one subcomponent per modifier),
	// with all participants included in each interaction. Modifiers may be null.
	public static List<Interaction> createReactionInteraction(Component parent, List<Component> sources, List<URI> sourceParticipantRoles, List<Component> targets, List<URI> targetParticipantRoles, List<Component> modifiers, List<URI> modifierParticipantRoles, List<URI> interactionTypes) throws SBOLGraphException {
		List<Interaction> interactions = new ArrayList<Interaction>();

		// Collect the subcomponent list for each source component
		List<List<SubComponent>> sourceFeatureSets = new ArrayList<>();
		for (Component source : sources) {
			List<SubComponent> features = getOrCreateSubComponents(parent, source);
			if (features == null || features.isEmpty()) return interactions;
			sourceFeatureSets.add(features);
		}

		// Collect the subcomponent list for each target component
		List<List<SubComponent>> targetFeatureSets = new ArrayList<>();
		for (Component target : targets) {
			List<SubComponent> features = getOrCreateSubComponents(parent, target);
			if (features == null || features.isEmpty()) return interactions;
			targetFeatureSets.add(features);
		}

		// Collect the subcomponent list for each modifier component (optional)
		List<List<SubComponent>> modifierFeatureSets = new ArrayList<>();
		if (modifiers != null) {
			for (Component modifier : modifiers) {
				List<SubComponent> features = getOrCreateSubComponents(parent, modifier);
				if (features == null || features.isEmpty()) return interactions;
				modifierFeatureSets.add(features);
			}
		}
		
		List<List<SubComponent>> modifierCombos = modifierFeatureSets.isEmpty()
				? List.of(List.of())
				: cartesianProduct(modifierFeatureSets);

		// Enumerate every combination of (one subcomponent per source),
		// (one subcomponent per target), and (one subcomponent per modifier)
		for (List<SubComponent> sourceCombo : cartesianProduct(sourceFeatureSets)) {
			for (List<SubComponent> targetCombo : cartesianProduct(targetFeatureSets)) {				
				for (List<SubComponent> modifierCombo : modifierCombos) {
					Interaction interaction = parent.createInteraction(interactionTypes);
					for (SubComponent sf : sourceCombo) {
						createParticipation(interaction, sourceParticipantRoles, sf);
					}
					for (SubComponent tf : targetCombo) {
						createParticipation(interaction, targetParticipantRoles, tf);
					}
					for (SubComponent mf : modifierCombo) {
						createParticipation(interaction, modifierParticipantRoles, mf);
					}
					interactions.add(interaction);
				}
			}
		}
		return interactions;
	}


		public static List<Interaction> createReactionInteractionFromFeatures(Component parent, List<List<Feature>> sourceFeatureSets, List<URI> sourceParticipantRoles, List<List<Feature>> targetFeatureSets, List<URI> targetParticipantRoles, List<List<Feature>> modifierFeatureSets, List<URI> modifierParticipantRoles, List<URI> interactionTypes) throws SBOLGraphException {
		List<Interaction> interactions = new ArrayList<Interaction>();

		List<List<Feature>> modifierCombos = modifierFeatureSets == null || modifierFeatureSets.isEmpty()
				? List.of(List.of())
				: cartesianProduct(modifierFeatureSets);

		// Enumerate every combination of (one feature per source),
		// (one feature per target), and (one feature per modifier)
		for (List<Feature> sourceCombo : cartesianProduct(sourceFeatureSets)) {
			for (List<Feature> targetCombo : cartesianProduct(targetFeatureSets)) {				
				for (List<Feature> modifierCombo : modifierCombos) {
					Interaction interaction = parent.createInteraction(interactionTypes);
					for (Feature sf : sourceCombo) {
						createParticipation(interaction, sourceParticipantRoles, sf);
					}
					for (Feature tf : targetCombo) {
						createParticipation(interaction, targetParticipantRoles, tf);
					}
					for (Feature mf : modifierCombo) {
						createParticipation(interaction, modifierParticipantRoles, mf);
					}
					interactions.add(interaction);
				}
			}
		}
		return interactions;
	}

	public static List<Interaction> createReactionInteraction(Component parent, List<Component> sources, List<URI> sourceParticipantRoles, List<Component> targets, List<URI> targetParticipantRoles, List<URI> interactionTypes) throws SBOLGraphException {
		return createReactionInteraction(parent, sources, sourceParticipantRoles, targets, targetParticipantRoles, null, null, interactionTypes);
	}

	public static List<Interaction> createReactionInteractionFromFeatures(Component parent, List<List<Feature>> sources, List<URI> sourceParticipantRoles, List<List<Feature>> targets, List<URI> targetParticipantRoles, List<URI> interactionTypes) throws SBOLGraphException {
		return createReactionInteractionFromFeatures(parent, sources, sourceParticipantRoles, targets, targetParticipantRoles, null, null, interactionTypes);
	}


	public static List<Interaction> createInhibitionInteraction(Component parent, Component source, Component target) throws SBOLGraphException {		
			return createInteraction(Arrays.asList(InteractionType.Inhibition.getUri()), parent,
				source, Arrays.asList(ParticipationRole.Inhibitor.getUri()),
				target, Arrays.asList(ParticipationRole.Inhibited.getUri()));
	}	

	public static List<Interaction> createInhibitionInteraction(Component parent, List<Feature> source, List<Feature> target) throws SBOLGraphException {		
			return createInteraction(Arrays.asList(InteractionType.Inhibition.getUri()), parent,
				source, Arrays.asList(ParticipationRole.Inhibitor.getUri()),
				target, Arrays.asList(ParticipationRole.Inhibited.getUri()));
	}

	public static List<Interaction> createStimulationInteraction(Component parent, Component source, Component target) throws SBOLGraphException {		
			return createInteraction(Arrays.asList(InteractionType.Stimulation.getUri()), parent,
				source, Arrays.asList(ParticipationRole.Stimulator.getUri()),
				target, Arrays.asList(ParticipationRole.Stimulated.getUri()));
	}

	public static List<Interaction> createStimulationInteraction(Component parent, List<Feature> source, List<Feature> target) throws SBOLGraphException {		
			return createInteraction(Arrays.asList(InteractionType.Stimulation.getUri()), parent,
				source, Arrays.asList(ParticipationRole.Stimulator.getUri()),
				target, Arrays.asList(ParticipationRole.Stimulated.getUri()));
	}

	public static List<Interaction> createControlInteraction(Component parent, Component source, Component target) throws SBOLGraphException {		
			return createInteraction(Arrays.asList(InteractionType.Control.getUri()), parent,
				source, Arrays.asList(ParticipationRole.Modifier.getUri()),
				target, Arrays.asList(ParticipationRole.Modified.getUri()));
	}

	public static List<Interaction> createControlInteraction(Component parent, List<Feature> source, List<Feature> target) throws SBOLGraphException {		
			return createInteraction(Arrays.asList(InteractionType.Control.getUri()), parent,
				source, Arrays.asList(ParticipationRole.Modifier.getUri()),
				target, Arrays.asList(ParticipationRole.Modified.getUri()));
	}

	public static List<Interaction> createGeneticProductionInteraction(Component parent, Component source, Component target) throws SBOLGraphException {		
			return createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()), parent,
				source, Arrays.asList(ParticipationRole.Template.getUri()),
				target, Arrays.asList(ParticipationRole.Product.getUri()));
	}

	public static List<Interaction> createGeneticProductionInteraction(Component parent, List<Feature> sourceFeatures, List<Feature> targetFeatures) throws SBOLGraphException {		
			return createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()), parent,
				sourceFeatures, Arrays.asList(ParticipationRole.Template.getUri()),
				targetFeatures, Arrays.asList(ParticipationRole.Product.getUri()));
	}

	public static List<Interaction> createDegradationInteraction(Component parent, Component source) throws SBOLGraphException {		
		List<SubComponent> features = getOrCreateSubComponents(parent, source);
		return createDegradationInteractionFromFeature(parent, (List<Feature>)(List<?>)features);						
	}

	public static List<Interaction> createDegradationInteractionFromFeature(Component parent, List<Feature> features) throws SBOLGraphException {		
		List<Interaction> interactions = new ArrayList<Interaction>();		
		if (features != null){
			for (Feature feature : features) {
				Interaction interaction = parent.createInteraction(Arrays.asList(InteractionType.Degradation.getUri()));
				createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant.getUri()), feature);
				interactions.add(interaction);
			}
		}						
		return interactions;
	}

	public static List<Interaction> createBiochemicalReactionInteraction(Component parent, List<Component> sources, List<Component> targets) throws SBOLGraphException {		
			return createReactionInteraction(parent, sources, Arrays.asList(ParticipationRole.Reactant.getUri()), targets, Arrays.asList(ParticipationRole.Product.getUri()), Arrays.asList(InteractionType.BiochemicalReaction.getUri()));
	}

	public static List<Interaction> createBiochemicalReactionInteractionFromFeatures(Component parent, List<List<Feature>> sources, List<List<Feature>> targets) throws SBOLGraphException {		
			return createReactionInteractionFromFeatures(parent, sources, Arrays.asList(ParticipationRole.Reactant.getUri()), targets, Arrays.asList(ParticipationRole.Product.getUri()), Arrays.asList(InteractionType.BiochemicalReaction.getUri()));
	}

	public static List<Interaction> createNonCovalentBindingInteraction(Component parent, List<Component> sources, List<Component> targets) throws SBOLGraphException {		
			return createReactionInteraction(parent, sources, Arrays.asList(ParticipationRole.Reactant.getUri()), targets, Arrays.asList(ParticipationRole.Product.getUri()), Arrays.asList(InteractionType.NonCovalentBinding.getUri()));
	}

	public static List<Interaction> createNonCovalentBindingInteractionFromFeatures(Component parent, List<List<Feature>> sources, List<List<Feature>> targets) throws SBOLGraphException {		
			return createReactionInteractionFromFeatures(parent, sources, Arrays.asList(ParticipationRole.Reactant.getUri()), targets, Arrays.asList(ParticipationRole.Product.getUri()), Arrays.asList(InteractionType.NonCovalentBinding.getUri()));
	}



	private static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
		List<List<T>> result = new ArrayList<>();
		result.add(new ArrayList<>());
		for (List<T> list : lists) {
			List<List<T>> next = new ArrayList<>();
			for (List<T> existing : result) {
				for (T item : list) {
					List<T> combo = new ArrayList<>(existing);
					combo.add(item);
					next.add(combo);
				}
			}
			result = next;
		}
		return result;
	}

	public static List<SubComponent> getOrCreateSubComponents(Component parent, Component child) throws SBOLGraphException {
		List<SubComponent> subComponents = getSubComponents(parent, child);
		// If not DNA and there is no subComponent yet, add a subcomponent for the child
		// if ((subComponents==null || subComponents.size()==0) &&
		// !child.getTypes().contains(ComponentType.DNA.getUrl()))
		if (subComponents == null || subComponents.size() == 0) {
			SubComponent subComponent = parent.createSubComponent(child);
			if (subComponents == null) {
				subComponents = new ArrayList<SubComponent>();
			}
			subComponents.add(subComponent);
		}
		return subComponents;
	}

	public static List<SubComponent> getSubComponents(Component parent, Component child) throws SBOLGraphException {
		List<SubComponent> found = null;
		List<SubComponent> features = parent.getSubComponents();
		if (features != null) {
			for (SubComponent feature : features) {
				if (feature.getInstanceOf().getUri().equals(child.getUri())) {
					if (found == null) {
						found = new ArrayList<SubComponent>();
					}
					found.add(feature);
				}
			}
		}
		return found;
	}

	public static Interaction createInteraction(List<URI> interactionTypes, Component container, Feature participant1,
			List<URI> participant1Roles, Feature participant2, List<URI> participant2Roles) throws SBOLGraphException {
		Interaction interaction = container.createInteraction(interactionTypes);
		createParticipation(interaction, participant1Roles, participant1);
		createParticipation(interaction, participant2Roles, participant2);
		return interaction;
	}

	

	public static Participation createParticipation(Interaction interaction, List<URI> roles, Feature feature)
			throws SBOLGraphException {
		Participation participation = interaction.createParticipation(roles, feature);
		return participation;
	}

	private static int getIndex(List items) {
		int index = 1;
		if (items != null) {
			index = items.size() + 1;
		}
		return index;
	}

	private static int getIndex(List items, Class instanceType) {
		int index = 1;
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				if (instanceType.isInstance(items.get(i))) {
					index++;
				}
			}
		}
		return index;
	}

	private static int getIndex(Set items) {
		int index = 1;
		if (items != null) {
			index = items.size() + 1;
		}
		return index;
	}

	private static String getSubString(String data, String searchString) {
		String found = null;
		int index = data.indexOf(searchString);
		if (index != -1 && data.length() > index + searchString.length()) {
			found = data.substring(index + searchString.length());
		}
		return found;
	}

	private static String getLocal(URI uri) {
		String local = null;
		String uriString = uri.toString();
		local = getSubString(uriString, "#");
		if (local == null) {
			local = getSubString(uriString, "/");
		}
		return local;
	}

	public static <T extends Identified> String createLocalName(URI entityType, List<T> items) {
		int suffix = getIndex(items);
		String name = null;
		boolean valid = false;
		while (!valid) {
			name = createLocalName(entityType, suffix);
			boolean uniqueName = true;
			if (items != null) {
				for (Identified identified : items) {
					if (identified.getUri().toString().endsWith(name)) {
						suffix++;
						uniqueName = false;
						break;
					}
				}
			}

			if (uniqueName) {
				valid = true;
				break;
			}
		}
		return name;
	}

	/*
	 * GM: 20230324
	 * public static String createLocalName(URI entityType, List items, Class
	 * entityClass)
	 * {
	 * int suffix=getIndex(items,entityClass);
	 * return createLocalName(entityType, suffix);
	 * }
	 */

	private static String createLocalName(URI entityType, int suffix) {
		return createLocalName(entityType, String.valueOf(suffix));
	}

	public static URI createLocalUri(Identified identified, URI entityType, List items) {
		String displayId = SBOLAPI.createLocalName(entityType, items);
		URI uri = SBOLAPI.append(identified.getUri(), displayId);
		return uri;
	}

	/*
	 * GM: 20230324
	 * public static URI createLocalUri2(Identified identified, URI entityType, List
	 * items, Class entityClass)
	 * {
	 * String displayId=SBOLAPI.createLocalName(entityType, items,entityClass);
	 * URI uri=SBOLAPI.append(identified.getUri(), displayId);
	 * return uri;
	 * }
	 */

	private static String createLocalName(URI entityType, String suffix) {
		String displayId = getLocal(entityType) + suffix;
		return displayId;
	}

	public static void appendComponent(SBOLDocument document, Component parent, Component child)
			throws SBOLGraphException {
		appendComponent(document, parent, child, Orientation.inline);
	}

	/*
	 * public static SubComponent appendComponentRemove(SBOLDocument document,
	 * Component parent, Component child, Orientation orientation) throws
	 * SBOLGraphException
	 * {
	 * SubComponent subComponent=parent.createSubComponent(child.getUri());
	 * subComponent.setOrientation(orientation);
	 * 
	 * if (child.getSequences()!=null && child.getSequences().size()>0)
	 * {
	 * List<URI> sequences= parent.getSequences();
	 * Sequence sequence=null;
	 * if (sequences!=null && sequences.size()>0)
	 * {
	 * sequence=(Sequence)document.getIdentified(sequences.get(0),Sequence.class);
	 * }
	 * else
	 * {
	 * sequence=createSequence(document, parent, Encoding.NucleicAcid, "");
	 * }
	 * 
	 * 
	 * if ( child.getSequences()!=null && child.getSequences().size()>0)
	 * {
	 * URI childSequenceUri=child.getSequences().get(0);
	 * Sequence childSequence=(Sequence)document.getIdentified(childSequenceUri,
	 * Sequence.class);
	 * if (orientation==Orientation.inline)
	 * {
	 * sequence.setElements(sequence.getElements() + childSequence.getElements());
	 * }
	 * else
	 * {
	 * throw new
	 * SBOLGraphException("Reverse complement sequence addition has not been implemented yet!"
	 * );
	 * }
	 * int start=sequence.getElements().length() + 1;
	 * int end=start + childSequence.getElements().length()-1;
	 * LocationBuilder builder=new Location.RangeLocationBuilder(start,
	 * end,sequence.getUri());
	 * Location location=subComponent.createLocation(builder);
	 * location.setOrientation(orientation);
	 * }
	 * }
	 * return subComponent;
	 * }
	 */

	public static SubComponent appendComponent(SBOLDocument document, Component parent, Component child,
			Orientation orientation) throws SBOLGraphException {
		SubComponent subComponent = parent.createSubComponent(child);
		subComponent.setOrientation(orientation);
		if (child.getSequences() != null && child.getSequences().size() > 0) {
			/*
			 * URI childSequenceUri=child.getSequences().get(0);
			 * Sequence childSequence=(Sequence)document.getIdentified(childSequenceUri,
			 * Sequence.class);
			 */
			//TODO:GMGM - handle multiple sequences and sequence annotations. Get the sequence based on the correct encoding type.
			Sequence childSequence = child.getSequences().get(0);
			createRange(document, parent, subComponent, childSequence.getElements(), orientation);
		}
		return subComponent;
	}

	public static SequenceFeature appendSequenceFeature(SBOLDocument document, Component parent, String elements,
			Orientation orientation) throws SBOLGraphException {
		SequenceFeature feature = null;
		if (elements != null && elements.length() > 0) {
			List<Sequence> sequences = parent.getSequences();
			Sequence sequence = null;
			int start, end;
			if (sequences != null && sequences.size() > 0) {
				sequence = parent.getSequences().get(0);
				start = sequence.getElements().length() + 1;
				end = start + elements.length() - 1;
			} else {
				sequence = createSequence(document, parent, Encoding.NucleicAcid, "");
				start = 1;
				end = elements.length() - 1;
			}

			if (orientation == Orientation.inline) {
				sequence.setElements(sequence.getElements() + elements);
			} else {
				//TODO:GMGM
				throw new SBOLGraphException("Reverse complement sequence addition has not been implemented yet!");
			}
			feature = parent.createSequenceFeature(start, end, sequence);
			if (feature != null) {
				feature.setOrientation(orientation);
			}
			((Range) feature.getLocations().get(0)).setOrientation(orientation);

		}
		return feature;
	}

	private static Range createRange(SBOLDocument document, Component parent, SubComponent subComponent,
			String elements, Orientation orientation) throws SBOLGraphException {
		Range range = null;
		if (elements != null && elements.length() > 0) {
			List<Sequence> sequences = parent.getSequences();
			Sequence sequence = null;
			int start, end;
			if (sequences != null && sequences.size() > 0) {
				sequence = parent.getSequences().get(0);
				start = sequence.getElements().length() + 1;
				end = start + elements.length() - 1;
			} else {
				sequence = createSequence(document, parent, Encoding.NucleicAcid, "");
				start = 1;
				end = elements.length();
			}
			//If reverse complement, get the reverse complement of the elements and add to the sequence
			if (orientation == Orientation.reverseComplement) {
				elements = Sequence.getReverseComplement(elements);
			}
			sequence.setElements(sequence.getElements() + elements);
			range = subComponent.createRange(start, end, sequence);
			range.setOrientation(orientation);
		}
		return range;

	}

	/*
	 * private static LocationBuilder createLocationBuilder(SBOLDocument document,
	 * Component parent, String elements, Orientation orientation) throws
	 * SBOLGraphException
	 * {
	 * LocationBuilder locationBuilder=null;
	 * if (elements!=null && elements.length()>0)
	 * {
	 * List<Sequence> sequences= parent.getSequences();
	 * Sequence sequence=null;
	 * int start, end;
	 * if (sequences!=null && sequences.size()>0)
	 * {
	 * sequence=parent.getSequences().get(0);
	 * start=sequence.getElements().length() + 1;
	 * end=start + elements.length()-1;
	 * }
	 * else
	 * {
	 * sequence=createSequence(document, parent, Encoding.NucleicAcid, "");
	 * start=1;
	 * end=elements.length()-1;
	 * }
	 * 
	 * if (orientation==Orientation.inline)
	 * {
	 * sequence.setElements(sequence.getElements() + elements);
	 * }
	 * else
	 * {
	 * throw new
	 * SBOLGraphException("Reverse complement sequence addition has not been implemented yet!"
	 * );
	 * }
	 * 
	 * 
	 * locationBuilder=new Location.RangeLocationBuilder(start, end,sequence);
	 * locationBuilder.setOrientation(orientation);
	 * }
	 * return locationBuilder;
	 * }
	 */

	public static Component createDnaComponent(SBOLDocument doc, String displayId, String name, String description,
			URI role, String sequence) throws SBOLGraphException {
		Component dna = createComponent(doc, displayId, ComponentType.DNA.getUri(), name, description, role);
		if (sequence != null && sequence.length() > 0) {
			createSequence(doc, dna, Encoding.NucleicAcid, sequence);
		}
		return dna;
	}

	public static Component createProteinComponent(SBOLDocument doc, Component container, String displayId, String name,
			String description, URI role, String sequence) throws SBOLGraphException {
		Component protein = createComponent(doc, displayId, ComponentType.Protein.getUri(), name, description, role);
		if (container != null) {
			container.createSubComponent(protein);
		}
		if (sequence != null && sequence.length() > 0) {
			createSequence(doc, protein, Encoding.AminoAcid, sequence);
		}
		return protein;
	}

	public static Component createProteinComponent(SBOLDocument doc, String displayId, String name, String description,
			URI role, String sequence) throws SBOLGraphException {
		return createProteinComponent(doc, null, displayId, name, description, role, sequence);
	}

	public static SubComponent addSubComponent(Component parent, Component child) throws SBOLGraphException {
		SubComponent subComponent = parent.createSubComponent(child);
		return subComponent;
	}

	public static Sequence createSequence(SBOLDocument doc, Component component, Encoding encoding, String elements)
			throws SBOLGraphException {
		String localName = createLocalName(DataModel.Sequence.uri, component.getSequences());

		Sequence seq = createSequence(doc, URI.create(component.getUri().toString() + "_" + localName), localName,
				component.getDisplayId() + " sequence", elements, encoding);
		component.setSequences(Arrays.asList(seq));
		return seq;
	}

	public static Sequence addSequence(SBOLDocument doc, Component component, Encoding encoding, String elements)
			throws SBOLGraphException {
		String localName = createLocalName(DataModel.Sequence.uri, component.getSequences());
		Sequence seq = createSequence(doc, URI.create(component.getUri().toString() + "_" + localName), localName,
				component.getDisplayId() + " sequence", elements, encoding);
		List<Sequence> sequences = component.getSequences();
		if (sequences == null) {
			component.setSequences(Arrays.asList(seq));
		} else {
			sequences.add(seq);
			component.setSequences(sequences);
		}
		return seq;
	}

	public static Encoding getEncodingType(SBOLDocument doc, Component component, String elements)
			throws SBOLGraphException {
		Encoding encoding = null;
		List<URI> componentTypes = component.getTypes();
		if (componentTypes == null || componentTypes.size() == 0) {
			throw new SBOLGraphException("Component must have at least one type to determine encoding.");
		}
		for (URI componentTypeURI : componentTypes) {
			boolean foundTypeMatch = false;
			ComponentType componentType = ComponentType.get(componentTypeURI);
			if (componentType != null) {
				List<Encoding> typeMatches = ComponentType.checkComponentTypeMatch(componentType);
				ComponentType.checkComponentTypeMatch(componentType);
				if (typeMatches != null && typeMatches.size() > 0) {
					foundTypeMatch = true;
					for (Encoding typeMatch : typeMatches) {
						if (typeMatch.isValidSequence(elements)) {
							encoding = typeMatch;
							break;
						}
					}
				}
			}
			if (foundTypeMatch) {
				break;
			}
		}
		if (encoding == null) {
			throw new SBOLGraphException("No encoding found for the provided sequence and component types.");
		}
		return encoding;
	}

	public static Sequence addSequence(SBOLDocument doc, Component component, String elements)
			throws SBOLGraphException {
		Encoding encoding = getEncodingType(doc, component, elements);
		Sequence seq = addSequence(doc, component,encoding, elements);
		return seq;
	}

	public static Component createComponent(SBOLDocument doc, URI uri, URI type, String name, String description,
			URI role) throws SBOLGraphException {
		URI namespace = null;
		if (doc.getBaseURI() != null) {
			namespace = SBOLUtil.toNameSpace(doc.getBaseURI());
		}
		Component component = doc.createComponent(uri, namespace, Arrays.asList(type));
		component.setName(name);
		component.setDescription(description);
		if (role != null) {
			component.setRoles(Arrays.asList(role));
		}

		return component;
	}

	public static Component createComponent(SBOLDocument doc, String displayId, URI type, String name,
			String description, URI role) throws SBOLGraphException {
		Component component = doc.createComponent(displayId, Arrays.asList(type));
		component.setName(name);
		component.setDescription(description);
		if (role != null) {
			component.setRoles(Arrays.asList(role));
		}

		return component;
	}

	public static URI append(URI uri, String id) {
		return append(uri.toString(), id);
	}

	public static URI append(String text, String add) {
		if (text.endsWith("/") || text.endsWith("#")) {
			return URI.create(String.format("%s%s", text, add));
		} else {
			return URI.create(String.format("%s/%s", text, add));
		}
	}

	public static Sequence createSequence(SBOLDocument doc, URI uri, String name, String description, String sequence,
			Encoding encoding) throws SBOLGraphException {
		Sequence sequenceEntity = doc.createSequence(uri, SBOLUtil.toNameSpace(doc.getBaseURI()));
		sequenceEntity.setName(name);
		sequenceEntity.setDescription(description);
		if (sequence != null) {
			sequenceEntity.setElements(sequence);
		}
		sequenceEntity.setEncoding(encoding);
		return sequenceEntity;

	}

	/*
	 * public static void mapTo(SubComponent subComponentInContainer,Component
	 * container, Component parent, Component child) throws SBOLGraphException
	 * {
	 * List<ComponentReference> childReferences=createComponentReference(container,
	 * parent, child);
	 * if (childReferences!=null)
	 * {
	 * for (ComponentReference compRef: childReferences)
	 * {
	 * String localName=SBOLAPI.createLocalName(DataModel.Constraint.uri,
	 * container.getConstraints());
	 * container.createConstraint(SBOLAPI.append(container.getUri(), localName),
	 * RestrictionType.Identity.verifyIdentical, subComponentInContainer.getUri(),
	 * compRef.getUri());
	 * }
	 * }
	 * }
	 */

	public static List<ComponentReference> mapTo(Component container, Component parent1, Component child1, Component parent2,
			Component child2) throws SBOLGraphException {
		List<ComponentReference> childReferences1 = createComponentReference(container, parent1, child1);
		List<ComponentReference> childReferences2 = createComponentReference(container, parent2, child2);
		if (childReferences1 != null && childReferences2 != null) {
			URI restriction = RestrictionType.IdentityRestriction.verifyIdentical.getUri();
			if (!child1.getUri().equals(child2.getUri())) {
				restriction = RestrictionType.IdentityRestriction.replaces.getUri();
			}
			for (ComponentReference compRef1 : childReferences1) {
				for (ComponentReference compRef2 : childReferences2) {

					container.createConstraint(restriction, compRef1, compRef2);
				}
			}
			childReferences1.addAll(childReferences2);
			return childReferences1;
		}

		return null;
	}

	public static List<ComponentReference> mapTo(Component container, Component toContainer, Component toEntity) throws SBOLGraphException {
		List<ComponentReference> childReferences = createComponentReference(container, toContainer, toEntity);
		return childReferences;
	}
	
	/*
	 * private static <T extends Feature> void createConstraint(Component container,
	 * List<T> subjects, List<T> objects) throws SBOLGraphException
	 * {
	 * if (subjects!=null && objects!=null){
	 * for (Feature compRef1: subjects)
	 * {
	 * for (Feature compRef2: objects)
	 * {
	 * String localName=SBOLAPI.createLocalName(DataModel.Constraint.uri,
	 * container.getConstraints());
	 * container.createConstraint(SBOLAPI.append(container.getUri(), localName),
	 * RestrictionType.Identity.verifyIdentical, compRef1.getUri(),
	 * compRef2.getUri());
	 * }
	 * }
	 * }
	 * }
	 */

	public static List<ComponentReference> mapTo(Component container, Component parent1, Component child1, Component containerChild)
			throws SBOLGraphException {
		List<ComponentReference> childReferences1 = createComponentReference(container, parent1, child1);
		List<SubComponent> childReferences2 = getSubComponents(container, containerChild);
		// createConstraint(containerChild, childReferences1, childReferences2);
		if (childReferences1 != null && childReferences2 != null) {
			URI restriction = RestrictionType.IdentityRestriction.verifyIdentical.getUri();
			if (!child1.getUri().equals(containerChild.getUri())) {
				restriction = RestrictionType.IdentityRestriction.replaces.getUri();
			}
			for (ComponentReference compRef1 : childReferences1) {
				for (SubComponent compRef2 : childReferences2) {
					container.createConstraint(restriction, compRef1,compRef2);
				}
			}
		}		
		return childReferences1;
	}

	// TODO:Remove
	/*
	 * public static List<ComponentReference> createComponentReference2(Component
	 * container, Component parent, Component child) throws SBOLGraphException
	 * {
	 * List<ComponentReference> componentReferences=null;
	 * List<SubComponent> subComponents=getSubComponents(parent, child);
	 * if (subComponents!=null)
	 * {
	 * for (SubComponent subComponent:subComponents)
	 * {
	 * ComponentReference compRef=container.createComponentReference(child.getUri(),
	 * subComponent.getUri());
	 * if (componentReferences==null)
	 * {
	 * componentReferences=new ArrayList<ComponentReference>();
	 * }
	 * componentReferences.add(compRef);
	 * }
	 * }
	 * return componentReferences;
	 * }
	 */

	public static List<ComponentReference> createComponentReference(Component container, Component parent, Component child) throws SBOLGraphException {
		List<ComponentReference> componentReferences = null;
		List<SubComponent> subComponentsInContainer = getSubComponents(container, parent);
		List<SubComponent> subComponentsInParent = getSubComponents(parent, child);

		if (subComponentsInContainer != null && subComponentsInParent != null) {
			for (SubComponent subComponentInContainer : subComponentsInContainer) {
				for (SubComponent subComponentInParent : subComponentsInParent) {
					ComponentReference compRef = container.createComponentReference(subComponentInParent,
							subComponentInContainer);
					if (componentReferences == null) {
						componentReferences = new ArrayList<ComponentReference>();
					}
					componentReferences.add(compRef);
				}
			}
		}
		return componentReferences;
	}

	public static List<ComponentReference> createComponentReference(Component container, Component parent, ComponentReference child) throws SBOLGraphException {
		List<ComponentReference> componentReferences = null;
		List<SubComponent> subComponentsInContainer = getSubComponents(container, parent);
		
		if (subComponentsInContainer != null) {
			for (SubComponent subComponentInContainer : subComponentsInContainer) {
				ComponentReference compRef = container.createComponentReference(child, subComponentInContainer);
					if (componentReferences == null) {
						componentReferences = new ArrayList<ComponentReference>();
					}
					componentReferences.add(compRef);				
			}
		}
		return componentReferences;
	}

	public static List<Constraint> createConstraint(Component container, Component component1, Component component2,
			URI restriction) throws SBOLGraphException {
		List<Constraint> result = null;
		List<SubComponent> subComponents1 = getOrCreateSubComponents(container, component1);
		List<SubComponent> subComponents2 = getOrCreateSubComponents(container, component2);

		if (subComponents1 != null && subComponents2 != null) {
			for (SubComponent subComponent1 : subComponents1) {
				for (SubComponent subComponent2 : subComponents2) {
					Constraint constraint = container.createConstraint(restriction, subComponent1, subComponent2);
					if (result == null) {
						result = new ArrayList<Constraint>();
					}
					result.add(constraint);
				}
			}
		}
		return result;
	}

	/*
	 * private static List<URI> getSubComponent(Component parent, Component child)
	 * throws SBOLGraphException
	 * {
	 * List<URI> result=null;
	 * for (SubComponent subComponent:parent.getSubComponents())
	 * {
	 * if (subComponent.getIsInstanceOf().equals(child.getUri()))
	 * {
	 * if (result==null)
	 * {
	 * result=new ArrayList<URI>();
	 * }
	 * result.add(subComponent.getUri());
	 * }
	 * 
	 * }
	 * return result;
	 * }
	 */

	/*
	 * public static List<Interaction> createInteractionDel(List<URI>
	 * interactionTypes, Component parent, Component participant1, List<URI>
	 * participant1Roles, Component participant2, List<URI> participant2Roles)
	 * throws SBOLGraphException, SBOLException
	 * {
	 * List<Interaction> interactions=new ArrayList<Interaction>();
	 * List<SubComponent> features1=getSubComponents(parent, participant1);
	 * List<SubComponent> features2=getSubComponents(parent, participant2);
	 * 
	 * for (Feature feature1: features1)
	 * {
	 * for (Feature feature2: features2)
	 * {
	 * Interaction interaction=createInteractionDel(interactionTypes, parent,
	 * feature1, participant1Roles, feature2, participant2Roles);
	 * interactions.add(interaction);
	 * }
	 * }
	 * return interactions;
	 * }
	 * 
	 * public static Interaction createInteractionDel(List<URI> interactionTypes,
	 * Component container, Feature participant1, List<URI> participant1Roles,
	 * Feature participant2, List<URI> participant2Roles) throws SBOLGraphException,
	 * SBOLException
	 * {
	 * int index=getIndex(container.getInteractionsDel());
	 * Interaction interaction=
	 * container.createInteractionDel(append(container.getUri(), "interaction" +
	 * index), interactionTypes);
	 * createParticipation(interaction, participant1Roles, participant1);
	 * createParticipation(interaction, participant2Roles, participant2);
	 * return interaction;
	 * }
	 * 
	 * public static Set<Interaction> createInteractionDel2(List<URI>
	 * interactionTypes, Component parent, Component participant1, List<URI>
	 * participant1Roles, Component participant2, List<URI> participant2Roles)
	 * throws SBOLGraphException, SBOLException
	 * {
	 * Set<Interaction> interactions=new HashSet<Interaction>();
	 * List<SubComponent> features1=getSubComponents(parent, participant1);
	 * List<SubComponent> features2=getSubComponents(parent, participant2);
	 * 
	 * for (Feature feature1: features1)
	 * {
	 * for (Feature feature2: features2)
	 * {
	 * Interaction interaction=createInteractionDel2(interactionTypes, parent,
	 * feature1, participant1Roles, feature2, participant2Roles);
	 * interactions.add(interaction);
	 * }
	 * }
	 * return interactions;
	 * }
	 * 
	 * public static Interaction createInteractionDel2(List<URI> interactionTypes,
	 * Component container, Feature participant1, List<URI> participant1Roles,
	 * Feature participant2, List<URI> participant2Roles) throws SBOLGraphException,
	 * SBOLException
	 * {
	 * int index=getIndex(container.getInteractionsDel2());
	 * Interaction interaction=
	 * container.createInteractionDel2(append(container.getUri(), "interaction" +
	 * index), interactionTypes);
	 * createParticipation(interaction, participant1Roles, participant1);
	 * createParticipation(interaction, participant2Roles, participant2);
	 * return interaction;
	 * }
	 */

	public static Set<Component> getRootComponents(SBOLDocument doc, ComponentType type) throws SBOLGraphException {
		Set<URI> childNodes = null;
		Set<Component> rootNodes = new HashSet<Component>();
		if (doc != null && doc.getComponents() != null) {

			for (Component compDef : doc.getComponents()) {
				if (compDef.getSubComponents() != null && compDef.getTypes().contains(type.getUri())) {
					for (SubComponent comp : compDef.getSubComponents()) {
						if (comp.getInstanceOf() != null) {
							if (childNodes == null) {
								childNodes = new HashSet<URI>();
							}
							childNodes.add(comp.getInstanceOf().getUri());
						}
					}
				}
			}
			for (Component compDef : doc.getComponents()) {
				if (!childNodes.contains(compDef.getUri()) && compDef.getTypes().contains(type.getUri())) {
					if (rootNodes == null) {
						rootNodes = new HashSet<Component>();
					}
					rootNodes.add(compDef);
				}
			}
		}
		return rootNodes;
	}

	public static String inferDisplayId(URI uri) throws SBOLGraphException {
		String result = null;
		String uriString = uri.toString();

		if (SBOLUtil.isURL(uriString))// .contains("://"))
		{
			// String path=uriString;// uri.getPath();
			String path = uri.getPath();

			int index = path.lastIndexOf("/");
			if (path.length() > index + 1) {
				result = path.substring(index + 1);
			} else {
				result = null;
			}
			if (result != null && uriString.endsWith(result)) {
				return result;
			} else {
				throw new SBOLGraphException("An SBOL URI MUST include the display id fragment. URI:" + uri);
			}
		}
		return null;
	}

	public static void printConnectivity(SBOLDocument doc) throws SBOLGraphException {
		printConnectivity(doc, System.out);
	}

	public static void printConnectivity(SBOLDocument doc, String filePath) throws SBOLGraphException, IOException {
		try (PrintStream out = new PrintStream(new FileOutputStream(filePath))) {
			printConnectivity(doc, out);
		}
	}

	public static void printConnectivity(SBOLDocument doc, PrintStream out) throws SBOLGraphException {
		List<TopLevel> topLevels = doc.getTopLevels();
		if (topLevels != null) {
			for (TopLevel topLevel : topLevels) {
				printConnectivity(topLevel, 0, doc, out);
			}
		}
	}

	private static void printConnectivity(Identified entity, int depth, SBOLDocument doc, PrintStream out) throws SBOLGraphException {
		String indent = "   ".repeat(depth);
		out.println(indent + "Entity: " + entity.getUri());

		Map<URI, List<Object>> connected = entity.getConnectedEntities();
		if (connected != null && !connected.isEmpty()) {
			for (Map.Entry<URI, List<Object>> entry : connected.entrySet()) {
				out.print(indent + "   -- " + getSBOLName(entry.getKey(), doc) + "\t-->");
				if (entry.getValue() != null) {
					if (entry.getValue().size() == 1) {
						Object value = entry.getValue().get(0);
						if (value instanceof Identified) {
							out.println(" " + ((Identified) value).getUri());
						} else {
							out.println(" " + value);
						}
					} else {
						out.println();
						for (Object value : entry.getValue()) {
							if (value instanceof Identified) {
								out.println(indent + "        " + ((Identified) value).getUri());
							} else {
								out.println(indent + "        " + value);
							}
						}
					}
				}
			}
		}

		List<Identified> children = entity.getChildren();
		if (children != null) {
			for (Identified child : children) {
				printConnectivity(child, depth + 1, doc, out);
			}
		}
	}

	private static String getSBOLName(URI uri, SBOLDocument doc) {
		String uriStr = uri.toString();
		for (Map.Entry<String, String> e : doc.getRDFModel().getNsPrefixMap().entrySet()) {
			if (uriStr.startsWith(e.getValue())) {
				if ((e.getValue().equalsIgnoreCase(URINameSpace.SBOL.getUri().toString())
						|| e.getValue().equalsIgnoreCase(URINameSpace.PROV.getUri().toString()))) {
					return uriStr.substring(e.getValue().length());
				} else {
					return e.getKey() + ":" + uriStr.substring(e.getValue().length());
				}
			}

		}
		return uriStr;
	}

	/*public static createStimulationInteraction(Component container, Feature source, List<URI> sourceRoles, Feature target,
			List<URI> targetRoles) throws SBOLGraphException {
		return createInteraction(Arrays.asList(URI.create("http://identifiers.org/biomodels.vocabulary/Stimulation")), container,
				source, sourceRoles, target, targetRoles);
	}*/

}
