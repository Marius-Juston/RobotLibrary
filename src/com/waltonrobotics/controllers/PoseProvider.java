package com.waltonrobotics.controllers;

public interface PoseProvider {
	Pose getPose();

	RobotPair getWheelPositions();
}
