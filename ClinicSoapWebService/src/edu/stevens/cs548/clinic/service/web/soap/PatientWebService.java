package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;

import javax.ejb.EJB;
import javax.jws.WebService;

//import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.util.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceRemote;

// Use JSR-181 annotations to specify Web service.
//Specify: endpoint interface (FQN), target namespace, service name, port name

@WebService(
		endpointInterface = "edu.stevens.cs548.clinic.service.web.soap.IPatientWebService", 
		targetNamespace = "http:/cs548.stevens.edu/clinic/service/web/soap/patient", 
		serviceName = "PatientWebService", portName = "PatientWebPort")

public class PatientWebService implements IPatientWebService {

	// Use CDI to inject the service
	IPatientServiceLocal service;
	
	/*@Override
	public long addPatient(PatientDto dto) throws PatientServiceExn {
		return service.addPatient(dto);
	}

	@Override
	public PatientDto getPatient(long id) throws PatientServiceExn {
		return service.getPatient(id);
	}

	@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		return service.getPatientByPatId(pid);
	}

	@Override
	public TreatmentDto getTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn,
			PatientServiceExn {
		return service.getTreatment(id, tid);
	}*/
	
	@EJB(beanName="PatientServiceBean")
	IPatientServiceRemote patientService;

	@Override
	public String siteInfo() {
		return patientService.siteInfo();
	}

	@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		return patientService.getPatientByPatId(pid);
	}

	@Override
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn {
		return patientService.getPatientByDbId(id);
	}

	@Override
	public PatientDto[] getPatientByNameDob(String name, Date dob) throws PatientServiceExn {
		return patientService.getPatientByNameDob(name, dob);
	}

	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		return patientService.getTreatment(id, tid);
	}

	@Override
	public long createPatient(String name, Date dob, long patientId) throws PatientServiceExn {
		
			return patientService.createPatient(name, dob, patientId);
	}

	@Override
	public void deletePatient(String name, long id) throws PatientServiceExn {
		 patientService.deletePatient(name, id);
		
	}

	@Override
	public void addDrugTreatment(long id, String diagnosis, String drug, float dosage) throws PatientNotFoundExn {
		 patientService.addDrugTreatment(id, diagnosis, drug, dosage);
		
	}

	@Override
	public TreatmentDto[] getTreatments(long id, long[] tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		return patientService.getTreatments(id, tid);
	}

	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		 patientService.deleteTreatment(id, tid);
	}

}
