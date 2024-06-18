package com.example.androidend.mapper;

import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import com.example.androidend.entity.Record;

import java.util.List;

@Mapper
public interface RecordMapper {
    List<Record> findByUserName(String username);

    void save(Record record);

    void update(Record record);

    Record findByBikeNumberAndStatus(String bikeNumber, byte status);

    Record findById(int id);

    Record findByUsernameAndStatus(String username, byte status);
}
