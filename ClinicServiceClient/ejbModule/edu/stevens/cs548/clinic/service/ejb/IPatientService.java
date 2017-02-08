package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;

import edu.stevens.cs548.clinic.service.dto.util.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;

public interface IPatientService {
	
	public class PatientServiceExn extends Exception {
		private static final long serialVersionUID = 1L;
		public PatientServiceExn (String m) {
			super(m);
		}
	}
	public class PatientNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;
		public PatientNotFoundExn (String m) {
			super(m);
		}
	}
	public class TreatmentNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;
		public TreatmentNotFoundExn (String m) {
			super(m);
		}
	}

	public long addPatient(PatientDto dto) throws PatientServiceExn;

	public PatientDto getPatient(long id) throws PatientServiceExn;
	
	public void deletePatients();
	
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn;
	
	public PatientDto getPatientByDbId (long id) throws PatientServiceExn;
	
	public PatientDto[] getPatientByNameDob (String name, Date dob) throws PatientServiceExn;
	
	public TreatmentDto getTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	public String siteInfo();
	
	public long createPatient(String name,Date dob, long patientId) throws PatientServiceExn;
	
	public void deletePatient(String name, long id) throws PatientServiceExn;
	
	public void addDrugTreatment(long id, String diagnosis,String drug,float dosage) throws PatientNotFoundExn;
	
	public TreatmentDto[] getTreatments (long id,long[] tid) throws PatientNotFoundExn,TreatmentNotFoundExn, PatientServiceExn;
	
	public void deleteTreatment(long id,long tid) throws PatientNotFoundExn,TreatmentNotFoundExn,PatientServiceExn;
	
}
