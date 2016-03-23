package org.ndm.photogrammetry;

import static org.junit.Assert.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class TestProjection {

	// Camera intrisics (same for both images)
	private CameraIntrinsics cameraIntrinsics = new CameraIntrinsicsImpl(new Point2D.Double(0.0,0.0), 55.00, 23.4, 15.6);
	
	// Camera location and post for first image
	private CameraAttitude eo1 = null;
	
	// Camera location and pose for second image
	private CameraAttitude eo2 = null;
	
	// Top-left corner of image in sensor space
	private Point2D.Double ctl = new Point2D.Double(-cameraIntrinsics.getSensorWidth()/2.0, cameraIntrinsics.getSensorHeight()/2.0);
	
	@Before
	public void setUp(){
		Quaternion q1 = new Quaternion(0.797491947,0.062671364,-0.027469146,0.599411253);
		RealMatrix cameraLocation = new Array2DRowRealMatrix(new double[]{-122.407555, 37.771863, 58.22620021/111000.0});
		eo1 = new BasicCameraAttitudeImpl(cameraLocation, q1);
		
		Quaternion q2 = new Quaternion(0.705722868,0.002313139,0.0046325279,0.708469152);
		RealMatrix cameraLocation2 = new Array2DRowRealMatrix(new double[]{-122.407555, 37.771863, 60.00804901/111000.0});
		eo2 = new BasicCameraAttitudeImpl(cameraLocation2, q2);
	}
	
	@Test
	public void testImageToGroundWithQuaternion(){
		
		// Image 1 7952x5304
		CollinearityTransform ct1 = new CollinearityTransform(cameraIntrinsics, eo1, 7952.0, 5304.0);
		
		ct1.printProjectedImagePointsWKT();
		
		Point2D.Double groundPoint = ct1.imageToGround(new Point2D.Double(3967.0, 2652.0), 0.0);
		//Point2D.Double groundPoint = ct1.imageToGround(new Point2D.Double(0.0, 0.0), 0.0);
		Point2D.Double centerTruth = new Point2D.Double(-122.40757158451548, 37.77193337018862);
		assertEquals(0.00, groundPoint.distance(centerTruth), 0.000001);
		
		Point2D.Double tlTruth = new Point2D.Double(-122.40767328090084, 37.77184686068321);
		Point2D.Double tl = ct1.imageToGround(ctl, 0.0);
		assertEquals(0.00, tl.distance(tlTruth), 0.000001);
		
		// Image 2 6000x4000
		CollinearityTransform ct2 = new CollinearityTransform(cameraIntrinsics, eo2, 6000.0, 4000.0);
		Point2D.Double groundPoint2 = ct2.imageToGround(new Point2D.Double(3000.0, 2000.0), 0.0);
		Point2D.Double centerTruth2 = new Point2D.Double(-122.40756030701576, 37.771861216348555);
		assertEquals(0.00, groundPoint2.distance(centerTruth2), 0.00000001);
		
		Point2D.Double tlTruth2 = new Point2D.Double(-122.4076366976499, 37.77174566702314);
		Point2D.Double tl2 = ct2.imageToGround(new Point2D.Double(0.0, 0.0), 0.0);
		assertEquals(0.00, tl2.distance(tlTruth2), 0.0000000001);
				
	}
	
	@Test
	public void testGroundToImageWithQuaternion(){
		// Image 1
		// Center point
		CollinearityTransform ct1 = new CollinearityTransform(cameraIntrinsics, eo1, 7952.0, 5304.0);
		Point2D.Double imagePoint = ct1.groundToImage(new Point2D.Double(-122.40757158451548, 37.77193337018862), 0.0);
		assertEquals(0.00, imagePoint.distance(new Point2D.Double(3976.0, 2652.0)), 0.0000001);
				
		// Top-left corner
		Point2D.Double tl = ct1.groundToImage(new Point2D.Double(-122.40767328090084, 37.77184686068321), 0.0);
		assertEquals(0.00, tl.distance(new Point2D.Double(0.0,0.0)), 0.00001);

	}
	
	@Test
	public void testCameraToImage() throws NoninvertibleTransformException{
		CollinearityTransform ct1 = new CollinearityTransform(cameraIntrinsics, eo1, 7952.0, 5304.0);
		Point2D.Double cameraCenter = (Double) ct1.getCameraToImageTransform().inverseTransform(new Point2D.Double(3976.0, 2652.0),null);
		assertEquals(0.00, cameraCenter.distance(new Point2D.Double(0.0,0.0)), 0.0000000001);
		
		Point2D.Double imageCenter = (Double) ct1.getCameraToImageTransform().transform(new Point2D.Double(0.0, 0.0),null);
		assertEquals(0.00, imageCenter.distance(new Point2D.Double(3976.0, 2652.0)), 0.00000001);
		
	}
}
