package serwisPlany;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.dniWolne.model.Dniwolne;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Przedmiottemat;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.StrukturaKursu;
import com.edziennik.sluchacze.zajecia.model.Zaklad;
import com.plany.model.Planyzaklady;
import com.userManager.model.User;

import my.util.MessagePlay;

@Named
@ViewScoped
public class PlanyZaklad  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CrudAllLocal crudAll;
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
    
   private List<Planyzaklady> planyZakladyPluton;
   private List<Przedmiottemat> przedmiottematAllPluton;
   private List<Przedmiottemat> selectedPrzedmiot;
   private List<Planyzaklady> selectedPrzedmiotZaplan;
   private Integer ileSelectedPrzedmiot=0;
   private Integer ileSelectedPrzedmiotZap=0;
   private List<Pluton> plutonyWybor;
   private Map<String,String> szkolenieSelectList;
   private Map<String,String> kompaniaSelectList;
   private Map<String,String> plutonSelectList;
   private Map<String,String> kompaniaSelectListImport;
   private Map<String,String> plutonSelectListImport;
   private Map<String,Integer>miesiace;
   private Map<Integer,Integer>lata;
   private Map<String,Map<String,Map<String,String>>> kalkulatorZajec;
   private Map<String,Map<String,String>> kalkulatorZajecPodsumowanie;
   private String widokAnalizy="specjalizacja";
   private LocalDate dataStartSzkolenie;
   private LocalDate dataEndSzkolenie;
   private Map<String,Map<String,Integer>> miesiaceDniPracujace;
   private Integer wszystkieGodziny;
   private Integer wszystkieDni;
   private Map<String,Integer> wszystkieGodzinyGrupy=new HashMap<String,Integer>();
   private Integer miesiacWybrany;
   private Integer rokWybrany;
   private String szkolenieOneSelect;
   private Date miesiacRok;
   private String kompaniaOneSelect;
   private String plutonOneSelect;
   private String kompaniaOneSelectImport;
   private String plutonOneSelectImport;
   private Pluton daneDoSzkolenia;
   private Pluton planyPlutonuImport;
   private Map<String,Serializable> filterValues=new HashMap<>();
   private List<String> zakladyFilterSelect;
   private List<String> specjalFilterSelect;
   private StrukturaKursu strk;
   private List<Dniwolne> dniWolne;
   private Map<String,String>dniPracujaceMap;
   private Integer plutonOneSelectImportMiesiac;
	@PostConstruct 
    public void init() {
		
		FacesContext fct=FacesContext.getCurrentInstance();
		HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
		miesiace=new LinkedHashMap<String,Integer>();
		lata=new HashMap<Integer,Integer>();
		miesiace.put("Styczeń",0);
		miesiace.put("Luty",1);
		miesiace.put("Marzec",2);
		miesiace.put("Kwiecień",3);
		miesiace.put("Maj",4);
		miesiace.put("Czerwiec",5);
		miesiace.put("Lipiec",6);
		miesiace.put("Sierpień",7);
		miesiace.put("Wrzesień",8);
		miesiace.put("Pazdziernik",9);
		miesiace.put("Listopad",10);
		miesiace.put("Grudzeń",11);
		
		miesiacRok=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(miesiacRok);
		if(rokWybrany==null)
		rokWybrany=c.get(Calendar.YEAR);
		 for(int i=c.get(Calendar.YEAR)-4;i<c.get(Calendar.YEAR)+4;i++) {
			 lata.put(i, i);
		 }
		 
				plutonyWybor=new ArrayList<Pluton>();
				szkolenieSelectList=new TreeMap<String,String>();
			
				HashMap<String, Object> hmsp=new HashMap<String,Object>();
				hmsp.put("archiwum","NIE");
				plutonyWybor=crudAll.getAllTermsParam("Pluton.findSzkolenia", hmsp);
				
				kompaniaSelectList=new TreeMap<String,String>();
				plutonSelectList=new TreeMap<String,String>();
				kompaniaSelectListImport=new TreeMap<String,String>();
				plutonSelectListImport=new TreeMap<String,String>();
				
				if(session.getAttribute("szkolenieOneSelect")!=null)
					szkolenieOneSelect=(String) session.getAttribute("szkolenieOneSelect");
				if(session.getAttribute("kompaniaOneSelect")!=null) {
					kompaniaOneSelect=(String) session.getAttribute("kompaniaOneSelect");
					kompaniaOneSelectImport=(String) session.getAttribute("kompaniaOneSelect");
				}
				if(session.getAttribute("plutonOneSelect")!=null)
					plutonOneSelect=(String) session.getAttribute("plutonOneSelect");
				
				if(plutonyWybor!=null) {
					
				for(Pluton pls:plutonyWybor) {
					szkolenieSelectList.put(pls.getOznaczenieSzkolenia(),pls.getOznaczenieSzkolenia());
				
					if(session.getAttribute("szkolenieOneSelect")!=null) {
					
						if(pls.getOznaczenieSzkolenia().equals(session.getAttribute("szkolenieOneSelect"))) {
							kompaniaSelectList.put(pls.getKompania().getNazwaKompania(),pls.getKompania().getNazwaKompania());
							}
						if(session.getAttribute("kompaniaOneSelect")!=null) {
							if(pls.getOznaczenieSzkolenia().equals(session.getAttribute("szkolenieOneSelect")) && pls.getKompania().getNazwaKompania().equals(session.getAttribute("kompaniaOneSelect")))
								plutonSelectList.put(pls.getNazwaPluton(),pls.getNazwaPluton());
							}
							if(pls.getOznaczenieSzkolenia().equals(session.getAttribute("szkolenieOneSelect"))) {
								kompaniaSelectListImport.put(pls.getKompania().getNazwaKompania(),pls.getKompania().getNazwaKompania());
								}
						
								if(pls.getOznaczenieSzkolenia().equals(session.getAttribute("szkolenieOneSelect")) && kompaniaOneSelectImport!=null && pls.getKompania().getNazwaKompania().equals(kompaniaOneSelectImport) && !pls.getNazwaPluton().equals(plutonOneSelect))
									plutonSelectListImport.put(pls.getNazwaPluton(),pls.getNazwaPluton());
					}
				}
			}
				if(session.getAttribute("szkolenieOneSelect")!=null && session.getAttribute("kompaniaOneSelect")!=null && session.getAttribute("plutonOneSelect")!=null) 
					znajdzSzkolenie();
					ileDoplanowac();
  }
	
	public void nowyPlutonRedirect() throws IOException {

		FacesContext.getCurrentInstance().getExternalContext().redirect("zaklady.xhtml");
	}
	
	public void importPlanInnyPlut() {
		 ileSelectedPrzedmiot=0;
		 setPlanyPlutonuImport(null);
		 Integer miesWybr=null;
		if(plutonOneSelectImportMiesiac!=null)
			miesWybr=plutonOneSelectImportMiesiac+1;
			
		if(plutonOneSelectImport!=null)
		setPlanyPlutonuImport(plutonyWybor.stream().filter(plf->plf.getOznaczenieSzkolenia().equals(session.getAttribute("szkolenieOneSelect")) && plf.getNazwaPluton().equals(plutonOneSelectImport) && plf.getKompania().getNazwaKompania().equals(kompaniaOneSelectImport)).findFirst().orElse(null));
          
		if(planyPlutonuImport!=null) {
            	
            for(Planyzaklady pzr:daneDoSzkolenia.getPlanyZakladys()) {
            	if(miesWybr !=null && miesWybr>0)
            	{
            		if(pzr.getMiesiac()==miesWybr) {
            			crudAll.delete(pzr);
            			
            		}
            	}else
            	crudAll.delete(pzr);
            }
            
            daneDoSzkolenia.setPlanyZakladys(new ArrayList<Planyzaklady>());
            	for(Planyzaklady pzl:planyPlutonuImport.getPlanyZakladys()) {
            		Planyzaklady pzs=new Planyzaklady();
            		pzs.setIleGodzin(pzl.getIleGodzin());
            		pzs.setKomentarz(pzl.getKomentarz());
            		pzs.setMiesiac(pzl.getMiesiac());
            		pzs.setPrzedmiottemat(pzl.getPrzedmiottemat());
            		pzs.setRok(pzl.getRok());
            		pzs.setDataWpisu(miesiacRok);
            		pzs.setPluton(daneDoSzkolenia);
            		pzs.setWpisal((Integer) session.getAttribute("idUser"));
            		daneDoSzkolenia.addPlanyzaklady(pzs);
            		if(miesWybr !=null && miesWybr>0)
                	{
            			if(pzl.getMiesiac()==miesWybr) {
            				crudAll.create(pzs);
            			}
                	}else
                		crudAll.create(pzs);          	
            	
            	}
            	
            	new MessagePlay("Import zakończył się powodzeniem.",null,FacesMessage.SEVERITY_INFO);
            	init();
            }
	}
	public void znajdzSzkolenie() {
		if(session.getAttribute("szkolenieOneSelect")!=null && session.getAttribute("kompaniaOneSelect")!=null && session.getAttribute("plutonOneSelect")!=null) {
		setDaneDoSzkolenia(plutonyWybor.stream().filter(plf->plf.getOznaczenieSzkolenia().equals(session.getAttribute("szkolenieOneSelect")) && plf.getNazwaPluton().equals(session.getAttribute("plutonOneSelect")) && plf.getKompania().getNazwaKompania().equals(session.getAttribute("kompaniaOneSelect"))).findFirst().orElse(null));
		strk=daneDoSzkolenia.getIdKurs();
		
				List<Zaklad>zl=new LinkedList<Zaklad>();
				List<Specjalizacja> sl=new LinkedList<Specjalizacja>();
				planyZakladyPluton=new ArrayList<Planyzaklady>();
				zakladyFilterSelect=new LinkedList<String>();
				specjalFilterSelect=new LinkedList<String>();
				wszystkieGodziny=daneDoSzkolenia.getIdKurs().getIleGodzin();
				
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
				
				dniWolne=crudAll.getAllTerms("Dniwolne.findAll");
				HashMap<String, Object> hmsp=new HashMap<String,Object>();
				hmsp.put("idStrukturaKursu",daneDoSzkolenia.getIdKurs().getIdStrukturaKursu());
				setPrzedmiottematAllPluton(crudAll.getAllTermsParam("Przedmiottemat.wybranyPluton", hmsp));
				
				HashMap<String, Object> hmsp2=new HashMap<String,Object>();
				hmsp2.put("idPluton",daneDoSzkolenia.getIdPluton());
				
				setPlanyZakladyPluton(crudAll.getAllTermsParam("Planyzaklady.findPoIdPlutonu", hmsp2));
			
				
				miesiaceDniPracujace=new LinkedHashMap<String,Map<String,Integer>>();
				dataStartSzkolenie=LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(daneDoSzkolenia.getTerminOd()));
				dataEndSzkolenie=LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(daneDoSzkolenia.getTerminDo()));
			
				Period periodDataStartEnd=Period.between(dataStartSzkolenie,dataEndSzkolenie);
			
				long wszystkieDniL=Math.round((daneDoSzkolenia.getTerminDo().getTime()-daneDoSzkolenia.getTerminOd().getTime())/(double) 86400000);
				wszystkieDni=(int) wszystkieDniL;
				
				Date mr=new Date();
				
				Calendar c = Calendar.getInstance();
			
				Map <String,Integer> dniPracujaceMap=new LinkedHashMap<String,Integer>();
				Map <String,Integer> dniWolneMap=new LinkedHashMap<String,Integer>();
			
				int ileMiesiecy=periodDataStartEnd.getMonths();
				
				if((periodDataStartEnd.getMonths()>0 && dataStartSzkolenie.plusMonths(periodDataStartEnd.getMonths()).getMonthValue()<dataEndSzkolenie.getMonthValue()) 
						|| (periodDataStartEnd.getMonths()==0 && dataStartSzkolenie.getMonthValue()!=dataEndSzkolenie.getMonthValue()))
				{	
					ileMiesiecy=ileMiesiecy+1;
				}
				
				for(int i=0;i<=ileMiesiecy;i++) {
					
					String miesiacRok=dataStartSzkolenie.plusMonths(i).getMonthValue()+"-"+dataStartSzkolenie.plusMonths(i).getYear();
					
//---------------------------------------------------------------obliczanie dni pracujacych w miesiacu--------------------------------------				
					c.setTime(mr);
					c.set(Calendar.MONTH, dataStartSzkolenie.plusMonths(i).getMonthValue()-1);
					c.set(Calendar.YEAR, dataStartSzkolenie.plusMonths(i).getYear());
				
					int ileDniWmiesiacu=c.getActualMaximum(Calendar.DAY_OF_MONTH);
					
					if(i==0) {
						if(ileMiesiecy==0)
							dniPracujaceMap.put(miesiacRok, dataEndSzkolenie.getDayOfMonth()-dataStartSzkolenie.getDayOfMonth());
						else
						dniPracujaceMap.put(miesiacRok, ileDniWmiesiacu-dataStartSzkolenie.getDayOfMonth());
					} 
					else if(i==ileMiesiecy) {
						dniPracujaceMap.put(miesiacRok, dataEndSzkolenie.getDayOfMonth());
					}
					else
						dniPracujaceMap.put(miesiacRok, ileDniWmiesiacu);
					
//----------------------------------------------------------obliczanie dni wolnych w miesiacu ------------------------------------------------
						for(Dniwolne dwl:dniWolne) {
							if(dwl.getDzien()!=null) {
							String dwlStr=new SimpleDateFormat("M-yyyy").format(dwl.getDzien());
						
							if(dwlStr.equals(miesiacRok) && dwl.getDzien().after(daneDoSzkolenia.getTerminOd()) && dwl.getDzien().before(daneDoSzkolenia.getTerminDo())) {
								Integer plusDay=1;
								if(dniWolneMap.get(dwlStr)!=null)
								plusDay=dniWolneMap.get(dwlStr)+1;
								dniWolneMap.put(dwlStr, plusDay);
								wszystkieDni=wszystkieDni-1;
							}
							}
						}
					
//------------------------------------------------------------połaczenie danych-----------------------------------------------------------------
						Map<String,Integer> dpracujace=new LinkedHashMap<String,Integer>();
						if(dniWolneMap.get(miesiacRok)==null)
							dniWolneMap.put(miesiacRok,0);
						dpracujace.put("dniPracujace", dniPracujaceMap.get(miesiacRok)-dniWolneMap.get(miesiacRok));
						miesiaceDniPracujace.put(miesiacRok, dpracujace);
					}
				
		
				for(Planyzaklady pz:planyZakladyPluton) {
					Przedmiottemat ptch=przedmiottematAllPluton.stream().filter(pf->pf.getId_przedmiotTemat()==pz.getPrzedmiottemat().getId_przedmiotTemat()).findFirst().orElse(null);
							if(ptch!=null) {
								if(ptch.getIleGodzin()==pz.getIleGodzin())
									przedmiottematAllPluton.remove(ptch);
								else
								{
									int ileGodz=ptch.getIleGodzin()-pz.getIleGodzin();
									ptch.setIleGodzin(ileGodz);
								}
							}
				}		
				analizaPlanyZaklady(widokAnalizy);
		  }
		
	}

	public void zmienWidokAnalizy(String widok) {
		widokAnalizy=widok;
		analizaPlanyZaklady(widokAnalizy);
	}

	public String godzninyNadzien(String miesiacRok) {
		if(kalkulatorZajecPodsumowanie.get(miesiacRok)!=null && kalkulatorZajecPodsumowanie.get(miesiacRok).get("zrealizowane")!=null && miesiaceDniPracujace.get(miesiacRok)!=null && miesiaceDniPracujace.get(miesiacRok).get("dniPracujace")!=null) {
		float zrealiz=Integer.valueOf(kalkulatorZajecPodsumowanie.get(miesiacRok).get("zrealizowane"));
		float dniPracuj=Integer.valueOf(miesiaceDniPracujace.get(miesiacRok).get("dniPracujace"));
		String ileGodzNadzien=String.format("%.02f",zrealiz/dniPracuj);
		return ileGodzNadzien;
		}
		return null;
	}
	
	public void analizaPlanyZaklady(String specjalZaklad) {
				kalkulatorZajec=new LinkedHashMap <String,Map<String,Map<String,String>>>();
				kalkulatorZajecPodsumowanie=new LinkedHashMap<String,Map<String,String>>();
				List<Specjalizacja> spec=new LinkedList<Specjalizacja>();
				spec=crudAll.getAllTerms("Specjalizacja.findAll");
				List<Zaklad> zak=new LinkedList<Zaklad>();
				zak=crudAll.getAllTerms("Zaklad.findAll");
				
				if(specjalZaklad.equals("zaklad"))
				{
					for(Zaklad specl:zak) {
						Map<String,Map<String,String>> kalkulatorZajecMiesiace=new TreeMap <String,Map<String,String>>();
						Integer ileGodzinWszystkichGrupa=0;
						
						for(Przedmiottemat pt:specl.getPrzedmiottemats()) {
							if(pt.getPrzedmiot().getStrukturaKursu().getIdStrukturaKursu()==daneDoSzkolenia.getIdKurs().getIdStrukturaKursu()) {
								ileGodzinWszystkichGrupa+=pt.getIleGodzin();
								wszystkieGodzinyGrupy.put(specl.getNazwaSkrot(),ileGodzinWszystkichGrupa);
							}
						}
						for(Przedmiottemat ptl:specl.getPrzedmiottemats()) {
							
							if(ptl.getPrzedmiot().getStrukturaKursu().getIdStrukturaKursu()==daneDoSzkolenia.getIdKurs().getIdStrukturaKursu()) {
								
								for(Planyzaklady pzl:ptl.getPlanyZakladys()) {
									if(daneDoSzkolenia.getIdPluton()==pzl.getPluton().getIdPluton()) {
										String keyMr=String.valueOf(pzl.getMiesiac())+"-"+String.valueOf(pzl.getRok());
									
										if(kalkulatorZajecMiesiace.get(keyMr)==null) {
											kalkulatorZajecMiesiace.put(keyMr, new TreeMap<String,String>());
											kalkulatorZajecMiesiace.get(keyMr).put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
										
										}else {
											kalkulatorZajecMiesiace.get(keyMr).put("zrealizowane", String.valueOf(Integer.valueOf(kalkulatorZajecMiesiace.get(keyMr).get("zrealizowane"))+pzl.getIleGodzin()));
										}
									
										if(kalkulatorZajecMiesiace.get("0")==null) {
											kalkulatorZajecMiesiace.put("0", new HashMap<String,String>());
											kalkulatorZajecMiesiace.get("0").put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
											float ileProcentZrealAll=Float.valueOf(pzl.getIleGodzin())*100/ileGodzinWszystkichGrupa;
											String ileProcentstr=String.format("%.02f",ileProcentZrealAll);
											kalkulatorZajecMiesiace.get("0").put("zrealizowaneProcentyAll",ileProcentstr);
										}else {
											kalkulatorZajecMiesiace.get("0").put("zrealizowane", String.valueOf(Integer.valueOf(kalkulatorZajecMiesiace.get("0").get("zrealizowane"))+pzl.getIleGodzin()));
											float ileProcentZrealAll=Float.valueOf(kalkulatorZajecMiesiace.get("0").get("zrealizowane"))*100/ileGodzinWszystkichGrupa;
											String ileProcentstr=String.format("%.02f",ileProcentZrealAll);
											kalkulatorZajecMiesiace.get("0").put("zrealizowaneProcentyAll",ileProcentstr);
										}
										
										
										
										if(kalkulatorZajecPodsumowanie.get("0")==null) {
											kalkulatorZajecPodsumowanie.put("0", new HashMap<String,String>());
											kalkulatorZajecPodsumowanie.get("0").put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
											float ileProcent=Float.valueOf(pzl.getIleGodzin())*100/wszystkieGodziny;
											String ileProcentstr=String.format("%.02f",ileProcent);
											kalkulatorZajecPodsumowanie.get("0").put("zrealizowaneProcentyAll", ileProcentstr);
											
										}else {
											kalkulatorZajecPodsumowanie.get("0").put("zrealizowane", String.valueOf(Integer.valueOf(kalkulatorZajecPodsumowanie.get("0").get("zrealizowane"))+pzl.getIleGodzin()));
											float ileProcent=Float.valueOf(kalkulatorZajecPodsumowanie.get("0").get("zrealizowane"))*100/wszystkieGodziny;
											String ileProcentstr=String.format("%.02f",ileProcent);
											kalkulatorZajecPodsumowanie.get("0").put("zrealizowaneProcentyAll", ileProcentstr);
											
										}
										
										
										
										if(kalkulatorZajecPodsumowanie.get(keyMr)==null) {
											kalkulatorZajecPodsumowanie.put(keyMr, new HashMap<String,String>());
											kalkulatorZajecPodsumowanie.get(keyMr).put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
											
										}else {
											if(kalkulatorZajecPodsumowanie.get(keyMr).get("zrealizowane")==null)
												kalkulatorZajecPodsumowanie.get(keyMr).put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
											else
											kalkulatorZajecPodsumowanie.get(keyMr).put("zrealizowane", String.valueOf(Integer.valueOf(kalkulatorZajecPodsumowanie.get(keyMr).get("zrealizowane"))+pzl.getIleGodzin()));
								
										}
										}
									}
							}
					}
		
				kalkulatorZajec.put(specl.getNazwaSkrot(), kalkulatorZajecMiesiace);
			
				Integer wszystkieDniLoop=wszystkieDni;
				float ileGodzinWszystkichGrupaLoop=ileGodzinWszystkichGrupa;
				
				for(Entry<String, Map<String, Integer>> miesiacePuste:miesiaceDniPracujace.entrySet()) {
				
					Float doZrealizowaniaFl=ileGodzinWszystkichGrupaLoop*miesiaceDniPracujace.get(miesiacePuste.getKey()).get("dniPracujace")/wszystkieDniLoop;
					Integer doZrealizowaniaInt=Math.round(doZrealizowaniaFl);
				
					if(doZrealizowaniaInt<0)
						doZrealizowaniaInt=0;
					
					wszystkieDniLoop=wszystkieDniLoop-miesiaceDniPracujace.get(miesiacePuste.getKey()).get("dniPracujace");
				
					if(kalkulatorZajecMiesiace.get(miesiacePuste.getKey())!=null && kalkulatorZajecMiesiace.get(miesiacePuste.getKey()).get("zrealizowane")!=null) {
						ileGodzinWszystkichGrupaLoop=ileGodzinWszystkichGrupaLoop-Float.valueOf(kalkulatorZajecMiesiace.get(miesiacePuste.getKey()).get("zrealizowane"));
						
					}
					else {
						ileGodzinWszystkichGrupaLoop=ileGodzinWszystkichGrupaLoop-doZrealizowaniaFl;
					}
					
					if(kalkulatorZajec.get(specl.getNazwaSkrot()).get(miesiacePuste.getKey())==null) {
							kalkulatorZajecMiesiace.put(miesiacePuste.getKey(), new TreeMap<String,String>());
							kalkulatorZajecMiesiace.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(doZrealizowaniaInt));
				}else {
					kalkulatorZajecMiesiace.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(doZrealizowaniaInt));
				}
					kalkulatorZajec.put(specl.getNazwaSkrot(), kalkulatorZajecMiesiace);
					
					
					if(kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey())==null) {
						kalkulatorZajecPodsumowanie.put(miesiacePuste.getKey(), new HashMap<String,String>());
						kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(doZrealizowaniaInt));
					}else {
						if(kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).get("doZrealizowaniaLimit")==null)
							kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(doZrealizowaniaInt));
						else
							kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(Integer.valueOf(kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).get("doZrealizowaniaLimit"))+doZrealizowaniaInt));
					}
				}
				
			}
	 }
