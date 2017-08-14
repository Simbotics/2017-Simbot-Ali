package org.simbotics.frc2017.auton.mode.step1Modes;

import org.simbotics.frc2017.auton.AutonOverride;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.shooter.ShooterSetRPM;
import org.simbotics.frc2017.auton.shooter.ShooterShootAndFeedTimer;
import org.simbotics.frc2017.auton.turret.TurretAim;
import org.simbotics.frc2017.auton.turret.TurretMoveToAngle;
import org.simbotics.frc2017.auton.turret.TurretWait;
import org.simbotics.frc2017.auton.util.AutonWaitUntilAutoTime;
import org.simbotics.frc2017.util.RobotConstants;

public class ShootingTest implements AutonMode{

	@Override
	public void addToMode(AutonBuilder ab) {
		
		ab.addCommand(new TurretMoveToAngle(10, 1500));
		ab.addCommand(new ShooterSetRPM(RobotConstants.ADJUSTABLE_RPM, 0.0));
		
		ab.addCommand(new TurretAim(10,2500));
		ab.addCommand(new TurretWait());

		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterShootAndFeedTimer(RobotConstants.ADJUSTABLE_RPM, 700,700, 12000, true, -1));
		ab.addCommand(new AutonWaitUntilAutoTime(15000));
		
	}

}



