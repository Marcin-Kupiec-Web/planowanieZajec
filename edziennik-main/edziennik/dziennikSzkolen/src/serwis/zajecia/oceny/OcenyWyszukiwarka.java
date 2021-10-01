package serwis.zajecia.oceny;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Ocena;
import com.userManager.model.UsersStudent;

import my.util.OcenyCompare;
import my.util.OcenyCompareDataWpisu;
import serwis.logowanie.Logowanie;

@Named
@ViewScoped
public class OcenyWyszukiwarka implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;
	@Inject
	private Logowanie zalogowanyUser;
	
	private UsersStudent ussluch=null;
	
	private int idUsers;
	
	private Integer idPlutonWybrany;
	
	private List<Ocena> ocenyList;
	
@PostConstruct
	public void init() {
	wyborPlutonu.prepareChangeSzkolenie();
	idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
 	idUsers=zalogowanyUser.getIdUsers();
	//szukam pluton	
		 if(zalogowanyUser.getPoziomUprawnien().equals("7")) {
				HashMap<String, Object> hmsl=new HashMap<String,Object>();
				hmsl.put("idUsers", zalogowanyUser.getIdUsers());
		        try {
		        	ussluch=(UsersStudent) crudAll.getAllTermsParam("findUserPoIdus", hmsl).get(0);
		        	ocenyList=ussluch.getSluchacze().getOcenas();
		        	Collections.sort(ocenyList,new OcenyCompareDataWpisu());
		        	       
		        } catch(IndexOutOfBoundsException e) {
		        }
			 
			}else {
				HashMap<String, Object> hmsp=new HashMap<String,Object>();
				hmsp.put("idPluton", idPlutonWybrany);			
				ocenyList=crudAll.getAllTermsParam("Ocena.findAllinPluton", hmsp);
				Collections.sort(ocenyList,new OcenyCompare());
			}
}




//----------------------------------------------------getters setters----------------------------------------------------------------
	public Integer getIdPlutonWybrany() {
		return idPlutonWybrany;
	}

	public void setIdPlutonWybrany(Integer idPlutonWybrany) {
		this.idPlutonWybrany = idPlutonWybrany;
	}

	public serwis.wyborPlutonu.wyborPlutonu getWyborPlutonu() {
		return wyborPlutonu;
	}

	public void setWyborPlutonu(serwis.wyborPlutonu.wyborPlutonu wyborPlutonu) {
		this.wyborPlutonu = wyborPlutonu;
	}
	public List<Ocena> getOcenyList() {
		return ocenyList;
	}
	public void setOcenyList(List<Ocena> ocenyList) {
		this.ocenyList = ocenyList;
	}




	public int getIdUsers() {
		return idUsers;
	}




	public void setIdUsers(int idUsers) {
		this.idUsers = idUsers;
	}




	public UsersStudent getUssluch() {
		return ussluch;
	}




	public void setUssluch(UsersStudent ussluch) {
		this.ussluch = ussluch;
	}

}
