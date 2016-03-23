package org.ndm.imageproc;

import org.junit.Test;

public class TestSimpleImageProcessor {

	@Test
	public void testSimpleImageProcessor(){
		
		TestimageProcessingRecipe recipe = new TestimageProcessingRecipe();
		SimpleImageProcessor ip = new SimpleImageProcessor();
		ip.process(recipe);
		
	}
}
