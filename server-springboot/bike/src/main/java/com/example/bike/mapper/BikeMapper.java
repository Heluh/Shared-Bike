package com.example.bike.mapper;


import com.example.bike.bean.Bike;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeMapper {

    // 显示所有单车列表的信息
    public List<Bike> showBike();

    /*
     * 根据id删除车辆
     */
    public int deleteBike(Integer id);

    /*
     * 更改车辆信息
     */
    public int updateBike(Bike bike);

    /*
     * 添加车辆信息
     */
    public int addBike(Bike bike);

    /*
     * 根据车辆编号查询车辆信息
     */
    public List<Bike> findBikeByNum(String bikeNumber);

}

