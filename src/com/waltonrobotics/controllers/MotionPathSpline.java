package com.waltonrobotics.controllers;

public class MotionPathSpline extends MotionProvider {
	Motion goal;

	private final Point2D[] controlPoints = new Point2D[4];
	private final double length;
	private final double angle0;
	private final double angle1;
	private final boolean isForwards;

	public MotionPathSpline(final Pose initial, final double l0, final Pose final_, final double l1,
			final double vCruise, final double aMax, final boolean isForwards) {
		super(vCruise, aMax);
		controlPoints[0] = initial.getX();
		controlPoints[1] = initial.offsetPoint(isForwards ? l0 : -l0);
		controlPoints[2] = final_.offsetPoint(isForwards ? -l1 : l1);
		controlPoints[3] = final_.getX();

		Point2D Xprev = evaluate(B(0));
		double length = 0;
		for (int i = 1; i <= 100; i++) {
			final double s = i / 100.0;
			final Point2D Xnext = evaluate(B(s));
			length += Xprev.distance(Xnext);
			Xprev = Xnext;
		}

		this.length = length;
		angle0 = initial.getAngle();
		angle1 = final_.getAngle();
		this.isForwards = isForwards;
		// System.out.println(initial.toString());
		// System.out.println(controlPoints[1].toString());
		// System.out.println(controlPoints[2].toString());
		// System.out.println(final_.toString());

	}

	private double[] B(final double s) {
		final double[] result = new double[4];
		final double r = 1 - s;
		result[0] = r * r * r;
		result[1] = 3 * r * r * s;
		result[2] = 3 * r * s * s;
		result[3] = s * s * s;
		return result;
	}

	private double[] dBds(final double s) {
		final double[] result = new double[4];
		final double r = 1 - s;
		result[0] = -3 * r * r;
		result[1] = 3 * r * r - 6 * r * s;
		result[2] = -3 * s * s + 6 * r * s;
		result[3] = 3 * s * s;
		return result;
	}

	private Point2D evaluate(final double[] shape) {
		final Point2D result = new Point2D(0, 0);
		for (int i = 0; i < 4; i++) {
			result.setX(result.getX() + shape[i] * controlPoints[i].getX());
			result.setY(result.getY() + shape[i] * controlPoints[i].getY());
		}
		return result;
	}

	@Override
	public Pose evaluatePose(final double s) {
		final Point2D X = evaluate(B(s));
		final Point2D dXds = evaluate(dBds(s));
		double theta;
		if (isForwards) {
			theta = Math.atan2(dXds.getY(), dXds.getX());
		} else {
			theta = Math.atan2(-dXds.getY(), -dXds.getX());
		}
		return new Pose(X, theta); // Find the values for v and a and position
	}

	@Override
	public double getFinalTheta() {
		return angle1;
	}

	@Override
	public double getInitialTheta() {
		return angle0;
	}

	@Override
	public double getLength() {
		return isForwards ? length : -length;
	}

	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitLinearAcceleration;
	}

}
