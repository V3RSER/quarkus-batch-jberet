package org.poc.panache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.mutiny.CompositeException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.poc.panache.entity.Transaccion;

import java.util.List;

@Path("transacciones")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class TransaccionResource {

    private static final Logger LOGGER = Logger.getLogger(TransaccionResource.class.getName());

    @GET
    public List<Transaccion> get() {
        return Transaccion.find("#Transaccion.findByCuentaMarcada")
                .page(1, 10)
                .list();
    }

    @GET
    @Path("{id}")
    public Transaccion getSingle(Long id) {
        return Transaccion.findById(id);
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            Throwable throwable = exception;

            int code = 500;
            if (throwable instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            // This is a Mutiny exception and it happens, for example, when we try to insert a new
            // Transaccion but the name is already in the database
            if (throwable instanceof CompositeException) {
                throwable = throwable.getCause();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", throwable.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", throwable.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
