package Async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import static java.lang.System.out;

public class ServerAsync {
    public static void main(String[] args) {
        try {

            ServerAsync serverAsync = new ServerAsync("127.0.0.1", 9898);

            serverAsync.run();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final Selector selector;
    private final DatagramChannel channel;
    private final InetSocketAddress address;

    private ByteBuffer buffer;
    private final int BUFFER_SIZE = 128;

    private boolean running = false;

    ServerAsync(String host, int port) throws IOException {
        this.channel = DatagramChannel.open();
        this.address = new InetSocketAddress(host, port);
        this.selector = Selector.open();

        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);

        initChannel();
    }

    private void initChannel() throws IOException {
        this.channel.configureBlocking(false);
        this.channel.socket().connect(this.address);
        this.channel.register(this.selector, SelectionKey.OP_READ);
    }

    public void stop(){
        this.running = false;
    }

    public void run() throws IOException {
        this.running = true;

        while(this.running){

            selector.select();

            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
            while (keyIter.hasNext()) {
                SelectionKey key = keyIter.next(); // Key is bit mask

                if(!key.isValid())
                    continue;

                if (key.isReadable())
                    handleRead(key);

                keyIter.remove();
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();

        this.buffer.clear();    // Prepare buffer for receiving

        SocketAddress clientAddress = channel.receive(this.buffer);

        if (clientAddress != null) {  // Did we receive something?
            out.println(this.buffer);
            // Register write with the selector
            //key.interestOps(SelectionKey.OP_WRITE);
        }
    }


}
