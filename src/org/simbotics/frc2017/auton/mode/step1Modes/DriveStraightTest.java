package org.simbotics.frc2017.auton.mode.step1Modes;

import org.simbotics.frc2017.auton.drive.DriveSetHighGear;
import org.simbotics.frc2017.auton.drive.DriveTurnRightUntilAngle;
import org.simbotics.frc2017.auton.drive.DriveWait;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;

public class DriveStraightTest implements AutonMode{

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveSetHighGear(true));
		//ab.addCommand(new Drive2dProfile("DriveStraightTest",false, 3, 0.01, 3000));
		ab.addCommand(new DriveTurnRightUntilAngle(45,1.0,750));
		ab.addCommand(new DriveWait());
		
		
	}

}



