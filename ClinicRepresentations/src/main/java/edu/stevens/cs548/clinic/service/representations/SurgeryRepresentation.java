package edu.stevens.cs548.clinic.service.representations;

import edu.stevens.cs548.clinic.service.web.rest.data.SurgeryType;

public class SurgeryRepresentation extends SurgeryType {
	
	public SurgeryRepresentation (SurgeryType surgery){
		this.setDate(surgery.getDate());
	}
	
	public SurgeryRepresentation(){
		
	}
}