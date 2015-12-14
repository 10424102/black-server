package org.team10424102.blackserver.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@SuppressWarnings("unused")
@Entity
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
