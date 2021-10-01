package servis.kontaUzytkownikow;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.edziennik.sluchacze.zajecia.model.Kompania;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.Zaklad;
import com.userManager.model.UsersStudent;

import my.util.PasswordValidator;
import serwis.RejestryLogi.RejestryLogi;
@Named
@ViewScoped
public class SluchaczManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAll crudUniv;
	@EJB
	private CrudAllLocal logi;
	
	private List<UsersStudent> ul;
	private List<UsersStudent> filteredUl;
	private boolean editAble;
	private String passRepeat;
	private String filtFindUser;
	private String id;
	private String nazwisko;
	private String poziomUprawnien;
	private String imie;
	private String login;
	private String haslo;
	private static PasswordValidator passwordValidator;
	private String zaklad;
	private String zablokowany;
	private String status;
	private String kompania;
	private String pluton;
	private String szkolenie;
	private UsersStudent selectedUser;

	
	private String[] selectedSpecjal;
	private List<String> selectedSpecjalListString;
	private List<Specjalizacja> selectedSpecjalList;
	
	private String imieNazwiskoSluchacza;
	private List<String> sluchaczeWplutonie;
	private List<Kompania> kompanieList;
	private List<String> kompanieListNazwa;
	private List<Pluton> plutonyList;
	private List<String> plutonListNazwa;
	private List<String> plutonListSzkolenie=new ArrayList<String>();
	private List<Sluchacze> sluchaczeSorted;

	private List<Zaklad> zakZapisList;

	private List<Specjalizacja> zakSpecjalList;

	private String zaklad2;

	private String specjalizacja;
	private Date zmianaHasla;
	private String myhasHaslo=null;
	private boolean edycjaArch;
	private String archiwum;
	private Map<String,Serializable> filterValues=new HashMap<>();
	
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);


