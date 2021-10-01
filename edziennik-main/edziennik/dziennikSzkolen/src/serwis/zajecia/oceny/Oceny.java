package serwis.zajecia.oceny;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Ocena;
import com.edziennik.sluchacze.zajecia.model.OcenyNaglowek;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Przedmiot;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.userManager.model.UsersStudent;

import my.util.MessagePlay;
import my.util.OcenyCompareDataWpisu;
import my.util.SluchaczCompare;
import serwis.RejestryLogi.RejestryLogi;
import serwis.logowanie.Logowanie;
import serwis.users.nameUser.NameUser;

@Named
@ViewScoped
public class Oceny implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAllLocal crudAll;
	@EJB
	private CrudAllLocal logi;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;
	@Inject
	private NameUser nameUser;
	@Inject
	private Logowanie zalogowanyUser;
	
	private List<Sluchacze> sluchaczeSorted;
	private Integer idPlutonWybrany;
	private List<Pluton> plutonWybranyList;
	private Pluton plutonWybrany;
	private List<Ocena> oceny;
	private Ocena poprawaOceny;
	private Map<String,String> jm;
	private Map<String,String> js;
	private List<String> zadanie;
	private String zadanieWybrane;
	private List<OcenyNaglowek> naglowekList;
	private List<OcenyNaglowek> naglowekListOkresowe;
	private String jmWybrana;
	private String jsWybrana;
	private List<Przedmiot> przedmiotList;
	private Przedmiot przedmiot;
	private String naglowek;
	private String komentarzKolumna;
	private Timestamp dataDzisTimeStamp;
	private String kolorKolumny="";
	private boolean doSredniej=true;
	private OcenyNaglowek wybranaKolumnaUpdate;
	private boolean brakKoloru=true;
	private String nowaOcena;
	private List<Ocena> ocenyEditList;
	private String nowaOcenaKomentarz;
	private Date nowaOcenaData=new Date();
	private String nowaOcenaKolor="e8e8e8";
	List<Przedmiot> lp;
	private Sluchacze sluchaczWybranyOceny;
	private OcenyNaglowek wybranaKolumnaOceny;
	private List<String> ocenyList;
	private boolean checkColumnOkres=false;
	private List<OcenyNaglowek> naglowekListSluch;
	private UsersStudent ussluch=null;
	private List<Integer> sluchaczes;
	private String srednia1;
	private String srednia2;
	private boolean editTem=false;
	private int wybranySluchacz;
	
	public Oceny() {
		// TODO Auto-generated constructor stub
	}
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
	
