package com.userManager.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Session Bean implementation class userManagerBean
 */
@Stateless
public class userManagerBean implements userManagerBeanLocal {
	@PersistenceContext(unitName="userManagerPU")
	private EntityManager em;
	private Integer idUs; 
    /**
     * Default constructor. 
     */
    public userManagerBean() {
       
    }

 //-----------------------------------------------------------------------------wszyscy uzytkownicy stali--------------------
    
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getUsers() {
		Query q=em.createNamedQuery("findAllUser");
		return q.getResultList();
	}

//--------------------------------------------------------------------------------wszyscy studenci-----------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getUsersStudent() {
		Query q=em.createNamedQuery("findAllUserStudent");
		return q.getResultList();
	}
	
//--------------------------------------------------------------------------------logowanie uzytkownika stałego-------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getUsers(String loginUser, String hasloUser) {
		Query q=em.createNamedQuery("loginUser");
		q.setParameter("loginUsers", loginUser);
		q.setParameter("hasloUsers", hasloUser);
		return q.getResultList();
	}
	
	//----------------------------------------------------------------------------logowanie studenta-------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getUsersStudent(String loginUser, String hasloUser) {
		Query q=em.createNamedQuery("loginUserStudent");
		q.setParameter("loginUsers", loginUser);
		q.setParameter("hasloUsers", hasloUser);
		return q.getResultList();
	}
	@Override
	public <U> U create(U u) {
		em.persist(u);
		return u;
	}

	@Override
	public <U> U update(U u) {
		return em.merge(u);
	}

	@Override
	public <U> void delete(U object) {
		em.remove(em.contains(object)?object:em.merge(object));
	}
	
//-----------------------------------------------------------------------------odnajdywanie uzytkownikow stałych--------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getUsers(String finderUser) {
		try {
			idUs=Integer.parseInt(finderUser);
		}catch(NumberFormatException e) {
			idUs=null;
		}
	
		Query q=em.createNamedQuery("findUser");
		q.setParameter("loginUsers", "%"+finderUser+"%");
		q.setParameter("idSluchacz", idUs);
		q.setParameter("nazwiskoUsers","%"+finderUser+"%");
		q.setParameter("imieUsers","%"+finderUser+"%");
		q.setParameter("statusUsers","%"+finderUser+"%");
		q.setParameter("zakladUsers","%"+finderUser+"%");
		return q.getResultList();
	}
	
//---------------------------------------------------------------------------odnajdywanie studentow------------------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getUsersStudents(String finderUser,String arche) {
		try {
			idUs=Integer.parseInt(finderUser);
		}catch(NumberFormatException e) {
			idUs=null;
		}
	
		Query q=em.createNamedQuery("findUserStudent");
		q.setParameter("archiwum", arche);
		q.setParameter("loginUsers", "%"+finderUser+"%");
		q.setParameter("idSluchacz", idUs);
		q.setParameter("nazwiskoUsers","%"+finderUser+"%");
		q.setParameter("imieUsers","%"+finderUser+"%");
		q.setParameter("statusUsers","%"+finderUser+"%");
		q.setParameter("zakladUsers","%"+finderUser+"%");
		return q.getResultList();
	}
	
//----------------------------------------------------------------------------czy powtarza sie uzytkownik staly---------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getUsersRepeat(String finderUser) {
		Query q=em.createNamedQuery("loginUserIfRepeat");
		q.setParameter("loginUsers", finderUser);
		return q.getResultList();
	}

	//----------------------------------------------------------------------------czy powtarza sie uzytkownik staly---------------------------------
	
		@SuppressWarnings("unchecked")
		@Override
		public <U> List<U> getUsersRepeatStudent(String finderUser) {
			Query q=em.createNamedQuery("loginUserIfRepeatStudent");
			q.setParameter("loginUsers", finderUser);
			return q.getResultList();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <U> List<U> getUsers(int finderUser) {
			Query q=em.createNamedQuery("findUserPoId");
			q.setParameter("idUsers", finderUser);
			return q.getResultList();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <U> List<U> getUsersZaklad(String findPoZaklad) {
			Query q=em.createNamedQuery("findUserPoZaklad");
			q.setParameter("zakladUsers", findPoZaklad);
			return q.getResultList();
		}

		@Override
		public <U> List<U> getZakkladUsers() {
			Query q=em.createNamedQuery("findZakladPoUsers");
			return q.getResultList();
		}
}
 