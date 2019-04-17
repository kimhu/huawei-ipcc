package com.eiisys.ipcc.config;

import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@EnableScheduling
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    /**
     * 用来控制是否执行定时任务，以及设置线程池大小
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
        
//        List cronTaskList = taskRegistrar.getCronTaskList();
//        List fixedDelayTaskList = taskRegistrar.getFixedDelayTaskList();
//        List FixedRateTaskList = taskRegistrar.getFixedRateTaskList();
//        List triggerTaskList = taskRegistrar.getTriggerTaskList();
//        
//        System.out.println(cronTaskList.size());
//        System.out.println(fixedDelayTaskList.size());
//        System.out.println(FixedRateTaskList.size());
//        System.out.println(triggerTaskList.size());
        
//        taskRegistrar.setFixedRateTasksList(null);
        
//        System.out.println("over");
    }

}
