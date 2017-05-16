package com.waltonrobotics.controllers;

public class RobotPair {

	private final double left;
	private final double right;

	public RobotPair(final double left, final double right) {
		this.left = left;
		this.right = right;
	}

	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public double mean() {
		return (getLeft() + getRight()) / 2;
	}

	@Override
	public String toString() {
		return String.format("%f, %f", getLeft(), getRight());
	}

}
