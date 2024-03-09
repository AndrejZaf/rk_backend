package com.rarekickz.rk_gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RkGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RkGatewayServiceApplication.class, args);
    }

}
