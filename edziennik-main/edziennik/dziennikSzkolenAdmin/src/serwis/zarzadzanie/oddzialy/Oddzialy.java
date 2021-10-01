package serwis.zarzadzanie.oddzialy;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Kompania;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.userManager.model.UsersStudent;

import my.util.MessagePlay;
import my.util.PasswordValidator;
import serwis.RejestryLogi.RejestryLogi;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Named
@ViewScoped
public class Oddzialy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Oddzialy() {
		// TODO Auto-generated constructor stub
	}
	@EJB
	private CrudAll crudSluchacze;
	@EJB
	private CrudAllLocal logi;
	
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
	
	@SuppressWarnings("unused")
	private List<String>stopnie;
	private List<String> plutonListNazwa;
	private List<String> plutonListSzkolenie;
	private String pluton;
	private List<Sluchacze> sluchaczeSorted;
	private String szkolenie;
	private List<Kompania> kompanieList;
	private String kompania;
	private List<Pluton> plutonyList;
	private Pluton wybranyPluton;
	private List<String> kompanieListNazwa;
	 private boolean edycjaArch;
	 private boolean automatyczneKonto;  
	private Integer nrDzienniks;
	private String stopiens;
	private String nazwiskos;
	private String imies;
	private String jednostkas;
	private String usunietys;
	private String stanowiskoSluchaczs=null;
	private String pokojs;
	private Integer kadrowy;
	private String pokoj;
	private String imieSluchacz;
	private String nazwiskoSluchacz;
	private Integer nrDziennikaSluchacz;
	private String stopienSluchacz;
	private String jednostkaKierujacaSluchacz;
	private Integer kadrowySluchacz;
	private boolean edycjaTabeli;
	private boolean archbool;
	private String archiwum="NIE";
	private String login;
	private String haslo;
	private String myhasHaslo=null;
	private static PasswordValidator passwordValidator;
	private Timestamp dataDzisTimeStamp=new Timestamp(Calendar.getInstance().getTimeInMillis());
	
	@PostConstruct
	public void init() {
    kompanieList=new ArrayList<Kompania>();
	kompanieListNazwa=new ArrayList<String>();
	plutonyList=new ArrayList<Pluton>();
	plutonListNazwa=new ArrayList<String>();
	plutonListSzkolenie=new ArrayList<String>();
	
   	HashMap<String, Object> hmsp=new HashMap<String,Object>();
	hmsp.put("archiwum", "NIE");
	kompanieList=crudSluchacze.getAllTermsParam("Kompania.findAll", hmsp);
		
		for(Kompania kompStr:kompanieList) {
			kompanieListNazwa.add(kompStr.getNazwaKompania());
		}
		
	
}
	
	public void rendExpTab() {
		String ed="";
		if(edycjaTabeli)
			ed="Edycja tabeli właczona";
			else
			ed="Edycja tabeli wyłaczona";
		new MessagePlay(ed.toString(),null,FacesMessage.SEVERITY_INFO);
	}
	
	public void changeKompania() {
		plutonListNazwa.clear();
		plutonListSzkolenie.clear();
		pluton=null;
		szkolenie=null;
		for(Kompania kompStr:kompanieList) {
			if(kompStr.getNazwaKompania().equals(kompania)) {
				plutonyList=kompStr.getPlutons();
				 for(Pluton plutStr:plutonyList) {
					 if(!plutonListNazwa.contains(plutStr.getNazwaPluton()))
					 plutonListNazwa.add(plutStr.getNazwaPluton());
				 } 
			}
		}
		
	}
	
	public void changePluton() {
		plutonListSzkolenie.clear();
		szkolenie=null;
		for(Pluton plutStr:plutonyList) {
			if(plutStr.getNazwaPluton().equals(pluton)) {
				if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
					plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
				}
			
			}
		}	
	}

	public Pluton changeSzkolenie() {
	if(szkolenie!=null)
	{
		for(Pluton plutStr:plutonyList) {
			if(plutStr.getNazwaPluton().equals(pluton) && plutStr.getOznaczenieSzkolenia().equals(szkolenie)) {
				if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
					plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
				}
				return plutStr;
			}
		}
	}
		return null;

	}
	
	public void findSluchacze() {
		wybranyPluton=changeSzkolenie();
		sluchaczeSorted=null;
		HashMap<String,Object> hms=new HashMap<String,Object>();
		String plutonF,kompaniaF,oznaczenieSzkoleniaF,imieSluchaczF,nazwiskoSluchaczF,stopienSluchaczF,jednostkaKierujacaF,kadrowySluchaczF,pokojF;
			
		if(pluton==null)
			plutonF="";
		else
			plutonF=pluton;
		
		if(kompania==null)
			kompaniaF="";
		else
			kompaniaF=kompania;
		
		if(szkolenie==null)
			oznaczenieSzkoleniaF="";
		else
			oznaczenieSzkoleniaF=szkolenie;
		
		if(imieSluchacz==null)
			imieSluchaczF="";
		else
			imieSluchaczF=imieSluchacz;
		
		if(nazwiskoSluchacz==null)
			nazwiskoSluchaczF="";
		else
			nazwiskoSluchaczF=nazwiskoSluchacz;
		
		if(stopienSluchacz==null)
			stopienSluchaczF="";
		else
			stopienSluchaczF=stopienSluchacz;
		
		if(jednostkaKierujacaSluchacz==null)
			jednostkaKierujacaF="";
		else
			jednostkaKierujacaF=jednostkaKierujacaSluchacz;
		
		if(kadrowySluchacz==null)
			kadrowySluchaczF="";
		else
			kadrowySluchaczF=String.valueOf(kadrowySluchacz);
		if(archbool)
			archiwum="TAK";
		else
			archiwum="NIE";
		
		if(pokoj==null)
			pokojF="";
		else
			pokojF=pokoj;
		
		hms.put("nazwaPluton", "%"+plutonF+"%");
		hms.put("nazwaKompania", "%"+kompaniaF+"%");
		hms.put("oznaczenieSzkolenia", "%"+oznaczenieSzkoleniaF+"%");
		hms.put("imieSluchacz", "%"+imieSluchaczF+"%");
		hms.put("nazwiskoSluchacz", "%"+nazwiskoSluchaczF+"%");
		hms.put("stopienSluchacz","%"+stopienSluchaczF+"%");
		hms.put("jednostkaKierujaca", "%"+jednostkaKierujacaF+"%");
		hms.put("idSluchacz", "%"+kadrowySluchaczF+"%");
		hms.put("archiwum", "%"+archiwum+"%");
		hms.put("pokoj", "%"+pokojF+"%");
		sluchaczeSorted=crudSluchacze.getAllTermsParam("Sluchacze.findSluchaczeFinder", hms);
	
	}
		
		
	public void onRowEditSluchacz(RowEditEvent ev) {
			Sluchacze sluchGet=(Sluchacze) ev.getObject();
			Pluton pl=null;
			if(szkolenie!=null) {
				pl=plutonyList.stream().filter(plt->plt.getOznaczenieSzkolenia().equals(szkolenie) && plt.getNazwaPluton().equals(pluton) && plt.getKompania().getNazwaKompania().equals(kompania)).findFirst().orElse(null);
			}
			if(pl!=null) {
				sluchGet.setPluton(pl);
			}
		
			
			if(sluchGet.getUsuniety().equals("TAK")) {
				if(sluchGet.getDataRemove()==null)
				sluchGet.setDataRemove(dataDzisTimeStamp);
				sluchGet.setUserRemove((Integer) session.getAttribute("idUser"));
			}else {
				sluchGet.setDataRemove(null);
				sluchGet.setUserRemove(null);
			}
			crudSluchacze.update(sluchGet);
			RejestryLogi rl=new RejestryLogi();
		
		String opis;
		int idu=(int) session.getAttribute("idUser");
		opis="Uaktualniono dane. Po wpisaniu: "+sluchGet.getStopienSluchacz()+" "+sluchGet.getNazwiskoSluchacz()+" "+sluchGet.getImieSluchacz()+" / nr.dz.: "+sluchGet.getNrDziennik()+" / stopień: "+sluchGet.getStopienSluchacz()+" / jenostka org.:"+sluchGet.getJednostkaKierujaca()+" / usunięty: "+sluchGet.getUsuniety()+"/komp-plut-szkol:"+sluchGet.getPluton().getKompania().getNazwaKompania()+"-"+sluchGet.getPluton().getNazwaPluton()+"-"+sluchGet.getPluton().getOznaczenieSzkolenia()+"/id:"+sluchGet.getIdSluchacz();
		logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,sluchGet.getIdS(), "Osoba"));
		
		rl=null;
		new MessagePlay("Uaktualniono dane słuchacza "+sluchGet.getNazwiskoSluchacz()+" "+sluchGet.getImieSluchacz(),null,FacesMessage.SEVERITY_INFO);
		}
	 
		public void addSluchacz() {
	
			Sluchacze newSluch=new Sluchacze();
			 newSluch.setImieSluchacz(imies);
			 newSluch.setNazwiskoSluchacz(nazwiskos);
			 newSluch.setJednostkaKierujaca(jednostkas);
			 newSluch.setNrDziennik(String.valueOf(nrDzienniks));
			 newSluch.setStopienSluchacz(stopiens);
			 newSluch.setUsuniety(usunietys);
			 newSluch.setIdSluchacz(kadrowy);
			 newSluch.setPokoj(pokojs);
			 newSluch.setObecnoscs(null);
			 newSluch.setOcenas(null);
			 newSluch.setStanowiskoSluchacz(stanowiskoSluchaczs);
		 
				RejestryLogi rl=new RejestryLogi();
				Calendar cal = Calendar.getInstance();  
				Timestamp dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
				String opis;
				int idu=(int) session.getAttribute("idUser");
					
			 if(automatyczneKonto) {
					UsersStudent userAdd;			
					userAdd=new UsersStudent();
					if(login==null || login.trim().length()==0)
						login=String.valueOf(newSluch.getIdSluchacz());
					if(haslo==null || haslo.trim().length()==0)
						haslo="Id"+String.valueOf(newSluch.getIdSluchacz())+"%20";
					if(haslo!=null) {
						
						passwordValidator=new PasswordValidator();
						Boolean res=passwordValidator.validate(haslo);
						if(res) {
							HashMap<String, Object> hms=new HashMap<String,Object>();
							hms.put("loginUsers", login);
							if(crudSluchacze.getAllTermsParam("loginUserIfRepeatStudent", hms).size()==0)
							{ 
								newSluch.setPluton(wybranyPluton);
								 wybranyPluton.addSluchacze(newSluch);
								 crudSluchacze.create(newSluch);
								 sluchaczeSorted.add(newSluch);
								 
							Date zmianaHasla=new Date();
							Calendar c=Calendar.getInstance();
							c.setTime(zmianaHasla);
							c.add(Calendar.DATE, -30);
							zmianaHasla=c.getTime();
							
							userAdd.setImieUsers(newSluch.getImieSluchacz());
							userAdd.setNazwiskoUsers(newSluch.getNazwiskoSluchacz());
							setMyhasHaslo(haslo);
							userAdd.setHasloUsers(myhasHaslo);
							userAdd.setLoginUsers(login);
							userAdd.setZablokowany("NIE");
							userAdd.setSluchacze(newSluch);
							userAdd.setStatusUsers("sluchacz");
							userAdd.setPoziomUprawnien("7");
							userAdd.setZmianaHasla(zmianaHasla);
							userAdd.setKompania(wybranyPluton.getKompania().getNazwaKompania());
							userAdd.setPluton(wybranyPluton.getNazwaPluton());
							userAdd.setSzkolenie(wybranyPluton.getOznaczenieSzkolenia());
							
							crudSluchacze.create(userAdd);
							login=null;
								//sluchacz jako uzytkownik
								opis="Przypisanie uprawnień słuchaczowi. Id: "+newSluch.getIdS()+", "+newSluch.getImieSluchacz()+" "+newSluch.getNazwiskoSluchacz()+", komp./plut./szkol.: "+newSluch.getPluton().getKompania().getNazwaKompania()+"/"+newSluch.getPluton().getNazwaPluton()+"/"+newSluch.getPluton().getOznaczenieSzkolenia();
								logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Nadanie uprawnień słuchaczowi.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,newSluch.getIdS(), "sluchacz"));
								//sluchacz jako sluchacz
								opis="Pierwsza rejestracja słuchacza. Wpisano: "+newSluch.getStopienSluchacz()+" "+newSluch.getNazwiskoSluchacz()+" "+newSluch.getImieSluchacz()+" / nr.dz.: "+newSluch.getNrDziennik()+" / stopień: "+newSluch.getStopienSluchacz()+" / jenostka org.:"+newSluch.getJednostkaKierujaca()+" / usunięty: "+newSluch.getUsuniety()+"/komp-plut-szkol:"+newSluch.getPluton().getKompania().getNazwaKompania()+"-"+newSluch.getPluton().getNazwaPluton()+"-"+newSluch.getPluton().getOznaczenieSzkolenia()+"/id:"+newSluch.getIdSluchacz();
								logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Pierwsza rejestracja",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,newSluch.getIdS(), "Osoba"));
								
								new MessagePlay("Zarejestrowano słuchacza: "+newSluch.getNazwiskoSluchacz()+" "+newSluch.getImieSluchacz(),null,FacesMessage.SEVERITY_INFO);
								new MessagePlay("Utworzono konto słuchacza:  "+newSluch.getNazwiskoSluchacz()+" "+newSluch.getImieSluchacz(),null,FacesMessage.SEVERITY_INFO);
							}	else
							new MessagePlay("Bład zapisu! Login: "+login+" jest juz zajęty!","Zmień login.",FacesMessage.SEVERITY_ERROR);
						}
					}
			 	}else
			 	{	newSluch.setPluton(wybranyPluton);
				    wybranyPluton.addSluchacze(newSluch);
			 		crudSluchacze.create(newSluch);
			 		sluchaczeSorted.add(newSluch);
			 		opis="Pierwsza rejestracja słuchacza. Wpisano: "+newSluch.getStopienSluchacz()+" "+newSluch.getNazwiskoSluchacz()+" "+newSluch.getImieSluchacz()+" / nr.dz.: "+newSluch.getNrDziennik()+" / stopień: "+newSluch.getStopienSluchacz()+" / jenostka org.:"+newSluch.getJednostkaKierujaca()+" / usunięty: "+newSluch.getUsuniety()+"/komp-plut-szkol:"+newSluch.getPluton().getKompania().getNazwaKompania()+"-"+newSluch.getPluton().getNazwaPluton()+"-"+newSluch.getPluton().getOznaczenieSzkolenia()+"/id:"+newSluch.getIdSluchacz();
					logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Pierwsza rejestracja",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,newSluch.getIdS(), "Osoba"));
					
					new MessagePlay("Zarejestrowano słuchacza "+newSluch.getNazwiskoSluchacz()+" "+newSluch.getImieSluchacz(),null,FacesMessage.SEVERITY_INFO);
					
			 	}
			 rl=null;			
		}

		public void closeAddSluchaczDialog() {
			nrDzienniks=null;
			stopiens=null;
			nazwiskos=null;
			imies=null;
			jednostkas=null;
			usunietys=null;
			stanowiskoSluchaczs=null;
			pokojs=null;
			kadrowy=null;
		}

		public void removeSluchacz(ActionEvent e) {
			Sluchacze remSluch=(Sluchacze) e.getComponent().getAttributes().get("removeRow");
			if((remSluch.getObecnoscs()==null && remSluch.getOcenas()==null) || (remSluch.getObecnoscs().size()==0 && remSluch.getOcenas().size()==0)) {
				 sluchaczeSorted.remove(remSluch);
				 //wybranyPluton.removeSluchacze(remSluch);
				 crudSluchacze.delete(remSluch);
				
				 RejestryLogi rl=new RejestryLogi();
					Calendar cal = Calendar.getInstance();  
					Timestamp dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
			
					String opis;
				opis="Usunięto słuchacza : "+remSluch.getStopienSluchacz()+" "+remSluch.getNazwiskoSluchacz()+" "+remSluch.getImieSluchacz()+"/komp-plut-szkol:"+remSluch.getPluton().getKompania().getNazwaKompania()+"-"+remSluch.getPluton().getNazwaPluton()+"-"+remSluch.getPluton().getOznaczenieSzkolenia()+"/id:"+remSluch.getIdSluchacz();
				int idu=(int) session.getAttribute("idUser");
				logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Usunięcie",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,remSluch.getIdS(), "Osoba"));
				
				rl=null;
			new MessagePlay("Usunięto słuchacza: "+remSluch.getNazwiskoSluchacz()+" "+remSluch.getImieSluchacz(),null,FacesMessage.SEVERITY_INFO);
			}
			else {
				new MessagePlay("Słuchacz: "+remSluch.getNazwiskoSluchacz()+" "+remSluch.getImieSluchacz()+" ma oceny i/lub obecności. Nie mogę usunać!",null,FacesMessage.SEVERITY_ERROR);
			}
			remSluch=null;
		}
		
		public void dodajKonto() {
			new MessagePlay("Mozliwość załozenia konta!","Domyślny login: kadrowy, hasło: Idkadrowy%20",FacesMessage.SEVERITY_INFO);
		}
