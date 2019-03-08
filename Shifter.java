package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Shifter extends Subsystem {

    private DoubleSolenoid shifter;

    private boolean highGear;

    public Shifter(Hardware hw){
        this.shifter = hw.shifter;
        this.highGear = false;

        start("Shifter");
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
            this.shifter.set(Value.kForward);
        } else {
            this.shifter.set(Value.kReverse);
        }
    }

    /**
     * To be coupled with the Toggle class
     * @param value The status of the shift button
     */
    public void setInHighGear(boolean value){
        highGear = !value;
    }

    public boolean isHighGear(){
        return highGear;
    }

}