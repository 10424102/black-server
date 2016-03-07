package org.projw.blackserver.models;

import javax.persistence.*;

//@JsonSerialize(using = ImageSerializer.class)
@Entity
@Table(name = "t_image")
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Column(columnDefinition = "mediumblob")
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @Column(columnDefinition = "char", length = 32)
    private String hash; // hex(md5(data)) 32 个字符

    private String tags;

    //<editor-fold desc="=== Getters & Setters ===">

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    //</editor-fold>
}
