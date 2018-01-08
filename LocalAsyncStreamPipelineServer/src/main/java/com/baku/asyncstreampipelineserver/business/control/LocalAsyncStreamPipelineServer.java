package com.baku.asyncstreampipelineserver.business.control;

import java.net.InetSocketAddress;

public class LocalAsyncStreamPipelineServer {

    public static void main(String[] args) {
        new StreamProducer(new InetSocketAddress(Integer.valueOf(args[0]))).run();
    }
}
