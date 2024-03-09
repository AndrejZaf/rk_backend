package com.rarekickz.rk_payment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RkPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RkPaymentServiceApplication.class, args);
    }

}
