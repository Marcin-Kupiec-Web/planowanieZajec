  package serwis.zajecia.tematy;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Obecnosc;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Przedmiot;
import com.edziennik.sluchacze.zajecia.model.Przedmiottemat;
import com.edziennik.sluchacze.zajecia.model.Tematy;
import com.userManager.model.User;

import my.util.MessagePlay;
import serwis.RejestryLogi.RejestryLogi;
import serwis.logowanie.Logowanie;
@Named
@ViewScoped
public class TematyServ implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CrudAllLocal crud;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;
	@Inject
	private serwis.users.nameUser.NameUser nameUser;
	@Inject
	private Logowanie zalogowanyUser;
	
	public serwis.users.nameUser.NameUser getNameUser() {
		return nameUser;
	}

	public void setNameUser(serwis.users.nameUser.NameUser nameUser) {
		this.nameUser = nameUser;
	}

	private Integer idPlutonWybrany;
	private Boolean moreTable=false;
	private List<Tematy> tematyWplutonie;
	private List<Tematy> selectedTemats;
	private List<Tematy> tematyWplutonieSort;
	private Tematy tematySelect;
	private Date dataFormGlowny;
	private List<String> usersAutocompleteMulti;
	private Map<String,String> jm;
	private Map<String,String> js;
	private Map<String,String> kpn;
	private List<User> us;
	private Pluton plutonWybrany;
	private boolean display;
	private boolean editTem=false;
	private int wszyscySluchacze;
	private Timestamp dataDzisTimeStamp;
	private List<Obecnosc> frekwencjaWdniu;
	private String jmWybrana;
	private String jsWybrana;
	private String  kpnWybrany;
	List<Przedmiot> lp;
	
	public TematyServ() {
		// TODO Auto-generated constructor stub
	}
@PostConstruct
public void init() {	
	initGo();
}

public void addMessage() {
    String summary = this.moreTable ? "Wszystkie kolumny!" : "Ukryto niektóre kolumny!";
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
}


public List<String> completeAreaUs(String query){
	List<String> results=new ArrayList<String>();
		for(User usAdd:us) {
			String imNazw=usAdd.getNazwiskoUsers().toLowerCase()+" "+usAdd.getImieUsers().toLowerCase();
			if(imNazw.contains(query.toLowerCase().trim()))
			results.add(usAdd.getNazwiskoUsers()+" "+usAdd.getImieUsers());
		}
	
	return results;
}

public void usersAutocompleteMultiAdd(String addImNazw){
	if(!zalogowanyUser.getPoziomUprawnien().equals("7"))
	{
	usersAutocompleteMulti=new ArrayList<String>();
	
	if(addImNazw!=null && addImNazw.trim().length()>0) {
		List<String>toAdd=new ArrayList<String>(Arrays.asList(addImNazw.split(", ")));
		usersAutocompleteMulti.addAll(toAdd);
		}
		else {
		int idUsers=zalogowanyUser.getIdUsers();
		usersAutocompleteMulti.add(nameUser.user(idUsers));
		}
	}
}

public void initGo() {
		wyborPlutonu.prepareChangeSzkolenie();
		idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
		dataFormGlowny=wyborPlutonu.getDataFormGlowny();
		Calendar cal = Calendar.getInstance();  
	 	dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
	 	setDisplay(false);
		if(idPlutonWybrany!=null && dataFormGlowny!=null) {
			
			HashMap<String, Object> hms=new HashMap<String,Object>();
			hms.put("idPluton", idPlutonWybrany);
			hms.put("zrealizowaneCzas", dataFormGlowny);
			tematyWplutonie=crud.getAllTermsParam("Tematy.findDate", hms);
			
			reinit();
					display = true;
			} else display = false;
		
		jm=new TreeMap<String,String>();
		js=new TreeMap<String,String>();
		kpn=new TreeMap<String,String>();
		lp=new LinkedList<Przedmiot>();
		
		 	HashMap<String, Object> hms=new HashMap<String,Object>();
			hms.put("idPluton", idPlutonWybrany);
					
				if(idPlutonWybrany!=null)
					{plutonWybrany=(Pluton) crud.getAllTermsParam("Pluton.findSluchaczePoIdPluton", hms).get(0);
					lp=plutonWybrany.getIdKurs().getPrzedmiots();
					
					 for(Przedmiot lpa:lp) {
							jm.put(lpa.getJm(), lpa.getJm());
						}
					 
						frekwencjaWdniu=new ArrayList<Obecnosc>();
						wszyscySluchacze=(int) plutonWybrany.getSluchaczes().stream().filter(sll->sll.getUsuniety().equals("NIE")).count();
						HashMap<String, Object> hmOb=new HashMap<String,Object>();
						hmOb.put("idPluton", idPlutonWybrany);
						hmOb.put("data", dataFormGlowny);
						setFrekwencjaWdniu(new ArrayList<Obecnosc>());
						setFrekwencjaWdniu(crud.getAllTermsParam("Obecnosc.findFrewDoTemat", hmOb));
					}
			us=new LinkedList<User>();
			us=crud.getAllTerms("findAllUser");
			
			
}

	public void setJmJs(String jmg, String jsg, boolean onlyJs) {
	for(Przedmiot lpa:lp) {
		if(lpa.getJm().equals(jmg))
		if(!onlyJs) {
			if(lpa.getJs()!=null)
				js.put(lpa.getJs(), lpa.getJs());
					else {
						 for(Przedmiottemat kpns:lpa.getPrzedmiottemats()){
							 if(kpns.getNazwa()!=null)
								 kpn.put(kpns.getNazwa(), kpns.getNazwa());
						 }
					}
			}
			if(jsg!=null && lpa.getJm().equals(jmg) && lpa.getJs().equals(jsg)) {
				 for(Przedmiottemat kpns:lpa.getPrzedmiottemats()){
					 if(kpns.getNazwa()!=null)
						 kpn.put(kpns.getNazwa(), kpns.getNazwa());
				 }
			}
		}
	}

