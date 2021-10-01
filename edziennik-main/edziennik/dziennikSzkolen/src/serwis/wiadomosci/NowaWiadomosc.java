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
import com.edziennik.sluchacze.zajecia.model.Kompania;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.edziennik.wiadomosci.model.Wiadomosci;
import com.userManager.model.User;
@Named
@ViewScoped
public class NowaWiadomosc implements Serializable{
	
	/**
	 * nowa wiadomość dla słuchaczy
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private serwis.users.nameUser.NameUser nameUser;

	@Inject
	private serwis.logowanie.Logowanie zalogowanyUser;
	
	private Pluton plutonWybrany;
	private List<Sluchacze> sluchaczeSorted;
	private User wpisujacyUser;
	private List<Sluchacze> selectedUsers;
	private Integer idUsers;
	private Date dataDzis=new Date();
	private Timestamp dataFormGlownyTimeStamp;
	private boolean render=false;
	private List<Sluchacze> selectedSluchacze;
	private List<Sluchacze> sluchaczeMultiList;
	private Wiadomosci wiadomosc;
	private List<Wiadomosci> wiadomosci;
	private boolean wszyscyAdresaci=false;
	private String kompania;
	private String pluton;
	private String szkolenie;
	private List<String> kompanieListNazwa;
	private List<Pluton> plutonyList;
	private List<String> plutonListNazwa;
	private List<String> plutonListSzkolenie=new ArrayList<String>();
	private List<Kompania> kompanieList;
	
	@PostConstruct 
	    public void init() {
			  	setIdUsers(zalogowanyUser.getIdUsers());
				HashMap<String, Object> hmsu=new HashMap<String,Object>();
				hmsu.put("idUsers", idUsers);
			   wpisujacyUser=(User) crudAll.getAllTermsParam("findUserPoId", hmsu).get(0);
			   			   
			  	Calendar cal = Calendar.getInstance();  
		    	dataDzis=cal.getTime();
		    	setDataFormGlownyTimeStamp(new Timestamp(cal.getTimeInMillis()));
		    	kompanieList=new ArrayList<Kompania>();
		    	kompanieListNazwa=new ArrayList<String>();
		    	plutonyList=new ArrayList<Pluton>();
		    	plutonListNazwa=new ArrayList<String>();
		    	
			  	wiadomosc = new Wiadomosci();
			    wiadomosci = new ArrayList<Wiadomosci>();
						    		    		
						HashMap<String,Object>hmpk=new HashMap<String, Object>();
						hmpk.put("archiwum","NIE");
						kompanieList=crudAll.getAllTermsParam("Kompania.findAll",hmpk);;
						
						for(Kompania kompStr:kompanieList) {
							kompanieListNazwa.add(kompStr.getNazwaKompania());
						}
	
	 }
	
public void selectedAll() {
	if(selectedSluchacze.size()!=0)
	wszyscyAdresaci=true;
	
}
	   public void createNew() {
		
		   		   
		   if(!wszyscyAdresaci) {				   
				
				  wiadomosc.setUser(wpisujacyUser);
				  wiadomosc.setSluchaczOdbiorca(selectedSluchacze);
				  wiadomosc.setKtoWpisal(idUsers);
			   	  wiadomosc.setDataWpisu(dataFormGlownyTimeStamp);
			   		crudAll.create(wiadomosc);
			   	wiadomosc=null;
			   	
			   
		   }else
		   {
		   		wiadomosc.setPluton(plutonWybrany);
		   		wiadomosc.setUser(wpisujacyUser);
		   		wiadomosc.setKtoWpisal(idUsers);
		   		wiadomosc.setDataWpisu(dataFormGlownyTimeStamp);
		   		crudAll.create(wiadomosc);
		   }
	            reinit();
	            addMessage("W y s ł a n o."," wiadomość.",FacesMessage.SEVERITY_INFO);
	            selectedSluchacze=null;
	            wszyscyAdresaci=false;
	    }
	     
	    public String reinit() {
	        wiadomosc = new Wiadomosci();
	        selectedSluchacze=null;
	        wszyscyAdresaci=false;
	    	plutonListNazwa.clear();
			plutonListSzkolenie.clear();
			pluton=null;
			szkolenie=null;
			kompania=null;
			sluchaczeSorted=null;
	        //addMessage("Rezygnacja.","Zamknięto wiadomość.",FacesMessage.SEVERITY_INFO);
	        return null;
	    }

	public void addMessage(String summary,String detail, Severity ms) {
		FacesMessage message=new FacesMessage(ms,summary,detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

		 
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
					plutonWybrany=plutStr;
					sluchaczeSorted=plutStr.getSluchaczes().stream().distinct().collect(Collectors.toList());
					sluchaczeMultiList=new ArrayList<Sluchacze>();
					sluchaczeSorted.forEach(sl->sluchaczeMultiList.add(sl));
				}
			}

		}
//-------------------------------------------------------------GETTERY SETTERY-----------------------------------------------------------------
	
	public List<Sluchacze> getSluchaczeSorted() {
		return sluchaczeSorted;
	}

	public void setSluchaczeSorted(List<Sluchacze> sluchaczeSorted) {
		this.sluchaczeSorted = sluchaczeSorted;
	}

	public void frekwencjaStart() throws IOException {
		System.out.println("ble ble bleeeeeeeeeeeee");
		FacesContext.getCurrentInstance().getExternalContext().redirect("frekwencja.xhtml?i=2&subPage=frekwencja");
	}
 

	public List<Sluchacze> getSelectedUsers() {
	return selectedUsers;
	}
	
	public void setSelectedUsers(List<Sluchacze> selectedUsers) {
	this.selectedUsers = selectedUsers;
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

	public List<Sluchacze> getSelectedSluchacze() {
		return selectedSluchacze;
	}

	public void setSelectedSluchacze(List<Sluchacze> selectedSluchacze) {
		this.selectedSluchacze = selectedSluchacze;
	}

	public List<Sluchacze> getSluchaczeMultiList() {
		return sluchaczeMultiList;
	}

	public void setSluchaczeMultiList(List<Sluchacze> sluchaczeMultiList) {
		this.sluchaczeMultiList = sluchaczeMultiList;
	}
	public boolean isWszyscyAdresaci() {
		return wszyscyAdresaci;
	}
	public void setWszyscyAdresaci(boolean wszyscyAdresaci) {
		this.wszyscyAdresaci = wszyscyAdresaci;
	}
	public User getWpisujacyUser() {
		return wpisujacyUser;
	}
	public void setWpisujacyUser(User wpisujacyUser) {
		this.wpisujacyUser = wpisujacyUser;
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

	public List<String> getKompanieListNazwa() {
		return kompanieListNazwa;
	}

	public void setKompanieListNazwa(List<String> kompanieListNazwa) {
		this.kompanieListNazwa = kompanieListNazwa;
	}

	public List<String> getPlutonListNazwa() {
		return plutonListNazwa;
	}

	public void setPlutonListNazwa(List<String> plutonListNazwa) {
		this.plutonListNazwa = plutonListNazwa;
	}

	public List<Pluton> getPlutonyList() {
		return plutonyList;
	}

	public void setPlutonyList(List<Pluton> plutonyList) {
		this.plutonyList = plutonyList;
	}

	public List<String> getPlutonListSzkolenie() {
		return plutonListSzkolenie;
	}

	public void setPlutonListSzkolenie(List<String> plutonListSzkolenie) {
		this.plutonListSzkolenie = plutonListSzkolenie;
	}

	public Timestamp getDataFormGlownyTimeStamp() {
		return dataFormGlownyTimeStamp;
	}

	public void setDataFormGlownyTimeStamp(Timestamp dataFormGlownyTimeStamp) {
		this.dataFormGlownyTimeStamp = dataFormGlownyTimeStamp;
	}


}

