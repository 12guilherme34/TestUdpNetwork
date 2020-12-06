package Async;

import java.io.IOException;
import java.net.*;

public class ClientAsync {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 8888;

        InetSocketAddress address = new InetSocketAddress(host, port);
        InetSocketAddress destinationAddress = new InetSocketAddress(host, 9898);


        try {
            DatagramSocket socket = new DatagramSocket(address);

            byte[] msg = "Hello Async.ServerAsync".getBytes();

            DatagramPacket pk = new DatagramPacket(msg, msg.length, InetAddress.getByName("127.0.0.1"), 9898);

            socket.send(pk);

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
