package com.waltonrobotics.controllers;

public class KinematicState {
	public static KinematicState interpolate(final KinematicState state0, final double p, final KinematicState state1,
			final double q) {
		return new KinematicState(p * state0.l + q * state1.l, state1.v, state1.a);
	}

	public final double l;
	public final double v;

	public final double a;

	public KinematicState(final double l, final double v, final double a) {
		this.l = l;
		this.v = v;
		this.a = a;
	}

	@Override
	public String toString() {
		return String.format("l=%f, v=%f, a=%f", l, v, a);
	}
}
