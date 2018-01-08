package com.baku.asyncstreampipeline.business.boundary;

import com.baku.asyncstreampipeline.business.control.DbClientProvider;
import com.baku.asyncstreampipeline.business.inbound.control.StreamReaderController;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("streamReaders")
public class ControlResource {

    @Inject
    StreamReaderController streamReaderController;
    
    @Inject
    DbClientProvider dbClientProvider;

    @OPTIONS
    public String getDbConnectionPoint() {
        return dbClientProvider.getMongoClient().getConnectPoint();
    }
    
    @GET
    @Produces(APPLICATION_JSON)
    public List<InetSocketAddress> getRunningReaders() {
        return new ArrayList<>(streamReaderController.getStreamReaders().keySet());
    }

    @POST
    public void addNewReader(@QueryParam("url") String url) throws MalformedURLException {
        streamReaderController.createStreamReaderFor(new InetSocketAddress(url.split(":")[0], Integer.valueOf(url.split(":")[1])));
    }
    
    @DELETE
    public void stopStreamingOut(@QueryParam("url") String url) throws MalformedURLException {
        streamReaderController.removeStringReaderFor(new InetSocketAddress(url.split(":")[0], Integer.valueOf(url.split(":")[1])));
    }
}
