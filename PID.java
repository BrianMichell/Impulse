package frc.robot;

class PID implements Runnable {

    private double kP, kI, kD;
    private MPU9250 gyro;
    private double setpoint;

    private long lastTimeMeasurement;
    private double previousError;
    private double integral;

    public double output;
    private boolean state;
    
    public PID(double kP, double kI, double kD, MPU9250 gyro) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.gyro = gyro;
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
                this.output = kP * error + kI * this.integral + kD * derivative;
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