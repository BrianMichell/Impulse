package frc.robot;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class Climber extends Subsystem {

    // DriverStation ds;
    BuiltInAccelerometer accelerometer;
    Encoder kneeEncoder;
    Encoder hipEncoder;

    SpeedControllerGroup knee, hip;

    public int stage;
    private final int DISABLED = 0;
    private final int STAGE_ONE = 1;
    private final int FINALIZE = 2;

    private final double HIP_MAX = 0.85;
    private final double HIM_MIN = 0.35;
    private final double KNEE_MAX = 1.00;
    private final double KNEE_MIN = 0.25;

    private final double angle = 28.5 * Math.PI / 180;

    private boolean climbRequested;
    private boolean climbInitiated;

    public Climber(Hardware hw) {
        // SmartDashboard.putNumber("Hip Log Denom", -50);
        // SmartDashboard.putNumber("Hip Min", 0.35);
        // SmartDashboard.putNumber("Hip Goal", -1200);

        // SmartDashboard.putNumber("Knee Log Denom", -50);
        // SmartDashboard.putNumber("Knee Min", 0.3);
        // SmartDashboard.putNumber("Knee Goal", 0);

        this.accelerometer = hw.accelerometer;
        this.kneeEncoder = hw.kneeEncoder;
        this.hipEncoder = hw.hipEncoder;
        this.stage = this.DISABLED;
        this.knee = hw.knee;
        this.hip = hw.hip;
        this.climbRequested = false;
        this.climbInitiated = false;
        start("Climber");
    }

    @Override
    protected void actions() {
        SmartDashboard.putBoolean("Climb requested", this.climbRequested);
        SmartDashboard.putBoolean("Climb initiated", this.climbInitiated);

        if (this.climbRequested) {
            // TODO Make sure isEndgame() is correct and reimplement
            // if(isEndgame() && this.climbRequested){
            if (!this.climbInitiated) {
                this.stage = STAGE_ONE;
                this.climbInitiated = true;
            }

            climb();
        } else {
            haltSystem();
        }
    }

    public void manualDrive(double kneeSpeed, double hipSpeed) {
        knee.set(kneeSpeed);
        hip.set(hipSpeed);
    }

    private double normalize(double val, double minPow, double maxPow) {
        if (val < 0)
            return Math.max(-maxPow, Math.min(val, -minPow));
        else
            return Math.min(Math.max(val, minPow), maxPow);
    }

    private void climb() {
        SmartDashboard.putNumber("Climb Stage", stage);

        switch (stage) {
        case STAGE_ONE: // Tip back for liftoff (Shift CG over the foot)
            double dAngle = -angle - Math.asin(accelerometer.getY());
            double angleLog = dAngle / 2;

            int dk = -1200 - hipEncoder.get();

            double log = (double) dk / -50;
            double min = 0.35;

            double hipPower = normalize(log - angleLog, min, 0.85);
            hip.set(hipPower);

            SmartDashboard.putNumber("Hip Diff", dk);
            SmartDashboard.putNumber("Hip Power", hipPower);

            int da = -kneeEncoder.get();

            log = (double) da / -55;
            min = 0.25;

            double kneePower = normalize(log, min, 0.85);
            knee.set(kneePower);

            SmartDashboard.putNumber("Knee Diff", da);
            SmartDashboard.putNumber("KNee Power", kneePower);

            break;
        case FINALIZE: // Bring entire assembly back up
            break;
        case DISABLED:
        default:
            haltSystem();
            break;
        }
    }

    /**
     * @return True if the acceleration due to gravity varies more than 1 meter per
     *         second in the y-axis
     */
    private boolean isTipping() {
        double y = accelerometer.getY();
        return Math.abs(y) >= 1.0;
    }

    @Override
    protected void haltSystem() {
        this.knee.stopMotor();
        this.hip.stopMotor();
    }

    /**
     * @return Has at least 130 seconds passed
     */
    private boolean isEndgame() {
        return true;
        // return ds.getMatchTime() <= 130;
    }

    /**
     * Applies power to the linkages to orient the bot to be parallel to the floor
     * again
     */
    private void recover() {
    }

    public void requestClimb(boolean isRequested) {
        this.climbRequested = isRequested;
    }

}