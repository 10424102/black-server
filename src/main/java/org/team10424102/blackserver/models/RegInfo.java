package org.team10424102.blackserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Embeddable;
import java.util.Date;

@SuppressWarnings("unused")
@Embeddable
public class RegInfo {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regTime = new Date();

    private String regIp;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "longitude", column = @Column(name = "reg_longitude")),
//            @AttributeOverride(name = "latitude", column = @Column(name = "reg_latitude"))
//    })
//    private Location regLocation;

    //<editor-fold desc="=== Getters & Setters ===">

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getRegIp() {
        return regIp;
    }

    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

//    public Location getRegLocation() {
//        return regLocation;
//    }
//
//    public void setRegLocation(Location regLocation) {
//        this.regLocation = regLocation;
//    }

    //</editor-fold>
}
