package serwisPlany;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.Zaklad;
import com.plany.model.Planyzaklady;
import com.plany.model.UserBlokadaPlany;
import com.userManager.model.User;

import my.util.MessagePlay;
import serwis.RejestryLogi.RejestryLogi;

@Named
@ViewScoped
public class PlanyWykladowcy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CrudAllLocal crudAll;
	private List<Planyzaklady> planyZakladyWykladowcy;
	private List<Planyzaklady> selectedPrzedmiotPlan;
	private List<Specjalizacja> userSpecjal;
	private List<SelectItem> userSpecjalListSelect;
	private Map<String,Integer>miesiace;
	private Map<Integer,Integer>lata;
	private Date miesiacRok;
	private Integer miesiacWybrany;
	private Integer miesiacWybranyPodsumowanie;
	private Integer rokWybrany;
	private String[] selectedUserSpecjal;
	private Map<String,Serializable> filterValues=new HashMap<>(); 
	private List<User> userSave;
	private List<User> userAll;
	private List<String> specjalFilterSelect;
	private boolean ograniczenia=false;
	private List<UserBlokadaPlany> usbp;
	private boolean blokadaCalydzien=false;
	private Integer userBlokada;
	private Date usblokDatOd;
	private Date usblokDatDo;
	private String komentarzBlok;
	private List<Zaklad> zakladAll;
	private Timestamp dataDzisTimeStamp;
	
	public List<UserBlokadaPlany> getUsbp() {
		return usbp;
	}
	public void setUsbp(List<UserBlokadaPlany> usbp) {
		this.usbp = usbp;
	}
	public PlanyWykladowcy() {
		// TODO Auto-generated constructor stub
	}
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
	
	@PostConstruct 
    public void init() {
		 planyZakladyWykladowcy=new ArrayList<Planyzaklady>();
		miesiace=new LinkedHashMap<String,Integer>();
		lata=new HashMap<Integer,Integer>();
		miesiace.put("Styczeń",1);
		miesiace.put("Luty",2);
		miesiace.put("Marzec",3);
		miesiace.put("Kwiecień",4);
		miesiace.put("Maj",5);
		miesiace.put("Czerwiec",6);
		miesiace.put("Lipiec",7);
		miesiace.put("Sierpień",8);
		miesiace.put("Wrzesień",9);
		miesiace.put("Pazdziernik",10);
		miesiace.put("Listopad",11);
		miesiace.put("Grudzeń",12);

		miesiacRok=new Date();
		Calendar c = Calendar.getInstance();
		dataDzisTimeStamp=new Timestamp(c.getTimeInMillis());
		
		c.setTime(miesiacRok);
		setRokWybrany(c.get(Calendar.YEAR));
		setMiesiacWybrany(c.get(Calendar.MONTH)+1);
		setMiesiacWybranyPodsumowanie(c.get(Calendar.MONTH)+1);
		 for(int i=c.get(Calendar.YEAR)-4;i<c.get(Calendar.YEAR)+4;i++) {
			 lata.put(i, i);
		 }
		 if(session.getAttribute("miesiacWybrany")!=null)
			 miesiacWybrany=(Integer) session.getAttribute("miesiacWybrany");
		 if(session.getAttribute("rokWybrany")!=null)
			 rokWybrany=(Integer) session.getAttribute("rokWybrany");
		 
		 HashMap<String, Object> hm=new HashMap<String,Object>();
			hm.put("miesiac",miesiacWybrany);
			hm.put("rok",rokWybrany);
		 planyZakladyWykladowcy=crudAll.getAllTermsParam("Planyzaklady.findAllNotArche", hm);
	
		 if(session.getAttribute("poziomUprawnien").equals("4") && planyZakladyWykladowcy!=null) {
			 planyZakladyWykladowcy=planyZakladyWykladowcy.stream().filter(pzwf->pzwf.getPrzedmiottemat().getZakladyToString().contains((CharSequence) session.getAttribute("zaklad"))).collect(Collectors.toList());
		
		 }
		 specjalFilterSelect=new LinkedList<String>();
	
		 List<Specjalizacja> sl=new LinkedList<Specjalizacja>();
		
		 sl=crudAll.getAllTerms("Specjalizacja.findAll");
		
				for(Specjalizacja specStr:sl) {
					if(!specjalFilterSelect.contains(specStr.getNazwa()))
					specjalFilterSelect.add(specStr.getNazwa());
				}
				
				userAll=crudAll.getAllTerms("findAllUser");
				List<User>usr=new ArrayList<User>();
				if(session.getAttribute("poziomUprawnien").equals("4") && userAll!=null) {
					for(User us:userAll) {
						for(Zaklad usz:us.getZaklads()) {
							if(usz.getNazwaSkrot().equals(session.getAttribute("zaklad"))){
								usr.add(us);
							}
						}
					}
					userAll=usr;
					setOgraniczenia(true);
				}
				usbp=new LinkedList<UserBlokadaPlany>();
				zakladAll=new ArrayList<Zaklad>();
				for(User usb:userAll) {
					for(UserBlokadaPlany usbl:usb.getUserBlokadaPlanys()) {
						int dataStart=Integer.parseInt(new SimpleDateFormat("MM").format(usbl.getDzienOd()));
						int dataEnd=Integer.parseInt(new SimpleDateFormat("MM").format(usbl.getDzienDo()));
					if(dataStart<=miesiacWybrany && dataEnd>=miesiacWybrany)
						usbp.add(usbl);
					}
					for(Zaklad zul:usb.getZaklads()) {
						if(zakladAll.stream().filter(zf->zf.getNazwaSkrot().equals(zul.getNazwaSkrot())).findFirst().orElse(null)==null) {
							zakladAll.add(zul);
						}
					}
				}
				
	}

	public int sumaGodzinMiesiac(User us, int miesiac) {
		int sumMiesiac=0;
		
		for(Planyzaklady pzl:us.getPlaynZakladys()) {
			if(pzl.getMiesiac()==miesiac && pzl.getRok().equals(rokWybrany))
			sumMiesiac+=pzl.getIleGodzin();
		}
		return sumMiesiac;
		
	}
	public int sumaGodzinMiesiacZaklad(Zaklad zak, int miesiac) {
		int sumMiesiac=0;
		
		for(User usl:userAll) {
			for(Planyzaklady pzl:usl.getPlaynZakladys()) {
				if(usl.getZaklads().stream().filter(uszl->uszl.getNazwaSkrot().equals(zak.getNazwaSkrot())).findFirst().orElse(null)!=null) {
					if(pzl.getMiesiac()==miesiac && pzl.getRok().equals(rokWybrany))
						sumMiesiac+=pzl.getIleGodzin();
				}
			}
		}
		return sumMiesiac;
		
	}
	public int sumaGodzinWszystkie(User us) {
		int sumAll=0;
	if(us!=null)
		for(Planyzaklady pzl:us.getPlaynZakladys()) {
			if(pzl.getRok().equals(rokWybrany))
			sumAll+=pzl.getIleGodzin();
		}
		return sumAll;
		
	}
	
	public int sumaGodzinWszystkieZaklad(Zaklad zak) {
		int sumAll=0;
	if(zak!=null)
		for(User usl:userAll) {
			for(Planyzaklady pzl:usl.getPlaynZakladys()) {
				if(usl.getZaklads().stream().filter(uszl->uszl.getNazwaSkrot().equals(zak.getNazwaSkrot())).findFirst().orElse(null)!=null) {
					if(pzl.getRok().equals(rokWybrany))
						sumAll+=pzl.getIleGodzin();
				}
			}
		}
		return sumAll;
		
	}
	
	public String zakladToString(List<Zaklad> zl) {
		String zls = null;
		if(zl!=null && zl.size()>0) {
		for(Zaklad uz:zl) {
			zls+=uz.getNazwaSkrot()+", ";
		}
	return zls.substring(0, zls.length() -1);	
		}
		return "brak";
	}
	
	public void dodajWykladowcow() {
			userSave=new LinkedList<User>();
			HashSet<User> usss=new HashSet<User>();
			for(String idWyklad:selectedUserSpecjal) {
				 for(Specjalizacja spl:userSpecjal) {
					 User usadd=spl.getUserss().stream().filter(usf->usf.getIdUsers()==Integer.valueOf(idWyklad)).findFirst().orElse(null);
					 if(usadd!=null && !userSave.contains(usadd)) {
					 usss.add(usadd);
					 break;
					 }
				 }
			}
			if(usss.size()>0) {
			List<User>usServTemp=new ArrayList<User>(usss);
			userSave.addAll(usServTemp);
			}
			userAll=crudAll.getAllTerms("findAllUser");
			for(Planyzaklady pzl:selectedPrzedmiotPlan) {
				pzl.setUserss(userSave);
				crudAll.update(pzl);
				for(User us:userSave) {
				RejestryLogi rl=new RejestryLogi();
				crudAll.create(rl.zapiszLogi(dataDzisTimeStamp, "Uzytkownik-przypisano zajęcia","plany",us.getIdUsers(),pzl.getPrzedmiottemat().getPrzedmiot().getJm()+"/"+pzl.getPrzedmiottemat().getPrzedmiot().getJs()+"/"+pzl.getPrzedmiottemat().getNazwa()+" miesiąc: "+pzl.getMiesiac()+", rok: "+pzl.getRok(), "osoba"));
				rl=null;
				}
			}
			new MessagePlay("Zapisano.",null,FacesMessage.SEVERITY_INFO);
	}
	
	public void onRowEditOgraniczenie(RowEditEvent ev) {
		UserBlokadaPlany ogrGet=(UserBlokadaPlany) ev.getObject();
			
		crudAll.update(ogrGet);
		new MessagePlay("Uaktualniono!",null,FacesMessage.SEVERITY_INFO);

	}
	public List<Planyzaklady> selectedRow(){
		return selectedPrzedmiotPlan;
	}
	public void changeSpecjal() {
		selectedPrzedmiotPlan=null;
	}
	public void onRowEdit(RowEditEvent ev) {
		Planyzaklady plz=(Planyzaklady) ev.getObject();
		crudAll.update(plz);
		new MessagePlay("Zapisono zmiany: ",null,FacesMessage.SEVERITY_INFO);
	
	}
	public void zmienMiesiac() {
		session.setAttribute("miesiacWybrany", miesiacWybrany);
		session.setAttribute("rokWybrany", rokWybrany);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("wykladowcy.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}
		
	}
	public void zmienMiesiacPodsumowanie() {
		session.setAttribute("rokWybrany", rokWybrany);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("podsumowanie.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}
		
	}
	public void dopiszWykladowcow() {
		selectedUserSpecjal=null;
		userSpecjal=crudAll.getAllTerms("Specjalizacja.findAll");
		userSpecjalListSelect=new ArrayList<SelectItem>();
		Map<String,Specjalizacja> specjalTemp=new HashMap<String,Specjalizacja>();
		
		for(Planyzaklady ptl:selectedPrzedmiotPlan){
			 for(Specjalizacja spf:ptl.getPrzedmiottemat().getSpecjalizacjas()) {
				
			if(userSpecjal.stream().filter(usf->usf.getNazwa().contains(spf.getNazwa())).findAny().orElse(null)!=null) {
						specjalTemp.put(spf.getNazwa(),spf);
				}	
			 }
			}
	if(specjalTemp.size()>0) {
		for(Entry<String, Specjalizacja> spe:specjalTemp.entrySet()) {
			SelectItemGroup userSpec=new SelectItemGroup(spe.getKey());
			List<User> uss=spe.getValue().getUserss().stream().filter(usfi->usfi.getZablokowany().equals("NIE")).collect(Collectors.toList());
			
			if(uss!=null && uss.size()>0) {
						SelectItem[] sius=new SelectItem[uss.size()];
		
						for(int is=0;is<uss.size();is++) {
							sius[is]=new SelectItem(String.valueOf(uss.get(is).getIdUsers()),uss.get(is).getNazwiskoUsers());
							userSpec.setSelectItems(sius);
						}
					
						userSpecjalListSelect.add(userSpec);			
		}
	}
}
}

	public String wykladowcyToString(List<User> usl) {
		String uss = null;
		if(usl!=null && usl.size()>0) {
		for(User us:usl) {
			uss+=us.getImieUsers().substring(0,1)+". "+us.getNazwiskoUsers()+", ";
		}
	return uss.substring(0, uss.length() -1);	
		}
		return "brak";	
	}
	
	public void dodajBlokade() {
		User usAddBlok=userAll.stream().filter(usf->usf.getIdUsers()==userBlokada).findFirst().orElse(null);
		UserBlokadaPlany ubp=new UserBlokadaPlany();
		ubp.setCalyDzien(blokadaCalydzien);
		ubp.setDzienOd(usblokDatOd);
		ubp.setDzienDo(usblokDatDo);
		ubp.setKomentarz(komentarzBlok);
		ubp.setUsers(usAddBlok);
		crudAll.create(usAddBlok.addUserBlokadaPlany(ubp));
		usbp.add(ubp);
		RejestryLogi rl=new RejestryLogi();
		crudAll.create(rl.zapiszLogi(dataDzisTimeStamp, "Uzytkownik-załozono blokadę","plany", usAddBlok.getIdUsers(),"Wpisano blokadę w planach od: "+ubp.getDzienOd()+" do: "+ubp.getDzienDo()+", cały dzień: "+ubp.getCalyDzien(), "osoba"));
		rl=null;
	
		
	}
public void removeBlokada(ActionEvent e) {
	UserBlokadaPlany remusb=(UserBlokadaPlany) e.getComponent().getAttributes().get("removeRow");
				usbp.remove(remusb);
				crudAll.delete(remusb);
				
				RejestryLogi rl=new RejestryLogi();
				crudAll.create(rl.zapiszLogi(dataDzisTimeStamp, "Uzytkownik-usunięto blokadę","plany", remusb.getUsers().getIdUsers(),"Usunięto blokadę w planach od: "+remusb.getDzienOd()+" do: "+remusb.getDzienDo()+", cały dzień: "+remusb.getCalyDzien(), "osoba"));
				rl=null;
				
				new MessagePlay("Usunięto",null,FacesMessage.SEVERITY_WARN);
				remusb=null;
}
//-----------------------------------------------------------------getters setters----------------------------------------------------------------
	public List<Planyzaklady> getPlanyZakladyWykladowcy() {
		return planyZakladyWykladowcy;
	}

	public void setPlanyZakladyWykladowcy(List<Planyzaklady> planyZakladyWykladowcy) {
		this.planyZakladyWykladowcy = planyZakladyWykladowcy;
	}

	public Integer getMiesiacWybrany() {
		return miesiacWybrany;
	}

	public void setMiesiacWybrany(Integer miesiacWybrany) {
		this.miesiacWybrany = miesiacWybrany;
	}

	public Integer getRokWybrany() {
		return rokWybrany;
	}

	public void setRokWybrany(Integer rokWybrany) {
		this.rokWybrany = rokWybrany;
	}

	public Map<String, Integer> getMiesiace() {
		return miesiace;
	}

	public void setMiesiace(Map<String, Integer> miesiace) {
		this.miesiace = miesiace;
	}

	public Map<Integer, Integer> getLata() {
		return lata;
	}

	public void setLata(Map<Integer, Integer> lata) {
		this.lata = lata;
	}

	public Date getMiesiacRok() {
		return miesiacRok;
	}

	public void setMiesiacRok(Date miesiacRok) {
		this.miesiacRok = miesiacRok;
	}

	public List<Planyzaklady> getSelectedPrzedmiotPlan() {
		return selectedPrzedmiotPlan;
	}

	public void setSelectedPrzedmiotPlan(List<Planyzaklady> selectedPrzedmiotPlan) {
		this.selectedPrzedmiotPlan = selectedPrzedmiotPlan;
	}

	public Map<String,Serializable> getFilterValues() {
		return filterValues;
	}

	public void setFilterValues(Map<String,Serializable> filterValues) {
		this.filterValues = filterValues;
	}

	public List<String> getSpecjalFilterSelect() {
		return specjalFilterSelect;
	}

	public void setSpecjalFilterSelect(List<String> specjalFilterSelect) {
		this.specjalFilterSelect = specjalFilterSelect;
	}
	public List<Specjalizacja> getUserSpecjal() {
		return userSpecjal;
	}
	public void setUserSpecjal(List<Specjalizacja> userSpecjal) {
		this.userSpecjal = userSpecjal;
	}

	public String[] getSelectedUserSpecjal() {
		return selectedUserSpecjal;
	}
	public void setSelectedUserSpecjal(String[] selectedUserSpecjal) {
		this.selectedUserSpecjal = selectedUserSpecjal;
	}

	public List<SelectItem> getUserSpecjalListSelect() {
		return userSpecjalListSelect;
	}

	public void setUserSpecjalListSelect(List<SelectItem> userSpecjalListSelect) {
		this.userSpecjalListSelect = userSpecjalListSelect;
	}

	public List<User> getUserAll() {
		return userAll;
	}

	public void setUserAll(List<User> userAll) {
		this.userAll = userAll;
	}
	public boolean isOgraniczenia() {
		return ograniczenia;
	}
	public void setOgraniczenia(boolean ograniczenia) {
		this.ograniczenia = ograniczenia;
	}

	public boolean isBlokadaCalydzien() {
		return blokadaCalydzien;
	}
	public void setBlokadaCalydzien(boolean blokadaCalydzien) {
		this.blokadaCalydzien = blokadaCalydzien;
	}
	public Integer getUserBlokada() {
		return userBlokada;
	}
	public void setUserBlokada(Integer userBlokada) {
		this.userBlokada = userBlokada;
	}

	public Date getUsblokDatOd() {
		return usblokDatOd;
	}
	public void setUsblokDatOd(Date usblokDatOd) {
		this.usblokDatOd = usblokDatOd;
	}
	public Date getUsblokDatDo() {
		return usblokDatDo;
	}
	public void setUsblokDatDo(Date usblokDatDo) {
		this.usblokDatDo = usblokDatDo;
	}
	public String getKomentarzBlok() {
		return komentarzBlok;
	}
	public void setKomentarzBlok(String komentarzBlok) {
		this.komentarzBlok = komentarzBlok;
	}
	public List<Zaklad> getZakladAll() {
		return zakladAll;
	}
	public void setZakladAll(List<Zaklad> zakladAll) {
		this.zakladAll = zakladAll;
	}
	public Integer getMiesiacWybranyPodsumowanie() {
		return miesiacWybranyPodsumowanie;
	}
	public void setMiesiacWybranyPodsumowanie(Integer miesiacWybranyPodsumowanie) {
		this.miesiacWybranyPodsumowanie = miesiacWybranyPodsumowanie;
	}

}
