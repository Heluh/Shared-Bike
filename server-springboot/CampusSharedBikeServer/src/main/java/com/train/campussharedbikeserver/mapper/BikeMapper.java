package com.train.campussharedbikeserver.mapper;

import com.train.campussharedbikeserver.bean.Bike;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeMapper {
    // 根据fenceId获取属于该围栏的单车列表
    List<Bike> selectBikeByFenceId(int fenceId);

}
