package com.letcome.vo;

import org.hibernate.annotations.Formula;
import org.springframework.web.bind.annotation.Mapping;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rjt on 16/8/18.
 */
@Entity(name="products")
public class ProductVO {
    public ProductVO(){
        super();
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="description",length = 100)
    private String description;

    @Column(name="created_at")
    private Date created_at;

    @Column(name="updated_at")
    private Date updated_at;

    @Column(name="longitude")
    private double longitude;//经度

    @Column(name="latitude")
    private double latitude;//纬度

    @Column(name="city")
    private String city;

    @Column(name="status")
    private String status;

    @Column(name="price")
    private float price;

    @Column(name="category_id")
    private Integer category_id;

    @Column(name="contact_info")
    private String contact_info;

    @Column(name="phone_num")
    private String phone_num;

    @Column(name="uid",nullable = false)
    private Integer uid;

    @OneToMany(cascade= {CascadeType.ALL},mappedBy="pid")
    private Set<FavoriteVO> favorites ;

    @OneToMany(cascade= {CascadeType.ALL},mappedBy="productid")
    private Set<ImageVO> images = new HashSet<ImageVO>();

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

//    public Set<FavoriteVO> getFavorites() {
//        return favorites;
//    }
//
//    public void setFavorites(Set<FavoriteVO> favorites) {
//        this.favorites = favorites;
//    }

    public Set<ImageVO> getImages() {
        return images;
    }

    public void setImages(Set<ImageVO> images) {
        this.images = images;
    }


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    final public static String STATUS_PUBLISH = "0";
    final public static String STATUS_SELLING = "1";
    final public static String STATUS_SOLD = "2";
}
