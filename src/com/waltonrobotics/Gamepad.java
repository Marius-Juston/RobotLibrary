package com.waltonrobotics;

import edu.wpi.first.wpilibj.Joystick;
/**
 *
 * Wrapper class for the game pad
 *
 */
public class Gamepad extends Joystick
{
	/**
	 *
	 * non d-pad buttons enum
	 *
	 */
	public enum Button
	{
		A(1), B(2), X(3), Y(4), L(5), R(6), BACK(7), START(8);

		private int index;

		private Button(final int index)
		{
			this.index = index;
		}
	}

	/**
	 * @param port the port of the controller
	 */
	public Gamepad(final int port)
	{
		super(port);
	}

	/**
	 *
	 * @param b
	 *            the button to get
	 *
	 *            A,B,X,Y,L,R,START, or BACK
	 *
	 * @return
	 *
	 */

	boolean getPressed(Button b)
	{
		return getRawButton(b.index);
	}

	/**
	 *
	 * @param index
	 *            the index of the button
	 *
	 *            A( 0 ), B( 1 ), X( 2 ), Y( 3 ), L( 7 ), R( 8 ), BACK( 9 ),
	 *            START( 10 );
	 *
	 * @return true if button pressed false if not pressed
	 *
	 */

	public boolean getButton(final int index)
	{
		return getRawButton(index);
	}

	/**
	 *
	 * 0 is not pressed and 1 is completely pressed
	 * @return the left trigger value between 0 and 1
	 *
	 */

	public double getLeftTrigger()
	{
		return getRawAxis(2);
	}

	/**
	 *
	 * left is 1 right is -1 //TODO check this
	 *
	 * @return the left thumb stick x value between -1 and 1
	 *
	 */

	public double getLeftX()
	{
		return getRawAxis(0);
	}

	/**
	 *
	 * forward is -1 and backward is 1 //TODO check this
	 *
	 * @return the left thumb stick y value between -1 and 1
	 *
	 */
	public double getLeftY()
	{
		return getRawAxis(1);
	}

	/**
	 *
	 * 0 is not pressed and 1 is completely pressed
	 * @return the right trigger value between 0 and 1
	 *
	 */

	public double getRightTrigger()
	{
		return getRawAxis(3);
	}

	/**
	 *
	 * left is 1 right is -1 //TODO check this
	 * @return the right thumb stick x value between -1 and 1
	 *
	 */

	public double getRightX()
	{
		return getRawAxis(4);
	}

	/**
	 *
	 * forward is -1 and backward is 1 //TODO check this
	 * @return the right thumb stick y value between -1 and 1
	 *
	 */
	public double getRightY()
	{
		return getRawAxis(3);
	}
}