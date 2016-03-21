package org.ndm.imageproc;

/**
 * An interface for classes that can schedule computation for images given a recipe.
 * @author nmcintyr
 *
 */
public interface ImageProcessor {

	/**
	 * Process a recipe that either doesn't have outputs or that handles it's own output.
	 * @param recipe
	 */
	public void process(ImageProcessingRecipe recipe);
	
	/**
	 * Process a recipe and store the output as a local image file.
	 * @param recipe
	 * @param outputFileName
	 */
	public void processAndStore(ImageProcessingRecipe recipe, String outputFileName);
	
}
