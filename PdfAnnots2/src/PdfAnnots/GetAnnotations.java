package PdfAnnots;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import java.io.IOException;
import java.util.ArrayList;

public final class GetAnnotations {
private String status = null;
public ArrayList<Annotation> annotations = new ArrayList();
public GetAnnotations( String ProcessDir,String pdfin) throws IOException, DocumentException 
 {
    try
    {
        PdfReader reader = new PdfReader(ProcessDir + pdfin); 
        Document document = new Document();  
        document.open();
        int n;

         PdfDictionary pageDict = null;
         PdfArray annotArray = null;
        //get number of pages in PDF for the for loop
            n = reader.getNumberOfPages();
            //ArrayList<Annotation> annotations = new ArrayList();
            String line = null;
            for (int p = 1; p <= n; p++) 
                {
                        pageDict = reader.getPageN(p);
                        annotArray = pageDict.getAsArray(PdfName.ANNOTS);
                        Annotation annot = new Annotation();
                        for (int i = 0; i < annotArray.size(); ++i){
                            PdfDictionary curAnnot = annotArray.getAsDict(i);
                            PdfString name = curAnnot.getAsString(PdfName.T);
                            PdfString contents = curAnnot.getAsString(PdfName.CONTENTS);
                            
                            if (! contents.equals(null))
                            { 
                                //System.out.println("PdfPage="+p+","+contents.toString().trim()); 
                                annot.Annotation("PdfPage="+p+","+contents.toString().trim());
                                annotations.add(annot);
                                //System.out.println(annot.toString());
                                //System.out.println("Array Count="+annotations.size()); 
                            }
                        }

                }
               document.close();
               reader.close();
               setStatus("ok");
            }catch(Exception ex)
              {
                setStatus(ex.getMessage());
              }

    
 }
public void setStatus(String status){
        this.status = status;
    }
public String getStatus(){
        return status;
    }
}
