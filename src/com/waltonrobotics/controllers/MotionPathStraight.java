package com.waltonrobotics.controllers;

public class MotionPathStraight extends MotionProvider {
	private Pose pose0;
	private Pose pose1;
	private double length;

	public MotionPathStraight(final Pose pose0, final double distance, final double vCruise, final double aMax) {
		super(vCruise, aMax);
		synchronized (this) {
			this.pose0 = pose0;
			pose1 = new Pose(pose0.offsetPoint(distance), pose0.getAngle());
			length = distance;
		}
	}

	@Override
	public Pose evaluatePose(final double s) {
		final Point2D X = Point2D.interpolate(pose0.getX(), 1.0 - s, pose1.getX(), s);
		final double angle = pose0.getAngle();
		return new Pose(X, angle);
	}

	@Override
	public double getFinalTheta() {
		return pose1.getAngle();
	}

	@Override
	public double getInitialTheta() {
		return pose0.getAngle();
	}

	@Override
	public double getLength() {
		return length;
	}

	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitLinearAcceleration;
	}

}
