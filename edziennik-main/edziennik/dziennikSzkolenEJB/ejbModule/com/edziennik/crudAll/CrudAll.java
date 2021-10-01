package com.edziennik.crudAll;

import java.util.HashMap;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Session Bean implementation class CrudAll
 */
@Stateless
@LocalBean
public class CrudAll implements CrudAllLocal {
	@PersistenceContext(unitName="userManagerPU")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public CrudAll() {
        // TODO Auto-generated constructor stub
    }

	@SuppressWarnings("unchecked")
	@Override
	public <C> List<C> getAllTerms(String findWhat) {
		// TODO Auto-generated method stub
		Query q=em.createNamedQuery(findWhat);
		return q.getResultList();
	}

	@Override
	public <C> C create(C createData) {
		// TODO Auto-generated method stub
		em.persist(createData);
		return createData;
	}

	@Override
	public <C> C update(C updateData) {
		// TODO Auto-generated method stub
		return em.merge(updateData);
	}

	@Override
	public <C> void delete(C deleteData) {
		// TODO Auto-generated method stub
		em.remove(em.contains(deleteData)?deleteData:em.merge(deleteData));
		
	}

	@SuppressWarnings("unchecked")
	public <C> List<C> getAllTermsParam(String findWhat, HashMap<String,Object>hm) {
		// TODO Auto-generated method stub
		Query q=em.createNamedQuery(findWhat);
	for(String key:hm.keySet()) {
		q.setParameter(key, hm.get(key));
	}
		return q.getResultList();
	}

}
