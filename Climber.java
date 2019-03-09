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

    private final double ANKLE_MAX_OUTPUT = 0.72;

    private PID ankleController, kneeController;

    private boolean climbRequested;
    private boolean climbInitiated;

    public Climber(Hardware hw) {
        this.accelerometer = hw.accelerometer;
        this.ankleEncoder = hw.ankleEncoder;
        this.kneeEncoder = hw.kneeEncoder;
        this.stage = this.DISABLED;
        this.ankle = hw.ankle;
        this.knee = hw.knee;
        this.ankleController = new PID(-0.065, -0.125, -0.65, this.ankleEncoder, 0.72);
        this.kneeController = new PID(-0.065, -0.125, -0.65, this.kneeEncoder);
        this.climbRequested = false;
        this.climbInitiated = false;
        // this.ds = DriverStation.getInstance();
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
                ankleController.enabled();
                kneeController.enabled();
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

    private void stageCommon() {
        if (!this.ankleController.isSteadyState() || !this.kneeController.isSteadyState()) {
            if (isTipping()) {
                recover();
            } else if (!this.climbRequested) {
                stage = DISABLED;
                return;
            }
            this.ankle.set(this.ankleController.output);
            this.knee.set(this.kneeController.output);
            SmartDashboard.putNumber("Ankle output", this.ankleController.output);
            SmartDashboard.putNumber("Knee output", this.kneeController.output);
        } else {
            stage = STAGE_TWO;
        }
    }

    private void climb() {
        SmartDashboard.putNumber("Climb Stage", stage);

        switch (stage) {
        case STAGE_ONE: // Tip back for liftoff (Shift CG over the foot)
            // this.ankleController.setGains(0.05, 0.0, 0.0);
            // this.kneeController.setGains(0.05, 0.0, 0.0);
            this.ankleController.setSetpoint(0);
            this.kneeController.setSetpoint(-205);
            stageCommon();
            break;
        case STAGE_TWO: // Lift off into outerspace (Move straight up)
            // this.ankleController.setGains(0.05, 0.0, 0.0);
            // this.kneeController.setGains(0.05, 0.0, 0.0);
            this.ankleController.setSetpoint(0);
            this.kneeController.setSetpoint(-890);
            stageCommon();
            break;
        case STAGE_THREE: // Enter orbit (Move front wheels onto the platform) (Possibly not needed)
            // this.ankleController.setGains(0.05, 0.0, 0.0);
            // this.kneeController.setGains(0.05, 0.0, 0.0);
            // this.ankleController.setSetpoint(-100);
            // this.kneeController.setSetpoint(-935);
            this.ankleController.setSetpoint(0);
            this.kneeController.setSetpoint(-1287);
            stageCommon();
            break;
        case FINALIZE: // Retract landing gear (Rotate foot up) (Optional)
            // this.ankleController.setGains(0.05, 0.0, 0.0);
            // this.kneeController.setGains(0.05, 0.0, 0.0);
            this.ankleController.setSetpoint(0);
            this.kneeController.setSetpoint(0);
            if (!this.ankleController.isSteadyState() || !this.kneeController.isSteadyState()) {
                this.ankle.set(this.ankleController.output);
                this.knee.set(this.kneeController.output);
                SmartDashboard.putNumber("Ankle output", this.ankleController.output);
                SmartDashboard.putNumber("Knee output", this.kneeController.output);
                if (!this.climbRequested) {
                    stage = DISABLED;
                    break;
                }
            } else {
                stage = DISABLED;
            }
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
        // ankleController.disabled();
        // kneeController.disabled();
    }

    // @Override
    // public void enable(){
    // //nothing
    // }

    // @Override
    // public void disable(){
    // haltSystem();
    // }

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
        if (accelerometer.getY() > 1.0) {
            this.ankleController.setPercentOutput(ANKLE_MAX_OUTPUT + 0.1);
        } else {
            this.ankleController.setPercentOutput(ANKLE_MAX_OUTPUT - 0.1);
        }
    }

    public void requestClimb(boolean isRequested) {
        this.climbRequested = isRequested;
    }

}