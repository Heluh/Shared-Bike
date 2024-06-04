package com.train.campussharedbikeserver.server;
import com.train.campussharedbikeserver.bean.Fence;
import com.train.campussharedbikeserver.mapper.FenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FenceService {
    @Autowired
    private FenceMapper fenceMapper;

    public List<Fence> selectAllFence(){
        return fenceMapper.selectAllFence();
    }
}
