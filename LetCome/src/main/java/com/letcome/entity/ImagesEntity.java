package com.letcome.entity;

import com.letcome.vo.MessageVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjt on 16/8/30.
 */
public class ImagesEntity extends ReturnEntity{
    List<ImageEntity> records = new ArrayList<ImageEntity>();

    public List<ImageEntity> getRecords() {
        return records;
    }

    public void setRecords(List<ImageEntity> records) {
        this.records = records;
    }
}