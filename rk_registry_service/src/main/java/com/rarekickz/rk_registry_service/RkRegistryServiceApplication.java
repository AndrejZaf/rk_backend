package com.rarekickz.rk_registry_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RkRegistryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RkRegistryServiceApplication.class, args);
    }

}
