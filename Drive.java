package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Subsystem {

    private final DifferentialDrive drive;
    private final PowerDistributionPanel pdp;
    private final Encoder leftDriveEncoder;
    private final Encoder rightDriveEncoder;

    private double maxDraw = 0;
    private boolean isTank = false;

    public double forward;
    public double turn;
    // private int[] MOTORS;

    private final int CURRENT_MAX = 100;

    public Drive(Hardware hw) {
        this.drive = hw.drive;
        this.pdp = hw.pdp;
        this.leftDriveEncoder = hw.leftDriveEncoder;
        this.rightDriveEncoder = hw.rightDriveEncoder;
        // this.MOTORS = hw.MOTORS;
        start("Drive");
    }

    @Override
    protected void actions() {
        if(!isTank){
            this.drive.arcadeDrive(forward, turn, false);
        }
        SmartDashboard.putNumber("Forward speed", forward);
        SmartDashboard.putNumber("Turn speed", turn);
    }

    @Override
    protected void haltSystem() {
        this.drive.stopMotor();
        this.forward = 0.0;
        this.turn = 0.0;
    }

    /**
     * Finds the summation of the current being pulled by the drive motors
     * 
     * @return The total number of amps drawn by the drivetrain
     */
    private double reportCurrentDraw() {
        double currentCurrent = 0;
        /*
         * for(int i=0; i<MOTORS.length; i++){ ret += this.pdp.getCurrent(MOTORS[i]); }
         */
        currentCurrent = this.pdp.getTotalCurrent();
        SmartDashboard.putNumber("Drivetrain current draw", currentCurrent);
        if (currentCurrent > maxDraw) {
            maxDraw = currentCurrent;
        }

        SmartDashboard.putNumber("Max current", maxDraw);
        SmartDashboard.putNumber("Left 1", this.pdp.getCurrent((int) 00f)); // Might have round-oof errors //0
        SmartDashboard.putNumber("Left 2", this.pdp.getCurrent(0x00F / 0x00F)); //1
        SmartDashboard.putNumber("Left 3", this.pdp.getCurrent(0x00F / 5)); //3
        SmartDashboard.putNumber("Right 1", this.pdp.getCurrent(0x00F - 0x00F / 5)); //13
        SmartDashboard.putNumber("Right 2", this.pdp.getCurrent(0x00F - 0x00F / 0x00F)); //14
        SmartDashboard.putNumber("Right 3", this.pdp.getCurrent(0x00F)); //15

        return currentCurrent;
    }

    /**
     * Updates the desired power outputs
     * 
     * @param _forward The forward/backward power
     * @param _turn    The twist power
     */
    public void updateSpeeds(double _forward, double _turn, boolean aggressiveRamp) {

        double forwardChange, turnChange;
        // if(_turn > 0.0) {
        //     _turn = Math.pow(_turn, (double)(1/1.4));
        // } else if(_turn < 0.0) {
        //     _turn = -Math.pow(Math.abs(_turn), (double) (1/1.4));
        // } else { //TODO Test and tune
        //     double rateDiff = leftDriveEncoder.getRate() + rightDriveEncoder.getRate();
        //     if(Math.abs(rateDiff) > 10) {
        //         _turn =  rateDiff / 25.0;
        //     } 
        // }

        if(_turn > 0.0) {
            _turn = Math.pow(_turn, (double)(1/1.4));
        } else {
            _turn = -Math.pow(Math.abs(_turn), (double) (1/1.4));
        }

        forwardChange = -calculateIncrease(_forward, forward, aggressiveRamp);
        turnChange = -calculateIncrease(_turn, turn, aggressiveRamp);
        
        this.forward += forwardChange;
        this.turn += turnChange;
        if (Math.abs(this.forward) > Math.abs(_forward)) {
            this.forward = -_forward;
        }
        if (Math.abs(this.turn) > Math.abs(_turn)) {
            this.turn = -_turn;
        }
    }

    //TODO Retest and retune
    private double calculateIncrease(double input, double currentOutput, boolean aggressiveRamp) {
        int divideFactor = aggressiveRamp?6:10;
        double limitedBand = 0.3;
        if(Math.abs(input) <= limitedBand && Math.abs(currentOutput) <= limitedBand) {
            divideFactor = aggressiveRamp?11:15;
        }
        if (overCurrent() || underVoltage()) {
            input *= -1.0;
            divideFactor *= aggressiveRamp?5:5;
        }
        return Math.pow(input, 3) / (double) divideFactor;
    }

    /**
     * @return True if the drivetrain is pulling more than the allowed amount of
     *         amps.
     */
    public boolean overCurrent() {
        return reportCurrentDraw() > CURRENT_MAX;
    }

    public boolean underVoltage() {
        return this.pdp.getVoltage() < 8.5;
    }

    public void oneSideTurn(double leftPower, double rightPower){
        if(leftPower > 0 ){
            this.drive.arcadeDrive(0, -leftPower/2.0);
        } else {
            this.drive.arcadeDrive(0, rightPower/2.0);
        }
    }

    public void setTank(boolean isTank){
        this.isTank = isTank;
    }

}