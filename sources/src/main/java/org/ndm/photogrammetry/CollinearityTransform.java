package org.ndm.photogrammetry;

import java.awt.geom.Point2D;

/**
 * A simple implementation of ground-to-image and image-to-ground transformations based on the collinearity condition.
 * @author nmcintyr
 *
 */
public class CollinearityTransform {

	private CameraIntrinsics cameraIntrinsics = null;
	private CameraAttitude exposureOrientation = null;
	
	public CollinearityTransform(){}
	
	public CollinearityTransform(CameraIntrinsics cameraIntrinsics, CameraAttitude exposureOrientation){
		this.cameraIntrinsics = cameraIntrinsics;
		this.exposureOrientation = exposureOrientation;
	}
	
	/**
	 * Given a point on the ground (object point) and it's height, compute the image point.
	 * @param groundPoint - a point on the ground
	 * @param objectHeight - the height of the point on the ground
	 * @return - the pixel location of the object in image space
	 */
	public Point2D.Double groundToImage(Point2D.Double groundPoint, Double objectHeight){
		
		// Implemented using the standard form (as opposed to the matrix form) to make testing/debugging 
		// easier.  Should re-implement in matrix form for simplicity/readability/performance.
		double r11 = exposureOrientation.getRotationMatrix().getEntry(0,0);
		double r12 = exposureOrientation.getRotationMatrix().getEntry(0,1);
		double r13 = exposureOrientation.getRotationMatrix().getEntry(0,2);
		double r21 = exposureOrientation.getRotationMatrix().getEntry(1,0);
		double r22 = exposureOrientation.getRotationMatrix().getEntry(1,1);
		double r23 = exposureOrientation.getRotationMatrix().getEntry(1,2);
		double r31 = exposureOrientation.getRotationMatrix().getEntry(2,0);
		double r32 = exposureOrientation.getRotationMatrix().getEntry(2,1);
		double r33 = exposureOrientation.getRotationMatrix().getEntry(2,2);
		
		double x0 = exposureOrientation.getCameraLocation().getEntry(0,0);
		double y0 = exposureOrientation.getCameraLocation().getEntry(1,0);
		double z0 = exposureOrientation.getCameraLocation().getEntry(2,0);
		
		double x = groundPoint.getX();
		double y = groundPoint.getY();
		double z = objectHeight;
		
		double den = ((x-x0)*r13)+((y-y0)*r23)+((z-z0)*r33);
		
		double xnum = ((x-x0)*r11)+((y-y0)*r21)+((z-z0)*r31);
		double ix = -cameraIntrinsics.getFocalLength()*(xnum/den);
		
		double ynum = ((x-x0)*r12)+((y-y0)*r22)+((z-z0)*r32);
		double iy = -cameraIntrinsics.getFocalLength()*(ynum/den);
		
		return new Point2D.Double(ix, iy);
	}
	
	/**
	 * Given 
	 * @param imagePoint
	 * @param objectHeight
	 * @return
	 */
	public Point2D.Double imageToGround(Point2D.Double imagePoint, Double objectHeight){
		
		// Implemented using the standard form (as opposed to the matrix form) to make testing/debugging 
		// easier.  Should re-implement in matrix form for simplicity/readability/performance.
		double r11 = exposureOrientation.getRotationMatrix().getEntry(0,0);
		double r12 = exposureOrientation.getRotationMatrix().getEntry(0,1);
		double r13 = exposureOrientation.getRotationMatrix().getEntry(0,2);
		double r21 = exposureOrientation.getRotationMatrix().getEntry(1,0);
		double r22 = exposureOrientation.getRotationMatrix().getEntry(1,1);
		double r23 = exposureOrientation.getRotationMatrix().getEntry(1,2);
		double r31 = exposureOrientation.getRotationMatrix().getEntry(2,0);
		double r32 = exposureOrientation.getRotationMatrix().getEntry(2,1);
		double r33 = exposureOrientation.getRotationMatrix().getEntry(2,2);

		double x0 = exposureOrientation.getCameraLocation().getEntry(0,0);
		double y0 = exposureOrientation.getCameraLocation().getEntry(1,0);
		double z0 = exposureOrientation.getCameraLocation().getEntry(2,0);

		double e0 = cameraIntrinsics.getPrinciplePoint().getX();
		double n0 = cameraIntrinsics.getPrinciplePoint().getY();
		double c = cameraIntrinsics.getFocalLength();
		
		double e = imagePoint.getX();
		double n = imagePoint.getY();
		double z = objectHeight;

		double den = ((e-e0)*r31)+((n-n0)*r32)-(c*r33);

		double xnum = ((e-e0)*r11)+((n-n0)*r12)-(c*r13);
		double gx = x0+(z-z0)*(xnum/den);

		double ynum = ((e-e0)*r21)+((n-n0)*r22)-(c*r23);
		double gy = y0+(z-z0)*(ynum/den);

		return new Point2D.Double(gx, gy);
		
	}

	public CameraIntrinsics getCameraIntrinsics() {
		return cameraIntrinsics;
	}

	public void setCameraIntrinsics(CameraIntrinsics cameraIntrinsics) {
		this.cameraIntrinsics = cameraIntrinsics;
	}

	public CameraAttitude getExposureOrientation() {
		return exposureOrientation;
	}

	public void setExposureOrientation(CameraAttitude exposureOrientation) {
		this.exposureOrientation = exposureOrientation;
	}
	
	/**
	 * Print out the center and corner of the image in WKT.  Useful for debugging camera and pose parameters.
	 */
	public void printProjectedImagePointsWKT(){
		
		// Project a point at the center of the image
		Point2D.Double groundPoint = imageToGround(new Point2D.Double(0.0, 0.0), 0.0);
		System.out.println("POINT("+groundPoint.x+" "+groundPoint.y+")");
		
		// Project a point at each (rough) corner of the image
		double right = cameraIntrinsics.getSensorWidth()/2.0;
		double top = cameraIntrinsics.getSensorHeight()/2.0;
		double left = -right;
		double bottom = -top;
		
		// Project the corners of the sensor down onto the ground
		Point2D.Double ctl = imageToGround(new Point2D.Double(left, top), 0.00);
		System.out.println("POINT("+ctl.x+" "+ctl.y+")");
		Point2D.Double ctr = imageToGround(new Point2D.Double(right, top), 0.00);
		System.out.println("POINT("+ctr.x+" "+ctr.y+")");
		Point2D.Double cbr = imageToGround(new Point2D.Double(right, bottom), 0.00);
		System.out.println("POINT("+cbr.x+" "+cbr.y+")");
		Point2D.Double cbl = imageToGround(new Point2D.Double(left, bottom), 0.00);
		System.out.println("POINT("+cbl.x+" "+cbl.y+")");
		
	}
	
	
}
