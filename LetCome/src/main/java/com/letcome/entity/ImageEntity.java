package com.letcome.entity;
import java.sql.Blob;

/**
 * Created by rjt on 16/8/12.
 */
public class ImageEntity {
    private Integer id;

    private Integer uid;

    private String imagename;

    private String imagepath;

    private String thumbpath;

    private Integer imageheight;

    private Integer imagewidth;

    private Integer thumbheight;

    private Integer thumbwidth;

    private byte[] image;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getThumbpath() {
        return thumbpath;
    }

    public void setThumbpath(String thumbpath) {
        this.thumbpath = thumbpath;
    }

    public Integer getImageheight() {
        return imageheight;
    }

    public void setImageheight(Integer imageheight) {
        this.imageheight = imageheight;
    }

    public Integer getImagewidth() {
        return imagewidth;
    }

    public void setImagewidth(Integer imagewidth) {
        this.imagewidth = imagewidth;
    }

    public Integer getThumbheight() {
        return thumbheight;
    }

    public void setThumbheight(Integer thumbheight) {
        this.thumbheight = thumbheight;
    }

    public Integer getThumbwidth() {
        return thumbwidth;
    }

    public void setThumbwidth(Integer thumbwidth) {
        this.thumbwidth = thumbwidth;
    }
}
