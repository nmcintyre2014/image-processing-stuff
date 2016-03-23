package org.ndm;

import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TfwUtil {

	public static AffineTransform atFromTfwLines(List<Double> lines){
		Double scaleX = lines.get(0);
		Double shearX = lines.get(1);
		Double shearY = lines.get(2);
		Double scaleY = lines.get(3);
		Double transX = lines.get(4);
		Double transY = lines.get(5);
	 
				
		AffineTransform xform = new AffineTransform(scaleX,shearY,shearX,scaleY,transX,transY);
		
		return xform;
	}
	
	public static AffineTransform atFromTfw(String tfwFile) throws IOException{
		File tfw = new File(tfwFile);
		return atFromTfw(tfw);
	}
	
	public static AffineTransform atFromTfw(File tfwFile) throws IOException{
		FileInputStream fis = new FileInputStream(tfwFile);
		 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		Double scaleX = new Double(br.readLine());
		Double shearX = new Double(br.readLine());
		Double shearY = new Double(br.readLine());
		Double scaleY = new Double(br.readLine());
		Double transX = new Double(br.readLine());
		Double transY = new Double(br.readLine());
	 
		br.close();
		//AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12)
		//scaleX,shearY,shearX,scaleY,tranX,tranY
		
		
		AffineTransform xform = new AffineTransform(scaleX,shearY,shearX,scaleY,transX,transY);
		
		return xform;
	}
	
	public static void writeTfwFile(String fileName, AffineTransform at) throws IOException{
		FileOutputStream fos = new FileOutputStream(new File(fileName));
		FileWriter writer = new FileWriter(new File(fileName));
		writer.write(""+at.getScaleX());
		writer.write(System.lineSeparator());
		writer.write(""+at.getShearX());
		writer.write(System.lineSeparator());
		writer.write(""+at.getShearY());
		writer.write(System.lineSeparator());
		writer.write(""+at.getScaleY());
		writer.write(System.lineSeparator());
		writer.write(""+at.getTranslateX());
		writer.write(System.lineSeparator());
		writer.write(""+at.getTranslateY());
		writer.write(System.lineSeparator());
		writer.close();
	}
}
