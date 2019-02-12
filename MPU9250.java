package frc.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.wpi.first.wpilibj.I2C;

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

    private ByteBuffer compBuffer;
    private byte[] dataBuffer = new byte[6];

    public MPU9250(){
        this(0x10, 0x10);
    }

    public MPU9250(int gyroDPS, int accelScale){
        this.compBuffer = ByteBuffer.wrap(this.dataBuffer);
        sensor = new I2C(I2C.Port.kOnboard, this.MPU9250_ADDRESS);
        sensor.write(29, 0x06); //Set accelerometer low pass filter to 5Hz
        sensor.write(26, 0x06); //Set gyro low pass filter to 5Hz
        sensor.write(27, gyroDPS); //Set gyro to desired rate of change
        sensor.write(28, accelScale); //Set accelerometer to desired scale
    }

    public int getGyroX(){
        return 0;
    }

    public int getGyroY(){
        return 0;
    }

    public int getGyroZ(){
        return 0;
    }

}