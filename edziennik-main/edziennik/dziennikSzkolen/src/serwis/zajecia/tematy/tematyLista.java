package serwis.zajecia.tematy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Obecnosc;
import com.edziennik.sluchacze.zajecia.model.Tematy;

import serwis.logowanie.Logowanie;

@Named
@ViewScoped
public class tematyLista implements Serializable{
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
	private List<Tematy> tematyPlutonWybrany;
	private boolean usuniety;
	private boolean display;
	private Integer idPlutonWybrany;
	private String czyUsunietyWyswietlic;
	private Integer ileTematow;
	public String getCzyUsunietyWyswietlic() {
		return czyUsunietyWyswietlic;
	}

	public void setCzyUsunietyWyswietlic(String czyUsunietyWyswietlic) {
		this.czyUsunietyWyswietlic = czyUsunietyWyswietlic;
	}


private List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyWplutonie;


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
	hms.put("usuniety", czyUsunietyWyswietlic);
	tematyPlutonWybrany=crudAll.getAllTermsParam("Tematy.findTemNotOrCross", hms);
		
		if(tematyPlutonWybrany.size()>0) {
			ileTematow=(int) tematyPlutonWybrany.stream().filter(tem->tem.getTematUsuniety().equals("NIE")).count();
		}
	}

public String getUserName(int idUser) {
	if (idUser>0)
	return nameUser.user(idUser);
	return null;
}

public String tematNieobecni(int plt,Date data, int godzL) {
	if(plt!=0 && data!=null && godzL!=0) {
	HashMap<String, Object> hms=new HashMap<String,Object>();
	hms.put("idPluton", plt);
	hms.put("godzLekc", godzL);
	hms.put("data", data);
	List<Obecnosc> ob=new ArrayList<Obecnosc>();
	ob=crudAll.getAllTermsParam("Obecnosc.findTemDoFrekw", hms);
	String obecnoscOut="";
	if(ob!=null && ob.size()>0) {
	for(Obecnosc ol:ob) {
		if(!ol.getWartosc().equals("Sp"))
		obecnoscOut+=ol.getSluchacze().getNazwiskoSluchacz()+" "+ol.getSluchacze().getImieSluchacz()+", ";
	}
	if(obecnoscOut.length()>2)
	return obecnoscOut.substring(0, obecnoscOut.length()-2);
	}
	}
	return "";
}
//-------------------------------------------gettery settery-----------------------------------------------------------------------------------

public List<com.edziennik.sluchacze.zajecia.model.Tematy> getTematyWplutonie() {
	return tematyWplutonie;
}

public void setTematyWplutonie(List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyWplutonie) {
	this.tematyWplutonie = tematyWplutonie;
}


public List<com.edziennik.sluchacze.zajecia.model.Tematy> getTematyPlutonWybrany() {
	return tematyPlutonWybrany;
}

public void setTematyPlutonWybrany(List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyPlutonWybrany) {
	this.tematyPlutonWybrany = tematyPlutonWybrany;
}

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



public Integer getIleTematow() {
	return ileTematow;
}

public serwis.users.nameUser.NameUser getNameUser() {
	return nameUser;
}

public void setNameUser(serwis.users.nameUser.NameUser nameUser) {
	this.nameUser = nameUser;
}


public void setIleTematow(Integer ileTematow) {
	this.ileTematow = ileTematow;
}

public Integer getIdPlutonWybrany() {
	return idPlutonWybrany;
}

public void setIdPlutonWybrany(Integer idPlutonWybrany) {
	this.idPlutonWybrany = idPlutonWybrany;
}



public Logowanie getZalogowanyUser() {
	return zalogowanyUser;
}

public void setZalogowanyUser(Logowanie zalogowanyUser) {
	this.zalogowanyUser = zalogowanyUser;
}

}
