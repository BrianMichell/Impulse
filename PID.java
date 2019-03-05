package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

class PID implements Runnable {

    private double kP, kI, kD;
    private Encoder encoder;
    private double setpoint;

    private long lastTimeMeasurement;
    private double previousError;
    // private double integral;

    public double output;
    private boolean state;

    private double maxOutput;
    
    /**
     * @param double kP The tuned proportional value
     * @param double kI The tuned integral value
     * @param double kD The tuned derivitive value
     * @param Encoder encoder The sensor used for feedback for the controller
     */
    public PID(double kP, double kI, double kD, Encoder encoder) {
        this(kP, kI, kD, encoder, 1.0);
    }
    
    /**
     * @param double maxOutput The absolute value of the maximum power able to be applied by the controller
     */
    public PID(double kP, double kI, double kD, Encoder encoder, double maxOutput){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.encoder = encoder;
        this.previousError = 0.0;
        // this.integral = 0.0;
        this.lastTimeMeasurement = System.nanoTime();
        this.setpoint = 0.0;
        this.output = 0.0;
        this.state = false;
        this.maxOutput = maxOutput;
        new Thread(this, "PID controller").start();
        
    }

    public void run(){
        while(!Thread.interrupted()) {
            if(this.state){
                long now = System.nanoTime();
                double dt = (now - this.lastTimeMeasurement) / (double) 1000000000; //Change of time (in seconds)
                double error = this.setpoint - this.encoder.get();
                // this.integral += error * dt; //Add to the rieman sum
                double derivative = (error - this.previousError) / dt;
                
                // this.output = normalize(kP * error + kI * this.integral + kD * derivative);
                this.output = normalize(kP * error + kD * derivative);
                
                this.previousError = error;
                this.lastTimeMeasurement = System.nanoTime();
            } else {
                lastTimeMeasurement = System.nanoTime();
            }
            SmartDashboard.putNumber("PID output", this.output);
        }
    }

    public void setSetpoint(double setpoint){
        this.setpoint = setpoint;
    }

    public void setPercentOutput(double percentOutput){
        this.maxOutput = percentOutput;
    }

    public void enable() {
        this.state = true;
    }

    public void disable() {
        this.state = false;
    }

    private double normalize(double value){
        return Math.max(-this.maxOutput, Math.min(value, this.maxOutput));
    }

    /**
     * If the plant is within +-5% of the target and outputting less than +-10% the
     * system is presumed to be at steadystate.
     */
    public boolean isSteadyState(){
        double percentError = (Math.abs(this.encoder.get() - this.setpoint) / this.setpoint) * 100.0;
        return (percentError <= 5 && Math.abs(this.output) < 0.1);
    }

    public void setGains(double kP, double kI, double kD){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

}