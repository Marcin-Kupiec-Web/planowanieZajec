package serwis.wyborPlutonu;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Kompania;
import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.userManager.model.UsersStudent;

import serwis.logowanie.Logowanie;
 

@Named
@SessionScoped
public class wyborPlutonu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAllLocal crudAll;
	@Inject
	private Logowanie zalogowanyUser;
	
private List<Kompania> kompanieList;
private List<String> kompanieListNazwa=new ArrayList<String>();
private List<Pluton> plutonyList=new ArrayList<Pluton>();
private Map<String,String> plutonListNazwa=new HashMap<String,String>();
private List<String> plutonListSzkolenie=new ArrayList<String>();
private List<Sluchacze> sluchaczeSorted;
private String kompaniaWybrana;
private String plutonWybrany;
private String wybraneSzkolenie;
private Kompania wybranaKompaniaObject;
private Date dataFormGlowny=new Date();
private Integer idPlutonWybrany=null;
private String jmpw=null;
private String jspw=null;
private String kpnpw=null;
private String move = null;

FacesContext fct=FacesContext.getCurrentInstance();
HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);

@PostConstruct
public void init() {
	
	
	HashMap<String, Object> hms=new HashMap<String,Object>();
	hms.put("archiwum", "NIE");
	kompanieList=crudAll.getAllTermsParam("Kompania.findAll", hms);

	for(Kompania kompStr:kompanieList) {
		kompanieListNazwa.add(kompStr.getNazwaKompania());
	}
	
	if(zalogowanyUser!=null && zalogowanyUser.getPoziomUprawnien().equals("7")) {
		HashMap<String, Object> hmsl=new HashMap<String,Object>();
		hmsl.put("idUsers", zalogowanyUser.getIdUsers());
        try {
        	UsersStudent sl=(UsersStudent) crudAll.getAllTermsParam("findUserPoIdus", hmsl).get(0);
    		idPlutonWybrany=sl.getSluchacze().getPluton().getIdPluton();
    		jmpw=sl.getSluchacze().getPluton().getIdKurs().getjModol();
    		jspw=sl.getSluchacze().getPluton().getIdKurs().getjSzkol();
    		kpnpw=sl.getSluchacze().getPluton().getIdKurs().getKpn();
    	
        } catch(IndexOutOfBoundsException e) {
        }
	}
	
}

	public void changeKompania() {
		
		plutonListNazwa.clear();
		plutonListSzkolenie.clear();
		plutonWybrany=null;
		wybraneSzkolenie=null;
		idPlutonWybrany=null;
		sluchaczeSorted=null;
		
		for(Kompania kompStr:kompanieList) {
			if(kompStr.getNazwaKompania().equals(kompaniaWybrana)) {
				plutonyList=kompStr.getPlutons();
				 for(Pluton plutStr:plutonyList) {
					 plutonListNazwa.put(plutStr.getNazwaPluton(),plutStr.getNazwaPluton());
				 } 
				 wybranaKompaniaObject=kompStr;
			}
		}
		
	}
	public void changePluton() {
		plutonListSzkolenie.clear();
		wybraneSzkolenie=null;
		idPlutonWybrany=null;
		sluchaczeSorted=null;
	
		
		for(Pluton plutStr:plutonyList) {
			if(plutStr.getNazwaPluton().equals(plutonWybrany) && plutStr.getArchiwum().equals("NIE")) {
				if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
					plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
				}
			
			}
		}
		
	}

