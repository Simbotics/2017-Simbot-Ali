package org.simbotics.frc2017.io;

import org.simbotics.frc2017.util.RobotConstants;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class RobotOutput {
	private static RobotOutput instance;
	
	private VictorSP driveLeftFront;
	private VictorSP driveLeftBack;
	private VictorSP driveRightFront;
	private VictorSP driveRightBack; 

	private VictorSP hanger1;
	private VictorSP hanger2;
	
	private CANTalon shooterMaster;
	private CANTalon shooterSlave1;
	private CANTalon shooterSlave2;
	private CANTalon shooterSlave3;
	
	private VictorSP turret; 
	
	private VictorSP blender;
	private VictorSP elevator; 
	
	private Solenoid lowGear;
	private Solenoid highGear;
	
	private Solenoid pouchPunchOut;
	private Solenoid pouchPunchIn;
	
	private Solenoid pouchRamp;
	private Solenoid pouchClampOpen;
	private Solenoid pouchClampClose;
	private Solenoid ballRamp;	
	
	private Relay cameraLight;
	
	
	
	private RobotOutput(){ // GOTCHA: change this back if pushing
		this.driveLeftFront = new VictorSP(0);  // 2
		this.driveLeftBack = new VictorSP(1);   // 3
		this.driveRightFront = new VictorSP(2); // 0
		this.driveRightBack = new VictorSP(3);  // 1
		
		this.hanger1 = new VictorSP(4);
		this.hanger2 = new VictorSP(5);
		
		this.shooterMaster = new CANTalon(1,20); // non 0 so that any new device wont be set to the shooter by accident 
		this.shooterSlave1 = new CANTalon(2,20); // control period is 20ms instead of default 10ms to prevent CAN Bus 100% utilization
		this.shooterSlave2 = new CANTalon(3,20);
		this.shooterSlave3 = new CANTalon(4,20);
		this.configureShooterTalons();
		
		this.blender = new VictorSP(6);
		this.elevator = new VictorSP(7); 
		this.turret = new VictorSP(8); 
		
		this.lowGear = new Solenoid(0);
		this.highGear = new Solenoid(1);
		
		this.pouchPunchOut = new Solenoid(2);
		this.pouchPunchIn = new Solenoid(3);
		
		this.pouchClampOpen = new Solenoid(4);
		this.pouchClampClose = new Solenoid(5);
		
		this.ballRamp = new Solenoid(7);
		this.pouchRamp = new Solenoid(6);
		this.cameraLight = new Relay(0);
		
			
	}
	
	public static RobotOutput getInstance(){
		if(instance == null){
			instance = new RobotOutput();
		}
		return instance;
	}
	
	//Shooter Configuration 
	private void configureShooterTalons(){
		this.shooterMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		
		this.shooterMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		this.shooterSlave1.changeControlMode(CANTalon.TalonControlMode.Follower);
		this.shooterSlave2.changeControlMode(CANTalon.TalonControlMode.Follower);
		this.shooterSlave3.changeControlMode(CANTalon.TalonControlMode.Follower);
		
		this.shooterSlave1.set(1); // will follow id 1
		this.shooterSlave2.set(1);
		this.shooterSlave3.set(1);
		
		
		this.shooterMaster.reverseSensor(true);
		
		this.shooterMaster.reverseOutput(false); 
		this.shooterSlave1.reverseOutput(true);
		this.shooterSlave2.reverseOutput(true); 
		this.shooterSlave3.reverseOutput(true); //switch for practice bot 
		
		
       // this.configureShooterPID(RobotConstants.shooterP, RobotConstants.shooterI, RobotConstants.shooterD,RobotConstants.shooterFF,(int) RobotConstants.shooterEps * 5);

		
		this.shooterMaster.setVoltageRampRate(84.0); // might need to be changed
		this.shooterSlave1.setVoltageRampRate(84.0);
		this.shooterSlave2.setVoltageRampRate(84.0);
		this.shooterSlave3.setVoltageRampRate(84.0);
		
		this.shooterMaster.SetVelocityMeasurementPeriod(CANTalon.VelocityMeasurementPeriod.Period_10Ms);
		this.shooterMaster.SetVelocityMeasurementWindow(1);
		
		this.shooterMaster.configNominalOutputVoltage(0.0,0);
		this.shooterMaster.configPeakOutputVoltage(12.5, 0); // will never go backwards
		
		this.shooterSlave1.configNominalOutputVoltage(0.0, 0.0);
		this.shooterSlave1.configPeakOutputVoltage(12.5, 0); // will never go backwards
		
		this.shooterSlave2.configNominalOutputVoltage(0.0, 0.0);
		this.shooterSlave2.configPeakOutputVoltage(12.5, 0); // will never go backwards
		
		this.shooterSlave3.configNominalOutputVoltage(0.0, 0.0);
		this.shooterSlave3.configPeakOutputVoltage(12.5, 0); // will never go backwards
		
		
		this.shooterMaster.enableBrakeMode(false);
		this.shooterSlave1.enableBrakeMode(false);
		this.shooterSlave2.enableBrakeMode(false);
		this.shooterSlave3.enableBrakeMode(false);
		
		//this.shooterMaster.configEncoderCodesPerRev(RobotConstants.shooterCountsPerRev); // ticks per rev may need to *4 because is a quad encoder
		
		this.configureShooterPID(RobotConstants.shooterP, RobotConstants.shooterI, RobotConstants.shooterD,RobotConstants.shooterFF,(int) RobotConstants.shooterEps * 5);
		
		this.shooterMaster.clearStickyFaults();
		this.shooterSlave1.clearStickyFaults();
		this.shooterSlave2.clearStickyFaults();
		this.shooterSlave3.clearStickyFaults();
		
	}
	
	//Shooter Methods
	public void configureShooterPID(double p, double i, double d, double velFF, int iRange){
		this.shooterMaster.setPID(p, i, d, velFF, iRange,0, 0);
		this.shooterMaster.setIZone(iRange);
		this.shooterMaster.setProfile(0);
	}
	
	public double getShooterRPM(){
		return this.shooterMaster.getSpeed();
	}
	
	public void setShooterTarget(double rpm){
		this.shooterMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
		this.shooterMaster.set(rpm);
	}
	
	public void setShooterOutput(double output){
		this.shooterMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		this.shooterMaster.set(output);
	}
	
	public double getShooterTarget(){
		return this.shooterMaster.getSetpoint();
	}
	
	public boolean isShooterWithinEps(double eps){
		return (Math.abs(this.getShooterRPM() - this.getShooterTarget()) <= eps);
	}
	
	
	//Motor Commands
	
	
	//Drive
	public void setDriveLeft(double output){
		this.driveLeftFront.set(output);
		this.driveLeftBack.set(output);
		
	}
	
	public void setDriveRight(double output){
		this.driveRightFront.set(-output);
		this.driveRightBack.set(-output);
		
	}
	
	//Hanger 
	
	public void setHanger(double output){
		if(output < 0){ // don't run the hanger backwards 
			output = 0;
		}
		this.hanger1.set(-output); //switch for practice
		this.hanger2.set(-output);
	}
	
	//Feeder
	
	public void setBlender(double output){
		this.blender.set(output);
	}
	
	public void setElevator(double output){
		this.elevator.set(-output); 
	}
	
	//Shooter
	public void setCameraLight(boolean on){
		if(on){
			this.cameraLight.set(Relay.Value.kForward);
		}else{
			this.cameraLight.set(Relay.Value.kOff);
		}
	}
	
	
	public void setTurret(double output){
		this.turret.set(output);
	}
	
	
	//Solenoid Commands 

	public void setHighGear(boolean isHigh){
		this.lowGear.set(isHigh);
		this.highGear.set(!isHigh);
	}
	
	public void setPouchRamp(boolean isOut){
		this.pouchRamp.set(isOut);
	}
	
	public void setPouchClamp(boolean isOpen){
		this.pouchClampClose.set(!isOpen);
		this.pouchClampOpen.set(isOpen);
	}
	
	public void setPouchPunch(boolean isOut){
		this.pouchPunchIn.set(isOut);
		this.pouchPunchOut.set(!isOut);
	}
	
	public void setBallRamp(boolean isOut){
		this.ballRamp.set(isOut);
	}
	
	public boolean getHighGear(){
		return !this.highGear.get();
	}
	
	
	
	public void stopAll() {
    	setDriveLeft(0);
    	setDriveRight(0);
    	setHanger(0);
    	setBlender(0);
    	setElevator(0);
    	setShooterOutput(0);
    	setTurret(0);
    	setCameraLight(false);
    	// shut off things here
    }
	
	
}
