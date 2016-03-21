package org.ndm.photogrammetry;

import static org.junit.Assert.assertEquals;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Before;
import org.junit.Test;

public class TestCollinearityTransform {

	// First test point
	private Point2D.Double groundPoint1 = new Point2D.Double(363552.124, 61488.048);
	private Double groundHeight1 = 588.079;
	private Point2D.Double imagePoint1 = new Point2D.Double(-33.288, 110.074);
	
	// Second test point
	private Point2D.Double groundPoint2 = new Point2D.Double(362571.087, 61198.320);
	private Double groundHeight2 = 596.670;
	private Point2D.Double imagePoint2 = new Point2D.Double(1.628, 5.182);
	
	// Camera Intrinsics
	private CameraIntrinsics cameraIntrinsics = new CameraIntrinsicsImpl(new Point2D.Double(0.0,0.0), 152.67, -9999.0, -9999.0);
	private CameraAttitude exposure = null;
	
	private CollinearityTransform unit = null;
	
	@Before
	public void setUp(){
		
		double[][] rMatArray = new double[3][3];
		rMatArray[0] = new double[]{-0.034091, 0.999407, 0.004822};
		rMatArray[1] = new double[]{-0.999419, -0.034096, 0.000621};
		rMatArray[2] = new double[]{0.000784, -0.004798, 0.999988};
		Array2DRowRealMatrix rotationMatrix = new Array2DRowRealMatrix(rMatArray);
		
		Array2DRowRealMatrix cameraLocation = new Array2DRowRealMatrix(new double[]{362530.603, 61215.834, 2005.742});
		exposure = new BasicCameraAttitudeImpl(cameraLocation, rotationMatrix);
		
		unit = new CollinearityTransform();
		unit.setCameraIntrinsics(cameraIntrinsics);
		unit.setExposureOrientation(exposure);
		unit.setCameraToImageTransform(new AffineTransform());
	}
	
	@Test
	public void testGroundToImage1(){
		
		// First test point
		Point2D.Double imagePoint = unit.groundToImage(groundPoint1, groundHeight1);
		assertEquals(imagePoint1.getX(), imagePoint.getX(), 0.001);
		assertEquals(imagePoint1.getY(), imagePoint.getY(), 0.001);
		
		// Second test point
		imagePoint = unit.groundToImage(groundPoint2, groundHeight2);
		assertEquals(imagePoint2.getX(), imagePoint.getX(), 0.001);
		assertEquals(imagePoint2.getY(), imagePoint.getY(), 0.001);
		
	}
	
	@Test
	public void testImageToGround(){
		
		// First test point
		Point2D.Double groundPoint = unit.imageToGround(imagePoint1, groundHeight1);
		assertEquals(groundPoint1.getX(), groundPoint.getX(), 0.01);
		assertEquals(groundPoint1.getY(), groundPoint.getY(), 0.01);
		
		// Second test point
		groundPoint = unit.imageToGround(imagePoint2, groundHeight2);
		assertEquals(groundPoint2.getX(), groundPoint.getX(), 0.01);
		assertEquals(groundPoint2.getY(), groundPoint.getY(), 0.01);
		
	}

}
