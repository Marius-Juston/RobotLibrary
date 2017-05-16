package com.waltonrobotics.controllers;

public abstract class MotionProvider {
	
	protected static double boundAngle(final double angle) {
		if (angle > Math.PI) {
			return angle - Math.PI;
		}
		else if (angle < -Math.PI) {
			return angle + Math.PI;
		}
		return angle;
	}

	private Motion initialMotion;
	private double vCruise;

	private double aMax;

	protected MotionProvider(final double vCruise, final double aMax) {
		if (vCruise == 0) {
			throw new IllegalArgumentException("vCruise cannot be 0");
		}
		this.vCruise = vCruise;

		if (aMax == 0) {
			throw new IllegalArgumentException("aMax cannot be 0");
		}
		this.aMax = aMax;
	}

	protected abstract Pose evaluatePose(double s);

	protected Pose getFinalPose() {
		return evaluatePose(1);
	}

	protected abstract double getFinalTheta();

	protected Pose getInitialPose() {
		return evaluatePose(0);
	}

	protected abstract double getInitialTheta();

	protected abstract double getLength();

	protected abstract LimitMode getLimitMode();

}