@PostConstruct
	public void init() {
	sluchaczeWplutonie=new ArrayList<String>();
    kompanieList=new ArrayList<Kompania>();
	kompanieListNazwa=new ArrayList<String>();
	plutonyList=new ArrayList<Pluton>();
	plutonListNazwa=new ArrayList<String>();
	zmianaHasla=new Date();
	Calendar c=Calendar.getInstance();
	c.setTime(zmianaHasla);
	c.add(Calendar.DATE, 30);
	zmianaHasla=c.getTime();	

	if(archiwum==null)
		archiwum="NIE";
	
	passwordValidator=new PasswordValidator();

		HashMap<String,Object>hmparamet=new HashMap<String, Object>();
		hmparamet.put("archiwum", archiwum);
		
		this.ul=crudUniv.getAllTermsParam("findAllUserStudent",hmparamet);
		
		HashMap<String,Object>hmp=new HashMap<String, Object>();
		hmp.put("archiwum","NIE");
		kompanieList=crudUniv.getAllTermsParam("Kompania.findAll",hmp);;
		
		for(Kompania kompStr:kompanieList) {
			kompanieListNazwa.add(kompStr.getNazwaKompania());
		}
			
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


		public void onRowEdit() {
			
			UsersStudent user=selectedUser;
			
			setZakZapisList(new ArrayList<Zaklad>());
			setZakSpecjalList(new ArrayList<Specjalizacja>());
			setZaklad2("");
			setSpecjalizacja("");
			

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
							opis="Aktualizacja konta sluchacza. Zmiana hasła i danych. Id: "+user.getSluchacze().getIdSluchacz()+" "+user.getImieUsers()+" "+user.getNazwiskoUsers()+", login: "+user.getLoginUsers();
							logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja konta słuchacza.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,user.getSluchacze().getIdSluchacz(), "Słuchacz"));
							
							rl=null;
						
						crudUniv.update(user);
						addMessage("Poprawny zapis.","Zapisano dane i hasło.",FacesMessage.SEVERITY_INFO);
				}else
					addMessage("Bład zapisu!","Nie zapisano. Hasło musi zawierać jedna wielka literę, jedna cyfrę, znak specjalny i nie moze być krótsze niz 8 znaków.",FacesMessage.SEVERITY_ERROR);
			}
			
			else {
					addMessage("Bład zapisu!","Nie zapisano. Hasło i hasło powtórzone sa rózne.",FacesMessage.SEVERITY_ERROR);
			}
			}else {
				
					opis="Aktualizacja konta sluchacza. Id: "+user.getSluchacze().getIdSluchacz()+" "+user.getImieUsers()+" "+user.getNazwiskoUsers()+", login: "+user.getLoginUsers();
					logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja konta słuchacza.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,user.getSluchacze().getIdSluchacz(), "Słuchacz"));
					
					rl=null;
				
				crudUniv.update(user);
				addMessage("Poprawny zapis.","Zapisano dane, hasło pozostało bez zmian.",FacesMessage.SEVERITY_INFO);
			}
			
		}
		
		
		public void addUser() {
			
			UsersStudent userAdd;			
			userAdd=new UsersStudent();
				
			if(!imie.trim().equals("") && imie!=null && !nazwisko.trim().equals("") && nazwisko!=null && haslo!=null && !haslo.trim().equals("") && !passRepeat.trim().equals("") && passRepeat!=null 
					&& zmianaHasla!=null) {
				if(haslo.equals(passRepeat)) {
					Boolean res=passwordValidator.validate(haslo);
				if(res) {
									
						HashMap<String, Object> hms=new HashMap<String,Object>();
						hms.put("loginUsers", login);
						if(crudUniv.getAllTermsParam("loginUserIfRepeatStudent", hms).size()==0)
								{
								if(kompania!=null && !kompania.trim().equals("") && !pluton.trim().equals("") && pluton!=null && !szkolenie.trim().equals("") && szkolenie!=null)
								{
										userAdd.setImieUsers(imie);
										userAdd.setNazwiskoUsers(nazwisko);
										setMyhasHaslo(haslo);
										userAdd.setHasloUsers(myhasHaslo);
										userAdd.setLoginUsers(login);
										userAdd.setZablokowany(zablokowany);
										userAdd.setSluchacze(wybranySluchacz);
										userAdd.setStatusUsers(status);
										userAdd.setPoziomUprawnien(poziomUprawnien);
										userAdd.setZmianaHasla(zmianaHasla);
										userAdd.setKompania(kompania);
										userAdd.setPluton(pluton);
										userAdd.setSzkolenie(szkolenie);
										userAdd.setUsuniety(false);
										ul.add(userAdd);
										crudUniv.create(userAdd);
										sluchaczeWplutonie.remove(wybranySluchacz.getNazwiskoSluchacz()+" "+wybranySluchacz.getImieSluchacz());
										login=null;
										RejestryLogi rl=new RejestryLogi();
										Calendar cal = Calendar.getInstance();  
										Timestamp dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
											String opis;
											int idu=(int) session.getAttribute("idUser");
											opis="Przypisanie uprawnień słuchaczowi. Id: "+wybranySluchacz.getIdS()+", "+wybranySluchacz.getImieSluchacz()+" "+wybranySluchacz.getNazwiskoSluchacz()+", komp./plut./szkol.: "+wybranySluchacz.getPluton().getKompania().getNazwaKompania()+"/"+wybranySluchacz.getPluton().getNazwaPluton()+"/"+wybranySluchacz.getPluton().getOznaczenieSzkolenia();
											logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Nadanie uprawnień słuchaczowi.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,wybranySluchacz.getIdS(), "sluchacz"));
											
											rl=null;
										
										addMessage("Poprawny zapis.","Zapisano dane.",FacesMessage.SEVERITY_INFO);
								}
								else
									addMessage("Bład zapisu!","Nie zapisano. Kompania, pluton, szkolenie - nie moga być puste!",FacesMessage.SEVERITY_ERROR);
								}
							else
								addMessage("Bład zapisu!","Login jest juz zajęty! Zmień login.",FacesMessage.SEVERITY_ERROR);
					
					}
					else
					addMessage("Bład zapisu!","Nie zapisano. Hasło musi zawierać jedna wielka literę, jedna cyfrę, znak specjalny i nie moze być krótsze niz 8 znaków.",FacesMessage.SEVERITY_ERROR);
				}
				else
					addMessage("Bład!","Nie zapisano. Hasło i hasło powtórzone sa rózne.",FacesMessage.SEVERITY_ERROR);
			}
			else
				addMessage("Bład.","Pola: imię, nazwisko, login, hasło, hasło powtórzone, data nie moga być puste.",FacesMessage.SEVERITY_ERROR);
			
		}
		
		public void del(ActionEvent e) {
		
			UsersStudent object=(UsersStudent) e.getComponent().getAttributes().get("removeRow");
			crudUniv.delete(object);
			RejestryLogi rl=new RejestryLogi();
			Calendar cal = Calendar.getInstance();  
			Timestamp dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
				String opis;
			
				HashMap<String,String>hmparamet=new HashMap<String, String>();
				hmparamet.put("archiwum", archiwum);
				
				int idu=(int) session.getAttribute("idUser");
				opis="Usunięcie konta słuchacza. Id: "+object.getSluchacze().getIdSluchacz()+" "+object.getImieUsers()+" "+object.getNazwiskoUsers()+", login: "+object.getLoginUsers();
				logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Usunięcie konta słuchacza.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,object.getSluchacze().getIdSluchacz(), "Słuchacz"));
				
				rl=null;
			
			ul.remove(object);	
		}
		
