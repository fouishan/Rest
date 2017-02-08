package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public class TreatmentFactory implements ITreatmentFactory {

	@Override
	public Treatment createDrugTreatment(String diagnosis, String drug, float dosage) {
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		treatment.setTreatmentType(TreatmentType.DRUG_TREATMENT.getTag());
		return treatment;
	}

	public Treatment createRadiologyTreatment(String diagnosis,List<Date> date)
	{
		RadiologyTreatment treatment = new RadiologyTreatment();
		
		for (Date radiologyDate : date) {
			RadiologyDate dates = new RadiologyDate();
			dates.setRadiologyDate(radiologyDate);
			treatment.getRadiologyDates().add(dates);
		}
		treatment.setDiagnosis(diagnosis);
		treatment.setTreatmentType(TreatmentType.RADIOLOGY.getTag());
		return treatment;
		
	}

	@Override
	public Treatment createSurgeryTreatment(String diagnosis, Date date) {
		Surgery surgeryTreatment = new Surgery();
		surgeryTreatment.setSurgeryDate(date);
		surgeryTreatment.setDiagnosis(diagnosis);
		surgeryTreatment.setTreatmentType(TreatmentType.SURGERY.getTag());
		return surgeryTreatment;
	}
}
