package serwis.zarzadzanie.dniWolne;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.dniWolne.model.Dniwolne;
import my.util.MessagePlay;

@Named
@ViewScoped
public class WolneDni implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CrudAllLocal dwolne;
	private List<Dniwolne>dwl;
	private String komentarz;
	private String[]miesiace= {"Styczeń","Luty","Marzec","Kwiecień","Maj","Czerwiec","Lipiec","Sierpień","Wrzesień","Pazdziernik","Listopad","Grudzień"};
	public WolneDni() {
		// TODO Auto-generated constructor stub
	}
	private Map<Integer,Integer>lata;
	private Integer rokWybrany;
	private Date dw;
	private int ileDniPracujacych;
	@PostConstruct
    public void init() {
		Calendar c = Calendar.getInstance();
		setRokWybrany(c.get(Calendar.YEAR));
		
		lata=new HashMap<Integer,Integer>();
		 for(int i=rokWybrany-4;i<rokWybrany+4;i++) {
			 lata.put(i, i);
		 }
		 
		dw=new Date();
		HashMap<String, Object> hm=new HashMap<String, Object>();
		hm.put("rok",rokWybrany);
		dwl=dwolne.getAllTermsParam("Dniwolne.findAllinYear", hm);
	}

	public void changeYear() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, rokWybrany);
		dw=c.getTime();
		HashMap<String, Object> hm=new HashMap<String, Object>();
		hm.put("rok",rokWybrany);
		dwl=dwolne.getAllTermsParam("Dniwolne.findAllinYear", hm);
		new MessagePlay("Zmieniono rok. ",null,FacesMessage.SEVERITY_INFO);
	}
	
   public void addDzien() {
	   Dniwolne dwnew=new Dniwolne();
	   Calendar cal=Calendar.getInstance();
	   
	   cal.setTime(dw);
	   dwnew.setDzien(dw);
	   dwnew.setKomentarz(komentarz);
	   dwnew.setMiesiac(cal.get(Calendar.MONTH));
	   dwnew.setRok(rokWybrany);
		SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
		if(dwl.stream().filter(dwf->format.format(dwf.getDzien()).equals(format.format(dwnew.getDzien()))).findFirst().orElse(null)==null) {
			dwl.add(dwnew);
			dwolne.create(dwnew);
			new MessagePlay("Zapisano dzień wolny: "+format.format(dwnew.getDzien()),null,FacesMessage.SEVERITY_INFO);
		}else
			new MessagePlay("Dzień: "+format.format(dwnew.getDzien())+" jest juz zarejestrowany jako wolny. ",null,FacesMessage.SEVERITY_ERROR);
   }
	
   public void dodajDzienWolnyAuto(){
		
		Calendar startCal=Calendar.getInstance();
	
		for(int im=0;im<12;im++) {
		startCal.setTime(dw);
		startCal.set(Calendar.MONTH, im);
		startCal.set(Calendar.YEAR, rokWybrany);
		startCal.set(Calendar.DAY_OF_MONTH,startCal.getActualMinimum(Calendar.DAY_OF_MONTH));
		int ileWmiesiacuDni=startCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int ileWeekend=0;
		setIleDniPracujacych(0);
		int nrDniaWtygodniu=startCal.get(Calendar.DAY_OF_WEEK);
		
		for(int i=1;i<ileWmiesiacuDni;i++) {
			
			if(nrDniaWtygodniu==8) {
				nrDniaWtygodniu=1;
			}
			if(nrDniaWtygodniu==7 || nrDniaWtygodniu==1) {
				ileWeekend++;
				startCal.set(Calendar.DAY_OF_MONTH, i);
				Date dat=startCal.getTime();
				SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
				if(dwl.stream().filter(dwf->format.format(dwf.getDzien()).equals(format.format(dat))).findFirst().orElse(null)==null)
				{
				Dniwolne dwsave=new Dniwolne();
				dwsave.setDzien(startCal.getTime());
				dwsave.setKomentarz("Weekend");
				dwsave.setMiesiac(im);
				dwsave.setRok(rokWybrany);
				dwl.add(dwsave);
				dwolne.create(dwsave);
				}
			}
			nrDniaWtygodniu++;
		}
		setIleDniPracujacych(ileWmiesiacuDni-ileWeekend);
		}
		new MessagePlay("Uaktualniono wolne weekendy. ",null,FacesMessage.SEVERITY_INFO);
		

	}
	
	public void onRowEdit(RowEditEvent ev) {
		Dniwolne dwGet=(Dniwolne) ev.getObject();
		SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
		dwolne.update(dwGet);
		new MessagePlay("Uaktualniono dzień: "+format.format(dwGet.getDzien()),null,FacesMessage.SEVERITY_INFO);

}
	public void remove(ActionEvent e) {
		Dniwolne remDw=(Dniwolne) e.getComponent().getAttributes().get("removeRow");
		
			dwl.remove(remDw);
			dwolne.delete(remDw);
			new MessagePlay("Usunięto dzień wolny: "+remDw.getDzien(),null,FacesMessage.SEVERITY_WARN);
		
			remDw=null;
}
//-------------------------------------------------------- getters setters ----------------------------------------------------
	public Date getDw() {
		return dw;
	}

	public void setDw(Date dw) {
		this.dw = dw;
	}

	public List<Dniwolne> getDwl() {
		return dwl;
	}

	public void setDwl(List<Dniwolne> dwl) {
		this.dwl = dwl;
	}
	public String[] getMiesiace() {
		return miesiace;
	}
	public void setMiesiace(String[] miesiace) {
		this.miesiace = miesiace;
	}
	public String getKomentarz() {
		return komentarz;
	}
	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public int getIleDniPracujacych() {
		return ileDniPracujacych;
	}

	public void setIleDniPracujacych(int ileDniPracujacych) {
		this.ileDniPracujacych = ileDniPracujacych;
	}

	public Map<Integer,Integer> getLata() {
		return lata;
	}

	public void setLata(Map<Integer,Integer> lata) {
		this.lata = lata;
	}

	public Integer getRokWybrany() {
		return rokWybrany;
	}

	public void setRokWybrany(Integer rokWybrany) {
		this.rokWybrany = rokWybrany;
	}
}
