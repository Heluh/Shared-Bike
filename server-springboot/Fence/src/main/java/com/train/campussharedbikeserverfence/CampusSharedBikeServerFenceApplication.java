package com.train.campussharedbikeserverfence;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@MapperScan("com.train.campussharedbikeserverfence.mapper")
@EnableDiscoveryClient
public class CampusSharedBikeServerFenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusSharedBikeServerFenceApplication.class, args);
    }

}
