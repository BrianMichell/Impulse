/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID;

public class Robot extends TimedRobot {

    /*
    DriverJoystick driver;
    
    Hardware hw;
    
    Climber climber;
    Drive drive;
    Hatch hatch;
    Shifter shift;

    Toggle toggle;

    EnergyLogger logger;
    */

    MPU9250 gyro;

    @Override
    public void robotInit() {
        //driver = new DriverJoystick(0);
        
        //hw = new Hardware();
        
        //climber = new Climber(hw);
        //drive = new Drive(hw);
        //hatch = new Hatch(hw);
        //shift = new Shifter(hw);

        //toggle = new Toggle();

        //logger = new EnergyLogger(hw, drive);

        gyro = new MPU9250();
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
        /*
        climber.enable();
        drive.enable();
        hatch.enable();
        shift.enable();

        if(logger.status == logger.DISABLED){
            logger.enable();
        }
        */
    }

    @Override
    public void teleopPeriodic() {
        //Collect all joystick inputs
        //boolean dRB = driver.joystick.getBumper(GenericHID.Hand.kRight);

        //shift.setInHighGear(toggle.update(dRB));

        //drive.updateSpeeds(driver.getForward(), driver.getTurn());
    }

    @Override
    public void testPeriodic() {
        //gyro.printData();
        //gyro.printAccel();
        //gyro.printGyro();
        gyro.getGyroX();
    }

    @Override
    public void disabledPeriodic(){
        /*
        climber.disable();
        drive.disable();
        hatch.disable();
        shift.disable();
        if(logger.status == logger.ENABLED){
            logger.status = logger.SAVE;
        }
        */
    }
}
