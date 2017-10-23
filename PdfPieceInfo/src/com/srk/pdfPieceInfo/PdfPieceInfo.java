package com.srk.pdfPieceInfo;

import com.itextpdf.text.DocumentException;

import itextpieceinfo.DocumentPieceInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.System.exit;

public class PdfPieceInfo
{
    
    
    
public static void main(String[] args) throws IOException, DocumentException {
    if (args.length==2)
	{
    	String processdir = args[0];
	    String annotationsfile = args[1];
  
	    Messenger messenger = new Messenger(processdir);
	    //Handler fileHandler  = null;
    	//fileHandler  = new FileHandler(processdir+"PdfAnnotations.log");            
    	//LOGGER.addHandler(fileHandler);
    	//LOGGER.setLevel(Level.ALL);
        
    	StringBuilder sb = new StringBuilder();

    	sb.append("**** Echo from program ****\n");
    	sb.append("Process Directory = "+processdir+"\n");
    	sb.append("Annotationsfile = "+annotationsfile+"\n");
    	sb.append("****************************\n");
    	messenger.setMessage("info",sb.toString());
	    
	    ProcessTimer pt = new ProcessTimer();
	    ProcessTimer rt = new ProcessTimer();
	    rt.setStartTime();
	    
	    
	    try{
	    	pt.setStartTime();
	    	messenger.setMessage("info","Processing Start .."+ processdir+"\n");
	    	
	    	ReadSideFile rsf = new ReadSideFile();
	    	
	    	ArrayList<String> keys = rsf.setKeys(processdir+annotationsfile);
	    	
	    	// run this to create Annotations tags..
	    	//new AddAnnotations(processdir, keys, LOGGER);
	    	// run this to create PieceInfo tags
	    	 messenger.setMessage("info","Start Processing files in .."+ processdir +"\n");
	    	new DocumentPieceInfo(processdir, keys, messenger);
	    	 messenger.setMessage("info","End Processing files in .."+ processdir +"\n");
	    	
	    	pt.setEndTime();
	    	pt.setLapsedTime();
	    	
	    	messenger.setMessage("info","Processing end  .."+ pt.toString()+"\n");
	    }catch(Exception e){
	    	messenger.setMessage("severe","Error.."+ e.getMessage()+"\n");
	    	 exit(99);
	    }

	 }else
	   {
		 System.out.println("******** parameters error *******\n******** parameters passed = "+args.length+"*********\n");
	       exit(99);
	   }  
	}
}

