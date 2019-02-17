package frc.robot;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;

class HackedDD {

    private VictorSPX l1, l2, l3, r1, r2, r3;

    public HackedDD(VictorSPX l1, VictorSPX l2, VictorSPX l3, VictorSPX r1, VictorSPX r2, VictorSPX r3){
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public void arcadeDrive(double forward, double turn){
        double rightOutput, leftOutput;
        if(forward >= 0.0){
            if(turn >= 0.0){
                leftOutput = 1.0;
                rightOutput = forward - turn;
            } else {
                leftOutput = forward + turn;
                rightOutput = 1.0;
            }
        } else {
            if(turn >= 0.0){
                leftOutput = forward + turn;
                rightOutput = 1.0;
            } else {
                leftOutput = 1.0;
                rightOutput = forward - turn;
            }
        }

        l1.set(ControlMode.PercentOutput, leftOutput);
        l2.set(ControlMode.PercentOutput, leftOutput);
        l3.set(ControlMode.PercentOutput, leftOutput);
        r1.set(ControlMode.PercentOutput, rightOutput);
        r2.set(ControlMode.PercentOutput, rightOutput);
        r3.set(ControlMode.PercentOutput, rightOutput);
    }

    public void stopMotor(){
        l1.set(ControlMode.PercentOutput, 0);
        l2.set(ControlMode.PercentOutput, 0);
        l3.set(ControlMode.PercentOutput, 0);
        r1.set(ControlMode.PercentOutput, 0);
        r2.set(ControlMode.PercentOutput, 0);
        r3.set(ControlMode.PercentOutput, 0);
    }

}