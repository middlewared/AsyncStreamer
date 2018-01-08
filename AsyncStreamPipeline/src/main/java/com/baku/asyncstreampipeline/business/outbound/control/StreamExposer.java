package com.baku.asyncstreampipeline.business.outbound.control;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;

@Singleton
public class StreamExposer {

    private final ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();

    private ServerSocketChannel serverSocketChannel;
    private SocketChannel clientSocketChannel;

    private boolean streaming;

    public void putMessageOnOutgoingQueue(List<String> mergedMessagesFromDb) {
        mergedMessagesFromDb.stream().forEach((message) -> {
            concurrentLinkedQueue.offer(message);
        });
    }

    public void startStreamingOut(int port) throws IOException {
        if (streaming) {
            return;
        }

        prepareSocketAndAwaitConnection(port);
    }

    public void acceptConnection() throws IOException {
        clientSocketChannel = serverSocketChannel.accept();
        streamTo(clientSocketChannel);
        closeConnectionAndSocket();
    }

    private void prepareSocketAndAwaitConnection(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException ex) {
            Logger.getLogger(StreamExposer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void streamTo(SocketChannel clientSocketChannel) throws IOException {
        streaming = true;
        while (concurrentLinkedQueue.peek() != null) {
            clientSocketChannel.write(ByteBuffer.wrap(concurrentLinkedQueue.poll().getBytes()));
        }
    }

    private void closeConnectionAndSocket() {
        try {
            clientSocketChannel.socket().close();
            clientSocketChannel.close();
            serverSocketChannel.socket().close();
            serverSocketChannel.close();
            streaming = false;
        } catch (IOException ex) {
            Logger.getLogger(StreamExposer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
