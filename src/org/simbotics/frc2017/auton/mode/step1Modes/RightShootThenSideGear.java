
package org.simbotics.frc2017.auton.mode.step1Modes;

import org.simbotics.frc2017.auton.AutonOverride;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.auton.drive.DriveSetOutput;
import org.simbotics.frc2017.auton.drive.DriveStraightProfile;
import org.simbotics.frc2017.auton.drive.DriveTurnToAnglePID;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.pouch.PouchScoreGearWhenSensor;
import org.simbotics.frc2017.auton.pouch.PouchWait;
import org.simbotics.frc2017.auton.shooter.ShooterSetRPM;
import org.simbotics.frc2017.auton.shooter.ShooterShootAndFeedTimer;
import org.simbotics.frc2017.auton.turret.TurretAim;
import org.simbotics.frc2017.auton.turret.TurretMoveToAngle;
import org.simbotics.frc2017.auton.turret.TurretSetPosition;
import org.simbotics.frc2017.auton.turret.TurretWait;
import org.simbotics.frc2017.auton.util.AutonWait;
import org.simbotics.frc2017.auton.util.AutonWaitUntilAutoTime;
import org.simbotics.frc2017.util.RobotConstants;

public class RightShootThenSideGear implements AutonMode{

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new TurretSetPosition(-9.5));
		ab.addCommand(new ShooterSetRPM(RobotConstants.ADJUSTABLE_RPM-300, 0.0));
		ab.addCommand(new TurretAim(-9.5, 1500));
		ab.addCommand(new TurretWait());
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterShootAndFeedTimer(RobotConstants.ADJUSTABLE_RPM-300,700,700,3500,true,-1));	
		ab.addCommand(new AutonWait(3000));
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		
		ab.addCommand(new TurretMoveToAngle(90, 2000));
		ab.addCommand(new DriveStraightProfile(100.0 / 12.0, 90, 9.0, 0.01, 3000));
		ab.addCommand(new DriveTurnToAnglePID(145, 0.01, 1000));
		
		ab.addCommand(new DriveStraightProfile(35 / 12.0, 145, 4.0, 0.01, 2000));
		ab.addCommand(new PouchScoreGearWhenSensor(2500));
		ab.addCommand(new DriveSetOutput(0.2));
		ab.addCommand(new PouchWait());
		
		
		ab.addCommand(new DriveStraightProfile(-26 / 12.0, 130,14.0, 0.3, 1.0, 1500));
		ab.addCommand(new DriveTurnToAnglePID(75,1.0,500));
		ab.addCommand(new DriveStraightProfile(184 / 12.0, 75, 14.0, 0.01, 3700));
		
		ab.addCommand(new AutonWaitUntilAutoTime(14750));
		ab.addCommand(new AutonOverride(RobotComponent.DRIVE));
		ab.addCommand(new DriveSetOutput(-0.3));
		ab.addCommand(new AutonWait(200));
		ab.addCommand(new DriveSetOutput(0.0));
		
		
		
	}

}



