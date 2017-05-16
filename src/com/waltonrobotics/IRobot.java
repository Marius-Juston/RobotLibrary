package com.waltonrobotics;

import com.waltonrobotics.subsystems.Drivetrain;
import com.waltonrobotics.subsystems.PoseEstimator;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Scheduler;

public abstract class IRobot extends IterativeRobot {
	private static final Preferences preferences = Preferences.getInstance();
	private static final PoseEstimator poseEstimator = new PoseEstimator();
	private static final Drivetrain drivetrain = new Drivetrain();
	private static OI oi;
	
	public abstract void OiInit(); 

	public static Drivetrain getDrivetrain() {
		return drivetrain;
	}

	public static OI getOi() {
		return oi;
	}

	public static PoseEstimator getPoseEstimator() {
		return poseEstimator;
	}

	public static Preferences getPreferences() {
		return preferences;
	}

	@Override
	public abstract void autonomousInit();

	@Override
	public void autonomousPeriodic() {

		super.autonomousPeriodic();
		displaySmartDashboardValues();
	}

	public abstract void createTestButtons();

	public abstract void displaySmartDashboardValues();

	@Override
	public void robotInit() {
		super.robotInit();

		RobotMap.init();
		Scheduler.getInstance().run();

		createTestButtons();
	}

	@Override
	public abstract void teleopInit();

	@Override
	public void teleopPeriodic() {

		super.teleopPeriodic();

		displaySmartDashboardValues();
	}
}
