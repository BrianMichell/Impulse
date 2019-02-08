package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import java.lang.Math;
import edu.wpi.first.wpilibj.Timer;

public class DriverJoystick implements Runnable {

    // private final int LEFT_X = 0;
    // private final int RIGHT_Y = 5;
    private final int LEFT_Y = 1;
    private final int RIGHT_X = 4;

    private double appliedPower[] = { 0, 0 };

    public XboxController joystick;

    private final Thread t;

    public DriverJoystick() {
        this(0);
    }

    public DriverJoystick(int port) {
        joystick = new XboxController(port);
        t = new Thread(this, "Joystick");
        t.start();
    }

    public void run() {

        while (!Thread.interrupted()) {
           
            appliedPower[0] = deadzone(-joystick.getRawAxis(LEFT_Y));
            appliedPower[1] = deadzone(joystick.getRawAxis(RIGHT_X)); // Values on the X axis must be inverted

            Timer.delay(0.005);
        }
    }

    /**
     * Applies an 8% deadzone to the joystick
     * 
     * @param inp The value of the target axis
     */
    private double deadzone(double inp) {
        return Math.abs(inp) > 0.08 ? inp : 0.0;
    }

    /**
     * Gets the output power of a desired axis
     * 
     * @param axis The axis desired
     * @return The calculated power of the axis (Not the raw value)
     */
    @Deprecated
    public double getAxis(int axis) {
        return appliedPower[axis];
    }

    /**
     * @return Returns the calculated forward power (This will differ from the raw
     *         value)
     */
    public double getForward() {
        return appliedPower[0];
    }

    /**
     * @return Returns the calculated turn power (This will differ from the raw
     *         value)
     */
    public double getTurn() {
        return appliedPower[1];
    }

}