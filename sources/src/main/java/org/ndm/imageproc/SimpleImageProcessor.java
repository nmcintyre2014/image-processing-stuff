package org.ndm.imageproc;

import java.awt.image.Raster;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * The simplest implementation of an image processor.  Just let JAI do all the work and schedule compute the
 * way it wants to.
 * @author nmcintyr
 *
 */
public class SimpleImageProcessor implements ImageProcessor{

	public SimpleImageProcessor(){
		// Give JAI 3/4 of the available JVM memory
		JAI.getDefaultInstance().getTileCache().setMemoryCapacity(Runtime.getRuntime().maxMemory()*3/4);
	}
	
	@Override
	public void process(ImageProcessingRecipe recipe) {
		// This triggers JAI to "render" the whole image.  In JAI lingo, rendering is the act of computing image pixels.
		Raster raster = recipe.getRecipeAsJaiRenderedOp().getData();
	}

	@Override
	public void processAndStore(ImageProcessingRecipe recipe,String outputFileName) {
		// The "filestore" operation, being something that outputs imagery to a file, also triggers JAI's rendering chain to begin.
		RenderedOp image = recipe.getRecipeAsJaiRenderedOp();
		
		try {
			ImageIO.write(image, "tif", new File(outputFileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		/*
		ParameterBlock pbS = new ParameterBlock();
		pbS.addSource(image);
		pbS.add(outputFileName);
		JAI.create("filestore", outputFileName, image);
		*/
		
	}

	@Override
	public void shutdown() {
		// Noop
		
	}

}
