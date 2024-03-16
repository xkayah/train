package com.mnus.batch.controller;

import com.mnus.batch.req.CronJobReq;
import com.mnus.batch.dto.CronJobDto;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.resp.CommonResp;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/admin/job")
public class JobController {
    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 触发一次定时任务
     */
    @RequestMapping(value = "/run")
    public CommonResp<Object> run(@RequestBody CronJobReq cronJobReq) throws SchedulerException {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("[run]name:{}, group:{}", jobClassName, jobGroupName);
        schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobClassName, jobGroupName));
        return CommonResp.success();
    }

    @RequestMapping(value = "/add")
    public CommonResp<Object> add(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        LOG.info("[add]name:{}, group:{}, cron:{}, desc:{}", jobClassName, jobGroupName, cronExpression, description);
        Scheduler scheduler = null;
        try {
            // 通过SchedulerFactory获取一个调度器实例
            scheduler = schedulerFactoryBean.getScheduler();
            // 启动调度器
            scheduler.start();
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassName)).withIdentity(jobClassName, jobGroupName).build();
            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withDescription(description).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("[add]SchedulerException.name:{}, group:{}, cron{}", jobClassName, jobGroupName, cronExpression, e);
            throw new BizException(BaseErrorCodeEnum.BATCH_ADD_JOB_SCHEDULER_ERROR);
        } catch (ClassNotFoundException e) {
            LOG.error("[add]ClassNotFoundException.name:{}, group:{}", jobClassName, jobGroupName, e);
            throw new BizException(BaseErrorCodeEnum.BATCH_ADD_JOB_CLASS_NOT_EXISTS);
        }
        return CommonResp.success();
    }

    @RequestMapping(value = "/pause")
    public CommonResp<Object> pause(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("[pause]name:{}, group:{}", jobClassName, jobGroupName);
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("[pause]:" + e);
            throw new BizException(BaseErrorCodeEnum.BATCH_PAUSE_JOB_SCHEDULER_ERROR);
        }
        return CommonResp.success();
    }

    @RequestMapping(value = "/resume")
    public CommonResp<Object> resume(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("[resume]name:{}, group:{}", jobClassName, jobGroupName);
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("[resume]:" + e);
            throw new BizException(BaseErrorCodeEnum.BATCH_RESUME_JOB_SCHEDULER_ERROR);
        }
        return CommonResp.success();
    }

    @RequestMapping(value = "/reschedule")
    public CommonResp<Object> reschedule(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        LOG.info("[reschedule]name:{}, group:{}, cron:{}, desc:{}", jobClassName, jobGroupName, cronExpression, description);
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTriggerImpl newTrigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            newTrigger.setStartTime(new Date()); // 重新设置开始时间
            CronTrigger trigger = newTrigger;
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withDescription(description).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            LOG.error("[reschedule]:" + e);
            throw new BizException(BaseErrorCodeEnum.BATCH_RESCHEDULE_JOB_SCHEDULER_ERROR);
        }
        return CommonResp.success();
    }

    @RequestMapping(value = "/delete")
    public CommonResp<Object> delete(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("[delete]name:{}, group:{}", jobClassName, jobGroupName);
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("[delete]:" + e);
            throw new BizException(BaseErrorCodeEnum.BATCH_DELETE_JOB_SCHEDULER_ERROR);
        }
        return CommonResp.success();
    }

    @RequestMapping(value = "/query")
    public CommonResp<List<CronJobDto>> query() {
        List<CronJobDto> cronJobDtoList = new ArrayList<>();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    CronJobDto cronJobResp = new CronJobDto();
                    cronJobResp.setName(jobKey.getName());
                    cronJobResp.setGroup(jobKey.getGroup());
                    // 获取定时任务的所有触发器
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    // 获取第一个触发器，向 dto 中设置值
                    CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
                    cronJobResp.setNextFireTime(cronTrigger.getNextFireTime());
                    cronJobResp.setPreFireTime(cronTrigger.getPreviousFireTime());
                    cronJobResp.setCronExpression(cronTrigger.getCronExpression());
                    cronJobResp.setDescription(cronTrigger.getDescription());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                    cronJobResp.setState(triggerState.name());

                    cronJobDtoList.add(cronJobResp);
                }
            }
        } catch (SchedulerException e) {
            LOG.error("[query]:" + e);
            throw new BizException(BaseErrorCodeEnum.BATCH_QUERY_JOB_SCHEDULER_ERROR);
        }
        return CommonResp.success(cronJobDtoList);
    }

}
