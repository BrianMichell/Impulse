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
    private final VictorSP leftDrive1, leftDrive2, rightDrive1, rightDrive2;
    public final DifferentialDrive drive;
    private final VictorSP ankleOne, ankleTwo, kneeOne, kneeTwo;
    public final SpeedControllerGroup ankle, knee;

    // Pneumatics
    public final Compressor compressor = new Compressor();
    public final DoubleSolenoid shifter = new DoubleSolenoid(0, 1);
    public final DoubleSolenoid hatchClamp = new DoubleSolenoid(4, 6);
    public final DoubleSolenoid climberPistons = new DoubleSolenoid(5, 7);

    // Encoders
    public final Encoder ankleEncoder, kneeEncoder;
    
    // Sensors
    public final BuiltInAccelerometer accelerometer;
    public final PowerDistributionPanel pdp;
    public final MPU9250 gyro;

    protected final int[] MOTORS = { 0, 1, 2, 15, 14, 13 };

    public Hardware() {
        // Motors
        leftDrive1 = new VictorSP(0);
        leftDrive2 = new VictorSP(1);
        rightDrive1 = new VictorSP(2);
        rightDrive2 = new VictorSP(3);

        SpeedControllerGroup left = new SpeedControllerGroup(leftDrive1, leftDrive2);
        SpeedControllerGroup right = new SpeedControllerGroup(rightDrive1, rightDrive2);

        drive = new DifferentialDrive(left, right);

        ankleOne = new VictorSP(5);
        ankleTwo = new VictorSP(6);
        kneeOne = new VictorSP(7);
        kneeTwo = new VictorSP(8);
        ankle = new SpeedControllerGroup(ankleOne, ankleTwo);
        knee = new SpeedControllerGroup(kneeOne, kneeTwo);

        // Encoders
        ankleEncoder = new Encoder(0, 1);
        kneeEncoder = new Encoder(2, 3);

        // Sensors
        accelerometer = new BuiltInAccelerometer();
        pdp = new PowerDistributionPanel(0);
        gyro = new MPU9250();

    }

}