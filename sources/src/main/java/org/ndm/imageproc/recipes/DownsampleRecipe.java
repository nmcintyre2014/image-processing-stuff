package org.ndm.imageproc.recipes;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.ndm.imageproc.ImageProcessingRecipe;

/**
 * This is a very simple example of an image processing recipe.  This recipe reads in an image
 * on the local file system and creates a 2x down-sampled copy of the image.
 * @author nmcintyr
 *
 */
public class DownsampleRecipe implements ImageProcessingRecipe{

	private String inputImage = null;
	
	public DownsampleRecipe(String inputImage){
		this.inputImage = inputImage;
	}
	
	@Override
	public RenderedOp getRecipeAsJaiRenderedOp() {
		
		// Node in the symbolic rendering graph to load an image.
		RenderedOp sourceImage = JAI.create("fileload", inputImage);
		
		// Node in the symbolic rendering graph to downsample an image by a factor of 2
		ParameterBlock pbDs = new ParameterBlock();
		pbDs.addSource(sourceImage);
		pbDs.add(0.5f);
		pbDs.add(0.5f);
		RenderedOp downsampled = JAI.create("subsampleaverage", pbDs);
		
		return downsampled;
	}

}
