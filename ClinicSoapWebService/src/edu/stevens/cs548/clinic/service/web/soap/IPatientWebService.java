package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import edu.stevens.cs548.clinic.service.dto.util.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.TreatmentNotFoundExn;

@WebService(
	name="IPatientWebPort",
	targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/patient")
@SOAPBinding(
		style=SOAPBinding.Style.DOCUMENT,
		use=SOAPBinding.Use.LITERAL,
		parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)
/*
 * Endpoint interface for the patient Web service.
 */
public interface IPatientWebService {
	
	/*@WebMethod
	public long addPatient (
			@WebParam(name="patient-dto",
			          targetNamespace="http://cs548.stevens.edu/clinic/dto")
			PatientDto dto) throws PatientServiceExn;

	@WebMethod
	@WebResult(name="patient-dto",
			   targetNamespace="http://cs548.stevens.edu/clinic/dto")
	public PatientDto getPatient(long id) throws PatientServiceExn;
	
	@WebMethod
	@WebResult(name="patient-dto",
	   		   targetNamespace="http://cs548.stevens.edu/clinic/dto")
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn;
	
	@WebMethod(operationName="patientGetTreatment")
	@WebResult(name="treatment-dto",
	           targetNamespace="http://cs548.stevens.edu/clinic/dto")
	public TreatmentDto getTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	@WebMethod
	public String siteInfo();*/

//	public long addPatient(PatientDto dto) throws PatientServiceExn;

//	public PatientDto getPatient(long id) throws PatientServiceExn;

	@WebMethod
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn;
	
	@WebMethod
	public PatientDto getPatientByDbId (long id) throws PatientServiceExn;
	
	@WebMethod
	public PatientDto[] getPatientByNameDob (String name, Date dob) throws PatientServiceExn;
	
	@WebMethod
	public TreatmentDto getTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	@WebMethod
	public String siteInfo();
	
	@WebMethod(operationName="create")
	public long createPatient(String name,Date dob, long patientId) throws PatientServiceExn;
	
	@WebMethod
	public void deletePatient(String name, long id) throws PatientServiceExn;
	
	@WebMethod
	public void addDrugTreatment(long id, String diagnosis,String drug,float dosage) throws PatientNotFoundExn;
	
	@WebMethod
	public TreatmentDto[] getTreatments (long id,long[] tid) throws PatientNotFoundExn,TreatmentNotFoundExn, PatientServiceExn;
	
	@WebMethod
	public void deleteTreatment(long id,long tid) throws PatientNotFoundExn,TreatmentNotFoundExn,PatientServiceExn;
}
