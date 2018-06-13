package com.example.Uploader.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Image {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String url;
    private String md5Hash;

    public Image() {
    }

    public Image(String name, String url, String md5Hash) {
        this.name = name;
        this.url = url;
        this.md5Hash = md5Hash;
    }


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setmd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    @Override
    public String toString() {
        return "Image{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", md5Checksum='" + md5Hash + '\'' +
                '}';
    }
}
