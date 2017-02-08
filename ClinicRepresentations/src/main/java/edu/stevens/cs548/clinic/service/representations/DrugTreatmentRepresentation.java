package edu.stevens.cs548.clinic.service.representations;

import edu.stevens.cs548.clinic.service.web.rest.data.DrugTreatmentType;

public class DrugTreatmentRepresentation extends DrugTreatmentType {
	
	public DrugTreatmentRepresentation (DrugTreatmentType drugTreatment){
		this.setDosage(drugTreatment.getDosage());
		this.setName(drugTreatment.getName());
	}
	
	public DrugTreatmentRepresentation()
	{
	
	}

}
