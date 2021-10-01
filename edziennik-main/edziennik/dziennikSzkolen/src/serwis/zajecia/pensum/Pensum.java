package serwis.zajecia.pensum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.sluchacze.zajecia.model.Tematy;
import com.userManager.model.User;

import serwis.logowanie.Logowanie;
@Named("pensum")
@ViewScoped
public class Pensum  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private CrudAll crudAll;
	@Inject
	private Logowanie zalogowanyUser;
	@Inject
	private serwis.users.nameUser.NameUser nameUser;
	
	private Map<Integer,Integer>lata;
	private Integer rokWybrany;
	private Date miesiacRok;
	private List<Tematy> tematyList;
	private Map<String, String> nauczyciele;
	private List <User> nauczycieleList=new ArrayList<User>();
	private String userAdminSelect;
	public Pensum() {
		// TODO Auto-generated constructor stub
	}
	
	@PostConstruct
	public void init() {
	
		lata=new HashMap<Integer,Integer>();
		miesiacRok=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(miesiacRok);
		if(rokWybrany!=null) {
			c.set(Calendar.YEAR, rokWybrany);
		}
		setRokWybrany(c.get(Calendar.YEAR));
		
		 for(int i=c.get(Calendar.YEAR)-4;i<c.get(Calendar.YEAR)+4;i++) {
			 lata.put(i, i);
		 }
		 if(userAdminSelect==null)
		 userAdminSelect=nameUser.user(zalogowanyUser.getIdUsers());
		 
			HashMap<String, Object> hm=new HashMap<String, Object>();
			hm.put("ktoZrealizowal", "%"+userAdminSelect+"%");
			hm.put("zrealizowaneCzas",String.valueOf(rokWybrany));
			tematyList=crudAll.getAllTermsParam("Tematy.findDoPensum", hm);
			
			nauczyciele=new TreeMap<String, String>();
			setNauczycieleList(crudAll.getAllTerms("findAllUser"));
			for(User lus:nauczycieleList) {
				nauczyciele.put(lus.getNazwiskoUsers()+" "+lus.getImieUsers(), lus.getNazwiskoUsers()+" "+lus.getImieUsers());
			}
	}
	
	public void zmienMiesiacPodsumowanie() {
		clearAllFilters();
		init();
	}
	public void clearAllFilters() {
		
		DataTable datTab=(DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formPensumLista:pensumTable");
		if(datTab!=null) {
		datTab.reset();
		datTab.setValueExpression("sortBy", null);
		}
		}
	public List<Tematy> getTematyList() {
		return tematyList;
	}
	public void setTematyList(List<Tematy> tematyList) {
		this.tematyList = tematyList;
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

	public Map<String, String> getNauczyciele() {
		return nauczyciele;
	}

	public void setNauczyciele(Map<String, String> nauczyciele) {
		this.nauczyciele = nauczyciele;
	}

	public List <User> getNauczycieleList() {
		return nauczycieleList;
	}

	public void setNauczycieleList(List <User> nauczycieleList) {
		this.nauczycieleList = nauczycieleList;
	}

	
	public String getUserAdminSelect() {
		return userAdminSelect;
	}

	public void setUserAdminSelect(String userAdminSelect) {
		this.userAdminSelect = userAdminSelect;
	}

}
