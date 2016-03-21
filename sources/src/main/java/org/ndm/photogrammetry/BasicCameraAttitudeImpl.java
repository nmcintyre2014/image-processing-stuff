package org.ndm.photogrammetry;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * A simple implementation of a exterior camera orientation.
 * @author nmcintyr
 *
 */
public class BasicCameraAttitudeImpl implements CameraAttitude{

	private RealMatrix cameraLocation = null;
	private RealMatrix rotationMatrix = null;
	
	public BasicCameraAttitudeImpl(){}
	
	public BasicCameraAttitudeImpl(RealMatrix cameraLocation, Quaternion quaternion){
		this.cameraLocation = cameraLocation;
		this.rotationMatrix = QuaternionUtil.rotationMatrixFromQuaternion2(quaternion);
	}
	
	public BasicCameraAttitudeImpl(RealMatrix cameraLocation, RealMatrix rotationMatrix){
		this.cameraLocation = cameraLocation;
		this.rotationMatrix = rotationMatrix;
	}
	
	@Override
	public RealMatrix getCameraLocation() {
		return cameraLocation;
	}

	@Override
	public RealMatrix getRotationMatrix() {
		return rotationMatrix;
	}

	public void setCameraLocation(RealMatrix cameraLocation) {
		this.cameraLocation = cameraLocation;
	}

	public void setRotationMatrix(RealMatrix rotationMatrix) {
		this.rotationMatrix = rotationMatrix;
	}
	
}
