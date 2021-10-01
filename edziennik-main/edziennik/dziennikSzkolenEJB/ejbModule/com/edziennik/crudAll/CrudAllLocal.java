package com.edziennik.crudAll;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

@Local
public interface CrudAllLocal {
	<C> List<C> getAllTerms(String findWhat); 
	<C> List<C> getAllTermsParam(String findWhat,HashMap<String,Object>hm); 
	<C> C create(C createData);
	<C> C update(C updateData);
	<C> void delete(C deleteData);
}
