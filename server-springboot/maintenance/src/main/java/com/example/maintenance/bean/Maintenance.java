package com.example.maintenance.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description ="维修参数")
@Data
public class Maintenance {
    @Schema(description ="id")
    private Integer id;

    @Schema(description ="车辆编号")
    @NotBlank(message = "车辆编号不能为空")
    private String bikeNumber;

    @Schema(description ="维修时间")
    private String maintenanceTime;

    @Schema(description ="维修类型")
    private String maintenanceType;

    @Schema(description ="维修状态")
    private String maintenanceStatus;

}