//------------------------------------------------------------getters setters----------------------------------------------------------------
	public List<String> getPlutonListNazwa() {
		return plutonListNazwa;
	}
	public void setPlutonListNazwa(List<String> plutonListNazwa) {
		this.plutonListNazwa = plutonListNazwa;
	}
	public List<String> getPlutonListSzkolenie() {
		return plutonListSzkolenie;
	}
	public void setPlutonListSzkolenie(List<String> plutonListSzkolenie) {
		this.plutonListSzkolenie = plutonListSzkolenie;
	}
	public String getPluton() {
		return pluton;
	}
	public void setPluton(String pluton) {
		this.pluton = pluton;
	}
	public List<Sluchacze> getSluchaczeSorted() {
		return sluchaczeSorted;
	}
	public void setSluchaczeSorted(List<Sluchacze> sluchaczeSorted) {
		this.sluchaczeSorted = sluchaczeSorted;
	}
	public String getSzkolenie() {
		return szkolenie;
	}
	public void setSzkolenie(String szkolenie) {
		this.szkolenie = szkolenie;
	}
	public List<Kompania> getKompanieList() {
		return kompanieList;
	}
	public void setKompanieList(List<Kompania> kompanieList) {
		this.kompanieList = kompanieList;
	}

	public String getKompania() {
		return kompania;
	}
	public void setKompania(String kompania) {
		this.kompania = kompania;
	}
	public List<Pluton> getPlutonyList() {
		return plutonyList;
	}
	public void setPlutonyList(List<Pluton> plutonyList) {
		this.plutonyList = plutonyList;
	}

	public List<String> getStopnie() {
		
		List<String> stp=new ArrayList<String>();
		stp.add("post.");
		stp.add("st.post.");
		stp.add("sier�.");
		stp.add("st.sierz.");
		stp.add("sierz.sztab.");
		stp.add("mł.asp.");
		stp.add("asp.");
		stp.add("st.asp.");
		stp.add("asp.sztab.");
		stp.add("podkom.");
		stp.add("kom.");
		stp.add("nadkom.");
		stp.add("podinsp.");
		stp.add("mł.insp.");
		stp.add("insp.");
				stp.add("st.szer.");
				stp.add("kpr.");
				stp.add("st.kpr.");
				stp.add("plut.");
				stp.add("sierz.");
				stp.add("st.sierz.");
				stp.add("mł.chor.");
				stp.add("chor.");
				stp.add("st.chor.");
				stp.add("ppor.");
				stp.add("por.");
				stp.add("kpt.");
				stp.add("mjr.");
				stp.add("ppłk.");
				stp.add("płk.");
					stp.add("ksc");
		return stp;
	}

	public void setStopnie(List<String> stopnie) {
		this.stopnie = stopnie;
	}

	public List<String> getKompanieListNazwa() {
		return kompanieListNazwa;
	}


	public void setKompanieListNazwa(List<String> kompanieListNazwa) {
		this.kompanieListNazwa = kompanieListNazwa;
	}


	public Integer getNrDzienniks() {
		return nrDzienniks;
	}


	public void setNrDzienniks(Integer nrDzienniks) {
		this.nrDzienniks = nrDzienniks;
	}


	public String getStopiens() {
		return stopiens;
	}


	public void setStopiens(String stopiens) {
		this.stopiens = stopiens;
	}


	public String getNazwiskos() {
		return nazwiskos;
	}


	public void setNazwiskos(String nazwiskos) {
		this.nazwiskos = nazwiskos;
	}


	public String getImies() {
		return imies;
	}


	public void setImies(String imies) {
		this.imies = imies;
	}


	public String getJednostkas() {
		return jednostkas;
	}


	public void setJednostkas(String jednostkas) {
		this.jednostkas = jednostkas;
	}


	public String getUsunietys() {
		return usunietys;
	}


	public void setUsunietys(String usunietys) {
		this.usunietys = usunietys;
	}


	public String getStanowiskoSluchaczs() {
		return stanowiskoSluchaczs;
	}


	public void setStanowiskoSluchaczs(String stanowiskoSluchaczs) {
		this.stanowiskoSluchaczs = stanowiskoSluchaczs;
	}
	 public Pluton getWybranyPluton() {
			return wybranyPluton;
		}

		public void setWybranyPluton(Pluton wybranyPluton) {
			this.wybranyPluton = wybranyPluton;
		}


		public String getImieSluchacz() {
			return imieSluchacz;
		}


		public void setImieSluchacz(String imieSluchacz) {
			this.imieSluchacz = imieSluchacz;
		}


		public String getNazwiskoSluchacz() {
			return nazwiskoSluchacz;
		}


		public void setNazwiskoSluchacz(String nazwiskoSluchacz) {
			this.nazwiskoSluchacz = nazwiskoSluchacz;
		}


		public Integer getNrDziennikaSluchacz() {
			return nrDziennikaSluchacz;
		}


		public void setNrDziennikaSluchacz(Integer nrDziennikaSluchacz) {
			this.nrDziennikaSluchacz = nrDziennikaSluchacz;
		}


		public String getStopienSluchacz() {
			return stopienSluchacz;
		}


		public void setStopienSluchacz(String stopienSluchacz) {
			this.stopienSluchacz = stopienSluchacz;
		}


		public String getJednostkaKierujacaSluchacz() {
			return jednostkaKierujacaSluchacz;
		}


		public void setJednostkaKierujacaSluchacz(String jednostkaKierujacaSluchacz) {
			this.jednostkaKierujacaSluchacz = jednostkaKierujacaSluchacz;
		}


		public boolean isEdycjaArch() {
			return edycjaArch;
		}


		public void setEdycjaArch(boolean edycjaArch) {
			this.edycjaArch = edycjaArch;
		}


		public Integer getKadrowy() {
			return kadrowy;
		}


		public void setKadrowy(Integer kadrowy) {
			this.kadrowy = kadrowy;
		}


		public Integer getKadrowySluchacz() {
			return kadrowySluchacz;
		}


		public void setKadrowySluchacz(Integer kadrowySluchacz) {
			this.kadrowySluchacz = kadrowySluchacz;
		}


		public boolean isEdycjaTabeli() {
			return edycjaTabeli;
		}


		public void setEdycjaTabeli(boolean edycjaTabeli) {
			this.edycjaTabeli = edycjaTabeli;
		}

		public Timestamp getDataDzisTimeStamp() {
			return dataDzisTimeStamp;
		}

		public void setDataDzisTimeStamp(Timestamp dataDzisTimeStamp) {
			this.dataDzisTimeStamp = dataDzisTimeStamp;
		}

		public boolean isArchbool() {
			return archbool;
		}

		public void setArchbool(boolean archbool) {
			this.archbool = archbool;
		}

		public String getArchiwum() {
			return archiwum;
		}

		public void setArchiwum(String archiwum) {
			this.archiwum = archiwum;
		}

		public String getPokoj() {
			return pokoj;
		}

		public void setPokoj(String pokoj) {
			this.pokoj = pokoj;
		}

		public String getPokojs() {
			return pokojs;
		}

		public void setPokojs(String pokojs) {
			this.pokojs = pokojs;
		}

		public boolean isAutomatyczneKonto() {
			return automatyczneKonto;
		}

		public void setAutomatyczneKonto(boolean automatyczneKonto) {
			this.automatyczneKonto = automatyczneKonto;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getHaslo() {
			return haslo;
		}

		public void setHaslo(String haslo) {
			this.haslo = haslo;
		}
		public String getMyhasHaslo() {
			return myhasHaslo;
		}

		public void setMyhasHaslo(String haslo) {	
				if(haslo!=null) {
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				md.update(haslo.getBytes());
				byte[] digest=md.digest();
				this.myhasHaslo=DatatypeConverter.printHexBinary(digest);
				
			}
			
		}
	
}
