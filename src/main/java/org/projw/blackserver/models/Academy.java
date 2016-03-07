package org.projw.blackserver.models;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "t_academy")
public class Academy {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_id")
    private Image logo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;


    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                        GET & SET                            //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

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

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    //</editor-fold>
}