public void listJm(Tematy temObj) {
	
	js.clear();
	kpn.clear();
	
	if(temObj!=null) {
		jmWybrana=temObj.getJM_tematy();
		jsWybrana=temObj.getJS_tematy();
	}
	
	 if(jmWybrana!=null)
		 setJmJs(jmWybrana, jsWybrana,false);
		
}
	public void listJs(Tematy temObj) {
		kpn.clear();
		
		if(temObj!=null) {
			jsWybrana=temObj.getJS_tematy();
		}
		 if(jmWybrana!=null && jsWybrana!=null)
			 setJmJs(jmWybrana, jsWybrana, true);
	}

	public void changeKPN(Tematy temObj) {
		if(temObj!=null) {
			kpnWybrany=temObj.getKPN_tematy();
		}
	}
	
	public void updateInit(Tematy temat) {
		tematySelect=temat;
		js=new TreeMap<String,String>();
		kpn=new TreeMap<String,String>();
		setJmJs(temat.getJM_tematy(), temat.getJS_tematy(),false);
		usersAutocompleteMultiAdd(temat.getKtoZrealizowal());
	}
	
	public void createInit() {
		usersAutocompleteMultiAdd(null);
	}
	public void removeRow(ActionEvent e) {
		com.edziennik.sluchacze.zajecia.model.Tematy remTem=(com.edziennik.sluchacze.zajecia.model.Tematy) e.getComponent().getAttributes().get("removeRow");
		remTem.setKiedyUsuniety(dataDzisTimeStamp);
		remTem.setKtoUsuna(zalogowanyUser.getIdUsers());
		remTem.setKtoUsunalStr(nameUser.user(zalogowanyUser.getIdUsers()));
		remTem.setTematUsuniety("TAK");
		crud.update(remTem);
		tematyWplutonie.remove(remTem);
	
		RejestryLogi rl=new RejestryLogi();
		
		String opis="Usunięto temat. JM/JS/KPN: "+remTem.getJM_tematy()+"/"+remTem.getJS_tematy()+"/"+remTem.getKPN_tematy()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia()+", dzien: "+remTem.getZrealizowaneCzas()+", godz.lekc.: "+remTem.getTematyGdzl();
		crud.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "usunięcie","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), remTem.getIdTematy(),remTem.getJM_tematy()+"/"+remTem.getJS_tematy()+"/"+remTem.getKPN_tematy(), "Tematy",opis,0));
		rl=null;
			
		reinit();
	}


	public void tematyStart() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("tematy.xhtml?i=2&subPage=tematy");
	}


	public void onCellEdit(Tematy event) {
			Tematy t=event;
		
		 	int idUsers=zalogowanyUser.getIdUsers();
	    	t.setDataWpisu(dataDzisTimeStamp);
	    	t.setZrealizowaneCzas(dataFormGlowny);
	        t.setIdWpisal(idUsers);
	        t.setTematUsuniety("NIE");
	        t.setHasNext(false);
	        t.setPluton(plutonWybrany);
	         int nieobecni=(int) frekwencjaWdniu.stream().filter(fl->fl.getGodzinaLekcyjna()==t.getTematyGdzl() && (!fl.getWartosc().equals("Sp"))).count();
	         int obecni=(int) (wszyscySluchacze-frekwencjaWdniu.stream().filter(fl->fl.getGodzinaLekcyjna()==t.getTematyGdzl() && (!fl.getWartosc().equals("Sp"))).count());
	        t.setNieobecni(String.valueOf(nieobecni));
	        t.setObecni(String.valueOf(obecni));
	        t.setKtoWpisal(nameUser.user(idUsers));
	        t.setKtoZrealizowal(usersAutocompleteMulti.toString().replace("[","").replace("]",""));
	        RejestryLogi rl=new RejestryLogi();
	        crud.update(t);
			String opis="Uaktualnino temat. JM/JS/KPN: "+t.getJM_tematy()+"/"+t.getJS_tematy()+"/"+t.getKPN_tematy()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia()+", dzien: "+t.getZrealizowaneCzas()+", godz.lekc.: "+t.getTematyGdzl();
			crud.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "poprawa","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), t.getIdTematy(),t.getJM_tematy()+"/"+t.getJS_tematy()+"/"+t.getKPN_tematy(), "Tematy",opis,0));
		
		        rl=null;
		  
		        jsWybrana=null;
		        kpnWybrany=null;
		        jmWybrana=null;
		        usersAutocompleteMulti=null;
		        new MessagePlay("Uaktualniono temat.",null,FacesMessage.SEVERITY_INFO);
	}

