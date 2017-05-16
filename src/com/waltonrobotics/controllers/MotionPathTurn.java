package com.waltonrobotics.controllers;

public class MotionPathTurn extends MotionProvider {
	private final Pose pose0;
	private final Pose pose1;

	public MotionPathTurn(final Pose pose0, final double dAngle, final double vCruise, final double rotAccelMax) {
		super(vCruise, rotAccelMax);
		this.pose0 = pose0;
		pose1 = new Pose(pose0.getX(), pose0.getAngle() + boundAngle(dAngle));
	}

	@Override
	public Pose evaluatePose(final double s) {
		final double r = 1.0 - s;
		return new Pose(pose0.getX(), r * pose0.getAngle() + s * pose1.getAngle());
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
		return 0;
	}

	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitRotationalAcceleration;
	}

}
