package com.example.maintenance.mapper;

import com.example.maintenance.bean.Maintenance;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceMapper {
    // 显示所有维修列表的信息
    public List<Maintenance> showMaintenance();

    /*
     * 根据id删除维修信息
     */
    public int deleteMaintenance(Integer id);

    /*
     * 更改维修信息
     */
    public int updateMaintenance(Maintenance maintenance);

    /*
     * 添加维修信息
     */
    public int addMaintenance(Maintenance maintenance);

    /*
     * 根据车辆编号查询维修信息
     */
    public List<Maintenance> findMaintenanceByNum(String bikeNumber);
}
