package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

//import com.ctre.phoenix.motorcontrol.can.*;

/*
public interface Hardware {
    public final SpeedControllerGroup leftDrive = new SpeedControllerGroup(new Victor(2), new Victor(3));
    public final SpeedControllerGroup rightDrive =  new SpeedControllerGroup(new Victor(0), new Victor(1));
    public final DifferentialDrive drive = new DifferentialDrive(leftDrive, rightDrive);
    public final PowerDistributionPanel pdp = new PowerDistributionPanel(0);
}
*/

public class Hardware {

    // Motors
    // public final SpeedControllerGroup leftDrive = new SpeedControllerGroup(new
    // Victor(2), new Victor(3));
    // public final SpeedControllerGroup rightDrive = new SpeedControllerGroup(new
    // Victor(0), new Victor(1));
    public final Talon leftDrive, rightDrive;
    public final DifferentialDrive drive;
    public final Victor thetaOne, thetaTwo, phiOne, phiTwo;

    // Pneumatics
    // public final Compressor compressor = new Compressor();
    // public final DoubleSolenoid leftShift = new DoubleSolenoid(0, 1);
    // public final DoubleSolenoid rightShift = new DoubleSolenoid(2, 3);

    // Encoders
    public final Encoder encoderTheta, encoderPhi, driveEncoderLeft, driveEncoderRight;
    
    // Sensors
    public final BuiltInAccelerometer accelerometer;
    public final PowerDistributionPanel pdp;

    protected final int[] MOTORS = { 0, 1, 2, 15, 14, 13 };

    public Hardware() {
        // Motors
        leftDrive = new Talon(0);
        rightDrive = new Talon(1);

        drive = new DifferentialDrive(leftDrive, rightDrive);

        thetaOne = new Victor(4);
        thetaTwo = new Victor(5);
        phiOne = new Victor(6);
        phiTwo = new Victor(7);

        // Encoders
        encoderTheta = new Encoder(0, 1);
        encoderPhi = new Encoder(2, 3);
        driveEncoderLeft = new Encoder(4, 5);
        driveEncoderRight = new Encoder(6, 7);

        // Sensors
        accelerometer = new BuiltInAccelerometer();
        pdp = new PowerDistributionPanel(0);

        thetaTwo.setInverted(true);
        phiTwo.setInverted(true);
    }

}