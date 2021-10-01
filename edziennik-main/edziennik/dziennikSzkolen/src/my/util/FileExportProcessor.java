package my.util;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
@Named
@RequestScoped
public class FileExportProcessor{

	public void preProcessPDF(Object document) {
	      Document pdf = (Document) document;
	      pdf.setPageSize(PageSize.A4.rotate());
	      pdf.open();	     
	    }
}
