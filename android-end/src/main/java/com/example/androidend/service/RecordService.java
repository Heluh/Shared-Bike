package com.example.androidend.service;

import com.example.androidend.entity.Coordinates;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.androidend.mapper.RecordMapper;
import com.example.androidend.entity.Record;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {
    @Autowired
    private RecordMapper recordMapper;

    public List<Record> findByUserName(String username) {
        try{
            return recordMapper.findByUserName(username);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Record save(Record record) {
        try{
            recordMapper.save(record);
            return record;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void update(Record record) {
        try{
            recordMapper.update(record);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Record findByBikeNumberAndStatus(String bikeNumber, byte status) {
        try{
            return recordMapper.findByBikeNumberAndStatus(bikeNumber, status);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void updateContrail(int recordId, Coordinates coordinates) {
        Record record = recordMapper.findById(recordId);
        if (record != null) {
            try {
                String contrailJson = record.getContrail();
                List<Coordinates> contrail;
                if (contrailJson == null || contrailJson.isEmpty()) {
                    contrail = new ArrayList<>();
                } else {
                    contrail = new ObjectMapper().readValue(contrailJson, new TypeReference<List<Coordinates>>() {
                    });
                }
                contrail.add(coordinates);
                contrailJson = new ObjectMapper().writeValueAsString(contrail);
                record.setContrail(contrailJson);
                recordMapper.update(record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Record findByUsernameAndStatus(String username, byte status) {
        try{
            return recordMapper.findByUsernameAndStatus(username, status);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
