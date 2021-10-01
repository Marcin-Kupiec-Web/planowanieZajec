package serwis.zarzadzanie.godzinyZajec;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAllLocal;
import com.godzinyZajec.model.Godzinyzajec;

import my.util.MessagePlay;
@Named
@ViewScoped
public class GodzinyZajec  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CrudAllLocal crudAll;
	
	private List<Godzinyzajec> GodzinyZajecList;
	private Integer nrGodziny;
	private String komentarz;
	private Date godzinaOd;
	private Date godzinaDo;
	
	public GodzinyZajec() {
		// TODO Auto-generated constructor stub
	}
	

	@PostConstruct
    public void init() {
		GodzinyZajecList=crudAll.getAllTerms("Godzinyzajec.findAll");
	}

   public void addGodzineZajec() {
	Godzinyzajec gzadd=new Godzinyzajec();
	gzadd.setNrGodzina(nrGodziny);
	gzadd.setGodzOd(godzinaOd);
	gzadd.setGodzDo(godzinaDo);
	gzadd.setKomentarz(komentarz);
	crudAll.create(gzadd);
	GodzinyZajecList.add(gzadd);
			new MessagePlay("Dodano nowa godzinę zajęć.",null,FacesMessage.SEVERITY_INFO);
   }
	
	
	public void onRowEdit(RowEditEvent ev) throws ParseException {
		Godzinyzajec gzGet=(Godzinyzajec) ev.getObject();
		crudAll.update(gzGet);
		new MessagePlay("Uaktualniono.",null,FacesMessage.SEVERITY_INFO);
}
	public void remove(ActionEvent e) {
		Godzinyzajec remGz=(Godzinyzajec) e.getComponent().getAttributes().get("removeRow");
		
		GodzinyZajecList.remove(remGz);
		crudAll.delete(remGz);
			new MessagePlay("Usunięto gozinę zajęć!",null,FacesMessage.SEVERITY_WARN);
		
			remGz=null;
}
//-------------------------------------------------------- getters setters ----------------------------------------------------
	
	public List<Godzinyzajec> getGodzinyZajecList() {
		return GodzinyZajecList;
	}

	public void setGodzinyZajecList(List<Godzinyzajec> godzinyZajecList) {
		GodzinyZajecList = godzinyZajecList;
	}

	public Integer getNrGodziny() {
		return nrGodziny;
	}

	public void setNrGodziny(Integer nrGodziny) {
		this.nrGodziny = nrGodziny;
	}

	public String getKomentarz() {
		return komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public Date getGodzinaOd() {
		return godzinaOd;
	}

	public void setGodzinaOd(Date godzinaOd) {
		this.godzinaOd = godzinaOd;
	}


	public Date getGodzinaDo() {
		return godzinaDo;
	}


	public void setGodzinaDo(Date godzinaDo) {
		this.godzinaDo = godzinaDo;
	}

}
