package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Subsystem {

    private final DifferentialDrive drive;
    private final PowerDistributionPanel pdp;

    private double maxDraw = 0;

    private double forward;
    private double turn;
    private int[] MOTORS;

    private final int CURRENT_MAX = 120;

    public Drive(Hardware hw) {
        super(hw, "Drive");
        this.drive = hw.drive;
        this.pdp = hw.pdp;
        this.MOTORS = hw.MOTORS;
    }

    @Override
    protected void actions() {
        this.drive.arcadeDrive(forward,turn);
    }

    @Override
    protected void haltSystem(){
        this.drive.stopMotor();
    }

    /**
     * Finds the summation of the current being pulled by the drive motors
     * @return The total number of amps drawn by the drivetrain
     */
    private double reportCurrentDraw(){
        double ret = 0;
        /*
        for(int i=0; i<MOTORS.length; i++){
            ret += this.pdp.getCurrent(MOTORS[i]);
        }
        */
        ret = this.pdp.getTotalCurrent();
        SmartDashboard.putNumber("Drivetrain current draw", ret);
        if(ret > maxDraw){
            maxDraw = ret;
        }
        SmartDashboard.putNumber("Max current", maxDraw);
        return ret;
    }

    /**
     * Updates the desired power outputs
     * @param forward The forward/backward power
     * @param turn The twist power
     */
    public void updateSpeeds(double forward, double turn){
        this.forward = forward;
        this.turn = turn;
    }

    /**
     * @return True if the drivetrain is pulling more than the allowed amount of amps.
     */
    public boolean overCurrent(){
        return reportCurrentDraw() > CURRENT_MAX;
    }

}