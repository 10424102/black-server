package org.projw.blackserver.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_page")
public class Page {
    @Id
    @GeneratedValue
    private Long id;

    //<editor-fold desc="=== Getters & Setters ===">

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    //</editor-fold>
}
