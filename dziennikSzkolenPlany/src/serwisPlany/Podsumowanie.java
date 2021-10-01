package serwisPlany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.Zaklad;
import com.plany.model.Planyzaklady;
import com.plany.model.UserBlokadaPlany;
import com.userManager.model.User;

@Named
@ViewScoped
public class Podsumowanie implements Serializable{
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
	private Integer miesiacWybranyPodsumowanie;
	private Integer rokWybrany;
	private String[] selectedUserSpecjal;
	private Map<String,Serializable> filterValues=new HashMap<>(); 
	private List<User> userAll;
	private List<String> specjalFilterSelect;
	private boolean ograniczenia=false;
	private List<UserBlokadaPlany> usbp;
	private boolean blokadaCalydzien=false;
	private Integer userBlokada;
	private Date usblokDatOd;
	private Date usblokDatDo;
    private List<String> zakladyFilterSelect;
	public Integer getMiesiacWybranyPodsumowanie() {
		return miesiacWybranyPodsumowanie;
	}
	public void setMiesiacWybranyPodsumowanie(Integer miesiacWybranyPodsumowanie) {
		this.miesiacWybranyPodsumowanie = miesiacWybranyPodsumowanie;
	}
	private String komentarzBlok;
	private List<Zaklad> zakladAll;
	private List<Specjalizacja> specjalAll;
	private List<Planyzaklady> planyZakladyRok;
	public List<UserBlokadaPlany> getUsbp() {
		return usbp;
	}
	public void setUsbp(List<UserBlokadaPlany> usbp) {
		this.usbp = usbp;
	}
	public Podsumowanie() {
		// TODO Auto-generated constructor stub
	}
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
	
