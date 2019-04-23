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
    private NetworkTableEntry offset;
    private double lastDistance;
    private double lastAngle;
    private double lastOffset;

    public VisionGetter() {
        inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("Vision");
        dist = table.getEntry("x");
        angle = table.getEntry("Angle");
        offset = table.getEntry("Offset");
        lastDistance = 0.0;
        lastAngle = 0.0;
        lastOffset = 0.0;
    }

    private double getDistance() {
        double distance = 0;
        distance = dist.getDouble(distance);
        lastDistance = distance != 0.0 ? distance : lastDistance; // Only updates last distance if a target was found
        return distance;
    }

    private double getAngle() {
        double ang = 0.0;
        ang = angle.getDouble(ang);
        lastAngle = ang != 0.0 ? ang : lastAngle;
        return ang;
    }

    private double getOffset() {
        double reportedOffset = 0.0;
        reportedOffset = offset.getDouble(reportedOffset);
        lastOffset = reportedOffset != 0.0 ? reportedOffset : lastOffset;
        return reportedOffset;
    }

    public double getEncoderDistance() {
        getDistance();
        return lastDistance;
    }

    public double getGyroAngle() {
        getAngle();
        return lastAngle;
    }

    public double getOffsetDistance() {
        getOffset();
        return lastOffset;
    }

}