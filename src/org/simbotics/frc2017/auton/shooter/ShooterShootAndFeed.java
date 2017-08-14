package org.simbotics.frc2017.auton.shooter;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.util.RobotConstants;
import org.simbotics.frc2017.util.SimPIDF;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterShootAndFeed extends AutonCommand {
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	private double targetRPM;
	private double elevatorSpeed;
	private double epsRange;
	private boolean usingCameraRPM;
	private boolean firstCylce = true;
	private SimPIDF elevatorControl;

	public ShooterShootAndFeed(double targetRPM, double elevatorSpeed, double epsRange, boolean usingCameraRPM,
			long timeOut) {
		super(RobotComponent.SHOOTER, timeOut);
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.targetRPM = targetRPM;
		this.elevatorSpeed = elevatorSpeed;
		this.epsRange = epsRange;
		this.usingCameraRPM = usingCameraRPM;
		this.elevatorControl = new SimPIDF(0, 0, 0, 0, 0);

	}

	@Override
	public boolean calculate() {

		if (this.usingCameraRPM) {
			double dist = this.sensorIn.getDistanceFromGoalInches();
			double x2 = SmartDashboard.getNumber("2_Shooter X^2: ", RobotConstants.SHOOTER_X_2);
			double x = SmartDashboard.getNumber("2_Shooter X: ", RobotConstants.SHOOTER_X);
			double shift = SmartDashboard.getNumber("2_Shooter Shift: ", RobotConstants.SHOOTER_SHIFT);
			
			this.targetRPM =  (int) (x2*(dist*dist)+x*(dist)+shift);
		}

		this.robotOut.setShooterTarget(this.targetRPM);

		double eP = SmartDashboard.getNumber("2_Elevator P: ", RobotConstants.elevatorP);
		double eI = SmartDashboard.getNumber("2_Elevator I: ", RobotConstants.elevatorI);
		double eD = SmartDashboard.getNumber("2_Elevator D: ", RobotConstants.elevatorD);
		double eFF = SmartDashboard.getNumber("2_Elevator FF: ", RobotConstants.elevatorFF);
		double eEps = SmartDashboard.getNumber("2_Elevator Eps: ", RobotConstants.elevatorEps);

		this.elevatorControl.setConstants(eP, eI, eD, eFF);
		this.elevatorControl.setFinishedRange(eEps);

		this.elevatorControl.setDesiredValue(this.elevatorSpeed);
		double output = this.elevatorControl.calcPID(this.sensorIn.getElevatorRPM());
		this.robotOut.setElevator(output);
		this.robotOut.setBlender(1.0);

		return false;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		this.robotOut.setShooterOutput(0.0);
		this.robotOut.setElevator(0.0);
		this.robotOut.setBlender(0.0);
	}

}
