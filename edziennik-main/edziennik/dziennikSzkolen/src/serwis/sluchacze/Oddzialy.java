package serwis.sluchacze;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Kompania;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;

import my.util.MessagePlay;

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
	
	private Integer nrDzienniks;
	private String stopiens;
	private String nazwiskos;
	private String imies;
	private String jednostkas;
	private String usunietys;
	private String stanowiskoSluchaczs=null;
	private Integer kadrowy;
	private String pokoj;
	private String imieSluchacz;
	private String nazwiskoSluchacz;
	private Integer nrDziennikaSluchacz;
	private String stopienSluchacz;
	private String jednostkaKierujacaSluchacz;
	private Integer kadrowySluchacz;
	private boolean edycjaTabeli;
	private String archiwum="NIE";
	private boolean archbool;

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
		String plutonF,kompaniaF,oznaczenieSzkoleniaF,imieSluchaczF,nazwiskoSluchaczF,stopienSluchaczF,jednostkaKierujacaF,kadrowySluchaczF,pokojF;;
			
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
		stp.add("sierż.");
		stp.add("st.sierż.");
		stp.add("sierż.sztab.");
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
				stp.add("sierż.");
				stp.add("st.sierż.");
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
		public String getArchiwum() {
			return archiwum;
		}

		public void setArchiwum(String archiwum) {
			this.archiwum = archiwum;
		}

		public boolean isArchbool() {
			return archbool;
		}

		public void setArchbool(boolean archbool) {
			this.archbool = archbool;
		}

		public String getPokoj() {
			return pokoj;
		}

		public void setPokoj(String pokoj) {
			this.pokoj = pokoj;
		}
	
}
