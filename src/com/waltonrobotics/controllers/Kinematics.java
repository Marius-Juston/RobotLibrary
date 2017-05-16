package com.waltonrobotics.controllers;

public class Kinematics {

	public static final double robotWidth = .70485; // practice bot

	public static KinematicPose staticPose(final Pose pose, final RobotPair wheelPosition, final double t) {
		final KinematicState left = new KinematicState(wheelPosition.getLeft(), 0, 0);
		final KinematicState right = new KinematicState(wheelPosition.getRight(), 0, 0);
		return new KinematicPose(pose, left, right, t, false);
	}

	private final MotionProvider motion;
	private KinematicPose lastPose;

	private KinematicPose nextPose;
	private double s;
	private final double v0;
	private final double v1;
	private final int nPoints;

	private double l0;

	public Kinematics(final MotionProvider motion, final RobotPair wheelPosition, final double t, final double v0,
			final double v1, final int nPoints) {
		this.motion = motion;
		s = 0.0;
		this.v0 = v0;
		this.v1 = v1;
		this.nPoints = nPoints;

		evaluateFirstPose(wheelPosition, t);
		evaluateNextPose(1.0 / nPoints);
	}

	private void evaluateFirstPose(final RobotPair wheelPosition, final double t) {
		final Pose pose = motion.evaluatePose(0);
		final KinematicState left = new KinematicState(wheelPosition.getLeft(), v0, 0);
		final KinematicState right = new KinematicState(wheelPosition.getRight(), v0, 0);

		lastPose = nextPose = new KinematicPose(pose, left, right, t, false);
		l0 = lastPose.getLCenter();
		// System.out.println("First Pose: " + lastPose.toString());
	}

	private void evaluateLastPose() {
		final Pose pose = motion.evaluatePose(1.0);
		final KinematicState left = new KinematicState(lastPose.getLeft().l, v1, 0);
		final KinematicState right = new KinematicState(lastPose.getRight().l, v1, 0);

		lastPose = nextPose = new KinematicPose(pose, left, right, lastPose.getT(), true);
		// System.out.println("Last Pose: " + lastPose.toString());
	}

	private void evaluateNextPose(final double ds) {
		lastPose = nextPose;

		if (lastPose.isFinished()) {
			evaluateLastPose();
			return;
		}

		boolean isFinished = false;
		if (s + ds > 1.0) {
			s = 1.0;
			isFinished = true;
		} else {
			s += ds;
		}

		// calculate the next pose from given motion
		final Pose pose = motion.evaluatePose(s);
		final double direction = Math.signum(motion.getLength());

		// estimate angle to turn s
		final double dl = lastPose.getX().distance(pose.getX()) * direction;
		final double dAngle = MotionProvider.boundAngle(pose.getAngle() - lastPose.getAngle());

		// estimate lengths each wheel will turn
		final double dlLeft = dl - dAngle * robotWidth / 2;
		final double dlRight = dl + dAngle * robotWidth / 2;

		// assuming one of the wheels will limit motion, calculate time this
		// step will take
		double dt = Math.max(Math.abs(dlLeft), Math.abs(dlRight)) / motion.getvCruise();
		double a = 0.0; // acceleration doesn't matter if following steady
						// motion
		// System.out.println(String.format("s=%f, dl=%f, dlLeft=%f, dlRight=%f,
		// dt=%f, direction=%f", s, dl, dlLeft, dlRight, dt, direction));
		// bound time steps for initial/final acceleration
		switch (motion.getLimitMode()) {
		case LimitLinearAcceleration:
			// assuming constant linear acceleration from initial/to final
			// speeds
			final double v = Math.abs(dl) / dt;
			final double lMidpoint = lastPose.getLCenter() + .5 * dl - l0;

			final double vAccel = Math.sqrt(v0 * v0 + motion.getaMax() * Math.abs(lMidpoint));
			final double vDecel = Math.sqrt(v1 * v1 + motion.getaMax() * Math.abs(motion.getLength() - lMidpoint));

			if (vAccel < v && vAccel < vDecel) {
				a = motion.getaMax();
				dt = Math.abs(dl) / vAccel;
			}

			if (vDecel < v && vDecel < vAccel) {
				a = -motion.getaMax();
				dt = Math.abs(dl) / vDecel;
			}

			// System.out.println(String.format("v=%f, vaccel=%f, vdecel=%f", v,
			// vAccel, vDecel));
			break;

		case LimitRotationalAcceleration:
			// assuming constant angular acceleration from/to zero angular speed
			final double omega = Math.abs(dlRight - dlLeft) / dt / robotWidth;
			final double thetaMidpoint = lastPose.getAngle() + .5 * dAngle;

			final double omegaAccel = Math.sqrt(
					motion.getaMax() * Math.abs(MotionProvider.boundAngle(thetaMidpoint - motion.getInitialTheta())));
			final double omegaDecel = Math.sqrt(
					motion.getaMax() * Math.abs(MotionProvider.boundAngle(thetaMidpoint - motion.getFinalTheta())));
			// System.out.println("OmegaAccel=" + omegaAccel);
			if (omegaAccel < omega && omegaAccel < omegaDecel) {
				dt = Math.abs(dlRight - dlLeft) / omegaAccel / robotWidth;
			}

			// System.out.println("OmegaDecel=" + omegaDecel);
			if (omegaDecel < omega && omegaDecel < omegaAccel) {
				dt = Math.abs(dlRight - dlLeft) / omegaDecel / robotWidth;
			}
			break;
		}

		// create new kinematic state. Old state is retained to interpolate
		// positions, new state contains estimate for speed and accel
		final KinematicState left = new KinematicState(lastPose.getLeft().l + dlLeft, dlLeft / dt, a * direction);
		final KinematicState right = new KinematicState(lastPose.getRight().l + dlRight, dlRight / dt, a * direction);

		nextPose = new KinematicPose(pose, left, right, lastPose.getT() + dt, isFinished);
		// System.out.println("Next Pose " + nextPose.toString());
	}

	public Pose getPose() {
		return new Pose(nextPose.getX(), nextPose.getAngle());
	}

	public double getTime() {
		return nextPose.getT();
	}

	public RobotPair getWheelPositions() {
		return new RobotPair(nextPose.getLeft().l, nextPose.getRight().l);
	}

	// calculates the path to follow within a particular distance
	public KinematicPose interpolatePose(final double t) {

		// System.out.println(String.format("Last t=%3.1f Next t=%3.1f Now
		// t=%3.1f", lastPose.t, nextPose.t, t));

		if (t <= lastPose.getT()) {
			return lastPose;
		}

		if (lastPose.isFinished()) {
			return lastPose;
		}

		while (t > nextPose.getT()) {
			evaluateNextPose(1.0 / nPoints);
			if (lastPose.isFinished()) {
				return lastPose;
			}
		}

		final double dt = nextPose.getT() - lastPose.getT();
		final double p = (nextPose.getT() - t) / dt;
		final double q = (t - lastPose.getT()) / dt;

		return KinematicPose.interpolate(lastPose, p, nextPose, q);
	}

	@Override
	public String toString() {
		return String.format("Last pose = %s Next pose = %s", lastPose, nextPose);
	}
}
