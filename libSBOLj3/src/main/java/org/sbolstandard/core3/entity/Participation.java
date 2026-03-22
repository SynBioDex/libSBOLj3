package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyName;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import jakarta.validation.constraints.NotEmpty;

public class Participation extends Identified{
	/*private List<URI> roles=null;
	private URI participant=null;
	private URI higherOrderParticipant=null;*/
	
	protected  Participation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Participation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		if ((this.getParticipant()==null && this.getHigherOrderParticipant()==null) || (this.getParticipant()!=null && this.getHigherOrderParticipant()!=null))
		{
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{PARTICIPANT_MUST_HAVE_ONE_PARTICIPANT_OR_HIGHERORDERPARTICIPANT}", DataModel.Participation.participant));      	
		}
		validationMessages = assertValidSBORole(validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Participation.participant, this.resource, getParticipant(), validationMessages);
		return validationMessages;
	}
	
	@NotEmpty(message = "{PARTICIPANT_ROLES_NOT_EMPTY}") 
	@PropertyName("roleAA")
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
	}
	
	public void setRoles(@PropertyName("roleZZ")  @NotEmpty(message = "{PARTICIPANT_ROLES_NOT_EMPTY}") List<URI> roles) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setRoles", new Object[] {roles}, List.class);
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	public Feature getParticipant() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.participant);	
		return contsructIdentified(DataModel.Participation.participant, Feature.getSubClassTypes());
	}

	public void setParticipant(Feature participant) {
		RDFUtil.setProperty(resource, DataModel.Participation.participant, SBOLUtil.toURI(participant));
	}
	
	public URI getHigherOrderParticipant() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.higherOrderParticipant);	
	}

	public Interaction getHigherOrderParticipantInteraction() throws SBOLGraphException {
		return contsructIdentified(DataModel.Participation.higherOrderParticipant, Interaction.class, DataModel.Interaction.uri);
		//return constructIdentified(DataModel.Participation.higherOrderParticipant, DataModel.Interaction.uri, Interaction.class);
	}


	public void setHigherOrderParticipant(URI higherOrderParticipantURI) {
		RDFUtil.setProperty(resource, DataModel.Participation.higherOrderParticipant, higherOrderParticipantURI);
	}

	public void setHigherOrderParticipant(Interaction higherOrderParticipant) {		
		setHigherOrderParticipant(SBOLUtil.toURI(higherOrderParticipant));
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Participation.uri;
	}
	
	//PARTICIPANT_ROLE_SBO_VALID
	public List<ValidationMessage> assertValidSBORole(List<ValidationMessage> validationMessages) throws SBOLGraphException {		
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			List<URI> roles=this.getRoles();
			if (roles!=null){
				Set<URI> sboRoles=null;
				for (URI role:roles){
					if (Configuration.getInstance().getSboParticipantRoles().contains(role.toString())) {
						sboRoles=SBOLUtil.addToSet(sboRoles, role);
					}
				}
				String message=null;
				//count should be zero
				if (sboRoles==null || sboRoles.size()==0){
					message="{PARTICIPANT_ROLE_SBO_VALID}";
				}
				else if (sboRoles.size()>1){
						message=String.format("{PARTICIPANT_ROLE_SBO_VALID}%s Multiple valid SBO roles: %s", ValidationMessage.INFORMATION_SEPARATOR, sboRoles);
				}
				
				if (message!=null) {
					ValidationMessage valMessage = new ValidationMessage(message, DataModel.role, roles);
					validationMessages=IdentifiedValidator.addToValidations(validationMessages, valMessage);
				}
				
			}
		}
		return validationMessages;
	}

	@Override
	public Map<URI, List<? extends Identified>> getReferencedChildEntitiesWithEdgeURIs() throws SBOLGraphException {
		Map<URI, List<? extends Identified>> identifieds = super.getReferencedChildEntitiesWithEdgeURIs();
		identifieds = addToMap(identifieds, DataModel.Participation.participant, Arrays.asList(getParticipant()));
		identifieds = addToMap(identifieds, DataModel.Participation.higherOrderParticipant, Arrays.asList(getHigherOrderParticipantInteraction()));
		return identifieds;
	}
	
}