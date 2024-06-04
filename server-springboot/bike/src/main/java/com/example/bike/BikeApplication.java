package com.example.bike;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



//配置扫描MyBatis接口的包路径
@MapperScan(basePackages={"com.example.bike.mapper"})
@SpringBootApplication
public class BikeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeApplication.class, args);
    }

}
