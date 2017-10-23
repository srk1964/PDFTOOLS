package com.srk.pdfPieceInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ReadSideFile 
	{
		private static  ArrayList<String> keys = new ArrayList<String>();
		public ReadSideFile() {		
	}
	
	public ArrayList<String> setKeys(String sidefile) throws RuntimeException, IOException
	{
		try(BufferedReader br = new BufferedReader(new FileReader(sidefile))) 
	      {
			for(String line; (line = br.readLine()) != null; ) 
		    {
				keys.add(line);        
		    }
		    return keys;
	     }
	}

}