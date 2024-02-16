package org.poc.jberet;


import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.poc.jberet.dto.TransaccionDto;

@Path("/api")
@RegisterRestClient(configKey="dataService-api")
public interface DataService {
    @POST
    @Path("/data")
    Response execute(TransaccionDto request);
}
