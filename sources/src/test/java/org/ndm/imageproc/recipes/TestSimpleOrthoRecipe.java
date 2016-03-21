package org.ndm.imageproc.recipes;

import static org.junit.Assert.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import javax.media.jai.InterpolationBilinear;
import javax.media.jai.RenderedOp;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import org.ndm.photogrammetry.BasicCameraAttitudeImpl;
import org.ndm.photogrammetry.CameraAttitude;
import org.ndm.photogrammetry.CameraIntrinsics;
import org.ndm.photogrammetry.CameraIntrinsicsImpl;

public class TestSimpleOrthoRecipe {

	@Test
	public void testSimpleOrthoRecipe() throws URISyntaxException{
		
		CameraIntrinsics intrinsics = new CameraIntrinsicsImpl(new Point2D.Double(0.0,0.0), 55.00, 23.4, 23.4);
		Quaternion q1 = new Quaternion(0.797491947,0.062671364,-0.027469146,0.599411253);
		RealMatrix cameraLocation = new Array2DRowRealMatrix(new double[]{-122.407555, 37.771863, 58.22620021/111000.0});
		CameraAttitude attitude = new BasicCameraAttitudeImpl(cameraLocation, q1);
		
		URL url = this.getClass().getClassLoader().getResource("yellow_image.jpg");
		String imageFile = Paths.get(url.toURI()).toAbsolutePath().toString();
		
		SimpleOrthoRecipe recipe = new SimpleOrthoRecipe();
		recipe.setAttitude(attitude);
		recipe.setIntrinsics(intrinsics);
		recipe.setResamplingKernel(new InterpolationBilinear());
		recipe.setAddAlpha(Boolean.TRUE);
		recipe.setSourceImageFile(imageFile);
		
		RenderedOp ortho = recipe.getRecipeAsJaiRenderedOp();
		assertEquals(329, ortho.getWidth());
		assertEquals(337, ortho.getHeight());
		
		AffineTransform destGeoTransform = recipe.getTargetGeoTransform();
		assertEquals(8.55060304E-7, destGeoTransform.getScaleX(), 0.00000001);
		assertEquals(-122.40770947285885, destGeoTransform.getTranslateX(), 0.000000001);
	}
}
