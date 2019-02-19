package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;

public class Hardware {

    // Motors
    public final VictorSPX2 leftDrive1, leftDrive2, leftDrive3, rightDrive1, rightDrive2, rightDrive3;
    public final DifferentialDrive drive;
    public final VictorSP thetaOne, thetaTwo, phiOne, phiTwo;

    // Pneumatics
    public final Compressor compressor = new Compressor();
    public final DoubleSolenoid shifter = new DoubleSolenoid(0, 1);
    public final DoubleSolenoid hatchClamp = new DoubleSolenoid(4, 6);
    public final DoubleSolenoid climberPistons = new DoubleSolenoid(5, 7);

    // Encoders
    public final Encoder encoderTheta, encoderPhi;
    
    // Sensors
    public final BuiltInAccelerometer accelerometer;
    public final PowerDistributionPanel pdp;
    public final MPU9250 gyro;

    protected final int[] MOTORS = { 0, 1, 2, 15, 14, 13 };

    public Hardware() {
        // Motors
        leftDrive1 = new VictorSPX2(1);
        leftDrive2 = new VictorSPX2(2);
        leftDrive3 = new VictorSPX2(3);
        rightDrive1 = new VictorSPX2(4);
        rightDrive2 = new VictorSPX2(5);
        rightDrive3 = new VictorSPX2(6);

        SpeedControllerGroup left = new SpeedControllerGroup(leftDrive1, leftDrive2, leftDrive3);
        SpeedControllerGroup right = new SpeedControllerGroup(rightDrive1, rightDrive2, rightDrive3);

        drive = new DifferentialDrive(left, right);

        thetaOne = new VictorSP(1);
        thetaTwo = new VictorSP(2);
        phiOne = new VictorSP(3);
        phiTwo = new VictorSP(4);

        // Encoders
        encoderTheta = new Encoder(0, 1);
        encoderPhi = new Encoder(2, 3);

        // Sensors
        accelerometer = new BuiltInAccelerometer();
        pdp = new PowerDistributionPanel(0);
        gyro = new MPU9250();

        thetaTwo.setInverted(true);
        phiTwo.setInverted(true);
    }

}