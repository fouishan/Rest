package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface IPatientDAO {
	
	public static class PatientExn extends Exception {
		private static final long serialVersionUID = 1L;
		public PatientExn (String msg) {
			super(msg);
		}
	}
	
	public class PatientServiceExn extends Exception {
		private static final long serialVersionUID = 1L;
		public PatientServiceExn (String m) {
			super(m);
		}
	}

	public void addPatient (Patient pat) throws PatientServiceExn;
	
	public Patient getPatientByPatientId (long pid) throws PatientExn;
	
	public Patient getPatient (long id) throws PatientExn;
	
	public void deletePatients();
	
	public List<Patient> getPatientByNameDob(String name, Date dob);

	public Patient getPatientByDbId(long id) throws PatientExn;
	
	public void deletePatient (Patient pat) throws PatientExn;

}
