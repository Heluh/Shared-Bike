package com.example.bike.bean;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Schema(description ="车辆参数")
@Data
public class Bike {

    @Schema(description ="id")
    private Integer id;

    @Schema(description ="车辆编号")
    @NotBlank(message = "车辆编号不能为空")
    private String bikeNumber;

    @Schema(description ="状态")
    private String status;

    @Schema(description ="经度")
    private double longitude;

    @Schema(description ="维度")
    private double latitude;

    @Schema(description ="创建时间")
    private String createTime;

    @Schema(description ="最近一次更新时间")
    private String lastUpdateTime;

    @Schema(description ="围栏编号")
    private int fenceId;
}
