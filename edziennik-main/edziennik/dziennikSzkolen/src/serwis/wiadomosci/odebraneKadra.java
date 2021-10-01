package serwis.wiadomosci;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.wiadomosci.model.Wiadomosci;
import com.userManager.model.User;
@Named
@ViewScoped
public class odebraneKadra implements Serializable{
	
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
	
	private User userZalogowany;
	private List<Wiadomosci> wiadomosciOdebraneUser;
	private Integer idUser;
	private Date dataDzis=new Date();
	private boolean render=false;

	
	@PostConstruct 
	    public void init() {
			  	setIdUser(zalogowanyUser.getIdUsers());
			  	LocalDateTime currentDate = LocalDateTime.now();
			  	LocalDateTime agoSixMonth=currentDate.minusMonths(6);
						HashMap<String, Object> hmsu=new HashMap<String,Object>();
						hmsu.put("idUsers", idUser);
						hmsu.put("dateFirst", Timestamp.valueOf(agoSixMonth));
						if (crudAll.getAllTermsParam("findUserMail.findAll", hmsu).size()>0);
						setWiadomosciOdebraneUser(crudAll.getAllTermsParam("findUserMail.findAll", hmsu));
			
			  	Calendar cal = Calendar.getInstance();  
		    	dataDzis=cal.getTime();
						
	 }
	

	public void addMessage(String summary,String detail, Severity ms) {
		FacesMessage message=new FacesMessage(ms,summary,detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public int sortByDate(Object dat1, Object dat2) {
	     return ((Date) dat2).compareTo((Date) dat1);
	}
//-------------------------------------------------------------GETTERY SETTERY-----------------------------------------------------------------
	

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

	public User getUserZalogowany() {
		return userZalogowany;
	}


	public void setUserZalogowany(User userZalogowany) {
		this.userZalogowany = userZalogowany;
	}


	public Integer getIdUser() {
		return idUser;
	}


	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}


	public List<Wiadomosci> getWiadomosciOdebraneUser() {
		return wiadomosciOdebraneUser;
	}


	public void setWiadomosciOdebraneUser(List<Wiadomosci> wiadomosciOdebraneUser) {
		this.wiadomosciOdebraneUser = wiadomosciOdebraneUser;
	}

}

