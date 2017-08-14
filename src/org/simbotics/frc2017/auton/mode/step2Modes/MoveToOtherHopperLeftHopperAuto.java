package org.simbotics.frc2017.auton.mode.step2Modes;

import org.simbotics.frc2017.auton.AutonOverride;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.auton.drive.DriveSetOutput;
import org.simbotics.frc2017.auton.drive.DriveStraightProfile;
import org.simbotics.frc2017.auton.drive.DriveTurnToAngleLeftSide;
import org.simbotics.frc2017.auton.drive.DriveTurnToAnglePID;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.turret.TurretMoveToAngle;
import org.simbotics.frc2017.auton.util.AutonWaitUntilAutoTime;


public class MoveToOtherHopperLeftHopperAuto implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		
		ab.addCommand(new TurretMoveToAngle(90, 1500));
		ab.addCommand(new DriveTurnToAngleLeftSide(270,5,1000));
		ab.addCommand(new DriveStraightProfile(-9.5, 270, 0.01, 1500));
		ab.addCommand(new DriveTurnToAnglePID(360,2,1000));
		ab.addCommand(new DriveStraightProfile(16.0, 360, 0.01, 2000));
		ab.addCommand(new AutonWaitUntilAutoTime(14700));
		ab.addCommand(new AutonOverride(RobotComponent.DRIVE));
		ab.addCommand(new DriveSetOutput(-0.3));
		
	}

}
