package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.service.dto.util.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;

public interface IProviderService {
	
	public class ProviderServiceExn extends Exception {
		private static final long serialVersionUID = 1L;
		public ProviderServiceExn (String m) {
			super(m);
		}
	}
	public class ProviderNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;
		public ProviderNotFoundExn (String m) {
			super(m);
		}
	}
	public class TreatmentNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;
		public TreatmentNotFoundExn (String m) {
			super(m);
		}
	}

	public long addProvider(ProviderDto dto) throws ProviderServiceExn;

	public ProviderDto getProviderByNPI(long pid) throws ProviderServiceExn;

	public ProviderDto getProviderByProId(long id) throws ProviderServiceExn;
	
	public long addTreatment(TreatmentDto tdto)  throws ProviderServiceExn;
	
	public void deleteProvider();
	
	public String siteInfo();
	
	public TreatmentDto getTreatment(long id, long tid)  
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;

}