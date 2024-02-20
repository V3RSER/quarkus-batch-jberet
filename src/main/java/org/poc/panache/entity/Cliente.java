package org.poc.panache.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
//@Cacheable
@Table(name = "clientes")
public class Cliente extends PanacheEntityBase {

    @Id
    private Long id;

    private String documento;


    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

}