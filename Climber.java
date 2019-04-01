package frc.robot;

// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

class Climber {

    // DriverStation ds;

    SpeedControllerGroup hip;

    public Climber(Hardware hw) {
        this.hip = hw.hip;
    }

    public void manualDrive(double hipSpeed) {
        if(isEndgame()) {
            hip.set(hipSpeed);
        }
    }

    /**
     * @return Has at least 130 seconds passed
     */
    private boolean isEndgame() {
        return true;
        // return ds.getMatchTime() <= 130;
    }

}