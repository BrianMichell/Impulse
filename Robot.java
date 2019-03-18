/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;

public class Robot extends TimedRobot {

    DriverJoystick driver;
    XboxController secondary;
    
    Hardware hw;
    
    Climber climber;
    Level2 level2;
    Drive drive;
    Hatch hatch;
    Shifter shift;

    Toggle shiftToggle;

    @Override
    public void robotInit() {
        driver = new DriverJoystick(0);
        secondary = new XboxController(1);
        
        hw = new Hardware();
        
        climber = new Climber(hw);
        level2 = new Level2(hw);
        drive = new Drive(hw);
        hatch = new Hatch(hw);
        shift = new Shifter(hw);

        shiftToggle = new Toggle();
        // hw.gyro.calibrateGX();

        UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
        cam.setResolution(40, 40);
      }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("Knee encoder", hw.kneeEncoder.get());
        SmartDashboard.putNumber("Hip encoder", hw.hipEncoder.get());
        SmartDashboard.putNumber("Stage", climber.stage);
        SmartDashboard.putNumber("Accel Z", hw.accelerometer.getZ());
        SmartDashboard.putNumber("Accel Y", hw.accelerometer.getY());
        SmartDashboard.putNumber("Accel X", hw.accelerometer.getX());
        SmartDashboard.putNumber("Tilt Angle", Math.asin(hw.accelerometer.getY()) * 180 / Math.PI);
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
        climber.enable();
        drive.enable();
        hatch.enable();
        shift.enable();
    }

    @Override
    public void teleopPeriodic() {
        //Collect all joystick inputs
        boolean dRB = driver.joystick.getBumper(GenericHID.Hand.kRight);
        boolean dLB = driver.joystick.getBumper(GenericHID.Hand.kLeft);
        double dRT = driver.joystick.getTriggerAxis(GenericHID.Hand.kRight);
        double dLT = driver.joystick.getTriggerAxis(GenericHID.Hand.kLeft);

        boolean sA = secondary.getAButton();
        boolean sB = secondary.getBButton();

        // double knee = secondary.getRawAxis(1);
        // double hip = secondary.getRawAxis(5);
        // climber.manualDrive(knee, hip);

        shift.setInHighGear(shiftToggle.update(dRB));
        hatch.setOpen(dLB);

        level2.actuate(driver.joystick.getYButton() || driver.joystick.getXButton() || driver.joystick.getBButton());
        // SmartDashboard.putNumber("Gyro", hw.gyro.getGyroX());

        climber.requestClimb(sA && sB);

        if(dRT > 0.2 || dLT > 0.2) {
            drive.setTank(true);
            drive.oneSideTurn(dLT, dRT);
        } else if(sA && sB) { //Hold both buttons to begin climb
            drive.updateSpeeds(-Math.abs(DriverJoystick.getForward()), -Math.abs(DriverJoystick.getTurn()), false); //Limit driver control to forward only
            // climber.requestClimb(true);
        } 
        else {
            drive.setTank(false);
            drive.updateSpeeds(DriverJoystick.getForward(), DriverJoystick.getTurn(), shift.isHighGear());
        }

    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void disabledPeriodic(){
        climber.disable();
        drive.disable();
        hatch.disable();
        shift.disable();
    }
}