public void createNew() {
	if(selectedTemats!=null) {
	 	int idUsers=zalogowanyUser.getIdUsers();
	for(Tematy te:selectedTemats) {
    	Tematy t=new Tematy();
    	t.setTematyGdzl(te.getTematyGdzl());
    	t.setJM_tematy(jmWybrana);
    	t.setJS_tematy(jsWybrana);
    	t.setKPN_tematy(kpnWybrany);
	 	t.setDataWpisu(dataDzisTimeStamp);
    	t.setZrealizowaneCzas(dataFormGlowny);
        t.setIdWpisal(idUsers);
        t.setTematUsuniety("NIE");
        t.setHasNext(false);
        t.setPluton(plutonWybrany);
         int nieobecni=(int) frekwencjaWdniu.stream().filter(fl->fl.getGodzinaLekcyjna()==t.getTematyGdzl() && (!fl.getWartosc().equals("Sp"))).count();
         int obecni=(int) (wszyscySluchacze-frekwencjaWdniu.stream().filter(fl->fl.getGodzinaLekcyjna()==t.getTematyGdzl() && (!fl.getWartosc().equals("Sp"))).count());
        t.setNieobecni(String.valueOf(nieobecni));
        t.setObecni(String.valueOf(obecni));
        t.setKtoWpisal(nameUser.user(idUsers));
        t.setKtoZrealizowal(usersAutocompleteMulti.toString().replace("[","").replace("]",""));
    	crud.create(t);
    	RejestryLogi rl=new RejestryLogi();
		String opis="Zapisano temat. JM/JS/KPN: "+t.getJM_tematy()+"/"+t.getJS_tematy()+"/"+t.getKPN_tematy()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia()+", dzien: "+t.getZrealizowaneCzas()+", godz.lekc.: "+t.getTematyGdzl();
		crud.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "zapis","e-dziennik",zalogowanyUser.getSessionLogedKlient(),zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), t.getIdTematy(),t.getJM_tematy()+"/"+t.getJS_tematy()+"/"+t.getKPN_tematy(), "Tematy",opis,0));
	rl=null;
tematyWplutonie.add(t);
	}
	}
	reinit();
	selectedTemats=null;
    jsWybrana=null;
    kpnWybrany=null;
    jmWybrana=null;
    usersAutocompleteMulti=null;
    new MessagePlay("Zapisano temat.",null,FacesMessage.SEVERITY_INFO);
}

