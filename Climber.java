package frc.robot;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class Climber extends Subsystem {

    // DriverStation ds;
    BuiltInAccelerometer accelerometer;
    Encoder ankleEncoder;
    Encoder kneeEncoder;

    SpeedControllerGroup ankle, knee;

    public int stage;
    private final int DISABLED = 0;
    private final int STAGE_ONE = 1;
    private final int STAGE_TWO = 2;
    private final int STAGE_THREE = 3;
    private final int FINALIZE = 4;

    private final double angle = 28.5 * Math.PI / 180;

    private boolean climbRequested;
    private boolean climbInitiated;

    public Climber(Hardware hw) {
        // SmartDashboard.putNumber("Knee Log Denom", -50);
        // SmartDashboard.putNumber("Knee Min", 0.35);
        // SmartDashboard.putNumber("Knee Goal", -1200);

        // SmartDashboard.putNumber("Ankle Log Denom", -50);
        // SmartDashboard.putNumber("Ankle Min", 0.3);
        // SmartDashboard.putNumber("Ankle Goal", 0);

        this.accelerometer = hw.accelerometer;
        this.ankleEncoder = hw.ankleEncoder;
        this.kneeEncoder = hw.kneeEncoder;
        this.stage = this.DISABLED;
        this.ankle = hw.ankle;
        this.knee = hw.knee;
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

    public void manualDrive(double ankleSpeed, double kneeSpeed) {
        ankle.set(ankleSpeed);
        knee.set(kneeSpeed);
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

            int dk = -1200 - kneeEncoder.get();

            double log = (double) dk / -50;
            double min = 0.35;

            double kneePower = normalize(log - angleLog, min, 0.85);
            knee.set(kneePower);

            SmartDashboard.putNumber("Knee Diff", dk);
            SmartDashboard.putNumber("Knee Power", kneePower);

            int da = -ankleEncoder.get();

            log = (double) da / -55;
            min = 0.25;

            double anklePower = normalize(log, min, 0.85);
            ankle.set(anklePower);

            SmartDashboard.putNumber("Ankle Diff", da);
            SmartDashboard.putNumber("Ankle Power", anklePower);

            break;
        case STAGE_TWO: 
            break;
        case STAGE_THREE:
            break;
        case FINALIZE:
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
        this.ankle.stopMotor();
        this.knee.stopMotor();
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