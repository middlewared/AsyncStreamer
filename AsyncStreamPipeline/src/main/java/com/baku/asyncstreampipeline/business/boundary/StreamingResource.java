package com.baku.asyncstreampipeline.business.boundary;

import com.baku.asyncstreampipeline.business.outbound.control.StreamExposer;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Stateless
@Path("streamOutput")
public class StreamingResource {

    @Inject
    StreamExposer streamExposer;

    @POST
    public void startStreamingOut(@QueryParam("port") int port) throws IOException {
        streamExposer.startStreamingOut(port);
    }

    @PUT
    public void acceptConnection() throws IOException {
        streamExposer.acceptConnection();
    }
}
