package com.waltonrobotics;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public abstract class IRobotMap {

	protected static Talon left;
	protected static Talon right;

	protected static Talon intake;

	private static Encoder encoderLeft;

	private static Encoder encoderRight;

	protected static Solenoid pneumaticsShifter;

	public static Encoder getEncoderLeft() {
		return encoderLeft;
	}

	public static Encoder getEncoderRight() {
		return encoderRight;
	}

	/**
	 * @return the intake
	 */
	public static Talon getIntake() {
		return intake;
	}

	public static Talon getLeft() {
		return left;
	}

	public static Solenoid getPneumaticsShifter() {
		return pneumaticsShifter;
	}

	public static Talon getRight() {
		return right;
	}

	static void init() {
		setLeft(new Talon(0));
		setRight(new Talon(1));

		setIntake(new Talon(2));
		setPneumaticsShifter(new Solenoid(0));

		setEncoderRight(new Encoder(new DigitalInput(0), new DigitalInput(1)));
		setEncoderLeft(new Encoder(new DigitalInput(2), new DigitalInput(3)));

	}

	public static void setEncoderLeft(final Encoder encoderLeft) {
		IRobotMap.encoderLeft = encoderLeft;
	}

	public static void setEncoderRight(final Encoder encoderRight) {
		IRobotMap.encoderRight = encoderRight;
	}

	public static void setIntake(final Talon intake) {
		IRobotMap.intake = intake;
	}

	public static void setLeft(final Talon left) {
		IRobotMap.left = left;
	}

	public static void setPneumaticsShifter(final Solenoid pneumaticsShifter) {
		IRobotMap.pneumaticsShifter = pneumaticsShifter;
	}

	public static void setRight(final Talon right) {
		IRobotMap.right = right;
	}

}
