package Sync;

import java.io.IOException;
import java.net.*;

public class ClientSync {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9989;
        int portServer = 8855;

        try {

            InetAddress ip = InetAddress.getByName(host);
            InetAddress ipServer = InetAddress.getByName(host);

            DatagramSocket socket = new DatagramSocket(port, ip);

            byte[] msg = new byte[17700];
            msg[17699] = (byte) 0xFF;
            DatagramPacket pk = new DatagramPacket(msg, msg.length, ipServer, portServer);

            socket.send(pk);
            System.out.println("Message Sent!");

        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
