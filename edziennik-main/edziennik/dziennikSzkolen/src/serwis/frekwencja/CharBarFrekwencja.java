package serwis.frekwencja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.edziennik.sluchacze.zajecia.model.Obecnosc;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.userManager.model.UsersStudent;

import my.util.SluchaczCompare;
import serwis.logowanie.Logowanie;

@Named
@RequestScoped
public class CharBarFrekwencja implements Serializable{
	
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
	
	public CharBarFrekwencja() {
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
			}
    	
    	
    	if(sluchaczeSorted!=null && sluchaczeSorted.size()>0) {
    		if(!zalogowanyUser.getPoziomUprawnien().equals("7")) {
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
       
    	ChartSeries spoznienia = new ChartSeries();
        ChartSeries usprawiedliwione = new ChartSeries();
        ChartSeries nieusprawiedliwione = new ChartSeries();
        
        usprawiedliwione.setLabel("Usprawiedliwione");
        nieusprawiedliwione.setLabel("Nieusprawiedliwione");
        spoznienia.setLabel("Spóznienia");
        
        for(Sluchacze sluch:sluchaczeSorted) {
     	    int liczUspr=0;
        	int liczNb=0;
        	int liczSp=0;
    		
    		for(Obecnosc obec:sluch.getObecnoscs()) {
    	
		    		if(obec.getWartosc().equals("Sp")) {
		    			liczSp++;
		    		}
		    		else if(obec.getWartosc().equals("N")) {
		    			liczNb++;
		    		}
		    		else if(!obec.getWartosc().equals("Ob") && !obec.getWartosc().equals("-")) {
		    			liczUspr++;
		    		}
       	}
    		
    		nieusprawiedliwione.set(sluch.getNazwiskoSluchacz()+" "+sluch.getImieSluchacz()+" ("+sluch.getNrDziennik()+")", liczNb);
    		usprawiedliwione.set(sluch.getNazwiskoSluchacz()+" "+sluch.getImieSluchacz()+" ("+sluch.getNrDziennik()+")", liczUspr);
    		spoznienia.set(sluch.getNazwiskoSluchacz()+" "+sluch.getImieSluchacz()+" ("+sluch.getNrDziennik()+")", liczSp);
    }
        
     
 
        horizontalBarModel.addSeries(usprawiedliwione);
        horizontalBarModel.addSeries(nieusprawiedliwione);
        horizontalBarModel.addSeries(spoznienia);
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setShadow(false);
        horizontalBarModel.setTitle("Frekwencja słuchaczy");
        horizontalBarModel.setLegendPosition("ne");
        horizontalBarModel.setStacked(true);
        
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Wartość");
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
	
}
