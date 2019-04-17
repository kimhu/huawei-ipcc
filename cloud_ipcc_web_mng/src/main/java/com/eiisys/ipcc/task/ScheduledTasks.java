package com.eiisys.ipcc.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务类 注意 ScheduleConfig
 * <p>cron表达式生成：http://cron.qqe2.com/
 * <p>{秒数} {分钟} {小时} {日期} {月份} {星期} {年份(可为空)}
 */
@Slf4j
@Component
public class ScheduledTasks {

    
    @Scheduled(cron="0 1 0 * * ?")
    public void createNewSubTables() {
        log.info(" begin currtime=");
        
    }
    

}
