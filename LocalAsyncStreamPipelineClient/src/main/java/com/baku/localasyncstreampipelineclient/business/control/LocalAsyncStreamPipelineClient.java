package com.baku.localasyncstreampipelineclient.business.control;

import java.net.InetSocketAddress;

public class LocalAsyncStreamPipelineClient {

    public static void main(String[] args) {
        new StreamReader(new InetSocketAddress(Integer.valueOf(args[0]))).run();
    }
}
