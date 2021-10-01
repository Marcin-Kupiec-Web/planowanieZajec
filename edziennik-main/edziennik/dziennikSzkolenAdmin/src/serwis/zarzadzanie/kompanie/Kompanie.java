package serwis.zarzadzanie.kompanie;

import java.io.Serializable;
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

import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Kompania;
import com.edziennik.sluchacze.zajecia.model.Pluton;

import my.util.MessagePlay;
import serwis.RejestryLogi.RejestryLogi;
@Named
@ViewScoped
public class Kompanie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Kompanie() {
		// TODO Auto-generated constructor stub
	}
	
	@EJB
		private CrudAll crudKompanie;
	@EJB
	private CrudAllLocal logi;
	 

		private List<Kompania> komp;
		private Date dataDzis;
		private Timestamp dataDzisTimeStamp;
		private boolean edycjaArch;
		private String archiwum;
		private String nazwaNowejKompanii;
		private boolean edycjaTabeli;
		FacesContext fct=FacesContext.getCurrentInstance();
		HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
		
	@PostConstruct
    public void init() {
		if(archiwum==null)
			archiwum="NIE";
		komp=new ArrayList<Kompania>();
		HashMap<String,Object>hmparamet=new HashMap<String, Object>();
		hmparamet.put("archiwum", archiwum);
		komp=crudKompanie.getAllTermsParam("Kompania.findAll",hmparamet);
	 	Calendar cal = Calendar.getInstance();  
	 	dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
	}
	
	public void rendExpTab() {
		String ed="";
		if(edycjaTabeli)
			ed="Edycja tabeli właczona";
			else
			ed="Edycja tabeli wyłaczona";
		new MessagePlay(ed.toString(),null,FacesMessage.SEVERITY_INFO);
	}
	
public void changeArche() {
			komp=null;
			if(edycjaArch) {
				setArchiwum("%");
				edycjaArch=true;
			}
			else {
				setArchiwum("NIE");
				edycjaArch=false;
			}
			HashMap<String,Object>hmparamet=new HashMap<String, Object>();
			hmparamet.put("archiwum", archiwum);
			komp=crudKompanie.getAllTermsParam("Kompania.findAll",hmparamet);
}
	public void addKompania() {
		if(komp.stream().filter(kom->kom.getNazwaKompania().equals(nazwaNowejKompanii)).findFirst().orElse(null)==null) {
				Kompania newKomp=new Kompania();
				newKomp.setNazwaKompania(nazwaNowejKompanii);
				newKomp.setArchiwum("NIE");
				newKomp.setDataUtowrzeniaKompania(dataDzisTimeStamp);
				komp.add(newKomp);
				crudKompanie.create(newKomp);
				
				RejestryLogi rl=new RejestryLogi();
				
				logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Utworzono nowa kompanię","administracja",newKomp.getIdKompania(), newKomp.getNazwaKompania(), "Kompania"));
				
				rl=null;
				new MessagePlay("Utworzono nowa kompanię: "+newKomp.getNazwaKompania(),null,FacesMessage.SEVERITY_INFO);
		}else
			new MessagePlay("Podana kompania juz istnieje! Nie zapisano. ",null,FacesMessage.SEVERITY_ERROR);
	}
	
	public void onRowEdit(RowEditEvent ev) {
		Kompania kompGet=(Kompania) ev.getObject();
		if(kompGet.getPlutons()!=null &&  kompGet.getPlutons().size()>0)
			for(Pluton pl:kompGet.getPlutons()) {
				pl.setArchiwum(kompGet.getArchiwum());
				crudKompanie.update(pl);
			}
		crudKompanie.update(kompGet);
		
		RejestryLogi rl=new RejestryLogi();
		
		logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Uaktualnienie","administracja",kompGet.getIdKompania(), kompGet.getNazwaKompania(), "Kompania"));
		
		rl=null;
		new MessagePlay("Zmieniono kompanię: "+kompGet.getIdKompania(),null,FacesMessage.SEVERITY_INFO);
		changeArche();
	}
	
	public void remove(ActionEvent e) {
				Kompania remKomp=(Kompania) e.getComponent().getAttributes().get("removeRow");
				if(remKomp.getArchiwum().equals("TAK")) {
						komp.remove(remKomp);
						crudKompanie.delete(remKomp);
						setArchiwum("%");
						RejestryLogi rl=new RejestryLogi();
						
						logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Usunięto","administracja",remKomp.getIdKompania(), remKomp.getNazwaKompania(), "Kompania"));
						
						rl=null;
						
						new MessagePlay("Usunięto kompanię: "+remKomp.getNazwaKompania(),null,FacesMessage.SEVERITY_WARN);
				}
				else {
					new MessagePlay("Kompania: "+remKomp.getNazwaKompania()+" musi być zarchiwizowana. Nie mogę usunać!",null,FacesMessage.SEVERITY_ERROR);
				}
				remKomp=null;
	}
//-------------------------------------------------------getters setters---------------------------------------------------------------------
	public List<Kompania> getKomp() {
		return komp;
	}

	public void setKomp(List<Kompania> komp) {
		this.komp = komp;
	}

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

	public String getNazwaNowejKompanii() {
		return nazwaNowejKompanii;
	}

	public void setNazwaNowejKompanii(String nazwaNowejKompanii) {
		this.nazwaNowejKompanii = nazwaNowejKompanii;
	}
	public boolean isEdycjaArch() {
		return edycjaArch;
	}
	public void setEdycjaArch(boolean edycjaArch) {
		this.edycjaArch = edycjaArch;
	}
	public String getArchiwum() {
		return archiwum;
	}
	public void setArchiwum(String archiwum) {
		this.archiwum = archiwum;
	}
	public boolean isEdycjaTabeli() {
		return edycjaTabeli;
	}
	public void setEdycjaTabeli(boolean edycjaTabeli) {
		this.edycjaTabeli = edycjaTabeli;
	}


	
}
