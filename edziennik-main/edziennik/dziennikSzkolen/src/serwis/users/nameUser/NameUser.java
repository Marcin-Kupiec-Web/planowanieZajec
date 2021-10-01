package serwis.users.nameUser;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAllLocal;
import com.userManager.model.User;
import com.userManager.model.UsersStudent;
@Named
@RequestScoped
public class NameUser {
	@EJB
	private CrudAllLocal crudAll;
	private List<User> us;
	
	public User userObject(int idUser) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idUser);
		us=crudAll.getAllTermsParam("findUserPoId", hms);
		return us.get(0);
	}

	public UsersStudent userStudentObject(int idUser) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idUser);
		return (UsersStudent) crudAll.getAllTermsParam("findUserPoIdus", hms).get(0);
	}
	
	public String user(int idUser) {
		HashMap<String, Object> hms=new HashMap<String,Object>();
		hms.put("idUsers", idUser);
		us=crudAll.getAllTermsParam("findUserPoId", hms);
	return us.get(0).getNazwiskoUsers()+" "+us.get(0).getImieUsers();
	}
	public List<User> getUs() {
		return us;
	}
	public void setUs(List<User> us) {
		this.us = us;
	}
	
}
