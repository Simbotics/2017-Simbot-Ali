package org.simbotics.frc2017.teleop;

import org.simbotics.frc2017.io.DriverInput;
import org.simbotics.frc2017.io.RobotOutput;

public class TeleopHanger implements TeleopComponent {

	private static TeleopHanger instance;
	
	private RobotOutput robotOut;
	private DriverInput driverIn;
	
	public static TeleopHanger getInstance(){
		if(instance == null){
			instance = new TeleopHanger();
		}
		return instance;
	}
	
	private TeleopHanger(){
		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
	}
	
	
	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		
		if(Math.abs(this.driverIn.getDriverHangerTrigger()) < 0.2){
			this.robotOut.setHanger(0);
		}else{
			this.robotOut.setHanger(this.driverIn.getDriverHangerTrigger());
		}
		
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.robotOut.setHanger(0);
	}
	

}
