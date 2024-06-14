package com.gxdcnjq.sharedbikesmis.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class RideRecord implements Parcelable {
    private String recordId; // 记录 ID
    private String startTime; // 开始时间
    private long duration; // 持续时间
    private double distance; // 总距离
    private List<Location> locationList; // 位置信息列表

    // 构造方法
    public RideRecord(String recordId, String startTime, long duration) {
        this.recordId = recordId;
        this.startTime = startTime;
        this.duration = duration;
        this.distance = 0.0; // 初始化距离为0
        this.locationList = new ArrayList<>(); // 初始化位置列表
    }

    // Parcelable 构造方法
    protected RideRecord(Parcel in) {
        recordId = in.readString();
        startTime = in.readString();
        duration = in.readLong();
        distance = in.readDouble();
        locationList = in.createTypedArrayList(Location.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recordId);
        dest.writeString(startTime);
        dest.writeLong(duration);
        dest.writeDouble(distance);
        dest.writeTypedList(locationList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RideRecord> CREATOR = new Creator<RideRecord>() {
        @Override
        public RideRecord createFromParcel(Parcel in) {
            return new RideRecord(in);
        }

        @Override
        public RideRecord[] newArray(int size) {
            return new RideRecord[size];
        }
    };


    // 获取记录 ID
    public String getRecordId() {
        return recordId;
    }

    // 设置记录 ID
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    // 获取开始时间
    public String getStartTime() {
        return startTime;
    }

    // 设置开始时间
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    // 获取持续时间
    public long getDuration() {
        return duration;
    }

    // 设置持续时间
    public void setDuration(long duration) {
        this.duration = duration;
    }

    // 获取总距离
    public double getDistance() {
        return distance;
    }

    // 设置总距离
    public void setDistance(double distance) {
        this.distance = distance;
    }

    // 获取位置信息列表
    public List<Location> getLocationList() {
        return locationList;
    }

    // 设置位置信息列表
    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

}
