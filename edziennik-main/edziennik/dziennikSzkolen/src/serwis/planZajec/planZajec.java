package serwis.planZajec;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.planZajec.model.PlanZajecZaplanowane;
import com.godzinyZajec.model.Godzinyzajec;
import com.userManager.model.User;

import serwis.logowanie.Logowanie;


@Named("planZajec")
@RequestScoped
public class planZajec {
	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private Logowanie zalogowanyUser;
	
	private List<User> userZaklad;
	private List<PlanZajecZaplanowane> planyLista;
    private TimelineModel model;  
    private Date start;  
    private Date end;  
   private Date rok=new Date();
   private String zaklad;
   private List<String> zaklady;
   private String move;
   private List<User> nauczycieleWolni;
	@PostConstruct 
    public void init() { 
		nauczycieleWolni=new ArrayList<User>();
		if(zaklad==null)
			zaklad=zalogowanyUser.getZakladUser();
    		zaklady=new ArrayList<String>();
    		zaklady=crudAll.getAllTerms("findZakladPoUsers");
    		model=null;
        // set initial start / end dates for the axis of the timeline  

    	HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("zakladUsers", zaklad);
		userZaklad=crudAll.getAllTermsParam("findUserPoZaklad",hms);
		
    	Calendar cal = Calendar.getInstance();  
    	cal.setTime(rok);
    		if(move!=null)
			{
				if(move.equals("prev")) {
					cal.add(Calendar.DATE, -1);
				} else if(move.equals("next")) {
					cal.add(Calendar.DATE, +1);
				}else
					cal.setTime(new Date());
			}
    		
    		rok=cal.getTime();
				
    	// cal.set(Calendar.AM_PM, Calendar.AM);
    	// cal.set(Calendar.HOUR,7);
    	cal.add(Calendar.HOUR_OF_DAY, -1);
    	 Date now = cal.getTime();  
         cal.setTimeInMillis(now.getTime());  
         start = cal.getTime();  
           
         cal.setTimeInMillis(now.getTime() + 2 * 60 * 60 * 1000);  
         end = cal.getTime();   
   
        // groups  
        // create timeline model  
        model = new TimelineModel();  
        List<Godzinyzajec> gzl=new ArrayList<Godzinyzajec>();
        gzl=crudAll.getAllTerms("Godzinyzajec.findAll");
      
        
        for(Godzinyzajec gz:gzl) {
        	Calendar cal2 = Calendar.getInstance();  
        	cal2.setTime(gz.getGodzOd());
        	
        	cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
        	cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
        	
        	SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
    
        	
        	Date starte=cal.getTime();
        	
        	cal2.setTime(gz.getGodzDo());
        	
        	cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
        	cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
        	
        	Date ende=cal.getTime();
        	
        	String kolor="godzinyZajec";
        	if(cal2.get(Calendar.HOUR_OF_DAY)>15) 
        		kolor="godzinyZajecPlus";
        	TimelineEvent event = new TimelineEvent(gz.getNrGodzina()+") "+sdf.format(gz.getGodzOd())+"-"+sdf.format(gz.getGodzDo()), starte, ende, false, "Godziny zajęć:", kolor);  
        
            model.add(event);  
        }
        
        for (User userZ : userZaklad) { 
        	
        	HashMap<String, Object> hmsp=new HashMap<String,Object>();
    		hmsp.put("imie", userZ.getImieUsers().trim());
    		hmsp.put("nazwisko", userZ.getNazwiskoUsers().trim());
    		hmsp.put("rok", cal.get(Calendar.YEAR));
            planyLista=crudAll.getAllTermsParam("PlanZajecZaplanowane.findImieNazwisko", hmsp);
           if(planyLista!=null && planyLista.size()>0) {
            for(PlanZajecZaplanowane planZ : planyLista) {
            	{
               	String explStr[];
            	explStr=planZ.getGodzina().split("-");
            	String explGodzStart[];
            	String explGodzEnd[];
            	explGodzStart=explStr[0].split("\\.");
            	explGodzEnd=explStr[1].split("\\.");
             	
            	cal.set(Calendar.YEAR, planZ.getRok());
            	cal.set(Calendar.MONTH, Integer.parseInt(planZ.getMiesiac())-1);
            	cal.set(Calendar.DAY_OF_MONTH, planZ.getDzien());
            
        		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(explGodzStart[0].trim()));
        		cal.set(Calendar.MINUTE, Integer.parseInt(explGodzStart[1].trim()));
        	Date starte=cal.getTime();
        		
	        	cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(explGodzEnd[0].trim()));
	        	cal.set(Calendar.MINUTE, Integer.parseInt(explGodzEnd[1].trim()));	
        	Date ende=cal.getTime();
          String przedmiotTemat=planZ.getKlasa().replace("\n","").replace("\r","");
          String godziny=planZ.getGodzina().replace("\n","").replace("\r","");
        	TimelineEvent event = new TimelineEvent(godziny+"<br />"+przedmiotTemat, starte, ende, false, userZ.getNazwiskoUsers()+" "+userZ.getImieUsers(), "available");  
            model.add(event);  
        	
            }
           }
        }else {
        	nauczycieleWolni.add(userZ);
        }
            
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
   
    public Date getStart() {  
        return start;  
    }  
   
    public Date getEnd() {  
        return end;  
    }

	public Date getRok() {
		return rok;
	}

	public void setRok(Date rok) {
		this.rok = rok;
	}

	public String getZaklad() {
		return zaklad;
	}

	public void setZaklad(String zaklad) {
		this.zaklad = zaklad;
	}

	public List<String> getZaklady() {
		return zaklady;
	}

	public void setZaklady(List<String> zaklady) {
		this.zaklady = zaklady;
	}  
	
	public Logowanie getZalogowanyUser() {
		return zalogowanyUser;
	}
	public void setZalogowanyUser(Logowanie zalogowanyUser) {
		this.zalogowanyUser = zalogowanyUser;
	}
	public List<User> getNauczycieleWolni() {
		return nauczycieleWolni;
	}
	public void setNauczycieleWolni(List<User> nauczycieleWolni) {
		this.nauczycieleWolni = nauczycieleWolni;
	}

}
