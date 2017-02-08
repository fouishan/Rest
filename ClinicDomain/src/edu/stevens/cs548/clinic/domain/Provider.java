package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import static javax.persistence.CascadeType.REMOVE;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;;

/**
 * Entity implementation class for Entity: Provider
 *
 */
@NamedQueries({
	@NamedQuery(name = "SearchProviderByProviderID", query = "select p from Provider p where p.NPI = :pid"),
	@NamedQuery(name = "CountProviderByProviderID", query = "select count(p) from Provider p where p.NPI = :pid"),
	@NamedQuery(name = "RemoveAllProvider", query = "delete from Provider p") })
@Entity
@Table(name="PROVIDER")
public class Provider implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	private long NPI;

	public long getNPI() {
		return NPI;
	}

	public void setNPI(long nPI) {
		NPI = nPI;
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private String specialization;

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	
	
	@OneToMany(cascade = REMOVE, mappedBy = "provider")
	@OrderBy
	private List<Treatment> treatments;

	public List<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO(ITreatmentDAO tdao) {
		this.treatmentDAO = tdao;
	}
	
	public long addTreatment(Treatment t) {
		// Persist treatment and set forward and backward links
		this.getTreatments().add(t);
		if (t.getProvider() != this) {
			t.setProvider(this);
		}
		return t.getId();
	}
	
	public long addDrugTreatment(String diagnosis, String drugName, float dosage, Patient patient) {
		DrugTreatment drugTreatment = new DrugTreatment();
		drugTreatment.setDiagnosis(diagnosis);
		drugTreatment.setDrug(drugName);
		drugTreatment.setDosage(dosage);
		drugTreatment.setPatient(patient);
		this.addTreatment(drugTreatment);
		return drugTreatment.getId();
	}

	public long addSurgeryTreatment(Date surgeryDate, String diagnosis, Patient patient) {
		Surgery surgeryTreatment = new Surgery();
		surgeryTreatment.setSurgeryDate(surgeryDate);
		surgeryTreatment.setDiagnosis(diagnosis);
		surgeryTreatment.setPatient(patient);
		this.addTreatment(surgeryTreatment);
		return surgeryTreatment.getId();
	}

	public long addRadiologyTreatment(List<Date> radiologyDates, String diagnosis, Patient patient) {
		RadiologyTreatment radiologyTreatment = new RadiologyTreatment();
		for (Date date : radiologyDates) {
			RadiologyDate dates = new RadiologyDate();
			dates.setRadiologyDate(date);
			radiologyTreatment.getRadiologyDates().add(dates);
		}
		radiologyTreatment.setDiagnosis(diagnosis);
		radiologyTreatment.setPatient(patient);
		this.addTreatment(radiologyTreatment);
		return radiologyTreatment.getId();
	}

	public <T> T exportTreatment(long tid, ITreatmentExporter<T> visitor) throws TreatmentExn {
		// Export a treatment without violated Aggregate pattern
		// Check that the exported treatment is a treatment for this provider.
		Treatment t = treatmentDAO.getTreatment(tid);
		if (t.getProvider() != this) {
			throw new TreatmentExn("Inappropriate treatment access: provider = " + id + ", treatment = " + tid);
		}
		return t.export(visitor);
	}
	
	public Provider()
	{
		super();
		treatments = new ArrayList<Treatment>();
	}
	
	public List<Long> getTreatmentIds() {
		List<Long> tIdList = new ArrayList<Long>();
		for (Treatment t : this.getTreatments()) {
			tIdList.add(t.getId());
		}
		return tIdList;
	}
}