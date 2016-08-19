package com.letcome.vo;

import java.sql.Blob;

import javax.persistence.*;

/**
 * Created by rjt on 16/8/12.
 */

@Entity(name="images")
public class ImageVO {
    public ImageVO(){
        super();
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="uid",nullable = false)
    private Integer uid;

    @Column(name="imagename",nullable = false)
    private String imagename;

    @Column( name = "imagepath",nullable = false)
//    @Column(name="image",columnDefinition="LONGBLOB",nullable = false)
    private String imagepath;

    @Column(name="productid",nullable = true)
    private Integer productid;

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
