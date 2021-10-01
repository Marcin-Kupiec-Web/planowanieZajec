package serwis.wiadomosci;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import com.userManager.model.User;

import serwis.users.nameUser.NameUser;

@Named
@RequestScoped
@FacesConverter(value = "userConverter", managed = true)
public class UserConverter implements Converter<Object> {
	@Inject
	private NameUser nameUser;
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
	     if(value != null && value.trim().length() > 0) {
	            try {
	                return nameUser.userObject(Integer.parseInt(value));
	            } catch(NumberFormatException e) {
	                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid user."));
	            }
	        }
	        else {
	            return null;
	        }
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
	    if(object != null) {
            return String.valueOf(((User) object).getIdUsers());
        }
        else {
            return null;
        }
    } 
	

	public NameUser getNameUser() {
		return nameUser;
	}

	public void setNameUser(NameUser nameUser) {
		this.nameUser = nameUser;
	}
 
}