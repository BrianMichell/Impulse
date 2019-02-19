package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Subsystem {

    private final DifferentialDrive drive;
    private final PowerDistributionPanel pdp;

    private double maxDraw = 0;

    public double forward;
    public double turn;
    // private int[] MOTORS;

    private final int CURRENT_MAX = 120;

    public Drive(Hardware hw) {
        super(hw, "Drive");
        this.drive = hw.drive;
        this.pdp = hw.pdp;
        // this.MOTORS = hw.MOTORS;
    }

    @Override
    protected void actions() {
        double _forward = DriverJoystick.getForward();
        double _turn = DriverJoystick.getTurn();
        updateSpeeds(_forward, _turn);
        this.drive.arcadeDrive(forward, turn);
        SmartDashboard.putNumber("Forward speed", forward);
        SmartDashboard.putNumber("Turn speed", turn);
    }

    @Override
    protected void haltSystem() {
        this.drive.stopMotor();
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
    public void updateSpeeds(double _forward, double _turn) {

        double forwardChange, turnChange;
        forwardChange = calculateIncrease(_forward);
        turnChange = calculateIncrease(_turn);

        // Flip the signs so the power creeps down
        if (Math.abs(forwardChange) < Math.abs(this.forward)) {
            if (forwardChange > this.forward) {
                forwardChange *= -1.0;
            }
            if (forwardChange == 0) {
                forwardChange = this.forward / 1.1125;
            }
        }

        if (Math.abs(turnChange) < Math.abs(this.turn)) {
            if (turnChange > this.turn) {
                turnChange *= -1;
            }
            if (turnChange == 0) {
                turnChange = this.turn / 1.1125;
            }
        }

        
        this.forward += forwardChange;
        this.turn += turnChange;
        if (Math.abs(this.forward) > Math.abs(_forward)) {
            this.forward = _forward;
        }
        if (Math.abs(this.turn) > Math.abs(_turn)) {
            this.turn = _turn;
        }
        System.out.println(this.forward);

    }

    private double calculateIncrease(double input) {
        int divideFactor = 5;
        //if (overCurrent()) {
        if(underVoltage()){
            input *= -1.0;
            divideFactor *= 10;
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

}