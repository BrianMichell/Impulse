package frc.robot;

import edu.wpi.first.networktables.*;

/**
 * Converts data from RaspberryPi vision system into
 * robot forward and turn commands.
 */
class VisionGetter {

    private NetworkTableInstance inst;
    private NetworkTableEntry dist;
    private NetworkTableEntry angle;
    private double lastDistance;

    public VisionGetter() {
        inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("Vision");
        dist = table.getEntry("x");
        angle = table.getEntry("Angle");
        lastDistance = 0.0;
    }

    public double getDistance() {
        double distance = 0;
        distance = dist.getDouble(distance);
        lastDistance = distance != 0.0 ? distance : lastDistance; // Only updates last distance if a target was found
        return distance;
    }

    public double getAngle() {
        double ang = 0.0;
        ang = angle.getDouble(ang);
        return ang;
    }

    public double getEncoderDistance() {
        return lastDistance;
    }

}