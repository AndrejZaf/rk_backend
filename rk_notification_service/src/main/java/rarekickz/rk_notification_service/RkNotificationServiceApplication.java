package rarekickz.rk_notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RkNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RkNotificationServiceApplication.class, args);
    }
}
