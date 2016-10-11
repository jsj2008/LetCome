package com.letcome.vo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by rjt on 16/8/22.
 */

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Table(name = "products_v")
public class ProductViewVO implements Serializable {

//    ProductViewID id;
//
//
//    @EmbeddedId
//    @AttributeOverrides({
//            @AttributeOverride(name = "id", column = @Column(name = "id")),
//            @AttributeOverride(name = "category_id", column = @Column(name = "category_id")),
//            @AttributeOverride(name = "city", column = @Column(name = "city")),
//            @AttributeOverride(name = "created_at", column = @Column(name = "created_at")),
//            @AttributeOverride(name = "description", column = @Column(name = "description")),
//            @AttributeOverride(name = "updated_at", column = @Column(name = "updated_at")),
//            @AttributeOverride(name = "image_id", column = @Column(name = "image_id")),
//            @AttributeOverride(name = "imagename", column = @Column(name = "imagename")),
//            @AttributeOverride(name = "imagepath", column = @Column(name = "imagepath")),
//            @AttributeOverride(name = "updated_at", column = @Column(name = "updated_at")),
//            @AttributeOverride(name = "uid", column = @Column(name = "uid")),
//            @AttributeOverride(name = "status", column = @Column(name = "status")),
//            @AttributeOverride(name = "price", column = @Column(name = "price")),
//            @AttributeOverride(name = "longitude", column = @Column(name = "longitude")),
//            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")) })
//
//    public ProductViewID getId() {
//        return id;
//    }
//
//    public void setId(ProductViewID id) {
//        this.id = id;
//    }
    //    public ProductViewVO(){
//        super();
//    }
//    @Id
//    @Column(name="id")
    private Integer id;
//
//    @Column(name="description")
    private String description;
//
//    @Column(name="created_at")
    private Date created_at;

//    @Column(name="updated_at")
    private Date updated_at;

//    @Column(name="longitude")
    private String longitude;//经度

//    @Column(name="latitude")
    private String latitude;//纬度

//    @Column(name="city")
    private String city;

//    @Column(name="status")
    private String status;

    private String contact_info;

//    @Column(name="price")
    private float price;

//    @Column(name="category_id")
    private Integer category_id;

//    @Column(name="uid",nullable = false)
    private Integer uid;

//    @Column(name="imagename",nullable = false)
    private String imagename;

    private String imagepath;

    private String thumbpath;

    private Integer imageheight;

    private Integer imagewidth;

    private Integer thumbheight;

    private Integer thumbwidth;


//    @Column( name = "image_id",nullable = false)
    private Integer image_id;

    private String is_favorite;

    private String fullname;

    private String qq;

    public String getThumbpath() {
        return thumbpath;
    }

    public void setThumbpath(String thumbpath) {
        this.thumbpath = thumbpath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getLongitude() {
        return longitude;
    }

//    public void setLongitude(String longitude) {
//        this.longitude = longitude;
//    }
    public void setLongitude(double longitude) {
        this.longitude = String.valueOf(longitude);
    }

    public String getLatitude() {
        return latitude;
    }

//    public void setLatitude(String latitude) {
//        this.latitude = latitude;
//    }
    public void setLatitude(double latitude) {
        this.latitude = String.valueOf(latitude);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
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

    public Integer getImage_id() {
        return image_id;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

}
