package serwis.zarzadzanie.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.event.RowEditEvent;

import com.edziennik.crudAll.CrudAll;
import com.edziennik.sale.model.Sale;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.Zaklad;

import my.util.MessagePlay;
@Named
@ViewScoped
public class Sales implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sales() {
		// TODO Auto-generated constructor stub
	}
	
			@EJB
		private CrudAll crudAll;
		private String[] selectedZaklad;
		private String[] selectedSpecjal;
		private List<String> selectedZakladListString;
		private List<Zaklad> selectedZakladList;
		private List<String> selectedSpecjalListString;
		private List<Specjalizacja> selectedSpecjalList;
		private String nazwaSali=null;
		private String komentarzDoSali=null;
		private List<Sale> wszystkieSale;
		private boolean edycjaTabeli=false;
		FacesContext fct=FacesContext.getCurrentInstance();
		HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
		
	@PostConstruct
    public void init() {
		selectedZakladList=new ArrayList<Zaklad>();
		
		selectedZakladList=crudAll.getAllTerms("Zaklad.findAll");
		
		selectedZakladListString=new ArrayList<String>();
	
		if (selectedZakladList != null)
		for (Zaklad zk:selectedZakladList) {
				selectedZakladListString.add(zk.getNazwaSkrot());
		}
		
		selectedSpecjalList=new ArrayList<Specjalizacja>();
		
		selectedSpecjalList=crudAll.getAllTerms("Specjalizacja.findAll");
		
		selectedSpecjalListString=new ArrayList<String>();
		
		if (selectedSpecjalList != null)
		for (Specjalizacja sp:selectedSpecjalList) {
				selectedSpecjalListString.add(sp.getNazwa());
		}
		
		wszystkieSale = new ArrayList<Sale>();
	
		wszystkieSale=crudAll.getAllTerms("Sale.findAll");
	}
	
	public void rendExpTab() {
		String ed="";
		if(edycjaTabeli)
			ed="Edycja tabeli właczona";
			else
			ed="Edycja tabeli wyłaczona";
		new MessagePlay(ed.toString(),null,FacesMessage.SEVERITY_INFO);
	}

	
	//------------------------------------------------------zapis sali-------------------------------------------
	
	public void addSala() {
		List<Zaklad> zakZapisList=new ArrayList<Zaklad>();
		List<Specjalizacja> zakSpecjalList=new ArrayList<Specjalizacja>();
		
		for(String zstr:selectedZaklad) {
					Zaklad zf=selectedZakladList.stream().filter(szf->szf.getNazwaSkrot().equals(zstr)).findFirst().orElse(null);
					if(zf!=null) {
								zakZapisList.add(zf);
					}
		}
		
		for(String zstrs:selectedSpecjal) {
			Specjalizacja zf=selectedSpecjalList.stream().filter(szf->(szf.getNazwa()).equals(zstrs)).findFirst().orElse(null);
			if(zf!=null) {
						zakSpecjalList.add(zf);
				}
		}
		Sale ifExistSala=wszystkieSale.stream().filter(s->s.getNazwa().equals(nazwaSali)).findFirst().orElse(null);
		if(ifExistSala==null) {
		if(nazwaSali!=null) {
				Sale salaSave=new Sale();
				salaSave.setNazwa(nazwaSali);
				salaSave.setZaklads(zakZapisList);
				salaSave.setSpecjalizacjas(zakSpecjalList);
				salaSave.setKomentarz(komentarzDoSali);
				crudAll.create(salaSave);
				wszystkieSale.add(salaSave);
		}
		new MessagePlay("Zapisano",null,FacesMessage.SEVERITY_INFO);
		}else {
			new MessagePlay("Błąd. Sala o takiej nazwie juz istnieje. Podaj inną nazwę.",null,FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public String[] onRowEditInitZaklad(List<Zaklad> zl) {
		int i=0;
		
		if(zl!=null && zl.size()>0) {
			selectedZaklad=new String[zl.size()];
			for(Zaklad uz:zl) {
				selectedZaklad[i]=uz.getNazwaSkrot();
				i++;
			}	
		}
		return selectedZaklad;
		
	}
	public String[] onRowEditInitSpecjal(List<Specjalizacja> zl) {
		int i=0;
		
		if(zl!=null && zl.size()>0) {
			selectedSpecjal=new String[zl.size()];
			for(Specjalizacja uz:zl) {
				selectedSpecjal[i]=uz.getNazwa();
				i++;
			}	
		}
		return selectedSpecjal;
		
	}
	public void onRowEdit(RowEditEvent evt) {
		
		List<Zaklad> zakZapisList=new ArrayList<Zaklad>();
		List<Specjalizacja> zakSpecjalList=new ArrayList<Specjalizacja>();
		
		for(String zstr:selectedZaklad) {
					Zaklad zf=selectedZakladList.stream().filter(szf->szf.getNazwaSkrot().equals(zstr)).findFirst().orElse(null);
					if(zf!=null) {
								zakZapisList.add(zf);
					}
		}
		
				for(String zstrs:selectedSpecjal) {
					Specjalizacja zf=selectedSpecjalList.stream().filter(szf->(szf.getNazwa()).equals(zstrs)).findFirst().orElse(null);
					if(zf!=null) {
								zakSpecjalList.add(zf);
					}
			}
			
			Sale sa=(Sale) evt.getObject();
			sa.setZaklads(zakZapisList);
			sa.setSpecjalizacjas(zakSpecjalList);
			crudAll.update(sa);
		
			new MessagePlay("Uaktualniono!",null,FacesMessage.SEVERITY_INFO);
	}
	
	public void remove(Sale sala) {
		
		if(sala.getPrzedmiottemats()==null && sala.getPrzedmiottemats().size()==0) {
				wszystkieSale.remove(sala);
				crudAll.delete(sala);
				
				new MessagePlay("Usunięto!",null,FacesMessage.SEVERITY_WARN);
		}else {
			new MessagePlay("Bląd! Sala przypisana jest do przedmiotu!",null,FacesMessage.SEVERITY_ERROR);
		}
}
	
	public String zakladToString(List<Zaklad> zl) {
		String zls ="";
		if(zl!=null && zl.size()>0) {
		for(Zaklad uz:zl) {
			zls+=uz.getNazwaSkrot()+", ";
		}
		zls=zls.trim();
		onRowEditInitZaklad(zl);
	return zls.substring(0, zls.length() -1);	
	
		}
		return "";
	}
	
	public String specjalToString(List<Specjalizacja> spec) {
		String sls ="";
		if(spec!=null && spec.size()>0) {
		for(Specjalizacja spl:spec) {
			sls+=spl.getNazwa()+", ";
		}
		sls=sls.trim();
		onRowEditInitSpecjal(spec);
	return sls.substring(0, sls.length() -1);	
		}
		return "";
	}
	
	public String[] getSelectedZaklad() {
		return selectedZaklad;
	}

	public void setSelectedZaklad(String[] selectedZaklad) {
		this.selectedZaklad = selectedZaklad;
	}

	public String[] getSelectedSpecjal() {
		return selectedSpecjal;
	}

	public void setSelectedSpecjal(String[] selectedSpecjal) {
		this.selectedSpecjal = selectedSpecjal;
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

	public List<String> getSelectedSpecjalListString() {
		return selectedSpecjalListString;
	}

	public void setSelectedSpecjalListString(List<String> selectedSpecjalListString) {
		this.selectedSpecjalListString = selectedSpecjalListString;
	}

	public List<Specjalizacja> getSelectedSpecjalList() {
		return selectedSpecjalList;
	}

	public void setSelectedSpecjalList(List<Specjalizacja> selectedSpecjalList) {
		this.selectedSpecjalList = selectedSpecjalList;
	}

	public String getNazwaSali() {
		return nazwaSali;
	}

	public void setNazwaSali(String nazwaSali) {
		this.nazwaSali = nazwaSali;
	}

	public String getKomentarzDoSali() {
		return komentarzDoSali;
	}

	public void setKomentarzDoSali(String komentarzDoSali) {
		this.komentarzDoSali = komentarzDoSali;
	}


	public List<Sale> getWszystkieSale() {
		return wszystkieSale;
	}


	public void setWszystkieSale(List<Sale> wszystkieSale) {
		this.wszystkieSale = wszystkieSale;
	}


	public boolean isEdycjaTabeli() {
		return edycjaTabeli;
	}


	public void setEdycjaTabeli(boolean edycjaTabeli) {
		this.edycjaTabeli = edycjaTabeli;
	}

}
