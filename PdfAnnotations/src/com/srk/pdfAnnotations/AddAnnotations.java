package com.srk.pdfAnnotations;

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

public AddAnnotations(String processdir, ArrayList<String> keys, Messenger messenger)  
 {
    try
    {
        Split(processdir, keys, messenger);
    }catch(Exception e)
    {
    	messenger.setMessage("severe","Error.."+ e.getMessage()+"\n");
    }    
 }
private static void Split(String processdir,ArrayList<String> keys, Messenger messenger)
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
	    String current_file = null;
	    int line = 0;
    	int x = 0; 
		    for (String keyrecs : keys){
		    	try {
		    	String[] splitter = keyrecs.split("\\|");
		    	stamp = new ArrayList<>();
		         lx = 5;
		         ux = 6;
		         line++;
		    	for(int k =0;k<splitter.length;k++){
		    		//first key has to be file so that we can open the PDF
		    		//subsequent keys are parsed into an array of PDF Annotations
				    if(k==0 && splitter[k].contains("FILE")){
				    	current_file = splitter[k];
				    	messenger.setMessage("info","processing  "+ current_file +"\n");
					document = new Document();
					//use file name to open reader
					pdfReader = new PdfReader(processdir+splitter[k].split("=")[1]);
					//use file name to create output file name for stamper
				    pdfout = String.format("%s_out.pdf",splitter[k].split("=")[1]);
					document.open();
					pdfStamper = new PdfStamper(pdfReader,new FileOutputStream(processdir+pdfout));
					//get number of pages
					n = pdfReader.getNumberOfPages();
				    }if(k==0 && ! splitter[k].contains("FILE")){
				    	messenger.setMessage("warning","no file name passed first key value must be File=filename "+ splitter[k]+" record number "+line+"\n");
				    	current_file = null;
				    }else if(k>0 && current_file!=null){
				    	if(splitter[k].split("=").length==2)
				    	{
					    	//add new annotations while incrementing the rectangle coordinates for lx ux
					    	key = splitter[k].split("=")[0];
					    	value = splitter[k].split("=")[1];
							stamp.add(PdfAnnotation.createText(pdfStamper.getWriter(), new Rectangle(lx, ly, ux, uy),null,"A::PDF(T)\nK("+key+")\nV("+value+")",false, null));
							//increment for next rectangle
							lx = lx + 5;
							ux = ux + 5;
				    	}else {
				    		messenger.setMessage("warning","invalid key value pair ("+ splitter[k]+ ") " + current_file +" \n");
					    	key = splitter[k];
					    	value = "";
							stamp.add(PdfAnnotation.createText(pdfStamper.getWriter(), new Rectangle(lx, ly, ux, uy),null,"A::PDF(T)\nK("+key+")\nV("+value+")",false, null));
							//increment for next rectangle
							lx = lx + 5;
							ux = ux + 5;
				    	}
				    }
				    
		    	}
		    	if (current_file!=null) 
				{
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

		        }catch(DocumentException | IOException ex){
		        	messenger.setMessage("warning","PDF Error "+ current_file +" " + ex.getMessage()+"\n");
		    	    continue;
		        }
	    }
	
	
	}
}