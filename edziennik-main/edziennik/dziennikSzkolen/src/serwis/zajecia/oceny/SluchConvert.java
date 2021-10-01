package serwis.zajecia.oceny;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.edziennik.sluchacze.zajecia.model.Sluchacze;

@SuppressWarnings("rawtypes")
@FacesConverter("sluchConvert")
public class SluchConvert implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		System.out.println("getAsObject");
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
		// TODO Auto-generated method stub
	
		
		if(object==null) {
			return "";
		}
		if(object instanceof Sluchacze)
		{		
			Sluchacze sluch=(Sluchacze) object;
			String name=sluch.getNazwiskoSluchacz();
			System.out.println("getAsString "+sluch.getNazwiskoSluchacz());
			return name;
		}
		
		return object.toString();
	}
	
}
