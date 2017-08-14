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
import org.simbotics.frc2017.auton.turret.TurretMoveToAngle;
import org.simbotics.frc2017.auton.util.AutonWait;

public class ScoreMiddleGear implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveSetHighGear(true));
		ab.addCommand(new PouchHoldGear());
		ab.addCommand(new AutonWait(40));
		
		ab.addCommand(new TurretMoveToAngle(270, 3000));
		
		ab.addCommand(new DriveStraightProfile(6.75, 90, 0.01, 3000)); // drive to gear peg
		ab.addCommand(new DriveWait());
	
		ab.addCommand(new DriveSetOutput(0.3));
		ab.addCommand(new PouchScoreGearWhenSensor(1000));
		ab.addCommand(new PouchWait());
		
		
		
	}

}
