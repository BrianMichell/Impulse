package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.VictorSP;

import com.ctre.phoenix.motorcontrol.can.*;

public class Hardware {

    // Motors
    public final VictorSPX leftDrive1, leftDrive2, leftDrive3, rightDrive1, rightDrive2, rightDrive3;
    public final HackedDD drive;
    public final VictorSP thetaOne, thetaTwo, phiOne, phiTwo;

    // Pneumatics
    public final Compressor compressor = new Compressor();
    public final DoubleSolenoid leftShift = new DoubleSolenoid(0, 1);
    public final DoubleSolenoid rightShift = new DoubleSolenoid(2, 3);

    // Encoders
    public final Encoder encoderTheta, encoderPhi;
    
    // Sensors
    public final BuiltInAccelerometer accelerometer;
    public final PowerDistributionPanel pdp;

    protected final int[] MOTORS = { 0, 1, 2, 15, 14, 13 };

    public Hardware() {
        // Motors
        leftDrive1 = new VictorSPX(0);
        leftDrive2 = new VictorSPX(1);
        leftDrive3 = new VictorSPX(3);
        rightDrive1 = new VictorSPX(4);
        rightDrive2 = new VictorSPX(5);
        rightDrive3 = new VictorSPX(6);

        //SpeedControllerGroup left = new SpeedControllerGroup(leftDrive1, leftDrive2, leftDrive3);
        //SpeedControllerGroup right = new SpeedControllerGroup(rightDrive1, rightDrive2, rightDrive3);

        drive = new HackedDD(leftDrive1, leftDrive2, leftDrive3, rightDrive1, rightDrive2, rightDrive3);
        //drive = new DifferentialDrive(leftDrive, rightDrive);

        thetaOne = new VictorSP(4);
        thetaTwo = new VictorSP(5);
        phiOne = new VictorSP(6);
        phiTwo = new VictorSP(7);

        // Encoders
        encoderTheta = new Encoder(0, 1);
        encoderPhi = new Encoder(2, 3);

        // Sensors
        accelerometer = new BuiltInAccelerometer();
        pdp = new PowerDistributionPanel(0);

        thetaTwo.setInverted(true);
        phiTwo.setInverted(true);
    }

}