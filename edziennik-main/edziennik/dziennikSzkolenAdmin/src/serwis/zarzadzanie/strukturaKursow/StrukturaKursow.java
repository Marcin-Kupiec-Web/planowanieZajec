package serwis.zarzadzanie.strukturaKursow;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.ToggleEvent;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sale.model.Sale;
import com.edziennik.sluchacze.zajecia.model.Przedmiot;
import com.edziennik.sluchacze.zajecia.model.Przedmiottemat;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.StrukturaKursu;
import com.edziennik.sluchacze.zajecia.model.Zaklad;

import my.util.MessagePlay;
import serwis.RejestryLogi.RejestryLogi;
@Named
@ViewScoped
public class StrukturaKursow implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StrukturaKursow() {
		// TODO Auto-generated constructor stub
	}
	
	@EJB
	private CrudAll crudKursy;
	@EJB
	private CrudAllLocal logi;
	
	private List<StrukturaKursu> strK;
	private String kurs;
	private String nazwa;
	private String jModol;
	private String jSzkol=null;
	private String kpn=null;
	private String komentarz;
	private Integer ileGodzin;
	private StrukturaKursu wybranyKurs;
	private Przedmiot wybranyPrzedmiot;
	private String modolPrzedmiot;
	private String nazwaPrzedmiot;
	private Integer ileGodzinPrzedmiot;
	private String komentarzPrzedmiot;
	private Timestamp dataDzisTimeStamp;
	private Map<String,Serializable> filterValues=new HashMap<>();
	private String[] selectedZaklad;
	private List<String> selectedZakladListString;
	private List<Zaklad> selectedZakladList;
	private List<Sale> saleList;
	private List<SelectItem> saleListGroup;
	private Integer korelacja;
	private String wybranaStrukturaKursu;
	private StrukturaKursu wybranaStrukturaObj;
	private List<Przedmiottemat> tematyAllFind;
	private String[] selectedSpecjal;
	private List<String> selectedSpecjalListString;
	private List<Specjalizacja> selectedSpecjalList;
	private String zakladWybranyFiltr;
	private String specjalWybranyFiltr;
	private String nazwaTemat;
	private String komentarzTemat;
	private Integer ileGodzinTemat;
	private Integer ileNauczyciel;
	private boolean edycjaTabeli;
	private String[] selectedSale;
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);

	@PostConstruct
    public void init() {
			strK=new ArrayList<StrukturaKursu>();
			strK=crudKursy.getAllTerms("StrukturaKursu.findAll");
			saleList=crudKursy.getAllTerms("Sale.findAll");
			
			selectedZakladList=new ArrayList<Zaklad>();
			selectedZakladList=crudKursy.getAllTerms("Zaklad.findAll");
			selectedZakladListString=new ArrayList<String>();
			
			for(Zaklad zk:selectedZakladList) {
					selectedZakladListString.add(zk.getNazwaSkrot());
			}
			
			selectedSpecjalList=new ArrayList<Specjalizacja>();
			selectedSpecjalList=crudKursy.getAllTerms("Specjalizacja.findAll");
			selectedSpecjalListString=new ArrayList<String>();
			
			saleListGroup=new ArrayList<SelectItem>();
			
			for(Specjalizacja sp:selectedSpecjalList) {
					selectedSpecjalListString.add(sp.getNazwa());
				
				
			}
			
			Map<String,List<Sale>> saleMap=new TreeMap<String,List<Sale>>();
		
			for(Sale slf:saleList) {
				String specjal=specjalToString(slf.getSpecjalizacjas());
			
			
				if(saleMap!=null && saleMap.get(specjal)!=null && saleMap.get(specjal).size()>0){
					saleMap.get(specjal).add(slf);
					saleMap.put(specjal, saleMap.get(specjal));
				}else {
					List<Sale> slAdd=new LinkedList<Sale>();
					slAdd.add(slf);
					saleMap.put(specjal, slAdd);
				}
			}
		
			for(Entry<String,List<Sale>> esl:saleMap.entrySet()) {
			
				SelectItemGroup seg=new SelectItemGroup(esl.getKey());
				SelectItem[] si=new SelectItem[esl.getValue().size()];
				int i=0;
					for(Sale sl:esl.getValue()) {
						si[i]=new SelectItem(sl.getNazwa(),sl.getNazwa());
						i++;
						
					}
					seg.setSelectItems(si);
					saleListGroup.add(seg);
			}
			
		Calendar cal = Calendar.getInstance();  
	 	dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
	}
	

	
	public String getTematInTable(Przedmiot prz,Przedmiottemat przt) {
		String tematRet=prz.getJm();
		if(prz.getJs()!=null)
			tematRet=tematRet+"/"+prz.getJs()+"/"+przt.getNazwa();
		return tematRet;
		
	}
	public String[] getSelectedZakladFromForm(List<Zaklad> selectedZakladList) {
			selectedZaklad=new String[selectedZakladList.size()];
			int i=0;
			for(Zaklad zk:selectedZakladList) {
					selectedZaklad[i]=zk.getNazwaSkrot();
					i++;
			}
		return selectedZaklad;
	}
	
	public String[] getSelectedSpecjalFromForm(List<Specjalizacja> selectedSpecjalList) {
		selectedSpecjal=new String[selectedSpecjalList.size()];
		int i=0;
		for(Specjalizacja sp:selectedSpecjalList) {
				selectedSpecjal[i]=sp.getNazwa();
				i++;
		}
	return selectedSpecjal;
}
	public void findPrzedmiotyAll(){
		if(wybranaStrukturaKursu!=null) {
		wybranaStrukturaObj=strK.stream().filter(skf->skf.getNazwa().equals(wybranaStrukturaKursu)).findFirst().orElse(null);
		
		HashMap<String, Object> hmsp=new HashMap<String,Object>();
		hmsp.put("idStrukturaKursu",wybranaStrukturaObj.getIdStrukturaKursu());
		tematyAllFind=crudKursy.getAllTermsParam("Przedmiottemat.wybranyPluton", hmsp);
		}
	}
	
	public Integer godzinyWpisane(List<Przedmiottemat> ptl) {
		int suma=0;
		if(ptl!=null)
		for(Przedmiottemat pt:ptl) {
			suma+=pt.getIleGodzin();
		}
		return suma;
	}
