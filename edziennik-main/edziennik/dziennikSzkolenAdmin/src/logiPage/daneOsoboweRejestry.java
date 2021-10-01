package logiPage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.logi.model.LogiDaneosobowe;

@Named
@ViewScoped
public class daneOsoboweRejestry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAll crudLogi;
	
	private List<LogiDaneosobowe> logi;
	private String idoF;
	private String dzialanieF;
	private String ipF;
	private String wpisujacyF;
	private String sesjaF;
	private String opisF;
	private Map<String,Serializable> filterValues=new HashMap<>();
	
	public daneOsoboweRejestry() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
    public void init() {
		logi=new ArrayList<LogiDaneosobowe>();
		logi=crudLogi.getAllTerms("LogiDaneosobowe.findAll");
	}

	public void findLogi() {
		filterValues.clear();
		DataTable datTab=(DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formLogi:dtLogi");
		if(datTab!=null) {
		datTab.reset();
		datTab.setValueExpression("sortBy", null);
		}
		logi.clear();
		if((dzialanieF!=null && !dzialanieF.equals("")) || (idoF!=null && !idoF.equals("")) || (ipF!=null && !ipF.equals("")) || (wpisujacyF!=null && !wpisujacyF.equals("")) || (sesjaF!=null && !sesjaF.equals("")) || (opisF!=null && !opisF.equals("")))
		{
		String dzialanieFl,idoFl,ipFl,wpisujacyFl,sesjaFl,opisFl;
		
		if(dzialanieF==null)
			dzialanieFl="";
		else
			dzialanieFl=dzialanieF.trim();
		
		if(ipF==null)
			ipFl="";
		else
			ipFl=ipF.trim();
				
		if(wpisujacyF==null)
			wpisujacyFl="";
		else
			wpisujacyFl=wpisujacyF.trim();
		
		if(sesjaF==null)
			sesjaFl="";
		else
			sesjaFl=sesjaF.trim();
		
		if(idoF==null)
			idoFl="";
		else
			idoFl=idoF.trim();
		
		if(opisF==null)
			opisFl="";
		else
			opisFl=opisF.trim();
		
		HashMap<String,Object> hms=new HashMap<String,Object>();
		hms.put("idOsoba", "%"+idoFl+"%");
		hms.put("kto", "%"+wpisujacyFl+"%");
		hms.put("ip", "%"+ipFl+"%");
		hms.put("dzialanie", "%"+dzialanieFl+"%");
		hms.put("sesja", "%"+sesjaFl+"%");
		hms.put("opis", "%"+opisFl+"%");
		logi=crudLogi.getAllTermsParam("LogiDaneosobowe.findLogiFinder", hms);
		}
	}
	
	
	public List<LogiDaneosobowe> getLogi() {
		return logi;
	}

	public void setLogi(List<LogiDaneosobowe> logi) {
		this.logi = logi;
	}

	public String getDzialanieF() {
		return dzialanieF;
	}

	public void setDzialanieF(String dzialanieF) {
		this.dzialanieF = dzialanieF;
	}

	public String getIpF() {
		return ipF;
	}

	public void setIpF(String ipF) {
		this.ipF = ipF;
	}

	public String getSesjaF() {
		return sesjaF;
	}

	public void setSesjaF(String sesjaF) {
		this.sesjaF = sesjaF;
	}

	public String getWpisujacyF() {
		return wpisujacyF;
	}

	public void setWpisujacyF(String wpisujacyF) {
		this.wpisujacyF = wpisujacyF;
	}

	public String getOpisF() {
		return opisF;
	}

	public void setOpisF(String opisF) {
		this.opisF = opisF;
	}

	public Map<String,Serializable> getFilterValues() {
		return filterValues;
	}

	public void setFilterValues(Map<String,Serializable> filterValues) {
		this.filterValues = filterValues;
	}

	public String getIdoF() {
		return idoF;
	}

	public void setIdoF(String idoF) {
		this.idoF = idoF;
	}
}
