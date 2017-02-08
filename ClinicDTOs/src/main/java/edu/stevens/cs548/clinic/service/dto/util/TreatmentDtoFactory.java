package edu.stevens.cs548.clinic.service.dto.util;


import edu.stevens.cs548.clinic.domain.DrugTreatment;
import edu.stevens.cs548.clinic.domain.RadiologyDate;
import edu.stevens.cs548.clinic.domain.RadiologyTreatment;
import edu.stevens.cs548.clinic.domain.Surgery;
import edu.stevens.cs548.clinic.service.dto.util.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;

public class TreatmentDtoFactory {
	
	ObjectFactory factory;
	
	public TreatmentDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public DrugTreatmentType createDrugTreatmentDto () {
		return factory.createDrugTreatmentType();
	}

	public SurgeryType createsurgeryTreatmentDto () {
		return factory.createSurgeryType();
	}
	public RadiologyType createRadiologyTreatmentDto () {
		return factory.createRadiologyType();
	}
	
	public TreatmentDto createTreatmentDto(){
		return factory.createTreatmentDto();
	}
	
	public TreatmentDto createTreatmentDto (DrugTreatment t) {
		TreatmentDto treatment = factory.createTreatmentDto();
		treatment.setDiagnosis(t.getDiagnosis());
		DrugTreatmentType drugTreatment = this.createDrugTreatmentDto();
		drugTreatment.setDosage(t.getDosage());
		drugTreatment.setName(t.getDrug());
		treatment.setDrugTreatment(drugTreatment);
		return treatment;
	}
	
	public TreatmentDto createTreatmentDto(Surgery t){
		TreatmentDto treatment = factory.createTreatmentDto();
		treatment.setDiagnosis(t.getDiagnosis());
		SurgeryType surgeryTreatment = this.createsurgeryTreatmentDto();
		surgeryTreatment.setDate(t.getSurgeryDate());
		treatment.setSurgery(surgeryTreatment);
		return treatment;
		
	}
	
	public TreatmentDto createTreatmentDto(RadiologyTreatment t){
		TreatmentDto treatment = factory.createTreatmentDto();
		treatment.setDiagnosis(t.getDiagnosis());
		RadiologyType radiologyTreatment = this.createRadiologyTreatmentDto();
		for(RadiologyDate d :t.getRadiologyDates()){
			radiologyTreatment.getDate().add(d.getRadiologyDate());	
		}
		treatment.setRadiology(radiologyTreatment);
		return treatment;
		
	}

}
