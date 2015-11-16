package com.example.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Image {
    public static final int FLAG_PRIVATE = 0x80000000;       // 这一位为 0 表示共有
    public static final int FLAG_USER_GROUP = 0x00000001;    // 表示用户组内可见
    public static final int FLAG_USER = 0x00000002;          // 用户可见

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private byte[] data;

    private Integer flags;

    private Integer uid;

    private Long gid;

    private String hash; // 选择速度最快的信息摘要算法 md5

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

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    //</editor-fold>
}
