package com.srk.pdfPieceInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AddAnnotations {

public AddAnnotations(String processdir, ArrayList<String> keys, Logger LOGGER)  
 {
    try
    {
        Split(processdir, keys, LOGGER);
    }catch(Exception e)
    {
      System.out.println("Error happened " + e.getMessage());
    }    
 }
private static void Split(String processdir,ArrayList<String> keys, Logger LOGGER) throws DocumentException, IOException
    {
    String pdfout = null;
    PdfStamper pdfStamper = null;
    ArrayList<PdfAnnotation> stamp;
    Document document = null;
    PdfReader pdfReader = null;
    int n = 0;
    int lx = 15;
    int ly = 0;
    int ux = 16;
    int uy = 1;
    String key;
    String value;
    try{  
    	int x = 0; 
    for (String keyrecs : keys){
    	String[] splitter = keyrecs.split("\\|");
    	stamp = new ArrayList<>();
         lx = 5;
         ux = 6;
    	for(int k =0;k<splitter.length;k++){
    		//first key has to be file so that we can open the PDF
    		//subsequent keys are parsed into an array of PDF Annotations
		    if(k==0 && splitter[k].contains("FILE")){
			document = new Document();
			//use file name to open reader
			pdfReader = new PdfReader(processdir+splitter[k].split("=")[1]);
			//use file name to create output file name for stamper
		    pdfout = String.format("%s_out.pdf",splitter[k].split("=")[1]);
			document.open();
			pdfStamper = new PdfStamper(pdfReader,new FileOutputStream(processdir+pdfout));
			//get number of pages
			n = pdfReader.getNumberOfPages();
		    }else if(k>0){
		    	//add new annotations while incrementing the rectangle coordinates for lx ux
		    	key = splitter[k].split("=")[0];
		    	value = splitter[k].split("=")[1];
				stamp.add(PdfAnnotation.createText(pdfStamper.getWriter(), new Rectangle(lx, ly, ux, uy),null,"A::PDF(T)\nK("+key+")\nV("+value+")",false, null));
				//increment for next rectangle
				lx = lx + 5;
				ux = ux + 5;
		    }
		    
    	}
			for(int p = 1;p<(n+1);p++)
			{
				//if(p==1)
				//{
					for(PdfAnnotation stamps:stamp)
						pdfStamper.addAnnotation(stamps, p);	
				//}
			 pdfStamper.close();
		     document.close();  
		     pdfReader.close();
		     x++;
			}
	  
   
    }
    }catch(DocumentException ex){
    	System.out.println(" Error " + ex.getMessage());
    }

}
private static void setMessage(Logger LOGGER, String lvl,String msg){
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