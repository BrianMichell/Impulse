package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import java.lang.Math;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class DriverJoystick implements Runnable {

    // private final int LEFT_X = 0;
    // private final int RIGHT_Y = 5;
    private final int LEFT_Y = 1;
    private final int RIGHT_X = 4;

    private final double POWER_GRACE_ZONE = 0.3;

    private double desiredPower[] = { 0, 0 };
    private double appliedPower[] = { 0, 0 };

    public XboxController joystick;

    private final Thread t;

    private boolean isDebug = true;

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
            // Update the array of desired power output
            desiredPower[0] = deadzone(joystick.getRawAxis(LEFT_Y));
            desiredPower[1] = deadzone(-joystick.getRawAxis(RIGHT_X)); // Values on the X axis must be inverted

            for (int i = 0; i < desiredPower.length; i++) {

                // Flip the signs so the power creeps down
                if (Math.abs(desiredPower[i]) < Math.abs(appliedPower[i])) {
                    if (desiredPower[i] > appliedPower[i]) {
                        desiredPower[i] *= -1.0;
                    }
                    if (desiredPower[i] == 0) {
                        desiredPower[i] = deadzone(-appliedPower[i] / 1.1125);
                    }
                }

                // Update the actual output
                appliedPower[i] += update(desiredPower[i], appliedPower[i]);

                // Cap the output at both ends to prevent runaway
                if (Math.abs(appliedPower[i]) > Math.abs(desiredPower[i])) {
                    appliedPower[i] = desiredPower[i];
                }

            }

            if (isDebug) {
                SmartDashboard.putNumber("Left X", appliedPower[0]);
                SmartDashboard.putNumber("Left Y", appliedPower[1]);
                SmartDashboard.putNumber("Raw left x", desiredPower[0]);
                SmartDashboard.putNumber("Raw left y", desiredPower[1]);
            }
            Timer.delay(0.005);
        }
    }

    /**
     * This method is used to calculate the ramping integral that should be applied
     * to the desired axis.
     * 
     * @param target       The target power [-1,1]
     * @param currentPower The current power of the axis [-1,1]
     * @return The integral that should be applied to the target axis
     */
    private double update(double target, double currentPower) {
        if(deadzone(currentPower/2.5) == 0){
            return Math.pow(target, 3)/1.75;
        } else {
            return Math.pow(currentPower, 3)/4;
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

    /**
     * Moves the desired power closer to zero when when it is outside the power
     * grace zone.
     */
    public void decrementPower() {
        for (int i = 0; i < desiredPower.length; i++) {
            if (Math.abs(appliedPower[i]) >= POWER_GRACE_ZONE) {
                appliedPower[i] += -2 * update(desiredPower[i], appliedPower[i]);
            }
        }
    }

}