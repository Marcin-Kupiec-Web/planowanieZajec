package serwis.zarzadzanie.start;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.edziennik.crudAll.CrudAll;

@Named("start")
@ViewScoped
public class Start implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ileUsers;
	private int ileUsersStudent;
	private int ileSluchacze;
	public Start() {
		// TODO Auto-generated constructor stub
	}
	@EJB
	private CrudAll crudjpa;
	
	@PostConstruct
    public void init() {
		if(crudjpa.getAllTerms("findAllUser")!=null)
		ileUsers=crudjpa.getAllTerms("findAllUser").size();
		if(crudjpa.getAllTerms("findAllUserStudentCount")!=null)
		setIleUsersStudent(crudjpa.getAllTerms("findAllUserStudentCount").size());
		if(crudjpa.getAllTerms("Sluchacze.findAllSluchacze")!=null)
		ileSluchacze=crudjpa.getAllTerms("Sluchacze.findAllSluchacze").size();
	}

	public int getIleUsers() {
		return ileUsers;
	}

	public void setIleUsers(int ileUsers) {
		this.ileUsers = ileUsers;
	}

	public int getIleUsersStudent() {
		return ileUsersStudent;
	}

	public void setIleUsersStudent(int ileUsersStudent) {
		this.ileUsersStudent = ileUsersStudent;
	}

	public int getIleSluchacze() {
		return ileSluchacze;
	}

	public void setIleSluchacze(int ileSluchacze) {
		this.ileSluchacze = ileSluchacze;
	}
}
