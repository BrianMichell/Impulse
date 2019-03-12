package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

class Level2 {

    DoubleSolenoid climberPistons;

    public Level2(Hardware hw){
        this.climberPistons = hw.climberPistons;
    }

    public void actuate(boolean extend){
        if(!extend){
            this.climberPistons.set(Value.kForward);
        } else {
            this.climberPistons.set(Value.kReverse);
        }
    }

}