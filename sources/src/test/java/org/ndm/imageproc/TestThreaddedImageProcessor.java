package org.ndm.imageproc;

import org.junit.Test;

public class TestThreaddedImageProcessor {

	@Test
	public void testThreaddedImageProcessor(){

		TestimageProcessingRecipe recipe = new TestimageProcessingRecipe();
		ThreaddedImageProcessor ip = new ThreaddedImageProcessor(2);
		ip.process(recipe);
		
	}
}
