package edu.stevens.cs548.clinic.service.representations;

import edu.stevens.cs548.clinic.service.web.rest.data.RadiologyType;

public class RadiologyRepresentation extends RadiologyType {
	
	public RadiologyRepresentation(RadiologyType radiology) {
	      this.date = radiology.getDate();
		}

		public RadiologyRepresentation() {
		}
}