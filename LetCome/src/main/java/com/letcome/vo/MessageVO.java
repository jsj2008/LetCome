package com.letcome.vo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rjt on 16/8/30.
 */

@Entity(name="messages")
public class MessageVO {
    public MessageVO(){
        super();
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="fromid")
    private Integer fromid;

    @Transient
    private String fromname;

    @Column(name="toid")
    private Integer toid;

    @Transient
    private String toname;

    @Column(name="content")
    private String content;

    @Column(name="created_at")
    private Date created_at;

    public Integer getId() {
        return id;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
