package org.simbotics.frc2017.auton.shooter;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.util.RobotConstants;
import org.simbotics.frc2017.util.SimPIDF;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterShootAndFeedTimer extends AutonCommand {
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	private double targetRPM;
	private double elevatorSpeedStart;
	private double elevatorSpeedEnd;
	private double epsRange;
	private boolean usingCameraRPM;
	private boolean firstCylce = true;
	private long timeToSwitchSpeeds;
	private SimPIDF elevatorControl;
	
	private boolean cookieMonster = false;

	public ShooterShootAndFeedTimer(double targetRPM, double elevatorSpeedStart, double elevatorSpeedEnd,long timeToSwitchSpeeds, boolean usingCameraRPM,
			long timeOut) {
		super(RobotComponent.SHOOTER, timeOut);
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.targetRPM = targetRPM;
		this.elevatorSpeedStart = elevatorSpeedStart;
		this.elevatorSpeedEnd= elevatorSpeedEnd;
		this.usingCameraRPM = usingCameraRPM;
		this.timeToSwitchSpeeds = timeToSwitchSpeeds;
		this.elevatorControl = new SimPIDF(0, 0, 0, 0, 0);

	}

	@Override
	public boolean calculate() {
		
		//this.cookieMonster = !this.cookieMonster;
		//this.robotOut.setBallRamp(this.cookieMonster);
		this.robotOut.setHanger(0.5);
		
		if (this.usingCameraRPM) {
			double x2;
			double x; 
			double shift;
			if(this.sensorIn.getTimeSinceAutoStarted() < this.timeToSwitchSpeeds){ // first speed
				if(this.elevatorSpeedStart > 1200){ // fast
					x2 = SmartDashboard.getNumber("2_Shooter X^2 Fast: ", RobotConstants.SHOOTER_X_2_FAST);
					x = SmartDashboard.getNumber("2_Shooter X Fast: ", RobotConstants.SHOOTER_X_FAST);
					shift = SmartDashboard.getNumber("2_Shooter Shift Fast: ", RobotConstants.SHOOTER_SHIFT_FAST);
				}else{ // slow
					x2 = SmartDashboard.getNumber("2_Shooter X^2: ", RobotConstants.SHOOTER_X_2);
					x = SmartDashboard.getNumber("2_Shooter X: ", RobotConstants.SHOOTER_X);
					shift = SmartDashboard.getNumber("2_Shooter Shift: ", RobotConstants.SHOOTER_SHIFT);
				}
			}else{ // second speed
				if(this.elevatorSpeedEnd > 1200){ // fast
					x2 = SmartDashboard.getNumber("2_Shooter X^2 Fast: ", RobotConstants.SHOOTER_X_2_FAST);
					x = SmartDashboard.getNumber("2_Shooter X Fast: ", RobotConstants.SHOOTER_X_FAST);
					shift = SmartDashboard.getNumber("2_Shooter Shift Fast: ", RobotConstants.SHOOTER_SHIFT_FAST);
				}else{ // slow
					x2 = SmartDashboard.getNumber("2_Shooter X^2: ", RobotConstants.SHOOTER_X_2);
					x = SmartDashboard.getNumber("2_Shooter X: ", RobotConstants.SHOOTER_X);
					shift = SmartDashboard.getNumber("2_Shooter Shift: ", RobotConstants.SHOOTER_SHIFT);
				}
			}
			double dist = this.sensorIn.getDistanceFromGoalInches();
			this.targetRPM =  (int) (x2*(dist*dist)+x*(dist)+shift);
		}
		double p = SmartDashboard.getNumber("2_Shooter P: ", RobotConstants.shooterP);
		double i = SmartDashboard.getNumber("2_Shooter I: ", RobotConstants.shooterI);
		double d = SmartDashboard.getNumber("2_Shooter D: ", RobotConstants.shooterD);
		double ff = SmartDashboard.getNumber("2_Shooter FF: ", RobotConstants.shooterFF);
		double eps = SmartDashboard.getNumber("2_Shooter Eps: " , RobotConstants.shooterEps);
		this.robotOut.configureShooterPID(p, i, d, ff, (int) eps);
		
		this.robotOut.setShooterTarget(this.targetRPM);

		double eP = SmartDashboard.getNumber("2_Elevator P: ", RobotConstants.elevatorP);
		double eI = SmartDashboard.getNumber("2_Elevator I: ", RobotConstants.elevatorI);
		double eD = SmartDashboard.getNumber("2_Elevator D: ", RobotConstants.elevatorD);
		double eFF = SmartDashboard.getNumber("2_Elevator FF: ", RobotConstants.elevatorFF);
		double eEps = SmartDashboard.getNumber("2_Elevator Eps: ", RobotConstants.elevatorEps);

		this.elevatorControl.setConstants(eP, eI, eD, eFF);
		this.elevatorControl.setFinishedRange(eEps);
		if(this.sensorIn.getTimeSinceAutoStarted() < this.timeToSwitchSpeeds){ // first speed
			this.elevatorControl.setDesiredValue(this.elevatorSpeedStart);
			double output = this.elevatorControl.calcPID(this.sensorIn.getElevatorRPM());
			this.robotOut.setElevator(output);
			this.robotOut.setBlender(1.0);
		}else{ // second speed
			this.elevatorControl.setDesiredValue(this.elevatorSpeedEnd);
			double output = this.elevatorControl.calcPID(this.sensorIn.getElevatorRPM());
			this.robotOut.setElevator(output);
			this.robotOut.setBlender(1.0);
		}
		

		return false;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		this.robotOut.setShooterOutput(0.0);
		this.robotOut.setElevator(0.0);
		this.robotOut.setBlender(0.0);
		this.robotOut.setHanger(0.0);
	}

}
