package org.poc.jberet.process.send;

import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.poc.panache.entity.Cuenta;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@Dependent
@Named
//@Transactional
public class CuentaDataReader implements ItemReader {

    @Inject
    @BatchProperty(name = "page")
    private int page;

    @Inject
    @BatchProperty(name = "page-size")
    private int pageSize;

    private List<Cuenta> sortedAccounts;

    private Iterator<Cuenta> accountsIterator;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        sortedAccounts = Cuenta.findPage(page, pageSize);
        accountsIterator = sortedAccounts.iterator();
    }

    @Override
    public void close() {
        sortedAccounts.clear();
        accountsIterator = null;
    }

    @Override
    public Object readItem() {
        return accountsIterator.hasNext() ? accountsIterator.next() : null;
    }

    @Override
    public Serializable checkpointInfo() {
        return null;
    }

}
