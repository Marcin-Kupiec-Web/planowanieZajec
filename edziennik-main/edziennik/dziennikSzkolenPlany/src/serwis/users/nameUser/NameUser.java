package serwis.users.nameUser;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.userManager.model.User;

@Named
@RequestScoped
public class NameUser {
	@EJB
	private CrudAllLocal crudAll;
	private List<User> us;
	
	public NameUser(int idUser) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idUser);
		us=crudAll.getAllTermsParam("findUserPoId", hms);
	}
	public NameUser() {
		// TODO Auto-generated constructor stub
	}
	
	public String user(int idUser) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idUser);
		us=crudAll.getAllTermsParam("findUserPoId", hms);
	return us.get(0).getNazwiskoUsers()+" "+us.get(0).getImieUsers();
	}
	
	public User userObj(int idUser) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idUser);
		us=crudAll.getAllTermsParam("findUserPoId", hms);
		if(us.get(0)!=null)
	return us.get(0);
		return null;
	}
	
	public List<User> getUs() {
		return us;
	}
	public void setUs(List<User> us) {
		this.us = us;
	}
}
