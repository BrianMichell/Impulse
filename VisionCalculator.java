package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

class VisionCalculator extends Subsystem{

    private DifferentialDrive drive;
    private VisionGetter visionGetter;
    private Encoder leftEncoder, rightEncoder;
    private MPU9250 gyro;

    public VisionCalculator(Hardware hw, VisionGetter vg) {
        this.drive = hw.drive;
        this.visionGetter = vg;
        this.leftEncoder = hw.leftDriveEncoder;
        this.rightEncoder = hw.rightDriveEncoder;
        this.gyro = hw.gyro;
    }

    @Override
    void actions() {
        double forward = encoderDelta(visionGetter.getEncoderDistance())  / 55;
        double turn = gyroDelta(visionGetter.getAngle()) / 55;
        this.drive.arcadeDrive(forward, turn);
    }

    @Override
    void haltSystem() {

    }

    private double encoderDelta(double targetDist) {
        return visionGetter.getAngle() > 0.0 ? leftEncoder.getDistance()-targetDist : rightEncoder.getDistance()-targetDist;
    }

    private double gyroDelta(double targetAngle) {
        return this.gyro.getGyroX() - targetAngle;
    }

    

}