package frc.robot;

import edu.wpi.first.wpilibj.Timer;

abstract class Subsystem implements Runnable {

    private int state;
    private final int DISABLED = 0;
    private final int ENABLED = 1;

    /**
     * Generic constructor for Subsystem class.
     */
    public Subsystem() {
    }

    /**
     * Overridden run() method from Runnable.
     * Runs until JVM stops.
     * There are two states that are checked
     * ENABLED runs actions()
     * DISABLED runs haltSystem()
     */
    public void run() {
        while (!Thread.interrupted()) {
            switch(state){
                case ENABLED:
                    actions();
                    break;
                case DISABLED:
                    haltSystem();
                    break;
            }
            Timer.delay(0.001);
        }
    }

    protected void start(){
        start("Subsystem");
    }

    protected void start(String name){
        state = DISABLED;
        new Thread(this, name).start();
    }

    /**
     * Enables the thread (calls actions() every loop)
     */
    public void enable() {
        state = ENABLED;
    }

    /**
     * Disables the thread (calls haltSystem() every loop)
     */
    public void disable() {
        state = DISABLED;
    }

    /**
     * The actions that should be taken every iteration
     * THIS METHOD MUST BE OVERRIDDEN
     */
    abstract void actions();

    /**
     * This method should stop ALL motors associated with the subsystem
     * THIS METHOD MUST BE OVERRIDDEN
     */
    abstract void haltSystem();
}