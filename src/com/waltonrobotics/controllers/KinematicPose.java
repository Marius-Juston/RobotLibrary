package com.waltonrobotics.controllers;

public class KinematicPose extends Pose {
	public static KinematicPose interpolate(final KinematicPose pose0, final double p, final KinematicPose pose1,
			final double q) {
		final Pose pose = Pose.interpolate(pose0, p, pose1, q);
		final KinematicState left = KinematicState.interpolate(pose0.getLeft(), p, pose1.getLeft(), q);
		final KinematicState right = KinematicState.interpolate(pose0.getRight(), p, pose1.getRight(), q);
		return new KinematicPose(pose, left, right, p * pose0.getT() + q * pose1.getT(), false);
	}

	private final KinematicState left;
	private final KinematicState right;
	private final double t;

	private final boolean isFinished;

	KinematicPose(final Pose pose, final KinematicState left, final KinematicState right, final double t,
			final boolean isFinished) {
		super(pose.getX(), pose.getAngle());
		this.left = left;
		this.right = right;
		this.t = t;
		this.isFinished = isFinished;
	}

	public double getLCenter() {
		return (getLeft().l + getRight().l) / 2.0;
	}

	public KinematicState getLeft() {
		return left;
	}

	public KinematicState getRight() {
		return right;
	}

	public double getT() {
		return t;
	}

	public double getVCenter() {
		return (getLeft().v + getRight().v) / 2.0;
	}

	public boolean isFinished() {
		return isFinished;
	}

	@Override
	public String toString() {
		return String.format("%s, left:%s, right:%s, t=%f, isFinished=%s", super.toString(), getLeft(), getRight(),
				getT(), isFinished());
	}
}