else {
			for(Specjalizacja specl:spec) {
				Map<String,Map<String,String>> kalkulatorZajecMiesiace=new TreeMap <String,Map<String,String>>();
				Integer ileGodzinWszystkichGrupa=0;
			
				for(Przedmiottemat pt:specl.getPrzedmiottemats()) {
					if(pt.getPrzedmiot().getStrukturaKursu().getIdStrukturaKursu()==daneDoSzkolenia.getIdKurs().getIdStrukturaKursu()) {
						ileGodzinWszystkichGrupa+=pt.getIleGodzin();
						wszystkieGodzinyGrupy.put(specl.getNazwa(),ileGodzinWszystkichGrupa);
					}
				}
				
					for(Przedmiottemat ptl:specl.getPrzedmiottemats()) {
					
						if(ptl.getPrzedmiot().getStrukturaKursu().getIdStrukturaKursu()==daneDoSzkolenia.getIdKurs().getIdStrukturaKursu()) {
							
							for(Planyzaklady pzl:ptl.getPlanyZakladys()) {
							if(daneDoSzkolenia.getIdPluton()==pzl.getPluton().getIdPluton()) {
								String keyMr=String.valueOf(pzl.getMiesiac())+"-"+String.valueOf(pzl.getRok());
								
										if(kalkulatorZajecMiesiace.get(keyMr)==null) {
											kalkulatorZajecMiesiace.put(keyMr, new TreeMap<String,String>());
											kalkulatorZajecMiesiace.get(keyMr).put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
										
										}else {
											
											kalkulatorZajecMiesiace.get(keyMr).put("zrealizowane", String.valueOf(Integer.valueOf(kalkulatorZajecMiesiace.get(keyMr).get("zrealizowane"))+pzl.getIleGodzin()));
										}
									
										if(kalkulatorZajecMiesiace.get("0")==null) {
											kalkulatorZajecMiesiace.put("0", new HashMap<String,String>());
											kalkulatorZajecMiesiace.get("0").put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
											float ileProcentZrealAll=Float.valueOf(pzl.getIleGodzin())*100/ileGodzinWszystkichGrupa;
											String ileProcentstr=String.format("%.02f",ileProcentZrealAll);
											kalkulatorZajecMiesiace.get("0").put("zrealizowaneProcentyAll",ileProcentstr);
										}else {
										
											kalkulatorZajecMiesiace.get("0").put("zrealizowane", String.valueOf(Integer.valueOf(kalkulatorZajecMiesiace.get("0").get("zrealizowane"))+pzl.getIleGodzin()));
											float ileProcentZrealAll=Float.valueOf(kalkulatorZajecMiesiace.get("0").get("zrealizowane"))*100/ileGodzinWszystkichGrupa;
											String ileProcentstr=String.format("%.02f",ileProcentZrealAll);
											kalkulatorZajecMiesiace.get("0").put("zrealizowaneProcentyAll",ileProcentstr);
											
										}
										
										
										
										if(kalkulatorZajecPodsumowanie.get("0")==null) {
											kalkulatorZajecPodsumowanie.put("0", new HashMap<String,String>());
											kalkulatorZajecPodsumowanie.get("0").put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
											float ileProcent=Float.valueOf(pzl.getIleGodzin())*100/wszystkieGodziny;
											String ileProcentstr=String.format("%.02f",ileProcent);
											kalkulatorZajecPodsumowanie.get("0").put("zrealizowaneProcentyAll", ileProcentstr);
											
										}else {
											if(ptl.getSpecjalizacjas().size()==1 || (ptl.getSpecjalizacjas().size()>1 && ptl.getSpecjalizacjas().get(0).getNazwa().equals(specl.getNazwa()))) {
											kalkulatorZajecPodsumowanie.get("0").put("zrealizowane", String.valueOf(Integer.valueOf(kalkulatorZajecPodsumowanie.get("0").get("zrealizowane"))+pzl.getIleGodzin()));
											float ileProcent=Float.valueOf(kalkulatorZajecPodsumowanie.get("0").get("zrealizowane"))*100/wszystkieGodziny;
											String ileProcentstr=String.format("%.02f",ileProcent);
											kalkulatorZajecPodsumowanie.get("0").put("zrealizowaneProcentyAll", ileProcentstr);
											}
										}
										
										
										
										if(kalkulatorZajecPodsumowanie.get(keyMr)==null) {
											kalkulatorZajecPodsumowanie.put(keyMr, new HashMap<String,String>());
											kalkulatorZajecPodsumowanie.get(keyMr).put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
											
										}else {
											if(kalkulatorZajecPodsumowanie.get(keyMr).get("zrealizowane")==null)
												kalkulatorZajecPodsumowanie.get(keyMr).put("zrealizowane", String.valueOf(pzl.getIleGodzin()));
											else {
												if(ptl.getSpecjalizacjas().size()==1 || (ptl.getSpecjalizacjas().size()>1 && ptl.getSpecjalizacjas().get(0).getNazwa().equals(specl.getNazwa()))) 
											kalkulatorZajecPodsumowanie.get(keyMr).put("zrealizowane", String.valueOf(Integer.valueOf(kalkulatorZajecPodsumowanie.get(keyMr).get("zrealizowane"))+pzl.getIleGodzin()));
											}
								
										}
									}
								}
						}
				}
	
			kalkulatorZajec.put(specl.getNazwa(), kalkulatorZajecMiesiace);
		
			int wszystkieDniLoop=wszystkieDni;
			float ileGodzinWszystkichGrupaLoop=ileGodzinWszystkichGrupa;
			
			for(Entry<String, Map<String, Integer>> miesiacePuste:miesiaceDniPracujace.entrySet()) {
			
				Float doZrealizowaniaFl=ileGodzinWszystkichGrupaLoop*miesiaceDniPracujace.get(miesiacePuste.getKey()).get("dniPracujace")/wszystkieDniLoop;
				int doZrealizowaniaInt=(int) Math.round(doZrealizowaniaFl);
			
				if(doZrealizowaniaInt<0)
					doZrealizowaniaInt=0;
				
				wszystkieDniLoop=wszystkieDniLoop-miesiaceDniPracujace.get(miesiacePuste.getKey()).get("dniPracujace");
			
				if(kalkulatorZajecMiesiace.get(miesiacePuste.getKey())!=null && kalkulatorZajecMiesiace.get(miesiacePuste.getKey()).get("zrealizowane")!=null) {
					ileGodzinWszystkichGrupaLoop=ileGodzinWszystkichGrupaLoop-Float.valueOf(kalkulatorZajecMiesiace.get(miesiacePuste.getKey()).get("zrealizowane"));
					
				}
				else {
					ileGodzinWszystkichGrupaLoop=ileGodzinWszystkichGrupaLoop-doZrealizowaniaFl;
				}
				
				if(kalkulatorZajec.get(specl.getNazwa()).get(miesiacePuste.getKey())==null) {
						kalkulatorZajecMiesiace.put(miesiacePuste.getKey(), new TreeMap<String,String>());
						kalkulatorZajecMiesiace.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(doZrealizowaniaInt));
			}else {
				kalkulatorZajecMiesiace.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(doZrealizowaniaInt));
			}
				kalkulatorZajec.put(specl.getNazwa(), kalkulatorZajecMiesiace);
				
				
				if(kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey())==null) {
					kalkulatorZajecPodsumowanie.put(miesiacePuste.getKey(), new HashMap<String,String>());
					kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(doZrealizowaniaInt));
				}else {
					if(kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).get("doZrealizowaniaLimit")==null)
						kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(doZrealizowaniaInt));
					else
						kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).put("doZrealizowaniaLimit", String.valueOf(Integer.valueOf(kalkulatorZajecPodsumowanie.get(miesiacePuste.getKey()).get("doZrealizowaniaLimit"))+doZrealizowaniaInt));
				}
			}
			
		}
	}
}
	
	public Integer wszystkieGodzinyGrupyFakt() {
		Integer ileTotal=0;
		  for (Map.Entry<String, Integer> entry : wszystkieGodzinyGrupy.entrySet()) {
			    // String key = entry.getKey();
			    ileTotal += entry.getValue();
			}
		    
		return ileTotal;
	}
	
	public void changeSzkolenie() {
		kompaniaSelectList=new TreeMap<String,String>();
		plutonSelectList=new TreeMap<String,String>();
	
		kompaniaOneSelect=null;
		plutonOneSelect=null;
		session.setAttribute("szkolenieOneSelect", szkolenieOneSelect);
		session.setAttribute("kompaniaOneSelect", null);
		session.setAttribute("plutonOneSelect", null);
		if(plutonyWybor!=null)
			for(Pluton pls:plutonyWybor) {
				if(pls.getOznaczenieSzkolenia().equals(szkolenieOneSelect)) {
				kompaniaSelectList.put(pls.getKompania().getNazwaKompania(),pls.getKompania().getNazwaKompania());
				
				}
				if(pls.getKompania().getNazwaKompania().equals(kompaniaOneSelect))
				plutonSelectList.put(pls.getNazwaPluton(),pls.getNazwaPluton());
		}
		
	}
	
	public void changeKompania() {
		plutonSelectList=new TreeMap<String,String>();
	
		plutonOneSelect=null;
		session.setAttribute("kompaniaOneSelect", kompaniaOneSelect);
		session.setAttribute("plutonOneSelect", null);
		if(plutonyWybor!=null)
			for(Pluton pls:plutonyWybor) {
				if(pls.getOznaczenieSzkolenia().equals(szkolenieOneSelect) && pls.getKompania().getNazwaKompania().equals(kompaniaOneSelect))
				plutonSelectList.put(pls.getNazwaPluton(),pls.getNazwaPluton());
		}
	}
	
	public void changePluton() {
		session.setAttribute("plutonOneSelect", plutonOneSelect);
	}
	
	public void changeKompaniaImport() {
		plutonSelectListImport=new TreeMap<String,String>();
		plutonOneSelectImport=null;
		if(plutonyWybor!=null)
			for(Pluton pls:plutonyWybor) {
				if(pls.getOznaczenieSzkolenia().equals(szkolenieOneSelect) && pls.getKompania().getNazwaKompania().equals(kompaniaOneSelectImport))
				plutonSelectListImport.put(pls.getNazwaPluton(),pls.getNazwaPluton());
		}
	}

		public void zapiszNowyPlan() {
			
			if(selectedPrzedmiot.size()>0) {
			for(Przedmiottemat ptz:selectedPrzedmiot) {
				Planyzaklady pzs=new Planyzaklady();
				pzs.setDataWpisu(miesiacRok);
				pzs.setPrzedmiottemat(ptz);
				pzs.setIleGodzin(ptz.getIleGodzin());
				pzs.setKomentarz(ptz.getKomentarz());
				pzs.setMiesiac(miesiacWybrany+1);
				pzs.setRok(rokWybrany);
				pzs.setPluton(daneDoSzkolenia);
				pzs.setWpisal((Integer) session.getAttribute("idUser"));
				crudAll.create(pzs);				
			}
			init();
			new MessagePlay("Zapisano",null,FacesMessage.SEVERITY_INFO);
			selectedPrzedmiot=null;
			ileSelectedPrzedmiot=0;
			}
			else
				new MessagePlay("Nie wybrano tematów!",null,FacesMessage.SEVERITY_WARN);
		}
		
		public void clearAllFilters(String komponent) {
			 ileSelectedPrzedmiot=0;
			selectedPrzedmiot=null;
			filterValues.clear();
			DataTable datTab=(DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(komponent);
			if(datTab!=null) {
			datTab.reset();
			datTab.setValueExpression("sortBy", null);
			}
			init();
			}
		public void onRowEdit(CellEditEvent ev) {
			liczeGodzinyPoprawione();
			 String summ="Uwzględniono poprawę liczby godzin oraz komentarz.";
			new MessagePlay(summ,"Wiersz: "+ev.getNewValue(),FacesMessage.SEVERITY_INFO);
			
		}
		
		public void liczeGodzinyPoprawione() {
			ileSelectedPrzedmiot=0;			 
			 for(Przedmiottemat ptl:selectedPrzedmiot) {
				 ileSelectedPrzedmiot+=ptl.getIleGodzin();
			 }
		}
		
		public void onRowEditZaplZaj(RowEditEvent ev) {
			Planyzaklady pzu=(Planyzaklady) ev.getObject();
			crudAll.update(pzu);
			new MessagePlay("Uaktualniono!",null,FacesMessage.SEVERITY_INFO);
			init();
			
		}
		
	 public void onRowSelect() {
		 ileSelectedPrzedmiot=0;
		 
		 for(Przedmiottemat ptl:selectedPrzedmiot) {
			 ileSelectedPrzedmiot+=ptl.getIleGodzin();
		 }
	 }

	public void ileDoplanowac() {
		if( miesiacWybrany!=null && rokWybrany!=null) {
				String miesiac_rok=String.valueOf(miesiacWybrany+1)+"-"+String.valueOf(rokWybrany);
				Integer zwrot=null;
					if(filterValues.get("specjalToString")!=null ) {
						if(kalkulatorZajec!=null) {
							String spec=(String) filterValues.get("specjalToString");
									if(kalkulatorZajec.get(spec).get(miesiac_rok).get("doZrealizowaniaLimit")!=null) {
											if(kalkulatorZajec.get(spec).get(miesiac_rok).get("zrealizowane")!=null)
												zwrot=Integer.valueOf(kalkulatorZajec.get(spec).get(miesiac_rok).get("doZrealizowaniaLimit"))-Integer.valueOf(kalkulatorZajec.get(spec).get(miesiac_rok).get("zrealizowane"));
											else
												zwrot=Integer.valueOf(kalkulatorZajec.get(spec).get(miesiac_rok).get("doZrealizowaniaLimit"));
									}
							}
							
						} else {zwrot=null;
							if(kalkulatorZajecPodsumowanie.get(miesiac_rok)!=null && kalkulatorZajecPodsumowanie.get(miesiac_rok).get("doZrealizowaniaLimit")!=null) {
								if(kalkulatorZajecPodsumowanie.get(miesiac_rok).get("zrealizowane")!=null)
									zwrot=Integer.valueOf(kalkulatorZajecPodsumowanie.get(miesiac_rok).get("doZrealizowaniaLimit"))-Integer.valueOf(kalkulatorZajecPodsumowanie.get(miesiac_rok).get("zrealizowane"));
								else
									zwrot=Integer.valueOf(kalkulatorZajecPodsumowanie.get(miesiac_rok).get("doZrealizowaniaLimit"));
							}
					}
				
						ileSelectedPrzedmiotZap=zwrot;
			}
		}
		public void info(String strInfo) {
			new MessagePlay(strInfo,null,FacesMessage.SEVERITY_INFO);
		}
		    public String convertMonth(Integer m) {
				ArrayList<String> mstr=new ArrayList<String>();
				mstr.add(0,"wszystkie");
				mstr.add(1,"sty");
				mstr.add(2,"lut");
				mstr.add(3,"marz");
				mstr.add(4,"kwi");
				mstr.add(5,"maj");
				mstr.add(6,"czer");
				mstr.add(7,"lip");
				mstr.add(8,"sier");
				mstr.add(9,"wrze");
				mstr.add(10,"pazd");
				mstr.add(11,"list");
				mstr.add(12,"grudz");
				return mstr.get(m);
		    }
		    
			public void removeRow(ActionEvent e) {
				Planyzaklady planRem= (Planyzaklady) e.getComponent().getAttributes().get("removeRow");
				crudAll.delete(planRem);
				init();
				new MessagePlay("Usunięto!",null,FacesMessage.SEVERITY_WARN);
		}
		
		public void deleteSelectedPlany() {
			if(selectedPrzedmiotZaplan!=null && selectedPrzedmiotZaplan.size()>0) {
				for(Planyzaklady zp:selectedPrzedmiotZaplan) {
					crudAll.delete(zp);
				}				 
				init();	
				ileSelectedPrzedmiot=0;
				//ileSelectedPrzedmiotZap+=9;
				new MessagePlay("Usunięto!",null,FacesMessage.SEVERITY_WARN);
			} else {
				new MessagePlay("Uwaga! Zaznacz wiersze do usunięcia.",null,FacesMessage.SEVERITY_WARN);
			}
		}
		
		public boolean hasSelected() {
				return 	selectedPrzedmiotZaplan!=null && selectedPrzedmiotZaplan.size()>0;
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
			
			public String wykladowcyToString(List<User> usl) {
				String uss = null;
				if(usl!=null && usl.size()>0) {
				for(User us:usl) {
					uss+=us.getImieUsers().substring(0,1)+". "+us.getNazwiskoUsers()+", ";
				}
				uss=uss.trim();
			return uss.substring(0, uss.length() -1);	
				}
				return "";
			}		
			
//----------------------------------------------------getters setters-------------------------------------------------
	public String getSzkolenieOneSelect() {
		return szkolenieOneSelect;
	}

	public void setSzkolenieOneSelect(String szkolenieOneSelect) {
		this.szkolenieOneSelect = szkolenieOneSelect;
	}
	public Pluton getDaneDoSzkolenia() {
		return daneDoSzkolenia;
	}
	public void setDaneDoSzkolenia(Pluton daneDoSzkolenia) {
		this.daneDoSzkolenia = daneDoSzkolenia;
	}
	
	public String getKompaniaOneSelect() {
		return kompaniaOneSelect;
	}
	public void setKompaniaOneSelect(String kompaniaOneSelect) {
		this.kompaniaOneSelect = kompaniaOneSelect;
	}
	public String getPlutonOneSelect() {
		return plutonOneSelect;
	}
	public void setPlutonOneSelect(String plutonOneSelect) {
		this.plutonOneSelect = plutonOneSelect;
	}
	public Map<String, String> getSzkolenieSelectList() {
		return szkolenieSelectList;
	}
	public void setSzkolenieSelectList(Map<String, String> szkolenieSelectList) {
		this.szkolenieSelectList = szkolenieSelectList;
	}
	public Map<String, String> getKompaniaSelectList() {
		return kompaniaSelectList;
	}
	public void setKompaniaSelectList(Map<String, String> kompaniaSelectList) {
		this.kompaniaSelectList = kompaniaSelectList;
	}
	public Map<String, String> getPlutonSelectList() {
		return plutonSelectList;
	}
	public void setPlutonSelectList(Map<String, String> plutonSelectList) {
		this.plutonSelectList = plutonSelectList;
	}
	public List<Pluton> getPlutonyWybor() {
		return plutonyWybor;
	}
	public void setPlutonyWybor(List<Pluton> plutonyWybor) {
		this.plutonyWybor = plutonyWybor;
	}
	
	public List<Planyzaklady> getPlanyZakladyPluton() {
		return planyZakladyPluton;
	}
	public void setPlanyZakladyPluton(List<Planyzaklady> planyZakladyPluton) {
		this.planyZakladyPluton = planyZakladyPluton;
	}
	public List<Przedmiottemat> getPrzedmiottematAllPluton() {
		return przedmiottematAllPluton;
	}
	public void setPrzedmiottematAllPluton(List<Przedmiottemat> przedmiottematAllPluton) {
		this.przedmiottematAllPluton = przedmiottematAllPluton;
	}
	public List<Przedmiottemat> getSelectedPrzedmiot() {
		return selectedPrzedmiot;
	}
	public void setSelectedPrzedmiot(List<Przedmiottemat> selectedPrzedmiot) {
		this.selectedPrzedmiot = selectedPrzedmiot;
	}
	public Date getMiesiacRok() {
		return miesiacRok;
	}
	public void setMiesiacRok(Date miesiacRok) {
		this.miesiacRok = miesiacRok;
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
	public Map<String, Serializable> getFilterValues() {
		return filterValues;
	}
	public void setFilterValues(Map<String, Serializable> filterValues) {
		this.filterValues = filterValues;
	}
	public List<String> getZakladyFilterSelect() {
		return zakladyFilterSelect;
	}
	public void setZakladyFilterSelect(List<String> zakladyFilterSelect) {
		this.zakladyFilterSelect = zakladyFilterSelect;
	}
	public List<String> getSpecjalFilterSelect() {
		return specjalFilterSelect;
	}
	public void setSpecjalFilterSelect(List<String> specjalFilterSelect) {
		this.specjalFilterSelect = specjalFilterSelect;
	}
	public StrukturaKursu getStrk() {
		return strk;
	}
	public void setStrk(StrukturaKursu strk) {
		this.strk = strk;
	}
	public Map<String, Map<String, Map<String, String>>> getKalkulatorZajec() {
		return kalkulatorZajec;
	}
	public void setKalkulatorZajec(Map<String, Map<String, Map<String, String>>> kalkulatorZajec) {
		this.kalkulatorZajec = kalkulatorZajec;
	}
	public String getWidokAnalizy() {
		return widokAnalizy;
	}
	public void setWidokAnalizy(String widokAnalizy) {
		this.widokAnalizy = widokAnalizy;
	}
	public LocalDate getDataEndSzkolenie() {
		return dataEndSzkolenie;
	}
	public void setDataEndSzkolenie(LocalDate dataEndSzkolenie) {
		this.dataEndSzkolenie = dataEndSzkolenie;
	}
	public LocalDate getDataStartSzkolenie() {
		return dataStartSzkolenie;
	}
	public void setDataStartSzkolenie(LocalDate dataStartSzkolenie) {
		this.dataStartSzkolenie = dataStartSzkolenie;
	}
	public Map<String, Map<String, Integer>> getMiesiaceDniPracujace() {
		return miesiaceDniPracujace;
	}
	public void setMiesiaceDniPracujace(Map<String, Map<String, Integer>> miesiaceDniPracujace) {
		this.miesiaceDniPracujace = miesiaceDniPracujace;
	}
	public Integer getWszystkieGodziny() {
		return wszystkieGodziny;
	}
	public void setWszystkieGodziny(Integer wszystkieGodziny) {
		this.wszystkieGodziny = wszystkieGodziny;
	}
	   public Map<String, Map<String, String>> getKalkulatorZajecPodsumowanie() {
			return kalkulatorZajecPodsumowanie;
		}
		public void setKalkulatorZajecPodsumowanie(Map<String, Map<String, String>> kalkulatorZajecPodsumowanie) {
			this.kalkulatorZajecPodsumowanie = kalkulatorZajecPodsumowanie;
		}
		public Map<String,Integer> getWszystkieGodzinyGrupy() {
			return wszystkieGodzinyGrupy;
		}
		public void setWszystkieGodzinyGrupy(Map<String,Integer> wszystkieGodzinyGrupy) {
			this.wszystkieGodzinyGrupy = wszystkieGodzinyGrupy;
		}
		public List<Dniwolne> getDniWolne() {
			return dniWolne;
		}
		public void setDniWolne(List<Dniwolne> dniWolne) {
			this.dniWolne = dniWolne;
		}
	
		public Map<String,String> getDniPracujaceMap() {
			return dniPracujaceMap;
		}
		public void setDniPracujaceMap(Map<String,String> dniPracujaceMap) {
			this.dniPracujaceMap = dniPracujaceMap;
		}
		public Integer getWszystkieDni() {
			return wszystkieDni;
		}
		public void setWszystkieDni(Integer wszystkieDni) {
			this.wszystkieDni = wszystkieDni;
		}

		public Map<String,String> getKompaniaSelectListImport() {
			return kompaniaSelectListImport;
		}

		public void setKompaniaSelectListImport(Map<String,String> kompaniaSelectListImport) {
			this.kompaniaSelectListImport = kompaniaSelectListImport;
		}

		public Map<String,String> getPlutonSelectListImport() {
			return plutonSelectListImport;
		}

		public void setPlutonSelectListImport(Map<String,String> plutonSelectListImport) {
			this.plutonSelectListImport = plutonSelectListImport;
		}

		public String getKompaniaOneSelectImport() {
			return kompaniaOneSelectImport;
		}

		public void setKompaniaOneSelectImport(String kompaniaOneSelectImport) {
			this.kompaniaOneSelectImport = kompaniaOneSelectImport;
		}

		public String getPlutonOneSelectImport() {
			return plutonOneSelectImport;
		}

		public void setPlutonOneSelectImport(String plutonOneSelectImport) {
			this.plutonOneSelectImport = plutonOneSelectImport;
		}

		public Pluton getPlanyPlutonuImport() {
			return planyPlutonuImport;
		}

		public void setPlanyPlutonuImport(Pluton planyPlutonuImport) {
			this.planyPlutonuImport = planyPlutonuImport;
		}

		public Integer getIleSelectedPrzedmiot() {
			return ileSelectedPrzedmiot;
		}

		public void setIleSelectedPrzedmiot(Integer ileSelectedPrzedmiot) {
			this.ileSelectedPrzedmiot = ileSelectedPrzedmiot;
		}

		public Integer getPlutonOneSelectImportMiesiac() {
			return plutonOneSelectImportMiesiac;
		}

		public void setPlutonOneSelectImportMiesiac(Integer plutonOneSelectImportMiesiac) {
			this.plutonOneSelectImportMiesiac = plutonOneSelectImportMiesiac;
		}

		public Integer getIleSelectedPrzedmiotZap() {
			return ileSelectedPrzedmiotZap;
		}

		public void setIleSelectedPrzedmiotZap(Integer ileSelectedPrzedmiotZap) {
			this.ileSelectedPrzedmiotZap = ileSelectedPrzedmiotZap;
		}

		public List<Planyzaklady> getSelectedPrzedmiotZaplan() {
			return selectedPrzedmiotZaplan;
		}

		public void setSelectedPrzedmiotZaplan(List<Planyzaklady> selectedPrzedmiotZaplan) {
			this.selectedPrzedmiotZaplan = selectedPrzedmiotZaplan;
		}
}
