package my.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serwis.logowanie.Logowanie;

public class CacheControlPhaseListener implements PhaseListener
{
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private Logowanie zalogowanyUser;
	public PhaseId getPhaseId()
    {
        return PhaseId.RENDER_RESPONSE;
    }
 
    public void afterPhase(PhaseEvent event)
    {
    }
 
    public void beforePhase(PhaseEvent event)
    {
        FacesContext facesContext = event.getFacesContext();
        HttpServletResponse response = (HttpServletResponse) facesContext
                .getExternalContext().getResponse();
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        // Stronger according to blog comment below that references HTTP spec
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "must-revalidate");
        // some date in the past
        response.addHeader("Expires", "Mon, 8 Aug 2006 10:00:00 GMT");
    
        if (zalogowanyUser!=null && zalogowanyUser.getSessionLogedKlient()==null) {
        	try {
        		 HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        		if(!request.getRequestURI().contains("index") && !request.getRequestURI().contains("errLog"))
				FacesContext.getCurrentInstance().getExternalContext().redirect("/dziennikSzkolen/index.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
      
    }
}