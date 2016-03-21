package org.ndm.photogrammetry;

/**
 * A surface model that returns a set value for every location.  Useful for testing or
 * just projecting an image onto its rough location on the ground.
 * @author nmcintyr
 *
 */
public class FlatSurfaceModel implements SurfaceModel{
	
	private Double fixedHeight = null;
	
	public FlatSurfaceModel(){}
	
	public FlatSurfaceModel(Double fixedHeight){
		this.fixedHeight = fixedHeight;
	}

	@Override
	public Double getHeightForLocation(java.awt.geom.Point2D.Double location) {
		return fixedHeight;
	}

	public Double getFixedHeight() {
		return fixedHeight;
	}

	public void setFixedHeight(Double fixedHeight) {
		this.fixedHeight = fixedHeight;
	}
	
}
