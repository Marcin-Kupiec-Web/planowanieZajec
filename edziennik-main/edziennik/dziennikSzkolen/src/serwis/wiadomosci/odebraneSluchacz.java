package serwis.wiadomosci;

import java.io.Serializable;
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

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.wiadomosci.model.Wiadomosci;
import com.userManager.model.UsersStudent;
@Named
@ViewScoped
public class odebraneSluchacz implements Serializable{
	
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
	
	private UsersStudent sluchaczZalogowany;
	private Integer idSluchacz;
	private Date dataDzis=new Date();
	private boolean render=false;
	private Wiadomosci wiadomosc;
	private List<Wiadomosci> wiadomosci;

	
	@PostConstruct 
	    public void init() {
				setIdSluchacz(zalogowanyUser.getIdUsers());
				
				HashMap<String, Object> hmsu=new HashMap<String,Object>();
				hmsu.put("idUsers", idSluchacz);
				
				sluchaczZalogowany=(UsersStudent) crudAll.getAllTermsParam("findUserPoIdus", hmsu).get(0);
			
			  	Calendar cal = Calendar.getInstance();  
		    	dataDzis=cal.getTime();
		    	
			    wiadomosci = new ArrayList<Wiadomosci>();
						
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


	public UsersStudent getSluchaczZalogowany() {
		return sluchaczZalogowany;
	}


	public void setSluchaczZalogowany(UsersStudent sluchaczZalogowany) {
		this.sluchaczZalogowany = sluchaczZalogowany;
	}


	public Integer getIdSluchacz() {
		return idSluchacz;
	}


	public void setIdSluchacz(Integer idSluchacz) {
		this.idSluchacz = idSluchacz;
	}

}

