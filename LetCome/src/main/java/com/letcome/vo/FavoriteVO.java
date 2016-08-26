package com.letcome.vo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rjt on 16/8/26.
 */
@Entity
@Table(name="favorites",
        uniqueConstraints = {@UniqueConstraint(columnNames={"uid", "pid"})})
public class FavoriteVO {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="uid")
    private Integer uid;

    @Column(name="pid")
    private Integer pid;

    @Column(name="created_at")
    private Date created_at;

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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
