package org.ndm.photogrammetry;

import java.awt.geom.Point2D;

public class CameraIntrinsicsImpl implements CameraIntrinsics{

	private Point2D.Double principlePoint = null;
	private Double focalLength = null;
	private Double sensorWidth = null;
	private Double sensorHeight = null;
	
	public CameraIntrinsicsImpl(){}
	
	public CameraIntrinsicsImpl(Point2D.Double principlePoint, Double focalLength, Double sensorWidth, Double sensorHeight){
		this.principlePoint = principlePoint;
		this.focalLength = focalLength;
		this.sensorWidth = sensorWidth;
		this.sensorHeight = sensorHeight;
	}
	
	@Override
	public Point2D.Double getPrinciplePoint(){
		return principlePoint;
	}

	@Override
	public Double getFocalLength() {
		return focalLength;
	}

	public void setPrinciplePoint(Point2D.Double principlePoint) {
		this.principlePoint = principlePoint;
	}

	public void setFocalLength(Double focalLength) {
		this.focalLength = focalLength;
	}

	public Double getSensorWidth() {
		return sensorWidth;
	}

	public void setSensorWidth(Double sensorWidth) {
		this.sensorWidth = sensorWidth;
	}

	public Double getSensorHeight() {
		return sensorHeight;
	}

	public void setSensorHeight(Double sensorHeight) {
		this.sensorHeight = sensorHeight;
	}
	
}
