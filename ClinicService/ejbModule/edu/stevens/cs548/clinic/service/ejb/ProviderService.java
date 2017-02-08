package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.util.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.util.ObjectFactory;

@Stateless(name="ProviderServiceBean")
public class ProviderService implements IProviderServiceLocal, IProviderServiceRemote {

	private IProviderFactory providerFactory;
	private ProviderDtoFactory providerDtoFactory;
	private IProviderDAO providerDAO;
	private IPatientDAO patientDAO;

	public ProviderService(){
		
		providerFactory = new ProviderFactory();
		providerDtoFactory = new ProviderDtoFactory();
	}
	
	@Inject
	@ClinicDomain
	private EntityManager em;

	@PostConstruct
	private void initialize() {
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);
	}
	
	@Override
	public long addProvider(ProviderDto dto) throws ProviderServiceExn {
		try {
			Provider provider = providerFactory.createProvider(dto.getProviderId(), dto.getName(),
					dto.getSpecialization());
			providerDAO.addProvider(provider);
			return provider.getId();
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public ProviderDto getProviderByNPI(long pid) throws ProviderServiceExn {
		ProviderDto providerDto;
		try {
			Provider provider = providerDAO.getProviderByNPI(pid);
			providerDto = providerDtoFactory.createProviderDto(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
		return providerDto;
	}

	@Override
	public ProviderDto getProviderByProId(long id) throws ProviderServiceExn {
		ProviderDto providerDto;
		try {
			Provider provider = providerDAO.getProviderByProviderId(id);
			providerDto = providerDtoFactory.createProviderDto(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
		return providerDto;
	}

	@Override
	public long addTreatment(TreatmentDto tdto) throws ProviderServiceExn {
		long tid = 0;
		try {
			Provider provider = providerDAO.getProviderByProviderId(tdto.getProvider());
			Patient patient = patientDAO.getPatient(tdto.getPatient());
			if (tdto.getDrugTreatment() != null) {
				tid = provider.addDrugTreatment(tdto.getDiagnosis(), tdto.getDrugTreatment().getName(),
						tdto.getDrugTreatment().getDosage(), patient);
			} else if (tdto.getRadiology() != null) {
				tid = provider.addRadiologyTreatment(tdto.getRadiology().getDate(), tdto.getDiagnosis(), patient);
			} else if (tdto.getSurgery() != null) {
				tid = provider.addSurgeryTreatment(tdto.getSurgery().getDate(), tdto.getDiagnosis(), patient);
			}
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderServiceExn(e.toString());
		}
		return tid;
	}

	@Override
	public void deleteProvider() {
		providerDAO.deleteProvider();
	}
	
	@Resource(name = "SiteInform")
	private String siteInformation;

	@Override
	public String siteInfo() {
		return siteInformation;
	}

	public class TreatmentExporter implements ITreatmentExporter<TreatmentDto> {

		private ObjectFactory factory = new ObjectFactory();

		@Override
		public TreatmentDto exportDrugTreatment(long tid, String diagnosis, String drug, float dosage) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			dto.setId(tid);
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
	throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			Provider provider = providerDAO.getProviderByProviderId(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return provider.exportTreatment(tid, visitor);
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
}
