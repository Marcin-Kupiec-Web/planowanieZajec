package serwis.wiadomosci;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.UnselectEvent;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.wiadomosci.model.Wiadomosci;
import com.userManager.model.User;
@Named
@ViewScoped
public class WiadomosciWyslane implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private serwis.users.nameUser.NameUser nameUser;

	@Inject
	private serwis.logowanie.Logowanie zalogowanyUser;
	
	private User wpisujacyUser;
	private Integer idUsers;
	private Date dataDzis=new Date();
	private boolean render=false;
	private Wiadomosci wiadomosc;
	private List<Wiadomosci> wiadomosci;
	private List<Wiadomosci> wiadomosciWyslaneUser;
	
	@PostConstruct 
	    public void init() {
			  	setIdUsers(zalogowanyUser.getIdUsers());
			 	LocalDateTime currentDate = LocalDateTime.now();
			  	LocalDateTime agoSixMonth=currentDate.minusMonths(6);
						HashMap<String, Object> hmsu=new HashMap<String,Object>();
						hmsu.put("idUsers", idUsers);
						hmsu.put("dateFirst", Timestamp.valueOf(agoSixMonth));
						if (crudAll.getAllTermsParam("findUserMailOut.findAll", hmsu).size()>0);
						setWiadomosciWyslaneUser(crudAll.getAllTermsParam("findUserMailOut.findAll", hmsu));
			   
			  	Calendar cal = Calendar.getInstance();  
		    	dataDzis=cal.getTime();
		    	
			    wiadomosci = new ArrayList<Wiadomosci>();
						
	 }
	

	public void addMessage(String summary,String detail, Severity ms) {
		FacesMessage message=new FacesMessage(ms,summary,detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	
	 public void onItemUnselect(UnselectEvent event) {
	        FacesContext context = FacesContext.getCurrentInstance();
	         
	        FacesMessage msg = new FacesMessage();
	        msg.setSummary("Item unselected: " + event.getObject().toString());
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	         
	        context.addMessage(null, msg);
	    }
	 
		public int sortByDate(Object dat1, Object dat2) {
		     return ((Date) dat2).compareTo((Date) dat1);
		}
//-------------------------------------------------------------GETTERY SETTERY-----------------------------------------------------------------
	
	
	public void setIdUsers(int idUsers) {
		this.idUsers = idUsers;
	}
	
	
	public Date getDataDzis() {
		return dataDzis;
	}
	
	
	public void setDataDzis(Date dataDzis) {
		this.dataDzis = dataDzis;
	}
	
	
	
	public boolean isRender() {
		return render;
	}
	
	
	public void setRender(boolean render) {
		this.render = render;
	}
	public serwis.users.nameUser.NameUser getNameUser() {
		return nameUser;
	}
	
	public void setNameUser(serwis.users.nameUser.NameUser nameUser) {
		this.nameUser = nameUser;
	}


	public Wiadomosci getWiadomosc() {
		return wiadomosc;
	}


	public void setWiadomosc(Wiadomosci wiadomosc) {
		this.wiadomosc = wiadomosc;
	}


	public List<Wiadomosci> getWiadomosci() {
		return wiadomosci;
	}


	public void setWiadomosci(List<Wiadomosci> wiadomosci) {
		this.wiadomosci = wiadomosci;
	}

	public Integer getIdUsers() {
		return idUsers;
	}

	public void setIdUsers(Integer idUsers) {
		this.idUsers = idUsers;
	}

	public User getWpisujacyUser() {
		return wpisujacyUser;
	}
	public void setWpisujacyUser(User wpisujacyUser) {
		this.wpisujacyUser = wpisujacyUser;
	}


	public List<Wiadomosci> getWiadomosciWyslaneUser() {
		return wiadomosciWyslaneUser;
	}


	public void setWiadomosciWyslaneUser(List<Wiadomosci> wiadomosciWyslaneUser) {
		this.wiadomosciWyslaneUser = wiadomosciWyslaneUser;
	}

}

