package serwis.logowanie;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.userManager.model.User;
import com.userManager.model.UsersStudent;

import serwis.RejestryLogi.RejestryLogi;

@Named
@SessionScoped

public class Logowanie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAllLocal crudAll;

	
	private String login;
	private String haslo;
	private String zakladUser;
	private String poziomUprawnien;
	private String status;
	private int idUsers;
	private String kompania=null;
	private String pluton=null;
	private String szkolenie=null;
	private Sluchacze sluchacz=null;
	private Date ostatnieLogowanie;
	private String sessionLogedKlient=null;
	private Date dzis=new Date();
	private boolean czyZmienicHaslo=false;
	private String messages;
	public Logowanie() {
		// TODO Auto-generated constructor stub
	}
	@PostConstruct
	public void init() {
		logout();
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
		
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("loginUsers", login);
		hms.put("hasloUsers", myHash);
		List<User> userLogin=crudAll.getAllTermsParam("loginUser", hms);
        
	
		
		Calendar cal = Calendar.getInstance();  
    	Date dataDzis=cal.getTime();
    	
    	cal.setTime(dataDzis);
    	Timestamp dataFormGlownyTimeStamp=new Timestamp(cal.getTimeInMillis());
	
		if(userLogin.size()==1){//to jest kadra
			User userZalogowany=userLogin.get(0);
				if(userZalogowany.getZablokowany()!="NIE") 
				{
					if(userZalogowany.getErrLog()<3)
					{
						setSessionLogedKlient(session.getId());
						setZakladUser(userZalogowany.getZakladUsers());
						setIdUsers(userZalogowany.getIdUsers());
						setPoziomUprawnien(userZalogowany.getPoziomUprawnien());
						setStatus(userZalogowany.getStatusUsers());
						setOstatnieLogowanie(userZalogowany.getOstatnieLogowanie());
							
						
							if(userZalogowany.getZmianaHasla().after(dzis)) {
									userZalogowany.setErrLog(0);
									userZalogowany.setOstatnieLogowanie(dataFormGlownyTimeStamp);
									crudAll.update(userZalogowany);
									RejestryLogi rl=new RejestryLogi();
									String opis="Zalogowano. Uzytkownik: "+userZalogowany.getNazwiskoUsers()+" "+userZalogowany.getImieUsers()+", ID: "+userZalogowany.getIdUsers();
									crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "logowanie","e-dziennik",sessionLogedKlient,userZalogowany.getIdUsers(),userZalogowany.getNazwiskoUsers()+" "+userZalogowany.getImieUsers(), userZalogowany.getIdUsers(),"zalogowano", userZalogowany.getStatusUsers(),opis,userZalogowany.getIdUsers()));
									rl=null;
										return"/start/terminarz.xhtml?i=0faces-redirect=true";
							}
						else
							{
							 	return"/zmianaHasla.xhtml?faces-redirect=true";
							}
					}else {
						return"/errLog.xhtml?faces-redirect=true";
					}
				}
			}
		else {//to jest słuchacz
			
			List<UsersStudent> userLoginstudent=crudAll.getAllTermsParam("loginUserStudent", hms);
					if(userLoginstudent.size()==1)
						{
						UsersStudent userZalogowanyStudent=userLoginstudent.get(0);
						if(userZalogowanyStudent.getZablokowany()!="NIE" && userZalogowanyStudent.getErrLog()<3) {
						setSessionLogedKlient(login+session.getId());
						setZakladUser(userZalogowanyStudent.getZakladUsers());
						setIdUsers(userZalogowanyStudent.getIdUsers());
						setPoziomUprawnien(userZalogowanyStudent.getPoziomUprawnien());
						setStatus(userZalogowanyStudent.getStatusUsers());
						setKompania(userZalogowanyStudent.getKompania());
						setPluton(userZalogowanyStudent.getPluton());
						setSzkolenie(userZalogowanyStudent.getSzkolenie());
						//setSluchacz(userZalogowanyStudent.getSluchacze());
						setOstatnieLogowanie(userZalogowanyStudent.getOstatnieLogowanie());
						
						if(userZalogowanyStudent.getZmianaHasla().after(dzis)) {
							userZalogowanyStudent.setOstatnieLogowanie(dataFormGlownyTimeStamp);
							crudAll.update(userZalogowanyStudent);
							
								setSessionLogedKlient(login+session.getId());
								RejestryLogi rl=new RejestryLogi();
								String opis="Zalogowano. Słuchacz: "+userZalogowanyStudent.getSluchacze().getNazwiskoSluchacz()+" "+userZalogowanyStudent.getSluchacze().getImieSluchacz()+", ID sluchacz: "+userZalogowanyStudent.getSluchacze().getIdS()+", ID uzytkownik: "+userZalogowanyStudent.getIdUsers()+", komp./plut./szkol.: "+userZalogowanyStudent.getSluchacze().getPluton().getKompania().getNazwaKompania()+"/"+userZalogowanyStudent.getSluchacze().getPluton().getNazwaPluton()+"/"+userZalogowanyStudent.getSluchacze().getPluton().getOznaczenieSzkolenia();
								crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "logowanie","e-dziennik",sessionLogedKlient,userZalogowanyStudent.getIdUsers(),userZalogowanyStudent.getSluchacze().getNazwiskoSluchacz()+" "+userZalogowanyStudent.getSluchacze().getImieSluchacz(), userZalogowanyStudent.getSluchacze().getIdS(),"zalogowano", userZalogowanyStudent.getStatusUsers(),opis,userZalogowanyStudent.getSluchacze().getIdS()));
								rl=null;
								return"/start/terminarz.xhtml?faces-redirect=true";
						}else
						{
							return"/zmianaHasla.xhtml?faces-redirect=true";
						}
						}
				} else {
					
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
							}else {
								HashMap<String, Object> hmserrsl=new HashMap<String,Object>();
								hmserrsl.put("loginUsers", login);
								List<UsersStudent> userSluchaczError=crudAll.getAllTermsParam("findUserStudentLogin", hmserr);
								if(userSluchaczError.size()==1) {
									UsersStudent usCheckst=userSluchaczError.get(0);
									if(usCheckst.getErrLog()>3) {
										return"/errLog.xhtml?faces-redirect=true";
									}else {
									int ileError=usCheckst.getErrLog()+1;
									usCheckst.setErrLog(ileError);
									usCheckst.setWhenErrLog(dataFormGlownyTimeStamp);
									crudAll.update(usCheckst);
									}
								}
							
						}
					}
				}
	
		
	return logout();
	}
	
	public String logout() {
		
	 logoutStart();
	return"/index.xhtml?faces-redirect=true";
		
	}

	public void logoutStart() {
		
	if(sessionLogedKlient !=null) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idUsers);
		Calendar cal = Calendar.getInstance();  
    	Date dataDzis=cal.getTime();
    	cal.setTime(dataDzis);
    	Timestamp dataFormGlownyTimeStamp=new Timestamp(cal.getTimeInMillis());
    	
		if(!status.equals("sluchacz")) {
		List<User> userLogin=crudAll.getAllTermsParam("findUserPoId", hms);
		RejestryLogi rl=new RejestryLogi();
		User userZalogowany=userLogin.get(0);
		String opis="Wylogowoano. Uzytkownik: "+userZalogowany.getNazwiskoUsers()+" "+userZalogowany.getImieUsers()+", ID: "+userZalogowany.getIdUsers();
		crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "wylogowanie","e-dziennik",sessionLogedKlient,userZalogowany.getIdUsers(),userZalogowany.getNazwiskoUsers()+" "+userZalogowany.getImieUsers(), userZalogowany.getIdUsers(),"wylogowano", userZalogowany.getStatusUsers(),opis,userZalogowany.getIdUsers()));
		rl=null;
		} else {
			List<UsersStudent> userLoginstudent=crudAll.getAllTermsParam("findUserPoIdus", hms);
			if(userLoginstudent.size()==1)
			{
			UsersStudent userZalogowanyStudent=userLoginstudent.get(0);
			RejestryLogi rl=new RejestryLogi();
			String opis="Wylogowano. Słuchacz: "+userZalogowanyStudent.getSluchacze().getNazwiskoSluchacz()+" "+userZalogowanyStudent.getSluchacze().getImieSluchacz()+", ID sluchacz: "+userZalogowanyStudent.getSluchacze().getIdS()+", ID uzytkownik: "+userZalogowanyStudent.getIdUsers()+", komp./plut./szkol.: "+userZalogowanyStudent.getSluchacze().getPluton().getKompania().getNazwaKompania()+"/"+userZalogowanyStudent.getSluchacze().getPluton().getNazwaPluton()+"/"+userZalogowanyStudent.getSluchacze().getPluton().getOznaczenieSzkolenia();
			crudAll.create(rl.zapiszLogiOpis(dataFormGlownyTimeStamp, "wylogowanie","e-dziennik",sessionLogedKlient,userZalogowanyStudent.getIdUsers(),userZalogowanyStudent.getSluchacze().getNazwiskoSluchacz()+" "+userZalogowanyStudent.getSluchacze().getImieSluchacz(), userZalogowanyStudent.getSluchacze().getIdS(),"wylogowano", userZalogowanyStudent.getStatusUsers(),opis,userZalogowanyStudent.getSluchacze().getIdS()));
			rl=null;
			}
		}
	}
		setLogin(null);
		setHaslo(null);
		setSessionLogedKlient(null);
		setZakladUser(null);
		setIdUsers(0);
		setPoziomUprawnien(null);
		setStatus(null);
		setKompania(null);
		setPluton(null);
		setSzkolenie(null);
		setSluchacz(null);
		
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

	public String getSessionLogedKlient() {
		return sessionLogedKlient;
	}

	public void setSessionLogedKlient(String sessionLogedKlient) {
		this.sessionLogedKlient = sessionLogedKlient;
	}

	public int getIdUsers() {
		return idUsers;
	}

	public void setIdUsers(int idUsers) {
		this.idUsers = idUsers;
	}

	public boolean isCzyZmienicHaslo() {
		return czyZmienicHaslo;
	}

	public void setCzyZmienicHaslo(boolean czyZmienicHaslo) {
		this.czyZmienicHaslo = czyZmienicHaslo;
	}

	public String getZakladUser() {
		return zakladUser;
	}

	public void setZakladUser(String zakladUser) {
		this.zakladUser = zakladUser;
	}

	public String getPoziomUprawnien() {
		return poziomUprawnien;
	}

	public void setPoziomUprawnien(String poziomUprawnien) {
		this.poziomUprawnien = poziomUprawnien;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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


	public Sluchacze getSluchacz() {
		return sluchacz;
	}

	public void setSluchacz(Sluchacze sluchacz) {
		this.sluchacz = sluchacz;
	}
	public Date getOstatnieLogowanie() {
		return ostatnieLogowanie;
	}
	public void setOstatnieLogowanie(Date ostatnieLogowanie) {
		this.ostatnieLogowanie = ostatnieLogowanie;
	}

	public String getMessages() {
		return messages;
	}
	public void setMessages(String messages) {
		this.messages = messages;
	}

}
