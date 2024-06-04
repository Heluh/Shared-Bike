package com.train.campussharedbikeserver.mapper;

import com.train.campussharedbikeserver.bean.Fence;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FenceMapper {
    // 查询所有围栏
    List<Fence> selectAllFence();
}
