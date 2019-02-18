package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Hatch extends Subsystem {

    private DoubleSolenoid hatchClamp;
    private boolean open;
    
    public Hatch(Hardware hw){
        super(hw, "Hatch");
        this.hatchClamp = hw.hatchClamp;
        this.open = true;
    }

    @Override
    protected void actions(){
        actuate();
    }

    @Override
    protected void haltSystem(){
    }

    private void actuate(){
        if(open){
            this.hatchClamp.set(Value.kReverse);
        } else {
            this.hatchClamp.set(Value.kForward);
        }
    }

    public void setOpen(boolean value){
        open = !value;
    }

}