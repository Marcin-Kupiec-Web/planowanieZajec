package com.edziennik.zajecia.frekwencja.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface FrekwencjaLocal {
	
	<F> List<F> getObecnoscInDay(Integer idPlutonWybrany, Date data); 
	<F> F create(F f);
	<F> F update(F f);
	<F> void delete(F f);
	<F> List<F> findByObecnoscById(int idF);
	<F> List<F> findNbSp(String idPluton);
}
