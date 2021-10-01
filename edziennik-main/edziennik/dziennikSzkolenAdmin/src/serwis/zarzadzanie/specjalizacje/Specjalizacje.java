package serwis.zarzadzanie.specjalizacje;

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
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;

import my.util.MessagePlay;
import serwis.RejestryLogi.RejestryLogi;
@Named
@ViewScoped
public class Specjalizacje implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Specjalizacje() {
		// TODO Auto-generated constructor stub
	}
	
	@EJB
		private CrudAll crudSpecjal;
	@EJB
		private CrudAllLocal logi;
	 

		private List<Specjalizacja> specjalList;
		private Date dataDzis;
		private Timestamp dataDzisTimeStamp;
	
		private String komentarz;
		private String nazwa;
		private String uprawnienia;
		
		FacesContext fct=FacesContext.getCurrentInstance();
		HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
		
	@PostConstruct
    public void init() {
	
		specjalList=new ArrayList<Specjalizacja>();
		specjalList=crudSpecjal.getAllTerms("Specjalizacja.findAll");
	 	Calendar cal = Calendar.getInstance();  
	 	dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
	}

	public void addSpecjal() {
	  if(specjalList.stream().filter(spf->spf.getNazwa().equals(nazwa)).findFirst().orElse(null)==null) {
				Specjalizacja newSpecjal=new Specjalizacja();
				newSpecjal.setKomentarz(komentarz);
				newSpecjal.setNazwa(nazwa);
				specjalList.add(newSpecjal);
				crudSpecjal.create(newSpecjal);
				
				RejestryLogi rl=new RejestryLogi();
				
				logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Utworzono nowa specjalizację","administracja",newSpecjal.getIdSpecjalizacja(),newSpecjal.getNazwa()+"/"+newSpecjal.getIdSpecjalizacja(), "Specjalizacja"));
				
				rl=null;
				new MessagePlay("Utworzono nowa specjalizację: "+newSpecjal.getNazwa(),null,FacesMessage.SEVERITY_INFO);
	  }else
		  new MessagePlay("Podana specjalizacja z podanymi uprawnieniami - istnieje! Nie zapisano! ZMIEŃ NAZWĘ LUB UPRAWNIENIA",null,FacesMessage.SEVERITY_ERROR);
	}
	
	public void onRowEdit(RowEditEvent ev) {
		Specjalizacja specjalGet=(Specjalizacja) ev.getObject();
		crudSpecjal.update(specjalGet);
		
		RejestryLogi rl=new RejestryLogi();
		
		logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Uaktualnienie","administracja", specjalGet.getIdSpecjalizacja(), specjalGet.getNazwa()+"/"+specjalGet.getKomentarz(), "Specjalizacja"));
		
		rl=null;
		new MessagePlay("Uaktualniono specjalizację: "+specjalGet.getNazwa(),null,FacesMessage.SEVERITY_INFO);
	}
	
	public void remove(ActionEvent e) {
				Specjalizacja remSpecjal=(Specjalizacja) e.getComponent().getAttributes().get("removeRow");
				if(remSpecjal.getPrzedmiottemats()==null || remSpecjal.getPrzedmiottemats().size()==0) {
						specjalList.remove(remSpecjal);
						crudSpecjal.delete(remSpecjal);
						RejestryLogi rl=new RejestryLogi();
						
						logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Usunięto","administracja", remSpecjal.getIdSpecjalizacja(), remSpecjal.getNazwa()+"/"+remSpecjal.getKomentarz(), "Specjalizacja"));
						
						rl=null;
						
						new MessagePlay("Usunięto specjalizację: "+remSpecjal.getNazwa(),null,FacesMessage.SEVERITY_WARN);
				}else
					new MessagePlay("Specjalizacja ma przypisane przedmioty! Nie usunięto specjalizacji: "+remSpecjal.getNazwa(),null,FacesMessage.SEVERITY_ERROR);
				remSpecjal=null;
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



	public String getKomentarz() {
		return komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public List<Specjalizacja> getSpecjalList() {
		return specjalList;
	}

	public void setSpecjalList(List<Specjalizacja> specjalList) {
		this.specjalList = specjalList;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getUprawnienia() {
		return uprawnienia;
	}

	public void setUprawnienia(String uprawnienia) {
		this.uprawnienia = uprawnienia;
	}
}
