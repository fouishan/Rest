package edu.stevens.cs548.clinic.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import edu.stevens.cs548.clinic.domain.DrugTreatment;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.RadiologyTreatment;
import edu.stevens.cs548.clinic.domain.Surgery;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.service.dto.util.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;

/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

	private static Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	/**
	 * Default constructor.
	 */
	public InitBean() {
	}
	
	public static void info(String m) {
		logger.info(m);
	}
	
	private Date setDate(String stringDate) {
		Date date = null;
		try {
			String pattern = "MM/dd/yyyy";
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			date = format.parse(stringDate);
		} catch (Exception e) {
			IllegalStateException ex = new IllegalStateException("Error while setting the state.");
			ex.initCause(e);
			throw ex;
		}
		return date;
	}
	
	@Inject
	private IPatientServiceLocal patService;

	@Inject
	private IProviderServiceLocal proService;
    
	@PostConstruct
	private void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		info("Initializing Service");
		logger.info("My name is Ishan Shah.");

		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(1984, 9, 4);
			
			logger.info("Initializing the patient factory.");
			PatientDtoFactory patientdtoFactory = new PatientDtoFactory();
			PatientFactory patientFactory = new PatientFactory();
			PatientDto patientdto;

			logger.info("Initializing the provider factory.");
			ProviderDtoFactory providerdtoFactory = new ProviderDtoFactory();
			ProviderFactory providerFactory = new ProviderFactory();
			ProviderDto providerdto;

			logger.info(" Initializing the TreatmentDtoFactory.");
			TreatmentDtoFactory treatmentdtoFactory = new TreatmentDtoFactory();
			TreatmentFactory treatmentFactory = new TreatmentFactory();
			TreatmentDto treatmentDto;

			logger.info("Deleting patients: ");
			patService.deletePatients();
			proService.deleteProvider();

			/*Adding Patients*/
			
			// Adding patient Peter Parker
			Patient peter = patientFactory.createPatient(1150, "Peter Parker", setDate("03/01/1992"), 24);
			patientdto = patientdtoFactory.createPatientDto(peter, 24);
			long peterId = patService.addPatient(patientdto);
			logger.info("Added Patient" + peter.getName() + " with id " + peterId);
			
			// Adding patient Chris Martin 
			Patient chris = patientFactory.createPatient(1155, "Chris Martin", setDate("01/10/1990"), 26);
			patientdto = patientdtoFactory.createPatientDto(chris, 26);
			long chrisId = patService.addPatient(patientdto);
			logger.info("Added Patient" + chris.getName() + " with id " + chrisId);
			
			// Adding patient Pablo Escobar
			Patient pablo = patientFactory.createPatient(1168, "Pablo Escobar", setDate("01/13/1994"), 22);
			patientdto = patientdtoFactory.createPatientDto(pablo, 22);
			long pabloId = patService.addPatient(patientdto);
			logger.info("Added Patient" + pablo.getName() + " with id " + pabloId);
			
			/*Adding providers*/
			
			//	Adding provider Dr. Farber
			Provider drFarber = providerFactory.createProvider(1111, "Dr. Farber", "Surgery");
			providerdto = providerdtoFactory.createProviderDto(drFarber);
			long drFarberId = proService.addProvider(providerdto);
			logger.info("Added provider " + drFarber.getName() + " with id " + drFarberId);
			
			// Adding Provider Dr. Garner
			Provider drGarner = providerFactory.createProvider(2222, "Dr. Garner", "Radiology");
			providerdto = providerdtoFactory.createProviderDto(drGarner);
			long drGarnerId = proService.addProvider(providerdto);
			logger.info("Added provider " + drGarner.getName() + " with id " + drGarnerId);
			/*Adding Treatments*/
			
			// Adding a drug Treatment for Peter through provider Dr.Farber
			
			Treatment drugTreatment = treatmentFactory.createDrugTreatment("Cough", "ColdStop", 3);
			treatmentDto = treatmentdtoFactory.createTreatmentDto((DrugTreatment) drugTreatment);
			treatmentDto.setPatient(peterId);
			treatmentDto.setProvider(drFarberId);
			long drugid = proService.addTreatment(treatmentDto);
			logger.info("Provider : " + drGarner.getName() + " supervising treatment with id " + drugid + " for patient " + peter.getName());
			
			// Adding Surgery Treatment for Chris through provider Dr. Garner 
			Treatment surgeryTreatment = treatmentFactory.createSurgeryTreatment("Fracture", setDate("01/10/2014"));
			treatmentDto = treatmentdtoFactory.createTreatmentDto((Surgery) surgeryTreatment);
			treatmentDto.setPatient(chrisId);
			treatmentDto.setProvider(drGarnerId);
			long surgid = proService.addTreatment(treatmentDto);
			logger.info("Provider : " + drFarber.getName() + " supervising treatment with id " + surgid + " for patient " + chris.getName());
			
			// Adding Radiology Treatment for Pablo Escobar through Provider Dr. Garner 
			List<Date> radiologyDates = new ArrayList<Date>();
			for (int i = 1; i < 4; i++) {
				radiologyDates.add(setDate("01/" + i + "/2016"));
			}
			
			Treatment radiologyTreatment = treatmentFactory.createRadiologyTreatment("Cancer", radiologyDates);
			treatmentDto = treatmentdtoFactory.createTreatmentDto((RadiologyTreatment) radiologyTreatment);
			treatmentDto.setPatient(pabloId);
			treatmentDto.setProvider(drGarnerId);
			long radid = proService.addTreatment(treatmentDto);
			logger.info("Provider : " + drGarner.getName() + " supervising treatment with id " + radid + " for patient "  + pablo.getName());
			
			// Adding a drug treatment
			Treatment drugTrmt = treatmentFactory.createDrugTreatment("Cold", "Advil", 2);
			treatmentDto = treatmentdtoFactory.createTreatmentDto((DrugTreatment) drugTrmt);
			treatmentDto.setPatient(pabloId);
			treatmentDto.setProvider(drGarnerId);
			long drugTreatmentid = proService.addTreatment(treatmentDto);
			logger.info("Provider : " + drGarner.getName() + " supervising treatment with id " + drugTreatmentid + " for patient " + pablo.getName());
			
			
		} catch (Exception e) {
			IllegalStateException ex = new IllegalStateException("Failed");
			ex.initCause(e);
			throw ex;
			
		} 
			
	}

}
