package itextpieceinfo;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

public class DocumentPieceInfo {
    static PdfName PIECE_INFO = new PdfName("PieceInfo");
    static PdfName LAST_MODIFIED = new PdfName("LastModified");
    static PdfName PRIVATE = new PdfName("Private");
    
    public DocumentPieceInfo(String processdir, ArrayList<String> keys, Logger LOGGER)  
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
	
	 PdfName PInfo = new PdfName("PieceInfo"); 
	 PdfName Default = new PdfName("Default");
	 PdfName LM = new PdfName("LastModified");
	 PdfName PRIVATE = new PdfName("Private");
	 PdfStamper pdfStamper = null;
//	 PdfImportedPage importedPage;
 String pdfout = null;
// PdfCopy copy = null;
 ArrayList<pieceInfo> dict;
 Document document = null;
 PdfReader pdfReader = null;

 String key;
 String value;
 try{  
 	int n = 0 ;
 for (String keyrecs : keys){
 	String[] splitter = keyrecs.split("\\|");
 	dict = new ArrayList<>();

 	for(int k =0;k<splitter.length;k++){
 		//first key has to be file so that we can open the PDF
 		//subsequent keys are parsed into an array of PDF Annotations
		    if(k==0 && splitter[k].contains("FILE")){
			document = new Document();
			//use file name to open reader
			pdfReader = new PdfReader(processdir+splitter[k].split("=")[1]);
			//use file name to create output file name for stamper
		    pdfout = String.format("%s_out.pdf",splitter[k].split("=")[1]);
			//copy = new PdfCopy(document,new FileOutputStream(processdir+pdfout));
			document.open();
			pdfStamper = new PdfStamper(pdfReader,new FileOutputStream(processdir+pdfout));
			//document.open();
			//get number of pages
			n = pdfReader.getNumberOfPages();
		    }else if(k>0){
		    	//add new annotations while incrementing the rectangle coordinates for lx ux
		    	key = splitter[k].split("=")[0];
		    	value = splitter[k].split("=")[1];
		    	pieceInfo pinfo = new pieceInfo();
		    	pinfo.setKey(key);
		    	pinfo.setValue(value);
				dict.add(pinfo);
		    }
		    
 	}
			for(int p = 1;p<(n+1);p++)
			{
				//PieceInfo top level
	            PdfDictionary catalog = new PdfDictionary();
	            catalog = pdfReader.getPageN(p);
	            PdfDictionary PiecepageDict = catalog.getAsDict(PInfo);
	            PiecepageDict = new PdfDictionary();
	            catalog.put(PInfo, PiecepageDict);
	          
	            //add default dictionary
	            PdfDictionary appData = PiecepageDict.getAsDict(Default);
	            appData = pdfReader.getPageN(p);
	            appData = new PdfDictionary();
	            PiecepageDict.put(Default, appData);
	            appData.put(LM, new PdfDate());
	            
	         // add private library
	            PdfDictionary privatepageDict = appData.getAsDict(PRIVATE);
	            privatepageDict = pdfReader.getPageN(p);
	            privatepageDict = new PdfDictionary();
	            appData.put(PRIVATE, privatepageDict);
            
					for(pieceInfo dicts:dict) 
					{
						privatepageDict.put( new PdfName(dicts.getKey()), new PdfString(dicts.getValue()));
					}
						
					//importedPage = copy.getImportedPage(pdfReader, p);
		            //copy.addPage(importedPage);
		      
			}
			 pdfStamper.close();
			 document.close(); 
		     pdfReader.close();
	  

 }
 }catch(DocumentException ex){
 	System.out.println(" Error " + ex.getMessage());
 }

}

} 