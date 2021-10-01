package my.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class MessagePlay {
	
	public MessagePlay() {
		
	}
	public MessagePlay(String summary,String detail, Severity ms) {
		addMessage(summary,detail,ms);
	}
	public void addMessage(String summary,String detail, Severity ms) {
		FacesMessage message=new FacesMessage(ms,summary,detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
