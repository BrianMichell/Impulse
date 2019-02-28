package frc.robot;

class PID implements Runnable {

    private double kP, kI, kD;
    private MPU9250 gyro;
    private double setpoint;

    private boolean zeiglerNicholasMethod;

    private long lastTimeMeasurement;
    private double previousError;
    private double integral;

    public double output;
    private boolean state;
    
    /**
     * @param double kP The tuned proportional value
     * @param double kI The tuned integral value
     * @param double kD The tuned derivitive value
     * @param MPU9250 gyro The sensor used for feedback for the controller
     * @param boolean zeiglerNicholsMethod True if the K values were calculated to use the non interactive PID method, false for the parallel algorithm.
     */
    public PID(double kP, double kI, double kD, MPU9250 gyro, boolean nonInteractiveAlgorithm) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.gyro = gyro;
        this.zeiglerNicholasMethod = nonInteractiveAlgorithm;
        this.previousError = 0.0;
        this.integral = 0.0;
        this.lastTimeMeasurement = System.nanoTime();
        this.setpoint = 0.0;
    }

    public void run(){
        while(!Thread.interrupted()) {
            if(this.state){
                long now = System.nanoTime();
                double dt = (now - this.lastTimeMeasurement) / (double) 1000000000;
                double error = this.setpoint - (this.gyro.getGyroX() % 360);
                this.integral += error * dt;
                double derivative = (error - this.previousError) / dt;
                if(this.zeiglerNicholasMethod) {
                    this.output = this.kP * (error + (1/kI * this.integral) + (kD * derivative));
                } else {
                    this.output = kP * error + kI * this.integral + kD * derivative;
                }
                this.previousError = error;
                this.lastTimeMeasurement = System.nanoTime();
            } else {
                lastTimeMeasurement = System.nanoTime();
            }
        }
    }

    public void setSetpoint(double setpoint){
        this.setpoint = setpoint;
    }

    public void enable() {
        this.state = true;
    }

    public void disable() {
        this.state = false;
    }

}