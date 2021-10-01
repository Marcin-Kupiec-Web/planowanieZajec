/**
 * 
 */
package serwis.frekwencja;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Obecnosc;
import com.edziennik.sluchacze.zajecia.model.ObecnoscPoprawa;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.edziennik.zajecia.frekwencja.service.FrekwencjaLocal;

import my.util.SluchaczCompare;
import serwis.RejestryLogi.RejestryLogi;
import serwis.logowanie.Logowanie;


@Named
@ViewScoped
public class Frekwencja implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private FrekwencjaLocal obecnosciWdniu;
	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private serwis.users.nameUser.NameUser nameUser;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;
	@Inject
	private Logowanie zalogowanyUser;
	
	private Integer idPlutonWybrany;
	private List<Pluton> plutonWybrany;
	private List<Sluchacze> sluchaczeSorted;
	private List<Obecnosc> obDzienList;
	private Date dataFormGlowny;
	private Timestamp dataFormGlownyTimeStamp;
	private String wyborObecnosc;
	private Map<String,String> wyborObecnoscList;
	private List<Sluchacze> selectedUsers;
	private boolean laczRozlacz;
	private String checkObecnoscTh;
	private Date dataDzis;
	private boolean render=false;
	private boolean editFrek=false;
	private int idUsers;
	private int andHour=10;
	@PostConstruct 
	    public void init() {
		wyborPlutonu.prepareChangeSzkolenie();
	  	idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
		checkObecnoscTh="0";
		 wyborObecnoscList=new LinkedHashMap<String,String>();
		 wyborObecnoscList.put("", "");
		 wyborObecnoscList.put("N", "N");
		 wyborObecnoscList.put("Sp", "Sp");
		 if(zalogowanyUser.getPoziomUprawnien().equals("1") || zalogowanyUser.getPoziomUprawnien().equals("2")) {
		 wyborObecnoscList.put("C", "C");
		 wyborObecnoscList.put("L", "L");
		 wyborObecnoscList.put("S", "S");
		 wyborObecnoscList.put("Sł", "Sł");
		 wyborObecnoscList.put("U", "U");
		 wyborObecnoscList.put("Wo", "Wo");
		 wyborObecnoscList.put("Wzn", "Wzn");
		 wyborObecnoscList.put("Wz", "Wz");
		 wyborObecnoscList.put("Wps", "Wps");
		 wyborObecnoscList.put("Zs", "Zs");
		 }
	
		 dataFormGlowny=wyborPlutonu.getDataFormGlowny();
		 idUsers=zalogowanyUser.getIdUsers();
		
	    	Calendar cal = Calendar.getInstance();  
	    	dataDzis=cal.getTime();
	    	
	    	cal.setTime(dataFormGlowny);
	    	dataFormGlownyTimeStamp=new Timestamp(cal.getTimeInMillis());
	    	
	    	HashMap<String, Object> hms=new HashMap<String,Object>();
			hms.put("idPluton", idPlutonWybrany);
			plutonWybrany=crudAll.getAllTermsParam("Pluton.findSluchaczePoIdPluton", hms);
	    	
				if(plutonWybrany!=null && plutonWybrany.size()>0) {
					sluchaczeSorted=plutonWybrany.get(0).getSluchaczes().stream().distinct().collect(Collectors.toList());
					Collections.sort(sluchaczeSorted,new SluchaczCompare());
				}
	    	
			obDzienList=obecnosciWdniu.getObecnoscInDay(idPlutonWybrany,dataFormGlowny);
			
				
				if(sluchaczeSorted!=null && sluchaczeSorted.size()>0) {
					
				for(Sluchacze sl:sluchaczeSorted) {
					List <Obecnosc> obSet=new ArrayList<Obecnosc>();
					sl.setObecnoscs(null);
					
					for(Obecnosc obDzien:obDzienList) {
						if(sl.getIdS()==obDzien.getSluchacze().getIdS()) {
							obDzien.setImieNazwiskoUser(nameUser.user(obDzien.getIdUsers()));
							obSet.add(obDzien);
						}
					}
					sl.setObecnoscs(obSet);
					obSet=null;
				}
				render=true;
	} else
		render = false;
		
	 }

	public void changeHoursPlus(){
		if(andHour<14)
		andHour++;
	}
	public void changeHoursMinus(){
		if(andHour>8)
		andHour--;
	}
	//=============================================================uaktualnia obecnosc==================================================================
	public void onObecnoscUpdate(ValueChangeEvent e) {
 if(e.getComponent().getAttributes().get("godzina")!=null && e.getNewValue().equals("2")) {
					Object godzinaDoUsuniecia=e.getComponent().getAttributes().get("godzina");
					Integer godzinaDoUsunieciaInt=Integer.valueOf(godzinaDoUsuniecia.toString());
				
					for(Obecnosc obR:obDzienList) {
							if(obR.getGodzinaLekcyjna()==godzinaDoUsunieciaInt && obR.getWartosc()!=null && !obR.getUsunieta().equals("TAK")) {
							
							 if(obR.getIdUsers()==idUsers) {
								obR.setUsunieta("TAK");
								obR.setDataUsunieta(dataDzis);
								obR.setIdUserUsunieta(idUsers);
								obR.setImieNazwiskoUserUsunal(nameUser.user(idUsers));
								obecnosciWdniu.update(obR); 
								
								RejestryLogi rl=new RejestryLogi();
								String opis="Usunieto frekwencję. Wpis: "+obR.getWartosc()+". Sluchacz: "+obR.getSluchacze().getImieSluchacz()+" "+obR.getSluchacze().getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.get(0).getKompania().getNazwaKompania()+"/"+plutonWybrany.get(0).getNazwaPluton()+"/"+plutonWybrany.get(0).getOznaczenieSzkolenia()+", dzien: "+obR.getData()+", godz.lekc.: "+obR.getGodzinaLekcyjna();
								crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "usuniecie","e-dziennik",zalogowanyUser.getSessionLogedKlient(),idUsers,nameUser.user(zalogowanyUser.getIdUsers()), obR.getIdObecnosc(),obR.getWartosc(), "Frekwencja",opis,obR.getSluchacze().getIdS()));
								rl=null;
								
							 }
							 else {
								 String summ="Nie masz prawa do usunięcia: "+obR.getSluchacze().getNazwiskoSluchacz()+" "+obR.getSluchacze().getImieSluchacz()+", godzina: "+obR.getGodzinaLekcyjna()+" / "+godzinaDoUsunieciaInt;
								
								 addMessage(summ,null,FacesMessage.SEVERITY_ERROR);
							 }
						}
					}
	
	}else {
		if(e.getComponent().getAttributes().get("action")!=null) {
				Obecnosc obUp=(Obecnosc) e.getComponent().getAttributes().get("action");
						
				if(e.getNewValue()!=null ) {					
							if(e.getNewValue().toString().trim().length()>0 ) {
								
								ObecnoscPoprawa obUpPopr=new ObecnoscPoprawa();
								obUpPopr.setIdObecnosc(obUp.getIdObecnosc());
								obUpPopr.setDataObPoprawa(obUp.getDataWpisu());
								obUpPopr.setKtoWstawil(obUp.getIdUsers());
								obUpPopr.setWartosc(obUp.getWartosc());
								obUpPopr.setUsunieta(obUp.getUsunieta());
								
								obecnosciWdniu.create(obUpPopr);
								
								obUp.setWartosc((String) e.getNewValue());
								obUp.setIdUsers(idUsers);
								obUp.setDataWpisu(dataFormGlownyTimeStamp);
								obUp.setUsunieta("NIE");
								obUp.setImieNazwiskoUser(nameUser.user(idUsers));
								obUp.setWartoscPoprawa(obUpPopr.getIdObecnoscPoprawa());
								obecnosciWdniu.update(obUp); 
								
								RejestryLogi rl=new RejestryLogi();
								String opis="Uaktualniono frekwencje. Wpis: "+obUp.getWartosc()+". Sluchacz: "+obUp.getSluchacze().getImieSluchacz()+" "+obUp.getSluchacze().getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.get(0).getKompania().getNazwaKompania()+"/"+plutonWybrany.get(0).getNazwaPluton()+"/"+plutonWybrany.get(0).getOznaczenieSzkolenia()+", dzien: "+obUp.getData()+", godz.lekc.:"+obUp.getGodzinaLekcyjna();
								crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "poprawa","e-dziennik",zalogowanyUser.getSessionLogedKlient(),idUsers,nameUser.user(zalogowanyUser.getIdUsers()), obUp.getIdObecnosc(),obUp.getWartosc(), "Frekwencja",opis,obUp.getSluchacze().getIdS()));
								rl=null;
							}
						}
						 else {if(!obUp.getUsunieta().equals("TAK")){ 
							 if(obUp.getIdUsers()==idUsers) {
								obUp.setUsunieta("TAK");
								obUp.setDataUsunieta(dataDzis);
								obUp.setIdUserUsunieta(idUsers);
								obUp.setImieNazwiskoUserUsunal(nameUser.user(idUsers));
								obecnosciWdniu.update(obUp); 
								
								RejestryLogi rl=new RejestryLogi();
								String opis="Usunieto frekwencje. Wpis: "+obUp.getWartosc()+". Sluchacz: "+obUp.getSluchacze().getImieSluchacz()+" "+obUp.getSluchacze().getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.get(0).getKompania().getNazwaKompania()+"/"+plutonWybrany.get(0).getNazwaPluton()+"/"+plutonWybrany.get(0).getOznaczenieSzkolenia()+", dzien: "+obUp.getData()+", godz.lekc.: "+obUp.getGodzinaLekcyjna();
								crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "usuniecie","e-dziennik",zalogowanyUser.getSessionLogedKlient(),idUsers,nameUser.user(zalogowanyUser.getIdUsers()), obUp.getIdObecnosc(),obUp.getWartosc(), "Frekwencja",opis,obUp.getSluchacze().getIdS()));
								rl=null;
							 }else {
								 String summ="Nie masz prawa do usunięcia: "+obUp.getSluchacze().getNazwiskoSluchacz()+" "+obUp.getSluchacze().getImieSluchacz()+", godzina: "+obUp.getGodzinaLekcyjna();
								
								 addMessage(summ,null,FacesMessage.SEVERITY_ERROR);
									
							 }
						}
					}
		}
	}
	}
	//============================================================zapisuje nowe obecności==============================================================================
	public void onObecnoscSave(ValueChangeEvent e) {
				if(e.getNewValue()!=null && e.getNewValue().toString().trim().length()>0 && e.getOldValue()==null) {
				Object ob= e.getComponent().getAttributes().get("obecnoscGodzL");
				Integer godzL=Integer.valueOf(ob.toString());
				Sluchacze slUp=(Sluchacze) e.getComponent().getAttributes().get("sluchaczeObject");
			if(slUp.getObecnoscs().stream().filter(obf->obf.getGodzinaLekcyjna()==godzL && obf.getData()==dataFormGlownyTimeStamp).findFirst().orElse(null)==null) {	
				Obecnosc obUp=new Obecnosc(); 
				obUp.setWartosc((String) e.getNewValue());
				obUp.setData(dataFormGlownyTimeStamp);
				obUp.setSluchacze(slUp);
				obUp.setGodzinaLekcyjna(godzL);
				obUp.setIdUsers(idUsers);
				obUp.setImieNazwiskoUser(nameUser.user(idUsers));
				obUp.setUsunieta("NIE");
				obUp.setDataWpisu(dataFormGlownyTimeStamp);
				obUp.setIdPluton(Integer.valueOf(idPlutonWybrany));
				obUp.setWartoscPoprawa(obUp.getIdObecnosc());
				slUp.addObecnosc(obUp);
				
				obecnosciWdniu.create(obUp);
				
				RejestryLogi rl=new RejestryLogi();
				String opis="Zapisano frekwencje. Wpis: "+obUp.getWartosc()+". Sluchacz: "+obUp.getSluchacze().getImieSluchacz()+" "+obUp.getSluchacze().getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.get(0).getKompania().getNazwaKompania()+"/"+plutonWybrany.get(0).getNazwaPluton()+"/"+plutonWybrany.get(0).getOznaczenieSzkolenia()+", dzien: "+obUp.getData()+", godz.lekc.: "+obUp.getGodzinaLekcyjna();
				crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "zapis","e-dziennik",zalogowanyUser.getSessionLogedKlient(),idUsers,nameUser.user(zalogowanyUser.getIdUsers()), obUp.getIdObecnosc(),obUp.getWartosc(), "Frekwencja",opis,obUp.getSluchacze().getIdS()));
				rl=null;
			}
			}
    }

	//===========================================================zapis frekwencji======================================================================================
	public void zapiszObecnosc() throws IOException {

		FacesContext.getCurrentInstance().getExternalContext().redirect("frekwencja.xhtml?i=2&subPage=frekwencja");
	}
	
	public void resetObecnosc(){
		checkObecnoscTh="0";
	}
	

