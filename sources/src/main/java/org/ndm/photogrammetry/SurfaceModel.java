package org.ndm.photogrammetry;

import java.awt.geom.Point2D;

/**
 * A simple interface that returns a height at a given location.
 * @author nmcintyr
 *
 */
public interface SurfaceModel {
	
	public Double getHeightForLocation(Point2D.Double location);
	
}
