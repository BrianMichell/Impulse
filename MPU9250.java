package frc.robot;

import java.nio.ByteBuffer;
import java.lang.System;

import edu.wpi.first.wpilibj.I2C;

//TODO Implement runnable for constant update of sensor information
class MPU9250 {

    //Register constants
    private final int MPU9250_ADDRESS = 0x68;
    private final int MAG_ADDRESS = 0x0C;

    //Gyro register constants
    public final int GYRO_250_DPS = 0x00;
    public final int GYRO_500_DPS = 0x08;
    public final int GYRO_1000_DPS = 0x10;
    public final int GYRO_2000_DPS = 0x18;

    //Accelerometer register constants
    public final int ACC_SCALE_2_G = 0x00;
    public final int ACC_SCALE_4_G = 0x08;
    public final int ACC_SCALE_8_G = 0x10;
    public final int ACC_SCALE_16_G = 0x18;
    
    private final I2C sensor;
    
    //The change in time and last time gyro x-axis was updated
    //TODO Update structure to do all gyro axes at once (starting with primary axis to ensure best accuracy)
    private long deltaTGX; //This does not need to be an instance variable 
    private long lastTGX;
    private double gX;

    public MPU9250(){
        this(0x10, 0x10);
    }
    
    //TODO change gyroDPS to instance variable to allow for headding calculations
    public MPU9250(int gyroDPS, int accelScale){
        sensor = new I2C(I2C.Port.kOnboard, this.MPU9250_ADDRESS);
        sensor.write(29, 0x06); //Set accelerometer low pass filter to 5Hz
        sensor.write(26, 0x06); //Set gyro low pass filter to 5Hz
        sensor.write(27, gyroDPS); //Set gyro to desired rate of change
        sensor.write(28, accelScale); //Set accelerometer to desired scale
        this.deltaTGX = 0;
        this.lastTGX = System.currentTimeMillis();
        gX = 0.0;
    }

    public double getGyroX(){
        byte[] dataBuffer = read(0x43,2);
        int xTmp = dataBuffer[0]<<8 | dataBuffer[1];
        long now = System.currentTimeMillis();
        deltaTGX = (now - lastTGX) / 1000; 
        gX += (xTmp * deltaTGX) * (1000/32768); //Omega * dt * resolution
        return gX;
    }
    
    public int getGyroY(){
        return 0;
    }
    
    public int getGyroZ(){
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