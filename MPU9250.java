package frc.robot;

import java.nio.ByteBuffer;
import java.lang.System;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;

//TODO Implement runnable for constant update of sensor information
class MPU9250 implements Runnable {

    //Register constants
    private final int MPU9250_ADDRESS = 0x68;
    private final int MAG_ADDRESS = 0x0C;

    //Gyro register constants
    public final int GYRO_250_DPS = 0x00;
    public final int GYRO_500_DPS = 0x08;
    public final int GYRO_1000_DPS = 0x10;
    public final int GYRO_2000_DPS = 0x18;

    private final double SCALE_250_DPS = 131;
    private final double SCALE_500_DPS = 65.5;
    private final double SCALE_1000_DPS = 32.8;
    private final double SCALE_2000_DPS = 16.4;

    //Accelerometer register constants
    public final int ACC_SCALE_2_G = 0x00;
    public final int ACC_SCALE_4_G = 0x08;
    public final int ACC_SCALE_8_G = 0x10;
    public final int ACC_SCALE_16_G = 0x18;
    
    private final I2C sensor;
    
    //The change in time and last time gyro x-axis was updated
    //TODO Update structure to do all gyro axes at once (starting with primary axis to ensure best accuracy)
    private double deltaTGX; //This does not need to be an instance variable 
    private long lastTGX;
    private double gX;

    public MPU9250(){
        this(0x10, 0x10);
    }
    
    //TODO change gyroDPS to instance variable to allow for headding calculations
    public MPU9250(int gyroDPS, int accelScale){
        sensor = new I2C(I2C.Port.kOnboard, this.MPU9250_ADDRESS);
        //sensor.write(29, 0x06); //Set accelerometer low pass filter to 5Hz
        //sensor.write(26, 0x06); //Set gyro low pass filter to 5Hz
        sensor.write(27, gyroDPS); //Set gyro to desired rate of change
        sensor.write(28, accelScale); //Set accelerometer to desired scale
        this.deltaTGX = 0;
        this.lastTGX = System.nanoTime();
        //this.lastTGX = System.currentTimeMillis(); //Millis() was too slow.
        gX = 1.0; //Start at 1 degree
        new Thread(this, "MPU9250").start();
    }

    public void run(){
        while(!Thread.interrupted()){
            updateGyroX();
            Timer.delay(0.0001);
        }
    }

    public double getGyroX(){
        System.out.println("Gyro X: " + this.gX);
        return this.gX;
    }

    private void updateGyroX(){
        byte[] dataBuffer = read(0x43,2);
        int xTmp = dataBuffer[0]<<8 | dataBuffer[1];
        xTmp += 37; //Hardcoded bias`
        //long now = System.currentTimeMillis();
        long now = System.nanoTime();
        deltaTGX = (double) (now - lastTGX) / 1000000000;  //Explicit double cast required.
        //deltaTGX = (double)(now - lastTGX) / 1000;
        lastTGX = now;
        gX += (double)(xTmp * deltaTGX) / SCALE_1000_DPS;// * (double)(2000/32768); //Omega * dt * resolution
    }
    
    private void updateGyroY(){
        /*
        byte[] dataBuffer = read(0x45, 2);
        int yTmp = dataBuffer[0]<<8 | dataBuffer[1];
        yTmp += 37; //Hardcoded bias
        long now = System.nanoTime();
        deltaTGY = (double) (now - lastTGY) / 1000000000;
        lastTGY = now;
        gY += (double)(yTmp * deltaTGY) / SCALE_1000_DPS;
        */
    }

    public int getGyroY(){
        //return gY;
        return 0;
    }

    private void updateGyroZ(){
        /*
        byte[] dataBuffer = read(0x47, 2);
        int zTmp = dataBuffer[0]<<8 | dataBuffer[1];
        zTmp += 37; //Hardcoded bias
        long now = System.nanoTime();
        deltaTGZ = (double) (now - lastTGZ) / 1000000000;
        lastTGZ = now;
        gZ += (double)(zTmp * deltaTGZ) / SCALE_1000_DPS;
        */
    }
    
    public int getGyroZ(){
        //return gZ;
        return 0;
    }
    
    public void printData(){
        byte[] dataBuffer = new byte[14];
        sensor.read(MPU9250_ADDRESS, 14, dataBuffer);
        ByteBuffer compBuffer = ByteBuffer.wrap(dataBuffer);
        int aX, aY, aZ, gX, gY, gZ;
        aX = -(dataBuffer[0]<<8 | dataBuffer[1]);
        aY = -(dataBuffer[2]<<8 | dataBuffer[3]);
        aZ = dataBuffer[4]<<8 | dataBuffer[5];

        gX = -(compBuffer.get(8)<<8 | compBuffer.get(9));
        gY = -(compBuffer.get(10)<<8 | compBuffer.get(11));
        gZ = compBuffer.get(12)<<8 | compBuffer.get(13);

        System.out.println("Accelerometer X: " + aX);
        System.out.println("Accelerometer Y: " + aY);
        System.out.println("Accelerometer Z: " + aZ);
        System.out.println("Gyro X: " + gX);
        System.out.println("Gyro Y: " + gY);
        System.out.println("Gyro Z: " + gZ);
    }

    public void printAccel(){
        byte[] accelRaw = new byte[6];
        sensor.read(0x3B, 6, accelRaw);
        System.out.println("One: " + ((accelRaw[0]<<8) | accelRaw[1]));
        System.out.println("Two: " + ((accelRaw[2]<<8) | accelRaw[3]));
        System.out.println("Three: "+ ((accelRaw[4]<<8) | accelRaw[5]));
    }
    
    public void printGyro(){
        byte[] rawGyro = new byte[6];
        sensor.read(0x43, 6, rawGyro);
        System.out.println("One: " + ((rawGyro[0]<<8) | rawGyro[1]));
    }
    
    private byte[] read(int address, int elements){
        byte[] tmp = new byte[elements];
        sensor.read(address, tmp.length, tmp);
        return tmp;
    }

}