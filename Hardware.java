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

    //Motors
    //public final SpeedControllerGroup leftDrive = new SpeedControllerGroup(new Victor(2), new Victor(3));
    //public final SpeedControllerGroup rightDrive =  new SpeedControllerGroup(new Victor(0), new Victor(1));
    public final Talon leftDrive = new Talon(0);
    public final Talon rightDrive = new Talon(1);
    public final DifferentialDrive drive = new DifferentialDrive(leftDrive, rightDrive);
    public final Victor thetaOne = new Victor(4);
    public final Victor thetaTwo = new Victor(5);
    public final Victor phiOne = new Victor(6);
    public final Victor phiTwo = new Victor(7);
    
    //Pneumatics
    //public final Compressor compressor = new Compressor();
    //public final DoubleSolenoid leftShift = new DoubleSolenoid(0, 1);
    //public final DoubleSolenoid rightShift = new DoubleSolenoid(2, 3);
    
    //Sensors
    public final Encoder encoderTheta = new Encoder(0, 1);
    public final Encoder encoderPhi = new Encoder(2, 3);
    public final BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();
    public final PowerDistributionPanel pdp = new PowerDistributionPanel(0);


    protected final int[] MOTORS = {0, 1, 2, 15, 14, 13};


    public Hardware() {
        this.thetaTwo.setInverted(true);
        this.phiTwo.setInverted(true);
    }

}