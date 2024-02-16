package org.poc.api;


import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.poc.panache.dto.CuentaDto;

@Path("/api")
@RegisterRestClient(configKey="dataService-api")
public interface DataService {
    @POST
    @Path("/data/0")
    Response execute(CuentaDto request);
}
