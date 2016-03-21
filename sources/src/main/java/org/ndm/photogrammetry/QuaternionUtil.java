package org.ndm.photogrammetry;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Utility functions to convert quaternions to rotation matrices and back.
 * @author nmcintyr
 *
 */
public class QuaternionUtil {

	/**
	 * Converts a quaternion into a rotation matrix.
	 * @param q - a quaternion
	 * @return - 3x3 rotation matrix
	 */
	public static RealMatrix rotationMatrixFromQuaternion2(Quaternion q){
		double[][] m = new double[3][3];
	    double sqw = q.getQ0()*q.getQ0();
	    double sqx = q.getQ1()*q.getQ1();
	    double sqy = q.getQ2()*q.getQ2();
	    double sqz = q.getQ3()*q.getQ3();

	    // invs (inverse square length) is only required if quaternion is not already normalised
	    double invs = 1 / (sqx + sqy + sqz + sqw);
	    m[0][0] = ( sqx - sqy - sqz + sqw)*invs ; // since sqw + sqx + sqy + sqz =1/invs*invs
	    m[1][1] = (-sqx + sqy - sqz + sqw)*invs ;
	    m[2][2] = (-sqx - sqy + sqz + sqw)*invs ;
	    
	    double tmp1 = q.getQ1()*q.getQ2();
	    double tmp2 = q.getQ3()*q.getQ0();
	    m[1][0] = 2.0 * (tmp1 + tmp2)*invs ;
	    m[0][1] = 2.0 * (tmp1 - tmp2)*invs ;
	    
	    tmp1 = q.getQ1()*q.getQ3();
	    tmp2 = q.getQ2()*q.getQ0();
	    m[2][0] = 2.0 * (tmp1 - tmp2)*invs ;
	    m[0][2] = 2.0 * (tmp1 + tmp2)*invs ;
	    
	    tmp1 = q.getQ2()*q.getQ3();
	    tmp2 = q.getQ1()*q.getQ0();
	    m[2][1] = 2.0 * (tmp1 + tmp2)*invs ;
	    m[1][2] = 2.0 * (tmp1 - tmp2)*invs ;      
	    
	    Array2DRowRealMatrix rotationMatrix = new Array2DRowRealMatrix(m);
	    return rotationMatrix;
	}
	
	/*
	public static RealMatrix rotationMatrixFromQuaterion(Quaternion q){
		
		double[][] rMatArray = new double[3][3];
		rMatArray[0][0] = 1-(2*q.getQ2()*q.getQ2())-(2*q.getQ3()*q.getQ3());
		rMatArray[0][1] = (2*q.getQ1()*q.getQ2())-(2*q.getQ3()*q.getQ0());
		rMatArray[0][2] = (2*q.getQ1()*q.getQ3())+(2*q.getQ1()*q.getQ0());
		rMatArray[1][0] = (2*q.getQ1()*q.getQ2())+(2*q.getQ3()*q.getQ0());
		rMatArray[1][1] = 1-(2*q.getQ1()*q.getQ1())-(2*q.getQ2()*q.getQ2());
		rMatArray[1][2] = (2*q.getQ2()*q.getQ3())-(2*q.getQ1()*q.getQ0());
		rMatArray[2][0] = (2*)
		Array2DRowRealMatrix rotationMatrix = new Array2DRowRealMatrix(rMatArray);
		return rotationMatrix;
		
	}
	*/
	
	/**
	 * Converts a rotation matrix into a quaternion
	 * @param r - rotatin matrix
	 * @return - a quaternion
	 */
	public static Quaternion quaternionFromRotationMatrix(RealMatrix r){
		double qw;
		double qx;
		double qy;
		double qz;
		
		double m00 = r.getEntry(0, 0);
		double m01 = r.getEntry(0, 1);
		double m02 = r.getEntry(0, 2);
		double m10 = r.getEntry(1, 0);
		double m11 = r.getEntry(1, 1);
		double m12 = r.getEntry(1, 2);
		double m20 = r.getEntry(2, 0);
		double m21 = r.getEntry(2, 1);
		double m22 = r.getEntry(2, 2);
		
		double tr = m00 + m11 + m22;

		if (tr > 0) { 
			double S = Math.sqrt(tr+1.0) * 2; // S=4*qw 
			qw = 0.25 * S;
			qx = (m21 - m12) / S;
			qy = (m02 - m20) / S; 
			qz = (m10 - m01) / S; 
		} else if ((m00 > m11)&(m00 > m22)) { 
			double S = Math.sqrt(1.0 + m00 - m11 - m22) * 2; // S=4*qx 
			qw = (m21 - m12) / S;
			qx = 0.25 * S;
			qy = (m01 + m10) / S; 
			qz = (m02 + m20) / S; 
		} else if (m11 > m22) { 
			double S = Math.sqrt(1.0 + m11 - m00 - m22) * 2; // S=4*qy
			qw = (m02 - m20) / S;
			qx = (m01 + m10) / S; 
			qy = 0.25 * S;
			qz = (m12 + m21) / S; 
		} else { 
			double S = Math.sqrt(1.0 + m22 - m00 - m11) * 2; // S=4*qz
			qw = (m10 - m01) / S;
			qx = (m02 + m20) / S;
			qy = (m12 + m21) / S;
			qz = 0.25 * S;
		}
		
		Quaternion q = new Quaternion(qw, qx, qy, qz);
		return q;
	}
	
}
