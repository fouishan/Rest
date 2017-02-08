package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import org.eclipse.persistence.annotations.TypeConverter;

/**
 * Entity implementation class for Entity: RadiologyDates
 *
 */

@Embeddable

@TypeConverter(name = "dateConverter", dataType = java.util.Date.class)
public class RadiologyDate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.DATE)
	@Column(name="radiologyDate", nullable=false) 
	
	private Date radiologyDate;
	
	public RadiologyDate() {
		super();
	}
	
	
	public Date getRadiologyDate() {
		return radiologyDate;
	}

	public void setRadiologyDate(Date radiologyDate) {
		this.radiologyDate = radiologyDate;
	}
   
}
