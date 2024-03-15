// package com.mnus.batch.job;
//
// import org.springframework.scheduling.annotation.Scheduled;
//
// /**
//  * 适合单体小项目，不适合集群
//  * 可以使用分布式锁解决任务重复问题，但无法实时更改定时任务状态和策略
//  *
//  * @author: <a href="https://github.com/xkayah">xkayah</a>
//  * @date: 2024/3/15 20:26:24
//  */
// @Component
// @EnableScheduling
// public class SpringBootTestJob {
//     @Scheduled(cron = "0/5 * * * * ?")
//     private void test() {
//         System.out.println("SpringBootTestJob start..." + DateTime.now());
//     }
// }
