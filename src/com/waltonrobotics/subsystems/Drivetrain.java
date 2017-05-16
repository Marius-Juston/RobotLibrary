package com.waltonrobotics.subsystems;

import com.waltonrobotics.IRobot;
import com.waltonrobotics.IRobotMap;
import com.waltonrobotics.command.Drive;
import com.waltonrobotics.controllers.MotionProfileController;
import com.waltonrobotics.controllers.MotionProvider;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends Subsystem {

	public enum Driver {
		Robert, Tank, Cheesy
	}

	private static final double PERIOD = .005;
	private static final double DEFAULTKV = 0.5;
	private static final double DEFAULTKK = 0;
	private static final double DEFAULTKA = 0.1;
	private static final double DEFAULTKP = 20;

	private final Talon right = IRobotMap.getRight();

	private final Talon left = IRobotMap.getLeft();
	private final Solenoid shifter = IRobotMap.getPneumaticsShifter();
	private Driver driver;
	private final MotionProfileController controller;

	public Drivetrain() {
		controller = new MotionProfileController(IRobot.getPoseEstimator(), PERIOD, this);
		setConstants();
		setDriver();
	}

	public void addControllerMotion(final MotionProvider motion) {
		controller.addMotion(motion);
	}

	public void cancelMotion() {
		controller.cancel();
	}

	public Driver convertedDriver(final String driver) {
		return Driver.valueOf(driver);
	}

	public void dumpSmartdashboardValues() {
		SmartDashboard.putNumber("Power Left", left.get());
		SmartDashboard.putNumber("Power Right", right.get());

	}

	public boolean getControllerStatus() {
		return controller.getEnabled();
	}

	public Driver getDriver() {
		return driver;
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new Drive());
	}

	public boolean isControllerFinished() {
		return controller.isFinished();
	}

	public void setConstants() {
		final Preferences pref = IRobot.getPreferences();
		final double kV = pref.getDouble("drivetrain.kV", DEFAULTKV);
		final double kK = pref.getDouble("drivetrain.kK", DEFAULTKK);
		final double kA = pref.getDouble("drivetrain.kA", DEFAULTKA);
		final double kP = pref.getDouble("drivetrain.kP", DEFAULTKP);
		// System.out.println(String.format("kV=%f, kK=%f, kA=%f, kP=%f", kV,
		// kK, kA, kP));
		controller.setKV(kV);
		controller.setKK(kK);
		controller.setKA(kA);
		controller.setKP(kP);

	}

	public void setDriver() {
		driver = convertedDriver(IRobot.getPreferences().getString("drivetrain.driver", "Robert"));
	}

	public synchronized void setSpeeds(final double leftSpeed, final double rightSpeed) {
		right.set(rightSpeed);
		left.set(-leftSpeed);
	}

	public synchronized boolean shiftDown() {
		if (!shifter.get()) {
			shifter.set(true);
			return true;
		}

		return false;
	}

	public synchronized boolean shiftUp() {
		if (shifter.get()) {
			shifter.set(false);
			return true;
		}
		return false;
	}

	public synchronized void startMotion() {
		controller.enable();
	}
}
