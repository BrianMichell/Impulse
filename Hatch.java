package frc.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class Hatch {

    private VictorSP intakeOne, intakeTwo;

    public Hatch(Hardware hw) {
        this.intakeOne = hw.intakeOne;
        this.intakeTwo = hw.intakeTwo;
    }

    public void setSpeed(double speed) {
        this.intakeOne.setSpeed(speed);
        this.intakeTwo.setSpeed(-speed);
    }

}