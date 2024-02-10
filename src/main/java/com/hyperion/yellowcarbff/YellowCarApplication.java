package com.hyperion.yellowcarbff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YellowCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(YellowCarApplication.class, args);
    }

}
