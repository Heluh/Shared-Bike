package com.train.campussharedbikeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BikeshareApplication {
    @Bean
    @LoadBalanced  //通过该注解就可以基于服务名进行服务调用
    public RestTemplate restTemplate() {  // 用于调用服务实例
        return new RestTemplate();
    }
    public static void main(String[] args) {
        SpringApplication.run(BikeshareApplication.class, args);
    }

}
