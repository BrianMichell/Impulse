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
// import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;

public class Robot extends TimedRobot {

    DriverJoystick driver;
    // XboxController secondary;
    
    Hardware hw;
    
    Climber climber;
    Level2 level2;
    Drive drive;
    Hatch hatch;
    Shifter shift;

    Toggle shiftToggle;

    PID driveController;

    @Override
    public void robotInit() {
        driver = new DriverJoystick(0);
        // secondary = new XboxController(1);
        
        hw = new Hardware();
        
        climber = new Climber(hw);
        level2 = new Level2(hw);
        drive = new Drive(hw);
        hatch = new Hatch(hw);
        shift = new Shifter(hw);

        shiftToggle = new Toggle();
        hw.gyro.calibrateGX();

        //driveController = new PID(10.0, 100000000000.0, 0.0, hw.gyro, true);
        
        /*
        osc period is about 1.25 seconds at Kp = 10.0 Ki = 100000000000 Kd = 0
        with 50% output damping on total output
        */
        //driveController = new PID(10.0/5, 1.25/2, 1.25/3, hw.gyro, true);
        //double Ku = 10.0;
        //double Tu = 1.25;
        //driveController = new PID(Ku/5.0, (2*Ku)/Tu/5, Ku*Tu/15, hw.gyro, true);

        double Tu = 0.5;
        double Ku = 20.0;
        // driveController = new PID(20.0, 0.0, 0.0, hw.gyro, false);
        // driveController = new PID(Ku/5.0, Tu/2.0, Tu/3.0, hw.gyro, true);
        // driveController = new PID(Ku/5.0, (2*Ku)/Tu/5.0, Ku*Tu/15.0, hw.gyro, true);
        driveController = new PID(Ku/5.0, 0, Tu/3.0, hw.gyro, true);
        // driveController = new PID(Ku/5.0, 0, Ku*Tu/15.0, hw.gyro, true);

        // CameraServer.getInstance().startAutomaticCapture();
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

        // double theta, phi;
        // theta = secondary.getRawAxis(1);
        // phi = secondary.getRawAxis(5);
        // climber.manualDrive(theta, phi);

        shift.setInHighGear(shiftToggle.update(dRB));
        hatch.setOpen(dLB);

        // level2.actuate(secondary.getBumper(GenericHID.Hand.kLeft));
        SmartDashboard.putNumber("Gyro", hw.gyro.getGyroX());
        //SmartDashboard.putNumber("Accel X", hw.gyro.getAccelX());
        //SmartDashboard.putNumber("Accel Y", hw.gyro.getAccelY());
        //SmartDashboard.putNumber("Accel Z", hw.gyro.getAccelZ());

        if(dRT > 0.2 || dLT > 0.2) {
            driveController.disable();
            drive.setTank(true);
            drive.oneSideTurn(dLT, dRT);
        } else if(driver.joystick.getYButton()) {
            drive.setTank(true);
            driveController.setSetpoint(0.0);
            driveController.enable();
            hw.drive.arcadeDrive(0, driveController.output);
            // drive.calculateTurn(hw.gyro.getGyroX(), 0.0);
        } else if(driver.joystick.getAButton()) {
            drive.setTank(true);
            driveController.setSetpoint(180.0);
            driveController.enable();
            hw.drive.arcadeDrive(0, driveController.output);
            // drive.calculateTurn(hw.gyro.getGyroX(), 180.0);
        } else if(driver.joystick.getXButton()) {
            drive.setTank(true);
            driveController.setSetpoint(90.0);
            driveController.enable();
            hw.drive.arcadeDrive(0, driveController.output);
            // drive.calculateTurn(hw.gyro.getGyroX(), 90.0);
        } else if(driver.joystick.getBButton()) {
            drive.setTank(true);
            driveController.setSetpoint(270.0);
            driveController.enable();
            hw.drive.arcadeDrive(0, driveController.output);
            // drive.calculateTurn(hw.gyro.getGyroX(), 270.0);
        } else {
            driveController.disable();
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
        // driveController.disable();
    }
}
