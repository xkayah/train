package com.mnus.batch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mnus.batch.feign.BusinessFeign;
import com.mnus.common.constance.MDCKey;
import com.mnus.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Date;
import java.util.UUID;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/16 14:51:47
 */
public class DailyTrainJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);
    public static final int OFFSET_DAY = 15;
    @Resource
    private BusinessFeign businessFeign;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        MDC.put(MDCKey.TID, UUID.randomUUID().toString());
        Date date = DateUtil.offsetDay(DateTime.now(), OFFSET_DAY).toJdkDate();
        CommonResp<Object> resp = businessFeign.genDailyTrain(date);
        LOG.info("[daily train job]gen date after 15 days.");
    }
}
