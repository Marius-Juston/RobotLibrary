package com.waltonrobotics.controllers;

public class Motion {
	// kK = 0.1
	// kV = 0.4

	private final RobotPair position;
	private final RobotPair velocity;
	private final RobotPair accel;
	private final boolean isDone;
	private final Pose pose;
	private final double time;

	public Motion(final double posLeft, final double velLeft, final double accLeft, final double posRight,
			final double velRight, final double accRight, final boolean isDone, final Pose pose_init, final double dt) {

		position = new RobotPair(posLeft, posRight);
		velocity = new RobotPair(velLeft, velRight);
		accel = new RobotPair(accLeft, accRight);

		this.isDone = isDone;

		pose = pose_init;
		time = dt;

		// RobotLoggerManager.setFileHandlerInstance("robot.controllers").info("");
	}

	@Override
	public String toString() {
		return String.format("position=%s; velocity=%s; accel=%s", position, velocity, accel);
	}

}
