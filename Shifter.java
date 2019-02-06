package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Shifter extends Subsystem {

    //private DoubleSolenoid leftShift;
    //private DoubleSolenoid rightShift;

    private boolean highGear;

    public Shifter(Hardware hw){
        super(hw, "Shifter");
        //this.leftShift = hw.leftShift;
        //this.rightShift = hw.rightShift;
        this.highGear = false;
    }

    @Override
    protected void actions(){
        shift();
    }

    @Override
    protected void haltSystem(){

    }

    /**
     * Handles the actual shifting
     */
    private void shift(){
        if(highGear){
            //this.leftShift.set(Value.kForward);
            //this.rightShift.set(Value.kForward);
        } else {
            //this.leftShift.set(Value.kReverse);
            //this.rightShift.set(Value.kReverse);
        }
    }

    /**
     * To be coupled with the Toggle class
     * @param value The status of the shift button
     */
    public void setInHighGear(boolean value){
        highGear = !value;
    }

}