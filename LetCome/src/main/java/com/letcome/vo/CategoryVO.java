package com.letcome.vo;

import javax.persistence.*;

/**
 * Created by rjt on 16/8/12.
 */

@Entity(name="categories")
public class CategoryVO {
    public CategoryVO(){
        super();
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="category_name",nullable = false)
    private String category_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}


//        insert into categories (categories.category_name) values ("电子设备");
//        insert into categories (categories.category_name) values ("机动车");
//        insert into categories (categories.category_name) values ("休闲娱乐");
//        insert into categories (categories.category_name) values ("家居");
//        insert into categories (categories.category_name) values ("书籍、影音");
//        insert into categories (categories.category_name) values ("时尚");
//        insert into categories (categories.category_name) values ("婴幼儿");
//        insert into categories (categories.category_name) values ("其他");
//        ALTER TABLE categories CONVERT TO CHARACTER SET utf8 COLLATE utf8_bin;