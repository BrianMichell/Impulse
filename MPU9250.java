package frc.robot;

import java.nio.ByteBuffer;
import java.lang.System;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;

class MPU9250 implements Runnable {

    // Register constants
    private final int MPU9250_ADDRESS = 0x68;
    private final int MAG_ADDRESS = 0x0C;

    // Gyro register constants
    public final int GYRO_250_DPS = 0x00;
    public final int GYRO_500_DPS = 0x08;
    public final int GYRO_1000_DPS = 0x10;
    public final int GYRO_2000_DPS = 0x18;

    /*
     * Numbers given in the datasheet were a third off private final double
     * SCALE_250_DPS = 131; private final double SCALE_500_DPS = 65.5; private final
     * double SCALE_1000_DPS = 32.8; private final double SCALE_2000_DPS = 16.4;
     */
    private final double SCALE_250_DPS = 131 * 1.112 / 2;
    private final double SCALE_500_DPS = 65.5 * 1.112 / 2;
    private final double SCALE_1000_DPS = 32.8 * 1.112 / 2;
    private final double SCALE_2000_DPS = 16.4 * 1.112 / 2;

    // Accelerometer register constants
    public final int ACC_SCALE_2_G = 0x00;
    public final int ACC_SCALE_4_G = 0x08;
    public final int ACC_SCALE_8_G = 0x10;
    public final int ACC_SCALE_16_G = 0x18;

    private final I2C sensor;

    // The change in time and last time gyro x-axis was updated
    // TODO Update structure to do all gyro axes at once (starting with primary axis
    // to ensure best accuracy)
    private double deltaTGX; // This does not need to be an instance variable
    private long lastTGX;
    private double gX;
    private int biasGX;

    private double deltaTGY;
    private long lastTGY;
    private double gY;
    private int biasGY;

    private double deltaTGZ;
    private long lastTGZ;
    private double gZ;
    private int biasGZ;

    private double scale;

    public MPU9250() {
        this(0x10, 0x10);
    }

    public MPU9250(int gyroDPS, int accelScale) {
        sensor = new I2C(I2C.Port.kOnboard, this.MPU9250_ADDRESS);
        sensor.write(29, 0x06); // Set accelerometer low pass filter to 5Hz
        sensor.write(26, 0x06); // Set gyro low pass filter to 5Hz
        sensor.write(27, gyroDPS); // Set gyro to desired rate of change
        sensor.write(28, accelScale); // Set accelerometer to desired scale
        this.deltaTGX = 0;
        this.lastTGX = System.nanoTime();
        this.biasGX = 0;
        gX = 0.0;
        this.scale = getDPS(gyroDPS);
        new Thread(this, "MPU9250").start();
    }

    public void run() {
        calibrateGX();
        while (!Thread.interrupted()) {
            updateGyroX();
            Timer.delay(0.0001);
        }
    }

    private double getDPS(int dps) {
        switch (dps) {
        case (GYRO_250_DPS):
            return SCALE_250_DPS;
        case (GYRO_500_DPS):
            return SCALE_500_DPS;
        case (GYRO_1000_DPS):
            return SCALE_1000_DPS;
        default:
            return SCALE_2000_DPS;
        }
    }

    // GYRO X START ----------------------------------------------
    /**
     * @return Returns true when completed calibrations
     */
    public boolean calibrateGX() { // TODO fix bug where the gyro will begin to eperience runaway drift (unknown
                                   // cause)
        int changeAccumulator = 0;
        double samples = 25;
        for (int i = 0; i < samples; i++) {
            byte[] tmp = read(0x43, 2);
            changeAccumulator += (tmp[0] << 8 | tmp[1]);
        }
        this.biasGX = -(int) (changeAccumulator / samples);
        System.out.println(biasGX);
        return true;
    }

    public void resetGX() {
        this.gX = 0.0;
    }

    public double getGXRate() {
        byte[] dataBuffer = read(0x43, 2); // TODO Change this back to X-axis instead of Z-axis
        int xTmp = dataBuffer[0] << 8 | dataBuffer[1];
        xTmp += biasGX;
        xTmp /= scale;
        return xTmp;
    }

    private void updateGyroX() {
        double xTmp = getGXRate();
        long now = System.nanoTime();
        deltaTGX = (double) (now - lastTGX) / (double) 1000000000; // Explicit double cast required.
        lastTGX = now;
        gX += (double) (xTmp * deltaTGX);
    }

    public double getGyroX() {
        return this.gX;
    }
    // GYRO X END ----------------------------------------------

    // GYRO Y START --------------------------------------------
    public int getGYRate() {
        /*
         * byte[] dataBuffer = read(0x45, 2); double yTmp = dataBuffer[0]<<8 |
         * dataBuffer[1]; yTmp += biasGY; yTmp /= scale; return yTmp;
         */
        return 0;
    }

    private void updateGyroY() {
        /*
         * double yTmp = getGYRate(); long now = System.nanoTime(); deltaTGY = (double)
         * (now - lastTGY) / 1000000000; lastTGY = now; gY += (double)(yTmp * deltaTGY)
         * / scale;
         */
    }

    public int getGyroY() {
        // return gY;
        return 0;
    }
    // GYRO Y END -----------------------------------------------

    // GYRO Z START ---------------------------------------------
    public double getGZRate() {
        /*
         * byte[] dataBuffer = read(0x47, 2); int zTmp = dataBuffer[0]<<8 |
         * dataBuffer[1]; zTmp += biasGZ; zTmp /= scale; return zTmp;
         */
        return 0;
    }

    private void updateGyroZ() {
        /*
         * long now = System.nanoTime(); deltaTGZ = (double) (now - lastTGZ) /
         * 1000000000; lastTGZ = now; gZ += (double)(zTmp * deltaTGZ) / scale;
         */
    }

    public int getGyroZ() {
        // return gZ;
        return 0;
    }
    // GYRO Z END ------------------------------------------------

    public int getAccelX() {
        byte[] buffer = read(0x3B, 2);
        int xTmp = buffer[0] << 8 | buffer[1];
        return xTmp;
    }

    public int getAccelY() {
        byte[] buffer = read(0x3D, 2);
        int yTmp = buffer[0] << 8 | buffer[1];
        return yTmp;
    }

    public int getAccelZ() {
        byte[] buffer = read(0x3F, 2);
        int zTmp = buffer[0] << 8 | buffer[1];
        return zTmp;
    }

    private byte[] read(int address, int elements) {
        byte[] tmp = new byte[elements];
        sensor.read(address, tmp.length, tmp);
        return tmp;
    }

}