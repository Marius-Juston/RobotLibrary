// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package com.waltonrobotics;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	private final Joystick left;
	private final Joystick right;
	private final Gamepad gamepad;

	/**
	 * Se3ts the loactions of the joysticks, if -1 is applied the joystic is
	 * rendered null
	 *
	 * @param left
	 * @param right
	 * @param gamepad
	 */
	public OI(final int leftI, final int rightI, final int gamepadI) {
		left = leftI >= 0 ? new Joystick(leftI) : null;
		
	        right = rightI >= 0 ? new Joystick(rightI) : null;

		gamepad = gamepadI >= 0 ? new Joystick(gamepadI) : null;
	}

	public Gamepad getGamepad() {
		return gamepad;
	}

	public Joystick getLeft() {
		return left;
	}

	public Joystick getRight() {
		return right;
	}
}
