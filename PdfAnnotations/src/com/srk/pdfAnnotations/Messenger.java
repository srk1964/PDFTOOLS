package com.srk.pdfAnnotations;

import java.util.logging.Level;

import static java.lang.System.exit;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

import java.util.logging.Logger;

public class Messenger {
	
static final Logger LOGGER = Logger.getLogger(PdfAnnotations.class.getName());
	
 public Messenger(String processdir) throws SecurityException, IOException{
	 final Handler fileHandler  = new FileHandler(processdir+"PdfAnnotations.log");            
 	 LOGGER.addHandler(fileHandler);
 	 LOGGER.setLevel(Level.ALL);
	}

	public void setMessage(String lvl, String msg) {
		switch(lvl){
		case "info":
			 System.out.println(msg);
			 LOGGER.log(Level.INFO,msg);
			 break;
		case "warning":
			 System.out.println(msg);
			 LOGGER.log(Level.WARNING,msg);
			 break;
		case "severe":
			 System.out.println(msg);
			 LOGGER.log(Level.SEVERE,msg);
			 exit(99);
			 break;
		default:
			 System.out.println(msg);
			 LOGGER.log(Level.ALL,msg);
			 break;
		}
		 
		
	}

}