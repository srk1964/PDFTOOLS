package Readers;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.BufferedImageLuminanceSource;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.itextpdf.text.DocumentException;

public class readDataMatrix {

	public readDataMatrix() {}
	public static ArrayList<BufferedImage> imageArray = new ArrayList<>();
	public static String filePath;
	public static String fileName;
	
	public static void main(String[] args) throws NotFoundException, IOException, DocumentException {
		filePath = args[0];
		fileName = args[1];
		
		System.out.println("Process Directory = "+ filePath +"\nPDF File = "+fileName);
		try {
			    System.out.println("Extracting barcodes from " + fileName);
			    ProcessPDF ppdf = new ProcessPDF(filePath,fileName);
			    System.out.println("Reading barcodes");
				imageArray = ppdf.getImageArray();
				read2D();
				System.out.println(imageArray.size() +" barcodes processed");
			
		} catch (IOException e) {
			System.out.println("Main Error "+ e.getMessage());
		}

	}
	
	public static void read2D() throws FileNotFoundException, IOException, NotFoundException
			{
			   try{
		       File file = new File(filePath+fileName+"_barcodes.txt");
			   System.out.println("Writing barcodes to " + file.getName());
			   String tempfile = file.getAbsolutePath();
			   FileWriter fw = new FileWriter(tempfile);
			   BufferedWriter bw = new BufferedWriter(fw);
			   
		       HashMap<DecodeHintType, Object> decodeHintMap = new HashMap<DecodeHintType, Object>();
			   decodeHintMap.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
			   
	               for(BufferedImage barcodes:imageArray){
	            	   BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(barcodes)));
	            	   Result codeResult = null;
					try {
							if(decodeHintMap.keySet() != null){
							codeResult = new DataMatrixReader().decode(binaryBitmap, decodeHintMap);
							}
						} catch (ChecksumException | FormatException e) {
							System.out.println("read2D readining barcode Error "+ e.getMessage());
						}
			               if(codeResult != null){
			            	   bw.write(codeResult.getText()+"\n");
			               }
		           }
	               bw.close();
			   }catch (Exception e) {
					System.out.println("read2D Image Array for loop Error "+ e.getMessage());
				}
              
			}

}
