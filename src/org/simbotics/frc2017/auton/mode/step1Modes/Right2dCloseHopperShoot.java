package org.simbotics.frc2017.auton.mode.step1Modes;

import org.simbotics.frc2017.auton.AutonOverride;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.auton.drive.Drive2dProfile;
import org.simbotics.frc2017.auton.drive.DriveSetHighGear;
import org.simbotics.frc2017.auton.drive.DriveSetOutputSides;
import org.simbotics.frc2017.auton.drive.DriveStraightProfile;
import org.simbotics.frc2017.auton.drive.DriveWait;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.pouch.SetBallRamp;
import org.simbotics.frc2017.auton.shooter.ShooterSetRPM;
import org.simbotics.frc2017.auton.shooter.ShooterShootAndFeedTimer;
import org.simbotics.frc2017.auton.turret.TurretAim;
import org.simbotics.frc2017.auton.turret.TurretMoveToAngle;
import org.simbotics.frc2017.auton.turret.TurretWait;
import org.simbotics.frc2017.auton.util.AutonWait;
import org.simbotics.frc2017.auton.util.AutonWaitUntilAutoTime;
import org.simbotics.frc2017.util.RobotConstants;

public class Right2dCloseHopperShoot implements AutonMode{

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveSetHighGear(true));
		ab.addCommand(new TurretMoveToAngle(10, 1500));
		ab.addCommand(new ShooterSetRPM(RobotConstants.ADJUSTABLE_RPM, 0.0));
		ab.addCommand(new SetBallRamp(true));
		
		ab.addCommand(new Drive2dProfile("RightHopperCurve",false, 8, 0.01, 1900));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new TurretAim(10, 3200));
		ab.addCommand(new DriveSetHighGear(false));
		ab.addCommand(new DriveStraightProfile(20 / 12.0, 0, 0.01, 1200));
	
		
		ab.addCommand(new AutonWait(400));
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterShootAndFeedTimer(RobotConstants.ADJUSTABLE_RPM,700,700,12000,false,-1));	
		
		
		ab.addCommand(new DriveSetOutputSides(0.30,0.3));
		
		ab.addCommand(new TurretWait());
		//ab.addCommand(new DriveSetOutputSides(0.0,0.0));
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		
		
		ab.addCommand(new ShooterShootAndFeedTimer(RobotConstants.ADJUSTABLE_RPM,700,700,12000,true,-1));		
		ab.addCommand(new AutonWaitUntilAutoTime(12000));
		ab.addCommand(new DriveSetHighGear(true));
	}

}



