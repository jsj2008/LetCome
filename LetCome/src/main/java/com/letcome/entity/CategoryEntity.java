package com.letcome.entity;

import com.letcome.vo.CategoryVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjt on 16/8/19.
 */
public class CategoryEntity extends ReturnEntity{
    List<CategoryVO> records = new ArrayList<CategoryVO>();

    public List<CategoryVO> getRecords() {
        return records;
    }

    public void setRecords(List<CategoryVO> records) {
        this.records = records;
    }
}
