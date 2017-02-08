package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RadiologyTreatment
 *
 */
@Entity
@DiscriminatorValue("R")
public class RadiologyTreatment extends Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ElementCollection
	@CollectionTable(name = "RADIOLOGYDATES", joinColumns = @JoinColumn(name = "radiologyTreatment_fk") )
	private List<RadiologyDate> radiologyDates = new ArrayList<RadiologyDate>();

	public RadiologyTreatment() {
		super();
		this.setTreatmentType("R");
	}
	
	public List<RadiologyDate> getRadiologyDates() {
		return radiologyDates;
	}

	public void setRadiologyDates(List<RadiologyDate> radiologyDates) {
		this.radiologyDates = radiologyDates;
	}

	public <T> T export(ITreatmentExporter<T> visitor)	
	{
		List<Date> rdList = new ArrayList<Date>();
		for(RadiologyDate rd : this.radiologyDates){
			rdList.add(rd.getRadiologyDate());
		}
		return visitor.exportRadiology(this.getId(),this.getDiagnosis(),rdList);
	}

	@Override
	public void visit(ITreatmentVisitor visitor) {
		// TODO Auto-generated method stub
	}
}
