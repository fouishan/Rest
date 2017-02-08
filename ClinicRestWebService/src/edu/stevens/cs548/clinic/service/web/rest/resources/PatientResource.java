package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;


import edu.stevens.cs548.clinic.service.dto.util.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceRemote;
import edu.stevens.cs548.clinic.service.representations.PatientRepresentation;
import edu.stevens.cs548.clinic.service.representations.Representation;
import edu.stevens.cs548.clinic.service.representations.TreatmentRepresentation;

@Path("/patient")
@RequestScoped
public class PatientResource {
	
	final static Logger logger = Logger.getLogger(PatientResource.class.getCanonicalName());
	
	@SuppressWarnings("unused")
	@Context
    private UriInfo uriInfo;
    
    private PatientDtoFactory patientDtoFactory;

    /**
     * Default constructor. 
     */
    public PatientResource() {
    	patientDtoFactory = new PatientDtoFactory();
    }
    
	@EJB(beanName="PatientServiceBean")
    private IPatientServiceRemote patientService;
	
	public class RecordNotFound extends WebApplicationException {
		private static final long serialVersionUID = 1L;
		public RecordNotFound(String message) {
	         super(Response.status(Response.Status.NOT_FOUND)
	             .entity(message).type(Representation.MEDIA_TYPE).build());
	     }
	}
	public class RecordNotCreated extends WebApplicationException{
		private static final long serialVersionUID = 1L;
		public RecordNotCreated(String message) {
	         super(Response.status(Response.Status.BAD_REQUEST)
	             .entity(message).type(Representation.MEDIA_TYPE).build());
		}
	}
    
    @GET
    @Path("site")
    @Produces("text/plain")
    public String getSiteInfo() {
    	return patientService.siteInfo();
    }

	@POST
	@Consumes("application/xml")
    public Response addPatient(PatientRepresentation patientRep) {
    	try {
    		PatientDto dto = patientDtoFactory.createPatientDto();
    		dto.setPatientId(patientRep.getPatientId());
    		dto.setName(patientRep.getName());
    		dto.setDob(patientRep.getDob());
    		dto.setAge(patientRep.getAge());
    		long id = patientService.addPatient(dto);
    		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("{id}");
    		URI url = ub.build(Long.toString(id));
    		return Response.created(url).build();
    	} catch (PatientServiceExn e) {
    		throw new RecordNotCreated("Unable to add Patient");
    	}
    }
    
	/**
	 * Query methods for patient resources.
	 */
    @GET
    @Path("{id}")
	@Produces("application/xml")
	public PatientRepresentation getPatient(@PathParam("id") String id) {
		try {
			long key = Long.parseLong(id);
			PatientDto patientDTO = patientService.getPatient(key);
			PatientRepresentation patientRep = new PatientRepresentation(patientDTO, uriInfo);
			return patientRep;
		} catch (PatientServiceExn e) {
			throw new RecordNotFound("No Patient Found");
		}
	}
    
	@GET
	@Path("byPatientId")
	@Produces("application/xml")
	public PatientRepresentation getPatientByPatientId(@QueryParam("id") String patientId) {
		try {
			long pid = Long.parseLong(patientId);
			PatientDto patientDTO = patientService.getPatientByPatId(pid);
			PatientRepresentation patientRep = new PatientRepresentation(patientDTO, uriInfo);
			return patientRep;
		} catch (PatientServiceExn e) {
			throw new RecordNotFound("No Patient Found");
		}
	}
	
	@GET
	@Produces("application/xml")
	public PatientRepresentation[] getPatientByNameDOB(@QueryParam("name") String name
			,@QueryParam("dob") String dob) {
		try {
			Date birthDate = DatatypeConverter.parseDate(dob).getTime();
			PatientDto[] patientDTO = patientService.getPatientByNameDob(name, birthDate);
			PatientRepresentation[] patientReps = new PatientRepresentation[patientDTO.length];
			for (int i = 0; i< patientDTO.length; i++){
				patientReps[i] = new PatientRepresentation(patientDTO[i], uriInfo);
			}
			return patientReps;
		} catch (PatientServiceExn e) {
			throw new RecordNotFound("No Patient Found");
		}
	}
    
	@GET
	@Path("{id}/treatments/{tid}")
	@Produces("application/xml")
    public TreatmentRepresentation getPatientTreatment(@PathParam("id") String id, @PathParam("tid") String tid) {
    	try {
    		TreatmentDto treatment = patientService.getTreatment(Long.parseLong(id), Long.parseLong(tid)); 
    		TreatmentRepresentation treatmentRep = new TreatmentRepresentation(treatment, uriInfo);
    		return treatmentRep;
    	} catch (PatientServiceExn e) {
    		throw new RecordNotFound("No Patient Found");
    	}
    }
}