@PostConstruct
public void init() {
	wyborPlutonu.prepareChangeSzkolenie();
	naglowekList=new LinkedList<OcenyNaglowek>();
	setNaglowekListOkresowe(new LinkedList<OcenyNaglowek>());
	idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
 	Calendar cal = Calendar.getInstance();  
 	dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
	jm=new TreeMap<String,String>();
	js=new TreeMap<String,String>();
	lp=new LinkedList<Przedmiot>();
	zadanie=new LinkedList<String>();	
	zadanie.add("Ou");
	zadanie.add("Op");
	zadanie.add("C");
	zadanie.add("A");
	zadanie.add("K");
	zadanie.add("Sp");
	zadanie.add("T");
	zadanie.add("Zo");
	zadanie.add("Zu");
	
	
	ocenyList=new LinkedList<String>();
	 for(int i=1;i<7;i++) {
		 ocenyList.add(String.valueOf(i));
	 }
	 ocenyList.add("zal");
	 ocenyList.add("nzal");
	 
//szukam pluton	
	HashMap<String, Object> hmsp=new HashMap<String,Object>();
	hmsp.put("idPluton", idPlutonWybrany);
	plutonWybranyList=crudAll.getAllTermsParam("Pluton.findSluchaczePoIdPluton", hmsp);
	 
	if(plutonWybranyList!=null && plutonWybranyList.size()==1) {
			plutonWybrany=plutonWybranyList.get(0);
		} else {
			session.setAttribute("jm", null);
			session.setAttribute("js", null);
		}
	
	jmWybrana=(String) session.getAttribute("jm");
	jsWybrana=(String) session.getAttribute("js");
	
	if(plutonWybrany!=null) {
			sluchaczeSorted=plutonWybrany.getSluchaczes().stream().distinct().collect(Collectors.toList());
			Collections.sort(sluchaczeSorted,new SluchaczCompare());
			//szukam przedmiotu
			hmsp=new HashMap<String,Object>();
			hmsp.put("jm", jmWybrana);
			hmsp.put("js", jsWybrana);
			hmsp.put("idStrukturaKursu", plutonWybrany.getIdKurs().getIdStrukturaKursu());
			przedmiotList=crudAll.getAllTermsParam("Przedmiot.findPoJMJSStrukt", hmsp);	
			
			 lp=plutonWybrany.getIdKurs().getPrzedmiots().stream().filter(fp->fp.getStrukturaKursu().getIdStrukturaKursu()==plutonWybrany.getIdKurs().getIdStrukturaKursu()).collect(Collectors.toList());
	
			 for(Przedmiot lpa:lp) {
					jm.put(lpa.getJm(), lpa.getJm());
					if(jmWybrana!=null && lpa.getJm().equals(jmWybrana) && lpa.getJs()!=null)
					js.put(lpa.getJs(), lpa.getJs());
				}
			
			if(przedmiotList!=null && przedmiotList.size()==1)
				{
					przedmiot=przedmiotList.get(0);
					
					//szukam naglowkow ocen
					hmsp=new HashMap<String,Object>();
					hmsp.put("idPrzedmiot", przedmiot.getIdPrzedmiot());
					hmsp.put("idPluton", plutonWybrany.getIdPluton());
					hmsp.put("usunieta", false);
					naglowekList=crudAll.getAllTermsParam("OcenyNaglowek.findPlutonPrzedmiot", hmsp);
				
					for(int i=0;i<3;i++) {
						
						OcenyNaglowek newKolumna=new OcenyNaglowek();
						String naglowekOkres;
						if(i==0)
							naglowekOkres="1";
						else if(i==1)
							naglowekOkres="2";
						else 
							naglowekOkres="3";
						OcenyNaglowek ocfind=naglowekList.stream().filter(ocnf->ocnf.getNaglowek().equals(naglowekOkres)&& ocnf.isOkresowa()).findFirst().orElse(null);
						if(ocfind!=null) {
							naglowekListOkresowe.add(ocfind);
							naglowekList.remove(ocfind);
						}
						else {
						newKolumna.setNaglowek(naglowekOkres);
						newKolumna.setPluton(plutonWybrany);
						newKolumna.setOcenas(null);
						newKolumna.setPrzedmiot(przedmiot);
						newKolumna.setKomentarz("Ocena okresowa - termin: "+naglowekOkres+". ");
						newKolumna.setKiedyWpisal(dataDzisTimeStamp);
						newKolumna.setKolor(kolorKolumny);
						newKolumna.setDoSredniej(doSredniej);
						newKolumna.setOkresowa(true);
						newKolumna.setZadanie("Z");
						newKolumna.setIdWpisal(zalogowanyUser.getIdUsers());
						newKolumna.setKtoWpisal(nameUser.user(zalogowanyUser.getIdUsers()));
						naglowekListOkresowe.add(newKolumna);
						}
					}
				}
		}
	
	if(zalogowanyUser.getPoziomUprawnien().equals("7")) {
		HashMap<String, Object> hmsl=new HashMap<String,Object>();
		hmsl.put("idUsers", zalogowanyUser.getIdUsers());
        try {
        	ussluch=(UsersStudent) crudAll.getAllTermsParam("findUserPoIdus", hmsl).get(0);
        	Collections.sort(ussluch.getSluchacze().getOcenas(),new OcenyCompareDataWpisu());
        	naglowekListSluch=ussluch.getSluchacze().getPluton().getOcenyNagloweks().stream().filter(ocnl->!ocnl.isUsunieta()).collect(Collectors.toList());
        
        } catch(IndexOutOfBoundsException e) {
        }
		
	}
  }

	public void listJM() {
		session.setAttribute("js",null);
		jsWybrana=null;
		js.clear();
		if(jmWybrana!=null) {
			session.setAttribute("jm",jmWybrana);
		lp=plutonWybrany.getIdKurs().getPrzedmiots().stream().filter(fp->fp.getStrukturaKursu().getIdStrukturaKursu()==plutonWybrany.getIdKurs().getIdStrukturaKursu()).collect(Collectors.toList());;
			
			for(Przedmiot lpa:lp) {
				if(lpa.getJm().equals(jmWybrana) && lpa.getJs()!=null)
				js.put(lpa.getJs(), lpa.getJs());
			}
		} else 
			session.setAttribute("jm",null);
			/*
			try {
				FacesContext ctx = FacesContext.getCurrentInstance();
		 		 HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
				ctx.getExternalContext().redirect(request.getRequestURI());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
	}
	
	public void setExpandedRowElement(int idS) {
		
		if(wybranySluchacz!=idS) {
			wybranySluchacz=idS;
		}
		else {
		wybranySluchacz=0;
		}
	}
	
	public void listJS() {
		if(jsWybrana!=null)
			session.setAttribute("js",jsWybrana);
		init();
		/*
		try {
			FacesContext ctx = FacesContext.getCurrentInstance();
	 		 HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
			ctx.getExternalContext().redirect(request.getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	public void ocenyStart() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("oceny.xhtml?i=2&subPage=oceny");
	}
	
	public void addKolumna() {
		OcenyNaglowek newKolumna=new OcenyNaglowek();
		newKolumna.setNaglowek(naglowek);
		newKolumna.setPluton(plutonWybrany);
		newKolumna.setOcenas(null);
		newKolumna.setPrzedmiot(przedmiot);
		newKolumna.setKomentarz(komentarzKolumna);
		newKolumna.setKiedyWpisal(dataDzisTimeStamp);
		newKolumna.setKolor(kolorKolumny);
		newKolumna.setDoSredniej(doSredniej);
		newKolumna.setOkresowa(false);
		newKolumna.setZadanie(zadanieWybrane);
		newKolumna.setIdWpisal(zalogowanyUser.getIdUsers());
		newKolumna.setKtoWpisal(nameUser.user(zalogowanyUser.getIdUsers()));
		crudAll.create(newKolumna);
		naglowekList.add(newKolumna);
		
		RejestryLogi rl=new RejestryLogi();
		String opis="Utworzono nowa kolumnę ocen. Zadanie: "+newKolumna.getZadanie()+", naglówek/KPN: "+newKolumna.getNaglowek()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia();
		logi.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "zapis","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), newKolumna.getIdOcenyNaglowek(), newKolumna.getNaglowek(), "Kolumna ocen",opis,0));
		rl=null;
		new MessagePlay("Utworzono nowa kolumnę ocen: "+newKolumna.getNaglowek(),null,FacesMessage.SEVERITY_INFO);
		
		naglowek=null;
		komentarzKolumna=null;
		kolorKolumny=null;
		doSredniej=true;
	}
	
	public void kolumnaUpdate(OcenyNaglowek ocn) {
		brakKoloru=true;
		wybranaKolumnaUpdate=ocn;
	}
	

	public void onRowEditUpdatecol() {
		OcenyNaglowek ocnGet= wybranaKolumnaUpdate;
		if(!brakKoloru)
			ocnGet.setKolor(null);
		crudAll.update(ocnGet);
		
		RejestryLogi rl=new RejestryLogi();
		String opis="Uaktualniono kolumnę ocen. Zadanie: "+ocnGet.getZadanie()+", naglówek/KPN: "+ocnGet.getNaglowek()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia();
		logi.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "poprawa","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), ocnGet.getIdOcenyNaglowek(), ocnGet.getNaglowek(), "Kolumna ocen",opis,0));
		rl=null;
		
		new MessagePlay("Uaktualniono!",null,FacesMessage.SEVERITY_INFO);
	}
	
	public void wybranaOcenaPoprawa(Ocena ocp) {
		poprawaOceny=ocp;
	}
	
	public void onRowEditUpdateOc() {
		Ocena ocGet=poprawaOceny;
		crudAll.update(ocGet);
		
		RejestryLogi rl=new RejestryLogi();
		String opis="Poprawiono ocenę. Ocena: "+ocGet.getOcenaWartosc()+", komentarz: "+ocGet.getKomentarz()+". Słuchacz: "+ocGet.getSluchacze().getImieSluchacz()+" "+ocGet.getSluchacze().getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia();
		logi.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "poprawa","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), ocGet.getIdOcena(), ocGet.getOcenaWartosc(), "Ocena",opis,ocGet.getSluchacze().getIdS()));
		rl=null;
		
		new MessagePlay("Poprawiono ocenę!",null,FacesMessage.SEVERITY_INFO);
	}
	
	public void removeKolmna() {
		wybranaKolumnaUpdate.setUsunieta(true);
		wybranaKolumnaUpdate.setImieNazwiskoUserUsunal(nameUser.user(zalogowanyUser.getIdUsers()));
		wybranaKolumnaUpdate.setImieNazwiskoUserUsunalId(zalogowanyUser.getIdUsers());
		wybranaKolumnaUpdate.setKiedyUsunal(dataDzisTimeStamp);
		crudAll.update(wybranaKolumnaUpdate);
		
		RejestryLogi rl=new RejestryLogi();
		String opis="Usunięto kolumnę ocen. Zadanie: "+wybranaKolumnaUpdate.getZadanie()+", naglówek/KPN: "+wybranaKolumnaUpdate.getNaglowek()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia();
		logi.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "usunięcie","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), wybranaKolumnaUpdate.getIdOcenyNaglowek(), wybranaKolumnaUpdate.getNaglowek(), "Kolumna ocen",opis,0));
		rl=null;
	
		naglowekList.remove(wybranaKolumnaUpdate);
		wybranaKolumnaUpdate=null;
		new MessagePlay("Usunięto!",null,FacesMessage.SEVERITY_WARN);
	}
	
	public void removeOcena(Ocena ocrem) {
		ocrem.setUsunieta(true);
		ocrem.setImieNazwiskoUserUsunal(nameUser.user(zalogowanyUser.getIdUsers()));
		ocrem.setImieNazwiskoUserUsunalId(zalogowanyUser.getIdUsers());
		ocrem.setDataRemove(dataDzisTimeStamp);
		crudAll.update(ocrem);
		ocenyEditList.remove(ocrem);
		RejestryLogi rl=new RejestryLogi();
		String opis="Usunięto ocenę. Ocena: "+ocrem.getOcenaWartosc()+", komentarz: "+ocrem.getKomentarz()+". Słuchacz: "+ocrem.getSluchacze().getImieSluchacz()+" "+ocrem.getSluchacze().getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia();
		logi.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "usunięcie","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), ocrem.getIdOcena(), ocrem.getOcenaWartosc(), "Ocena",opis,ocrem.getSluchacze().getIdS()));
	
		rl=null;
		new MessagePlay("Usunięto ocenę!",null,FacesMessage.SEVERITY_INFO);
	}
	
	public void removeOcenaRowEx(Ocena ocrem) {
		ocrem.setUsunieta(true);
		ocrem.setImieNazwiskoUserUsunal(nameUser.user(zalogowanyUser.getIdUsers()));
		ocrem.setImieNazwiskoUserUsunalId(zalogowanyUser.getIdUsers());
		ocrem.setDataRemove(dataDzisTimeStamp);
		crudAll.update(ocrem);
		
		RejestryLogi rl=new RejestryLogi();
		String opis="Usunięto ocenę. Ocena: "+ocrem.getOcenaWartosc()+", komentarz: "+ocrem.getKomentarz()+". Słuchacz: "+ocrem.getSluchacze().getImieSluchacz()+" "+ocrem.getSluchacze().getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia();
		logi.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "usunięcie","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), ocrem.getIdOcena(), ocrem.getOcenaWartosc(), "Ocena",opis,ocrem.getSluchacze().getIdS()));
	
		rl=null;
		
		new MessagePlay("Usunięto ocenę!",null,FacesMessage.SEVERITY_INFO);
		
		for(OcenyNaglowek ocl:naglowekList) {
			if(ocl.getOcenas()!=null)
			ocl.getOcenas().remove(ocrem);
		}
	}
	
	public void resetFormKolumn() {
		naglowek=null;
		komentarzKolumna=null;
		kolorKolumny="";
		doSredniej=true;
		wybranaKolumnaUpdate=null;
	}
	
	public boolean blankKolor() {
		return brakKoloru;
	}
	

	public void ocenyClickCell(List<Ocena> ocenyList,Sluchacze sl,OcenyNaglowek oc, boolean okres) {
		setSluchaczWybranyOceny(sl);
		sluchaczes=new ArrayList<Integer>();
		sluchaczes.add(sl.getIdS());
		oc.setOkresowa(okres);
		setWybranaKolumnaOceny(oc);
		checkColumnOkres=okres;
		ocenyEditList=ocenyList;
	}
	
	public void addNowaOcena() {
	
		if(checkColumnOkres && wybranaKolumnaOceny.getOcenas()==null) {
			OcenyNaglowek newKolumna=wybranaKolumnaOceny;
			crudAll.create(newKolumna);
		}
		for(Integer sll:sluchaczes) {
			Ocena no=new Ocena();
			no.setDataWpisu(dataDzisTimeStamp);
			no.setDataOcena(nowaOcenaData);
			no.setKomentarz(nowaOcenaKomentarz);
			no.setOcenaWartosc(nowaOcena);
			no.setOcenyNglowek(wybranaKolumnaOceny);
			no.setIdUzytkownik(nameUser.userObject(zalogowanyUser.getIdUsers()));
			no.setUsunieta(false);
			no.setKolor(nowaOcenaKolor);
			if(sluchaczWybranyOceny.getIdS()==Integer.valueOf(sll))
			ocenyEditList.add(no);
			Sluchacze sz=plutonWybrany.getSluchaczes().stream().filter(s->s.getIdS()==Integer.valueOf(sll)).findFirst().orElse(null);
			if(sz!=null) {
				no.setSluchacze(sz);
				crudAll.create(no);
				if(wybranaKolumnaOceny.getOcenas()==null)
					wybranaKolumnaOceny.setOcenas(new ArrayList<Ocena>());
					wybranaKolumnaOceny.addOcena(no);
					
				RejestryLogi rl=new RejestryLogi();
				String opis="Dodano ocenę. Ocena: "+no.getOcenaWartosc()+", komentarz: "+no.getKomentarz()+". Słuchacz: "+sluchaczWybranyOceny.getImieSluchacz()+" "+sluchaczWybranyOceny.getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia();
				logi.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "zapis","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), no.getIdOcena(), no.getOcenaWartosc(), "Ocena",opis,sluchaczWybranyOceny.getIdS()));
				rl=null;
			}
	}
		
	
		
		new MessagePlay("Zapisano ocenę",null,FacesMessage.SEVERITY_INFO);
	
	}

	public void resetFormDeo() {
		 nowaOcenaData=new Date();
		 nowaOcenaKomentarz=null;
		 nowaOcena=null;
		 checkColumnOkres=false;
		 wybranaKolumnaOceny=null;
		 sluchaczWybranyOceny=null;
		 nowaOcenaKolor="e8e8e8";
		 poprawaOceny=null;
	}
 
	public List<Ocena> ocenyKolumnaSluchaczacz(OcenyNaglowek ocn,int idS){
		//naglowekList
		List<Ocena> ocr=new LinkedList<Ocena>();
		
		if(ocn!=null && ocn.getOcenas()!=null) {
				
				ocr.addAll(ocn.getOcenas().stream().filter(ocf->ocf.getSluchacze().getIdS()==idS && !ocf.getUsunieta()).collect(Collectors.toList()));
				Collections.sort(ocr,new OcenyCompareDataWpisu());
				
		}
		//srednia1=String.format("%.02f", intsrednia1);
		//srednia2=String.format("%.02f", intsrednia2);
		return ocr;
	}
	
	public LinkedList<String> ocenyKolumnaSluchaczaczSrednia(int idS){
   
		double ileOcen=0;
		double sumaOcen=0;
		double sredniaPrint=0;
		
		double ileOcen2=0;
		double sumaOcen2=0;
		double sredniaPrint2=0;
		LinkedList<String> listSrednia = new LinkedList<String>(); 
		listSrednia.add(0,"0,00");
		listSrednia.add(1,"0,00");
		for(OcenyNaglowek ocl:naglowekList) {
		
			if(ocl.isDoSredniej() && ocl.getOcenas()!=null)
			for(Ocena ocp:ocl.getOcenas()) {
				if(!ocp.getOcenaWartosc().equals("nzal") && !ocp.getOcenaWartosc().equals("zal") && !ocp.getUsunieta() && ocp.getSluchacze().getIdS()==idS) {
				ileOcen++;
				sumaOcen+=Double.valueOf(ocp.getOcenaWartosc());
				if(!ocp.getOcenaWartosc().equals("1")) {
					ileOcen2++;
					sumaOcen2+=Double.valueOf(ocp.getOcenaWartosc());
				}
				}
			}
		}
		if(ileOcen>0) {
		sredniaPrint=sumaOcen/ileOcen;
		listSrednia.set(0, String.format("%.02f", sredniaPrint));
		}
		if(ileOcen2>0) {
			sredniaPrint2=sumaOcen2/ileOcen2;
			listSrednia.set(1, String.format("%.02f", sredniaPrint2));
			}
		
		return listSrednia;
	}
	
	
	public List<Ocena> ocenyWykazRowExp(int idS){
		List<Ocena> ocr=new LinkedList<Ocena>();
		if(naglowekList!=null && naglowekList.size()>0)
		for(OcenyNaglowek ocn:naglowekList) {
			if(ocn!=null && ocn.getOcenas()!=null)
			ocr.addAll(ocn.getOcenas().stream().filter(ocf->ocf.getSluchacze().getIdS()==idS && !ocf.getUsunieta()).collect(Collectors.toList()));
		}
		if(naglowekListOkresowe!=null && naglowekListOkresowe.size()>0)
			for(OcenyNaglowek ocn:naglowekListOkresowe) {
				if(ocn!=null && ocn.getOcenas()!=null)
				ocr.addAll(ocn.getOcenas().stream().filter(ocf->ocf.getSluchacze().getIdS()==idS && !ocf.getUsunieta()).collect(Collectors.toList()));
			}
		
		return ocr;
	}
	public List<OcenyNaglowek> pobierzNaglowkiOcenySluchacz(int idPrzedmiot){
		return naglowekListSluch.stream().filter(ong-> ong.getPrzedmiot().getIdPrzedmiot()==idPrzedmiot && (!ong.isOkresowa())).collect(Collectors.toList());
	}
	public List<OcenyNaglowek> pobierzNaglowkiOcenySluchaczOkres(int idPrzedmiot){
		return naglowekListSluch.stream().filter(ong-> ong.getPrzedmiot().getIdPrzedmiot()==idPrzedmiot  && (ong.isOkresowa())).collect(Collectors.toList());
	}
	
	public List<Ocena> pobierzOcenySluchaczJMJS(int idPrzedmiot){
		List<OcenyNaglowek> geton=naglowekListSluch.stream().filter(ong-> ong.getPrzedmiot().getIdPrzedmiot()==idPrzedmiot).collect(Collectors.toList());
		List<Ocena> ocwrite=new LinkedList<Ocena>();
		
		if(geton!=null && geton.size()>0)
		for(OcenyNaglowek ocl:geton) {
			ocwrite.addAll(ocl.getOcenas().stream().filter(ocs->ocs.getSluchacze().getIdS()==ussluch.getSluchacze().getIdS()).collect(Collectors.toList()));
		}
		return ocwrite.stream().distinct().filter(ocw->!ocw.getUsunieta()).collect(Collectors.toList());
	}
	//--------------------------------------getters setters------------------------------------------------------------------
	
	public serwis.wyborPlutonu.wyborPlutonu getWyborPlutonu() {
		return wyborPlutonu;
	}
	public void setWyborPlutonu(serwis.wyborPlutonu.wyborPlutonu wyborPlutonu) {
		this.wyborPlutonu = wyborPlutonu;
	}
	public List<Sluchacze> getSluchaczeSorted() {
		return sluchaczeSorted;
	}
	public void setSluchaczeSorted(List<Sluchacze> sluchaczeSorted) {
		this.sluchaczeSorted = sluchaczeSorted;
	}

	public List<Ocena> getOceny() {
		return (List<Ocena>) oceny;
	}
	public void setOceny(List<Ocena> oceny) {
		this.oceny = (List<Ocena>) oceny;
	}
	
	public String getJmWybrana() {
		return jmWybrana;
	}
	public void setJmWybrana(String jmWybrana) {
		this.jmWybrana = jmWybrana;
	}
	public String getJsWybrana() {
		if(session.getAttribute("js")!=null)
			jsWybrana=(String) session.getAttribute("js");
		return jsWybrana;
	}
	public void setJsWybrana(String jsWybrana) {
		this.jsWybrana = jsWybrana;
	}

	public Przedmiot getPrzedmiot() {
		return przedmiot;
	}
	public void setPrzedmiot(Przedmiot przedmiot) {
		this.przedmiot = przedmiot;
	}
	public List<Przedmiot> getPrzedmiotList() {
		return przedmiotList;
	}
	public void setPrzedmiotList(List<Przedmiot> przedmiotList) {
		this.przedmiotList = przedmiotList;
	}
	public List<OcenyNaglowek> getNaglowekList() {
		return naglowekList;
	}
	public void setNaglowekList(List<OcenyNaglowek> naglowekList) {
		this.naglowekList = naglowekList;
	}

	public String getNaglowek() {
		return naglowek;
	}
	public void setNaglowek(String naglowek) {
		this.naglowek = naglowek;
	}
	public String getKomentarzKolumna() {
		return komentarzKolumna;
	}
	public void setKomentarzKolumna(String komentarzKolumna) {
		this.komentarzKolumna = komentarzKolumna;
	}
	public List<Pluton> getPlutonWybranyList() {
		return plutonWybranyList;
	}
	public void setPlutonWybranyList(List<Pluton> plutonWybranyList) {
		this.plutonWybranyList = plutonWybranyList;
	}
	public Pluton getPlutonWybrany() {
		return plutonWybrany;
	}
	public void setPlutonWybrany(Pluton plutonWybrany) {
		this.plutonWybrany = plutonWybrany;
	}
	public String getKolorKolumny() {
		return kolorKolumny;
	}
	public void setKolorKolumny(String kolorKolumny) {
		this.kolorKolumny = kolorKolumny;
	}
	public boolean isDoSredniej() {
		return doSredniej;
	}
	public void setDoSredniej(boolean doSredniej) {
		this.doSredniej = doSredniej;
	}
	public OcenyNaglowek getWybranaKolumnaUpdate() {
		return wybranaKolumnaUpdate;
	}
	public void setWybranaKolumnaUpdate(OcenyNaglowek wybranaKolumnaUpdate) {
		this.wybranaKolumnaUpdate = wybranaKolumnaUpdate;
	}
	public boolean isBrakKoloru() {
		return brakKoloru;
	}
	public void setBrakKoloru(boolean brakKoloru) {
		this.brakKoloru = brakKoloru;
	}
	public List<String> getOcenyList() {
		return ocenyList;
	}
	public void setOcenyList(List<String> ocenyList) {
		this.ocenyList = ocenyList;
	}
	public String getNowaOcena() {
		return nowaOcena;
	}
	public void setNowaOcena(String nowaOcena) {
		this.nowaOcena = nowaOcena;
	}
	public List<Ocena> getOcenyEditList() {
		return ocenyEditList;
	}
	public void setOcenyEditList(List<Ocena> ocenyEditList) {
		this.ocenyEditList = ocenyEditList;
	}
	public String getNowaOcenaKomentarz() {
		return nowaOcenaKomentarz;
	}
	public void setNowaOcenaKomentarz(String nowaOcenaKomentarz) {
		this.nowaOcenaKomentarz = nowaOcenaKomentarz;
	}
	public String getNowaOcenaKolor() {
		return nowaOcenaKolor;
	}
	public void setNowaOcenaKolor(String nowaOcenaKolor) {
		this.nowaOcenaKolor = nowaOcenaKolor;
	}
	public Date getNowaOcenaData() {
		return nowaOcenaData;
	}
	public void setNowaOcenaData(Date nowaOcenaData) {
		this.nowaOcenaData = nowaOcenaData;
	}
	public Sluchacze getSluchaczWybranyOceny() {
		return sluchaczWybranyOceny;
	}
	public void setSluchaczWybranyOceny(Sluchacze sluchaczWybranyOceny) {
		this.sluchaczWybranyOceny = sluchaczWybranyOceny;
	}
	public OcenyNaglowek getWybranaKolumnaOceny() {
		return wybranaKolumnaOceny;
	}
	public void setWybranaKolumnaOceny(OcenyNaglowek wybranaKolumnaOceny) {
		this.wybranaKolumnaOceny = wybranaKolumnaOceny;
	}
	public Map<String,String> getJm() {
		return jm;
	}
	public void setJm(Map<String,String> jm) {
		this.jm = jm;
	}
	public Map<String,String> getJs() {
		return js;
	}
	public void setJs(Map<String,String> js) {
		this.js = js;
	}
	public List<String> getZadanie() {
		return zadanie;
	}
	public void setZadanie(List<String> zadanie) {
		this.zadanie = zadanie;
	}
	public String getZadanieWybrane() {
		return zadanieWybrane;
	}
	public void setZadanieWybrane(String zadanieWybrane) {
		this.zadanieWybrane = zadanieWybrane;
	}
	public List<OcenyNaglowek> getNaglowekListOkresowe() {
		return naglowekListOkresowe;
	}
	public void setNaglowekListOkresowe(List<OcenyNaglowek> naglowekListOkresowe) {
		this.naglowekListOkresowe = naglowekListOkresowe;
	}
	public boolean isCheckColumnOkres() {
		return checkColumnOkres;
	}
	public void setCheckColumnOkres(boolean checkColumnOkres) {
		this.checkColumnOkres = checkColumnOkres;
	}
	public List<OcenyNaglowek> getNaglowekListSluch() {
		return naglowekListSluch;
	}
	public void setNaglowekListSluch(List<OcenyNaglowek> naglowekListSluch) {
		this.naglowekListSluch = naglowekListSluch;
	}
	public UsersStudent getUssluch() {
		return ussluch;
	}
	public void setUssluch(UsersStudent ussluch) {
		this.ussluch = ussluch;
	}
	public List<Integer> getSluchaczes() {
		return sluchaczes;
	}
	public void setSluchaczes(List<Integer> sluchaczes) {
		this.sluchaczes = sluchaczes;
	}
	public String getSrednia1() {
		return srednia1;
	}
	public void setSrednia1(String srednia1) {
		this.srednia1 = srednia1;
	}
	public String getSrednia2() {
		return srednia2;
	}
	public void setSrednia2(String srednia2) {
		this.srednia2 = srednia2;
	}
	public boolean isEditTem() {
		return editTem;
	}
	public void setEditTem(boolean editTem) {
		this.editTem = editTem;
	}
	public int getWybranySluchacz() {
		return wybranySluchacz;
	}
	public void setWybranySluchacz(int wybranySluchacz) {
		this.wybranySluchacz = wybranySluchacz;
	}

	public Ocena getPoprawaOceny() {
		return poprawaOceny;
	}

	public void setPoprawaOceny(Ocena poprawaOceny) {
		this.poprawaOceny = poprawaOceny;
	}
	
}
