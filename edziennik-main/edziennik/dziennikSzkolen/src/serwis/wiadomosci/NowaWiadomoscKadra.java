package serwis.wiadomosci;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.wiadomosci.model.Wiadomosci;
import com.userManager.model.User;
@Named
@ViewScoped
public class NowaWiadomoscKadra implements Serializable{
	
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
	private List<User> selectedUsers;
	private Wiadomosci wiadomosc;
	private List<Wiadomosci> wiadomosci;
	private Timestamp dataFormGlownyTimeStamp;

	@PostConstruct 
	    public void init() {
				setIdUsers(zalogowanyUser.getIdUsers());
			  	
			
				HashMap<String, Object> hmsu=new HashMap<String,Object>();
				hmsu.put("idUsers", idUsers);
			   wpisujacyUser=(User) crudAll.getAllTermsParam("findUserPoId", hmsu).get(0);
			      
			  	Calendar cal = Calendar.getInstance();  
		    	dataDzis=cal.getTime();
		    	setDataFormGlownyTimeStamp(new Timestamp(cal.getTimeInMillis()));
		    	
			  	wiadomosc = new Wiadomosci();
			    wiadomosci = new ArrayList<Wiadomosci>();
						    		    		
	 }
	
	public List<User> completeAreaUs(String query){

	    String queryLowerCase = query.toLowerCase();
        List<User> allusers = crudAll.getAllTerms("findAllUserNotBlock");
        return allusers.stream().filter(u -> (u.getNazwiskoUsers()+" "+u.getImieUsers()).toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
	}
	
	   public void createNew() {
	
				  wiadomosc.setUser(wpisujacyUser);
				  wiadomosc.setUserOdbiorca(selectedUsers);
				  wiadomosc.setKtoWpisal(idUsers);
			   	  wiadomosc.setDataWpisu(dataFormGlownyTimeStamp);
			   		crudAll.create(wiadomosc);
			   		wiadomosc=null;
			   	
	            reinit();
	            addMessage("W y s ł a n o."," wiadomość.",FacesMessage.SEVERITY_INFO);
	            selectedUsers=null;
	    }
	     
	    public String reinit() {
	        wiadomosc = new Wiadomosci();
	        selectedUsers=null;

	        //addMessage("Rezygnacja.","Zamknięto wiadomość.",FacesMessage.SEVERITY_INFO);
	        return null;
	    }

	public void addMessage(String summary,String detail, Severity ms) {
		FacesMessage message=new FacesMessage(ms,summary,detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	
	
//-------------------------------------------------------------GETTERY SETTERY-----------------------------------------------------------------


	public void frekwencjaStart() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("frekwencja.xhtml?i=2&subPage=frekwencja");
	}
 
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


	public List<User> getSelectedUsers() {
		return selectedUsers;
	}


	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public Timestamp getDataFormGlownyTimeStamp() {
		return dataFormGlownyTimeStamp;
	}

	public void setDataFormGlownyTimeStamp(Timestamp dataFormGlownyTimeStamp) {
		this.dataFormGlownyTimeStamp = dataFormGlownyTimeStamp;
	}
}

