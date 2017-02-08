package edu.stevens.cs548.clinic.service.web.soap;

import javax.ejb.EJB;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.util.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceRemote;

// Use JSR-181 annotations to specify Web service.
//Specify: endpoint interface (FQN), target namespace, service name, port name

@WebService(endpointInterface = "edu.stevens.cs548.clinic.service.web.soap.IProviderWebService",
			targetNamespace = "http://cs548.stevens.edu/clinic/service/web/soap/provider", 
			serviceName = "ProviderWebService", 
			portName = "ProviderWebPort")

public class ProviderWebService implements IProviderWebService {

	@EJB(beanName="ProviderServiceBean")
	IProviderServiceRemote service;

	@Override
	public long addProvider(ProviderDto dto) throws ProviderServiceExn {
		return service.addProvider(dto);
	}

	@Override
	public ProviderDto getProviderByNPI(long id) throws ProviderServiceExn {
		return service.getProviderByNPI(id);
	}

	@Override
	public ProviderDto getProviderByProId(long pid) throws ProviderServiceExn {
		return service.getProviderByProId(pid);
	}

	@Override
	public String siteInfo() {
		return service.siteInfo();
	}

	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn {
		return service.getTreatment(id, tid);
	}

}