package com.practice.waitingqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WaitingQueueSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaitingQueueSchedulerApplication.class, args);
    }

}
