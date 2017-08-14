package org.simbotics.frc2017.util;

import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SimDriveControl {

	private RobotOutput robotOut;
	private SensorInput sensorIn;
	
	private SimDriveMode driveMode = SimDriveMode.VELOCITY;
	
	private SimPIDF leftDrivePIDF;
	private SimPIDF rightDrivePIDF;
	private double prevDesiredVelocityLeft;
	private double prevDesiredVelocityRight;
	
	public SimDriveControl() {
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		
		this.leftDrivePIDF = new SimPIDF(RobotConstants.encPHigh,RobotConstants.encIHigh, RobotConstants.encDHigh,RobotConstants.encKvHigh,0);
		this.rightDrivePIDF = new SimPIDF(RobotConstants.encPHigh,RobotConstants.encIHigh, RobotConstants.encDHigh,RobotConstants.encKvHigh,0);
	}
	
	public enum SimDriveMode {
		OUTPUT,VELOCITY
	}
	
	public void setDriveMode(SimDriveMode mode){
		this.driveMode = mode;
	}
	
	public void setDrive(double x, double y){
		SmartDashboard.putString("12_DRIVE CONTROL STATE: ", this.driveMode.toString());
		if(this.driveMode == SimDriveMode.VELOCITY && Math.abs(y) >= 0.25){
			this.velocityDrive(x, y);
		}else{
			this.leftDrivePIDF.resetErrorSum();
			this.rightDrivePIDF.resetErrorSum();
			this.outputDrive(x, y);
		}
	}
	
	
	private void outputDrive(double x, double y){
		// arcade calculations
		double leftOut = SimLib.calcLeftTankDrive(x, y);
		double rightOut = SimLib.calcRightTankDrive(x, y);

		// outputs to motors
		this.robotOut.setDriveLeft(leftOut);
		this.robotOut.setDriveRight(rightOut);
	}
	
	private void velocityDrive(double x, double y){
		double p;
		double i;
		double d;
		double f;
		double desiredVelocityLeft;
		double desiredVelocityRight;
		
		if(this.robotOut.getHighGear()){ // high gear
			p=0.15;
			i=0;
			d=0;
			f=RobotConstants.encKvHigh;
			
			desiredVelocityLeft = (y * RobotConstants.DRIVE_MAX_VEL_HIGH) + (x * RobotConstants.DRIVE_MAX_TURN_RATE_HIGH);
			desiredVelocityRight = (y * RobotConstants.DRIVE_MAX_VEL_HIGH ) - (x * RobotConstants.DRIVE_MAX_TURN_RATE_HIGH);
			
		}else{ // low gear
			p=0.1;
			i=0;
			d=0;
			f=RobotConstants.encKvLow;
			
			desiredVelocityLeft = (y * RobotConstants.DRIVE_MAX_VEL_LOW) + (x * RobotConstants.DRIVE_MAX_TURN_RATE_LOW);
			desiredVelocityRight = (y * RobotConstants.DRIVE_MAX_VEL_LOW) - (x * RobotConstants.DRIVE_MAX_TURN_RATE_LOW);
		}
		
		if(Math.abs(desiredVelocityLeft - prevDesiredVelocityLeft) > 1.0) { // if our target is too different then reset i
			this.leftDrivePIDF.resetErrorSum();
		}
		
		if(Math.abs(desiredVelocityRight - prevDesiredVelocityRight) > 1.0) { // if our target is too different then reset i
			this.rightDrivePIDF.resetErrorSum();
		}
		
		this.leftDrivePIDF.setConstants(p, i, d, f);
		this.rightDrivePIDF.setConstants(p, i, d, f);
		
		this.leftDrivePIDF.setDesiredValue(desiredVelocityLeft);
		this.rightDrivePIDF.setDesiredValue(desiredVelocityRight);
		
		double leftOut = this.leftDrivePIDF.calcPID(this.sensorIn.getLeftDriveSpeedFPS());
		double rightOut = this.rightDrivePIDF.calcPID(this.sensorIn.getRightDriveSpeedFPS());
	
		this.robotOut.setDriveLeft(leftOut);
		this.robotOut.setDriveRight(rightOut);
	}
}
