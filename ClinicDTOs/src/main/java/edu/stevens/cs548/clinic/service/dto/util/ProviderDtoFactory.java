package edu.stevens.cs548.clinic.service.dto.util;

import java.util.List;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.util.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDto;

public class ProviderDtoFactory {
	
	ObjectFactory factory;
	
	public ProviderDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public ProviderDto createProviderDto () {
		return factory.createProviderDto();
	}
	
	public ProviderDto createProviderDto (Provider p) {
		ProviderDto d = factory.createProviderDto();
		d.setId(p.getId());
		d.setProviderId(p.getNPI());
		d.setSpecialization(p.getSpecialization());
		List<Long> tids = p.getTreatmentIds();
		for(Long tid : tids){
			d.getTreatments().add(tid);
		}
		return d;
	}
}