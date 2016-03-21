package org.ndm.imageproc.recipes;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import javax.media.jai.RenderedOp;

import org.junit.Test;

public class TestDownsampleRecipe {

	@Test
	public void testDownsample() throws URISyntaxException{
		URL url = this.getClass().getClassLoader().getResource("yellow_image.jpg");
		String imageFile = Paths.get(url.toURI()).toAbsolutePath().toString();
		
		DownsampleRecipe recipe = new DownsampleRecipe(imageFile);
		
		RenderedOp downsampledImage = recipe.getRecipeAsJaiRenderedOp();
		
		// The image started out as 256x256 pixels.  The recipe is supposed to downsample by a factory of 2.
		assertEquals(128, downsampledImage.getWidth());
		assertEquals(128, downsampledImage.getHeight());
	}
}
