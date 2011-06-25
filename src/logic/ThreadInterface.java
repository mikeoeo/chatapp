package logic;

/**
 *
 * @author Periklis Ntanasis
 */
public class ThreadInterface implements Runnable {

    private Thread runner;

    @Override
    synchronized public void run() {
    }

    /**
     * Starts the execution of the thread.
     */
    synchronized public void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.setDaemon(true);
            runner.start();
        }
    }
    
    /**
     * Stops the execution of the thread.
     */
    public void stop()
    {
        runner.interrupt();
        runner = null;
    }
}
