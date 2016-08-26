package com.letcome.entity;

import com.letcome.vo.ProductViewVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjt on 16/8/22.
 */
public class ProductViewEntity extends  ReturnEntity {
    public List<ProductViewVO> getRecords() {
        return records;
    }

    public void setRecords(List<ProductViewVO> records) {
        this.records = records;
    }

    List<ProductViewVO> records = new ArrayList<ProductViewVO>();
}
