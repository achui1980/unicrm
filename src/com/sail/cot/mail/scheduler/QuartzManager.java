/**
 * 
 */
package com.sail.cot.mail.scheduler;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

/** *//**
 * @Title:Quartz管理类
 * 
 * @Description:
 * 
 * @Copyright: 
 * @author achui  2009-05-11 14:19:01
 * @version 1.00.000
 *
 */
public class QuartzManager {
   private static SchedulerFactory sf = new StdSchedulerFactory();
   private static String JOB_GROUP_NAME = "group1";
   private static String TRIGGER_GROUP_NAME = "trigger1";
  
   
   /** *//**
    *  添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
    * @param jobName 任务名
    * @param job     任务
    * @param time    时间设置，参考quartz说明文档
    * @throws SchedulerException
    * @throws ParseException
    */
   public static void addJob(String jobName,Job job,int minutes,JobDataMap dataMap) 
                               throws Exception{
       Scheduler sched = sf.getScheduler();
       JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, job.getClass());//任务名，任务组，任务执行类
       jobDetail.setJobDataMap(dataMap);
       //触发器
       Trigger trigger = TriggerUtils.makeMinutelyTrigger(minutes);
       trigger.setName("Trigger_"+jobName);  
	   long currtime = System.currentTimeMillis();// + 1*60*1000;//1分钟后启动
	   trigger.setStartTime(new Date(currtime));   
//       CronTrigger  trigger = 
//            new CronTrigger(jobName, TRIGGER_GROUP_NAME);//触发器名,触发器组
//       trigger.setCronExpression(time);//触发器时间设定
       sched.scheduleJob(jobDetail,trigger);
       //启动
       if(!sched.isShutdown())
          sched.start();
   }
   
   /** *//**
    * 添加一个定时任务
    * @param jobName 任务名
    * @param jobGroupName 任务组名
    * @param triggerName  触发器名
    * @param triggerGroupName 触发器组名
    * @param job     任务
    * @param time    时间设置，参考quartz说明文档
    * @param dataMap    传递的参数
    * @param triggerCfgMap    传递的参数
    * @throws SchedulerException
    * @throws ParseException
    */
   public static void addJob(String jobName,String jobGroupName,
                             String triggerName,String triggerGroupName,
                             Job job,String time,JobDataMap dataMap,Map triggerCfgMap) 
                               throws SchedulerException, ParseException{
       Scheduler sched = sf.getScheduler();
       JobDetail jobDetail = new JobDetail(jobName, jobGroupName, job.getClass());//任务名，任务组，任务执行类
       jobDetail.setJobDataMap(dataMap);
       //触发器
       CronTrigger  trigger = 
            new CronTrigger(triggerName, triggerGroupName);//触发器名,触发器组
       trigger.setCronExpression(time);//触发器时间设定
       if(triggerCfgMap != null){
    	   if(triggerCfgMap.get("START_TIME") != null)
    		   trigger.setStartTime((Date)triggerCfgMap.get("START_TIME"));
    	   if(triggerCfgMap.get("END_TIME") != null)
    		   trigger.setEndTime((Date)triggerCfgMap.get("END_TIME"));
       }
       sched.scheduleJob(jobDetail,trigger);
       if(!sched.isShutdown())
          sched.start();
   }
   
   /** *//**
    * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
    * @param jobName
    * @param time
    * @throws SchedulerException
    * @throws ParseException
    */
   public static void modifyJobTime(String jobName,String time) 
                                  throws SchedulerException, ParseException{
       Scheduler sched = sf.getScheduler();
       Trigger trigger =  sched.getTrigger(jobName,TRIGGER_GROUP_NAME);
       if(trigger != null){
           CronTrigger  ct = (CronTrigger)trigger;
           ct.setCronExpression(time);
           sched.resumeTrigger(jobName,TRIGGER_GROUP_NAME);
       }
   }
   
   /** *//**
    * 修改一个任务的触发时间
    * @param triggerName
    * @param triggerGroupName
    * @param time
    * @throws SchedulerException
    * @throws ParseException
    */
   public static void modifyJobTime(String triggerName,String triggerGroupName,
                                    String time,Date startDate,Date endDate) 
                                  throws SchedulerException, ParseException{
       Scheduler sched = sf.getScheduler();
       Trigger trigger =  sched.getTrigger(triggerName,triggerGroupName);
       if(trigger != null){
           CronTrigger  ct = (CronTrigger)trigger;
           if(startDate !=null)
        	   ct.setStartTime(startDate);
           if(endDate != null)
        	   ct.setEndTime(endDate);
           //修改时间
           ct.setCronExpression(time);
           //重启触发器
           sched.resumeTrigger(triggerName,triggerGroupName);
       }
   }
   
   /** *//**
    * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
    * @param jobName
    * @throws SchedulerException
    */
   public static void removeJob(String jobName) 
                               throws SchedulerException{
       Scheduler sched = sf.getScheduler();
       sched.pauseTrigger(jobName,TRIGGER_GROUP_NAME);//停止触发器
       sched.unscheduleJob(jobName,TRIGGER_GROUP_NAME);//移除触发器
       sched.deleteJob(jobName,JOB_GROUP_NAME);//删除任务
   }
   
   /** *//**
    * 移除一个任务
    * @param jobName
    * @param jobGroupName
    * @param triggerName
    * @param triggerGroupName
    * @throws SchedulerException
    */
   public static void removeJob(String jobName,String jobGroupName,
                                String triggerName,String triggerGroupName) 
                               throws SchedulerException{
       Scheduler sched = sf.getScheduler();
       sched.pauseTrigger(triggerName,triggerGroupName);//停止触发器
       sched.unscheduleJob(triggerName,triggerGroupName);//移除触发器
       sched.deleteJob(jobName,jobGroupName);//删除任务
   }
   
   public static Scheduler getScheduler() throws SchedulerException{
	   Scheduler sched = sf.getScheduler();
	   return sched;
   }
}