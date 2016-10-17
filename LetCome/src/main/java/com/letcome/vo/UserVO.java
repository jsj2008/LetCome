package com.letcome.vo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rjt on 16/8/10.
 */

@Entity(name="users")
public class UserVO {

    public UserVO(){
        super();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="email",unique = true,nullable = false)
    private String email;

    @Column(name="pwd",nullable = false)
    private String pwd;

    @Column(name="fullname",length=16,nullable = false)
    private String fullname;

    @Column(name="qq",length=16)
    private String qq;

    @Column(name="openid")
    private String openid;

    @Transient
    private Integer access_token;

    @Transient
    private Integer mid;

    @Transient
    private Integer fromid;

    @Transient
    private Integer toid;

    @Transient
    private String content;

    @Transient
    private Date created_at;

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getFromid() {
        return fromid;
    }

    public void setFromid(Integer fromid) {
        this.fromid = fromid;
    }

    public Integer getToid() {
        return toid;
    }

    public void setToid(Integer toid) {
        this.toid = toid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getAccess_token() {
        return access_token;
    }

    public void setAccess_token(Integer access_token) {
        this.access_token = access_token;
    }
}