package org.ndm.photogrammetry;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.media.jai.Warp;

/**
 * A JAI warp based on a CollinearityTransform.  Used to warp a raw camera image onto 
 * onto a surface model using a camera model and attitude information.
 * @author nmcintyr
 *
 */
public class CollinearityWarp extends Warp {

	private AffineTransform destPixelToWorldTransform = null;
	private SurfaceModel surfaceModel = null;
	private CollinearityTransform transform = null;
	
	public CollinearityWarp(){}
	
	public CollinearityWarp(AffineTransform destPixelToWorldTransform,
			SurfaceModel surfaceModel, CollinearityTransform transform) {
		super();
		this.destPixelToWorldTransform = destPixelToWorldTransform;
		this.surfaceModel = surfaceModel;
		this.transform = transform;
	}
	
	@Override
	public float[] warpSparseRect(int x, int y, int width, int height,int periodX, int periodY, float[] destRect) {
		
		if (destRect == null) {
			destRect = new
					float[((width + periodX - 1) / periodX) *
					      ((height + periodY - 1) / periodY) * 2];
		}

		width += x;
		height += y;
		int index = 0;

		for (int j = y; j < height; j += periodY) {
			for (int i = x; i < width; i += periodX) {

				double sample = i+0.5;
				double line = j+0.5;

				// Get the location of the destination pixel in pixel space
				Point2D.Double destPixelLocation = new Point2D.Double(sample,line);
				
				// Convert it to location in the world space of the output image
				Point2D.Double worldLocation = new Point2D.Double();
				destPixelToWorldTransform.transform(destPixelLocation, worldLocation);
				
				// Get the height of the surface at the pixel location.
				Double surfaceHeight = surfaceModel.getHeightForLocation(worldLocation);
				
				// Map the world location back to a pixel location in the source image.
				Point2D.Double sourcePixelLocation = transform.groundToImage(worldLocation, surfaceHeight);
				
				destRect[index++] = (float) sourcePixelLocation.x;
				destRect[index++] = (float) sourcePixelLocation.y;                 
			}
		}
		
		return destRect;
	}

	public AffineTransform getDestPixelToWorldTransform() {
		return destPixelToWorldTransform;
	}

	public void setDestPixelToWorldTransform(
			AffineTransform destPixelToWorldTransform) {
		this.destPixelToWorldTransform = destPixelToWorldTransform;
	}

	public SurfaceModel getSurfaceModel() {
		return surfaceModel;
	}

	public void setSurfaceModel(SurfaceModel surfaceModel) {
		this.surfaceModel = surfaceModel;
	}

	public CollinearityTransform getTransform() {
		return transform;
	}

	public void setTransform(CollinearityTransform transform) {
		this.transform = transform;
	}
	
	

}
