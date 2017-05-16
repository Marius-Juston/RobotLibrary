package com.waltonrobotics.controllers;

import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.waltonrobotics.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;

public class MotionProfileController {
	private class MPCTask extends TimerTask {

		@Override
		public void run() {
			calculate();
		}

	}

	private double kV = 0, kK = 0, kA = 0, kP = 0;
	private Kinematics currentKinematics = null;
	private KinematicPose staticKinematicPose;
	private final BlockingDeque<MotionProvider> motions = new LinkedBlockingDeque<>();
	private final PoseProvider poseProvider;
	private final java.util.Timer controller;
	private boolean isEnabled;

	private final int nPoints = 50;
	private final Drivetrain drivetrain;

	public MotionProfileController(final PoseProvider poseProvider, final double period, final Drivetrain drivetrain) {
		this.drivetrain = drivetrain;

		this.poseProvider = poseProvider;
		controller = new java.util.Timer();

		controller.schedule(new MPCTask(), 0L, (long) (period * 1000));

		staticKinematicPose = Kinematics.staticPose(poseProvider.getPose(), poseProvider.getWheelPositions(),
				Timer.getFPGATimestamp());
	}

	public synchronized void addMotion(final MotionProvider motion) {
		motions.addLast(motion);
		// System.out.println("added motion" + motion.toString());
	}

	private void calculate() {

		double leftPower = 0;
		double rightPower = 0;
		boolean enabled;

		synchronized (this) {
			enabled = isEnabled;
		}

		if (enabled) {

			final double time = Timer.getFPGATimestamp();

			final RobotPair wheelPositions = poseProvider.getWheelPositions();

			KinematicPose kinematicPose;
			if (currentKinematics != null) {
				kinematicPose = currentKinematics.interpolatePose(time);
			} else {
				kinematicPose = staticKinematicPose;
			}

			// System.out.println("time:" + time+ " " + wheelPositions + " " +
			// kinematicPose);
			synchronized (this) {
				// feed forward
				leftPower += kV * kinematicPose.getLeft().v + kK + kA * kinematicPose.getLeft().a;
				rightPower += kV * kinematicPose.getRight().v + kK + kA * kinematicPose.getRight().a;
				// feed back
				leftPower += kP * (kinematicPose.getLeft().l - wheelPositions.getLeft());
				rightPower += kP * (kinematicPose.getRight().l - wheelPositions.getRight());

			}

			leftPower = Math.max(-1, Math.min(1, leftPower));
			rightPower = Math.max(-1, Math.min(1, rightPower));

			// System.out.println(String.format("LP=%f,RP=%f, err_l=%f,
			// err_r=%f", leftPower,rightPower,
			// kinematicPose.left.l - wheelPositions.left,
			// kinematicPose.right.l - wheelPositions.right));

			drivetrain.setSpeeds(leftPower, rightPower);

			if (kinematicPose.isFinished()) {
				final MotionProvider newMotion = motions.pollFirst();
				if (newMotion != null) {
					currentKinematics = new Kinematics(newMotion, currentKinematics.getWheelPositions(),
							currentKinematics.getTime(), 0, 0, nPoints);
					// System.out.println("starting new motion:" +
					// currentKinematics.toString());
				} else {
					staticKinematicPose = Kinematics.staticPose(currentKinematics.getPose(),
							currentKinematics.getWheelPositions(), currentKinematics.getTime());
					currentKinematics = null;
				}
			}
		}
	}

	public synchronized void cancel() {
		isEnabled = false;
		currentKinematics = null;
		motions.clear();
		drivetrain.setSpeeds(0, 0);
	}

	public synchronized void enable() {
		// System.out.println(String.format("kK=%f, kV=%f, kA=%f, kP=%f", kK,
		// kV, kA, kP));
		final MotionProvider newMotion = motions.poll();
		if (newMotion != null) {
			currentKinematics = new Kinematics(newMotion, poseProvider.getWheelPositions(), Timer.getFPGATimestamp(), 0,
					0, nPoints);
			// System.out.println("starting new motion:" +
			// currentKinematics.toString());
			isEnabled = true;
		}
	}

	public void free() {
		controller.cancel();
		// RobotLoggerManager.setFileHandlerInstance("robot.controller").info("MPCTask
		// is destroyed.");
	}

	public boolean getEnabled() {
		return isEnabled;
	}

	public synchronized double getKA() {
		return kA;
	}

	public synchronized double getKK() {
		return kK;
	}

	public synchronized double getKP() {
		return kP;
	}

	public synchronized double getKV() {
		return kV;
	}

	public synchronized boolean isFinished() {
		return currentKinematics == null;
	}

	public synchronized void setKA(final double kA) {
		this.kA = kA;
	}

	public synchronized void setKK(final double kK) {
		this.kK = kK;
	}

	public synchronized void setKP(final double kP) {
		this.kP = kP;
	}

	public synchronized void setKV(final double kV) {
		this.kV = kV;
	}
}