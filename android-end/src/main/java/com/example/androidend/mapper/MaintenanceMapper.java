package com.example.androidend.mapper;


import com.example.androidend.entity.Maintenance;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface MaintenanceMapper {

    @Select("SELECT * FROM maintenance WHERE id = #{id}")
    Maintenance findById(@Param("id") Long id);

    @Select("SELECT * FROM maintenance")
    List<Maintenance> findAll();

    @Insert("INSERT INTO maintenance(bikeNumber, maintenanceTime, maintenanceType, maintenanceStatus, username) " +
            "VALUES(#{bikeNumber}, #{maintenanceTime}, #{maintenanceType}, #{maintenanceStatus}, #{username})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Maintenance maintenance);

    @Delete("DELETE FROM maintenance WHERE id = #{id}")
    void deleteById(@Param("id") Long id);

    @Update("UPDATE maintenance SET bikeNumber = #{bikeNumber}, maintenanceTime = #{maintenanceTime}, " +
            "maintenanceType = #{maintenanceType}, maintenanceStatus = #{maintenanceStatus}, username = #{username} WHERE id = #{id}")
    void update(Maintenance maintenance);
}