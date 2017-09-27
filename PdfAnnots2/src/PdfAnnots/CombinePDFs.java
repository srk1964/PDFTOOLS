package PdfAnnots;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopy.PageStamp;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinePDFs {
private String status = null;
public static void CombinePDFs( String processdir,String workingdir,String pdfout) throws IOException, DocumentException 
 {
    try
    {
        File f = new File(workingdir);
        File[] list = f.listFiles();
        ArrayList<File> pdfs = new ArrayList<>(Arrays.asList(list));
        OutputStream mergedpdf = new FileOutputStream(new File(processdir+pdfout));
        combinePdfs(pdfs, mergedpdf);
    }catch(Exception e)
    {
      System.out.println("Error happened " + e.getMessage());
    }    
 }
private static void combinePdfs(List<File> list, OutputStream outputStream) throws DocumentException, IOException
        {
            Document document = new Document();
        PdfCopy copy = new PdfCopy(document, outputStream);
        PageStamp stamp;
        document.open();
        int n;
        int pageNo = 0;
        PdfImportedPage page;
        Chunk chunk;
        for (File inputStream : list){
            PdfReader pdfReader = new PdfReader(inputStream.getAbsolutePath());
            //System.out.println("Combining " + inputStream.getAbsolutePath());
            n = pdfReader.getNumberOfPages();
            for (int i = 0; i < n; ) {
                pageNo++;
                page = copy.getImportedPage(pdfReader, ++i);
                stamp = copy.createPageStamp(page);
                chunk = new Chunk(String.format("Page %d", pageNo));
                if (i == 1)
                    chunk.setLocalDestination("p" + pageNo);
                ColumnText.showTextAligned(stamp.getUnderContent(),
                        Element.ALIGN_RIGHT, new Phrase(chunk),
                        559, 810, 0);
                stamp.alterContents();
                copy.addPage(page);
            }
                    pdfReader.close();
        }
        document.close();
        }

public void setStatus(String status){
        this.status = status;
    }
public String getStatus(){
        return status;
    }
}
