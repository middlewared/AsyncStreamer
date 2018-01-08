package com.baku.asyncstreampipeline.business.inbound.control;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedExecutorService;

@Singleton
public class StreamReaderController {

    @Resource
    ManagedExecutorService managedExecutorService;

    private final Map<InetSocketAddress, Future> streamReadersList = new ConcurrentHashMap<>();

    public void createStreamReaderFor(InetSocketAddress url) {
        readAsynchronouslyFrom(url);
    }

    public void removeStringReaderFor(InetSocketAddress ulr) {
        streamReadersList.get(ulr).cancel(true);
        streamReadersList.remove(ulr);
    }
    
    public Map<InetSocketAddress, Future> getStreamReaders() {
        return streamReadersList;
    }

    private void readAsynchronouslyFrom(InetSocketAddress url) {
        StreamReader streamReader = new StreamReader(url);
        streamReadersList.put(url, managedExecutorService.submit(streamReader));
    }
}
