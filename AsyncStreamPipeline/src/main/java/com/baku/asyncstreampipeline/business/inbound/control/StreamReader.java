package com.baku.asyncstreampipeline.business.inbound.control;

import com.baku.asyncstreampipeline.business.control.MessagesRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class StreamReader implements Runnable {

    private final InetSocketAddress url;

    public StreamReader(InetSocketAddress url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = openSocket();
            readStreamWhileSocketIsProvidingData(socketChannel);
            closeAfterStreamingEnds();
        } catch (IOException ex) {
            handleException(ex);
        }
    }

    private SocketChannel openSocket() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(url);
        return socketChannel;
    }

    private void readStreamWhileSocketIsProvidingData(SocketChannel socketChannel) throws IOException {
        BufferedReader clientSocketInputStream = new BufferedReader(new InputStreamReader(socketChannel.socket().getInputStream()));
        String incomingMessage;
        while ((incomingMessage = clientSocketInputStream.readLine()) != null) {
            lookupMessageRepository().transformAndWrite(incomingMessage);
        }
    }

    private void closeAfterStreamingEnds() {
        lookupStreamReaderController().removeStringReaderFor(url);
    }

    private void handleException(IOException ex) {
        Logger.getLogger(StreamReader.class.getName()).log(Level.SEVERE, null, ex);
        closeAfterStreamingEnds();
    }

    private StreamReaderController lookupStreamReaderController() {
        try {
            return (StreamReaderController) InitialContext.doLookup("java:app/AsyncStreamPipeline/StreamReaderController!com.baku.asyncstreampipeline.business.inbound.control.StreamReaderController");
        } catch (NamingException ex) {
            Logger.getLogger(StreamReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private MessagesRepository lookupMessageRepository() {
        try {
            return (MessagesRepository) InitialContext.doLookup("java:app/AsyncStreamPipeline/MessagesRepository!com.baku.asyncstreampipeline.business.control.MessagesRepository");
        } catch (NamingException ex) {
            Logger.getLogger(StreamReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
