package PdfAnnots;

import static PdfAnnots.CombinePDFs.CombinePDFs;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class PdfAnnots
{
    public static String CLIENT_ID = null;
    public static String CLIENT_PRODUCT_ID = null;
    public static String JOB_NUMBER = null;
    public static String ENVIRONMENT = null;
    private static final Logger LOGGER = Logger.getLogger(PdfAnnots.class.getName());
    
public static void main(String[] args) throws IOException, DocumentException {
    if (args.length==9)
	{
    	String bypasszip = args[0];
    	CLIENT_ID = args[1];
	    CLIENT_PRODUCT_ID = args[2];
	    JOB_NUMBER = args[3];
	    ENVIRONMENT = args[4];
	    String processdir = args[5];
	    String zipfile = args[6];
	    String pdfout = args[7];
	    String xmlout = args[8];
	    
	    Handler fileHandler  = null;
    	fileHandler  = new FileHandler(processdir+JOB_NUMBER+".log");            
    	LOGGER.addHandler(fileHandler);
    	LOGGER.setLevel(Level.ALL);
        
    	StringBuilder sb = new StringBuilder();

    	sb.append("======================================================\n");
    	sb.append("Skip Zip Process = "+bypasszip+"\n");
    	sb.append("Client ID = "+CLIENT_ID+"\n");
    	sb.append("Client Product ID = "+CLIENT_PRODUCT_ID+"\n");
    	sb.append("Job Number = "+JOB_NUMBER+"\n");
    	sb.append("Environment = "+ENVIRONMENT+"\n");
    	sb.append("Process Directory = "+processdir+"\n");
    	sb.append("ZIPFILE = "+zipfile+"\n");
    	sb.append("PDFOUT = "+pdfout+"\n");
    	sb.append("XMLFOUT = "+xmlout+"\n");
    	sb.append("======================================================\n");
    	setMessage("info",sb.toString());
	    
	    String workingdir = processdir+"temp";
	    ProcessTimer pt = new ProcessTimer();
	    ProcessTimer rt = new ProcessTimer();
	    rt.setStartTime();
	    
	    if(bypasszip.equalsIgnoreCase("skip")){
	    	setMessage("info","Bypassed Processing zip file .."+ zipfile);
	    }
	    else{
	    //unzip process
	    try{
	    	setMessage("info","Processing zip file .."+ zipfile);
	    	pt.setStartTime();
	    	HandleFiles hf = new HandleFiles(processdir,zipfile,workingdir);
	    	pt.setEndTime();
	    	pt.setLapsedTime();
	    	setMessage("info","Processed zip file .."+ pt.toString());
	    }catch(Exception e){
	    	 setMessage("severe","Error during unzip process.."+ e.getMessage());
	    	 exit(99);
	    }
	    }
	    
	    //Combing PDFs process
	    try{
	    	pt.setStartTime();
	    	setMessage("info","Combining files in.."+ workingdir);
	    	CombinePDFs(processdir,workingdir,pdfout);
	    	setMessage("info",pdfout+ " created ");
	    	pt.setEndTime();
	    	pt.setLapsedTime();
	    	setMessage("info","Combining files .."+ pt.toString());
	    }catch(Exception e){
	    	 setMessage("severe","Error during Combining PDFs process.."+ e.getMessage());
	    	 exit(99);
	    }
	    
	    //Capture annotations process
	    try{ 
	    	setMessage("info","Capture annotations from.."+ pdfout);
	    	pt.setStartTime();
	        GetAnnotations ga = new GetAnnotations(processdir,pdfout);
	        pt.setEndTime();
	        pt.setLapsedTime();
	        setMessage("info","Capture annotations .."+ pt.toString());
	        //Use annotations to write xml file
	        if(ga.getStatus().equals("ok") && ga.annotations.size() > 0)
	        {
	        pt.setStartTime();
	          setMessage("info","Writing XML file...Page Count="+ga.annotations.size());
	          WriteXML wxml = new WriteXML(processdir+xmlout,ga.annotations);
	          pt.setEndTime();
	          pt.setLapsedTime();
		      setMessage("info","XML file written .."+ pt.toString());
	        }else{
	        	setMessage("severe","Annotation count "+ ga.annotations.size()+"\nError creating output file... error "+ ga.getStatus());
	          exit(99);
	                }
	     }catch(Exception e){
	    	 setMessage("severe","Error during Annotation process.."+ e.getMessage());
	    	 exit(99);
	     }
	    rt.setEndTime();
    	rt.setLapsedTime();
    	setMessage("info","Total Processing time .."+ rt.toString());
	 }else
	   {
	       setMessage("severe","******** Three parameters not passed*******\nparameters passed = "+args.length);
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

