package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import com.ctre.phoenix.motorcontrol.can.*;

public class Hardware {

    // Motors
    private final WPI_VictorSPX leftDrive1, leftDrive2, leftDrive3, rightDrive1, rightDrive2, rightDrive3;
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
    // public final MPU9250 gyro;

    protected final int[] MOTORS = { 0, 1, 2, 15, 14, 13 };

    public Hardware() {
        // Motors
        leftDrive1 = new WPI_VictorSPX(1);
        leftDrive2 = new WPI_VictorSPX(2);
        leftDrive3 = new WPI_VictorSPX(3);
        rightDrive1 = new WPI_VictorSPX(4);
        rightDrive2 = new WPI_VictorSPX(5);
        rightDrive3 = new WPI_VictorSPX(6);

        SpeedControllerGroup left = new SpeedControllerGroup(leftDrive1, leftDrive2, leftDrive3);
        SpeedControllerGroup right = new SpeedControllerGroup(rightDrive1, rightDrive2, rightDrive3);

        drive = new DifferentialDrive(left, right);

        ankleOne = new VictorSP(2); // Victor 9
        ankleTwo = new VictorSP(3); // Victor 7 (N/A)
        kneeOne = new VictorSP(0); //Victor 10
        kneeTwo = new VictorSP(1); //Victor 8
        kneeTwo.setInverted(true);
        ankle = new SpeedControllerGroup(ankleOne, ankleTwo);
        knee = new SpeedControllerGroup(kneeOne, kneeTwo);

        // Encoders
        ankleEncoder = new Encoder(0, 1);
        kneeEncoder = new Encoder(2, 3);

        ankleEncoder.reset();
        kneeEncoder.reset();

        // Sensors
        accelerometer = new BuiltInAccelerometer();
        pdp = new PowerDistributionPanel(0);
        // gyro = new MPU9250();

        /**
         * Victor 9 = PWM 2
         * Victor 10 = PWM 0
         * Victor 7 = DEAD (Would have been 3)
         * Victor 8 = 1
         */

    }

}