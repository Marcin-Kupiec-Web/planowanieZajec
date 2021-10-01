package serwis.users.nameUser;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.userManager.model.User;
import com.userManager.service.userManagerBeanLocal;
@Named
@RequestScoped
public class NameUser {
	@EJB
	private userManagerBeanLocal user;
	private List<User> us;
	public NameUser(int idUser) {
		us=user.getUsers(idUser);
	}
	public NameUser() {
		// TODO Auto-generated constructor stub
	}
	
	public String user(int idUser) {
	us=user.getUsers(idUser);
	return us.get(0).getNazwiskoUsers()+" "+us.get(0).getImieUsers();
	}
	public List<User> getUs() {
		return us;
	}
	public void setUs(List<User> us) {
		this.us = us;
	}
}
