package com.letcome.entity;

import com.letcome.vo.MessageVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjt on 16/8/30.
 */
public class MessageEntity  extends ReturnEntity{
    List<MessageVO> records = new ArrayList<MessageVO>();

    public List<MessageVO> getRecords() {
        return records;
    }

    public void setRecords(List<MessageVO> records) {
        this.records = records;
    }
}