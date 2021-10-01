package com.userManager.service;

import java.util.List;

import javax.ejb.Local;

@Local
public interface userManagerBeanLocal {
<U> List<U> getUsers(); 
<U> List<U> getZakkladUsers(); 
<U> List<U> getUsersStudent(); 
<U> List<U> getUsers(String finderUser); 
<U> List<U> getUsers(int finderUser); 
<U> List<U> getUsersZaklad(String findPoZaklad); 
<U> List<U> getUsersStudents(String finderUser,String arche); 
<U> List<U> getUsers(String loginUser,String hasloUser); 
<U> List<U> getUsersStudent(String loginUser,String hasloUser); 
<U> List<U> getUsersRepeat(String finderUser);
<U> List<U> getUsersRepeatStudent(String finderUserStudent);
<U> U create(U u);
<U> U update(U u);
<U> void delete(U u);

}