	@PostConstruct 
    public void init() {
		
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
		c.setTime(miesiacRok);
		if(rokWybrany!=null) {
			c.set(Calendar.YEAR, rokWybrany);
		} else
		setRokWybrany(c.get(Calendar.YEAR));
		setMiesiacWybranyPodsumowanie(c.get(Calendar.MONTH)+1);
		 for(int i=c.get(Calendar.YEAR)-4;i<c.get(Calendar.YEAR)+4;i++) {
			 lata.put(i, i);
		 }
	

				userAll=crudAll.getAllTerms("findAllUser");
				zakladAll=new ArrayList<Zaklad>();
				specjalAll=new ArrayList<Specjalizacja>();
				for(User usb:userAll) {
					for(Zaklad zul:usb.getZaklads()) {
						if(zakladAll.stream().filter(zf->zf.getNazwaSkrot().equals(zul.getNazwaSkrot())).findFirst().orElse(null)==null) {
							zakladAll.add(zul);
						}
					}
					for(Specjalizacja spl:usb.getSpecjalizacjas()) {
						if(specjalAll.stream().filter(zfs->zfs.getNazwa().equals(spl.getNazwa())).findFirst().orElse(null)==null) {
							specjalAll.add(spl);
						}
					}
				}
				HashMap<String, Object> hmsp=new HashMap<String,Object>();
				hmsp.put("rok",rokWybrany);
				planyZakladyRok=crudAll.getAllTermsParam("Planyzaklady.findPoRok", hmsp);
				
				zakladyFilterSelect=new LinkedList<String>();
				specjalFilterSelect=new LinkedList<String>();
				List<Zaklad>zl=new LinkedList<Zaklad>();
				List<Specjalizacja> sl=new LinkedList<Specjalizacja>();
				zl=crudAll.getAllTerms("Zaklad.findAll");
				sl=crudAll.getAllTerms("Specjalizacja.findAll");
				if(zl!=null && sl!=null) {
					for(Zaklad zakStr:zl) {
						zakladyFilterSelect.add(zakStr.getNazwaSkrot());
					}
					for(Specjalizacja specStr:sl) {
						specjalFilterSelect.add(specStr.getNazwa());
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
		if(zak!=null && planyZakladyRok!=null)
			for(Planyzaklady pzl:planyZakladyRok) {
						if(pzl.getPrzedmiottemat().getZaklads().stream().filter(uszl->uszl.getNazwaSkrot().equals(zak.getNazwaSkrot())).findFirst().orElse(null)!=null)
							if(pzl.getMiesiac()==miesiac)
							sumMiesiac+=pzl.getIleGodzin();
				}
		return sumMiesiac;
		
	}
	public int sumaGodzinMiesiacSpecjal(Specjalizacja spec, int miesiac) {
		int sumMiesiac=0;
		if(spec!=null && planyZakladyRok!=null)
			for(Planyzaklady pzl:planyZakladyRok) {
						if(pzl.getPrzedmiottemat().getSpecjalizacjas().stream().filter(uszl->uszl.getNazwa().equals(spec.getNazwa())).findFirst().orElse(null)!=null)
							if(pzl.getMiesiac()==miesiac)
							sumMiesiac+=pzl.getIleGodzin();
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
		if(zak!=null && planyZakladyRok!=null)
			for(Planyzaklady pzl:planyZakladyRok) {
						if(pzl.getPrzedmiottemat().getZaklads().stream().filter(uszl->uszl.getNazwaSkrot().equals(zak.getNazwaSkrot())).findFirst().orElse(null)!=null)
							sumAll+=pzl.getIleGodzin();
				}
		return sumAll;
		
	}
	public int sumaGodzinWszystkieSpecjal(Specjalizacja spec) {
		int sumAll=0;
	if(spec!=null && planyZakladyRok!=null)
			for(Planyzaklady pzl:planyZakladyRok) {
						if(pzl.getPrzedmiottemat().getSpecjalizacjas().stream().filter(uszl->uszl.getNazwa().equals(spec.getNazwa())).findFirst().orElse(null)!=null)
							sumAll+=pzl.getIleGodzin();
				}
		return sumAll;
	}
	
	public String zakladToString(List<Zaklad> zl) {
		String zls ="";
		if(zl!=null && zl.size()>0) {
		for(Zaklad uz:zl) {
			zls+=uz.getNazwaSkrot()+", ";
		}
		zls=zls.trim();
	return zls.substring(0, zls.length() -1);	
		}
		return "";
	}
	
	public String specjalToString(List<Specjalizacja> spec) {
		String sls ="";
		if(spec!=null && spec.size()>0) {
		for(Specjalizacja spl:spec) {
			sls+=spl.getNazwa()+", ";
		}
		sls=sls.trim();
	return sls.substring(0, sls.length() -1);	
		}
		return "";
	}
	public void zmienMiesiacPodsumowanie() {
		init();
	}


	public String wykladowcyToString(List<User> usl) {
		String uss="";
		if(usl!=null && usl.size()>0) {
		for(User us:usl) {
			uss+=us.getImieUsers().substring(0,1)+". "+us.getNazwiskoUsers()+", ";
		}
		uss=uss.trim();
	return uss.substring(0, uss.length() -1).trim();	
		}
		return "";	
	}


//-----------------------------------------------------------------getters setters----------------------------------------------------------------
	public List<Planyzaklady> getPlanyZakladyWykladowcy() {
		return planyZakladyWykladowcy;
	}

	public void setPlanyZakladyWykladowcy(List<Planyzaklady> planyZakladyWykladowcy) {
		this.planyZakladyWykladowcy = planyZakladyWykladowcy;
	}

	public Integer getMiesiacWybrany() {
		return miesiacWybranyPodsumowanie;
	}

	public void setMiesiacWybrany(Integer miesiacWybranyPodsumowanie) {
		this.miesiacWybranyPodsumowanie = miesiacWybranyPodsumowanie;
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
	public List<Specjalizacja> getSpecjalAll() {
		return specjalAll;
	}
	public void setSpecjalAll(List<Specjalizacja> specjalAll) {
		this.specjalAll = specjalAll;
	}
	public List<Planyzaklady> getPlanyZakladyRok() {
		return planyZakladyRok;
	}
	public void setPlanyZakladyRok(List<Planyzaklady> planyZakladyRok) {
		this.planyZakladyRok = planyZakladyRok;
	}
	public List<String> getZakladyFilterSelect() {
		return zakladyFilterSelect;
	}
	public void setZakladyFilterSelect(List<String> zakladyFilterSelect) {
		this.zakladyFilterSelect = zakladyFilterSelect;
	}


}