public void changeSzkolenie() {
	prepareChangeSzkolenie();
	
		 	try {
		 		FacesContext ctx = FacesContext.getCurrentInstance();
		 		 HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
				ctx.getExternalContext().redirect(request.getRequestURI());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
}

public void prepareChangeSzkolenie() {
	
			if(move!=null)
			{
					Calendar cal=Calendar.getInstance();
					
					if(move.equals("prev")) {
						cal.setTime(dataFormGlowny);
						cal.add(Calendar.DATE, -1);
					} else if(move.equals("next")) {
						cal.setTime(dataFormGlowny);
						cal.add(Calendar.DATE, +1);
					}
					dataFormGlowny=cal.getTime();
			}
			
		if(!zalogowanyUser.getPoziomUprawnien().equals("7"))
			{
		
			if(wybraneSzkolenie == null || wybraneSzkolenie.isEmpty()){
				idPlutonWybrany = null;
				sluchaczeSorted=null;
				jspw = null;
				kpnpw = null;
			} else
			{
				for(Pluton plutStr:plutonyList) {
					if(plutStr.getNazwaPluton().equals(plutonWybrany) && plutStr.getOznaczenieSzkolenia().equals(wybraneSzkolenie)) {
						if(!plutonListSzkolenie.contains(plutStr.getOznaczenieSzkolenia())) {
							plutonListSzkolenie.add(plutStr.getOznaczenieSzkolenia());
						}
						idPlutonWybrany=plutStr.getIdPluton();
						jmpw=plutStr.getIdKurs().getjModol();
						jspw=plutStr.getIdKurs().getjSzkol();
						kpnpw=plutStr.getIdKurs().getKpn();
						}
					}
			}
		}
		move=null;
}
	public List<Kompania> getKompanieList() {
		return kompanieList;
	}

	public void setKompanieList(List<Kompania> kompanieList) {
		this.kompanieList = kompanieList;
	}

	public String getKompaniaWybrana() {
		return kompaniaWybrana;
	}

	public void setKompaniaWybrana(String kompaniaWybrana) {
		this.kompaniaWybrana = kompaniaWybrana;
	}

	public List<String> getKompanieListNazwa() {
		return kompanieListNazwa;
	}

	public void setKompanieListNazwa(List<String> kompanieListNazwa) {
		this.kompanieListNazwa = kompanieListNazwa;
	}
	public List<Pluton> getPlutonyList() {
		return plutonyList;
	}

	public void setPlutonyList(List<Pluton> plutonyList) {
		this.plutonyList = plutonyList;
	}

	public Date getDataFormGlowny() {
		return dataFormGlowny;
	}

	public void setDataFormGlowny(Date dataFormGlowny) {
		this.dataFormGlowny = dataFormGlowny;
	}

	public Map<String, String> getPlutonListNazwa() {
		return plutonListNazwa;
	}

	public void setPlutonListNazwa(Map<String, String> plutonListNazwa) {
		this.plutonListNazwa = plutonListNazwa;
	}

	public List<String> getPlutonListSzkolenie() {
		return plutonListSzkolenie;
	}

	public void setPlutonListSzkolenie(List<String> plutonListSzkolenie) {
		this.plutonListSzkolenie = plutonListSzkolenie;
	}

	public String getPlutonWybrany() {
		return plutonWybrany;
	}

	public void setPlutonWybrany(String plutonWybrany) {
		this.plutonWybrany = plutonWybrany;
	}


	public String getWybraneSzkolenie() {
		return wybraneSzkolenie;
	}

	public void setWybraneSzkolenie(String wybraneSzkolenie) {
		this.wybraneSzkolenie = wybraneSzkolenie;
	}

	public Kompania getWybranaKompaniaObject() {
		return wybranaKompaniaObject;
	}

	public void setWybranaKompaniaObject(Kompania wybranaKompaniaObject) {
		this.wybranaKompaniaObject = wybranaKompaniaObject;
	}

	public Integer getIdPlutonWybrany() {
		return idPlutonWybrany;
	}

	public void setIdPlutonWybrany(Integer idPlutonWybrany) {
		this.idPlutonWybrany = idPlutonWybrany;
	}


	public List<Sluchacze> getSluchaczeSorted() {
		return sluchaczeSorted;
	}

	public void setSluchaczeSorted(List<Sluchacze> sluchaczeSorted) {
		this.sluchaczeSorted = sluchaczeSorted;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public String getJmpw() {
		return jmpw;
	}

	public void setJmpw(String jmpw) {
		this.jmpw = jmpw;
	}

	public String getJspw() {
		return jspw;
	}

	public void setJspw(String jspw) {
		this.jspw = jspw;
	}

	public String getKpnpw() {
		return kpnpw;
	}

	public void setKpnpw(String kpnpw) {
		this.kpnpw = kpnpw;
	}

}

