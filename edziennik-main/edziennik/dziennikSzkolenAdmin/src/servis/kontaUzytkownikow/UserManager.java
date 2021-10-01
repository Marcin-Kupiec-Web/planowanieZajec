package servis.kontaUzytkownikow;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.Zaklad;
import com.userManager.model.User;

import my.util.PasswordValidator;
import serwis.RejestryLogi.RejestryLogi;

@Named
@ViewScoped
public class UserManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAll crudUniv;
	@EJB
	private CrudAllLocal logi;
	
	private List<User> ul;
	private String passRepeat=null;
	private String filtFindUser;
	private String haslo=null;
	private static PasswordValidator passwordValidator;
	
	private User selectedUser;
	
	private String[] selectedZaklad;
	private List<String> selectedZakladListString;
	private List<Zaklad> selectedZakladList;
	
	private String[] selectedSpecjal;
	private List<String> selectedSpecjalListString;
	private List<Specjalizacja> selectedSpecjalList;
	
	private Date zmianaHasla;
	private String myhasHaslo=null;
	private boolean edycjaArch;
	private String archiwum;
	private Map<String,Serializable> filterValues=new HashMap<>();
	
	public String getArchiwum() {
		return archiwum;
	}

	public void setArchiwum(String archiwum) {
		this.archiwum = archiwum;
	}

	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);


