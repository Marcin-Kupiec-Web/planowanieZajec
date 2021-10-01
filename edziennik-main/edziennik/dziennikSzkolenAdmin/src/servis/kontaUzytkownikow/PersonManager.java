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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Kompania;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.Zaklad;
import com.userManager.model.User;
import com.userManager.model.UsersStudent;

import my.util.PasswordValidator;
import serwis.RejestryLogi.RejestryLogi;
@SuppressWarnings("deprecation")
@ManagedBean(name="personManager", eager=true)
@ViewScoped
public class PersonManager<U> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAll crudUniv;
	@EJB
	private CrudAllLocal logi;
	
	private List<U> ul;
	private List<U> filteredUl;
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
	private User selectedUser;

	private String[] selectedZaklad;
	private List<String> selectedZakladListString;
	private List<Zaklad> selectedZakladList;
	
	private String[] selectedSpecjal;
	private List<String> selectedSpecjalListString;
	private List<Specjalizacja> selectedSpecjalList;
	
	private String imieNazwiskoSluchacza;
	private List<String> sluchaczeWplutonie;
	private List<Kompania> kompanieList;
	private List<String> kompanieListNazwa;
	private List<Pluton> plutonyList;
	private List<String> plutonListNazwa;

	private Date zmianaHasla;
	private String myhasHaslo=null;
	private String url="/kontaUzytkownikow/wykladowcy.xhtml";
	private String strUrl=(String) FacesContext.getCurrentInstance().getViewRoot().getViewId();
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
	if(strUrl.equals(url)) {
		this.ul=crudUniv.getAllTerms("findAllUser");	
	}
	else {
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


		public void onRowEdit(RowEditEvent event) {
			@SuppressWarnings("unchecked")
			U user=((U) event.getObject());
			
			List<Zaklad> zakZapisList=new ArrayList<Zaklad>();
			List<Specjalizacja> zakSpecjalList=new ArrayList<Specjalizacja>();
			String zaklad="";
			String specjalizacja="";
			
			if(strUrl.equals(url)) {
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
						if(user.getClass().getSimpleName().equals("User")) {
							((User) user).setHasloUsers(myhasHaslo);
							((User) user).setZaklads(zakZapisList);
							((User) user).setSpecjalizacjas(zakSpecjalList);
					
								opis="Aktualizacja danych uzytkownika. Zmiana hasła i danych. Id: "+((User) user).getIdUsers()+" "+((User) user).getImieUsers()+" "+((User) user).getNazwiskoUsers()+", login: "+((User) user).getLoginUsers()+", zakład: "+zaklad+", specjalizacja: "+specjalizacja;
								logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja danych.",session.getId(), opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,((User) user).getIdUsers(), "Uzytkownik"));
								
								rl=null;
						}
						else {
							((UsersStudent) user).setHasloUsers(myhasHaslo);
							opis="Aktualizacja konta sluchacza. Zmiana hasła i danych. Id: "+((UsersStudent) user).getSluchacze().getIdSluchacz()+" "+((UsersStudent) user).getImieUsers()+" "+((UsersStudent) user).getNazwiskoUsers()+", login: "+((UsersStudent) user).getLoginUsers();
							logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja konta słuchacza.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,((UsersStudent) user).getSluchacze().getIdSluchacz(), "Słuchacz"));
							
							rl=null;
						}
						
						crudUniv.update(user);
						addMessage("Poprawny zapis.","Zapisano dane i hasło.",FacesMessage.SEVERITY_INFO);
				}else
					addMessage("Bład zapisu!","Nie zapisano. Hasło musi zawierać jedna wielka literę, jedna cyfrę, znak specjalny i nie moze być krótsze niz 8 znaków.",FacesMessage.SEVERITY_ERROR);
			}
			
			else {
					addMessage("Bład zapisu!","Nie zapisano. Hasło i hasło powtórzone sa rózne.",FacesMessage.SEVERITY_ERROR);
			}
			}else {
				
				if(strUrl.equals(url)) {
				((User) user).setZaklads(zakZapisList);
				((User) user).setSpecjalizacjas(zakSpecjalList);
				
				opis="Aktualizacja danych uzytkownika. Id: "+((User) user).getIdUsers()+" "+((User) user).getImieUsers()+" "+((User) user).getNazwiskoUsers()+", login: "+((User) user).getLoginUsers()+", zakład: "+zaklad+", specjalizacja: "+specjalizacja;
				logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja danych.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,((User) user).getIdUsers(), "Uzytkownik"));
				
				rl=null;
				}else {
					((UsersStudent) user).setHasloUsers(myhasHaslo);
					opis="Aktualizacja konta sluchacza. Id: "+((UsersStudent) user).getSluchacze().getIdSluchacz()+" "+((UsersStudent) user).getImieUsers()+" "+((UsersStudent) user).getNazwiskoUsers()+", login: "+((UsersStudent) user).getLoginUsers();
					logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Aktualizacja konta słuchacza.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,((UsersStudent) user).getSluchacze().getIdSluchacz(), "Słuchacz"));
					
					rl=null;
				}
				crudUniv.update(user);
				addMessage("Poprawny zapis.","Zapisano dane, hasło pozostało bez zmian.",FacesMessage.SEVERITY_INFO);
			}
			
		}
		
		@SuppressWarnings("unchecked")
		public void addUser() {
			
		 U userAdd;			
		 if(strUrl.equals(url))
			userAdd=(U) new User();
				else
			userAdd=(U) new UsersStudent();
				
			if(!imie.trim().equals("") && imie!=null && !nazwisko.trim().equals("") && nazwisko!=null && haslo!=null && !haslo.trim().equals("") && !passRepeat.trim().equals("") && passRepeat!=null 
					&& zmianaHasla!=null) {
				if(haslo.equals(passRepeat)) {
					Boolean res=passwordValidator.validate(haslo);
				if(res) {
					if(strUrl.equals(url)) {
						
						HashMap<String, Object> hms=new HashMap<String,Object>();
						hms.put("loginUsers", login);
						
						if(crudUniv.getAllTermsParam("loginUserIfRepeat",hms).size()==0) {
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
								((User) userAdd).setImieUsers(imie);
								((User) userAdd).setNazwiskoUsers(nazwisko);
								setMyhasHaslo(haslo);
								((User) userAdd).setHasloUsers(myhasHaslo);
								((User) userAdd).setLoginUsers(login);
								((User) userAdd).setZakladUsers(zaklad);
								((User) userAdd).setZablokowany(zablokowany);
								((User) userAdd).setStatusUsers(status);
								((User) userAdd).setPoziomUprawnien(poziomUprawnien);
								((User) userAdd).setZmianaHasla(zmianaHasla);
								((User) userAdd).setZaklads(zakZapisList);
								((User) userAdd).setSpecjalizacjas(zakSpecjalList);
								ul.add((U) userAdd);
								crudUniv.create(userAdd);
						RejestryLogi rl=new RejestryLogi();
						Calendar cal = Calendar.getInstance();  
						Timestamp dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
							String opis;
							int idu=(int) session.getAttribute("idUser");
							opis="Pierwsza rejestracja uzytkownika. Id: "+((User) userAdd).getIdUsers()+" "+((User) userAdd).getImieUsers()+" "+((User) userAdd).getNazwiskoUsers()+", login: "+((User) userAdd).getLoginUsers()+", zakład: "+zaklad+", specjalizacja: "+specjalizacja;
							logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Pierwsza rejestracja.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,((User) userAdd).getIdUsers(), "kadra"));
							
							rl=null;
							
								addMessage("Poprawny zapis.","Zapisano dane.",FacesMessage.SEVERITY_INFO);
					}
						else
							addMessage("Bład zapisu!","Login jest juz zajęty! Zmień login.",FacesMessage.SEVERITY_ERROR);
				}
					else {	
						HashMap<String, Object> hms=new HashMap<String,Object>();
						hms.put("loginUsers", login);
						if(crudUniv.getAllTermsParam("loginUserIfRepeatStudent", hms).size()==0)
								{
								if(kompania!=null && !kompania.trim().equals("") && !pluton.trim().equals("") && pluton!=null && !szkolenie.trim().equals("") && szkolenie!=null)
								{
										((UsersStudent) userAdd).setImieUsers(imie);
										((UsersStudent) userAdd).setNazwiskoUsers(nazwisko);
										setMyhasHaslo(haslo);
										((UsersStudent) userAdd).setHasloUsers(myhasHaslo);
										((UsersStudent) userAdd).setLoginUsers(login);
										((UsersStudent) userAdd).setZablokowany(zablokowany);
										((UsersStudent) userAdd).setSluchacze(wybranySluchacz);
										((UsersStudent) userAdd).setStatusUsers(status);
										((UsersStudent) userAdd).setPoziomUprawnien(poziomUprawnien);
										((UsersStudent) userAdd).setZmianaHasla(zmianaHasla);
										((UsersStudent) userAdd).setKompania(kompania);
										((UsersStudent) userAdd).setPluton(pluton);
										((UsersStudent) userAdd).setSzkolenie(szkolenie);
										ul.add((U) userAdd);
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
			@SuppressWarnings("unchecked")
			U object=(U) e.getComponent().getAttributes().get("removeRow");
			crudUniv.delete(object);
			RejestryLogi rl=new RejestryLogi();
			Calendar cal = Calendar.getInstance();  
			Timestamp dataDzisTimeStamp=new Timestamp(cal.getTimeInMillis());
				String opis;
				
			if(strUrl.equals(url)) {
				
					int idu=(int) session.getAttribute("idUser");
					opis="Usunięcie uzytkownika. Id: "+((User) object).getIdUsers()+" "+((User) object).getImieUsers()+" "+((User) object).getNazwiskoUsers()+", login: "+((User) object).getLoginUsers();
					logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Usunięcie uzytkownika.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,((User) object).getIdUsers(), "Uzytkownik"));
					
					rl=null;
			}
			else {
				HashMap<String,String>hmparamet=new HashMap<String, String>();
				hmparamet.put("archiwum", archiwum);
				
				int idu=(int) session.getAttribute("idUser");
				opis="Usunięcie konta słuchacza. Id: "+((UsersStudent) object).getSluchacze().getIdSluchacz()+" "+((UsersStudent) object).getImieUsers()+" "+((UsersStudent) object).getNazwiskoUsers()+", login: "+((UsersStudent) object).getLoginUsers();
				logi.create(rl.zapiszLogiOsoba(dataDzisTimeStamp,"Usunięcie konta słuchacza.",session.getId(),opis,session.getAttribute("userImieNazwisko")+" Id: "+session.getAttribute("idUser"),idu,((UsersStudent) object).getSluchacze().getIdSluchacz(), "Słuchacz"));
				
				rl=null;
			}
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

			for(Pluton plutStr:plutonyList) {
				if(plutStr.getNazwaPluton().equals(pluton)) {
					if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
						plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
					}
				
				}
			}
			
		}

		public void changeSzkolenie() {
			for(Pluton plutStr:plutonyList) {
				if(plutStr.getNazwaPluton().equals(pluton) && plutStr.getOznaczenieSzkolenia().equals(szkolenie)) {
					if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
						plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
						
					}
						//zeby nie było duplikatow
					sluchaczeSorted=plutStr.getSluchaczes().stream().distinct().collect(Collectors.toList());
					@SuppressWarnings("unchecked")
					List<UsersStudent>uls=(List<UsersStudent>) ul;
					for(Sluchacze sl:sluchaczeSorted) {
						if(uls.stream().filter(ulsf->ulsf.getSluchacze().getIdS()==sl.getIdS()).findFirst().orElse(null)==null) {
							sluchaczeWplutonie.add(sl.getNazwiskoSluchacz()+" "+sl.getImieSluchacz());
						}
						
					}
				}
			}

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
		
		
		 public void save(User us) {
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


			private List<String> plutonListSzkolenie=new ArrayList<String>();
			private List<Sluchacze> sluchaczeSorted;
			
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
			
		
			
			public List<U> getFilteredUl() {
				return filteredUl;
			}
			
			public void setFilteredUl(List<U> filteredUl) {
				this.filteredUl = filteredUl;
			}
			
			public List<U> getUl() {
				return ul;
			}
			
			public void setUl(List<U> ul) {
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
			
			public PersonManager() {
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

			public User getSelectedUser() {
				return selectedUser;
			}

			public void setSelectedUser(User selectedUser) {
				this.selectedUser = selectedUser;
			}
}
