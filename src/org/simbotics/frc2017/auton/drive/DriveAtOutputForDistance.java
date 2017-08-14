package org.simbotics.frc2017.auton.drive;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.util.RobotConstants;
import org.simbotics.frc2017.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAtOutputForDistance extends AutonCommand {

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	private SimPID gyroControl;
	private double output;
	private double distance;
	private double startPosition;
	private boolean firstCycle = true;
	private double angle;
	
	public DriveAtOutputForDistance(double output,double angle, double distance) {
		super(RobotComponent.DRIVE,-1);
		this.sensorIn = SensorInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.output = output;
		this.distance = distance;
		this.angle = angle;
		double gP = SmartDashboard.getNumber("2_Gyro P HIGH: ", RobotConstants.gyroPHigh);
		double gI = SmartDashboard.getNumber("2_Gyro I HIGH: ", RobotConstants.gyroIHigh);
		double gD = SmartDashboard.getNumber("2_Gyro D HIGH: ", RobotConstants.gyroDHigh);
		double gEps = SmartDashboard.getNumber("2_Path Turn Eps: ", RobotConstants.gyroEps);
		this.gyroControl = new SimPID(gP, gI, gD, gEps);
		
	}

	@Override
	public boolean calculate() {
		if(this.firstCycle){
			this.startPosition = this.sensorIn.getDrivePositionState();
			this.firstCycle = false;
			this.gyroControl.setDesiredValue(this.angle);
		}
		
		double currentPosition = this.sensorIn.getDrivePositionState();
		if(currentPosition > (this.startPosition + this.distance)){ // we have passed the point so stop now
			this.robotOut.setDriveLeft(0);
			this.robotOut.setDriveRight(0);
			return true;
		}else{
			double xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());
			this.robotOut.setDriveLeft(this.output+xVal);
			this.robotOut.setDriveRight(this.output-xVal);
			return false;
		}
		
		
	
	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);
	}

}
