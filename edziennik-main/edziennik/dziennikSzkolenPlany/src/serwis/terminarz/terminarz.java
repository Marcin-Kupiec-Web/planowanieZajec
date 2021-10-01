package serwis.terminarz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Kompania;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.terminarz.model.Terminarz;
import com.userManager.model.User;
import com.userManager.model.UsersStudent;

import serwis.logowanie.Logowanie;



@Named
@ViewScoped
public class terminarz implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public terminarz() {
		// TODO Auto-generated constructor stub
	}

		@EJB
		private CrudAllLocal crudAll;
		@Inject
		private Logowanie zalogowanyUser;
		
	private List<User> userWpisal;
	private ScheduleModel eventModel;
    private ScheduleModel lazyEventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private Terminarz terminarzEvent=new Terminarz();
    private List<Terminarz> wpisyTerminarz;
    private String kolor;
    private boolean edycja=false;
    private boolean pokazDialog;
    private Map <String,Object> mapTerminarz=new HashMap<String,Object>();
    private String kompania;
	private String pluton;
	private Integer idPluton;
	private String szkolenie;
	private List<Kompania> kompanieList;
	private List<Pluton> plutonyList;
	private List<String> plutonListSzkolenie=new ArrayList<String>();
	private List<String> plutonListNazwa;
	private List<String> kompanieListNazwa;
	private Pluton plutonWybrany;
  	FacesContext fct=FacesContext.getCurrentInstance();
	 	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
	@PostConstruct
    public void init() {
	 
		kompanieList=new ArrayList<Kompania>();
    	kompanieListNazwa=new ArrayList<String>();
    	plutonyList=new ArrayList<Pluton>();
    	plutonListNazwa=new ArrayList<String>();
			
		HashMap<String,Object>hmpk=new HashMap<String, Object>();
		hmpk.put("archiwum","NIE");
		kompanieList=crudAll.getAllTermsParam("Kompania.findAll",hmpk);;
		
		for(Kompania kompStr:kompanieList) {
			kompanieListNazwa.add(kompStr.getNazwaKompania());
		}
		HashMap<String, Object> hmsl=new HashMap<String,Object>();
		hmsl.put("idUsers", (int) session.getAttribute("idUser"));
        try {
        	UsersStudent sl=(UsersStudent) crudAll.getAllTermsParam("findUserPoIdus", hmsl).get(0);
        	idPluton=sl.getSluchacze().getPluton().getIdPluton();
        } catch(IndexOutOfBoundsException e) {
        }
        
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("ktoWpisal", (int) session.getAttribute("idUser"));
		hms.put("zaklad", session.getAttribute("zaklad"));
		
		if(Integer.parseInt((String) session.getAttribute("poziomUprawnien"))<7) {
			hms.put("idPluton", "%");
		}else
		hms.put("idPluton", idPluton);
 		wpisyTerminarz=crudAll.getAllTermsParam("Terminarz.findAllowed",hms);
      
    	eventModel = new DefaultScheduleModel();
    	
    	for(Terminarz termServ:wpisyTerminarz) {
    		if(termServ.getTresc()!=null && termServ.getTerminOd()!=null && termServ.getTerminDo()!=null) {
    			String kolor=termServ.getDoWiadomosci();
    			if(termServ.getPlutont()!=null)
    				kolor="pluton";
    			ScheduleEvent wstawEvent=new DefaultScheduleEvent(termServ.getTresc(), termServ.getTerminOd(), termServ.getTerminDo(),kolor);
    			((DefaultScheduleEvent) wstawEvent).setAllDay(termServ.isCalyDzien());
    			//((DefaultScheduleEvent) wstawEvent).setDescription(termServ.getTresc()+"<br /> dodano: "+termServ.getDataWpisu().get(Calendar.DAY_OF_MONTH)+" / "+termServ.getDataWpisu().get(Calendar.MONTH)+" / "+termServ.getDataWpisu().get(Calendar.YEAR));
    		
    			eventModel.addEvent(wstawEvent);
    			mapTerminarz.put(wstawEvent.getId(), termServ);
    		}
    	}
    	
         
        lazyEventModel = new LazyScheduleModel() {
             
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void loadEvents(Date start, Date end) {
                Date random = getRandomDate(start);
                addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));
                 
                random = getRandomDate(start);
                addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
            }   
        };
    }
   
	public String ktoWpisalString(int idWpisal) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idWpisal);
		userWpisal=crudAll.getAllTermsParam("findUserPoId",hms);
		String osoba="";
    	if(userWpisal.size()!=0) {
    		osoba=userWpisal.get(0).getNazwiskoUsers();
    		if(userWpisal.get(0).getZakladToString()!=null) {
    			osoba+=" / "+userWpisal.get(0).getZakladToString();
    		}
    		return osoba;
    	}		
    	return "Imię Nazwisko";
    }
    
 
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
 
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
     
    public void addEvent(ActionEvent actionEvent) {
 
 		if(event.getId() == null) {
 			 if(edycja) {
 			terminarzEvent.setTerminOd(event.getStartDate());
 			terminarzEvent.setTerminDo(event.getEndDate());
 			terminarzEvent.setTresc(event.getTitle());
 			terminarzEvent.setCalyDzien(event.isAllDay());
 			terminarzEvent.setKolor(kolor);
        	if(plutonWybrany!=null)
        		terminarzEvent.setDoWiadomosci("pluton");
        	else
        	terminarzEvent.setDoWiadomosci(terminarzEvent.getDoWiadomosci());
 			terminarzEvent.setKtoWpisal((int) session.getAttribute("idUser"));
 			terminarzEvent.setDataWpisu(today());
 			terminarzEvent.setPlutont(plutonWybrany);
            crudAll.create(terminarzEvent);
            ((DefaultScheduleEvent) event).setStyleClass(terminarzEvent.getDoWiadomosci());
            eventModel.addEvent(event);
            mapTerminarz.put(event.getId(),terminarzEvent);
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Zapisano", "Zdarzenie.");
  	   		addMessage(message);
 			
  	   	 event = new DefaultScheduleEvent();
         terminarzEvent=new Terminarz();
         
      	PrimeFaces current = PrimeFaces.current();
 		current.executeScript("PF('eventDialog').hide()");
 		
 		} 
      }
        else {
        	if(terminarzEvent.getDoWiadomosci()!=null && event.getStartDate()!=null && event.getStartDate()!=null && event.getEndDate()!=null && event.getTitle()!=null && !event.getTitle().trim().equals(""))
        	{ if(edycja) {        	
		        	 if((int)session.getAttribute("idUser")==terminarzEvent.getKtoWpisal() || session.getAttribute("poziomUprawnien").equals("1")) {
				        	terminarzEvent.setTerminOd(event.getStartDate());
				        	terminarzEvent.setTerminDo(event.getEndDate());
				        	terminarzEvent.setTresc(event.getTitle());
				        	terminarzEvent.setCalyDzien(event.isAllDay());
				        	terminarzEvent.setKolor(kolor);
				        	if(plutonWybrany!=null)
				        		terminarzEvent.setDoWiadomosci("pluton");
				        	else
				        	terminarzEvent.setDoWiadomosci(terminarzEvent.getDoWiadomosci());
				        	terminarzEvent.setKtoWpisal((int)session.getAttribute("idUser"));
				        	terminarzEvent.setDataWpisu(today());
				        	terminarzEvent.setPlutont(plutonWybrany);
				        	crudAll.update(terminarzEvent);     
				              ((DefaultScheduleEvent) event).setStyleClass(terminarzEvent.getDoWiadomosci());
				              eventModel.updateEvent(event);
				     		
				     	 	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Uaktualniono dane", "Zdarzenie.");
		          	   		addMessage(message);
		          	   		
		          	     	PrimeFaces current = PrimeFaces.current();
				     		current.executeScript("PF('eventDialog').hide()");
				     		event = new DefaultScheduleEvent();
			                terminarzEvent=new Terminarz();
		        	 }
		        	 	else {
		        		 	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie zapisano", "Brak uprawnień.");
		          	   		addMessage(message);
		        	 		}
		        	 
        }
     }    
   }	
 		
		
    }
    
    public void removeEvent(ActionEvent actionEvent) {
    	terminarzEvent=(Terminarz) mapTerminarz.get(event.getId());
    	crudAll.delete(terminarzEvent);
    	eventModel.deleteEvent(event);
    	terminarzEvent=new Terminarz();
    	plutonWybrany=null;
    	
	 	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usunięto. ", "Zdarzenie.");
	   		addMessage(message);
    }
     

	public void onEventSelect(SelectEvent selectEvent) {
	  	plutonWybrany=null;
    	kompania=null;
    	pluton=null;
    	szkolenie=null;
    	
        event = (ScheduleEvent) selectEvent.getObject();
        terminarzEvent=(Terminarz) mapTerminarz.get(event.getId());
        plutonWybrany=terminarzEvent.getPlutont();
        if(plutonWybrany!=null) {
        	kompania=plutonWybrany.getKompania().getNazwaKompania();
        	pluton=plutonWybrany.getNazwaPluton();
        	szkolenie=plutonWybrany.getOznaczenieSzkolenia();
        	
        	for(Kompania kompStr:kompanieList) {
				if(kompStr.getNazwaKompania().equals(kompania)) {
					plutonyList=kompStr.getPlutons();
					 for(Pluton plutStr:plutonyList) {
						 if(!plutonListNazwa.contains(plutStr.getNazwaPluton()))
						 plutonListNazwa.add(plutStr.getNazwaPluton());
					 } 
				}
			}


    		for(Pluton plutStr:plutonyList) {
				if(plutStr.getNazwaPluton().equals(pluton)) {
					if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
						plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
					}
				
				}
			}
        }
    }
     
	public boolean edycjaListener() {
		setPokazDialog(false);
		return edycja;
	}
	
    public void onDateSelect(SelectEvent selectEvent) {
       	plutonWybrany=null;
    	kompania=null;
    	pluton=null;
    	szkolenie=null;
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject(),"wpisujacy");
        terminarzEvent=new Terminarz(); 
        terminarzEvent.setDoWiadomosci("wpisujacy");
     	  if(edycja) {
			  	        setPokazDialog(true);
     	 			}
     	  			else
     	  				setPokazDialog(false);
    }
  
    public void onEventMove(ScheduleEntryMoveEvent event) {
       if(edycja) {
    	 terminarzEvent=(Terminarz) mapTerminarz.get(event.getScheduleEvent().getId());

    	 if((int)session.getAttribute("idUser")==terminarzEvent.getKtoWpisal()) {
    	if(event.getMinuteDelta()==0 && event.getDayDelta()==0){
    		terminarzEvent.setCalyDzien(true);
    		((DefaultScheduleEvent) event.getScheduleEvent()).setAllDay(true);
  
   
    	}else {
    		
    		terminarzEvent.setCalyDzien(false);
    		((DefaultScheduleEvent) event.getScheduleEvent()).setAllDay(false);
    	}
    	terminarzEvent.setTerminOd(((DefaultScheduleEvent) event.getScheduleEvent()).getStartDate());
    	terminarzEvent.setTerminDo(((DefaultScheduleEvent) event.getScheduleEvent()).getEndDate());
    	terminarzEvent.setTresc(((DefaultScheduleEvent) event.getScheduleEvent()).getTitle());
    	terminarzEvent.setKolor(kolor);
    	terminarzEvent.setDoWiadomosci(terminarzEvent.getDoWiadomosci());
    	terminarzEvent.setKtoWpisal((int)session.getAttribute("idUser"));
    	terminarzEvent.setDataWpisu(today());
    	crudAll.update(terminarzEvent);
    	
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Przeniesiono, uaktualniono", "Nowy termin: "+event.getScheduleEvent().getStartDate()+" - "+event.getScheduleEvent().getEndDate());
    		addMessage(message);
    	 }
    	 else {
    		 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie zapisano", "Brak uprawnień.");
      	   	addMessage(message);
    	 }
    	  terminarzEvent=new Terminarz();
       }
       else {
    	   FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie zapisano", "Włacz edycję i zapisz.");
    	   addMessage(message);
       }
     
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
    if(edycja) {
 		terminarzEvent=(Terminarz) mapTerminarz.get(event.getScheduleEvent().getId());
 		 if((int)session.getAttribute("idUser")==terminarzEvent.getKtoWpisal()) {
    	terminarzEvent.setTerminOd(event.getScheduleEvent().getStartDate());
    	terminarzEvent.setTerminDo(event.getScheduleEvent().getEndDate());
    	terminarzEvent.setTresc(event.getScheduleEvent().getTitle());
    	terminarzEvent.setCalyDzien(event.getScheduleEvent().isAllDay());
    	terminarzEvent.setKolor(kolor);
    	terminarzEvent.setDoWiadomosci(terminarzEvent.getDoWiadomosci());
    	terminarzEvent.setKtoWpisal((int)session.getAttribute("idUser"));
    	terminarzEvent.setDataWpisu(today());
    	crudAll.update(terminarzEvent);
    	
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Uaktualniono", "Zmieniono przedział czasowy.");
        addMessage(message);
 		 }
 		 else {
    		 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie zapisano", "Brak uprawnień.");
      	   	addMessage(message);
    	 }
    }
    else {
 	   FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie zapisano", "Włacz edycję i zapisz.");
 	   addMessage(message);
    }
    terminarzEvent=new Terminarz();
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    
	 public void changeKompania() {
			plutonListNazwa.clear();
			plutonListSzkolenie.clear();
			pluton=null;
			szkolenie=null;
			setPlutonWybrany(null);
			
			for(Kompania kompStr:kompanieList) {
				if(kompStr.getNazwaKompania().equals(kompania)) {
					plutonyList=kompStr.getPlutons();
					 for(Pluton plutStr:plutonyList) {
						 if(!plutonListNazwa.contains(plutStr.getNazwaPluton()))
						 plutonListNazwa.add(plutStr.getNazwaPluton());
					 } 
				}
			}
			
		}
		public void changePluton() {
			plutonListSzkolenie.clear();
			szkolenie=null;
			setPlutonWybrany(null);
			for(Pluton plutStr:plutonyList) {
				if(plutStr.getNazwaPluton().equals(pluton)) {
					if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
						plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
					}
				
				}
			}
			
		}

		public void changeSzkolenie() {
			
			setPlutonWybrany(null);
				
			for(Pluton plutStr:plutonyList) {
				if(plutStr.getNazwaPluton().equals(pluton) && plutStr.getOznaczenieSzkolenia().equals(szkolenie)) {
					if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
						plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
						
					}
						//zeby nie było duplikatow
					setPlutonWybrany(plutStr);
				}
			}
		}
    
    //getters setters
    
    public Map<String, Object> getMapTerminarz() {
		return mapTerminarz;
	}

	public void setMapTerminarz(Map<String, Object> mapTerminarz) {
		this.mapTerminarz = mapTerminarz;
	}

	public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
    }
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
     
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }
    public Terminarz getTerminarzEvent() {
		return terminarzEvent;
	}

	public void setTerminarzEvent(Terminarz terminarzEvent) {
		this.terminarzEvent = terminarzEvent;
	}

	public String getKolor() {
		return kolor;
	}

	public void setKolor(String kolor) {
		this.kolor = kolor;
	}

	public boolean isEdycja() {
		return edycja;
	}

	public void setEdycja(boolean edycja) {
		this.edycja = edycja;
	}

	public boolean getPokazDialog() {
		return pokazDialog;
	}

	public void setPokazDialog(boolean b) {
		this.pokazDialog = b;
	}

	public List<User> getUserWpisal() {
		return userWpisal;
	}

	public void setUserWpisal(List<User> userWpisal) {
		this.userWpisal = userWpisal;
	}

	public Logowanie getZalogowanyUser() {
		return zalogowanyUser;
	}

	public void setZalogowanyUser(Logowanie zalogowanyUser) {
		this.zalogowanyUser = zalogowanyUser;
	}

	public String getKompania() {
		return kompania;
	}

	public void setKompania(String kompania) {
		this.kompania = kompania;
	}

	public String getPluton() {
		return pluton;
	}

	public void setPluton(String pluton) {
		this.pluton = pluton;
	}

	public String getSzkolenie() {
		return szkolenie;
	}

	public void setSzkolenie(String szkolenie) {
		this.szkolenie = szkolenie;
	}

	public List<Kompania> getKompanieList() {
		return kompanieList;
	}

	public void setKompanieList(List<Kompania> kompanieList) {
		this.kompanieList = kompanieList;
	}

	public List<Pluton> getPlutonyList() {
		return plutonyList;
	}

	public void setPlutonyList(List<Pluton> plutonyList) {
		this.plutonyList = plutonyList;
	}

	public List<String> getPlutonListSzkolenie() {
		return plutonListSzkolenie;
	}

	public void setPlutonListSzkolenie(List<String> plutonListSzkolenie) {
		this.plutonListSzkolenie = plutonListSzkolenie;
	}

	public Pluton getPlutonWybrany() {
		return plutonWybrany;
	}

	public void setPlutonWybrany(Pluton plutonWybrany) {
		this.plutonWybrany = plutonWybrany;
	}

	public List<String> getPlutonListNazwa() {
		return plutonListNazwa;
	}

	public void setPlutonListNazwa(List<String> plutonListNazwa) {
		this.plutonListNazwa = plutonListNazwa;
	}

	public List<String> getKompanieListNazwa() {
		return kompanieListNazwa;
	}

	public void setKompanieListNazwa(List<String> kompanieListNazwa) {
		this.kompanieListNazwa = kompanieListNazwa;
	}

	public Integer getIdPluton() {
		return idPluton;
	}

	public void setIdPluton(Integer idPluton) {
		this.idPluton = idPluton;
	}

	
}
