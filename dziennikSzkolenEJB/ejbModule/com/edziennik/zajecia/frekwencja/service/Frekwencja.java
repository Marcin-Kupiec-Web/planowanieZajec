package com.edziennik.zajecia.frekwencja.service;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Session Bean implementation class Frekwencja
 */
@Stateless
@LocalBean
public class Frekwencja implements FrekwencjaLocal {
	@PersistenceContext(unitName="userManagerPU")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public Frekwencja() {
        // TODO Auto-generated constructor stub
    }

	@SuppressWarnings("unchecked")
	public <F> List<F> getObecnoscInDay(Integer idPluton, Date data) {
		Query q=em.createNamedQuery("Obecnosc.findWgPlutonuDoObecnosci");
		q.setParameter("idPluton", idPluton);
		q.setParameter("data", data,TemporalType.DATE);
		return q.getResultList();
	}

	@Override
	public <F> F create(F f) {
		// TODO Auto-generated method stub
		em.persist(f);
		return f;
	}

	@Override
	public <F> F update(F f) {
		// TODO Auto-generated method stub
		return em.merge(f);
	}

	@Override
	public <F> void delete(F object) {
		// TODO Auto-generated method stub
		em.remove(em.contains(object)?object:em.merge(object));
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <F> List<F> findByObecnoscById(int idF) {
		// TODO Auto-generated method stub
		Query q=em.createNamedQuery("ObecnoscPoprawa.findById");
		q.setParameter("idObPopr", idF);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <F> List<F> findNbSp(String idPluton) {
		// TODO Auto-generated method stub
		Query q=em.createNamedQuery("Obecnosc.findNbSp");
		q.setParameter("idPluton", idPluton);
		return q.getResultList();
	}

	

}
