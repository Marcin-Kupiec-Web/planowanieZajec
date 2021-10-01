package serwis.szkoleniaLine;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Pluton;

@Named
@RequestScoped
public class Szkolenia {
	@EJB
	private CrudAllLocal crudAll;
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
    private TimelineModel model;  
   private String move;
   private List<Pluton> plutonyTerminarz;

	@PostConstruct 
    public void init() { 
    		
    		model=null;
    		HashMap<String, Object> hmsp=new HashMap<String,Object>();
    		hmsp.put("archiwum","NIE");
    		setPlutonyTerminarz(crudAll.getAllTermsParam("Pluton.findSzkolenia", hmsp));
    		
        // groups  
        // create timeline model  
        model = new TimelineModel();  
        for(Pluton plutTime:plutonyTerminarz) {
        	
        	TimelineEvent event = new TimelineEvent(plutTime.getOznaczenieSzkolenia(), plutTime.getTerminOd(), plutTime.getTerminDo(), false, plutTime.getIdKurs().getNazwa(), "timelineKomp"+plutTime.getKompania().getNazwaKompania().toString());  
            model.add(event);  
        }
      
        move=null;
    }  
    public String getMove() {
	return move;
}

public void setMove(String move) {
	this.move = move;
}

    public TimelineModel getModel() {  
        return model;  
    }  
   
	public List<Pluton> getPlutonyTerminarz() {
		return plutonyTerminarz;
	}
	public void setPlutonyTerminarz(List<Pluton> plutonyTerminarz) {
		this.plutonyTerminarz = plutonyTerminarz;
	}  

}
