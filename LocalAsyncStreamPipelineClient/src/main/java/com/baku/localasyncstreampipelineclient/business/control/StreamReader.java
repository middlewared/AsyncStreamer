package com.baku.localasyncstreampipelineclient.business.control;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamReader implements Runnable {

    private final InetSocketAddress address;
    private SocketChannel clientSocketChannel;

    public StreamReader(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public void run() {
        try {
            openSocketAndAwaitConnection();
            readDataFromChannel();
            closeChannelWhenNoMoreDataIsIncoming();
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(StreamReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void openSocketAndAwaitConnection() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        clientSocketChannel = SocketChannel.open();
        clientSocketChannel.connect(address);
        System.out.println("connected to: " + clientSocketChannel.getRemoteAddress());
    }

    private void readDataFromChannel() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        ByteBuffer destination = ByteBuffer.allocate(1024);
        while (clientSocketChannel.read(destination) != -1) {
            destination.flip();
            for (int i = 0; i < destination.limit(); i++) {
                System.out.print((char) destination.get());
            }
            destination.clear();
        }
    }

    private void closeChannelWhenNoMoreDataIsIncoming() throws IOException {
        clientSocketChannel.close();
    }
}
