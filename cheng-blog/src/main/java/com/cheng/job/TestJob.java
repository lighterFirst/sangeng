package com.cheng.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void test(){

        //要执行的代码
       /* System.out.println("开启了定时任务，每隔5s执行一次");*/

    }

}
