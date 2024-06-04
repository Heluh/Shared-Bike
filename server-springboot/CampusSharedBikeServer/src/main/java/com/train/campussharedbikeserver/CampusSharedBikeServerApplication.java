package com.train.campussharedbikeserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@MapperScan("com.train.campussharedbikeserver.mapper")
@EnableDiscoveryClient
public class CampusSharedBikeServerApplication {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }
    public static void main(String[] args) {
        SpringApplication.run(CampusSharedBikeServerApplication.class, args);
    }

}
