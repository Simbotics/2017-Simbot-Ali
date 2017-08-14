package org.simbotics.frc2017.auton.drive;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.util.RobotConstants;
import org.simbotics.frc2017.util.SimLib;
import org.simbotics.frc2017.util.SimMotionProfile;
import org.simbotics.frc2017.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightProfile extends AutonCommand {

	private RobotOutput robotOut;
	private SensorInput sensorIn;

	private SimMotionProfile encControl;
	private SimPID gyroControl;

	private boolean firstCycle = true;
	private double target;
	private double maxOutput;
	private double angle;

	public DriveStraightProfile(double target,double angle ,double eps, long timeOut) {
		this(target,angle ,-1, eps, timeOut);
	}

	public DriveStraightProfile(double target,double angle,double maxVel, double eps, long timeOut) {
		this(target,angle ,maxVel, eps, -1, timeOut);
	}

	public DriveStraightProfile(double target,double angle ,double maxVel, double eps, double epsVel, long timeOut) {
		this(target,angle, maxVel, -1, eps, epsVel, timeOut);
	}

	public DriveStraightProfile(double target,double angle, double maxVel, double maxAccel, double eps, double epsVel,
			long timeOut) {
		this(target,angle, maxVel, maxAccel, maxAccel, 1.0, eps, epsVel, timeOut);
	}

	public DriveStraightProfile(double target,double angle ,double maxVel, double maxAccel, double maxDecel, double maxOutput,
			double eps, double epsVel, long timeOut) {
		super(RobotComponent.DRIVE, timeOut);

		this.target = target;
		this.angle = angle;
		this.maxOutput = maxOutput;
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();

		double configuredMaxVelocity;
		double configuredMaxAccel;
		double configuredMaxDecel;

		double eEps;
		if (eps != -1) {
			eEps = eps;
		} else {
			eEps = SmartDashboard.getNumber("2_Enc Eps: ", RobotConstants.encEps);
		}

		if (epsVel == -1) {
			epsVel = 0.01;
		}

		double eP = SmartDashboard.getNumber("2_Enc P HIGH: ", RobotConstants.encPHigh);
		double eI = SmartDashboard.getNumber("2_Enc I HIGH: ", RobotConstants.encIHigh);
		double eD = SmartDashboard.getNumber("2_Enc D HIGH: ", RobotConstants.encDHigh);
		double eKv = SmartDashboard.getNumber("2_Enc VelFF HIGH: ", RobotConstants.encKvHigh);
		double eKF = SmartDashboard.getNumber("2_Enc AcclFF HIGH: ", RobotConstants.encKaHigh);

		double gP = SmartDashboard.getNumber("2_Gyro P HIGH: ", RobotConstants.gyroPHigh);
		double gI = SmartDashboard.getNumber("2_Gyro I HIGH: ", RobotConstants.gyroIHigh);
		double gD = SmartDashboard.getNumber("2_Gyro D HIGH: ", RobotConstants.gyroDHigh);
		double gEps = SmartDashboard.getNumber("2_Path Turn Eps: ", RobotConstants.gyroEps);

		if (maxVel != -1) { // use the custom max
			configuredMaxVelocity = maxVel;
		} else { // use the default max
			configuredMaxVelocity = RobotConstants.DRIVE_MAX_VEL_HIGH;
		}

		if (maxAccel != -1) {
			configuredMaxAccel = maxAccel;
		} else {
			configuredMaxAccel = RobotConstants.DRIVE_MAX_ACCEL_HIGH;
		}

		if (maxDecel != -1) {
			configuredMaxDecel = maxDecel;
		} else {
			configuredMaxDecel = RobotConstants.DRIVE_MAX_DECEL_HIGH;
		}

		this.encControl = new SimMotionProfile(eP, eI, eD, eKv, eKF, eEps, epsVel);
		this.gyroControl = new SimPID(gP, gI, gD, gEps);

		this.encControl.configureProfile(this.sensorIn.getDeltaTime(), configuredMaxVelocity, configuredMaxAccel,
				configuredMaxDecel);
		

	}

	@Override
	public boolean calculate() {
		if (this.firstCycle) {
			this.encControl.setDesiredValue(this.sensorIn.getDrivePositionState(),
					this.sensorIn.getDriveVelocityState(), this.sensorIn.getDriveAccelerationState(),
					this.sensorIn.getDriveFeet() + this.target); // sets the
																	// targets
																	// and gives
																	// the
																	// robots
																	// starting
																	// state
			this.gyroControl.setDesiredValue(this.angle);
			this.firstCycle = false;
			System.out.println("First Cycle");
		}
		this.encControl.setDebug(true);

		double yVal = this.encControl.calculate(this.sensorIn.getDriveFeet(), this.sensorIn.getDriveSpeedFPS());
		double xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());
		

		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		double rightDrive = SimLib.calcRightTankDrive(xVal, yVal);

		if (leftDrive > this.maxOutput) { // going too fast
			leftDrive = this.maxOutput;
		} else if (leftDrive < -this.maxOutput) {
			leftDrive = -this.maxOutput;
		}

		if (rightDrive > this.maxOutput) { // going too fast
			rightDrive = this.maxOutput;
		} else if (rightDrive < -this.maxOutput) {
			rightDrive = -this.maxOutput;
		}

		if (this.encControl.isDone()) {
			this.robotOut.setDriveLeft(0.0);
			this.robotOut.setDriveRight(0.0);
			System.out.println("CONTROL FINISHED");
			return true;
		} else {
			this.robotOut.setDriveLeft(leftDrive);
			this.robotOut.setDriveRight(rightDrive);
			System.out.println("CONTROL NOT FINISHED");
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
