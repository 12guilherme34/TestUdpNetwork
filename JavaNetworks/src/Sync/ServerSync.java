package Sync;


import java.io.IOException;
import java.net.*;

interface ILog{

}

class ConsoleLog implements ILog{}

public class ServerSync {
    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 8855;

        try {

            ServerSync serverSync = new ServerSync(host, port, new ConsoleLog());

            System.out.println("---- Server Connection ----");
            System.out.println("Host: " + host);
            System.out.println("Port: " + port);

            serverSync.run();

        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String byteArrayToHexString(byte[] bytes, int size){
        String ret = "";

        for(int i = 0; i < size; i++){
            ret += "0x" + Integer.toHexString(0xFF & bytes[i]) + " ";
        }

        return ret;
    }


    private final ILog logger;
    private final int BUFFER_SIZE = 1500;

    private final String host;
    private final int port;

    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private final byte[] buffer;

    private boolean running = false;

    ServerSync(String host, int port, ILog logger) throws UnknownHostException, SocketException {
        this.host = host;
        this.port = port;
        this.logger = logger;

        buffer = new byte[BUFFER_SIZE];

        InetAddress ip = InetAddress.getByName(host);
        this.socket = new DatagramSocket(this.port, ip);
        this.packet = new DatagramPacket(this.buffer, this.buffer.length);
    }

    public void close(){
        this.running = false;
    }

    public void run() throws IOException {
        this.running = true;

        while(running){

            socket.receive(this.packet);
            System.out.println("----- Message Received -----" );
            System.out.println("Message Size: " + this.packet.getLength());
            System.out.println("Message Content: " +  byteArrayToHexString(this.packet.getData(), this.packet.getLength()));
        }

        socket.close();
    }
}
