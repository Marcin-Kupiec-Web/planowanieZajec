package serwis.zarzadzanie.plutony;

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
import com.edziennik.sluchacze.zajecia.model.StrukturaKursu;

import my.util.MessagePlay;
import serwis.RejestryLogi.RejestryLogi;
@Named
@ViewScoped

public class Plutony implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Plutony() {
		// TODO Auto-generated constructor stub
	}
	
	@EJB
	private CrudAll crudPlutony;
	@EJB
	private CrudAllLocal logi;
	 

		private List<Pluton> pluto;
		private Date dataDzis;
		private Timestamp dataDzisTimeStamp;
		private String nazwaNowegoPlutonu;
		private String nazwaNowegoSzkolenia;
		private Date dataPlutonOd=new Date();
		private Date dataPlutonDo=new Date();
		private List<String> wyborKompanii;
		private List<String> strukturaKursu;
		private String wybranaKompania;
		private String wybranaStrukturaKursu;
		private String archiwum;
		private List<Kompania> komp;
		private List<StrukturaKursu> str;
		 private boolean edycjaArch;
		 private boolean edycjaTabeli;
		FacesContext fct=FacesContext.getCurrentInstance();
		HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
		
	@PostConstruct
    public void init() {
	 	Calendar cal = Calendar.getInstance();  
	 	setDataDzisTimeStamp(new Timestamp(cal.getTimeInMillis()));
	 	
		pluto=new ArrayList<Pluton>();
		HashMap<String,Object>hmparamet=new HashMap<String, Object>();
		hmparamet.put("archiwum", "NIE");
		pluto=crudPlutony.getAllTermsParam("Pluton.findAll", hmparamet);
		
		komp=crudPlutony.getAllTerms("Kompania.findAllall");
		str=crudPlutony.getAllTerms("StrukturaKursu.findAll");
		
		wyborKompanii=new ArrayList<String>();
		strukturaKursu=new ArrayList<String>();
		for(Kompania k:komp) {
			wyborKompanii.add(k.getNazwaKompania());
		}
		for(StrukturaKursu sk:str) {
			strukturaKursu.add(sk.getNazwa());
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
	
	public void changeArche() {
		if(edycjaArch)
			archiwum="%";
		else
			archiwum="NIE";
		HashMap<String,Object>hmparamet=new HashMap<String, Object>();
		hmparamet.put("archiwum",archiwum);
		pluto=crudPlutony.getAllTermsParam("Pluton.findAll", hmparamet);
	}
	public void addPluton() {
		if(pluto.stream().filter(fp->fp.getNazwaPluton().equals(nazwaNowegoPlutonu) && fp.getOznaczenieSzkolenia().equals(nazwaNowegoSzkolenia)).findFirst().orElse(null)==null)
		{
		Pluton newPlut=new Pluton();
		newPlut.setNazwaPluton(nazwaNowegoPlutonu);;
		newPlut.setArchiwum("NIE");
		StrukturaKursu strKs=str.stream().filter(strku->strku.getNazwa().equals(wybranaStrukturaKursu)).findFirst().orElse(null);
		newPlut.setIdKurs(strKs);
		newPlut.setKompania(komp.stream().filter(kompf->kompf.getNazwaKompania().equals(wybranaKompania)).findFirst().orElse(null));
		newPlut.setOznaczenieSzkolenia(nazwaNowegoSzkolenia);
		newPlut.setRodzajKursu(strKs.getKurs());
		newPlut.setSluchaczes(null);
		Calendar cal = Calendar.getInstance();  
		cal.setTime(dataPlutonOd);
		newPlut.setRokPluton(cal.get(Calendar.YEAR));
		newPlut.setTerminOd(dataPlutonOd);
		newPlut.setTerminDo(dataPlutonDo);
		newPlut.setTematies(null);
		newPlut.setWydarzenias(null);
		pluto.add(newPlut);
		crudPlutony.create(newPlut);
		
		
		RejestryLogi rl=new RejestryLogi();
		logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Utworzono nowy pluton","administracja", Integer.valueOf(newPlut.getIdPluton()), newPlut.getNazwaPluton(), "Pluton"));
		rl=null;
		new MessagePlay("Utworzono nowy pluton: "+newPlut.getNazwaPluton(),null,FacesMessage.SEVERITY_INFO);
		}else
			new MessagePlay("Pluton o podanej nazwie i podanymy oznaczeniu szkolenia juz istnieje! Nie zapisano!",null,FacesMessage.SEVERITY_ERROR);
	}
	public void onRowEdit(RowEditEvent ev) {
				Pluton plutGet=(Pluton) ev.getObject();
				Pluton plCheck=pluto.stream().filter(fp->fp.getNazwaPluton().equals(plutGet.getNazwaPluton()) && fp.getOznaczenieSzkolenia().equals(plutGet.getOznaczenieSzkolenia()) && fp.getIdPluton()!=plutGet.getIdPluton()).findAny().orElse(null);
				if(plCheck==null) {
				StrukturaKursu strkch=str.stream().filter(strc->strc.getNazwa().equals(plutGet.getIdKurs().getNazwa())).findFirst().orElse(null);
				Kompania kompch=komp.stream().filter(kompc->kompc.getNazwaKompania().equals(plutGet.getKompania().getNazwaKompania())).findFirst().orElse(null);
				if(strkch!=null) {
					plutGet.setIdKurs(strkch);
				}
				if(kompch!=null) {
					plutGet.setKompania(kompch);
				}
				
				crudPlutony.update(plutGet);
				RejestryLogi rl=new RejestryLogi();
				logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Uaktualniono pluton","administracja",Integer.valueOf(plutGet.getIdPluton()), plutGet.getNazwaPluton(), "Pluton"));
				rl=null;
				new MessagePlay("Uaktualniono pluton: "+plutGet.getNazwaPluton(),null,FacesMessage.SEVERITY_INFO);
				changeArche();
		}else
			new MessagePlay("Pluton o podanej nazwie i podanymy oznaczeniu szkolenia juz istnieje! Nie zapisano!",null,FacesMessage.SEVERITY_ERROR);
	}
	
	public void remove(ActionEvent e) {
				Pluton remPlut=(Pluton) e.getComponent().getAttributes().get("removeRow");
				
				if(remPlut.getArchiwum().equals("TAK")) {
					
					pluto.remove(remPlut);
					crudPlutony.delete(remPlut);
				
					RejestryLogi rl=new RejestryLogi();
					
					logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Usutnięto pluton","administracja",remPlut.getIdPluton(),"nazwa: "+remPlut.getNazwaPluton(), "Kurs"));
					
					rl=null;
					
					new MessagePlay("Usunięto pluton: "+remPlut.getNazwaPluton(),null,FacesMessage.SEVERITY_WARN);
					}else {
						new MessagePlay("Pluton: "+remPlut.getNazwaPluton()+" musi być zarchiwizowany. Nie mogę usunać!",null,FacesMessage.SEVERITY_ERROR);
					}
					remPlut=null;
	}
//---------------------------------------------GETTERS SETTERS-----------------------------------------------------------------
	public List<Pluton> getPluto() {
		return pluto;
	}

	public void setPluto(List<Pluton> pluto) {
		this.pluto = pluto;
	}

	public Timestamp getDataDzisTimeStamp() {
		return dataDzisTimeStamp;
	}

	public void setDataDzisTimeStamp(Timestamp dataDzisTimeStamp) {
		this.dataDzisTimeStamp = dataDzisTimeStamp;
	}

	public Date getDataDzis() {
		return dataDzis;
	}

	public void setDataDzis(Date dataDzis) {
		this.dataDzis = dataDzis;
	}



	public String getNazwaNowegoPlutonu() {
		return nazwaNowegoPlutonu;
	}



	public void setNazwaNowegoPlutonu(String nazwaNowegoPlutonu) {
		this.nazwaNowegoPlutonu = nazwaNowegoPlutonu;
	}

	public Date getDataPlutonOd() {
		return dataPlutonOd;
	}

	public void setDataPlutonOd(Date dataPlutonOd) {
		this.dataPlutonOd = dataPlutonOd;
	}

	public Date getDataPlutonDo() {
		return dataPlutonDo;
	}

	public void setDataPlutonDo(Date dataPlutonDo) {
		this.dataPlutonDo = dataPlutonDo;
	}

	public List<String> getWyborKompanii() {
		return wyborKompanii;
	}

	public void setWyborKompanii(List<String> wyborKompanii) {
		this.wyborKompanii = wyborKompanii;
	}

	public List<Kompania> getKomp() {
		return komp;
	}

	public void setKomp(List<Kompania> komp) {
		this.komp = komp;
	}

	public String getWybranaKompania() {
		return wybranaKompania;
	}

	public void setWybranaKompania(String wybranaKompania) {
		this.wybranaKompania = wybranaKompania;
	}

	public List<String> getStrukturaKursu() {
		return strukturaKursu;
	}

	public void setStrukturaKursu(List<String> strukturaKursu) {
		this.strukturaKursu = strukturaKursu;
	}

	public String getWybranaStrukturaKursu() {
		return wybranaStrukturaKursu;
	}

	public void setWybranaStrukturaKursu(String wybranaStrukturaKursu) {
		this.wybranaStrukturaKursu = wybranaStrukturaKursu;
	}

	public List<StrukturaKursu> getStr() {
		return str;
	}

	public void setStr(List<StrukturaKursu> str) {
		this.str = str;
	}

	public String getNazwaNowegoSzkolenia() {
		return nazwaNowegoSzkolenia;
	}

	public void setNazwaNowegoSzkolenia(String nazwaNowegoSzkolenia) {
		this.nazwaNowegoSzkolenia = nazwaNowegoSzkolenia;
	}

	public String getArchiwum() {
		return archiwum;
	}

	public void setArchiwum(String archiwum) {
		this.archiwum = archiwum;
	}

	public boolean isEdycjaArch() {
		return edycjaArch;
	}

	public void setEdycjaArch(boolean edycjaArch) {
		this.edycjaArch = edycjaArch;
	}

	public boolean isEdycjaTabeli() {
		return edycjaTabeli;
	}

	public void setEdycjaTabeli(boolean edycjaTabeli) {
		this.edycjaTabeli = edycjaTabeli;
	}
}
