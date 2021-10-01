package serwis.logowanie;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.edziennik.crudAll.CrudAllLocal;
import com.userManager.model.User;
import com.userManager.service.userManagerBeanLocal;

import serwis.RejestryLogi.RejestryLogi;
@Named
@SessionScoped
public class Logowanie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private userManagerBeanLocal usmanabean;
	@EJB
	private CrudAllLocal crudAll;
	private String login;
	private String haslo;
	private List<User> userLogin;
	private String sessionLoged=null;
	private int idUsers;
	public Logowanie() {
		// TODO Auto-generated constructor stub
	}
	
	public String logowanieValidate() {
		FacesContext fct=FacesContext.getCurrentInstance();
		HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		md.update(haslo.getBytes());
		byte[] digest=md.digest();
		String myHash=DatatypeConverter.printHexBinary(digest);
		
		Calendar cal = Calendar.getInstance();  
    	Date dataDzis=cal.getTime();
    	cal.setTime(dataDzis);
    	Timestamp dataFormGlownyTimeStamp=new Timestamp(cal.getTimeInMillis());
    	
		userLogin=usmanabean.getUsers(login,myHash);
		
		if(userLogin.size()==1)
			{
					User userZalogowany=userLogin.get(0);
					if(userZalogowany.getErrLog()<3)
					{
							if((userZalogowany.getPoziomUprawnien().equals("1") || userZalogowany.getPoziomUprawnien().equals("4") || userZalogowany.getPoziomUprawnien().equals("5") || userZalogowany.getPoziomUprawnien().equals("6")))
								{	idUsers=userLogin.get(0).getIdUsers();
									session.setAttribute("idUser", idUsers);
									session.setAttribute("zaklad", userLogin.get(0).getZakladUsers());
									String imieNazw=userLogin.get(0).getImieUsers()+" "+userLogin.get(0).getNazwiskoUsers();
									session.setAttribute("userImieNazwisko", imieNazw);
									session.setAttribute("poziomUprawnien",userLogin.get(0).getPoziomUprawnien());
									setSessionLoged(session.getId());
							    	
									RejestryLogi rl=new RejestryLogi();
									String opis="Zalogowano. Uzytkownik: "+userZalogowany.getNazwiskoUsers()+" "+userZalogowany.getImieUsers()+", ID: "+userZalogowany.getIdUsers();
									usmanabean.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "logowanie","administracja",sessionLoged,userZalogowany.getIdUsers(),userZalogowany.getNazwiskoUsers()+" "+userZalogowany.getImieUsers(), userZalogowany.getIdUsers(),"zalogowano", userZalogowany.getStatusUsers(),opis,userZalogowany.getIdUsers()));
									rl=null;
									
									return"/start/start.xhtml?i=0faces-redirect=true";
							  }
					}else {
						return"/errLog.xhtml?faces-redirect=true";
					}	
	}else {
			HashMap<String, Object> hmserr=new HashMap<String,Object>();
			hmserr.put("loginUsers", login);
			List<User> userError=crudAll.getAllTermsParam("findUserLogin", hmserr);
		
			if(userError.size()==1) {
					User usCheck=userError.get(0);
					if(usCheck.getErrLog()>3) {
						return"/errLog.xhtml?faces-redirect=true";
					}else {
					int ileError=usCheck.getErrLog()+1;
					usCheck.setErrLog(ileError);
					usCheck.setWhenErrLog(dataFormGlownyTimeStamp);
					crudAll.update(usCheck);
					}
			}
	}
		ExternalContext ec=FacesContext.getCurrentInstance().getExternalContext();
		ec.invalidateSession();
		setSessionLoged(null);
		return"/index.xhtml?faces-redirect=true";
	}
	public String logout() {
		logoutStart();
	return"/index.xhtml?faces-redirect=true";
		
	}
	
	public void logoutStart() {
		if(sessionLoged !=null) {
			HashMap<String, Object> hms=new HashMap<String,Object>();
			hms.put("idUsers", idUsers);
			Calendar cal = Calendar.getInstance();  
	    	Date dataDzis=cal.getTime();
	    	cal.setTime(dataDzis);
	    	Timestamp dataFormGlownyTimeStamp=new Timestamp(cal.getTimeInMillis());
	    	
			List<User> userLogin=crudAll.getAllTermsParam("findUserPoId", hms);
			RejestryLogi rl=new RejestryLogi();
			User userZalogowany=userLogin.get(0);
			String opis="Wylogowoano. Uzytkownik: "+userZalogowany.getNazwiskoUsers()+" "+userZalogowany.getImieUsers()+", ID: "+userZalogowany.getIdUsers();
			crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "wylogowanie","e-dziennik",sessionLoged,userZalogowany.getIdUsers(),userZalogowany.getNazwiskoUsers()+" "+userZalogowany.getImieUsers(), userZalogowany.getIdUsers(),"wylogowano", userZalogowany.getStatusUsers(),opis,userZalogowany.getIdUsers()));
			rl=null;
		
		}
		
		setLogin(null);
		setHaslo(null);
		setSessionLoged(null);
		ExternalContext ec=FacesContext.getCurrentInstance().getExternalContext();
		ec.invalidateSession();
		ec.getSessionMap().put("logowanie", null);
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getHaslo() {
		return haslo;
	}
	public void setHaslo(String haslo) {
		this.haslo = haslo;
	}

	public List<User> getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(List<User> userLogin) {
		this.userLogin = userLogin;
	}

	public String getSessionLoged() {
		return sessionLoged;
	}

	public void setSessionLoged(String sessionLoged) {
		this.sessionLoged = sessionLoged;
	}

}
