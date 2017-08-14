package org.simbotics.frc2017.io;

import org.opencv.core.Point;
import org.simbotics.frc2017.imaging.SimCamera;
import org.simbotics.frc2017.imaging.SimTargetingInfo;
import org.simbotics.frc2017.util.Debugger;
import org.simbotics.frc2017.util.SimEncoder;
import org.simbotics.frc2017.util.SimNavx;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorInput {
	
	private static SensorInput instance;
	
	private SimCamera simCam;
	private SimTargetingInfo simTargeting;
	
	private SimEncoder encDriveLeft;
	private SimEncoder encDriveRight;
	private SimEncoder encTurret;
	private SimEncoder encElevator; 
	private DigitalInput springLightSensor;
	
	private SimNavx navx;
	private RobotOutput robotOut;
	
	private DriverStation driverStation;
	private PowerDistributionPanel pdp;
	
	private double lastTime = 0.0;
	private double deltaTime = 20.0;
	
	private boolean firstCycle = true;
	
	private static double TURRET_TICKS_PER_DEGREE = 1024.0 * (106.0/18.0) / 360.0; // encoder ticks * turret ratio / 360 to get degrees
	private static double DRIVE_TICKS_PER_INCH_HIGH = (1024.0) * (44.0/30.0) / (Math.PI*4.125); // encoder ratio * drive output ratio / (PI*D)
	private static double DRIVE_TICKS_PER_INCH_LOW = (1024.0) * (60.0/14.0) / (Math.PI*4.125); // encoder ratio * drive output ratio / (PI*D)
	private static double ELEVATOR_TICKS_PER_REV = 1024.0;
	
	private double elevatorRPM;
	
	private double leftDriveSpeedFPS;
	private double rightDriveSpeedFPS;
	
	private double lastLeftDriveSpeedFPS = 0;
	private double lastRightDriveSpeedFPS = 0;
	private double leftDriveAccelerationFPSSquared; 
	private double rightDriveAccelerationFPSSquared; 
	private double leftDriveJerkFPSCubed; 
	private double rightDriveJerkFPSCubed;
	private double lastLeftDriveAccelerationFPSSquared;
	private double lastRightDriveAccelerationFPSSquared;
	
	private double gyroAngle;
	private double lastGyroAngle;
	private double gyroVelocity = 0; // degrees per second
	private double lastGyroVelocity =0;
	private double gyroAcceleration =0; 
	
	private double drivePositionState;
	private double driveVelocityState;
	private double driveAccelerationState;
	private double driveJerkState;
	
	private double driveXPos = 0;
	private double driveYPos = 0;
	
	
	private double gyroPositionState;
	private double gyroVelocityState;
	private double gyroAccelerationState;
	
	private double turretPositionState;
	private double turretVelocityState;
	private double turretAccelerationState;
	private double turretLastVelocityState = 0;
	
	private Thread cameraThread;
	private boolean cameraThreadRunning = false;
	private boolean usingNavX = true;
	
	private double distanceFromGoalInches = 90; //rough distance for auto incase camera doesnt work
	private boolean checkForMovement = false;
	private boolean hasMoved = false;
	
	
	private DriverStationState driverStationMode = DriverStationState.DISABLED;
	private DriverStation.Alliance alliance; 
	private double matchTime; 
	
	
	private long SystemTimeAtAutonStart = 0;
	private long timeSinceAutonStarted = 0;
	
	private SensorInput(){
		this.simTargeting = new SimTargetingInfo();
		this.simCam = new SimCamera();
		this.cameraThread = new Thread(this.simCam);
		this.cameraThread.setPriority(Thread.NORM_PRIORITY-1);
		this.robotOut = RobotOutput.getInstance();
		
		this.encDriveLeft = new SimEncoder(0,1);
		this.encDriveRight = new SimEncoder(3,2);
		this.encTurret = new SimEncoder(4,5);
		this.encElevator = new SimEncoder(7,6);
		
		this.springLightSensor = new DigitalInput(8);
		
		if (this.usingNavX) {
			this.navx = new SimNavx();
		}
		this.pdp = new PowerDistributionPanel();
		this.driverStation = DriverStation.getInstance();
				
		
		this.reset();
	}
	
	public static SensorInput getInstance() {
        if(instance == null) {
            instance = new SensorInput();
        }
        return instance;
    }
	
	public void reset(){
		if(this.usingNavX) {
			this.navx.reset();
		}
		this.encDriveLeft.reset();
		this.encDriveRight.reset();
		this.driveXPos = 0;
		this.driveYPos = 0;
		this.encTurret.reset();
		this.firstCycle = true;
	}
	
	public void resetAutonTimer(){
		this.SystemTimeAtAutonStart = System.currentTimeMillis();
	}
	
	public void resetDriveEncoders() {
		this.encDriveLeft.reset();
		this.encDriveRight.reset();
	}
	
	public void update(){
		if(!this.cameraThreadRunning) {
			Debugger.println("start cam thread");
			this.cameraThread.start();
			this.cameraThreadRunning = true;
		}
		if (this.lastTime == 0.0) {
    		this.deltaTime = 20;
    		this.lastTime = System.currentTimeMillis();
    	} else {
    		this.deltaTime = System.currentTimeMillis() - this.lastTime;
    		this.lastTime = System.currentTimeMillis();
    	}
		
		if(this.driverStation.isAutonomous()){
			this.timeSinceAutonStarted = System.currentTimeMillis() - this.SystemTimeAtAutonStart;
			SmartDashboard.putNumber("12_Time Since Auto Start:", this.timeSinceAutonStarted);
		}
		
		
    	if(this.firstCycle){
    		this.firstCycle = false;
    		if(this.usingNavX) {
    			this.lastGyroAngle = this.navx.getAngle();
    		} else {
    			this.lastGyroAngle = 0.0;
    		}
    	}
    	
    	if(this.usingNavX) {
			this.navx.update();
		}
		
    	
    	Point target = this.simCam.getMidPoint();
    	if (target != null) {
    		double targetP = this.simTargeting.getPitchToTargetDegrees(target);
    		double targetD = this.simTargeting.getDistanceToTargetInches(target);
    		double targetY = this.simTargeting.getYawToTargetDegrees(target);
    		Debugger.println("target p" + targetP, "CAMERA");
        	Debugger.println("target d " + targetD, "CAMERA");
        	Debugger.println("target y " + targetY, "CAMERA");
        	SmartDashboard.putNumber("2_target pitch", targetP);
        	SmartDashboard.putNumber("2_target distance", targetD);
        	SmartDashboard.putNumber("2_target yaw", targetY);
        	SmartDashboard.putNumber("2_target X", target.x);
        	SmartDashboard.putNumber("2_target y", target.y);
        	
    	}
    	
    	this.encDriveLeft.updateSpeed();
    	this.encDriveRight.updateSpeed();
    	this.encTurret.updateSpeed();
    	this.encElevator.updateSpeed();
    	

    	this.turretPositionState = (this.encTurret.get() / TURRET_TICKS_PER_DEGREE) + 90; // start facing forwards 
    	this.turretVelocityState = (this.encTurret.speed() / TURRET_TICKS_PER_DEGREE) / (this.deltaTime/1000.0); 
    	this.turretAccelerationState = (this.turretVelocityState - this.turretLastVelocityState) / (this.deltaTime/1000.0);
    	this.turretLastVelocityState = this.turretVelocityState;
    	
    	this.elevatorRPM = (this.encElevator.speed() / ELEVATOR_TICKS_PER_REV) / (this.deltaTime/60000.0); 
    	
    	double left;
    	double right;
    	
        if(this.robotOut.getHighGear()){
        	left = this.getEncoderLeftSpeed() / DRIVE_TICKS_PER_INCH_HIGH;
    		right = this.getEncoderRightSpeed() / DRIVE_TICKS_PER_INCH_HIGH;
        }else{
        	left = this.getEncoderLeftSpeed() / DRIVE_TICKS_PER_INCH_LOW;
        	right = this.getEncoderRightSpeed() / DRIVE_TICKS_PER_INCH_LOW;
        }
    	
    
    	
    	this.leftDriveSpeedFPS = (left/12)/(this.deltaTime/1000.0);
		this.rightDriveSpeedFPS =  (right/12)/(this.deltaTime/1000.0);
    	
		this.leftDriveAccelerationFPSSquared = (this.leftDriveSpeedFPS-this.lastLeftDriveSpeedFPS)/(this.deltaTime/1000.0);
		this.rightDriveAccelerationFPSSquared = (this.rightDriveSpeedFPS-this.lastRightDriveSpeedFPS)/(this.deltaTime/1000.0);
    	
		this.leftDriveJerkFPSCubed = (this.leftDriveAccelerationFPSSquared-this.lastLeftDriveAccelerationFPSSquared)/(this.deltaTime/1000.0);
		this.rightDriveJerkFPSCubed = (this.rightDriveAccelerationFPSSquared-this.lastRightDriveAccelerationFPSSquared)/(this.deltaTime/1000.0);
    	
		
		this.lastLeftDriveSpeedFPS = this.leftDriveSpeedFPS;
    	this.lastRightDriveSpeedFPS = this.rightDriveSpeedFPS;
    	
    	this.lastLeftDriveAccelerationFPSSquared = this.leftDriveAccelerationFPSSquared;
    	this.lastRightDriveAccelerationFPSSquared = this.rightDriveAccelerationFPSSquared;
		
    	if(this.usingNavX) {
    		this.gyroAngle = this.navx.getAngle();
    	} else {
    		this.gyroAngle = 0.0;
    	}
    	this.gyroVelocity = (this.gyroAngle-this.lastGyroAngle)/(this.deltaTime/1000.0); // gyro speed in degrees per second
    	this.lastGyroAngle = this.gyroAngle; 

    	this.gyroAcceleration = (this.gyroVelocity - this.lastGyroVelocity) / (this.deltaTime/1000.0);
    	this.lastGyroVelocity = this.gyroVelocity;
    	
    	this.drivePositionState = this.getDriveFeet();
    	this.driveVelocityState= this.getDriveSpeedFPS();
    	this.driveAccelerationState= this.getDriveAcceleration();
    	this.driveJerkState = this.getDriveJerk();
    	
    	this.gyroPositionState= this.getAngle();
    	this.gyroVelocityState= this.getGyroVelocity();
    	this.gyroAccelerationState= this.getGyroAcceleration();
    	
    	if(this.driverStation.isDisabled()){
    		this.driverStationMode = DriverStationState.DISABLED;
    	}else if(this.driverStation.isAutonomous()){
    		this.driverStationMode = DriverStationState.AUTONOMOUS;
    	}else if(this.driverStation.isOperatorControl()){
    		this.driverStationMode = DriverStationState.TELEOP;
    	}
    	
    	this.alliance = this.driverStation.getAlliance();
    	this.matchTime = this.driverStation.getMatchTime();
    	
    	if(this.checkForMovement){
    		if(this.getIsDriveMoving()){ // if the drive has moved once since we starting looking
    			this.hasMoved = true;
    		}
    	}else{
    		this.hasMoved = false;
    	}
    	
    	double driveXSpeed = this.driveVelocityState * Math.cos(Math.toRadians(this.gyroPositionState));
    	double driveYSpeed = this.driveVelocityState * Math.sin(Math.toRadians(this.gyroPositionState));
    	
    	driveXPos += driveXSpeed * this.deltaTime/1000.0;
    	driveYPos += driveYSpeed * this.deltaTime/1000.0;
    	
    	
    	
    	SmartDashboard.putString("1_MODE: ", this.driverStationMode.toString());
    	SmartDashboard.putString("1_ALLIANCE: ", this.alliance.toString());
    	SmartDashboard.putNumber("1_MATCH TIME: ", this.matchTime);
    	
    	SmartDashboard.putBoolean("123_isHighGear: ", this.robotOut.getHighGear());
    	SmartDashboard.putNumber("123_DRIVE POSITION FEET: ", this.getDriveFeet());
    	SmartDashboard.putNumber("123_DRIVE SPEED: ", this.getDriveSpeedFPS());
    	SmartDashboard.putNumber("23_DRIVE ACCEL: ", this.getDriveAcceleration());
    	
    	SmartDashboard.putNumber("123_DRIVE POSITION X: ", this.getDriveXPos());
    	SmartDashboard.putNumber("123_DRIVE POSITION Y: ", this.getDriveYPos());
    	
    	SmartDashboard.putNumber("123_Gyro: ", this.getAngle());
    	SmartDashboard.putNumber("23_GYRO SPEED: ", this.getGyroVelocity());
    	SmartDashboard.putNumber("23_GYRO ACCEL: ", this.getGyroAcceleration());
    	
    	SmartDashboard.putNumber("123_TURRET POSITION: ", this.getTurretPositionState());
    	SmartDashboard.putNumber("23_TURRET VELOCITY: ", this.getTurretVelocityState());
    	SmartDashboard.putNumber("23_TURRET ACCEL: ", this.getTurretAccelerationState());
    	
    	SmartDashboard.putNumber("3_Left Enc",this.getEncoderLeft());
    	SmartDashboard.putNumber("3_Right Enc",this.getEncoderRight());
    	
    	SmartDashboard.putNumber("123_Shooter RPM: ", this.robotOut.getShooterRPM());
    	SmartDashboard.putNumber("123_DistanceFromGoalInches: ", this.getDistanceFromGoalInches());
    	SmartDashboard.putNumber("123_Elevator RPM: ", this.elevatorRPM);
    	
    	SmartDashboard.putNumber("2_DRIVE_LEFT_POSITION_FEET", this.getLeftDriveFeet());
    	SmartDashboard.putBoolean("12_IsDriveMoving: ", this.getIsDriveMoving());
    	SmartDashboard.putBoolean("12_HasDriveMoved: ", this.hasMoved);
    	SmartDashboard.putBoolean("123_Spring Light Sensor: ", this.getSpringLightSensor());
    	
    	
	}
	
	public SimCamera getSimCamera() {
		return this.simCam;
	}
	
	private enum DriverStationState {
		AUTONOMOUS,
		TELEOP,
		DISABLED,
	}
	
	public void setTurretPosition(double angle){
		this.encTurret.set((int) ((angle-90)*TURRET_TICKS_PER_DEGREE));
	}
	
	public void setMinArea(double area){
		this.simCam.setMinArea(area);
	}
	
	public double getDriveXPos(){
		return this.driveXPos;
	}
	
	public double getDriveYPos(){
		return this.driveYPos;
	}
	
	public DriverStation.Alliance getAllianceColour(){
		return this.alliance;
	}
	
	public double getMatchTime(){
		return this.matchTime;
	}
	
	public boolean getSpringLightSensor(){
		return !this.springLightSensor.get();
	}
	
	public long getTimeSinceAutoStarted(){
		return this.timeSinceAutonStarted;
	}
	
	public double getElevatorRPM(){
		return this.elevatorRPM;
	}
	
	public boolean getIsDriveMoving(){
		return (Math.abs(this.getDriveSpeedFPS()) > 0.1 || Math.abs(this.getGyroVelocity()) > 5.0); // returns true if the drive base is moving
	}
	
	public void setCheckingForMovement(boolean checking){
		this.hasMoved = false;
		this.checkForMovement = checking;
	}
	
	public boolean getHasDriveMoved(){
		return this.hasMoved;
	}
	
	
	public double getDistanceFromGoalInches(){
		return this.distanceFromGoalInches;
	}
	
	public void setDistanceFromGoalInches(double distance){
		this.distanceFromGoalInches = distance;
	}
	
	public double getDeltaTime(){
		return this.deltaTime;
	}
	
	public double getDrivePositionState(){
		return this.drivePositionState;
	}
	
	public double getDriveVelocityState(){
		return this.driveVelocityState;
	}
	
	
	public double getDriveAccelerationState(){
		return this.driveAccelerationState;
	}
	
	public double getDriveJerkState(){
		return this.driveJerkState;
	}
	
	public double getTurretPositionState(){
		return this.turretPositionState;
	}
	
	public double getTurretVelocityState(){
		return this.turretVelocityState;
	}
	
	
	public double getTurretAccelerationState(){
		return this.turretAccelerationState;
	}
	
	public double getGyroPositionState(){
		return this.gyroPositionState;
	}
	
	public double getGyroVelocityState(){
		return this.gyroVelocityState;
	}
	
	
	public double getGyroAccelerationState(){
		return this.gyroAccelerationState;
	}
	
	
	public double getLastTickLength() {
    	return this.deltaTime;
    }
	
	// DRIVE //
	
    
	public int getEncoderLeft() {
    	return this.encDriveLeft.get();
    }
	
	public int getEncoderLeftSpeed() {
    	return this.encDriveLeft.speed();
    }
	
	public double getLeftDriveInches(){
		return this.getEncoderLeft() / DRIVE_TICKS_PER_INCH_HIGH;
	}
	
	public double getLeftDriveFeet() {
		return this.getLeftDriveInches() / 12.0;
	}
	    
	public double getLeftDriveSpeedInches(){
		return this.getEncoderLeftSpeed() / DRIVE_TICKS_PER_INCH_HIGH;
	}
	
	public int getEncoderRight() {
    	return this.encDriveRight.get();
    }
	
	public int getEncoderRightSpeed() {
    	return this.encDriveRight.speed();
    }

    public double getRightDriveInches(){
    	return this.getEncoderRight() / DRIVE_TICKS_PER_INCH_HIGH;
    }
    
    public double getRightDriveFeet() {
		return this.getRightDriveInches() / 12.0;
	}

    public double getRightDriveSpeedInches(){
    	return this.getEncoderRightSpeed() / DRIVE_TICKS_PER_INCH_HIGH;
    }
    
    public double getDriveEncoderAverage() {
    	return (this.getEncoderRight() + this.getEncoderLeft())/2.0;
    }
    
    public double getDriveInches(){
    	return this.getDriveEncoderAverage() / DRIVE_TICKS_PER_INCH_HIGH;
    }
    
    public double getDriveEncoderSpeedAverage(){
    	return (this.getEncoderLeftSpeed() + this.getEncoderRightSpeed()) / 2.0;
    }
    
    public double getLeftDriveSpeedFPS(){
    	return this.leftDriveSpeedFPS;
    }
    
    public double getRightDriveSpeedFPS(){
    	return this.rightDriveSpeedFPS; 
    }
    
    public double getDriveFeet(){
    	return this.getDriveInches()/12.0; 
    }
    
    public double getDriveSpeedFPS(){
    	return (this.leftDriveSpeedFPS+this.rightDriveSpeedFPS)/2.0;
    }
    
    public double getLeftDriveAcceleration(){
    	return this.leftDriveAccelerationFPSSquared;
    }
    
    public double getRightDriveAcceleration(){
    	return this.rightDriveAccelerationFPSSquared;
    }
    
    public double getLeftDriveJerk(){
    	return this.leftDriveJerkFPSCubed;
    }
    
    public double getRightDriveJerk(){
    	return this.rightDriveJerkFPSCubed;
    }
    
    public double getDriveAcceleration(){
    	return (this.getLeftDriveAcceleration() + this.getRightDriveAcceleration())/2.0; 
    }
    
    public double getDriveJerk(){
    	return (this.getLeftDriveJerk() + this.getRightDriveJerk())/2.0; 
    }
 	
	// GYRO //
	
	public double getAngle(){
		if(this.usingNavX) {
			return this.navx.getAngle();
		} else {
			return 0.0;
		}
    }
	
	public double getGyroVelocity(){
		return this.gyroVelocity;
	}
    
	public double getGyroAcceleration(){
		return this.gyroAcceleration;
	}
    public double getAbsoluteAngle() {
    	if (this.usingNavX) {
    		return 360 - this.navx.getFusedHeading();
    	} else {
    		return 360 - 0.0;
    	}
    }
    
    public double getZAxisValue() {
    	if (this.usingNavX) {
    		return this.navx.getRoll();
    	} else {
    		return 0.0;
    	}
    }
    
    // PDP //
    
    public double getVoltage(){
    	return this.pdp.getVoltage();
    }
    
    public double getCurrent(int port){
    	return this.pdp.getCurrent(port);
    }
    
    // Turret // 
 
    public double getTurretAngle() { //GOTCHA: USE ACTUAL ENCODER 0 deg is right
    	return this.turretPositionState;
    }
    
}
