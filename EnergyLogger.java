package frc.robot;

import java.io.*;
import edu.wpi.first.wpilibj.Timer;

class EnergyLogger implements Runnable {

    FileWriter out;
    double now;
    Hardware hw;
    Drive drive;
    boolean errorWriting;

    public int status;
    public final int ENABLED = 1;
    public final int DISABLED = 0;
    public final int SAVE = 2;

    public EnergyLogger(Hardware hw, Drive drive) {
        now = Timer.getFPGATimestamp();
        errorWriting = false;
        try {
            this.out = new FileWriter(Double.toString(now));
        } catch (Exception e){
            System.err.println(e);
            this.errorWriting = true;
        }
        this.hw = hw;
        this.drive = drive;
        this.status = DISABLED;
    }

    public void run() {
        try{
            out.append(buildHeadder());
        } catch (Exception e){
            errorWriting = true;
            System.err.println(e);
        }
        while(!Thread.interrupted()){
            if(errorWriting){
                save();
                break;
            }
            switch(status){
            case ENABLED:
                this.now = Timer.getFPGATimestamp();
                double voltage = this.hw.pdp.getVoltage();
                double forward = this.drive.forward;
                double turn = this.drive.turn;
                String line = String.format("%f,%f,%f,%f,", this.now, voltage, forward, turn);
                line += getCurrents();
                try{
                    out.append(line);
                } catch (Exception e){
                    errorWriting = true;
                    System.err.println(e);
                }
                break;
            case DISABLED:
                break;
            case SAVE:
                save();
                break;
            }
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

    public void enable() {
        status = ENABLED;
    }

    public void disable() {
        status = DISABLED;
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
        return "Time,Battery Voltage,Forward power input,Turn power input,L1,L2,L3,R1,R2,R3\n";
    }

}