//-------------------------------------------------------------------------------------------------------------------------------	
	
	public void addMessageChangeLacz() {
		boolean summ=this.laczRozlacz;
		String summStr;
		if(summ)
			summStr="Funkcja wstawiajaca wybrana wartość do wszystkich zaznaczonych pól w danym wierszu - WYŁĄCZONA";
		else
			summStr="Funkcja wstawiajaca wybranea wartość do wszystkich zaznaczonych pól w danym wierszu - WŁĄCZONA";
		addMessage(summStr,null,FacesMessage.SEVERITY_INFO);
	}
	
	public void addMessageSelectCol() {
		String summ;
		if(checkObecnoscTh.equals("1")) {
			summ="Przygotowano do zapisania. Kliknij: ZAPISZ";
			addMessage(summ,null,FacesMessage.SEVERITY_INFO);
		}
		else if(checkObecnoscTh.equals("2")) {
			summ="Przygotowano do usunięcia. Kliknij: USUĹ�";
			addMessage(summ,null,FacesMessage.SEVERITY_WARN);
		}
		
	}
	
	public void frekwencjaStatystyka(ActionEvent actionEvent) {
	    addMessage("Data updated",null,FacesMessage.SEVERITY_INFO);
	}
	 
	public void frekwnecjaWyszukiwarka(ActionEvent actionEvent) {
	    addMessage("Data deleted",null,FacesMessage.SEVERITY_INFO);
	}
	
	public void addMessage(String summary,String detail, Severity ms) {
		FacesMessage message=new FacesMessage(ms,summary,detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

//-------------------------------------------------------------GETTERY SETTERY-----------------------------------------------------------------
	
	public Date getDataFormGlowny() {
		setDataFormGlowny(wyborPlutonu.getDataFormGlowny());
		return dataFormGlowny;
	}

	public void setDataFormGlowny(Date dataFormGlowny) {
		this.dataFormGlowny = dataFormGlowny;
	}

	public List<Sluchacze> getSluchaczeSorted() {
		return sluchaczeSorted;
	}

	public void setSluchaczeSorted(List<Sluchacze> sluchaczeSorted) {
		this.sluchaczeSorted = sluchaczeSorted;
	}

	public Integer getIdPlutonWybrany() {
	if(wyborPlutonu.getIdPlutonWybrany()!=null) {
		setIdPlutonWybrany(wyborPlutonu.getIdPlutonWybrany());
	}
	return idPlutonWybrany;
}

	public void setIdPlutonWybrany(Integer integer) {
	this.idPlutonWybrany = integer;
}

	public serwis.wyborPlutonu.wyborPlutonu getWyborPlutonu() {
	return wyborPlutonu;
}

	public void setWyborPlutonu(serwis.wyborPlutonu.wyborPlutonu wyborPlutonu) {
	this.wyborPlutonu = wyborPlutonu;
}

	public List<Obecnosc> getObDzienList() {
	return obDzienList;
}

	public void setObDzienList(List<Obecnosc> obDzienList) {
	this.obDzienList = obDzienList;
}

	public void frekwencjaStart() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("frekwencja.xhtml?i=2&subPage=frekwencja");
	}
 

public List<Sluchacze> getSelectedUsers() {
	return selectedUsers;
}
public void setSelectedUsers(List<Sluchacze> selectedUsers) {
	this.selectedUsers = selectedUsers;
}
public Map<String, String> getWyborObecnoscList() {
	return wyborObecnoscList;
}


public void setWyborObecnoscList(Map<String, String> wyborObecnoscList) {
	this.wyborObecnoscList = wyborObecnoscList;
}


public String getWyborObecnosc() {
	return wyborObecnosc;
}


public void setWyborObecnosc(String wyborObecnosc) {
	this.wyborObecnosc = wyborObecnosc;
}
public int getIdUsers() {
	return idUsers;
}


public void setIdUsers(int idUsers) {
	this.idUsers = idUsers;
}

public boolean isLaczRozlacz() {
	return laczRozlacz;
}


public void setLaczRozlacz(boolean laczRozlacz) {
	this.laczRozlacz = laczRozlacz;
}

public String getCheckObecnoscTh() {
	return checkObecnoscTh;
}


public void setCheckObecnoscTh(String checkObecnoscTh) {
	this.checkObecnoscTh = checkObecnoscTh;
}
public Timestamp getDataFormGlownyTimeStamp() {
	return dataFormGlownyTimeStamp;
}


public void setDataFormGlownyTimeStamp(Timestamp dataFormGlownyTimeStamp) {
	this.dataFormGlownyTimeStamp = dataFormGlownyTimeStamp;
}

public Date getDataDzis() {
	return dataDzis;
}


public void setDataDzis(Date dataDzis) {
	this.dataDzis = dataDzis;
}



public boolean isRender() {
	return render;
}


public void setRender(boolean render) {
	this.render = render;
}
public serwis.users.nameUser.NameUser getNameUser() {
	return nameUser;
}

public void setNameUser(serwis.users.nameUser.NameUser nameUser) {
	this.nameUser = nameUser;
}

public Logowanie getZalogowanyUser() {
	return zalogowanyUser;
}

public void setZalogowanyUser(Logowanie zalogowanyUser) {
	this.zalogowanyUser = zalogowanyUser;
}

public boolean isEditFrek() {
	return editFrek;
}

public void setEditFrek(boolean editFrek) {
	this.editFrek = editFrek;
}

public int getAndHour() {
	return andHour;
}

public void setAndHour(int andHour) {
	this.andHour = andHour;
}
}
