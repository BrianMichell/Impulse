package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

class VisionCalculator extends Subsystem{

    private DifferentialDrive drive;
    private VisionGetter visionGetter;
    private Encoder leftEncoder, rightEncoder;

    public VisionCalculator(Hardware hw, VisionGetter vg) {
        this.drive = hw.drive;
        this.visionGetter = vg;
        this.leftEncoder = hw.leftDriveEncoder;
        this.rightEncoder = hw.rightDriveEncoder;
    }

    @Override
    void actions() {
        double forward = visionGetter.getEncoderDistance() / 55;
        double turn = visionGetter.getAngle() / 55;
        this.drive.arcadeDrive(forward, turn);
    }

    @Override
    void haltSystem() {

    }

    

}