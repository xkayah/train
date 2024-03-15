package com.mnus.batch.job;

import cn.hutool.core.date.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/15 20:44:00
 */
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("QuartzTestJob start..." + DateTime.now());
    }
}
