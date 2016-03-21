package org.ndm.imageproc;

import javax.media.jai.RenderedOp;

public interface ImageProcessingRecipe {

	public RenderedOp getRecipeAsJaiRenderedOp();
	
}
