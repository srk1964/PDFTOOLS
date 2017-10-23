package itextpieceinfo;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.srk.pdfPieceInfo.Messenger;

public class DocumentPieceInfo {
    static PdfName PIECE_INFO = new PdfName("PieceInfo");
    static PdfName LAST_MODIFIED = new PdfName("LastModified");
    static PdfName PRIVATE = new PdfName("Private");
    
    public DocumentPieceInfo(String processdir, ArrayList<String> keys, Messenger messenger )  
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
	 String current_file = null;
	
	 String key;
	 String value;
     int n = 0 ;
     int line = 0;
 for (String keyrecs : keys){
		try {
	 	String[] splitter = keyrecs.split("\\|");
	 	dict = new ArrayList<>();
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
					//copy = new PdfCopy(document,new FileOutputStream(processdir+pdfout));
					document.open();
					pdfStamper = new PdfStamper(pdfReader,new FileOutputStream(processdir+pdfout));
					//get number of pages
						n = pdfReader.getNumberOfPages();	
				    }if(k==0 && ! splitter[k].contains("FILE")){
				    	messenger.setMessage("warning","no file name passed first key value must be File=filename "+ splitter[k]+" record number "+line+"\n");
				    	current_file = null;
				    }else if(k>0 && current_file!=null){
				    	if(splitter[k].split("=").length==2) {
				    	key = splitter[k].split("=")[0];
				    	value = splitter[k].split("=")[1];
				    	pieceInfo pinfo = new pieceInfo();
				    	pinfo.setKey(key);
				    	pinfo.setValue(value);
						dict.add(pinfo);
				    	}else {
				    		messenger.setMessage("warning","invalid key value pair ("+ splitter[k]+ ") " + current_file +" \n");
				    		key = splitter[k];
					    	value = "";
					    	pieceInfo pinfo = new pieceInfo();
					    	pinfo.setKey(key);
					    	pinfo.setValue(value);
							dict.add(pinfo);
		
				    	}
				    }
				    
		 	}
				if (current_file!=null) 
				{
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
			      
				}
					pdfStamper.close();
					document.close(); 
				    pdfReader.close();
				}
	
	
		  
	 }catch(DocumentException | IOException ex){
	    messenger.setMessage("warning","PDF Error "+ current_file +" " + ex.getMessage()+"\n");
	    continue;
	 }
 }

}

} 