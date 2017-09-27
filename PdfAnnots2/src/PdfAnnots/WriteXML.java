package PdfAnnots;

import static PdfAnnots.PdfAnnots.CLIENT_ID;
import static PdfAnnots.PdfAnnots.CLIENT_PRODUCT_ID;
import static PdfAnnots.PdfAnnots.ENVIRONMENT;
import static PdfAnnots.PdfAnnots.JOB_NUMBER;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author SKosta
 */
public class WriteXML {

    public WriteXML(String xmlout,ArrayList annotations) throws IOException{
    File file = new File(xmlout);
    String tempfile = file.getAbsolutePath();
    //FileWriter fw = new FileWriter(file.getAbsoluteFile());
    FileWriter fw = new FileWriter(tempfile);
    BufferedWriter bw = new BufferedWriter(fw);
    Iterator<Annotation> x = annotations.iterator();
    String xmlheader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String account = null;  
    int totaldocs = getDocCount(annotations);
       while (x.hasNext())
        {
            final Annotation annot = x.next();
            if(Integer.parseInt(annot.getPdfpage())==1){
            
                bw.write(xmlheader+"\n");
                bw.write("<Report>\n");
                bw.write("<Header>\n");
                bw.write("<ClientID>"+CLIENT_ID+"</ClientID>\n");
                bw.write("<ClientProductID>"+CLIENT_PRODUCT_ID+"</ClientProductID>\n");
                bw.write("<JobNumber>"+JOB_NUMBER+"</JobNumber>\n");
                bw.write("<Environment>"+ENVIRONMENT+"</Environment>\n");
                bw.write("<TotalDocuments>"+  totaldocs  +"</TotalDocuments>\n");
                bw.write("<TotalPages>"+  annotations.size()  +"</TotalPages>\n");
                bw.write("<DocumentKeyName>VSAccount</DocumentKeyName>\n");
                bw.write("<Indexes>\n");
                bw.write("<Index>AccountType</Index>\n");
                bw.write("<Index>VSAccount</Index>\n");
                bw.write("<Index>Name</Index>\n");
                bw.write("<Index>Date</Index>\n");
                bw.write("</Indexes>\n");
                bw.write("<Overlays>\n\t<Overlay>\n</Overlay>\n</Overlays>\n<Inserts>\n<Insert>\n</Insert>\n</Inserts>");
                bw.write("</Header>\n");
                
                bw.write("<Document>\n");
                 account = annot.getVsaccount();
                 bw.write(annot.toXML());
            }
            if(! account.equals(annot.getVsaccount())){
                bw.write("</Document>\n");
                bw.write("<Document>\n");
                account = annot.getVsaccount();
                bw.write(annot.toXML());
            }
            
        }   
        bw.write("</Document>\n");
        bw.write("</Report>");
        bw.close();
    }

private int getDocCount(ArrayList annotations) {
       int totaldocs = 0;
    Iterator<Annotation> x = annotations.iterator();
    String account = null;  
       while (x.hasNext())
        {
            final Annotation annot = x.next();
            if(Integer.parseInt(annot.getPdfpage())==1){
                totaldocs++;
                account = annot.getVsaccount();
            }
            if(! account.equals(annot.getVsaccount())){
                totaldocs++;
                account = annot.getVsaccount();
            }
        } 
       return totaldocs;
    }
    
}
