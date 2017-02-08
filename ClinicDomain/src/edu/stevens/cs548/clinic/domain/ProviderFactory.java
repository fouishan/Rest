package edu.stevens.cs548.clinic.domain;

public class ProviderFactory implements IProviderFactory {

	@Override
	public Provider createProvider(long NPI, String name, String specialization) {
		Provider provider = new Provider();
		provider.setNPI(NPI);
		provider.setName(name);
		provider.setSpecialization(specialization);
		return provider;
	}
}