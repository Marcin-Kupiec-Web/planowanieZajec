package serwis.sluchacze;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.model.chart.DonutChartModel;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;

@Named
@RequestScoped

public class Start implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CrudAll crudUniv;
	private DonutChartModel donutModel2;
	private List<Sluchacze> slAll;
	
	public Start() {
		// TODO Auto-generated constructor stub
	}
	@PostConstruct
	public void init() {
		createDonutModels();
	}
	
    private void createDonutModels() {
 
        donutModel2 = initDonutModel();
        donutModel2.setLegendPosition("e");
        donutModel2.setSliceMargin(7);
        donutModel2.setShowDataLabels(true);
        donutModel2.setDataFormat("value");
        donutModel2.setShadow(true);
        donutModel2.setExtender("customExtender");
    }
	
    private DonutChartModel initDonutModel() {
        DonutChartModel model = new DonutChartModel();
    	slAll=crudUniv.getAllTerms("Sluchacze.findAllSluchacze");
        int wszyscy=slAll.size();
        int podst=(int) slAll.stream().filter(slaf->slaf.getPluton().getIdKurs().getKurs().equals("podstawowy")).count();
        int specjal=(int) slAll.stream().filter(slaf->slaf.getPluton().getIdKurs().getKurs().equals("specjalistyczny")).count();
        
        Map<String, Number> circle2 = new LinkedHashMap<String, Number>();
        circle2.put("Wszyscy", 0);
        circle2.put("K. podstawowy",podst);
        circle2.put("Szk. pecjalistyczne", specjal);
        model.addCircle(circle2);
 
        Map<String, Number> circle3 = new LinkedHashMap<String, Number>();
        circle3.put("Wszyscy", wszyscy);
        circle3.put("K. podstawowy", 0);
        circle3.put("Szk. pecjalistyczne", 0);
        model.addCircle(circle3);
 
        return model;
    }
	public DonutChartModel getDonutModel2() {
		return donutModel2;
	}
	public void setDonutModel2(DonutChartModel donutModel2) {
		this.donutModel2 = donutModel2;
	}
	public List<Sluchacze> getSlAll() {
		return slAll;
	}
	public void setSlAll(List<Sluchacze> slAll) {
		this.slAll = slAll;
	}

}
