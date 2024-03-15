// package com.mnus.batch.config;
//
// import com.mnus.batch.job.TestJob;
// import org.quartz.*;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// /**
//  * @author: <a href="https://github.com/xkayah">xkayah</a>
//  * @date: 2024/3/15 20:43:27
//  */
// @Configuration
// public class QuartzConfig {
//
//     /**
//      * 声明一个任务
//      */
//     @Bean
//     public JobDetail jobDetail() {
//         return JobBuilder.newJob(TestJob.class)
//                 .withIdentity("TestJob", "test")
//                 .storeDurably()
//                 .build();
//     }
//
//     /**
//      * 声明一个触发器：何时触发任务
//      */
//     @Bean
//     public Trigger trigger() {
//         return TriggerBuilder.newTrigger()
//                 .forJob(jobDetail())
//                 .startNow()
//                 .withSchedule(
//                         CronScheduleBuilder.
//                                 cronSchedule("*/2 * * * * ?"))
//                 .build();
//     }
// }
