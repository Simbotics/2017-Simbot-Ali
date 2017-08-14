package org.simbotics.frc2017.auton.mode.step1Modes;

import org.simbotics.frc2017.auton.drive.DriveSetHighGear;
import org.simbotics.frc2017.auton.drive.DriveSetOutput;
import org.simbotics.frc2017.auton.drive.DriveStraightProfile;
import org.simbotics.frc2017.auton.drive.DriveWait;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.pouch.PouchHoldGear;
import org.simbotics.frc2017.auton.pouch.PouchScoreGearWhenSensor;
import org.simbotics.frc2017.auton.pouch.PouchWait;
import org.simbotics.frc2017.auton.util.AutonWait;

public class ScoreGearSideStraight implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveSetHighGear(true));
		ab.addCommand(new PouchHoldGear());
		ab.addCommand(new AutonWait(40));
		
		ab.addCommand(new DriveStraightProfile(9.85, 90,7.5,0.01, 3000));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveSetOutput(0.1));
		
		ab.addCommand(new PouchScoreGearWhenSensor(1500));
		ab.addCommand(new PouchWait());
		ab.addCommand(new DriveSetOutput(0.0));
		
	}

}
