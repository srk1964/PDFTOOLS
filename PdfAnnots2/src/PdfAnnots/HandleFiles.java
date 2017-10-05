package PdfAnnots;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class HandleFiles {
public HandleFiles(String zipFilePath,String zipfile,String destDir) throws IOException{
	
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(dir.exists()) {
        	System.out.println("Cleaning temp directory "+ destDir);
        	for(File f: dir.listFiles()) 
  			  f.delete();
        	
        	}else {
        		dir.mkdirs();
        	}
 
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        
        try {
            fis = new FileInputStream(zipFilePath+zipfile);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                //System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            throw e;
        }
         
    }    
    
}     

