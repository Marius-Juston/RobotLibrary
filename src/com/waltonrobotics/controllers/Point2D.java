package com.waltonrobotics.controllers;

public class Point2D {
	public static Point2D interpolate(final Point2D pose0, final double p, final Point2D pose1, final double q) {
		return new Point2D(pose0.getX() * p + pose1.getX() * q, pose0.getY() * p + pose1.getY() * q);
	}

	private double x;

	private double y;

	public Point2D(final double x, final double y) {
		this.setX(x);
		this.setY(y);
	}

	// Calculates distance between two points
	public double distance(final Point2D other) {
		final double deltaX = Math.pow(getX() - other.getX(), 2);
		final double deltaY = Math.pow(getY() - other.getY(), 2);
		return Math.sqrt(deltaX + deltaY);
	}

	public Point2D offsetPoint(final double l, final double angle) {
		return new Point2D(getX() + l * Math.cos(angle), getY() + l * Math.sin(angle));
	}

	@Override
	public String toString() {
		return String.format("x=%f, y=%f", getX(), getY());
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}