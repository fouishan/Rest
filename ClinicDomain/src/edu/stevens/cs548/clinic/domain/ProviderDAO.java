package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ProviderDAO implements IProviderDAO {

	private EntityManager em;
	private TreatmentDAO treatmentDAO;

	public ProviderDAO(EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderDAO.class.getCanonicalName());

	@Override
	public long addProvider(Provider provider) throws ProviderExn {
		long pid = provider.getNPI();
		Query query = em.createNamedQuery("CountProviderByProviderID").setParameter("pid", pid);
		Long numExisting = (Long) query.getSingleResult();
		if (numExisting < 1) {
			em.persist(provider);
			em.flush();
			provider.setTreatmentDAO(this.treatmentDAO);
			return provider.getId();
		} else {
			throw new ProviderExn("Insertion: Provider with provider id (" + pid + ") already exists.");
		}
	}

	@Override
	public Provider getProviderByProviderId(long pid) throws ProviderExn {
		Provider provider = em.find(Provider.class, pid);
		if (provider == null) {
			throw new ProviderExn("No provider Found with the provided Id");
		} else {
			provider.setTreatmentDAO(this.treatmentDAO);
			return provider;
		}
	}

	@Override
	public Provider getProviderByNPI(long pid) throws ProviderExn {
		TypedQuery<Provider> query = em.createNamedQuery("SearchProviderByProviderID", Provider.class).setParameter("pid",
				pid);
		List<Provider> providerList = query.getResultList();
		if (providerList.isEmpty() || providerList.size() > 1) {
			throw new ProviderExn("Ambiguious result - provider record found with provided provider Id : "+providerList.size());
		} else {
			Provider provider = providerList.get(0);
			provider.setTreatmentDAO(this.treatmentDAO);
			return provider;
		}
	}

	@Override
	public void deleteProvider() {
		Query update = em.createNamedQuery("RemoveAllProvider");
		em.createQuery("delete from Treatment t").executeUpdate();
		update.executeUpdate();
		
	}
}