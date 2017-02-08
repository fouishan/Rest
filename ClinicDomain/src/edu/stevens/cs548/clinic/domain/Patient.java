package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Patient
 *
 */

@NamedQueries({
	@NamedQuery(
		name="SearchPatientByPatientID",
		query="select p from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name="CountPatientByPatientID",
		query="select count(p) from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name = "RemoveAllPatients", 
		query = "delete from Patient p"),
	@NamedQuery(
			name = "SearchPatientByNameDOB",
	query = "select p from Patient p where p.name = :name  and p.birthDate = :dob")
})

@Entity
@Table(name="PATIENT")
public class Patient implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	private long patientId;
	
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	void addTreatment(Treatment t){
		this.treatmentDAO.addTreatment(t);
		this.getTreatments().add(t);
		if(t.getPatient() != this)
			t.setPatient(this);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	@OneToMany(mappedBy = "patient", cascade = REMOVE)
	@OrderBy
	private List<Treatment> treatments;

	protected List<Treatment> getTreatments() {
		return treatments;
	}

	protected void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO(ITreatmentDAO tdao)
	{
		this.treatmentDAO = tdao;
	}
	
	public List<Long> getTreatmentIds(){
		List<Long> tids = new ArrayList<Long>();
		for(Treatment t : this.getTreatments()){
			tids.add(t.getId());
		}
		return tids;
	}
	
	public void addDrugTreatment(String diagnosis, String drug, float dosage)
	{
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		this.addTreatment(treatment);
	}
	
	public void visitreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn
	{
		Treatment t = treatmentDAO.getTreatment(tid);
		if(t.getPatient() == this)
		{
			throw new TreatmentExn("Inappropriate treatment access: Patient = " + id
					+ ", treatment = " + tid);
		}
		t.visit(visitor);
	}
	
	public void visitTreatments(ITreatmentVisitor visitor)
	{
		for(Treatment t : this.getTreatments())
		{
			t.visit(visitor);
		}
	}
	
	public void deleteTreatment(long tid) throws TreatmentExn
	{
		Treatment t = treatmentDAO.getTreatment(tid);
		if(t.getPatient() == this)
		{
			throw new TreatmentExn("Inappropriate treatment access: Patient = " + id
					+ ", treatment = " + tid);
		}
		treatmentDAO.deleteTreatment(t);
	}
	
	public <T> T exportTreatment(long tid, ITreatmentExporter<T> visitor) throws TreatmentExn {
		// Export a treatment without violated Aggregate pattern
		// Check that the exported treatment is a treatment for this provider.
		Treatment t = treatmentDAO.getTreatment(tid);
		if (t.getPatient() != this) {
			throw new TreatmentExn("Inappropriate treatment access: Patient = " + id + ", treatment = " + tid);
		}
		return t.export(visitor);
	}

	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
	}
}