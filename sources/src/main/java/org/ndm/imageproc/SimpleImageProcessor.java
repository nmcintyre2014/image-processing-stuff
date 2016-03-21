package org.ndm.imageproc;

import java.awt.image.Raster;

import javax.media.jai.JAI;

/**
 * The simplest implementation of an image processor.  Just let JAI do all the work and schedule compute the
 * way it wants to.
 * @author nmcintyr
 *
 */
public class SimpleImageProcessor implements ImageProcessor{

	@Override
	public void process(ImageProcessingRecipe recipe) {
		// This triggers JAI to "render" the whole image.  In JAI lingo, rendering is the act of computing image pixels.
		Raster raster = recipe.getRecipeAsJaiRenderedOp().getData();
	}

	@Override
	public void processAndStore(ImageProcessingRecipe recipe,String outputFileName) {
		// The "filestore" operation, being something that outputs, also triggers JAI's rendering chain to begin.
		JAI.create("filestore", outputFileName, recipe.getRecipeAsJaiRenderedOp());
		
	}

}
