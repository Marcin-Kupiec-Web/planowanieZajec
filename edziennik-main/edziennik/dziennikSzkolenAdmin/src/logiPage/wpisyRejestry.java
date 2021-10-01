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
import com.edziennik.logi.model.Logi;

@Named
@ViewScoped
public class wpisyRejestry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAll crudLogi;
	
	private List<Logi> logi;
	private String iduF;
	private String dzialanieF;
	private String ipF;
	private String modolF;
	private String wpisujacyF;
	private String sesjaF;
	private String obiektF;
	private String opisF;
	private Map<String,Serializable> filterValues=new HashMap<>();
	
	public wpisyRejestry() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
    public void init() {
		logi=new ArrayList<Logi>();
	}

	public void findLogi() {
		filterValues.clear();
		DataTable datTab=(DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formLogi:dtLogi");
		if(datTab!=null) {
		datTab.reset();
		datTab.setValueExpression("sortBy", null);
		}
		logi.clear();
		if((dzialanieF!=null && !dzialanieF.equals("")) || (iduF!=null && !iduF.equals("")) || (ipF!=null && !ipF.equals("")) || (modolF!=null && !modolF.equals("")) || (wpisujacyF!=null && !wpisujacyF.equals("")) || (sesjaF!=null && !sesjaF.equals("")) || (obiektF!=null && !obiektF.equals("")) || (opisF!=null && !opisF.equals("")))
		{
		String dzialanieFl,iduFl,ipFl,modolFl,wpisujacyFl,sesjaFl,obiektFl,opisFl;
		
		if(dzialanieF==null)
			dzialanieFl="";
		else
			dzialanieFl=dzialanieF.trim();
		
		if(ipF==null)
			ipFl="";
		else
			ipFl=ipF.trim();
		
		if(modolF==null)
			modolFl="";
		else
			modolFl=modolF.trim();
		
		if(wpisujacyF==null)
			wpisujacyFl="";
		else
			wpisujacyFl=wpisujacyF.trim();
		
		if(sesjaF==null)
			sesjaFl="";
		else
			sesjaFl=sesjaF.trim();
		
		if(iduF==null)
			iduFl="";
		else
			iduFl=iduF.trim();
		
		if(obiektF==null)
			obiektFl="";
		else
			obiektFl=obiektF.trim();		

		if(opisF==null)
			opisFl="";
		else
			opisFl=opisF.trim();
		
		HashMap<String,Object> hms=new HashMap<String,Object>();
		
		hms.put("idUser", "%"+iduFl+"%");
		hms.put("ip", "%"+ipFl+"%");
		hms.put("dzialanie", "%"+dzialanieFl+"%");
		hms.put("kto", "%"+wpisujacyFl+"%");
		hms.put("modol", "%"+modolFl+"%");
		hms.put("sesja", "%"+sesjaFl+"%");
		hms.put("nazwaObiektu", "%"+obiektFl+"%");
		hms.put("opis", "%"+opisFl+"%");
		logi=crudLogi.getAllTermsParam("Logi.findLogiFinder", hms);
		}
	}
	
	public List<Logi> getLogi() {
		return logi;
	}

	public void setLogi(List<Logi> logi) {
		this.logi = logi;
	}

	public String getIduF() {
		return iduF;
	}

	public void setIduF(String iduF) {
		this.iduF = iduF;
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

	public String getWpisujacyF() {
		return wpisujacyF;
	}

	public void setWpisujacyF(String wpisujacyF) {
		this.wpisujacyF = wpisujacyF;
	}

	public String getModolF() {
		return modolF;
	}

	public void setModolF(String modolF) {
		this.modolF = modolF;
	}

	public String getObiektF() {
		return obiektF;
	}

	public void setObiektF(String obiektF) {
		this.obiektF = obiektF;
	}

	public String getSesjaF() {
		return sesjaF;
	}

	public void setSesjaF(String sesjaF) {
		this.sesjaF = sesjaF;
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
}
