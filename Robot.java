/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;

public class Robot extends TimedRobot {

    DriverJoystick driver;
    XboxController secondary;
    
    Hardware hw;
    
    Climber climber;
    Drive drive;
    Hatch hatch;

    VisionCalculator vision;

    // Toggle aggressiveRamp;

    DriverStation ds;

    @Override
    public void robotInit() {
        driver = new DriverJoystick(0);
        secondary = new XboxController(1);
        
        hw = new Hardware();
        
        climber = new Climber(hw);
        drive = new Drive(hw);
        hatch = new Hatch(hw);

        vision = new VisionCalculator(hw, new VisionGetter());

        // hw.gyro.calibrateGX();

        // aggressiveRamp = new Toggle();

        UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
        cam.setResolution(40, 40);
        cam.setFPS(30);

        ds = DriverStation.getInstance();
      }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void autonomousInit() {
        teleopInit();
    }

    @Override
    public void autonomousPeriodic() {
        teleopPeriodic();
    }

    @Override
    public void teleopInit(){
        drive.enable();
    }

    @Override
    public void teleopPeriodic() {
        //Collect all joystick inputs
        boolean dLB = driver.joystick.getBumper(GenericHID.Hand.kLeft);
        boolean dRB = driver.joystick.getBumper(GenericHID.Hand.kRight);
        double dRT = driver.joystick.getTriggerAxis(GenericHID.Hand.kRight);
        double dLT = driver.joystick.getTriggerAxis(GenericHID.Hand.kLeft);
        boolean dY = driver.joystick.getYButton();
        boolean dX = driver.joystick.getXButton();

        //Full speed climb
        boolean sY = secondary.getYButton();
        boolean sX = secondary.getXButton();

        double sLT = secondary.getTriggerAxis(GenericHID.Hand.kLeft);

        boolean sRB = secondary.getBumper(GenericHID.Hand.kRight);

        double rumbleIntensity = ds.getMatchTime() >= 28 && ds.getMatchTime() <= 30? 0.75 : 0;

        driver.joystick.setRumble(RumbleType.kRightRumble, rumbleIntensity);
        driver.joystick.setRumble(RumbleType.kLeftRumble, rumbleIntensity);
        secondary.setRumble(RumbleType.kRightRumble, rumbleIntensity);
        secondary.setRumble(RumbleType.kLeftRumble, rumbleIntensity);

        // double hip = secondary.getRawAxis(1);
        // climber.manualDrive(hip);

        // aggressiveRamp.update(dY);

        // SmartDashboard.putNumber("Gyro", hw.gyro.getGyroX());

        double climberSpeed = 0.0;
        double intakeSpeed = 0.0;

        if(dRT > 0.2 || dLT > 0.2) {
            drive.setTank(true);
            drive.oneSideTurn(dLT, dRT);
            vision.disable();
        } else if(dX) {
            vision.enable();
        } else if(sX && sY) {
            vision.disable();
            climberSpeed = 0.60;
            drive.updateSpeeds(-Math.abs(DriverJoystick.getForward()), -Math.abs(DriverJoystick.getTurn()), true);
        } else {
            vision.disable();
            drive.setTank(false);
            drive.updateSpeeds(-DriverJoystick.getForward(), -DriverJoystick.getTurn(), true);
        }

        if(sLT > 0.3) {
            climberSpeed = -sLT/2.0;
        }

        if(dLB) {
            intakeSpeed = -1.0;
        }
        if(dRB) {
            intakeSpeed = 1.0;
        }

        hatch.setSpeed(intakeSpeed);
        climber.manualDrive(climberSpeed);

    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void disabledPeriodic(){
        drive.disable();
        vision.disable();
    }
}