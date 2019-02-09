package frc.robot;

import java.io.*;
import edu.wpi.first.wpilibj.Timer;

class EnergyLogger implements Runnable {

    FileWriter out;
    double now;
    Hardware hw;
    Drive drive;
    boolean errorWriting;

    public EnergyLogger(Hardware hw, Drive drive) {
        now = Timer.getFPGATimestamp();
        this.hw = hw;
        this.drive = drive;
        errorWriting = false;
    }

    public void run() {
        try{
            out.append(buildHeadder());
        } catch (Exception e){
            errorWriting = true;
            System.err.println(e);
        }
        while(!Thread.interrupted()){
            this.now = Timer.getFPGATimestamp();
            double voltage= this.hw.pdp.getVoltage();

        }

    }

    private void save() {
        try{
            out.flush();
            out.close();
        } catch (Exception e){
            System.err.println(e);
        }
    }

    private void enable() {

    }

    private void disable() {

    }

    private String getCurrents(){
        double l1, l2, l3, r1, r2, r3;
        l1 = this.hw.pdp.getCurrent(0);
        l2 = this.hw.pdp.getCurrent(1);
        l3 = this.hw.pdp.getCurrent(2);
        r1 = this.hw.pdp.getCurrent(13);
        r2 = this.hw.pdp.getCurrent(14);
        r3 = this.hw.pdp.getCurrent(15);
        String ret = String.format("%f,%f,%f,%f,%f,%f\n", l1, l2, l3, r1, r2, r3);
        return ret;
    }

    private String buildHeadder(){
        return "Time,Battery Voltage,Power Input,L1,L2,L3,R1,R2,R3\n";
    }

}