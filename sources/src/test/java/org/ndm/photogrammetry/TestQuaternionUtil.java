package org.ndm.photogrammetry;

import static org.junit.Assert.*;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class TestQuaternionUtil {

	private RealMatrix rotationMatrix = null;
	
	@Before
	public void setUp(){
		double[][] rMatArray = new double[3][3];
		rMatArray[0] = new double[]{-0.034091, 0.999407, 0.004822};
		rMatArray[1] = new double[]{-0.999419, -0.034096, 0.000621};
		rMatArray[2] = new double[]{0.000784, -0.004798, 0.999988};
		rotationMatrix = new Array2DRowRealMatrix(rMatArray);
	}
	
	@Test
	public void testToQuaternionAndBack(){
		
		// Convert rotation matrix to quaternion
		Quaternion q = QuaternionUtil.quaternionFromRotationMatrix(rotationMatrix);
		// Convert it back to matrix form
		RealMatrix r = QuaternionUtil.rotationMatrixFromQuaternion2(q);
		
		// Make sure the rotation matrix is the same after the conversion
		assertEquals(rotationMatrix.getEntry(0, 0), r.getEntry(0, 0), 0.0001);
		assertEquals(rotationMatrix.getEntry(0, 1), r.getEntry(0, 1), 0.0001);
		assertEquals(rotationMatrix.getEntry(0, 2), r.getEntry(0, 2), 0.0001);
		assertEquals(rotationMatrix.getEntry(1, 0), r.getEntry(1, 0), 0.0001);
		assertEquals(rotationMatrix.getEntry(1, 1), r.getEntry(1, 1), 0.0001);
		assertEquals(rotationMatrix.getEntry(1, 2), r.getEntry(1, 2), 0.0001);
		assertEquals(rotationMatrix.getEntry(2, 0), r.getEntry(2, 0), 0.0001);
		assertEquals(rotationMatrix.getEntry(2, 1), r.getEntry(2, 1), 0.0001);
		assertEquals(rotationMatrix.getEntry(2, 2), r.getEntry(2, 2), 0.0001);
		
	}
}
