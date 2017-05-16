package com.waltonrobotics.controllers;

public class Pose {
	public static Pose interpolate(final Pose pose0, final double p, final Pose pose1, final double q) {
		return new Pose(Point2D.interpolate(pose0.getX(), p, pose1.getX(), q),
				p * pose0.getAngle() + q * pose1.getAngle());
	}

	private final Point2D X;

	private final double angle;

	public Pose(final Point2D X, final double angle) {
		this.X = X;
		this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}

	public Point2D getX() {
		return X;
	}

	public Point2D offsetPoint(final double l) {
		return new Point2D(getX().getX() + l * Math.cos(getAngle()), getX().getY() + l * Math.sin(getAngle()));
	}

	@Override
	public String toString() {
		return String.format("x=%f, y=%f, angle=%f", X.getX(), X.getY(), getAngle());
	}

}
