package Readers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public final class ProcessPDF {
	public ArrayList<BufferedImage> imageArray = new ArrayList<>();

	public final ArrayList<BufferedImage> getImageArray() {
		return imageArray;
	}

public ProcessPDF( String ProcessDir,String pdfin) throws IOException, DocumentException 
 {
     try
     {
    	 PdfReader reader = new PdfReader(ProcessDir+pdfin);
    	 PdfReaderContentParser parser = new PdfReaderContentParser(reader);
    	 ImageRenderListener listener = new ImageRenderListener(ProcessDir,pdfin);

        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            	parser.processContent(i, listener);
            }

            reader.close();
        }catch(Exception ex)
          {
        	System.out.println("Error "+ ex.getMessage());
          }

}

class ImageRenderListener implements RenderListener
{
    final String name;
    final String path;
    int counter = 1;

    public ImageRenderListener(String path, String name)
    {
        this.name = name;
        this.path= path;
    }

    public void beginTextBlock() { }
    public void renderText(TextRenderInfo renderInfo) { }
    public void endTextBlock() { }

    public void renderImage(ImageRenderInfo renderInfo)
    {
        try
        {
        	PdfImageObject image = renderInfo.getImage();
            if (image == null) return;

            if(renderInfo.getRef() == null){
            	imageArray.add(image.getBufferedImage());
            }

            PdfDictionary imageDictionary = image.getDictionary();
            PRStream maskStream = (PRStream) imageDictionary.getAsStream(PdfName.SMASK);
            if (maskStream != null)
            {
                PdfImageObject maskImage = new PdfImageObject(maskStream);
                imageArray.add(maskImage.getBufferedImage());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
}


