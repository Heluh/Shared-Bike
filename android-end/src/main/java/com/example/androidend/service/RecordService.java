package com.example.androidend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.androidend.mapper.RecordMapper;

import java.util.List;

@Service
public class RecordService {
    @Autowired
    private RecordMapper recordMapper;

    public List<Record> findByUserName(String username) {
        return recordMapper.findByUserName(username);
    }

}
