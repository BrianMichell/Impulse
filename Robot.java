/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;

public class Robot extends TimedRobot {

    DriverJoystick driver;
    XboxController secondary;
    
    Hardware hw;
    
    Climber climber;
    Drive drive;
    Hatch hatch;

    @Override
    public void robotInit() {
        driver = new DriverJoystick(0);
        secondary = new XboxController(1);
        
        hw = new Hardware();
        
        climber = new Climber(hw);
        drive = new Drive(hw);
        hatch = new Hatch(hw);

        // hw.gyro.calibrateGX();

        UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
        cam.setResolution(40, 40);
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
        hatch.enable();
    }

    @Override
    public void teleopPeriodic() {
        //Collect all joystick inputs
        boolean dLB = driver.joystick.getBumper(GenericHID.Hand.kLeft);
        double dRT = driver.joystick.getTriggerAxis(GenericHID.Hand.kRight);
        double dLT = driver.joystick.getTriggerAxis(GenericHID.Hand.kLeft);

        //Full speed climb
        boolean sA = secondary.getAButton();
        boolean sB = secondary.getBButton();
        //Half speed climb
        boolean sY = secondary.getYButton();
        boolean sX = secondary.getXButton();
        double sLT = secondary.getTriggerAxis(GenericHID.Hand.kLeft);


        // double hip = secondary.getRawAxis(1);
        // climber.manualDrive(hip);
        hatch.setOpen(dLB);

        // SmartDashboard.putNumber("Gyro", hw.gyro.getGyroX());

        double climberSpeed = 0.0;

        if(dRT > 0.2 || dLT > 0.2) {
            drive.setTank(true);
            drive.oneSideTurn(dLT, dRT);
        } else if(sA && sB) { //Hold both buttons to begin climb
            climberSpeed = -1.0;
            drive.updateSpeeds(-Math.abs(DriverJoystick.getForward()), -Math.abs(DriverJoystick.getTurn())); //Limit driver control to forward only
        } else if(sX && sY) {
            climberSpeed = -0.5;
            drive.updateSpeeds(-Math.abs(DriverJoystick.getForward()), -Math.abs(DriverJoystick.getTurn()));
        } else {
            drive.setTank(false);
            drive.updateSpeeds(-DriverJoystick.getForward(), -DriverJoystick.getTurn());
        }

        if(sLT > 0.3) {
            climberSpeed = sLT/2.0;
        }

        climber.manualDrive(climberSpeed);

    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void disabledPeriodic(){
        drive.disable();
        hatch.disable();
    }
}
