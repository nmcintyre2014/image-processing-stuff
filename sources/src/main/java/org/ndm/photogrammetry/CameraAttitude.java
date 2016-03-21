package org.ndm.photogrammetry;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Describes the orientation of the exposure in reference to the object (ground) coordinate system.
 * @author nmcintyr
 *
 */
public interface CameraAttitude {

	/** Get the column matrix representing the camera location
	 * 
	 * @return - the camera location
	 */
	public RealMatrix getCameraLocation();
	
	/**
	 * Get the sensor rotation matrix
	 * @return
	 */
	public RealMatrix getRotationMatrix();
	
}
