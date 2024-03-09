package rarekickz.rk_order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RkOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RkOrderServiceApplication.class, args);
    }

}
