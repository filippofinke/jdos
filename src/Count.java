/**
 *
 * @author afink
 */
public class Count extends Thread {
    
    private static int count;
    
    public static synchronized int getCount() {
        return count;
    }
    
    public static synchronized void increment(int value) {
        count += value;
    }
    
    private static synchronized void reset() {
        count = 0;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while(!isInterrupted()) {
            if(System.currentTimeMillis() - start >= 1000) {
                start = System.currentTimeMillis();
                System.out.println(Count.getCount() + " r/s");
                Count.reset();
            }
        }
        System.out.println("Counter killed!");
    }
    
}
