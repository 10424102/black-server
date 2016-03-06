package org.projw.blackserver.models;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "t_netbar")
public class Netbar {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "logo_id")
    private Image logo;
    private String location;

    //<editor-fold desc="=== Getters & Setters ===">


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //</editor-fold>
}
