package org.ndm.imageproc.recipes;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.ndm.imageproc.ImageProcessingRecipe;
import org.ndm.photogrammetry.CameraAttitude;
import org.ndm.photogrammetry.CameraIntrinsics;
import org.ndm.photogrammetry.CollinearityTransform;
import org.ndm.photogrammetry.CollinearityWarp;
import org.ndm.photogrammetry.FlatSurfaceModel;

/**
 * A recipe for orthorectifying an image - in reality this recipe could be used to 
 * model any image warp, not just an orthorectification.
 * @author nmcintyr
 *
 */
public class SimpleOrthoRecipe implements ImageProcessingRecipe{

	private String sourceImageFile = null;
	private CameraIntrinsics intrinsics = null;
	private CameraAttitude attitude = null;
	private AffineTransform targetGeoTransform = null;
	
	private Interpolation resamplingKernel = null;
	private Boolean addAlpha = Boolean.FALSE;
	
	@Override
	public RenderedOp getRecipeAsJaiRenderedOp() {
		
		// Read in the source image
		RenderedOp sourceImage = JAI.create("fileload", sourceImageFile);
		
		// Build the warp
		CollinearityTransform ct = new CollinearityTransform(intrinsics, attitude, (double) sourceImage.getWidth(), (double) sourceImage.getHeight());
		
		targetGeoTransform = computeOutputTransform(ct, sourceImage);
		
		
		/*
		// Add an alpha channel if requested and if there's not already one
		RenderedOp postAlpha = sourceImage;
		if(addAlpha){
			// If the image has 1 band or 3 bands, we should add an alpha channel
			if(sourceImage.getSampleModel().getNumBands() == 3 || sourceImage.getSampleModel().getNumBands() == 1){
				
			}
		}
		*/
		
		FlatSurfaceModel surfaceModel = new FlatSurfaceModel(0.0);
		CollinearityWarp warp = new CollinearityWarp(targetGeoTransform, surfaceModel, ct);
		
		// Warp it into the output image space
		ParameterBlock pbW = new ParameterBlock();
		pbW.addSource(sourceImage);
		pbW.add(warp);
		pbW.add(resamplingKernel);
		RenderedOp warped = JAI.create("warp", pbW);
		
		return warped;
	}
	
	private AffineTransform computeOutputTransform(CollinearityTransform ct, RenderedOp sourceImage){
		// Project Some points from the image to figure out ground sample distance (gsd) and the resulting image geo transform
		Point2D.Double ul = ct.imageToGround(new Point2D.Double(0.0, 0.0), 0.0);
		Point2D.Double ur = ct.imageToGround(new Point2D.Double(sourceImage.getWidth(), 0.0), 0.0);
		Point2D.Double lr = ct.imageToGround(new Point2D.Double(sourceImage.getWidth(), sourceImage.getHeight()), 0.0);
		Point2D.Double ll = ct.imageToGround(new Point2D.Double(0.0, sourceImage.getHeight()), 0.0);
		Rectangle2D.Double rect = new Rectangle2D.Double();
		rect.add(ul);
		rect.add(ur);
		rect.add(lr);
		rect.add(ll);
		Double translateX = rect.getMinX();
		Double translateY = rect.getMaxY();

		Point2D.Double ulPlusOone = ct.imageToGround(new Point2D.Double(1.0, 0.0), 0.0);
		Double scale = ul.distance(ulPlusOone);
		
		return new AffineTransform(scale, 0.0, 0.0, -scale, translateX, translateY);
	}

	public String getSourceImageFile() {
		return sourceImageFile;
	}

	public void setSourceImageFile(String sourceImageFile) {
		this.sourceImageFile = sourceImageFile;
	}

	public CameraIntrinsics getIntrinsics() {
		return intrinsics;
	}

	public void setIntrinsics(CameraIntrinsics intrinsics) {
		this.intrinsics = intrinsics;
	}

	public CameraAttitude getAttitude() {
		return attitude;
	}

	public void setAttitude(CameraAttitude attitude) {
		this.attitude = attitude;
	}

	public AffineTransform getTargetGeoTransform() {
		return targetGeoTransform;
	}

	public void setTargetGeoTransform(AffineTransform targetGeoTransform) {
		this.targetGeoTransform = targetGeoTransform;
	}

	public Interpolation getResamplingKernel() {
		return resamplingKernel;
	}

	public void setResamplingKernel(Interpolation resamplingKernel) {
		this.resamplingKernel = resamplingKernel;
	}

	public Boolean getAddAlpha() {
		return addAlpha;
	}

	public void setAddAlpha(Boolean addAlpha) {
		this.addAlpha = addAlpha;
	}

}
