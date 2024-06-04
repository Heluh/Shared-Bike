package com.train.campussharedbikeserverfence.mapper;

import com.train.campussharedbikeserverfence.entity.Fence;
import com.train.campussharedbikeserverfence.entity.FenceType2MaxCount;
import com.train.campussharedbikeserverfence.entity.FenceTypeCount;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FenceMapper {
    // 查询所有围栏
    void insertFence(Fence fence);

    Fence getFence(int id);

    int getFenceCount();

    List<Fence> getFenceList(int page, int size);

    void updateFence(Fence fence);

    void deleteFence(int id);

    @Select("SELECT type, COUNT(*) AS count FROM fence GROUP BY type")
    @Results({
            @Result(property = "type", column = "type"),
            @Result(property = "count", column = "count")
    })
    List<FenceTypeCount> getFenceTypeCount();

    @Select("SELECT fence_name, capacity FROM fence WHERE type = 2")
    @Results({
            @Result(property = "fenceName", column = "fence_name"),
            @Result(property = "capacity", column = "capacity")
    })
    List<FenceType2MaxCount> getFenceType2MaxCount();
}
