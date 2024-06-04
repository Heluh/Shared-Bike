package com.train.campussharedbikeserver.mapper;

import com.train.campussharedbikeserver.bean.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordMapper {
    /*
     * 获取用户的骑行记录
     */
    List<Record> getAllRecord();
}
