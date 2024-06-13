package com.example.androidend.mapper;

import com.example.androidend.entity.Fence;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FenceMapper {
    List<Fence> getFenceList();

    Fence getFenceById(int id);

}
