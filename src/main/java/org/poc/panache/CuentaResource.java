package org.poc.panache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.CompositeException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.poc.panache.entity.Cuenta;

import java.io.Serializable;
import java.util.List;

@Path("cuentas")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class CuentaResource {

    private static final Logger LOGGER = Logger.getLogger(CuentaResource.class.getName());

    @GET
    public List<Cuenta> get() {
        return Cuenta.findAll()
                .page(1, 10)
                .list();
    }

    @POST
    @Path("/page")
    public List<Integer> page(RequestPage requestPage) {
        List<Cuenta> cuentas = Cuenta.findAll(Sort.by("idCuenta"))
                .page(requestPage.page, requestPage.pageSize)
                .list();

        return cuentas.stream().map(Cuenta::getIdCuenta).toList();
    }

    public static class RequestPage implements Serializable {

        public Integer page;
        public Integer pageSize;
    }

    @GET
    @Path("{id}")
    public Cuenta getSingle(Long id) {
        return Cuenta.findById(id);
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
            // Cuenta but the name is already in the database
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
