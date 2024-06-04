package com.train.campussharedbikeservercharity.mapper;

import com.train.campussharedbikeservercharity.entity.Bike;
import com.train.campussharedbikeservercharity.entity.BikeFromCharity;
import com.train.campussharedbikeservercharity.entity.CharityEvent;
import com.train.campussharedbikeservercharity.entity.CharityEventWithBikeCount;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharityMapper {
    List<CharityEventWithBikeCount> getAllCharityEvents(int page, int count);

    CharityEvent getCharityEventById(int id);

    void deleteCharityEventById(int id);

    void insertCharityEvent(CharityEvent charityEvent);

    void updateCharityEvent(CharityEvent charityEvent);

    int getCharityEventCount();

    List<CharityEventWithBikeCount> getOpenCharityEvents(int page, int count);

    int getOpenCharityEventCount();

    List<BikeFromCharity> getBikesWithNullId(int page, int count);

    List<BikeFromCharity> getBikesWithNonNullId(int page, int count);

    int getBikesWithNullIdCount();

    int getBikesWithNonNullIdCount();

    @Delete("DELETE FROM bikeFromCharity WHERE id = #{id}")
    void deleteBike(int id);

    void addBikeDonation(BikeFromCharity bike);

    @Insert("INSERT INTO bike (bikeNumber, status, longitude, latitude, createTime, lastUpdateTime, fenceId) " +
            "VALUES (#{bikeNumber}, #{status}, #{longitude}, #{latitude}, #{createTime}, #{lastUpdateTime}, #{fenceId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertBike(Bike bike);

    void updateBindBikeId(int bindBikeId, int bikeId);
}
