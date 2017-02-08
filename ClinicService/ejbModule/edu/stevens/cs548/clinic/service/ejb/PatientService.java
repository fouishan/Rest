package edu.stevens.cs548.clinic.service.ejb;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IPatientFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.service.dto.util.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.util.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.util.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.util.SurgeryType;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name="PatientServiceBean")
public class PatientService implements IPatientServiceLocal,
		IPatientServiceRemote {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientService.class.getCanonicalName());

	private IPatientFactory patientFactory;
	
	private PatientDtoFactory patientDtoFactory;

	private IPatientDAO patientDAO;

	/**
	 * Default constructor.
	 */
	public PatientService() {
		patientFactory = new PatientFactory();
		// patientDAO = new PatientDAO(em);
		patientDtoFactory = new PatientDtoFactory();
	}
	
	@PostConstruct
	private void initialize()
	{
		patientDAO = new PatientDAO(em);
	}

	@Inject @ClinicDomain
	private EntityManager em;

	/**
	 * @see IPatientService#addPatient(String, Date, long)
	 */
	@Override
	public long addPatient(PatientDto dto) throws PatientServiceExn {
		// Use factory to create patient entity, and persist with DAO
		try {
			Patient patient = patientFactory.createPatient(dto.getPatientId(), dto.getName(), dto.getDob(), dto.getAge());
			patientDAO.addPatient(patient);
			return patient.getId();
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
		catch(Exception e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @see IPatientService#getPatient(long)
	 */
	@Override
	public PatientDto getPatient(long id) throws PatientServiceExn {
		PatientDto patientDto;
		try{
			Patient patient = patientDAO.getPatient(id);
			patientDto = patientDtoFactory.createPatientDto(patient);
		}catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
		}
		return patientDto;
	}

	/**
	 * @see IPatientService#getPatientByPatId(long)
	 */
	/*@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
	PatientDto patientDto;
	try{
		Patient patient = patientDAO.getPatientByPatientId(pid);
		patientDto = patientDtoFactory.createPatientDto(patient);
	}catch(PatientExn e){
		throw new PatientServiceExn(e.toString());
	}
	return patientDto;
	}*/

	public class TreatmentExporter implements ITreatmentExporter<TreatmentDto> {
		
		private ObjectFactory factory = new ObjectFactory();
		
		@Override
		public TreatmentDto exportDrugTreatment(long tid, String diagnosis, String drug,
				float dosage) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			DrugTreatmentType drugInfo = factory.createDrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			dto.setDrugTreatment(drugInfo);
			return dto;
		}

		@Override
		public TreatmentDto exportRadiology(long tid, String diagnosis, List<Date> dates) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			dto.setId(tid);
			RadiologyType radiology = factory.createRadiologyType();
			radiology.getDate().addAll(dates);
			dto.setRadiology(radiology);
			return dto;
		}

		@Override
		public TreatmentDto exportSurgery(long tid, String diagnosis, Date date) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			dto.setId(tid);
			SurgeryType surgery = factory.createSurgeryType();
			surgery.setDate(date);
			dto.setSurgery(surgery);
			return dto;
		}
		
	}
	
	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			Patient patient = patientDAO.getPatient(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return patient.exportTreatment(tid, visitor);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	@Resource(name="SiteInfo")
	private String siteInformation;
	
	
	@Override
	public String siteInfo() {
		return siteInformation;
	}
	
	private PatientDto patientToDto(Patient patient)
	{
		return new PatientDto(patient);
	}

	@Override
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn {
		try
		{
			Patient patient = patientDAO.getPatientByDbId(id);
			return new PatientDto(patient);
		}		
		catch(PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		try
		{
			Patient patient = patientDAO.getPatientByPatientId(pid);
			logger.info("Inside PatientService 1");
			logger.info("ID: " + patient.getId()
			+ "\n patientId: " + patient.getPatientId()
			+ "\n name: " + patient.getName()
			+ "\n Birthday: " + patient.getBirthDate()
			+ "\n Treatments: " + patient.getTreatmentIds().get(0));
			PatientDto Pd = new PatientDto(patient);
			logger.info("Inside PatientService 2");
			return Pd;
		}		
		catch(PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public PatientDto[] getPatientByNameDob(String name, Date dob) {
		List<Patient> patients = patientDAO.getPatientByNameDob(name, dob);
		PatientDto[] dto = new PatientDto[patients.size()];
		for(int i=0; i < dto.length; i++)
		{
			dto[i] = new PatientDto(patients.get(i));
		}
		return dto;
	}

	@Override
	public void deletePatient(String name, long id) throws PatientServiceExn {
		try
		{
			Patient patient = patientDAO.getPatientByDbId(id);
			if(!name.equals(patient.getName()))
			{
				throw new PatientServiceExn("Tried to delete wrong patient: name = " + name + " , id = " + id);
			}
			else
			{
				patientDAO.deletePatient(patient);
			}
		}
		catch(PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	//@Override
	public long createPatient(String name, Date dob, long patientId) throws PatientServiceExn {
		Patient patient;
		try {
			patient = this.patientFactory.createPatient(patientId, name, dob
					, Period.between(LocalDate.now(), dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears());
			patientDAO.addPatient(patient);
		}
		catch(PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
		catch(Exception e){
			throw new PatientServiceExn(e.toString());
		}
		return patient.getId();
	}

	@Override
	public void addDrugTreatment(long id, String diagnosis, String drug, float dosage) throws PatientNotFoundExn {
		try
		{
			Patient patient = patientDAO.getPatientByDbId(id);
			patient.addDrugTreatment(diagnosis, drug, dosage);
		}
		catch(PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
	}
	
	static class TreatmentPDOToDTO implements ITreatmentVisitor {

		private TreatmentDto dto;
		
		public TreatmentDto getDto(){
			return dto;
		}
		
		@Override
		public void visitDrugTreatment(long tid, String diagnosis, String drug, float dosage) {
			dto = new TreatmentDto();
			dto.setDiagnosis(diagnosis);
			DrugTreatmentType drugInfo = new DrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			dto.setDrugTreatment(drugInfo);
		}

		@Override
		public void visitRadiology(long tid, String diagnosis, List<Date> dates) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitSurgery(long tid, String diagnosis, Date date) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public TreatmentDto[] getTreatments(long id, long[] tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try
		{
			Patient patient = patientDAO.getPatientByDbId(id);
			TreatmentDto[] treatments = new TreatmentDto[tid.length];
			for(int i = 0; i<treatments.length; i++)
			{
				TreatmentPDOToDTO visitor = new TreatmentPDOToDTO();
				patient.visitreatment(tid[i], visitor);
				treatments[i] = visitor.getDto();
			}
			return treatments;
		}
		catch(PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
		catch(TreatmentExn e){
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try
		{
			Patient patient = patientDAO.getPatientByDbId(id);
			patient.deleteTreatment(tid);
		}
		catch(PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
		catch(TreatmentExn e){
			throw new PatientServiceExn(e.toString());
		}
	}
	
	@Override
	public void deletePatients() {
		patientDAO.deletePatients();
	}
}
