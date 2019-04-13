package frc.robot;

import edu.wpi.first.networktables.*;

/**
 * Converts data from RaspberryPi vision system into
 * robot forward and turn commands.
 */
class TelemetryGetter {

    NetworkTableInstance inst;
    NetworkTableEntry dist;
    NetworkTableEntry angle;

    public TelemetryGetter() {
        inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("Vision");
        dist = table.getEntry("x");
        angle = table.getEntry("Angle");
    }

    public double getDistance() {
        double distance = 0;
        distance = dist.getDouble(distance);
        return distance;
    }

    public double getAngle() {
        double ang = 0.0;
        ang = angle.getDouble(ang);
        return ang;
    }

}