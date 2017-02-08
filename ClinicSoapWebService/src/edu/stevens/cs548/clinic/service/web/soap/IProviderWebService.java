package edu.stevens.cs548.clinic.service.web.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import edu.stevens.cs548.clinic.service.dto.util.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.TreatmentNotFoundExn;

@WebService(name = "IProviderWebPort"
			, targetNamespace = "http://cs548.stevens.edu/clinic/service/web/soap/provider")
/*
 * Endpoint interface for the provider Web service.
 */

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, 
use = SOAPBinding.Use.LITERAL, 
parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)

public interface IProviderWebService {

	@WebMethod
	public long addProvider(
			//@WebParam(name = "provider-dto", targetNamespace = "http://cs548.stevens.edu/clinic/dto") 
			ProviderDto dto)
					throws ProviderServiceExn;

	@WebMethod
	public ProviderDto getProviderByNPI(long id) throws ProviderServiceExn;

	@WebMethod
	public ProviderDto getProviderByProId(long pid) throws ProviderServiceExn;

	@WebMethod(operationName = "providerGetTreatment")
	public TreatmentDto getTreatment(long id, long tid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;

	@WebMethod
	public String siteInfo();

}