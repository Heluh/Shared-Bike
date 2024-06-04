package com.train.campussharedbikeservercharity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@MapperScan("com.train.campussharedbikeservercharity.mapper")
@EnableDiscoveryClient
public class CampusSharedBikeServerCharityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusSharedBikeServerCharityApplication.class, args);
    }

}
