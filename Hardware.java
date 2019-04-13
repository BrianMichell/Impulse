package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import com.ctre.phoenix.motorcontrol.can.*;

public class Hardware {

    // Motors
    private final WPI_VictorSPX leftDrive1, leftDrive2, leftDrive3, rightDrive1, rightDrive2, rightDrive3;
    public final DifferentialDrive drive;
    private final VictorSP hipOne, hipTwo;
    public final VictorSP intakeOne, intakeTwo;
    public final SpeedControllerGroup hip;

    // Sensors
    public final PowerDistributionPanel pdp;
    public final Encoder leftDriveEncoder;
    public final Encoder rightDriveEncoder;
    public final MPU9250 gyro;

    protected final int[] MOTORS = { 0, 1, 2, 15, 14, 13 };
    private final double DISTANCE_PER_PULSE = Math.PI * 6 / 255;

    public Hardware() {
        // Motors
        // leftDrive1 = new WPI_VictorSPX(1);
        // leftDrive2 = new WPI_VictorSPX(2);
        // leftDrive3 = new WPI_VictorSPX(3);
        // rightDrive1 = new WPI_VictorSPX(4);
        // rightDrive2 = new WPI_VictorSPX(5);
        // rightDrive3 = new WPI_VictorSPX(6);
        leftDrive1 = new WPI_VictorSPX(1);
        leftDrive2 = new WPI_VictorSPX(5);
        leftDrive3 = new WPI_VictorSPX(4);
        rightDrive1 = new WPI_VictorSPX(3);
        rightDrive2 = new WPI_VictorSPX(2);
        rightDrive3 = new WPI_VictorSPX(6);

        SpeedControllerGroup left = new SpeedControllerGroup(leftDrive1, leftDrive2, leftDrive3);
        SpeedControllerGroup right = new SpeedControllerGroup(rightDrive1, rightDrive2, rightDrive3);

        drive = new DifferentialDrive(left, right);

        hipOne = new VictorSP(0); //Victor 10
        hipTwo = new VictorSP(1); //Victor 8
        hip = new SpeedControllerGroup(hipOne, hipTwo);

        intakeOne = new VictorSP(2);
        intakeTwo = new VictorSP(3);

        // Sensors 
        pdp = new PowerDistributionPanel(0);
        leftDriveEncoder = new Encoder(8, 9);
        rightDriveEncoder = new Encoder(0, 1);
        leftDriveEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        rightDriveEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        gyro = new MPU9250();

        /**
         * Victor 9 = PWM 2
         * Victor 10 = PWM 0
         * Victor 7 = DEAD (Would have been 3)
         * Victor 8 = 1
         */

    }

}