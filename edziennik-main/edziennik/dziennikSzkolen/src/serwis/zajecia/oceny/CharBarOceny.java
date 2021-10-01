package serwis.zajecia.oceny;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.edziennik.sluchacze.zajecia.model.Ocena;
import com.edziennik.sluchacze.zajecia.model.OcenyNaglowek;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.userManager.model.UsersStudent;

import my.util.SluchaczCompare;
import serwis.logowanie.Logowanie;

@Named
@RequestScoped
public class CharBarOceny implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private Logowanie zalogowanyUser;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu;

	
	private Integer idPlutonWybrany;
	private List<Pluton> plutonWybrany;
	private List<Sluchacze> sluchaczeSorted;
	private String styleChart;
	private List<OcenyNaglowek> naglowekList;
	
	public CharBarOceny() {
	}

	private BarChartModel barModel;
    private HorizontalBarChartModel horizontalBarModel=new HorizontalBarChartModel();
 
    @PostConstruct
    public void init() {
    	wyborPlutonu.prepareChangeSzkolenie();
    	idPlutonWybrany=wyborPlutonu.getIdPlutonWybrany();
    	sluchaczeSorted=new ArrayList<Sluchacze>();
    	HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idPluton", idPlutonWybrany);
		 plutonWybrany=crudAll.getAllTermsParam("Pluton.findSluchaczePoIdPluton", hms);
		 
			if(plutonWybrany!=null && plutonWybrany.size()>0) {
				if(zalogowanyUser.getPoziomUprawnien().equals("7")) {
					HashMap<String, Object> hmsl=new HashMap<String,Object>();
					hmsl.put("idUsers", zalogowanyUser.getIdUsers());
			        try {
			        	UsersStudent sl=(UsersStudent) crudAll.getAllTermsParam("findUserPoIdus", hmsl).get(0);
			        	sluchaczeSorted.add(sl.getSluchacze());
			        } catch(IndexOutOfBoundsException e) {
			            System.err.println("Nie ma takiego słuchacza!");
			        }
				}
				else {
					sluchaczeSorted=plutonWybrany.get(0).getSluchaczes().stream().distinct().collect(Collectors.toList());
					Collections.sort(sluchaczeSorted,new SluchaczCompare());
				}
				HashMap<String, Object> hmsp=new HashMap<String,Object>();
				hmsp=new HashMap<String,Object>();
				hmsp.put("idPluton", plutonWybrany.get(0).getIdPluton());
				hmsp.put("usunieta", false);
				naglowekList=crudAll.getAllTermsParam("OcenyNaglowek.findPoPluton", hmsp);
			}
    	
    	
    	if(sluchaczeSorted!=null && sluchaczeSorted.size()>0) {
    		if(!zalogowanyUser.getStatus().equals("sluchacz")) {
    		styleChart=String.valueOf(sluchaczeSorted.size()*42+100)+"px";
    		}
    		else
    			styleChart="200px";
    		createHorizontalBarModel();
    	}
    }

    public BarChartModel getBarModel() {
        return barModel;
    }
     
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }
     
    private HorizontalBarChartModel createHorizontalBarModel() {
    	
    	horizontalBarModel=new HorizontalBarChartModel();
       
        //ChartSeries biezace = new ChartSeries();
        ChartSeries biezacePoprawione = new ChartSeries();
       // ChartSeries biezaceOkres = new ChartSeries();
        
        //biezace.setLabel("Średnia ocen biezacych");
        biezacePoprawione.setLabel("Średnia ocen biezacych poprawionych");
        
        for(Sluchacze sluch:sluchaczeSorted) {

    		
    		double liczOcBiezPopraw=0;
    		double sredniaBiezacePopraw=0;
    		
    		//double liczOcOkres=0;
    		//double sredniaOkres=0;
    	
    		if(sluch.getOcenas()!=null) {
    			List<Ocena> ocr=new LinkedList<Ocena>();
    			for(OcenyNaglowek ocl:naglowekList) {
    				
    				if(ocl.isDoSredniej() && ocl.getOcenas()!=null && !ocl.isOkresowa()) {
    				ocr.addAll(ocl.getOcenas().stream().filter(ocf->ocf.getSluchacze().getIdS()==sluch.getIdS() && !ocf.getUsunieta()  && 
    							!ocf.getOcenaWartosc().equals("nzal") && 
    							!ocf.getOcenaWartosc().equals("1") &&
    							!ocf.getOcenaWartosc().equals("zal")).collect(Collectors.toList()));
    					}
    			}
    			if(ocr.size()>0)
    			for (Ocena ocsrednia:ocr) {
					liczOcBiezPopraw++;
	    			sredniaBiezacePopraw+=Double.valueOf(ocsrednia.getOcenaWartosc());
				}
    			if(liczOcBiezPopraw>0)
            		biezacePoprawione.set(sluch.getNazwiskoSluchacz()+" "+sluch.getImieSluchacz()+" ("+sluch.getNrDziennik()+")", sredniaBiezacePopraw/liczOcBiezPopraw);
        		else
        			biezacePoprawione.set(sluch.getNazwiskoSluchacz()+" "+sluch.getImieSluchacz()+" ("+sluch.getNrDziennik()+")", 0);
    		}
  
    	
    	
    	
    }
        
     
       if(biezacePoprawione!=null)
        horizontalBarModel.addSeries(biezacePoprawione);
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setShadow(false);
        horizontalBarModel.setTitle("Średnie ocen");
        horizontalBarModel.setLegendPosition("ne");
        horizontalBarModel.setStacked(true);
        
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Średnia");
        xAxis.setMin(0);
         
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Słuchacze");    
        
        horizontalBarModel.setExtender("customExtender");
        return horizontalBarModel;
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
	public List<Sluchacze> getSluchaczeSorted() {
		return sluchaczeSorted;
	}

	public void setSluchaczeSorted(List<Sluchacze> sluchaczeSorted) {
		this.sluchaczeSorted = sluchaczeSorted;
	}

	public String getStyleChart() {
		return styleChart;
	}

	public void setStyleChart(String styleChart) {
		this.styleChart = styleChart;
	}

	public List<OcenyNaglowek> getNaglowekList() {
		return naglowekList;
	}

	public void setNaglowekList(List<OcenyNaglowek> naglowekList) {
		this.naglowekList = naglowekList;
	}

	
}
