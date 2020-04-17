package com.qingcheng.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderTask {
    @Scheduled(cron = "0 0/2 * * * ?")
    public void handleOrderTimout(){

    }
}
