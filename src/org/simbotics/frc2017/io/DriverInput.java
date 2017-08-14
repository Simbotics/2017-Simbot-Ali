package org.simbotics.frc2017.io;

import org.simbotics.frc2017.util.LogitechF310Gamepad;

public class DriverInput {

	private static DriverInput instance;
	
	private LogitechF310Gamepad driver;
	private LogitechF310Gamepad operator;
	
	private boolean autonIncreaseStepWasPressed = false;
    private boolean autonDecreaseStepWasPressed = false;
    
    private boolean autonIncreaseModeWasPressed = false;
    private boolean autonDecreaseModeWasPressed = false;
    
    private boolean autonIncreaseMode10WasPressed = false;
    private boolean autonDecreaseMode10WasPressed = false;
    
	
	
	private DriverInput(){
		this.driver = new LogitechF310Gamepad(0);
		this.operator = new LogitechF310Gamepad(1);
	}
	
	public static DriverInput getInstance(){
		if(instance == null){
			instance = new DriverInput();
		}
		return instance;
	}
	
	//DRIVER CONTROLS 
	public double getDriverX(){
		return this.driver.getLeftX();
	}
	
	public double getDriverY(){
		return this.driver.getLeftY();
	}
	
	public double getDriverRightX(){
		return this.driver.getRightX();
	}
	
	public double getDriverRightY(){
		return this.driver.getRightY();
	}
	
    
    public boolean getDriverLowGearButton(){
    	return this.driver.getLeftTrigger() > 0.3;
    }
    
    public double getDriverHangerTrigger(){
    	return this.driver.getRightTrigger();
    }
    
    public boolean getDriverOutputButton(){
    	return this.driver.getBlueButton();
    }
    
    public boolean getDriverVelocityButton(){
    	return this.driver.getYellowButton();
    }
    
    public boolean getDriverScoreGearButton(){
    	return this.driver.getRightBumper();
    }
    
    public boolean getDriverGetGearButton(){
    	return this.driver.getLeftBumper();
    }
    
    public boolean getDriverManualScoreGearButton(){
    	return this.driver.getRedButton();
    }
    
    //SHOOTER CONTROLS

    public boolean getOperatorStartButton(){
    	return this.operator.getStartButton();
    }
    
    public boolean getOperatorBallRampButton(){
    	return this.operator.getRightBumper();
        	
    }
    
    
    public boolean getShooterOffButton(){
    	return this.operator.getRightTrigger() > 0.3;
    }
    
    public boolean getTurretLeftButton(){
    	return this.operator.getBlueButton();
    }
    
    public boolean getTurretMidButton(){
    	return this.operator.getYellowButton();
    }
    
    public boolean getTurretRightButton(){
    	return this.operator.getRedButton();
    }
    
    public boolean getTurretBackButton(){
    	return this.operator.getGreenButton();
    }
    
    public boolean getTurretCameraAimButton(){
    	return this.operator.getPOVDown();
    }
    
    public double getTurretManualStick(){
    	double x = this.operator.getLeftX();
    	if(Math.abs(x) <= 0.2){
    		x=0;
    	}
    	return x;
    }
    
    public boolean getFeederShootButton(){
    	return this.operator.getLeftTrigger() > 0.3;
    }
    
    public boolean getFeedReverseButton(){
    	return this.operator.getLeftBumper();
    }
    
    
	//AUTO SELECTION CONTROLS
	
	public boolean getAutonSetDelayButton(){
		return this.driver.getRightTrigger() > 0.3;
	}
	
	public double getAutonDelayStick(){
		return this.driver.getLeftY();
	}
	
	
	public boolean getAutonStepIncrease() {
    	// only returns true on rising edge
    	boolean result = this.driver.getRightBumper() && !this.autonIncreaseStepWasPressed;
    	this.autonIncreaseStepWasPressed = this.driver.getRightBumper();
    	return result;
    	
    }
	
	public boolean getAutonStepDecrease() {
    	// only returns true on rising edge
    	boolean result = this.driver.getLeftBumper() && !this.autonDecreaseStepWasPressed;
    	this.autonDecreaseStepWasPressed = this.driver.getLeftBumper();
    	return result;
    
    }
	
	public boolean getAutonModeIncrease() {
    	// only returns true on rising edge
    	boolean result = this.driver.getRedButton() && !this.autonIncreaseModeWasPressed;
    	this.autonIncreaseModeWasPressed = this.driver.getRedButton();
    	return result;
    	
    }
    
    public boolean getAutonModeDecrease() {
    	// only returns true on rising edge
    	boolean result = this.driver.getGreenButton() && !this.autonDecreaseModeWasPressed;
    	this.autonDecreaseModeWasPressed = this.driver.getGreenButton();
    	return result;
    
    }
    
    public boolean getAutonModeIncreaseBy10() {
    	// only returns true on rising edge
    	boolean result = this.driver.getYellowButton() && !this.autonIncreaseMode10WasPressed;
    	this.autonIncreaseMode10WasPressed = this.driver.getYellowButton();
    	return result;
    	
    }
    
    public boolean getAutonModeDecreaseBy10() {
    	// only returns true on rising edge
    	boolean result = this.driver.getBlueButton() && !this.autonDecreaseMode10WasPressed;
    	this.autonDecreaseMode10WasPressed = this.driver.getBlueButton();
    	return result;
    
    }
	
	
	
	
	
	
}