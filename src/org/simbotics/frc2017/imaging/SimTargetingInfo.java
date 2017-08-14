package org.simbotics.frc2017.imaging;

import org.opencv.core.Point;

// Yaw: getAngleX
//Pitch: getAngleY

public class SimTargetingInfo {
	
	private Point target;
	
	//image properties in pixels
	private static final double IMAGE_WIDTH = 320;
	private static final double IMAGE_HEIGHT = 240;
	private static final double IMAGE_CENTER_X = 159.5;
	private static final double IMAGE_CENTER_Y = 119.5;
	
	//robot/field properties in inches
	private static final double CAMERA_HEIGHT = 35.0;
	private static final double GOAL_HEIGHT = 86.0; // Height of the middle of top target
	private static final double CAMERA_PITCH_OFFSET_DEGREES = 25.5; // if its pointing up 5 degrees from forward, offset is positive 5
	
	//offsets
	private static final double SHOOTER_TO_CAMERA_OFFSET = 7.0;
	private static final double GOAL_RADIUS = 7.5; 
	
	//FOV constants
	private static final double DIAG_FOV = 68.5; // diagonal field of view in degrees (from datasheet)
	private static final double HORIZONTAL_FOV = 57.1543; 
	private static final double VERTICAL_FOV = 44.4425;
	
	private static final double FOCAL_LENGTH = 293.741259558;

	// HOW TO CALCULATE HORIZONTAL FOV FROM DIAGONAL FOV
	// here are the equations relating horiz, vert, and diag FOV to the focal point derived from pinhole camera model
	// just isolate the focal point of eq 1 and 3, and equate them to solve for fh in terms of fd
	// FOVHoriz = 2 * atan2(W/2, f)  
	// FOVVert = 2 * atan2(H/2, f)  
	// FOVDiag = 2 * atan2(sqrt(W^2 + H^2)/2, f)
	// focal length = IMAGE_WIDTH/(2.0 * Math.tan(Math.toRadians(HORIZONTAL_FOV/2)));
	
	public SimTargetingInfo() {
		
	}
	
	public double getYawToTargetDegrees(Point target) {
		this.target = target;
		double xPixel = this.target.x;
		double xDistFromImageCenter = IMAGE_CENTER_X - xPixel;
		
		double yawInRadians = Math.atan2(xDistFromImageCenter, FOCAL_LENGTH);
		return Math.toDegrees(yawInRadians);
	}
	
	public double getPitchToTargetDegrees(Point target) {
		this.target = target;
		double yPixel = this.target.y;
		double yDistFromImageCenter = IMAGE_CENTER_Y - yPixel;
		
		double pitchInRadians =  Math.atan2(yDistFromImageCenter,  FOCAL_LENGTH);
		return Math.toDegrees(pitchInRadians);
	}

	public double getDistanceToTargetInches(Point target) {
		this.target = target;
		double pitchToTarget = this.getPitchToTargetDegrees(target) + CAMERA_PITCH_OFFSET_DEGREES;
		
		double distBetweenTargetAndCamera = GOAL_HEIGHT - CAMERA_HEIGHT;
		double distanceToFrontOfGoal = distBetweenTargetAndCamera / Math.tan(Math.toRadians(pitchToTarget)); 
		return distanceToFrontOfGoal + GOAL_RADIUS + SHOOTER_TO_CAMERA_OFFSET;
	}

}