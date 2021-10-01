package serwis.zajecia.tematy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Przedmiot;

@Named
@RequestScoped
public class CharBarTematy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;

	private Integer idPlutonWybrany;
	private List<com.edziennik.sluchacze.zajecia.model.Tematy> tematyPlutonWybrany;
	private List<com.edziennik.sluchacze.zajecia.model.Tematy> tematySorted;
	private boolean display;
	private Map<String,String> jm;
	private Map<String,String> js;
	private String styleChart;
	
	public CharBarTematy() {
		// TODO Auto-generated constructor stub
	}
	
    private HorizontalBarChartModel horizontalBarModel;
    private BarChartModel barModel;
 
    @PostConstruct
    public void init() {
    	wyborPlutonu.prepareChangeSzkolenie();
    	idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
 
    	HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idPluton", idPlutonWybrany);
		tematyPlutonWybrany=crudAll.getAllTermsParam("Tematy.findTemNotCross", hms);
		
    	if(tematyPlutonWybrany!=null && tematyPlutonWybrany.size()>0) {
        	jm=new TreeMap<String,String>();
        	js=new TreeMap<String,String>();
    		display=true;
    		tematySorted=tematyPlutonWybrany;
    		createHorizontalBarModel() ;
    	}
    	else
    		display=false;
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }
 
 
 private void createHorizontalBarModel() {
	  	
       	Map<String,ChartSeries> chsarrl=new TreeMap<String,ChartSeries>();
    	
    	if(tematyPlutonWybrany!=null) {
    	Pluton plutonWybrany=tematyPlutonWybrany.get(0).getPluton();
		List<Przedmiot> lp=plutonWybrany.getIdKurs().getPrzedmiots();
		
			for(Przedmiot lpa:lp) {
				jm.put(lpa.getJm(), lpa.getJm());
				if(lpa.getJs()!=null)
				js.put(lpa.getJs(),lpa.getJs());
				  ChartSeries chs = new ChartSeries();
			
				  if(lpa.getJs()!=null)
				  chsarrl.put(lpa.getJs(),chs);
			}
       	}
 if(chsarrl.size()>0) {
	 horizontalBarModel=new HorizontalBarChartModel();
		    	for(Entry<String, ChartSeries> chses:chsarrl.entrySet()) {
		    		chses.getValue().setLabel(chses.getKey());
		    		
		    		   for(Entry<String,String> jms:jm.entrySet()) {
		    	        	if(js.size()>0)
		    	        	chses.getValue().set(jms.getKey(), tematySorted.stream().filter(tem->tem.getJS_tematy()!=null && tem.getJS_tematy().equals(chses.getKey()) && tem.getJM_tematy().equals(jms.getKey())).count());
		    
		    		   }
		    		      horizontalBarModel.addSeries(chses.getValue());
		    	}
		    
		        horizontalBarModel.setAnimate(true);
		        horizontalBarModel.setShadow(false);
		        horizontalBarModel.setTitle("Zrealizowane tematy");
		        horizontalBarModel.setLegendPosition("ne");
		        horizontalBarModel.setStacked(true);
		        
		        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
		        xAxis.setLabel("Godziny lekcyjne");
		        xAxis.setMin(0);
		        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
		        yAxis.setLabel(wyborPlutonu.getJmpw()+" / "+wyborPlutonu.getJspw());    
		        
		        horizontalBarModel.setExtender("customExtender");
		 }else {
			 barModel=new BarChartModel();
			 ChartSeries jmchs=new ChartSeries();
			 jmchs.setLabel(wyborPlutonu.getJmpw());
			 
			 for(Entry<String,String>jmes:jm.entrySet()) {
				 jmchs.set(jmes.getKey(),tematySorted.stream().filter(tmf->tmf.getJM_tematy().equals(jmes.getKey())).count());
			 }
			 barModel.addSeries(jmchs);
			 barModel.setTitle("Zrealizowane tematy");
			 Axis xAxis = barModel.getAxis(AxisType.X);
		        xAxis.setLabel(wyborPlutonu.getJmpw()+" / "+wyborPlutonu.getKpnpw());
		        xAxis.setMin(0);
		        Axis yAxis = barModel.getAxis(AxisType.Y);
		        yAxis.setLabel("Liczba godzin lekcyjnych");    
		        
		       barModel.setExtender("customExtender");
		 }
 }

	public Integer getIdPlutonWybrany() {
		return idPlutonWybrany;
	}
	public void setIdPlutonWybrany(Integer idPlutonWybrany) {
		this.idPlutonWybrany = idPlutonWybrany;
	}
    public serwis.wyborPlutonu.wyborPlutonu getWyborPlutonu() {
		return wyborPlutonu;
	}
	public void setWyborPlutonu(serwis.wyborPlutonu.wyborPlutonu wyborPlutonu) {
		this.wyborPlutonu = wyborPlutonu;
	}
	public String getStyleChart() {
		return styleChart;
	}
	public void setStyleChart(String styleChart) {
		this.styleChart = styleChart;
	}
	public List<com.edziennik.sluchacze.zajecia.model.Tematy> getTematySorted() {
		return tematySorted;
	}
	public void setTematySorted(List<com.edziennik.sluchacze.zajecia.model.Tematy> tematySorted) {
		this.tematySorted = tematySorted;
	}
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public Map<String,String> getJm() {
		return jm;
	}
	public void setJm(Map<String,String> jm) {
		this.jm = jm;
	}
	public Map<String,String> getJs() {
		return js;
	}
	public void setJs(Map<String,String> js) {
		this.js = js;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}
}
