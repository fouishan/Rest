package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class PatientDAO implements IPatientDAO {

	private EntityManager em;
	private TreatmentDAO treatmentDAO;

	public PatientDAO(EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientDAO.class.getCanonicalName());

	@Override
	public void addPatient(Patient patient) throws PatientServiceExn {
		try {
			long pid = patient.getPatientId();
			Query query = em.createNamedQuery("CountPatientByPatientID").setParameter("pid", pid);
			Long numExisting = (Long) query.getSingleResult();
			if (numExisting < 1) {
				em.persist(patient);
				em.flush();
				patient.setTreatmentDAO(this.treatmentDAO);
			} else {
				throw new PatientServiceExn("Insertion: Patient with patient id (" + pid + ") already exists.");
			}
		} catch (Exception e) {
			logger.info("Exception in Patient DAO at Add patient : " + e.toString());
		}
	}

	@Override
	public Patient getPatient(long id) throws PatientExn {
		Patient p = em.find(Patient.class, id);
		if (p == null) {
			throw new PatientExn("Patient not found: Primary key = " + id);
		} else {
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}

	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		TypedQuery<Patient> query = em.createNamedQuery("SearchPatientByPatientID", Patient.class).setParameter("pid",
				pid);
		List<Patient> patients = query.getResultList();
		if (patients.size() > 1) {
			throw new PatientExn("Duplicate Patient record: primary key: " + pid);
		} else if (patients.size() < 1)
			throw new PatientExn("Patient not found.Patient ID: " + pid);
		else {
			Patient p = patients.get(0);
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}

	@Override
	public void deletePatients() {
		try {
			Query update = em.createNamedQuery("RemoveAllPatients");
			em.createQuery("delete from Treatment t").executeUpdate();
			update.executeUpdate();
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	@Override
	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery<Patient> query = em.createNamedQuery("SearchPatientByNameDOB", Patient.class)
				.setParameter("name", name).setParameter("dob", dob);
		List<Patient> patients = query.getResultList();
		for (Patient p : patients) {
			p.setTreatmentDAO(this.treatmentDAO);
		}
		return patients;
	}

	@Override
	public Patient getPatientByDbId(long id) throws PatientExn {
		Patient p = em.find(Patient.class, id);
		if (p == null) {
			throw new PatientExn("Patient not found: Primary key = " + id);
		} else {
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}

	@Override
	public void deletePatient(Patient pat) throws PatientExn {
		Query update = em.createNamedQuery("RemoveAllPatients");
		em.createQuery("delete from Treatment t").executeUpdate();
		update.executeUpdate();
	}
}