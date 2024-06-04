package com.train.campussharedbikeserver.server;

import com.train.campussharedbikeserver.bean.Record;
import com.train.campussharedbikeserver.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    @Autowired
    private RecordMapper recordMapper;

    public List<Record> getAllRecord() {
        return recordMapper.getAllRecord();
    }

}
