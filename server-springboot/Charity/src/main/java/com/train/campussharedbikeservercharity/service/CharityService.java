package com.train.campussharedbikeservercharity.service;

import com.train.campussharedbikeservercharity.entity.BikeFromCharity;
import com.train.campussharedbikeservercharity.entity.CharityEvent;
import com.train.campussharedbikeservercharity.entity.CharityEventWithBikeCount;

import java.util.List;

public interface CharityService {
    // 获取所有捐赠活动
    List<CharityEventWithBikeCount> getAllCharityEvents(int page, int count);

    // 获取指定id的捐赠活动
    CharityEvent getCharityEventById(int id);

    // 删除指定id的捐赠活动
    void deleteCharityEventById(int id);

    // 插入新的捐赠活动
    void insertCharityEvent(CharityEvent charityEvent);

    // 更新指定id的捐赠活动
    void updateCharityEvent(CharityEvent charityEvent);

    // 获取捐赠活动的总数
    int getCharityEventCount();

    // 获取当前开放的捐赠活动
    List<CharityEventWithBikeCount> getOpenCharityEvents(int page, int count);

    // 获取当前开放的捐赠活动的总数
    int getOpenCharityEventCount();

    List<BikeFromCharity> getBikesWithNonNullId(int page, int count);


    List<BikeFromCharity> getBikesWithNullId(int page, int count);

    int getBikesWithNullIdCount();

    int getBikesWithNonNullIdCount();

    void deleteBike(int id);

    void addBikeDonation(BikeFromCharity bike);

    void insertBikeAndUpdateBinding(int bikeId, int fenceId);
}
