package com.baku.asyncstreampipelineserver.business.control;

import java.io.IOException;
import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_UP;
import java.net.InetSocketAddress;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static java.util.concurrent.TimeUnit.MINUTES;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamProducer implements Runnable {

    private final InetSocketAddress address;
    private AsynchronousServerSocketChannel serverSocketChannel;
    private AsynchronousSocketChannel clientSocketChannel;

    public StreamProducer(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public void run() {
        try {
            clientSocketChannel = openSocketAndAwaitConnection().get(30, MINUTES);
            System.out.println("Client connected from " + clientSocketChannel.getRemoteAddress());
            produceDataToChannel();
            closeChannels();
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(StreamProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Future<AsynchronousSocketChannel> openSocketAndAwaitConnection() throws IOException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(address);
        System.out.println("Listening on: " + address.toString());
        return serverSocketChannel.accept();
    }

    private void produceDataToChannel() throws IOException, InterruptedException {
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        for (int i = 0; i < 100; i++) {
            Thread.sleep(1000);
            clientSocketChannel.write(encoder.encode(CharBuffer.wrap(randomXMLData())));
        }
    }

    private void closeChannels() throws IOException {
        clientSocketChannel.close();
        serverSocketChannel.close();
    }

    private String randomXMLData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return "<data>"
                + "<source>" + this.address + "</source>"
                + "<timestamp>" + sdf.format(new Date()) + "</timestamp>"
                + "<amount>" + BigDecimal.valueOf((Math.random() * 500) - 250).setScale(3, HALF_UP).doubleValue() + "</amount>"
                + "</data>\n";
    }
}