//---------------------------------------------------------------------------------struktura ogólnie wiersz1----------------------------------------------------	
	public void onRowEdit(RowEditEvent ev) {
				StrukturaKursu st=(StrukturaKursu) ev.getObject();
				if(st.getjSzkol().trim().length()==0) {
					jSzkol=null;
					st.setjSzkol(jSzkol);
				}
				if(st.getKpn().trim().length()==0) {
					kpn=null;
					st.setKpn(kpn);
				}
				
				
				crudKursy.update(st);
			
				RejestryLogi rl=new RejestryLogi();
				
				logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Uaktualniono kurs","administracja",st.getIdStrukturaKursu(), "nazwa: "+st.getNazwa(), "Kurs"));
				
				rl=null;
				new MessagePlay("Zminiono kurs: "+st.getKurs(),null,FacesMessage.SEVERITY_INFO);
	}
	
	public void onRowEditPrzedmiot(RowEditEvent evp) {
			Przedmiot stprz=(Przedmiot) evp.getObject();
			crudKursy.update(stprz);
		
			RejestryLogi rl=new RejestryLogi();
			
			logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Uaktualniono przedmiot","administracja",stprz.getIdPrzedmiot(), "modoł: "+stprz.getJm(), "Przedmiot"));
			
			rl=null;
			new MessagePlay("Zminiono przedmiot: "+stprz.getIdPrzedmiot(),null,FacesMessage.SEVERITY_INFO);
	}
	
	public void onRowEditPrzedmiotAll(RowEditEvent evp) {
		List<Sale> saleNewList=new ArrayList<Sale>();
		
		for(String zstr:selectedSale) {
			Sale sf=saleList.stream().filter(szf->szf.getNazwa().equals(zstr)).findFirst().orElse(null);
			if(sf!=null) {
				saleNewList.add(sf);
			}
		}
		
		Przedmiottemat stprz=(Przedmiottemat) evp.getObject();
		stprz.setSales(saleNewList);
		crudKursy.update(stprz);
	
		new MessagePlay("Zminiono!",null,FacesMessage.SEVERITY_INFO);
}
	
	public void remove(ActionEvent e) {
		StrukturaKursu remStr=(StrukturaKursu) e.getComponent().getAttributes().get("removeRow");
				if(remStr.getPlutons().size()==0) {
						strK.remove(remStr);
						crudKursy.delete(remStr);
					
						RejestryLogi rl=new RejestryLogi();
						
						logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Usutnięto kurs","administracja",remStr.getIdStrukturaKursu(),"nazwa: "+remStr.getNazwa(), "Kurs"));
						
						rl=null;
						
						new MessagePlay("Usunięto kurs: "+remStr.getKurs(),null,FacesMessage.SEVERITY_WARN);
				}else {
						new MessagePlay("Struktura kursu: "+remStr.getNazwa()+" przypisana jest do plutonu. Nie mogę usunać!",null,FacesMessage.SEVERITY_ERROR);
				}
				remStr=null;
	}

	
	public void addKurs() {
		if(kurs!=null && kurs.trim().length()>0 && jModol!=null && jModol.trim().length()>0 && ileGodzin!=null) {
			if(jSzkol.trim().length()==0)
				jSzkol=null;
			if(kpn.trim().length()==0)
				kpn=null;
					StrukturaKursu sk=new StrukturaKursu();
					sk.setKurs(kurs);
					sk.setNazwa(nazwa);
					sk.setIleGodzin(ileGodzin);
					sk.setjModol(jModol);
					sk.setKomentarz(komentarz);
					sk.setKtoWpisal((int) session.getAttribute("idUser"));
					sk.setjSzkol(jSzkol);
					sk.setKpn(kpn);
					sk.setPrzedmiots(null);
					strK.add(sk);
					crudKursy.create(sk);
					
					RejestryLogi rl=new RejestryLogi();
					
					logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Utworzono nowy kurs","administracja",sk.getIdStrukturaKursu(), "nazwa: "+sk.getNazwa(), "Kurs"));
					
					rl=null;
					
					try {
						FacesContext.getCurrentInstance().getExternalContext().redirect("strukturaKursow.xhtml");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}else
				new MessagePlay("Nazwa kursu, modół główny i liczba godzin nie moga być puste!",null,FacesMessage.SEVERITY_ERROR);
	}
	public void requestPrzedmiot(ActionEvent e) {
			StrukturaKursu addPrzedmiot=(StrukturaKursu) e.getComponent().getAttributes().get("addRow");
			wybranyKurs=addPrzedmiot;
	}
	public void requesTemat(ActionEvent e) {
			Przedmiot addPrzedmiot=(Przedmiot) e.getComponent().getAttributes().get("addRowp");
			wybranyPrzedmiot=addPrzedmiot;
			wybranyKurs=addPrzedmiot.getStrukturaKursu();
	}
//-----------------------------------------------------------------------przedmiot wiersz1_1-----------------------------------------------------------------------------------------------

	public void onRowTogle1(ToggleEvent te) {
		if(te.getVisibility().toString().equals("HIDDEN")) {
			wybranyPrzedmiot=null;
			wybranyKurs=null;
		};
		clearAllFilters("formKursy:kursy:przedmiot");
		clearAllFilters("formKursy:kursy:przedmiot:temats");
	}

	public void onRowTogle2(ToggleEvent te) {
		if(te.getVisibility().toString().equals("HIDDEN")) {
			wybranyPrzedmiot=null;
		};

	}
	public void removePrzedmiot(ActionEvent e) {
				Przedmiot remPrz=(Przedmiot) e.getComponent().getAttributes().get("removeRow");
				int ids=remPrz.getStrukturaKursu().getIdStrukturaKursu();
				strK.stream().filter(sk->sk.getIdStrukturaKursu() == ids).findFirst().orElse(null).getPrzedmiots().remove(remPrz);
				crudKursy.delete(remPrz);
				
				RejestryLogi rl=new RejestryLogi();
				
				logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Usunięto przedmiot","administracja",remPrz.getIdPrzedmiot(), "modoół: "+remPrz.getJm(), "Przedmiot"));
				
				rl=null;
				new MessagePlay("Usunięto przedmiot: "+remPrz.getIdPrzedmiot(),null,FacesMessage.SEVERITY_WARN);
				remPrz=null;
	}
	
	public void addPrzedmiot(ActionEvent e) {
		if(modolPrzedmiot!=null && modolPrzedmiot.trim().length()>0 && wybranyKurs!=null) {
					Przedmiot prz=new Przedmiot();
					prz.setJm(modolPrzedmiot);
					prz.setJs(nazwaPrzedmiot);
					prz.setKomentarz(komentarzPrzedmiot);
					prz.setIleGodzin(ileGodzinPrzedmiot);
					prz.setStrukturaKursu(wybranyKurs);
					prz.setPrzedmiottemats(null);
					crudKursy.create(prz);
					
					if(wybranyKurs.getPrzedmiots()!=null)
							wybranyKurs.getPrzedmiots().add(prz);
					else {
							List<Przedmiot>przdl=new ArrayList<Przedmiot>();
							wybranyKurs.setPrzedmiots(przdl);
					}
					
					RejestryLogi rl=new RejestryLogi();
					
					logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Utworzono nowy przedmiot","administracja",prz.getIdPrzedmiot(), "modół: "+prz.getJm(), "Przedmiot"));
					
					rl=null;
					
					new MessagePlay("Zapisano",null,FacesMessage.SEVERITY_INFO);
			}else
					new MessagePlay("Pola "+wybranyKurs.getjModol()+" oraz liczba godzin nie moga być puste!",null,FacesMessage.SEVERITY_ERROR);
	
	}

	public void dialogClose() {
		modolPrzedmiot=null;
		nazwaPrzedmiot=null;
		komentarzPrzedmiot=null;
		ileGodzinPrzedmiot=null;
		nazwa=null;
		kurs=null;
		jModol=null;
		jSzkol=null;
		kpn=null;
		komentarz=null;
		ileGodzin=null;
		selectedZaklad=null;
		selectedSpecjal=null;
		nazwaTemat=null;
		komentarzTemat=null;
		ileGodzinTemat=null;
		ileNauczyciel=null;
	}
	

//---------------------------------------------------------------------------tematy wiersz 1_1_1--------------------------------------------------------
	public void addTemat(ActionEvent e) {
		List<Zaklad> zakZapisList=new ArrayList<Zaklad>();
		List<Specjalizacja> zakSpecjalList=new ArrayList<Specjalizacja>();
		
		for(String zstr:selectedZaklad) {
					Zaklad zf=selectedZakladList.stream().filter(szf->szf.getNazwaSkrot().equals(zstr)).findFirst().orElse(null);
					if(zf!=null) {
								zakZapisList.add(zf);
					}
		}
		
		for(String zstrs:selectedSpecjal) {
			Specjalizacja zf=selectedSpecjalList.stream().filter(szf->(szf.getNazwa()).equals(zstrs)).findFirst().orElse(null);
			if(zf!=null) {
						zakSpecjalList.add(zf);
				}
		}
		
		List<Sale> saleNewList=new ArrayList<Sale>();
		
		for(String zstr:selectedSale) {
			Sale sf=saleList.stream().filter(szf->szf.getNazwa().equals(zstr)).findFirst().orElse(null);
			if(sf!=null) {
				saleNewList.add(sf);
			}
		}
		
		if(komentarzTemat!=null && nazwaTemat!=null && wybranyPrzedmiot!=null && wybranyPrzedmiot!=null) {
				Przedmiottemat przt=new Przedmiottemat();
				przt.setKomentarz(komentarzTemat);
				przt.setNazwa(nazwaTemat);
				przt.setPrzedmiot(wybranyPrzedmiot);
				przt.setIleGodzin(ileGodzinTemat);
				przt.setIleWykladowcow(ileNauczyciel);
				przt.setZaklads(zakZapisList);
				przt.setSpecjalizacjas(zakSpecjalList);
				przt.setSales(saleNewList);
				przt.setKorelacja(korelacja);
				crudKursy.create(przt);
				if(wybranyPrzedmiot.getPrzedmiottemats()!=null)
				wybranyPrzedmiot.getPrzedmiottemats().add(przt);
				else {
						List<Przedmiottemat> ptl=new ArrayList<Przedmiottemat>();
						ptl.add(przt);
						wybranyPrzedmiot.setPrzedmiottemats(ptl);
				}
			
				RejestryLogi rl=new RejestryLogi();
				
				logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Utworzono nowy temat. ","administracja",przt.getId_przedmiotTemat(), "Temat: "+przt.getNazwa()+", JM/JS:"+przt.getPrzedmiot().getJm()+"/"+przt.getPrzedmiot().getJs(), "Temat"));
				
				rl=null;
		}
		new MessagePlay("Zapisano",null,FacesMessage.SEVERITY_INFO);
		findPrzedmiotyAll();
	}
	
	public void removeTemat(ActionEvent e) {
				Przedmiottemat remTem=(Przedmiottemat) e.getComponent().getAttributes().get("removeRowt");
				Przedmiot remPrz=remTem.getPrzedmiot();
				remPrz.getPrzedmiottemats().remove(remTem);
				crudKursy.delete(remTem);
				
				RejestryLogi rl=new RejestryLogi();
				logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Usunięto temat","administracja",remTem.getId_przedmiotTemat(), "Temat: "+remTem.getNazwa()+", JM/JS:"+remTem.getPrzedmiot().getJm()+"/"+remTem.getPrzedmiot().getJs(), "Temat"));
				
				rl=null;
				new MessagePlay("Usunięto przedmiot: "+remPrz.getIdPrzedmiot(),null,FacesMessage.SEVERITY_WARN);
				remPrz=null;
				init();
				findPrzedmiotyAll();
	}
	
	public void onRowEditTemat(RowEditEvent evt) {
			
		List<Zaklad> zakZapisList=new ArrayList<Zaklad>();
		List<Specjalizacja> zakSpecjalList=new ArrayList<Specjalizacja>();
		
		for(String zstr:selectedZaklad) {
					Zaklad zf=selectedZakladList.stream().filter(szf->szf.getNazwaSkrot().equals(zstr)).findFirst().orElse(null);
					if(zf!=null) {
								zakZapisList.add(zf);
					}
		}
		
				for(String zstrs:selectedSpecjal) {
					Specjalizacja zf=selectedSpecjalList.stream().filter(szf->(szf.getNazwa()).equals(zstrs)).findFirst().orElse(null);
					if(zf!=null) {
								zakSpecjalList.add(zf);
					}
			}
				
				List<Sale> saleNewList=new ArrayList<Sale>();
				
				for(String zstr:selectedSale) {
					Sale sf=saleList.stream().filter(szf->szf.getNazwa().equals(zstr)).findFirst().orElse(null);
					if(sf!=null) {
						saleNewList.add(sf);
					}
				}
			
			Przedmiottemat sttem=(Przedmiottemat) evt.getObject();
			sttem.setZaklads(zakZapisList);
			sttem.setSpecjalizacjas(zakSpecjalList);
			sttem.setSales(saleNewList);
			crudKursy.update(sttem);
		
			RejestryLogi rl=new RejestryLogi();
			
			logi.create(rl.zapiszLogi(dataDzisTimeStamp, "Uaktualniono temat","administracja",sttem.getId_przedmiotTemat(),"Temat: "+sttem.getNazwa()+", JM/JS:"+sttem.getPrzedmiot().getJm()+"/"+sttem.getPrzedmiot().getJs(), "Temat"));
			
			rl=null;
			new MessagePlay("Uaktualniono temat: "+sttem.getNazwa(),null,FacesMessage.SEVERITY_INFO);
			findPrzedmiotyAll();
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


	
	public String saleToString(List<Sale> sl) {
		String sls ="";
		selectedSale=null;
		int i=0;
		if(sl!=null && sl.size()>0) {
			selectedSale=new String[sl.size()];
		for(Sale uz:sl) {
			sls+=uz.getNazwa()+", ";
			selectedSale[i]=uz.getNazwa();
			i++;
		}
		sls=sls.trim();
	return sls.substring(0, sls.length() -1);	
	
		}
		return "";
	}

	public void rendExpTab() {
		String ed="";
		if(edycjaTabeli)
			ed="Edycja tabeli właczona";
			else
			ed="Edycja tabeli wyłaczona";
		new MessagePlay(ed.toString(),null,FacesMessage.SEVERITY_INFO);
	}
//------------------------------------------------------------------------------czyści filtry------------------	
	public void clearAllFilters(String komponent) {
		filterValues.clear();
		DataTable datTab=(DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(komponent);
		if(datTab!=null) {
		datTab.reset();
		datTab.setValueExpression("sortBy", null);
		}
		findPrzedmiotyAll();
		}
	
	
//----------------------------------------------------------------------------gettery settery----------------------------------------------------------------
	public List<StrukturaKursu> getStrK() {
		return strK;
	}

	public void setStrK(List<StrukturaKursu> strK) {
		this.strK = strK;
	}

	public String getKurs() {
		return kurs;
	}

	public void setKurs(String kurs) {
		this.kurs = kurs;
	}


	public String getjModol() {
		return jModol;
	}

	public void setjModol(String jModol) {
		this.jModol = jModol;
	}

	public String getjSzkol() {
		return jSzkol;
	}

	public void setjSzkol(String jSzkol) {
		this.jSzkol = jSzkol;
	}

	public String getKpn() {
		return kpn;
	}

	public void setKpn(String kpn) {
		this.kpn = kpn;
	}

	public String getKomentarz() {
		return komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public Integer getIleGodzin() {
		return ileGodzin;
	}

	public void setIleGodzin(Integer ileGodzin) {
		this.ileGodzin = ileGodzin;
	}

	public StrukturaKursu getWybranyKurs() {
		return wybranyKurs;
	}

	public void setWybranyKurs(StrukturaKursu wybranyKurs) {
		this.wybranyKurs = wybranyKurs;
	}

	public String getModolPrzedmiot() {
		return modolPrzedmiot;
	}

	public void setModolPrzedmiot(String modolPrzedmiot) {
		this.modolPrzedmiot = modolPrzedmiot;
	}

	public String getNazwaPrzedmiot() {
		return nazwaPrzedmiot;
	}

	public void setNazwaPrzedmiot(String nazwaPrzedmiot) {
		this.nazwaPrzedmiot = nazwaPrzedmiot;
	}

	public Integer getIleGodzinPrzedmiot() {
		return ileGodzinPrzedmiot;
	}

	public void setIleGodzinPrzedmiot(Integer ileGodzinPrzedmiot) {
		this.ileGodzinPrzedmiot = ileGodzinPrzedmiot;
	}

	public String getKomentarzPrzedmiot() {
		return komentarzPrzedmiot;
	}

	public void setKomentarzPrzedmiot(String komentarzPrzedmiot) {
		this.komentarzPrzedmiot = komentarzPrzedmiot;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String[] getSelectedZaklad() {
		return selectedZaklad;
	}

	public void setSelectedZaklad(String[] selectedZaklad) {
		this.selectedZaklad = selectedZaklad;
	}

	public List<Zaklad> getSelectedZakladList() {
		return selectedZakladList;
	}

	public void setSelectedZakladList(List<Zaklad> selectedZakladList) {
		this.selectedZakladList = selectedZakladList;
	}

	public List<String> getSelectedZakladListString() {
		return selectedZakladListString;
	}

	public void setSelectedZakladListString(List<String> selectedZakladListString) {
		this.selectedZakladListString = selectedZakladListString;
	}

	public String getNazwaTemat() {
		return nazwaTemat;
	}

	public void setNazwaTemat(String nazwaTemat) {
		this.nazwaTemat = nazwaTemat;
	}
	public String getKomentarzTemat() {
		return komentarzTemat;
	}
	public void setKomentarzTemat(String komentarzTemat) {
		this.komentarzTemat = komentarzTemat;
	}
	public Integer getIleGodzinTemat() {
		return ileGodzinTemat;
	}
	public void setIleGodzinTemat(Integer ileGodzinTemat) {
		this.ileGodzinTemat = ileGodzinTemat;
	}
	public Przedmiot getWybranyPrzedmiot() {
		return wybranyPrzedmiot;
	}
	public void setWybranyPrzedmiot(Przedmiot wybranyPrzedmiot) {
		this.wybranyPrzedmiot = wybranyPrzedmiot;
	}
	public Integer getIleNauczyciel() {
		return ileNauczyciel;
	}
	public void setIleNauczyciel(Integer ileNauczyciel) {
		this.ileNauczyciel = ileNauczyciel;
	}

	public String[] getSelectedSpecjal() {
		return selectedSpecjal;
	}

	public void setSelectedSpecjal(String[] selectedSpecjal) {
		this.selectedSpecjal = selectedSpecjal;
	}

	public List<Specjalizacja> getSelectedSpecjalList() {
		return selectedSpecjalList;
	}

	public void setSelectedSpecjalList(List<Specjalizacja> selectedSpecjalList) {
		this.selectedSpecjalList = selectedSpecjalList;
	}

	public List<String> getSelectedSpecjalListString() {
		return selectedSpecjalListString;
	}

	public void setSelectedSpecjalListString(List<String> selectedSpecjalListString) {
		this.selectedSpecjalListString = selectedSpecjalListString;
	}

	public Map<String,Serializable> getFilterValues() {
		return filterValues;
	}

	public void setFilterValues(Map<String,Serializable> filterValues) {
		this.filterValues = filterValues;
	}

	public Integer getKorelacja() {
		return korelacja;
	}

	public void setKorelacja(Integer korelacja) {
		this.korelacja = korelacja;
	}

	public String getWybranaStrukturaKursu() {
		return wybranaStrukturaKursu;
	}

	public void setWybranaStrukturaKursu(String wybranaStrukturaKursu) {
		this.wybranaStrukturaKursu = wybranaStrukturaKursu;
	}

	public StrukturaKursu getWybranaStrukturaObj() {
		return wybranaStrukturaObj;
	}

	public void setWybranaStrukturaObj(StrukturaKursu wybranaStrukturaObj) {
		this.wybranaStrukturaObj = wybranaStrukturaObj;
	}

	public List<Przedmiottemat> getTematyAllFind() {
		return tematyAllFind;
	}

	public void setTematyAllFind(List<Przedmiottemat> tematyAllFind) {
		this.tematyAllFind = tematyAllFind;
	}

	public String getZakladWybranyFiltr() {
		return zakladWybranyFiltr;
	}

	public void setZakladWybranyFiltr(String zakladWybranyFiltr) {
		this.zakladWybranyFiltr = zakladWybranyFiltr;
	}

	public String getSpecjalWybranyFiltr() {
		return specjalWybranyFiltr;
	}

	public void setSpecjalWybranyFiltr(String specjalWybranyFiltr) {
		this.specjalWybranyFiltr = specjalWybranyFiltr;
	}

	public boolean isEdycjaTabeli() {
		return edycjaTabeli;
	}

	public void setEdycjaTabeli(boolean edycjaTabeli) {
		this.edycjaTabeli = edycjaTabeli;
	}
	public List<Sale> getSaleList() {
		return saleList;
	}
	public void setSaleList(List<Sale> saleList) {
		this.saleList = saleList;
	}
	public String[] getSelectedSale() {
		return selectedSale;
	}
	public void setSelectedSale(String[] selectedSale) {
		this.selectedSale = selectedSale;
	}



	public List<SelectItem> getSaleListGroup() {
		return saleListGroup;
	}



	public void setSaleListGroup(List<SelectItem> saleListGroup) {
		this.saleListGroup = saleListGroup;
	}
}