//-----------------------------------------------------------------------------do słuchaczy--------------------------------------------------------------------------------		
		private Sluchacze wybranySluchacz;
		
		public void changeKompania() {
			plutonListNazwa.clear();
			plutonListSzkolenie.clear();
			pluton=null;
			szkolenie=null;
			sluchaczeSorted=null;
			sluchaczeWplutonie=new ArrayList<String>();
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
			sluchaczeSorted=null;
			sluchaczeWplutonie=new ArrayList<String>();
			for(Pluton plutStr:plutonyList) {
				if(plutStr.getNazwaPluton().equals(pluton)) {
					if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
						plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
					}
				
				}
			}
			
		}

		public void changeSzkolenie() {
			sluchaczeWplutonie=new ArrayList<String>();
			for(Pluton plutStr:plutonyList) {
				if(plutStr.getNazwaPluton().equals(pluton) && plutStr.getOznaczenieSzkolenia().equals(szkolenie)) {
					if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
						plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
						
					}
						//zeby nie było duplikatow
					sluchaczeSorted=plutStr.getSluchaczes().stream().distinct().collect(Collectors.toList());
					
					List<UsersStudent>uls=(List<UsersStudent>) ul;
					for(Sluchacze sl:sluchaczeSorted) {
						if(uls.stream().filter(ulsf->ulsf.getSluchacze().getIdS()==sl.getIdS()).findFirst().orElse(null)==null) {
							sluchaczeWplutonie.add(sl.getNazwiskoSluchacz()+" "+sl.getImieSluchacz());
						}
						
					}
				}
			}

		}

		public void clearForm() {
			plutonListNazwa.clear();
			plutonListSzkolenie.clear();
			kompania=null;
			pluton=null;
			szkolenie=null;
			sluchaczeSorted=null;
			sluchaczeWplutonie=new ArrayList<String>();
		}
		
		public void checkSluchacz() {
			String imNazw;
			
			for(Sluchacze sl:sluchaczeSorted) {
				imNazw=sl.getNazwiskoSluchacz()+" "+sl.getImieSluchacz();
				if(imNazw.equals(imieNazwiskoSluchacza)) {
					login=String.valueOf(sl.getIdSluchacz());
					imie=sl.getImieSluchacz();
					nazwisko=sl.getNazwiskoSluchacz();
					wybranySluchacz=sl;
				}
			}
		}
		
		public void addMessage(String summary,String detail, Severity ms) {
			FacesMessage message=new FacesMessage(ms,summary,detail);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		
		 public void save(UsersStudent us) {
			addMessage("Sukces: "+selectedUser.getIdUsers(),"Uaktualniono dane. Osoba:  "+selectedUser.getNazwiskoUsers(),FacesMessage.SEVERITY_INFO);
		 	}

//-----------------------------------------------------------gettery settery-----------------------------------------------------------------------------
		 public List<String> getPlutonListNazwa() {
				return plutonListNazwa;
			}

			public void setPlutonListNazwa(List<String> plutonListNazwa) {
				this.plutonListNazwa = plutonListNazwa;
			}

			public List<String> getPlutonListSzkolenie() {
				return plutonListSzkolenie;
			}

			public void setPlutonListSzkolenie(List<String> plutonListSzkolenie) {
				this.plutonListSzkolenie = plutonListSzkolenie;
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
		public String getPoziomUprawnien() {
			return poziomUprawnien;
		}

		public void setPoziomUprawnien(String poziomUprawnien) {
			this.poziomUprawnien = poziomUprawnien;
		}

		public List<Kompania> getKompanieList() {
			return kompanieList;
		}

		public void setKompanieList(List<Kompania> kompanieList) {
			this.kompanieList = kompanieList;
		}

		public List<String> getKompanieListNazwa() {
			return kompanieListNazwa;
		}

		public void setKompanieListNazwa(List<String> kompanieListNazwa) {
			this.kompanieListNazwa = kompanieListNazwa;
		}
		public void setNazwisko(String nazwisko) {
			this.nazwisko = nazwisko;
		}

		public String getImie() {
			return imie;
		}

		public void setImie(String imie) {
			this.imie = imie;
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

		public String getZaklad() {
			return zaklad;
		}

		public void setZaklad(String zaklad) {
			this.zaklad = zaklad;
		}

		public String getZablokowany() {
			return zablokowany;
		}

		public void setZablokowany(String zablokowany) {
			this.zablokowany = zablokowany;
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
		
		
		public void rowSelectedUser(UsersStudent user) {
			selectedUser=user;
		}
		
		 public void closeDialog() {
			 selectedUser=null;
			 addMessage("Ok."," Dziękuję.",FacesMessage.SEVERITY_ERROR);
		 }
		 
		public boolean isEditAble() {
			return editAble;
			}

			public void setEditAble(boolean editAble) {
			this.editAble = editAble;
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

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public Date getZmianaHasla() {
				return zmianaHasla;
			}

			public void setZmianaHasla(Date zmianaHasla) {
				this.zmianaHasla = zmianaHasla;
			}
					
			public List<UsersStudent> getFilteredUl() {
				return filteredUl;
			}
			
			public void setFilteredUl(List<UsersStudent> filteredUl) {
				this.filteredUl = filteredUl;
			}
			
			public List<UsersStudent> getUl() {
				return ul;
			}
			
			public void setUl(List<UsersStudent> ul) {
				this.ul = ul;
			}

			public List<Sluchacze> getSluchaczeSorted() {
				return sluchaczeSorted;
			}

			public void setSluchaczeSorted(List<Sluchacze> sluchaczeSorted) {
				this.sluchaczeSorted = sluchaczeSorted;
			}

			public String getImieNazwiskoSluchacza() {
				return imieNazwiskoSluchacza;
			}

			public void setImieNazwiskoSluchacza(String imieNazwiskoSluchacza) {
				this.imieNazwiskoSluchacza = imieNazwiskoSluchacza;
			}

			public List<String> getSluchaczeWplutonie() {
				return sluchaczeWplutonie;
			}

			public void setSluchaczeWplutonie(List<String> sluchaczeWplutonie) {
				this.sluchaczeWplutonie = sluchaczeWplutonie;
			}
	
			
			public String getNazwisko() {
				return nazwisko;
			}
			
			public SluchaczManager() {
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

			public Sluchacze getWybranySluchacz() {
				return wybranySluchacz;
			}

			public void setWybranySluchacz(Sluchacze wybranySluchacz) {
				this.wybranySluchacz = wybranySluchacz;
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

			public UsersStudent getSelectedUser() {
				return selectedUser;
			}

			public void setSelectedUser(UsersStudent selectedUser) {
				this.selectedUser = selectedUser;
			}

			public List<Zaklad> getZakZapisList() {
				return zakZapisList;
			}

			public void setZakZapisList(List<Zaklad> zakZapisList) {
				this.zakZapisList = zakZapisList;
			}

			public List<Specjalizacja> getZakSpecjalList() {
				return zakSpecjalList;
			}

			public void setZakSpecjalList(List<Specjalizacja> zakSpecjalList) {
				this.zakSpecjalList = zakSpecjalList;
			}

			public String getZaklad2() {
				return zaklad2;
			}

			public void setZaklad2(String zaklad2) {
				this.zaklad2 = zaklad2;
			}

			public String getSpecjalizacja() {
				return specjalizacja;
			}

			public void setSpecjalizacja(String specjalizacja) {
				this.specjalizacja = specjalizacja;
			}
			public String getArchiwum() {
				return archiwum;
			}

			public void setArchiwum(String archiwum) {
				this.archiwum = archiwum;
			}
}
