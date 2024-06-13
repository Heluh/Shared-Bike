package com.train.campussharedbikeservercharity.service;

import com.train.campussharedbikeservercharity.entity.Bike;
import com.train.campussharedbikeservercharity.entity.BikeFromCharity;
import com.train.campussharedbikeservercharity.entity.CharityEvent;
import com.train.campussharedbikeservercharity.entity.CharityEventWithBikeCount;
import com.train.campussharedbikeservercharity.mapper.CharityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CharityServiceImpl implements CharityService {

    private final CharityMapper charityMapper;

    @Autowired
    public CharityServiceImpl(CharityMapper charityMapper) {
        this.charityMapper = charityMapper;
    }

    @Override
    public List<CharityEventWithBikeCount> getAllCharityEvents(int page, int count) {
        return charityMapper.getAllCharityEvents((page - 1) * count, count);
    }

    @Override
    public CharityEvent getCharityEventById(int id) {
        return charityMapper.getCharityEventById(id);
    }

    @Override
    public void deleteCharityEventById(int id) {
        charityMapper.deleteCharityEventById(id);
    }

    @Override
    public void insertCharityEvent(CharityEvent charityEvent) {
        charityMapper.insertCharityEvent(charityEvent);
    }

    @Override
    public void updateCharityEvent(CharityEvent charityEvent) {
        charityMapper.updateCharityEvent(charityEvent);
    }

    @Override
    public int getCharityEventCount() {
        return charityMapper.getCharityEventCount();
    }

    @Override
    public List<CharityEventWithBikeCount> getOpenCharityEvents(int page, int count) {
        return charityMapper.getOpenCharityEvents((page - 1) * count, count);
    }

    @Override
    public int getOpenCharityEventCount() {
        return charityMapper.getOpenCharityEventCount();
    }

    @Override
    public List<BikeFromCharity> getBikesWithNonNullId(int page, int count) {
        return charityMapper.getBikesWithNonNullId((page - 1) * count, count);
    }

    @Override
    public List<BikeFromCharity> getBikesWithNullId(int page, int count) {
        return charityMapper.getBikesWithNullId((page - 1) * count, count);
    }

    @Override
    public int getBikesWithNullIdCount() {
        return charityMapper.getBikesWithNullIdCount();
    }

    @Override
    public int getBikesWithNonNullIdCount() {
        return charityMapper.getBikesWithNonNullIdCount();
    }

    @Override
    public void deleteBike(int id) {
        charityMapper.deleteBike(id);
    }

    @Override
    public void addBikeDonation(BikeFromCharity bike) {
        charityMapper.addBikeDonation(bike);
    }

    @Override
    public void insertBikeAndUpdateBinding(int bikeId, int fenceId) {
        Bike bike = new Bike();
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String bikeNumber = dateFormat.format(currentTime);

        bike.setBikeNumber(bikeNumber + bikeId);
        bike.setStatus(0);
        bike.setLongitude(0.0);
        bike.setLatitude(0.0);
        bike.setFenceId(fenceId);
        bike.setCreateTime(currentTime);
        bike.setLastUpdateTime(currentTime);

        charityMapper.insertBike(bike);

        int generatedBikeId = bike.getId();
        System.out.println("generatedBikeId: " + generatedBikeId);

        charityMapper.updateBindBikeId(generatedBikeId, bikeId);
    }
}
