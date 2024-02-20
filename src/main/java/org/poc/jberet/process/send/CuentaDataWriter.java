package org.poc.jberet.process.send;

import io.quarkus.logging.Log;
import io.quarkus.panache.common.Parameters;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.poc.api.DataService;
import org.poc.panache.dto.CuentaDto;
import org.poc.panache.entity.Cuenta;

import java.util.List;
import java.util.Objects;

@Dependent
@Named
//@Transactional
public class CuentaDataWriter extends AbstractItemWriter {

    @Inject
    @RestClient
    private DataService dataService;

    @Inject
    @BatchProperty(name = "page")
    private int page;

    @Override
    public void writeItems(List<Object> items) {
//        Log.info(" \tPage-" + page + " :\tobtenidos "+ items.size() + " items.");

        List<Integer> successfullySentIds = sendAccounts(items);
        int updatedAccounts = updateAccountsStatus(successfullySentIds);

        Log.info(updatedAccounts + " cuentas actualizadas.");
//        successfullySentIds.forEach(id -> {
//            Log.info("cuenta id: " + id + " actualizada a procesada");
//        });
        Log.info(" \tPage-" + page + ": \tprocesados " + items.size() + " items.");
    }

    private List<Integer> sendAccounts(List<Object> items) {
        return items.stream()
                .map(item -> (CuentaDto) item)
                .map(accountDto -> {
                    try (Response response = dataService.execute(accountDto)) {
                        response.close();
                        if (response.getStatus() == 200) {
//                            Log.info(" \t --> Page-" + page + ":\t Item id: " + accountDto.getNumCuenta() + " \tstatus: " + response.getStatus());
                            return Integer.parseInt(accountDto.getNumCuenta());
                        }
                    } catch (Exception e) {
                        Log.error(" \t --> Page-" + page + ":\t Item error: " + e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private int updateAccountsStatus(List<Integer> accountIds) {
        if (accountIds.isEmpty()) {
            return 0;
        }

        String updateQuery = "UPDATE Cuenta SET batchEjecutado = true WHERE idCuenta IN :ids";
        return Cuenta.updateStatus(updateQuery, Parameters.with("ids", accountIds));

    }
}
