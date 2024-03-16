package com.mnus.batch.job;

import cn.hutool.core.date.DateTime;
import com.mnus.common.constance.MDCKey;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/15 20:44:00
 */
@DisallowConcurrentExecution
public class TestJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(TestJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // todo 自定义线程池，在每次启动线程时添加 tid
        MDC.put(MDCKey.TID, UUID.randomUUID().toString());
        System.out.println("QuartzTestJob start..." + DateTime.now());
        LOG.info("do test job...");
        System.out.println("QuartzTestJob end  ..." + DateTime.now());
    }
}
