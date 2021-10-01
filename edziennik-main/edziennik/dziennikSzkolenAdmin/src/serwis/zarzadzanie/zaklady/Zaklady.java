package serwis.zarzadzanie.zaklady;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Zaklad;

import my.util.MessagePlay;
import serwis.RejestryLogi.RejestryLogi;
@Named
@ViewScoped
public class Zaklady implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Zaklady() {
		// TODO Auto-generated constructor stub
	}
	
	@EJB
		private CrudAll crudZaklady;
	@EJB
	private CrudAllLocal logi;
	 

		private List<Zaklad> zakladList;
		private Date dataDzis;
		private Timestamp dataDzisTimeStamp;
	
		private String nazwaNowegoZakladu;
		private String nazwaPelnaZakladu;
		private String usuniety;
		private String komentarz;
	



		FacesContext fct=FacesContext.getCurrentInstance();
		HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
		
	@PostConstruct
    public void init() {
	
		zakladList=new ArrayList<Zaklad>();
		zakladList=crudZaklady.getAllTerms("Zaklad.findAll");
	 	Calendar cal = Calendar.getInstance();  
	 	dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
	}

	public void addZaklad() {
		if(zakladList.stream().filter(zakf->zakf.getNazwaSkrot().equals(nazwaNowegoZakladu)).findFirst().orElse(null)==null) {
				Zaklad newZaklad=new Zaklad();
				newZaklad.setNazwaSkrot(nazwaNowegoZakladu);
				newZaklad.setNazwaPelna(nazwaPelnaZakladu);
				newZaklad.setKomentarz(komentarz);
				newZaklad.setUsuniety(usuniety);
				zakladList.add(newZaklad);
				crudZaklady.create(newZaklad);
				
				RejestryLogi rl=new RejestryLogi();
				
				logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Utworzono nowy zaklad.","administracja",newZaklad.getIdZaklad(), newZaklad.getNazwaPelna()+"/"+newZaklad.getNazwaSkrot(), "Zakład"));
				
				rl=null;
				new MessagePlay("Utworzono nowy zaklad: "+newZaklad.getNazwaPelna(),null,FacesMessage.SEVERITY_INFO);
		}else
			new MessagePlay("Podany zakład juz istnieje! Nie zapisano. ",null,FacesMessage.SEVERITY_ERROR);
	}
	
	public void onRowEdit(RowEditEvent ev) {
		Zaklad zakGet=(Zaklad) ev.getObject();
		crudZaklady.update(zakGet);
		
		RejestryLogi rl=new RejestryLogi();
		
		logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Uaktualnienie","administracja",zakGet.getIdZaklad(), zakGet.getNazwaPelna()+"/"+zakGet.getNazwaSkrot(), "Zakład"));
		
		rl=null;
		new MessagePlay("Uaktualniono zakład: "+zakGet.getNazwaPelna()+"("+zakGet.getNazwaSkrot()+")",null,FacesMessage.SEVERITY_INFO);

	}
	
	public void remove(ActionEvent e) {
				Zaklad remZak=(Zaklad) e.getComponent().getAttributes().get("removeRow");
				if(remZak.getPrzedmiottemats()==null || remZak.getPrzedmiottemats().size()==0) {
						zakladList.remove(remZak);
						crudZaklady.delete(remZak);
						RejestryLogi rl=new RejestryLogi();
						
						logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Usunięto","administracja",remZak.getIdZaklad(), remZak.getNazwaPelna()+"/"+remZak.getNazwaSkrot(), "Zakład"));
						
						rl=null;
						
						new MessagePlay("Usunięto zaklad: "+remZak.getNazwaPelna(),null,FacesMessage.SEVERITY_WARN);
				}else
					new MessagePlay("Zakład ma przypisane przedmioty! Nie usunięto zakładu: "+remZak.getNazwaPelna(),null,FacesMessage.SEVERITY_ERROR);
				remZak=null;
	}
//-------------------------------------------------------getters setters---------------------------------------------------------------------
	

	public Date getDataDzis() {
		return dataDzis;
	}

	public void setDataDzis(Date dataDzis) {
		this.dataDzis = dataDzis;
	}

	public Timestamp getDataDzisTimeStamp() {
		return dataDzisTimeStamp;
	}

	public void setDataDzisTimeStamp(Timestamp dataDzisTimeStamp) {
		this.dataDzisTimeStamp = dataDzisTimeStamp;
	}

	public String getNazwaPelnaZakladu() {
		return nazwaPelnaZakladu;
	}

	public void setNazwaPelnaZakladu(String nazwaPelnaZakladu) {
		this.nazwaPelnaZakladu = nazwaPelnaZakladu;
	}

	public String getKomentarz() {
		return komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}
	public List<Zaklad> getZakladList() {
		return zakladList;
	}

	public void setZakladList(List<Zaklad> zakladList) {
		this.zakladList = zakladList;
	}

	public String getNazwaNowegoZakladu() {
		return nazwaNowegoZakladu;
	}

	public void setNazwaNowegoZakladu(String nazwaNowegoZakladu) {
		this.nazwaNowegoZakladu = nazwaNowegoZakladu;
	}

	public String getUsuniety() {
		return usuniety;
	}

	public void setUsuniety(String usuniety) {
		this.usuniety = usuniety;
	}
}
