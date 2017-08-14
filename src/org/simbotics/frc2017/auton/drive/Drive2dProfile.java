package org.simbotics.frc2017.auton.drive;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.util.RobotConstants;
import org.simbotics.frc2017.util.SimLib;
import org.simbotics.frc2017.util.SimMotionProfile;
import org.simbotics.frc2017.util.SimPID;
import org.simbotics.frc2017.util.path.generation.PathList;
import org.simbotics.frc2017.util.path.generation.WaypointSequence;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive2dProfile extends AutonCommand {

	private RobotOutput robotOut;
	private SensorInput sensorIn;

	private SimMotionProfile encControl;
	private SimPID gyroControl;
	
	private WaypointSequence path;

	private boolean firstCycle = true;
	private double totalPathLength;
	private double maxOutput;
	private boolean isBackwards;
	private int currentTargetWaypoint = 0;
	private double eEps;
	
	

	public Drive2dProfile(String WaypointSequence ,boolean isBackwards,double eps, long timeOut) {
		this(WaypointSequence,isBackwards,-1, eps, timeOut);
	}

	public Drive2dProfile(String WaypointSequence,boolean isBackwards,double maxVel, double eps, long timeOut) {
		this(WaypointSequence,isBackwards,maxVel, eps, -1, timeOut);
	}
	
	public Drive2dProfile(String WaypointSequence,boolean isBackwards,double maxVel, double eps, double turnP, long timeOut) {
		this(WaypointSequence,isBackwards,maxVel, -1, -1, 1.0, eps, -1, turnP,timeOut);
	}

	public Drive2dProfile(String WaypointSequence,boolean isBackwards,double maxVel, double maxAccel, double eps, double epsVel,
			long timeOut) {
		this(WaypointSequence,isBackwards,maxVel, maxAccel, maxAccel, 1.0, eps, epsVel, -1,timeOut);
	}

	public Drive2dProfile(String WaypointSequence,boolean isBackwards,double maxVel, double maxAccel, double maxDecel, double maxOutput,
			double eps, double epsVel,double turnP ,long timeOut) {
		super(RobotComponent.DRIVE, timeOut);

		PathList.getPaths();
		
		this.path = PathList.getPathByName(WaypointSequence);
		this.isBackwards = isBackwards;
		this.totalPathLength = this.path.getWaypointSequenceDistance();
		this.maxOutput = maxOutput;
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();

		double configuredMaxVelocity;
		double configuredMaxAccel;
		double configuredMaxDecel;

		
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
		
		if(turnP != -1){
			gP = turnP;
			gI = 0.0001;
			gD = 0.00;
		}else{
			
		}
		
		
		
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
			if(this.isBackwards){
				this.encControl.setDesiredValue(this.sensorIn.getDrivePositionState(),
					this.sensorIn.getDriveVelocityState(), this.sensorIn.getDriveAccelerationState(),
					this.sensorIn.getDriveFeet() - (this.totalPathLength + this.path.getDistanceToWaypoint(this.sensorIn.getDriveXPos(),
							this.sensorIn.getDriveYPos(), 0)));
			}else{
				this.encControl.setDesiredValue(this.sensorIn.getDrivePositionState(),
					this.sensorIn.getDriveVelocityState(), this.sensorIn.getDriveAccelerationState(),
					this.sensorIn.getDriveFeet() + (this.totalPathLength + this.path.getDistanceToWaypoint(this.sensorIn.getDriveXPos(),
							this.sensorIn.getDriveYPos(), 0)));
			}
			this.firstCycle = false;
			System.out.println("First Cycle");
		}
		
		if(SimLib.isWithinRange(this.path.getWaypoint(currentTargetWaypoint).x, this.sensorIn.getDriveXPos(), 0.7)
				 && SimLib.isWithinRange(this.path.getWaypoint(currentTargetWaypoint).y, this.sensorIn.getDriveYPos(), 0.7)){
			currentTargetWaypoint++;
		}
		
		
		
		if(currentTargetWaypoint > path.getNumWaypoints()-1){ // we made it to the last waypoint
			currentTargetWaypoint = path.getNumWaypoints()-1; 
			this.gyroControl.setDesiredValue(this.path.getWaypoint(currentTargetWaypoint).theta); // go to the final angle
		}else{
			if(this.isBackwards){
				this.gyroControl.setDesiredValue(this.path.getAngleBetweenWaypoint(this.sensorIn.getDriveXPos(),this.sensorIn.getDriveYPos(),
						currentTargetWaypoint) + 180);
			}else{
				this.gyroControl.setDesiredValue(this.path.getAngleBetweenWaypoint(this.sensorIn.getDriveXPos(),this.sensorIn.getDriveYPos(),
						currentTargetWaypoint));
			}
			
		}
		
		SmartDashboard.putNumber("123_Current Waypoint: ",currentTargetWaypoint);
		SmartDashboard.putNumber("123_Current Target Angle: ", this.gyroControl.getDesiredVal());
		
		
		this.encControl.setDebug(true);
		
		double yVal;
		double xVal;
		
		//double angleDelta = Math.abs(this.gyroControl.getDesiredVal() - this.sensorIn.getGyroPositionState());
		
		//yVal = Math.max(0,((-1*(angleDelta*angleDelta)*0.0006)+1)) * this.encControl.calculate(this.sensorIn.getDriveFeet(), this.sensorIn.getDriveSpeedFPS());
		yVal = this.encControl.calculate(this.sensorIn.getDriveFeet(), this.sensorIn.getDriveSpeedFPS());
		xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());
		
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
			if(eEps != SmartDashboard.getNumber("2_Enc Eps: ", RobotConstants.encEps)){
				this.robotOut.setDriveLeft(0.0);
				this.robotOut.setDriveRight(0.0);
				System.out.println("CONTROL FINISHED");
				return true;
			}else{
				if(SimLib.isWithinRange(this.path.getWaypoint(this.path.getNumWaypoints()-1).theta,
						this.sensorIn.getGyroPositionState(),1.0)){
					this.robotOut.setDriveLeft(0.0);
					this.robotOut.setDriveRight(0.0);
					System.out.println("CONTROL FINISHED");
					return true;
				}else{
					this.robotOut.setDriveLeft(leftDrive);
					this.robotOut.setDriveRight(rightDrive);
					System.out.println("CONTROL NOT FINISHED");
					return false;
				}
			}
			
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
