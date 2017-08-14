package org.simbotics.frc2017.teleop;

import org.simbotics.frc2017.io.DriverInput;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.util.SimBezierCurve;
import org.simbotics.frc2017.util.SimDriveControl;
import org.simbotics.frc2017.util.SimDriveControl.SimDriveMode;
import org.simbotics.frc2017.util.SimPoint;

public class TeleopDrive implements TeleopComponent {

	private static TeleopDrive instance;

	private RobotOutput robotOut;
	private DriverInput driverIn;

	private SimBezierCurve SimCurveXH;
	private SimBezierCurve SimCurveYH;
	private SimDriveControl driveControl; 

	
	
	public static TeleopDrive getInstance() {
		if (instance == null) {
			instance = new TeleopDrive();
		}
		return instance;
	}

	private TeleopDrive() {

		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		
		this.driveControl = new SimDriveControl();

		
		SimPoint zero = new SimPoint(0, 0);
		//High gear
		SimPoint xP1H = new SimPoint(0.0, 0.20);
		SimPoint xP2H = new SimPoint(0.55, -0.1);

		SimPoint yP1H = new SimPoint(0.0, 0.54);
		SimPoint yP2H = new SimPoint(0.45, -0.07);
		
		SimPoint one = new SimPoint(1, 1);
		
		this.SimCurveXH = new SimBezierCurve(zero, xP1H, xP2H, one);
		this.SimCurveYH = new SimBezierCurve(zero, yP1H, yP2H, one);
		
		
	}


	@Override
	public void calculate() {

		// driver inputs for drive
		if(this.driverIn.getDriverLowGearButton()) {
			this.robotOut.setHighGear(false);
		} else{
			this.robotOut.setHighGear(true);
		}
		
		if(this.driverIn.getDriverOutputButton()){
			this.driveControl.setDriveMode(SimDriveMode.OUTPUT);
		}else if(this.driverIn.getDriverVelocityButton()){
			this.driveControl.setDriveMode(SimDriveMode.VELOCITY);
		}
		
		double x;
		double y;
		
		//Deadzone Calculation
		if(Math.abs(this.driverIn.getDriverRightX()) < 0.05){
			x = 0;
		}else{
			x = this.SimCurveXH.getPoint(this.driverIn.getDriverRightX()).getY();
		}
		
		if(Math.abs(this.driverIn.getDriverY())  < 0.1){
			y = 0;
		}else{
			y = this.SimCurveYH.getPoint(this.driverIn.getDriverY()).getY();
		}
		
		if(this.driverIn.getDriverGetGearButton()){
			if(Math.abs(x) < 0.05 && Math.abs(y) < 0.1){ // not trying to move or turn
				x = 0;
				y = 0.0; // slow forwards movement 
			}
		}
		
		this.driveControl.setDrive(x, y);
	}

	@Override
	public void disable() {
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

	
}
