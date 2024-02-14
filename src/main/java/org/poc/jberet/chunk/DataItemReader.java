package org.poc.jberet.chunk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.logging.Log;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.poc.panache.entity.Transaccion;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@Dependent
@Named
@Transactional
public class DataItemReader implements ItemReader {

    @Inject
    @BatchProperty(name = "page")
    private int page;

    @Inject
    @BatchProperty(name = "page-size")
    private int pageSize;

    private Iterator<PanacheEntityBase> entityIterator;


    @Override
    public void open(Serializable checkpoint) throws Exception {

        List<PanacheEntityBase> pageEntities = Transaccion.find("#Transaccion.findByCuentaMarcada")
                .page(page, pageSize)
                .list();

        entityIterator = pageEntities.iterator();

        Log.info("Page-" + page + ": " + pageEntities.size() + " obtenidos.");
    }

    @Override
    public void close() {
        entityIterator.remove();
        Log.info("Page-" + page + ": terminada.");

    }

    @Override
    public Object readItem() throws Exception {
        return entityIterator.hasNext() ? entityIterator.next() : null;
    }

    @Override
    public Serializable checkpointInfo() {
        return null;
    }

}
