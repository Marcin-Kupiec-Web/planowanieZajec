package servis.kalendarz;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
@Named
@ViewScoped
public class CalendarView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date dateLeftPanel;

	public CalendarView() {
	}
	
	public void onDateSelect(SelectEvent event) {
		FacesContext facesContext=FacesContext.getCurrentInstance();
		SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Date Selected",format.format(event.getObject())));
	}
	public void click() {
		PrimeFaces.current().ajax().update("form:display");
		PrimeFaces.current().executeScript("PF('dlg').show()");
	}

	public Date getDateLeftPanel() {
		return dateLeftPanel;
	}
	public void setDateLeftPanel(Date dateLeftPanel) {
		this.dateLeftPanel = dateLeftPanel;
	}

}

