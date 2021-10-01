package serwis.logowanie;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.edziennik.crudAll.CrudAllLocal;
import com.userManager.model.User;
import com.userManager.model.UsersStudent;

import my.util.PasswordValidator;
import serwis.RejestryLogi.RejestryLogi;
import serwis.users.nameUser.NameUser;
@Named
@RequestScoped
public class ZmianaHasla implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ZmianaHasla() {
		// TODO Auto-generated constructor stub
	}

	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private Logowanie zalogowanyUser;
	@Inject
	private NameUser nameUser;
	
	private String noweHaslo;
	private String reNowehaslo;
	private static PasswordValidator passwordValidator=new PasswordValidator();
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);

	@PostConstruct
	public void init() {
	}
	
	public void zmienHaslo(Boolean red) {
	
		if(!noweHaslo.equals(zalogowanyUser.getHaslo()))	{
		if(noweHaslo.equals(reNowehaslo)) {

			Boolean res=passwordValidator.validate(noweHaslo);
				if(res) {		
					String zmianioneHaslo=getMyhasHaslo(noweHaslo);
					
					
					Date zmianaHaslaDat=new Date();
					Calendar c=Calendar.getInstance();
					c.setTime(zmianaHaslaDat);
					Timestamp dataFormGlownyTimeStamp=new Timestamp(c.getTimeInMillis());
					c.add(Calendar.DATE, 30);
					zmianaHaslaDat=c.getTime();
					
					if(!zalogowanyUser.getStatus().equals("sluchacz")) {
						
						User us=nameUser.userObject(zalogowanyUser.getIdUsers());
						us.setHasloUsers(zmianioneHaslo);
						us.setZmianaHasla(zmianaHaslaDat);
						crudAll.update(us);
						zalogowanyUser.setSessionLogedKlient(us.getLoginUsers()+session.getId());
						RejestryLogi rl=new RejestryLogi();
						String opis="Uzytkownik zmienił swoje hasło. Uzytkownik: "+us.getNazwiskoUsers()+" "+us.getImieUsers()+", ID: "+us.getIdUsers();
						crudAll.create(rl.zapiszLogiDaneOsob(dataFormGlownyTimeStamp, "zmiana hasła",us.getIdUsers(),us.getNazwiskoUsers()+" "+us.getImieUsers(), us.getIdUsers(),"zmieniono hasło", "kadra",opis,us.getIdUsers()));
						rl=null;
					}
					else{
						UsersStudent us=nameUser.userStudentObject(zalogowanyUser.getIdUsers());
						us.setHasloUsers(zmianioneHaslo);
						us.setZmianaHasla(zmianaHaslaDat);
						crudAll.update(us);
						RejestryLogi rl=new RejestryLogi();
						String opis="Słuchacz zmienił swoje hasło. Słuchacz: "+us.getNazwiskoUsers()+" "+us.getImieUsers()+", ID: "+us.getIdUsers();
						crudAll.create(rl.zapiszLogiDaneOsob(dataFormGlownyTimeStamp, "zmiana hasła",us.getIdUsers(),us.getNazwiskoUsers()+" "+us.getImieUsers(), us.getIdUsers(),"zmieniono hasło", "sluchacz",opis,us.getIdUsers()));
						rl=null;
						zalogowanyUser.setSessionLogedKlient(us.getLoginUsers()+session.getId());
					}
					
					try {if(red)
						FacesContext.getCurrentInstance().getExternalContext().redirect("start/terminarz.xhtml?i=0&faces-redirect=true");
					else {
						addMessage("Uaktualniono Twoje hasło","Poprawny zapis",FacesMessage.SEVERITY_INFO);
					}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}else
				addMessage("Nie zapisano. Hasło musi zawierać jedna wielka literę, jedna cyfrę, znak specjalny i nie moze być krótsze niz 8 znakówa.","Nie zapisano. Hasło musi zawierać jedna wielka literę, jedna cyfrę, znak specjalny i nie moze być krótsze niz 8 znakówa.",FacesMessage.SEVERITY_ERROR);
		}
		else {
			addMessage("Hasło i powtórzone hasło sa rózne.","Hasło i powtórzone hasło sa rózne.",FacesMessage.SEVERITY_ERROR);
		}
	 }else {
		 addMessage("Stare i nowe hasło musza być rózne!","Bład zapisu.",FacesMessage.SEVERITY_ERROR);
	 }
}

	public String getMyhasHaslo(String haslo) {	
		if(haslo!=null) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		md.update(haslo.getBytes());
		byte[] digest=md.digest();
		return DatatypeConverter.printHexBinary(digest);
		
	}
		return null;
}
	public void addMessage(String summary,String detail, Severity ms) {
		FacesMessage message=new FacesMessage(ms,summary,detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	public static PasswordValidator getPasswordValidator() {
		return passwordValidator;
	}

	public static void setPasswordValidator(PasswordValidator passwordValidator) {
		ZmianaHasla.passwordValidator = passwordValidator;
	}

	
	public String getNoweHaslo() {
		return noweHaslo;
	}





	public void setNoweHaslo(String noweHaslo) {
		this.noweHaslo = noweHaslo;
	}





	public String getReNowehaslo() {
		return reNowehaslo;
	}





	public void setReNowehaslo(String reNowehaslo) {
		this.reNowehaslo = reNowehaslo;
	}





	public Logowanie getZalogowanyUser() {
		return zalogowanyUser;
	}





	public void setZalogowanyUser(Logowanie zalogowanyUser) {
		this.zalogowanyUser = zalogowanyUser;
	}

	public NameUser getNameUser() {
		return nameUser;
	}

	public void setNameUser(NameUser nameUser) {
		this.nameUser = nameUser;
	}
}
