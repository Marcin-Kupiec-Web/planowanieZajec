package serwis.plutony;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.edziennik.sluchacze.zajecia.model.Pluton;

@Named
@RequestScoped
public class Plutony implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Plutony() {
		// TODO Auto-generated constructor stub
	}
	
	@EJB
	private CrudAllLocal crudPlutony;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu; 
	
	private List<Pluton> pluto;
		
	@PostConstruct
    public void init() {
		pluto=new ArrayList<Pluton>();
		HashMap<String,Object>hmparamet=new HashMap<String, Object>();
		hmparamet.put("archiwum", "NIE");
		pluto=crudPlutony.getAllTermsParam("Pluton.findAll", hmparamet);
	}
	
public void goPlutonZajecia(Pluton pl,String page, String param1, String param2) throws IOException {
	wyborPlutonu.setKompaniaWybrana(pl.getKompania().getNazwaKompania());
	wyborPlutonu.changeKompania();
	wyborPlutonu.setPlutonWybrany(pl.getNazwaPluton());
	wyborPlutonu.changePluton();
	wyborPlutonu.setWybraneSzkolenie(pl.getOznaczenieSzkolenia());
	wyborPlutonu.setIdPlutonWybrany(pl.getIdPluton());
	wyborPlutonu.prepareChangeSzkolenie();
	FacesContext.getCurrentInstance().getExternalContext().redirect(page+"?"+param1+"&"+param2);
}

//---------------------------------------------GETTERS SETTERS-----------------------------------------------------------------
	public List<Pluton> getPluto() {
		return pluto;
	}

	public void setPluto(List<Pluton> pluto) {
		this.pluto = pluto;
	}

	public serwis.wyborPlutonu.wyborPlutonu getWyborPlutonu() {
		return wyborPlutonu;
	}

	public void setWyborPlutonu(serwis.wyborPlutonu.wyborPlutonu wyborPlutonu) {
		this.wyborPlutonu = wyborPlutonu;
	}
}
