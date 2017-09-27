package com.srk.pdfAnnotations;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.System.exit;

public class PdfAnnotations
{
    
    public static final Logger LOGGER = Logger.getLogger(PdfAnnotations.class.getName());
    
public static void main(String[] args) throws IOException, DocumentException {
    if (args.length==2)
	{
    	String processdir = args[0];
	    String annotationsfile = args[1];

	    
	    Handler fileHandler  = null;
    	fileHandler  = new FileHandler(processdir+"PdfAnnotations.log");            
    	LOGGER.addHandler(fileHandler);
    	LOGGER.setLevel(Level.ALL);
        
    	StringBuilder sb = new StringBuilder();

    	sb.append("**** Echo from program ****\n");
    	sb.append("Process Directory = "+processdir+"\n");
    	sb.append("Annotationsfile = "+annotationsfile+"\n");
    	sb.append("****************************\n");
    	setMessage("info",sb.toString());
	    
	    ProcessTimer pt = new ProcessTimer();
	    ProcessTimer rt = new ProcessTimer();
	    rt.setStartTime();
	    
	    
	    //Combing PDFs process
	    try{
	    	pt.setStartTime();
	    	setMessage("info","Processing Start .."+ processdir+"\n");
	    	
	    	ReadSideFile rsf = new ReadSideFile();
	    	
	    	ArrayList<String> keys = rsf.setKeys(processdir+annotationsfile);
	    	
	    	new AddAnnotations(processdir, keys, LOGGER);
	    	
	    	pt.setEndTime();
	    	pt.setLapsedTime();
	    	
	    	setMessage("info","Processing end  .."+ pt.toString()+"\n");
	    }catch(Exception e){
	    	 setMessage("severe","Error.."+ e.getMessage()+"\n");
	    	 exit(99);
	    }

	 }else
	   {
	       setMessage("severe","******** parameters error *******\n******** parameters passed = "+args.length+"*********\n");
	       exit(99);
	   }  
	}
private static void setMessage(String lvl,String msg){
	switch(lvl){
	case "info":
		 System.out.println(msg);
		 LOGGER.log(Level.INFO,msg);
		 break;
	case "severe":
		 System.out.println(msg);
		 LOGGER.log(Level.SEVERE,msg);
		 break;
	default:
		 System.out.println(msg);
		 LOGGER.log(Level.ALL,msg);
		 break;
	}
	 
	
}
}

