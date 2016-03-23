package org.ndm.imageproc;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

public class TestimageProcessingRecipe implements ImageProcessingRecipe{

	@Override
	public RenderedOp getRecipeAsJaiRenderedOp() {
		ParameterBlock pbC = new ParameterBlock();
		pbC.add(1024f);
		pbC.add(1024f);
		pbC.add(new Byte[]{0x00, (byte) 0xff, 0x00});
		return JAI.create("constant", pbC);
	}

}
