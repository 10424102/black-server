package org.projw.blackserver.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
@Table(name = "t_college")
public class College {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String nameExt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_id")
    private Image logo;

    private String location;

    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY)
    private Set<Academy> academies = new HashSet<>();

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

    public String getNameExt() {
        return nameExt;
    }

    public void setNameExt(String nameExt) {
        this.nameExt = nameExt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public Set<Academy> getAcademies() {
        return academies;
    }

    public void setAcademies(Set<Academy> academies) {
        this.academies = academies;
    }

    //</editor-fold>
}