public void reinit() {
	tematyWplutonieSort=new ArrayList<Tematy>();
	for(int i=1;i<15;i++)	{
		int k=i;
				Tematy tempty=new Tematy();
				tempty.setTematyGdzl(i);
				
				Tematy t=tematyWplutonie.stream().filter(temat->temat.getTematyGdzl()==k).findFirst().orElse(tempty);
				Tematy tNext=tematyWplutonie.stream().filter(temat->temat.getTematyGdzl()==k+1).findFirst().orElse(null);
			
				if(t.getIdWpisal()!=0 && t.getKtoWpisal()==null) {
				t.setKtoWpisal(nameUser.user(t.getIdWpisal()));
				}
				if(tNext==null && t!=null && t.getJM_tematy()!=null && t.getTematyGdzl()!=14) {
					t.setHasNext(true);
				}
				tematyWplutonieSort.add(t);
	}
}
	public void addMessage(String summary,String detail, Severity ms) {
			FacesMessage message=new FacesMessage(ms,summary,detail);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
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

	public Date getDataFormGlowny() {
		return dataFormGlowny;
	}

	public void setDataFormGlowny(Date dataFormGlowny) {
		this.dataFormGlowny = dataFormGlowny;
	}

	public List<Tematy> getTematyWplutonie() {
		return tematyWplutonie;
	}

	public void setTematyWplutonie(List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyWplutonie) {
		this.tematyWplutonie = tematyWplutonie;
	}

	public List<com.edziennik.sluchacze.zajecia.model.Tematy> getTematyWplutonieSort() {
		return tematyWplutonieSort;
	}

	public void setTematyWplutonieSort(List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyWplutonieSort) {
		this.tematyWplutonieSort = tematyWplutonieSort;
	}

	public List<String> getUsersAutocompleteMulti() {
		return usersAutocompleteMulti;
	}

	public void setUsersAutocompleteMulti(List<String> usersAutocompleteMulti) {
		this.usersAutocompleteMulti = usersAutocompleteMulti;
	}

	public Map<String, String> getJm() {
		return jm;
	}

	public void setJm(Map<String, String> jm) {
		this.jm = jm;
	}

	public Map<String, String> getJs() {
		return js;
	}

	public void setJs(Map<String, String> js) {
		this.js = js;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
	public Pluton getPlutonWybrany() {
		return plutonWybrany;
	}

	public void setPlutonWybrany(Pluton plutonWybrany) {
		this.plutonWybrany = plutonWybrany;
	}

	public Map<String, String> getKpn() {
		return kpn;
	}

	public void setKpn(Map<String, String> kpn) {
		this.kpn = kpn;
	}

	public int getWszyscySluchacze() {
		return wszyscySluchacze;
	}

	public void setWszyscySluchacze(int wszyscySluchacze) {
		this.wszyscySluchacze = wszyscySluchacze;
	}

	public List<Obecnosc> getFrekwencjaWdniu() {
		return frekwencjaWdniu;
	}

	public void setFrekwencjaWdniu(List<Obecnosc> frekwencjaWdniu) {
		this.frekwencjaWdniu = frekwencjaWdniu;
	}

	public Logowanie getZalogowanyUser() {
		return zalogowanyUser;
	}

	public void setZalogowanyUser(Logowanie zalogowanyUser) {
		this.zalogowanyUser = zalogowanyUser;
	}

	public Boolean getMoreTable() {
		return moreTable;
	}

	public void setMoreTable(Boolean moreTable) {
		this.moreTable = moreTable;
	}

	public boolean isEditTem() {
		return editTem;
	}

	public void setEditTem(boolean editTem) {
		this.editTem = editTem;
	}

	public List<Tematy> getSelectedTemats() {
		return selectedTemats;
	}

	public void setSelectedTemats(List<Tematy> selectedTemats) {
		this.selectedTemats = selectedTemats;
	}

	public String getJsWybrana() {
		return jsWybrana;
	}

	public void setJsWybrana(String jsWybrana) {
		this.jsWybrana = jsWybrana;
	}

	public String getJmWybrana() {
		return jmWybrana;
	}

	public void setJmWybrana(String jmWybrana) {
		this.jmWybrana = jmWybrana;
	}

	public List<Przedmiot> getLp() {
		return lp;
	}

	public void setLp(List<Przedmiot> lp) {
		this.lp = lp;
	}

	public String getKpnWybrany() {
		return kpnWybrany;
	}

	public void setKpnWybrany(String kpnWybrany) {
		this.kpnWybrany = kpnWybrany;
	}

	public Tematy getTematySelect() {
		return tematySelect;
	}

	public void setTematySelect(Tematy tematySelect) {
		this.tematySelect = tematySelect;
	}

}





/*
package serwis.zajecia.tematy;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Obecnosc;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Przedmiot;
import com.edziennik.sluchacze.zajecia.model.Przedmiottemat;
import com.userManager.model.User;

import serwis.RejestryLogi.RejestryLogi;
import serwis.logowanie.Logowanie;
@Named
@RequestScoped
public class Tematy {

	@EJB
	private CrudAllLocal crud;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;
	@Inject
	private serwis.users.nameUser.NameUser nameUser;
	@Inject
	private Logowanie zalogowanyUser;
	
	public serwis.users.nameUser.NameUser getNameUser() {
		return nameUser;
	}

	public void setNameUser(serwis.users.nameUser.NameUser nameUser) {
		this.nameUser = nameUser;
	}

	private Integer idPlutonWybrany;
	
	private Boolean moreTable=false;
	
	private List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyWplutonie;

	private List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyWplutonieSort;
	
	private List<com.edziennik.sluchacze.zajecia.model.Tematy> tematySelection;
	
	private Date dataFormGlowny;
	
	private List<String> usersAutocompleteMulti;
	
	private List<Integer> tematyGdzlDo;
	private Map<String,String> jm;
	private Map<String,String> js;
	private Map<String,String> kpn;
	private List<User> us;
	private int  tematyGodzlDoSelected;
	private String  jmSelected;
	private String  jsSelected;
	private String  kpnSelected;
	private Pluton plutonWybrany;
	private boolean display;
	private int wszyscySluchacze;
	private Timestamp dataDzisTimeStamp;
	private List<Obecnosc> frekwencjaWdniu;
	public Tematy() {
		// TODO Auto-generated constructor stub
	}
@PostConstruct
public void init() {
	
initGo();

}

public void addMessage() {
    String summary = this.moreTable ? "Wszystkie kolumny!" : "Ukryto niektóre kolumny!";
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
}
public List<String> completeAreaUs(String query){

	List<String> results=new ArrayList<String>();
		for(User usAdd:us) {
			String imNazw=usAdd.getNazwiskoUsers().toLowerCase()+" "+usAdd.getImieUsers().toLowerCase();
			if(imNazw.contains(query.toLowerCase().trim()))
			results.add(usAdd.getNazwiskoUsers()+" "+usAdd.getImieUsers());
		}
	
	return results;
}


public void initGo() {
		idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
	
		dataFormGlowny=wyborPlutonu.getDataFormGlowny();
		Calendar cal = Calendar.getInstance();  
	 	dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
	 	setDisplay(false);
		if(idPlutonWybrany!=null && dataFormGlowny!=null) {
			
			HashMap<String, Object> hms=new HashMap<String,Object>();
			hms.put("idPluton", idPlutonWybrany);
			hms.put("zrealizowaneCzas", dataFormGlowny);
			tematyWplutonie=crud.getAllTermsParam("Tematy.findDate", hms);
			
			tematyWplutonieSort=new ArrayList<com.edziennik.sluchacze.zajecia.model.Tematy>();
					for(int i=1;i<15;i++)	{
						int k=i;
								com.edziennik.sluchacze.zajecia.model.Tematy tempty=new com.edziennik.sluchacze.zajecia.model.Tematy();
								tempty.setTematyGdzl(i);
								
								com.edziennik.sluchacze.zajecia.model.Tematy t=tematyWplutonie.stream().filter(temat->temat.getTematyGdzl()==k).findFirst().orElse(tempty);
								com.edziennik.sluchacze.zajecia.model.Tematy tNext=tematyWplutonie.stream().filter(temat->temat.getTematyGdzl()==k+1).findFirst().orElse(null);
							
								if(t.getIdWpisal()!=0 && t.getKtoWpisal()==null) {
								t.setKtoWpisal(nameUser.user(t.getIdWpisal()));
								}
								if(tNext==null && t!=null && t.getJM_tematy()!=null && t.getTematyGdzl()!=14) {
									t.setHasNext(true);
								}
								tematyWplutonieSort.add(t);
					}
					display=true;
			}
		
		jm=new TreeMap<String,String>();
		js=new TreeMap<String,String>();
		kpn=new TreeMap<String,String>();
			
		 	HashMap<String, Object> hms=new HashMap<String,Object>();
			hms.put("idPluton", idPlutonWybrany);
					
				if(idPlutonWybrany!=null)
					{plutonWybrany=(Pluton) crud.getAllTermsParam("Pluton.findSluchaczePoIdPluton", hms).get(0);
					List<Przedmiot> lp=plutonWybrany.getIdKurs().getPrzedmiots();
					
						for(Przedmiot lpa:lp) {
							jm.put(lpa.getJm(), lpa.getJm());
							if(lpa.getJs()!=null)
							js.put(lpa.getJs(), lpa.getJs());
							
						}
						frekwencjaWdniu=new ArrayList<Obecnosc>();
						wszyscySluchacze=(int) plutonWybrany.getSluchaczes().stream().filter(sll->sll.getUsuniety().equals("NIE")).count();
						HashMap<String, Object> hmOb=new HashMap<String,Object>();
						hmOb.put("idPluton", idPlutonWybrany);
						hmOb.put("data", dataFormGlowny);
						setFrekwencjaWdniu(new ArrayList<Obecnosc>());
						setFrekwencjaWdniu(crud.getAllTermsParam("Obecnosc.findFrewDoTemat", hmOb));
					}
			us=new LinkedList<User>();
			us=crud.getAllTerms("findAllUser");
			
			
}


	public void changeJs() {
		List<Przedmiot> lp=plutonWybrany.getIdKurs().getPrzedmiots();
			for(Przedmiot lpa:lp) {
					if(js.size()>0) {
						if(lpa.getJm().equals(jmSelected) && lpa.getJs().equals(jsSelected))
								 for(Przedmiottemat kpns:lpa.getPrzedmiottemats()) {
										 if(kpns.getNazwa()!=null)
										 kpn.put(kpns.getNazwa(), kpns.getNazwa());
								 }
			}else {
					if(lpa.getJm().equals(jmSelected))
						 for(Przedmiottemat kpns:lpa.getPrzedmiottemats()){
								 if(kpns.getNazwa()!=null)
									 kpn.put(kpns.getNazwa(), kpns.getNazwa());
				}
			}
		}
	}

	public String getJmJs(String jms,String jss) {
			if(jms!=null && jss!=null) {
			jsSelected=jss;
			jmSelected=jms;
			List<Przedmiot> lp=plutonWybrany.getIdKurs().getPrzedmiots();
			kpn.clear();
			for(Przedmiot lpa:lp) {
				jm.put(lpa.getJm(), lpa.getJm());
				if(lpa.getJs()!=null) {
				js.put(lpa.getJs(), lpa.getJs());
				if(lpa.getJm().equals(jmSelected) && lpa.getJs().equals(jsSelected))
					 for(Przedmiottemat kpns:lpa.getPrzedmiottemats()) {
							 if(kpns.getNazwa()!=null)
							 kpn.put(kpns.getNazwa(), kpns.getNazwa());
					 }
				}
				else {
					if(lpa.getJm().equals(jmSelected))
						 for(Przedmiottemat kpns:lpa.getPrzedmiottemats()){
								 if(kpns.getNazwa()!=null)
									 kpn.put(kpns.getNazwa(), kpns.getNazwa());
				}
				}
			}
		}
		return jms;
	}
	
	public void (ActionEvent e) {
			com.edziennik.sluchacze.zajecia.model.Tematy remTem=(com.edziennik.sluchacze.zajecia.model.Tematy) e.getComponent().getAttributes().get("removeRow");
			remTem.setKiedyUsuniety(dataDzisTimeStamp);
			remTem.setKtoUsuna(zalogowanyUser.getIdUsers());
			remTem.setKtoUsunalStr(nameUser.user(zalogowanyUser.getIdUsers()));
			remTem.setTematUsuniety("TAK");
			crud.update(remTem);
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("tematy.xhtml?i=2&subPage=tematy");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			RejestryLogi rl=new RejestryLogi();
			
			String opis="Usunięto temat. JM/JS/KPN: "+remTem.getJM_tematy()+"/"+remTem.getJS_tematy()+"/"+remTem.getKPN_tematy()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia()+", dzien: "+remTem.getZrealizowaneCzas()+", godz.lekc.: "+remTem.getTematyGdzl();
			crud.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "Usunięcie",zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), remTem.getIdTematy(),remTem.getJM_tematy()+"/"+remTem.getJS_tematy()+"/"+remTem.getKPN_tematy(), "Tematy",opis,0));
			rl=null;
	}

	public String usersAutocompleteMultiAdd(String addImNazw){
		if(!zalogowanyUser.getStatus().equals("sluchacz"))
		{
		usersAutocompleteMulti=new ArrayList<String>();
		
		if(addImNazw!=null && addImNazw.trim().length()>0) {
			List<String>toAdd=new ArrayList<String>(Arrays.asList(addImNazw.split(",")));
			usersAutocompleteMulti.addAll(toAdd);
		}
		else {
			int idUsers=zalogowanyUser.getIdUsers();
			usersAutocompleteMulti.add(nameUser.user(idUsers));
		}
	}
	return addImNazw;
	}

	public List<Integer> tematyGdzlDo(int dogo) {
		tematyGdzlDo=new ArrayList<Integer>();
		tematyGdzlDo.add(dogo);
		if (tematyWplutonie.stream().filter(temat->temat.getTematyGdzl()==dogo).count()>0)
			return tematyGdzlDo;
		
			for(int i=dogo+1;i<16;i++) {
				int k=i;
				if(tematyWplutonie.stream().filter(temat->temat.getTematyGdzl()==k).count()==0) {
				tematyGdzlDo.add(i);
				}else
					return tematyGdzlDo;
			}
		return tematyGdzlDo;
	
}

	public void tematyStart() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("tematy.xhtml?i=2&subPage=tematy");
	}

	public void onRowEdit(RowEditEvent e) {
			com.edziennik.sluchacze.zajecia.model.Tematy t=(com.edziennik.sluchacze.zajecia.model.Tematy) e.getObject();
			
		 	Calendar cal = Calendar.getInstance();  
	    	Date dataDzis=cal.getTime();
	    	int idUsers=zalogowanyUser.getIdUsers();
	    	t.setJM_tematy(jmSelected);
	    	t.setJS_tematy(jsSelected);
	    	t.setDataWpisu(dataDzis);
	    	t.setZrealizowaneCzas(dataFormGlowny);
	        t.setIdWpisal(idUsers);
	        t.setTematUsuniety("NIE");
	        t.setHasNext(false);
	        t.setPluton( plutonWybrany);
	         int nieobecni=(int) frekwencjaWdniu.stream().filter(fl->fl.getGodzinaLekcyjna()==t.getTematyGdzl() && (!fl.getWartosc().equals("Sp"))).count();
	         int obecni=(int) (wszyscySluchacze-frekwencjaWdniu.stream().filter(fl->fl.getGodzinaLekcyjna()==t.getTematyGdzl() && (!fl.getWartosc().equals("Sp"))).count());
	        t.setNieobecni(String.valueOf(nieobecni));
	        t.setObecni(String.valueOf(obecni));
	        t.setKtoWpisal(nameUser.user(idUsers));
	        t.setKtoZrealizowal(usersAutocompleteMulti.toString().replace("[", "").replace("]", ""));
	        RejestryLogi rl=new RejestryLogi();
	        
		        for(int i=t.getTematyGdzl();i<=tematyGodzlDoSelected;i++) {
			        	t.setTematyGdzl(i);
			        	int il=i;
			    if(tematyWplutonie.stream().filter(tmf->tmf.getTematyGdzl()==il).count()>0)
			    { 
			    crud.update(t);
				String opis="Uaktualnino temat. JM/JS/KPN: "+t.getJM_tematy()+"/"+t.getJS_tematy()+"/"+t.getKPN_tematy()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia()+", dzien: "+t.getZrealizowaneCzas()+", godz.lekc.: "+t.getTematyGdzl();
				crud.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "Poprawa",zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), t.getIdTematy(),t.getJM_tematy()+"/"+t.getJS_tematy()+"/"+t.getKPN_tematy(), "Tematy",opis,0));
				
			    }
			    else {
			    	com.edziennik.sluchacze.zajecia.model.Tematy tc=new com.edziennik.sluchacze.zajecia.model.Tematy();
			    	
			    	tc.setJM_tematy(t.getJM_tematy());
			    	tc.setJS_tematy(t.getJS_tematy());
			    	tc.setKPN_tematy(t.getKPN_tematy());
			    	tc.setTematyGdzl(i);
			        tc.setObecni(t.getObecni());
			        tc.setNieobecni(t.getNieobecni());
			    	tc.setDataWpisu(t.getDataWpisu());
			    	tc.setZrealizowaneCzas(t.getZrealizowaneCzas());
			        tc.setIdWpisal(idUsers);
			        tc.setTematUsuniety("NIE");
			        tc.setHasNext(false);
			        tc.setPluton(t.getPluton());
			        tc.setKtoWpisal(t.getKtoWpisal());
			        tc.setKtoZrealizowal(t.getKtoZrealizowal());
			        tc.setPluton(t.getPluton());
			       
			    	crud.create(tc);
			    
					String opis="Zapisano temat. JM/JS/KPN: "+tc.getJM_tematy()+"/"+tc.getJS_tematy()+"/"+tc.getKPN_tematy()+". Komp./plut./szkol: "+plutonWybrany.getKompania().getNazwaKompania()+"/"+plutonWybrany.getNazwaPluton()+"/"+plutonWybrany.getOznaczenieSzkolenia()+", dzien: "+tc.getZrealizowaneCzas()+", godz.lekc.: "+tc.getTematyGdzl();
					crud.create(rl.zapiszLogiOpis(dataDzisTimeStamp, "Zapis",zalogowanyUser.getIdUsers(),nameUser.user(zalogowanyUser.getIdUsers()), tc.getIdTematy(),tc.getJM_tematy()+"/"+tc.getJS_tematy()+"/"+tc.getKPN_tematy(), "Tematy",opis,0));
			    
			    }
			 
		      }
		        rl=null;
		        try {
					FacesContext.getCurrentInstance().getExternalContext().redirect("tematy.xhtml?i=2&subPage=tematy");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	}
	public void copyDown(ActionEvent e) {
			com.edziennik.sluchacze.zajecia.model.Tematy tematCopy=new com.edziennik.sluchacze.zajecia.model.Tematy();
			com.edziennik.sluchacze.zajecia.model.Tematy temat=(com.edziennik.sluchacze.zajecia.model.Tematy) e.getComponent().getAttributes().get("copyRowy");
			tematCopy.setTematyGdzl(temat.getTematyGdzl()+1);
			tematCopy.setZrealizowaneCzas(temat.getZrealizowaneCzas());
			tematCopy.setJM_tematy(temat.getJM_tematy());
			tematCopy.setJS_tematy(temat.getJS_tematy());
			tematCopy.setKPN_tematy(temat.getKPN_tematy());
			tematCopy.setKtoWpisal(temat.getKtoWpisal());
			tematCopy.setKtoZrealizowal(temat.getKtoZrealizowal());
			tematCopy.setObecni(temat.getObecni());
			tematCopy.setNieobecni(temat.getNieobecni());
			//temat.setHasNext(false);
		
			tematyWplutonieSort.set(temat.getTematyGdzl(),tematCopy);
			tematyWplutonieSort.set(temat.getTematyGdzl()-1,temat);
}

	public void addMessage(String summary,String detail, Severity ms) {
			FacesMessage message=new FacesMessage(ms,summary,detail);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
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

	public Date getDataFormGlowny() {
		return dataFormGlowny;
	}

	public void setDataFormGlowny(Date dataFormGlowny) {
		this.dataFormGlowny = dataFormGlowny;
	}

	public List<com.edziennik.sluchacze.zajecia.model.Tematy> getTematyWplutonie() {
		return tematyWplutonie;
	}

	public void setTematyWplutonie(List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyWplutonie) {
		this.tematyWplutonie = tematyWplutonie;
	}

	public List<com.edziennik.sluchacze.zajecia.model.Tematy> getTematyWplutonieSort() {
		return tematyWplutonieSort;
	}

	public void setTematyWplutonieSort(List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyWplutonieSort) {
		this.tematyWplutonieSort = tematyWplutonieSort;
	}

	public List<Integer> getTematyGdzlDo() {
		return tematyGdzlDo;
	}

	public void setTematyGdzlDo(List<Integer> tematyGdzlDo) {
		this.tematyGdzlDo = tematyGdzlDo;
	}

	public int getTematyGodzlDoSelected() {
		return tematyGodzlDoSelected;
	}

	public void setTematyGodzlDoSelected(int tematyGodzlDoSelected) {
		this.tematyGodzlDoSelected = tematyGodzlDoSelected;
	}

	public List<com.edziennik.sluchacze.zajecia.model.Tematy> getTematySelection() {
		return tematySelection;
	}

	public void setTematySelection(List<com.edziennik.sluchacze.zajecia.model.Tematy> tematySelection) {
		this.tematySelection = tematySelection;
	}

	public List<String> getUsersAutocompleteMulti() {
		return usersAutocompleteMulti;
	}

	public void setUsersAutocompleteMulti(List<String> usersAutocompleteMulti) {
		this.usersAutocompleteMulti = usersAutocompleteMulti;
	}

	public Map<String, String> getJm() {
		return jm;
	}

	public void setJm(Map<String, String> jm) {
		this.jm = jm;
	}

	public String getJmSelected() {
		return jmSelected;
	}

	public void setJmSelected(String jmSelected) {
		this.jmSelected = jmSelected;
	}

	public Map<String, String> getJs() {
		return js;
	}

	public void setJs(Map<String, String> js) {
		this.js = js;
	}

	public String getJsSelected() {
		return jsSelected;
	}

	public void setJsSelected(String jsSelected) {
		this.jsSelected = jsSelected;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
	public Pluton getPlutonWybrany() {
		return plutonWybrany;
	}

	public void setPlutonWybrany(Pluton plutonWybrany) {
		this.plutonWybrany = plutonWybrany;
	}

	public Map<String, String> getKpn() {
		return kpn;
	}

	public void setKpn(Map<String, String> kpn) {
		this.kpn = kpn;
	}

	public String getKpnSelected() {
		return kpnSelected;
	}

	public void setKpnSelected(String kpnSelected) {
		this.kpnSelected = kpnSelected;
	}


	public int getWszyscySluchacze() {
		return wszyscySluchacze;
	}

	public void setWszyscySluchacze(int wszyscySluchacze) {
		this.wszyscySluchacze = wszyscySluchacze;
	}

	public List<Obecnosc> getFrekwencjaWdniu() {
		return frekwencjaWdniu;
	}

	public void setFrekwencjaWdniu(List<Obecnosc> frekwencjaWdniu) {
		this.frekwencjaWdniu = frekwencjaWdniu;
	}

	public Logowanie getZalogowanyUser() {
		return zalogowanyUser;
	}

	public void setZalogowanyUser(Logowanie zalogowanyUser) {
		this.zalogowanyUser = zalogowanyUser;
	}

	public Boolean getMoreTable() {
		return moreTable;
	}

	public void setMoreTable(Boolean moreTable) {
		this.moreTable = moreTable;
	}

}
*/
