package org.ndm.photogrammetry;

import java.awt.geom.Point2D;

/**
 * Describes some very basic intrinsic characteristics of the camera.  
 * A more complete implementation would include, for example, lens distortion.
 * @author nmcintyr
 *
 */
public interface CameraIntrinsics {

	public Point2D.Double getPrinciplePoint();
	public Double getFocalLength();
	public Double getSensorWidth();
	public Double getSensorHeight();
	
}
