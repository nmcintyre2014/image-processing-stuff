package org.ndm;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.ndm.imageproc.ImageProcessor;
import org.ndm.imageproc.SimpleImageProcessor;
import org.ndm.imageproc.ThreaddedImageProcessor;
import org.ndm.imageproc.recipes.SimpleOrthoRecipe;
import org.ndm.photogrammetry.BasicCameraAttitudeImpl;
import org.ndm.photogrammetry.CameraAttitude;
import org.ndm.photogrammetry.CameraIntrinsics;
import org.ndm.photogrammetry.CameraIntrinsicsImpl;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Orthorectifier {
	
	private static final Double metersPerDegreeEquator = 111319.9;
	
	@Parameter(names = "-if", description = "Full path to CSV file with camaera attitude.", required = true)
	private String csvFile = null;
	
	@Parameter(names = "-id", description = "The image id (from attitude file) to process.", required = true)
	private String imageId = null;
	
	@Parameter(names = "-r", description = "Resmpling kernel (one of: Nearest, Bilinear, Cubic)")
	private String resamplingKernel = "Bilinear";
	
	@Parameter(names = "-of", description = "The name of the output image file.", required = true)
	private String outputFile = null;
	
	@Parameter(names = "-nt", description = "Number of processing threads.  Defaults to number of available processor cores." )
	private Integer numThreads = Runtime.getRuntime().availableProcessors();
	
	@Parameter(names = "-a", description = "Add alpha transparency to image." )
	private Boolean addAlpha = Boolean.TRUE;
	
	@Parameter(names = "-f", description = "Lens focal lenth (mm).")
	private Double focalLength = 55.0;
	
	@Parameter(names = "-sw", description = "Sensor width (mm).")
	private Double sensorWidth = 23.4;
	
	@Parameter(names = "-sh", description = "Sensor height (mm).")
	private Double sensorHeight = 15.6;
	
	public static void main(String[] args) throws IOException{
		
		Orthorectifier orthorectifier = new Orthorectifier();
		
		try {
			parseCommandLine(args, orthorectifier);
		} catch (Exception e) {
			System.exit(9);
		}
		
		orthorectifier.go();
		
	}
	
	public static void parseCommandLine(String[] args, Orthorectifier orthorectifier) throws Exception{
		JCommander jc = new JCommander(orthorectifier);
		try {
			jc.parse(args);
		} catch (Exception e){
			System.err.println(e.getMessage());	
			System.err.println();
			jc.usage();
			throw new Exception("Shutting down.");
		}
	}
	
	public void go() throws IOException{
		File inputCsvFile = new File(csvFile);
		FileReader reader = new FileReader(inputCsvFile);
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
		
		for(CSVRecord record : csvParser.getRecords()){
			if(imageId.equals(record.get(0))){
				
				Path path = Paths.get(csvFile);
				String imageFile = path.getParent().toAbsolutePath().toString()+"/"+imageId;
				//System.out.println(imageFile);
				
				Double longitude = Double.parseDouble(record.get(1));
				Double latitude = Double.parseDouble(record.get(2));
				Double height = Double.parseDouble(record.get(3));
				Double q0 = Double.parseDouble(record.get(4));
				Double q1 = Double.parseDouble(record.get(5));
				Double q2 = Double.parseDouble(record.get(6));
				Double q3 = Double.parseDouble(record.get(7));
				
				Interpolation interp = new InterpolationNearest();
				if("Nearest".equals(resamplingKernel)){
					interp = new InterpolationNearest();
				} else if("Bilinear".equals(resamplingKernel)){
					interp = new InterpolationBilinear();
				} else if("Cubic".equals(resamplingKernel)){
					interp = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
				}
				
				Quaternion quaternion = new Quaternion(q0, q1, q2, q3);
				Double metersPerDegree = Math.cos(latitude)*metersPerDegreeEquator;
				RealMatrix cameraLocation = new Array2DRowRealMatrix(new double[]{longitude, latitude, height/metersPerDegree});
				CameraAttitude attitude = new BasicCameraAttitudeImpl(cameraLocation, quaternion);
				CameraIntrinsics intrinsics = new CameraIntrinsicsImpl(new Point2D.Double(0.0, 0.0), focalLength, sensorWidth, sensorHeight);
				
				SimpleOrthoRecipe recipe = new SimpleOrthoRecipe();
				recipe.setAddAlpha(addAlpha);
				recipe.setAttitude(attitude);
				recipe.setIntrinsics(intrinsics);
				recipe.setResamplingKernel(interp);
				recipe.setSourceImageFile(imageFile);
				
				ImageProcessor processor = new ThreaddedImageProcessor(numThreads);
				processor.processAndStore(recipe, outputFile);
				
				String tfwFile = outputFile.replace(".tif", ".tfw");
				TfwUtil.writeTfwFile(tfwFile, recipe.getTargetGeoTransform());
				
				processor.shutdown();
			}
		}
	}
}
