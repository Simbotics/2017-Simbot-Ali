package org.simbotics.frc2017.auton.drive;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.util.RobotConstants;
import org.simbotics.frc2017.util.SimLib;
import org.simbotics.frc2017.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTurnToAngleLeftSide extends AutonCommand {

	private RobotOutput robotOut;
	private SensorInput sensorIn;

	private SimPID gyroControl;

	private boolean firstCycle = true;
	private double target;
	private double maxOutput;

	

	public DriveTurnToAngleLeftSide(double target, double eps, long timeOut) {
		this(target, 1.0, eps, timeOut);
	}
	
	public DriveTurnToAngleLeftSide(double target, long timeOut){
		this(target,1.0,-1,timeOut);
	}

	public DriveTurnToAngleLeftSide(double target, double maxOutput,double eps,long timeOut) {
		super(RobotComponent.DRIVE, timeOut);
		this.target = target;
		this.maxOutput = maxOutput;
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();


		double gEps;

		if (eps != -1) {
			gEps = eps;
		} else {
			gEps = SmartDashboard.getNumber("2_Path Turn Eps: ", RobotConstants.gyroEps);
		}

		
		double gP = SmartDashboard.getNumber("2_Gyro P HIGH: ", RobotConstants.gyroPHigh);
		double gI = SmartDashboard.getNumber("2_Gyro I HIGH: ", RobotConstants.gyroIHigh);
		double gD = SmartDashboard.getNumber("2_Gyro D HIGH: ", RobotConstants.gyroDHigh);


		this.gyroControl = new SimPID(gP, gI, gD, gEps);
		;

	}

	@Override
	public boolean calculate() {
		
		if (this.firstCycle) {
			this.firstCycle = false;
			double angle = this.sensorIn.getAngle();
			double offset = angle % 360;

			if (this.target - offset < -180) {
				this.gyroControl.setDesiredValue(angle + 360 + this.target - offset);
			} else if (this.target - offset < 180) {
				this.gyroControl.setDesiredValue(angle + this.target - offset);
			} else {
				this.gyroControl.setDesiredValue(angle - 360 + this.target - offset);
			}
		}

		double yVal = 0;
		double xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());
		
		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		
		if (leftDrive > this.maxOutput) { // going too fast
			leftDrive = this.maxOutput;
		} else if (leftDrive < -this.maxOutput) {
			leftDrive = -this.maxOutput;
		}

		

		if (this.gyroControl.isDone()) {
			this.robotOut.setDriveLeft(0.0);
			this.robotOut.setDriveRight(0.0);
			System.out.println("Control Finished");
			return true;
		} else {
			this.robotOut.setDriveLeft(leftDrive);
			this.robotOut.setDriveRight(-0.1);
			System.out.println("Control NOT Finished");
			return false;
		}

	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

}