@PostConstruct
	public void init() {
	
	zmianaHasla=new Date();
	selectedUser=new User();
	Calendar c=Calendar.getInstance();
	c.setTime(zmianaHasla);
	c.add(Calendar.DATE, 30);
	zmianaHasla=c.getTime();
	
	selectedZakladList=new ArrayList<Zaklad>();
	selectedZakladList=crudUniv.getAllTerms("Zaklad.findAll");
	selectedZakladListString=new ArrayList<String>();
	if(archiwum==null)
		archiwum="NIE";
	for(Zaklad zk:selectedZakladList) {
			selectedZakladListString.add(zk.getNazwaSkrot());
	}
	
	selectedSpecjalList=new ArrayList<Specjalizacja>();
	selectedSpecjalList=crudUniv.getAllTerms("Specjalizacja.findAll");
	selectedSpecjalListString=new ArrayList<String>();
	
	for(Specjalizacja sp:selectedSpecjalList) {
			selectedSpecjalListString.add(sp.getNazwa());
	}
	
	passwordValidator=new PasswordValidator();
	
		this.ul=crudUniv.getAllTerms("findAllUser");	

			
}

	public String toString(String[] strOut) {
			String outs="";
			if(strOut!=null)
			outs=Arrays.stream(strOut).collect(Collectors.joining(", "));
			return outs;
			
		}

	public void changeArche() {
			this.ul=null;
			if(edycjaArch) {
				setArchiwum("%");
				edycjaArch=true;
			}
			else {
				setArchiwum("NIE");
				edycjaArch=false;
			}
			HashMap<String,Object>hmparamet=new HashMap<String, Object>();
			hmparamet.put("archiwum", archiwum);
			this.ul=crudUniv.getAllTermsParam("findAllUserStudent",hmparamet);
}
//-----------------------------------------------------------------------------------------------------------------------------------------------------


	public void addSaveUser() {
		
			User user=selectedUser;			
			List<Zaklad> zakZapisList=new ArrayList<Zaklad>();
			List<Specjalizacja> zakSpecjalList=new ArrayList<Specjalizacja>();
			String zaklad="";
			String specjalizacja="";			
		
			for(String zstr:selectedZaklad) {
						Zaklad zf=selectedZakladList.stream().filter(szf->szf.getNazwaSkrot().equals(zstr)).findFirst().orElse(null);
						if(zf!=null) {
									zakZapisList.add(zf);
									zaklad+=zf.getNazwaPelna()+"("+zf.getNazwaSkrot()+") ";
						}
			}
			
			for(String zstrs:selectedSpecjal) {
				Specjalizacja zf=selectedSpecjalList.stream().filter(szf->(szf.getNazwa()).equals(zstrs)).findFirst().orElse(null);
				if(zf!=null) {
							zakSpecjalList.add(zf);
							specjalizacja+=zf.getNazwa();
					}
			}
		
	//jezeli haslo wpisane (zhaszowane) - myhasHaslo jest inne od istniejacego - ((User) user).getHasloUsers()
			String opis;
			RejestryLogi rl=new RejestryLogi();
			Calendar cal = Calendar.getInstance();  
			Timestamp dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
				
				int idu=(int) session.getAttribute("idUser");
			if(!passRepeat.trim().equals("") && !haslo.trim().equals(""))
			{  
					setMyhasHaslo(haslo);
						
					if(haslo.equals(passRepeat)) {
						
						Boolean res=passwordValidator.validate(haslo);
							if(res) {							
									user.setHasloUsers(myhasHaslo);
									user.setZaklads(zakZapisList);
									user.setSpecjalizacjas(zakSpecjalList);
									user.setUsuniety(false);
										
							if(user.getIdUsers()!=0) {
										opis="Aktualizacja danych uzytkownika. Zmiana hasła i danych. Id: "+((User) user).getIdUsers()+" "+((User) user).getImieUsers()+" "+((User) user).getNazwiskoUsers()+", login: "+((User) user).getLoginUsers()+", zakład: "+zaklad+", specjalizacja: "+specjalizacja;
										logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja danych.",session.getId(), opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,((User) user).getIdUsers(), "Uzytkownik"));
																	
										crudUniv.update(user);
										}else 
										{									
											ul.add(user);
											crudUniv.create(user);		
											opis="Pierwsza rejestracja uzytkownika. Id: "+user.getIdUsers()+" "+user.getImieUsers()+" "+user.getNazwiskoUsers()+", login: "+user.getLoginUsers()+", zakład: "+zaklad+", specjalizacja: "+specjalizacja;
												logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Pierwsza rejestracja.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,user.getIdUsers(), "kadra"));
																					
													
										}
							addMessage("Poprawny zapis.","Zapisano dane.",FacesMessage.SEVERITY_INFO);
						}else
							addMessage("Bład zapisu!","Nie zapisano. Hasło musi zawierać jedna wielka literę, jedna cyfrę, znak specjalny i nie moze być krótsze niz 8 znaków.",FacesMessage.SEVERITY_ERROR);
					}
					
					else {
							addMessage("Bład zapisu!","Nie zapisano. Hasło i hasło powtórzone sa rózne.",FacesMessage.SEVERITY_ERROR);
					}
			}else if(user.getIdUsers()!=0){
				
				user.setZaklads(zakZapisList);
				user.setSpecjalizacjas(zakSpecjalList);
				
				opis="Aktualizacja danych uzytkownika. Id: "+user.getIdUsers()+" "+user.getImieUsers()+" "+user.getNazwiskoUsers()+", login: "+user.getLoginUsers()+", zakład: "+zaklad+", specjalizacja: "+specjalizacja;
				logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja danych.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,user.getIdUsers(), "Uzytkownik"));
				
				crudUniv.update(user);
				addMessage("Poprawny zapis.","Zapisano dane, hasło pozostało bez zmian.",FacesMessage.SEVERITY_INFO);
			}
			rl=null;
		}
	
	public void del(ActionEvent e) {
			User object=(User) e.getComponent().getAttributes().get("removeRow");
			if(object.getZablokowany().equals("TAK")) {
				
				RejestryLogi rl=new RejestryLogi();
				Calendar cal = Calendar.getInstance();  
				Timestamp dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
				String opis;
									
			int idu=(int) session.getAttribute("idUser");
			opis="Usunięcie uzytkownika. Id: "+object.getIdUsers()+" "+object.getImieUsers()+" "+object.getNazwiskoUsers()+", login: "+object.getLoginUsers();
			logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Usunięcie uzytkownika.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,object.getIdUsers(), "Uzytkownik"));
												
				object.setUsuniety(true);
				object.setZablokowany("TAK");
				object.setImieUsers("Usuniety");
				object.setNazwiskoUsers("Usuniety");
				object.setLoginUsers(dataDzisTimeStamp.toString());
				crudUniv.update(object);			
				rl=null;						
				ul.remove(object);	
				addMessage("Usunięto!","Operacja nieodwracalna.",FacesMessage.SEVERITY_INFO);
			}else {
				 addMessage("Nie usulnięto!","Uzytkownik musi być zablokowany.",FacesMessage.SEVERITY_FATAL);
			}
		}
		

		
	public void addMessage(String summary,String detail, Severity ms) {
			FacesMessage message=new FacesMessage(ms,summary,detail);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
	public void rowSelectedUser(User user) {
			 selectedUser=new User();
			 selectedZaklad=null;
			 selectedSpecjal=null;
			 
			if(user!=null && user.getZakladToString()!=null)
			selectedZaklad=user.getZakladToString().split(", ");
			if(user!=null && user.getSpecjalizacjaToString()!=null)
			selectedSpecjal=user.getSpecjalizacjaToString().split(", ");
			
			if(user!=null)
				selectedUser=user;
		}
		
	 public void closeDialog() {
			 selectedUser=null;
			 selectedZaklad=null;
			 selectedSpecjal=null;
			 addMessage("Bład zapisu!","Login jest juz zajęty! Zmień login.",FacesMessage.SEVERITY_ERROR);
		 }

		
//-----------------------------------------------------------gettery settery-----------------------------------------------------------------------------
	
	public String getHaslo() {
			return haslo;
		}

	public void setHaslo(String haslo) {
			this.haslo = haslo;
		}

	public String getMyhasHaslo() {
			return myhasHaslo;
		}

	public void setMyhasHaslo(String haslo) {	
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
				this.myhasHaslo=DatatypeConverter.printHexBinary(digest);
				
			}
			
		}
	
	public String zakladyToString(List<Zaklad> usl) {
			String uss = null;
			if(usl!=null && usl.size()>0) {
			for(Zaklad zak:usl) {
				uss+=zak.getNazwaSkrot()+", ";
			}
		return uss.substring(0, uss.length() -1);	
			}
			return "brak";
		}
		
	
	public String getPassRepeat() {
				return passRepeat;
			}

	public void setPassRepeat(String passRepeat) {
				this.passRepeat = passRepeat;
			}

	public String getFiltFindUser() {
				return filtFindUser;
			}

	public void setFiltFindUser(String filtFindUser) {
				this.filtFindUser = filtFindUser;
			}

	public Date getZmianaHasla() {
				return zmianaHasla;
			}

	public void setZmianaHasla(Date zmianaHasla) {
				this.zmianaHasla = zmianaHasla;
			}
			
			
	public UserManager() {
			}

	public String[] getSelectedZaklad() {
				return selectedZaklad;
			}

	public void setSelectedZaklad(String[] selectedZaklad) {
				this.selectedZaklad = selectedZaklad;
			}

	public List<String> getSelectedZakladListString() {
				return selectedZakladListString;
			}

	public void setSelectedZakladListString(List<String> selectedZakladListString) {
				this.selectedZakladListString = selectedZakladListString;
			}

	public List<Zaklad> getSelectedZakladList() {
				return selectedZakladList;
			}

	public void setSelectedZakladList(List<Zaklad> selectedZakladList) {
				this.selectedZakladList = selectedZakladList;
			}

	public String[] getSelectedSpecjal() {
				return selectedSpecjal;
			}

	public void setSelectedSpecjal(String[] selectedSpecjal) {
				this.selectedSpecjal = selectedSpecjal;
			}

	public List<Specjalizacja> getSelectedSpecjalList() {
				return selectedSpecjalList;
			}

	public void setSelectedSpecjalList(List<Specjalizacja> selectedSpecjalList) {
				this.selectedSpecjalList = selectedSpecjalList;
			}

	public List<String> getSelectedSpecjalListString() {
				return selectedSpecjalListString;
			}

	public void setSelectedSpecjalListString(List<String> selectedSpecjalListString) {
				this.selectedSpecjalListString = selectedSpecjalListString;
			}

	 public boolean isEdycjaArch() {
				return edycjaArch;
			}

	public void setEdycjaArch(boolean edycjaArch) {
				this.edycjaArch = edycjaArch;
			}

	public Map<String,Serializable> getFilterValues() {
				return filterValues;
			}

	public void setFilterValues(Map<String,Serializable> filterValues) {
				this.filterValues = filterValues;
			}

	public User getSelectedUser() {
				return selectedUser;
			}

	public void setSelectedUser(User selectedUser) {
				this.selectedUser = selectedUser;
			}
	public List<User> getUl() {
				return ul;
			}

	public void setUl(List<User> ul) {
				this.ul = ul;
			}

}
