package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.SpeedController;

public class VictorSPX2 implements SpeedController {
    final VictorSPX v;

    protected VictorSPX2(int devNum) {
        v = new VictorSPX(devNum);
    }

    @Override
    public void pidWrite(double output) {
        // TODO: Implement, but not necessary
    }

    @Override
    public void set(double speed) {
        v.set(ControlMode.PercentOutput, speed);
    }

    @Override
    public double get() {
        return v.getMotorOutputPercent();
    }

    @Override
    public void setInverted(boolean isInverted) {
        v.setInverted(isInverted);
    }

    @Override
    public boolean getInverted() {
        return v.getInverted();
    }

    @Override
    public void disable() {
        set(0);
    }

    @Override
    public void stopMotor() {
        set(0);
    }
}