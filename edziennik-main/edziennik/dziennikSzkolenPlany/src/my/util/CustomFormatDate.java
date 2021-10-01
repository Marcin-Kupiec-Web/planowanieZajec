package my.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("customFormatDate")
@RequestScoped
public class CustomFormatDate {

	private String datePattern="dd-MM-yyyy";

	public CustomFormatDate() {
		// TODO Auto-generated constructor stub
	}
public String formatDat(Date date) {
	if(date!=null)
	{
		DateFormat format=new SimpleDateFormat(datePattern);
		return format.format(date);
	}
	return null;
	
}
public String getDatePattern() {
	return datePattern;
}
public void setDatePattern(String datePattern) {
	this.datePattern = datePattern;
}
}
