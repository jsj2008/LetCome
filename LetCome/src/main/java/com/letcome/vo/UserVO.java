package com.letcome.vo;

import javax.persistence.*;
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


}