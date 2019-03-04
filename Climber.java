package frc.robot;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;


class Climber extends Subsystem {

    DriverStation ds;
    BuiltInAccelerometer accelerometer;
    Encoder ankleEncoder;
    Encoder kneeEncoder;

    SpeedControllerGroup ankle, knee;

    private double linkageOneSpeed;
    private double linkageTwoSpeed;

    private int stage;
    private final int DISABLED = 0;
    private final int STAGE_ONE = 1;
    private final int STAGE_TWO = 2;
    private final int STAGE_THREE = 3;
    private final int FINALIZE = 4;

    private final double RATE = 50.0;

    public Climber(Hardware hw){
        super(hw, "Climber");
        this.ds = DriverStation.getInstance();
        this.accelerometer = hw.accelerometer;
        this.ankleEncoder = hw.ankleEncoder;
        this.kneeEncoder = hw.kneeEncoder;
        this.stage = this.DISABLED;
        this.linkageOneSpeed = 0.0;
        this.linkageTwoSpeed = 0.0;
        this.ankle = hw.ankle;
        this.knee = hw.knee;
    }

    @Override
    protected void actions(){
        if(isEndgame()){
            //climb();
        }
    }

    public void manualDrive(double ankleSpeed, double kneeSpeed){
        ankle.set(ankleSpeed);
        knee.set(kneeSpeed);
    }

    private void climb(){
        switch(stage){
            case DISABLED:
                haltSystem();
                break;
            case STAGE_ONE:
                if(isTipping()){
                    recover();
                } else {
                    setAngles(0,0);
                }
                break;
            case STAGE_TWO:
                if(isTipping()){
                    recover();
                } else {
                    setAngles(0,0);
                }
                break;
            case STAGE_THREE:
                if(isTipping()){
                    recover();
                } else {
                    setAngles(0,0);
                }
                break;
            case FINALIZE:
                setAngles(0,0);
                break;
        }
    }

    /**
     * @return True if the acceleration due to gravity varies more than 1 meter per second in the x axis
     */
    private boolean isTipping(){
        double x = Math.abs(accelerometer.getX())-9.8;
        return x >= 1.0 || x <= 1.0; 
    }

    @Override
    protected void haltSystem(){
        this.linkageOneSpeed = 0.0;
        this.linkageTwoSpeed = 0.0;
    }

    /**
     * @return Has at least 130 seconds passed
     */
    private boolean isEndgame() {
        return ds.getMatchTime() <= 130;
    }

    /**
     * Applies power to the linkages to orient the bot
     * to be parallel to the floor again
     */
    private void recover(){
        //TODO Get logic to recover from tilt
    }

    /**
     * Applies the power to linkages to get them to 
     * the desired angle
     * @param theta Theta is the desired angle closer to the bot (linkageOne)
     * @param phi Phi is the desired angle closer to the foot (linkageTwo)
     */
    @Deprecated
    private void setAngles(int ankleAngle, int kneeAngle){
        
    }

}