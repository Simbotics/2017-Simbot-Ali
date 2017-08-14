package org.simbotics.frc2017;

import org.simbotics.frc2017.auton.AutonControl;
import org.simbotics.frc2017.io.DriverInput;
import org.simbotics.frc2017.io.Logger;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.teleop.TeleopControl;
import org.simbotics.frc2017.util.Debugger;
import org.simbotics.frc2017.util.RobotConstants;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

	private RobotOutput robotOut;
	private DriverInput driverInput;
	private SensorInput sensorInput;
	private TeleopControl teleopControl;
    private Logger logger;
    
    
    private boolean pushToDashboard = true; 
      
	@Override
	public void robotInit() {
		if(this.pushToDashboard){ 
			RobotConstants.pushValues();
		}		
		this.robotOut = RobotOutput.getInstance();
		this.driverInput = DriverInput.getInstance();
		this.sensorInput = SensorInput.getInstance();
		this.teleopControl = TeleopControl.getInstance();
		this.logger = Logger.getInstance();
		
		// Debugger init - set your flag here (or turn off others!)
		Debugger.defaultOn();
		Debugger.flagOn("CAMERA");
		Debugger.println("CAMERA FLAG ON", "CAMERA");
		
		
	}

	@Override
	public void autonomousInit() {
		AutonControl.getInstance().initialize();
		SensorInput.getInstance().reset();
		SensorInput.getInstance().resetAutonTimer();
		this.logger.openFile();
	}
	
	@Override
	public void autonomousPeriodic() {
		this.sensorInput.update();
    	AutonControl.getInstance().runCycle();
    	this.logger.logAll();
	}

	@Override
	public void teleopInit() {
		Debugger.println("TELEOP INIT");
	}
	
	@Override
	public void teleopPeriodic() {
		this.sensorInput.update();
    	this.teleopControl.runCycle();
	}
	
	@Override
	public void disabledInit() {
		this.robotOut.stopAll();
    	this.teleopControl.disable();
    	this.logger.close();
	}

	@Override
	public void disabledPeriodic() {
		this.sensorInput.update();
    	AutonControl.getInstance().updateModes();
	}
}

