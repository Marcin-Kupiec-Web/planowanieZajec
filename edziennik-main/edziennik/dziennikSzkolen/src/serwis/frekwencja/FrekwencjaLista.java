/**
 * 
 */
package serwis.frekwencja;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Obecnosc;
import com.edziennik.sluchacze.zajecia.model.ObecnoscPoprawa;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.userManager.model.User;
import com.userManager.model.UsersStudent;

import my.util.SluchaczCompare;
import serwis.RejestryLogi.RejestryLogi;
import serwis.logowanie.Logowanie;

@Named
@ViewScoped
public class FrekwencjaLista implements Serializable{
	
	/**
	 * wybranySluchacz
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAllLocal crudAll;
	
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;
	@Inject
	private Logowanie zalogowanyUser;
	@Inject
	private serwis.users.nameUser.NameUser nameUser;
	
	private List<Sluchacze> sluchaczeSortedOb;

	private List<Pluton> plutonWybrany;
	private Integer idPlutonWybrany;
	private boolean render=false;
	private boolean renderObDialog=false;
	private List<Obecnosc> selectedObecnosc;
	private Map<String,Serializable> filterValues=new HashMap<>();

	public Map<String, Serializable> getFilterValues() {
		return filterValues;
	}


	public void setFilterValues(Map<String, Serializable> filterValues) {
		this.filterValues = filterValues;
	}

	private String wartoscObSelected;
	private Date dataFormGlowny;
	private Timestamp dataFormGlownyTimeStamp;
	private Map<String,String> wyborObecnoscList;
	private Map<String,String> wyborObecnoscList2;
	private Integer wybranySluchacz;
	private int idUsers;
	private List<User> us;
	
	@PostConstruct 
	    public void init() {
		wyborPlutonu.prepareChangeSzkolenie();

		sluchaczeSortedOb=new ArrayList<Sluchacze>();
		 wyborObecnoscList=new LinkedHashMap<String,String>();
		 wyborObecnoscList.put("", "");
		 wyborObecnoscList.put("N", "N");
		 wyborObecnoscList.put("Sp", "Sp");
		 wyborObecnoscList.put("C", "C");
		 wyborObecnoscList.put("L", "L");
		 wyborObecnoscList.put("Sł", "Sł");
		 wyborObecnoscList.put("S", "S");
		 wyborObecnoscList.put("U", "U");
		 wyborObecnoscList.put("Wps", "Wps");
		 wyborObecnoscList.put("Wzn", "Wzn");
		 wyborObecnoscList.put("Wo", "Wo");
		 wyborObecnoscList.put("Zs", "Zs");
		 
		 wyborObecnoscList2=new LinkedHashMap<String,String>();
		 wyborObecnoscList2.put("N", "N");
		 wyborObecnoscList2.put("Sp", "Sp");
		 wyborObecnoscList2.put("C", "C");
		 wyborObecnoscList2.put("L", "L");
		 wyborObecnoscList2.put("Sł", "Sł");
		 wyborObecnoscList2.put("S", "S");
		 wyborObecnoscList2.put("U", "U");
		 wyborObecnoscList2.put("Wps", "Wps");
		 wyborObecnoscList2.put("Wzn", "Wzn");
		 wyborObecnoscList2.put("Wo", "Wo");
		 wyborObecnoscList2.put("Zs", "Zs");
		 
		 	idUsers=zalogowanyUser.getIdUsers();
			Calendar cal = Calendar.getInstance();  
	    	cal.getTime();
	    	dataFormGlowny=wyborPlutonu.getDataFormGlowny();
	    	cal.setTime(dataFormGlowny);
	    	dataFormGlownyTimeStamp=new Timestamp(cal.getTimeInMillis());
		 
			idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
			HashMap<String, Object> hms=new HashMap<String,Object>();
			hms.put("idPluton", idPlutonWybrany);
			plutonWybrany=crudAll.getAllTermsParam("Pluton.findSluchaczePoIdPluton", hms);
		
			 if(plutonWybrany!=null && plutonWybrany.size()>0) {
				
				 if(zalogowanyUser.getPoziomUprawnien().equals("7")) {
						HashMap<String, Object> hmsl=new HashMap<String,Object>();
						hmsl.put("idUsers", zalogowanyUser.getIdUsers());
				        try {
				        	UsersStudent sl=(UsersStudent) crudAll.getAllTermsParam("findUserPoIdus", hmsl).get(0);
				        	sluchaczeSortedOb.add(sl.getSluchacze());
				        } catch(IndexOutOfBoundsException e) {
				         
				        }
						
					}else {
						sluchaczeSortedOb=plutonWybrany.get(0).getSluchaczes();
						Collections.sort(sluchaczeSortedOb,new SluchaczCompare());
					}
				}
				
			 if(sluchaczeSortedOb!=null && sluchaczeSortedOb.size()>0) {
					render=true;
			}
			 else
				 render = false;
	}
	
	 
public void obChange() {

	
	if(selectedObecnosc!=null && wartoscObSelected!=null) {
		//clearAllFilters();
		wybranySluchacz=selectedObecnosc.get(0).getSluchacze().getIdS();
		
			for(Obecnosc ob:selectedObecnosc) {
				ObecnoscPoprawa obUpPopr=new ObecnoscPoprawa();
				obUpPopr.setIdObecnosc(ob.getIdObecnosc());
				obUpPopr.setDataObPoprawa(ob.getDataWpisu());
				obUpPopr.setKtoWstawil(ob.getIdUsers());
				obUpPopr.setWartosc(ob.getWartosc());
				obUpPopr.setUsunieta(ob.getUsunieta());
				
				crudAll.create(obUpPopr);
				
				ob.setWartosc(wartoscObSelected);
				ob.setIdUsers(idUsers);
				ob.setDataWpisu(dataFormGlownyTimeStamp);
				ob.setUsunieta("NIE");
				ob.setImieNazwiskoUser(user(idUsers));
				ob.setWartoscPoprawa(obUpPopr.getIdObecnoscPoprawa());
				crudAll.update(ob);
				RejestryLogi rl=new RejestryLogi();
				
				String opis="Uaktualniono frekwencję (dowodca). Wpis: "+ob.getWartosc()+". Słuchacz: "+ob.getSluchacze().getImieSluchacz()+" "+ob.getSluchacze().getNazwiskoSluchacz()+". Komp./plut./szkol: "+plutonWybrany.get(0).getKompania().getNazwaKompania()+"/"+plutonWybrany.get(0).getNazwaPluton()+"/"+plutonWybrany.get(0).getOznaczenieSzkolenia()+", dzien: "+ob.getData()+", godz.lekc.: "+ob.getGodzinaLekcyjna();
				crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "poprawa","e-dziennik",zalogowanyUser.getSessionLogedKlient(),idUsers,nameUser.user(zalogowanyUser.getIdUsers()),ob.getIdObecnosc(),ob.getWartosc(), "Frekwencja",opis,ob.getSluchacze().getIdS()));
			
			}
			wartoscObSelected=null;
			selectedObecnosc=null;
		}
	
}
public void clearAllFilters() {
	
	wybranySluchacz=null;
	selectedObecnosc=null;
	DataTable datTab=(DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:sluchaczeObecnoscLista:dtObNb");
	if(datTab!=null) {
	datTab.reset();
	datTab.setValueExpression("sortBy", null);
	}
	}

private String user(int idUser) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idUser);
		us=crudAll.getAllTermsParam("findUserPoId", hms);
return us.get(0).getNazwiskoUsers()+" "+us.get(0).getImieUsers();
}
public serwis.wyborPlutonu.wyborPlutonu getWyborPlutonu() {
		return wyborPlutonu;
	}


	public void setWyborPlutonu(serwis.wyborPlutonu.wyborPlutonu wyborPlutonu) {
		this.wyborPlutonu = wyborPlutonu;
	}

	public String getWartoscObSelected() {
		return wartoscObSelected;
	}

	public void setWartoscObSelected(String wartoscObSelected) {
		this.wartoscObSelected = wartoscObSelected;
	}


	public List<Sluchacze> getSluchaczeSortedOb() {
		return sluchaczeSortedOb;
	}

	public boolean isRenderObDialog() {
		return renderObDialog;
	}

	public void setRenderObDialog(boolean renderObDialog) {
		this.renderObDialog = renderObDialog;
	}


	public void setSluchaczeSortedOb(List<Sluchacze> sluchaczeSortedOb) {
		this.sluchaczeSortedOb = sluchaczeSortedOb;
	}
	public Map<String, String> getWyborObecnoscList() {
		return wyborObecnoscList;
	}

	public void setWyborObecnoscList(Map<String, String> wyborObecnoscList) {
		this.wyborObecnoscList = wyborObecnoscList;
	}

	public List<Obecnosc> getSelectedObecnosc() {
		return selectedObecnosc;
	}

	public void setSelectedObecnosc(List<Obecnosc> selectedObecnosc) {
		this.selectedObecnosc = selectedObecnosc;
	}

public boolean isRender() {
	return render;
}


public Map<String, String> getWyborObecnoscList2() {
	return wyborObecnoscList2;
}

public void setWyborObecnoscList2(Map<String, String> wyborObecnoscList2) {
	this.wyborObecnoscList2 = wyborObecnoscList2;
}
public void setRender(boolean render) {
	this.render = render;
}
public Integer getWybranySluchacz() {
	return wybranySluchacz;
}

public void setWybranySluchacz(Integer wybranySluchacz) {
	this.wybranySluchacz = wybranySluchacz;
}


public Logowanie getZalogowanyUser() {
	return zalogowanyUser;
}


public void setZalogowanyUser(Logowanie zalogowanyUser) {
	this.zalogowanyUser = zalogowanyUser;
}
}

