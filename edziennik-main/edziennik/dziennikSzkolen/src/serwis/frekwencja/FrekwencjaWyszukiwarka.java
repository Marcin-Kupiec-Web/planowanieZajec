package serwis.frekwencja;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Obecnosc;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import serwis.logowanie.Logowanie;

@Named
@ViewScoped
public class FrekwencjaWyszukiwarka implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAllLocal crudAll;
	
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;
	@Inject
	private serwis.users.nameUser.NameUser nameUser;
	@Inject
	private Logowanie zalogowanyUser;
	
	private List<Obecnosc> obecnoscPlutonWybrany;
	private boolean usuniety;
	private boolean display;
	private Integer idPlutonWybrany;
	private String czyUsunietyWyswietlic;
	private Integer ileWpisow;


@PostConstruct
public void init() {
	wyborPlutonu.prepareChangeSzkolenie();
	idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
	if(idPlutonWybrany!=null) {
		display=true;
	}else
		display=false;
	
	if(zalogowanyUser.getPoziomUprawnien().equals("1")) {
		czyUsunietyWyswietlic="%";
		 usuniety=true;
	}
	else {
		czyUsunietyWyswietlic="NIE";
		usuniety=false;
	}
	
	HashMap<String, Object> hms=new HashMap<String,Object>();
	hms.put("idPluton", idPlutonWybrany);
	hms.put("usunieta", czyUsunietyWyswietlic);
	obecnoscPlutonWybrany=crudAll.getAllTermsParam("Obecnosc.findObNotOrCross", hms);
		
		if(obecnoscPlutonWybrany.size()>0) {
			ileWpisow=(int) obecnoscPlutonWybrany.stream().filter(ob->ob.getUsunieta().equals("NIE")).count();
		}
	}

public String getUserName(int idUser) {
	if (idUser>0)
	return nameUser.user(idUser);
	return null;
}

public String tematNieobecnosc(Pluton pl) {

	return null;
}
//-------------------------------------------gettery settery-----------------------------------------------------------------------------------


public boolean isDisplay() {
	return display;
}

public void setDisplay(boolean display) {
	this.display = display;
}
public serwis.wyborPlutonu.wyborPlutonu getWyborPlutonu() {
	return wyborPlutonu;
}

public void setWyborPlutonu(serwis.wyborPlutonu.wyborPlutonu wyborPlutonu) {
	this.wyborPlutonu = wyborPlutonu;
}



public boolean isUsuniety() {
	return usuniety;
}



public void setUsuniety(boolean usuniety) {
	this.usuniety = usuniety;
}

public Integer getIleWpisow() {
	return ileWpisow;
}

public void setIleWpisow(Integer ileWpisow) {
	this.ileWpisow = ileWpisow;
}

public String getCzyUsunietyWyswietlic() {
	return czyUsunietyWyswietlic;
}



public void setCzyUsunietyWyswietlic(String czyUsunietyWyswietlic) {
	this.czyUsunietyWyswietlic = czyUsunietyWyswietlic;
}
public serwis.users.nameUser.NameUser getNameUser() {
	return nameUser;
}

public void setNameUser(serwis.users.nameUser.NameUser nameUser) {
	this.nameUser = nameUser;
}

public List<Obecnosc> getObecnoscPlutonWybrany() {
	return obecnoscPlutonWybrany;
}

public void setObecnoscPlutonWybrany(List<Obecnosc> obecnoscPlutonWybrany) {
	this.obecnoscPlutonWybrany = obecnoscPlutonWybrany;
}


}
