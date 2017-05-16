package com.waltonrobotics.command;

import com.waltonrobotics.IRobot;
import com.waltonrobotics.OI;
import com.waltonrobotics.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Drive extends Command {

	private final Drivetrain drivetrain = IRobot.getDrivetrain();
	private final OI oi = IRobot.getOi();
	private double minThrottle = 0;

	public Drive() {
		requires(drivetrain);
	}

	private void cheesyDrive() {
		final double throttle = (getLeftThrottle() + 1) / 2;
		final double forward = -getRightThrottle();
		final double turn = getTurn();
		drivetrain.setSpeeds(throttle * (forward + turn), throttle * (forward - turn));
		// System.out.println("Cheesy Error Stuff: throttle: "+throttle+";
		// forward: "+forward+"; turn: "+turn+"; speeds:
		// ("+RobotMap.left.get()+", "+(-RobotMap.right.get())+")");
	}

	// Called repeatedly when this Command is scheduled to run
	// change
	@Override
	protected void execute() {
		if (oi.getShiftUp().get()) {
			drivetrain.shiftUp();
		} else if (oi.getShiftDown().get()) {
			drivetrain.shiftDown();
		}

		// TODO make this better make it so that the enums have their own
		// functions
		switch (drivetrain.getDriver()) {
		case Tank:
			tankDrive();
			break;
		case Cheesy:
			cheesyDrive();
			break;
		case Robert:
			robertDrive();
			break;
		}
	}

	public double getLeftThrottle() {
		if (Math.abs(oi.getLeft().getY()) < 0.3) {
			return 0;
		}
		return oi.getLeft().getY();
	}

	public double getLeftTurn() {
		if (Math.abs(oi.getLeft().getX()) < 0.1) {
			return 0;
		}
		return oi.getLeft().getX();

	}

	public double getRightThrottle() {
		if (Math.abs(oi.getRight().getY()) < 0.3) {
			return 0;
		}
		return oi.getRight().getY();

	}

	public double getTurn() {
		if (Math.abs(oi.getRight().getX()) < 0.1) {
			return 0;
		}
		return oi.getRight().getX();

	}

	public double getZThrottle() {
		if (Math.abs(oi.getLeft().getZ()) < 0.3) {
			return 0;
		}
		return oi.getRight().getZ();

	}

	@Override
	protected void initialize() {
		drivetrain.setDriver();

		updateMinThrottle();
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	private void robertDrive() {
		double throttle = Math.pow((oi.getLeft().getAxis(Joystick.AxisType.kZ) + 1) / 2, 2);

		// used for testing
		// updateMinThrottle();
		throttle = Math.max(minThrottle, throttle);

		final double forward = -getLeftThrottle();
		final double turn = getLeftTurn();
		drivetrain.setSpeeds(throttle * (forward + turn), throttle * (forward - turn));
	}

	private void tankDrive() {
		drivetrain.setSpeeds(-getLeftThrottle(), -getRightThrottle());
	}

	private void updateMinThrottle() {
		minThrottle = Preferences.getInstance().getDouble("drivetrain.minThrottle", 0.3);
	}
}
