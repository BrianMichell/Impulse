package frc.robot;

import edu.wpi.first.wpilibj.Timer;

public class Subsystem implements Runnable {

    private int state;
    private final int DISABLED = 0;
    private final int ENABLED = 1;

    private final Thread t;

    /**
     * Generic constructor for Subsystem class.
     */
    public Subsystem(Hardware hw) {
        state = DISABLED;
        t = new Thread(this, "Subsystem");
        t.start();
    }

    /**
     * Creates a subsystem with a name in the thread.
     * @param name The name of the subsystem in question.
     */
    public Subsystem(Hardware hw, String name){
        state = DISABLED;
        t = new Thread(this, name);
        t.start();
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
            Timer.delay(0.005);
        }
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
     * THIS METHOD SHOULD BE OVERRIDDEN
     */
    protected void actions() {
        System.out.println("***WARNING***");
        System.out.println("actions() function has not been overridden");
    }

    /**
     * This method should stop ALL motors associated with the subsystem
     * THIS METHOD SHOULD BE OVERRIDDEN
     */
    protected void haltSystem(){
        System.out.println("***WARNING***");
        System.out.println("haltSystem() function has not been overridden");
    }

}