package com.waltonrobotics.subsystems;

import com.waltonrobotics.IRobotMap;
import com.waltonrobotics.controllers.Point2D;
import com.waltonrobotics.controllers.Pose;
import com.waltonrobotics.controllers.PoseProvider;
import com.waltonrobotics.controllers.RobotPair;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoseEstimator extends Subsystem implements PoseProvider {

	private static final double distancePerPulse = 0.0005814; // Pracice bot
	// private static final double wheelDistance = .70485; // practice bot

	private final Encoder encoderLeft = IRobotMap.getEncoderLeft();
	private final Encoder encoderRight = IRobotMap.getEncoderRight();
	// private RobotPair wheelPositions;
	private double angle;
	private Point2D X;

	public PoseEstimator() {

		synchronized (this) {
			// 0.0024936392
			X = new Point2D(0, 0);
			angle = 0;
			encoderLeft.setDistancePerPulse(distancePerPulse);
			encoderRight.setDistancePerPulse(-distancePerPulse);
			reset();
			// wheelPositions = new RobotPair(encoderLeft.getDistance(),
			// encoderRight.getDistance());

		}

		// RobotLoggerManager.setFileHandlerInstance("robot.subsystems").info("PoseEstimator
		// is created.");
	}

	public synchronized void dumpSmartdashboardValues() {
		SmartDashboard.putNumber("EncoderLeft", encoderLeft.getDistance());
		SmartDashboard.putNumber("EncoderRight", encoderRight.getDistance());

		SmartDashboard.putNumber("VelocityLeft", encoderLeft.getRate());
		SmartDashboard.putNumber("VelocityRight", encoderRight.getRate());
	}

	@Override
	public synchronized Pose getPose() {
		return new Pose(X, angle);
	}

	@Override
	public synchronized RobotPair getWheelPositions() {
		return new RobotPair(encoderLeft.getDistance(), encoderRight.getDistance());
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public synchronized void reset() {
		encoderLeft.reset();
		encoderRight.reset();
	}

}
