package edu.stevens.cs548.clinic.service.representations;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.util.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.ProviderType;

public class ProviderRepresentation extends ProviderType {
	
	public List<LinkType> getLinksTreatments() {
		return this.getTreatments();
	}
	
	public static LinkType getProviderLink(long id, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("provider").path("{id}");
		String providerURI = ub.build(Long.toString(id)).toString();

		LinkType link = new LinkType();
		link.setUrl(providerURI);
		link.setRelation(Representation.RELATION_PROVIDER);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	private ProviderDtoFactory providerDtoFactory;
	
	public ProviderRepresentation () {
		super();
		this.providerDtoFactory = new ProviderDtoFactory();
	}
	
	public ProviderRepresentation (ProviderDto dto, UriInfo uriInfo) {
		this();
		this.id = getProviderLink(dto.getId(), uriInfo);
		this.providerId =  dto.getProviderId();
		this.name = dto.getName();
		this.specialization = dto.getSpecialization();
		/*
		 * Call getTreatments to initialize empty list.
		 */
		List<LinkType> links = this.getTreatments();
		for (long t : dto.getTreatments()) {
			links.add(TreatmentRepresentation.getTreatmentLink(t, uriInfo));
		}
		this.getTreatments().addAll(links);
	}
	
	public ProviderDto getProviderDto() {
		ProviderDto p = providerDtoFactory.createProviderDto();
		p.setId(Representation.getId(this.id));
		p.setProviderId(this.providerId);
		p.setName(this.name);
		p.setSpecialization(this.specialization);
		return p;
	}
}