package org.simbotics.frc2017.auton.mode.step1Modes;

import org.simbotics.frc2017.auton.drive.Drive2dProfile;
import org.simbotics.frc2017.auton.drive.DriveSetHighGear;
import org.simbotics.frc2017.auton.drive.DriveStraightProfile;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.pouch.PouchScoreGearWhenSensor;
import org.simbotics.frc2017.auton.pouch.PouchWait;

public class LeftScoreSideGear implements AutonMode{

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveSetHighGear(true));
		ab.addCommand(new Drive2dProfile("LeftSideGearCurve",false, 8, 0.01,0.012 ,2500));
		ab.addCommand(new PouchScoreGearWhenSensor(3500));
		ab.addCommand(new DriveStraightProfile(6 / 12.0, 30, 0.01, 500));
		ab.addCommand(new PouchWait());
		
		
		
		
	}

}



