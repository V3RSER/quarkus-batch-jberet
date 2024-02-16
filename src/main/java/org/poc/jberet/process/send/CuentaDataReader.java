package org.poc.jberet.process.send;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.poc.panache.entity.Cuenta;

import java.io.Serializable;
import java.util.Iterator;

@Dependent
@Named
@Transactional
public class CuentaDataReader implements ItemReader {

    @Inject
    @BatchProperty(name = "page")
    private int page;

    @Inject
    @BatchProperty(name = "page-size")
    private int pageSize;

    private Iterator<PanacheEntityBase> entityIterator;


    @Override
    public void open(Serializable checkpoint) throws Exception {
        entityIterator = Cuenta.findAll(Sort.by("idCuenta"))
                .page(page, pageSize)
                .list()
                .iterator();
    }

    @Override
    public void close() {
        entityIterator.remove();
    }

    @Override
    public Object readItem() {
        return entityIterator.hasNext() ? entityIterator.next() : null;
    }

    @Override
    public Serializable checkpointInfo() {
        return null;
    }

}
