package com.ikkong.vote.task;

import com.ikkong.core.toolbox.Record;
import com.ikkong.vote.model.Merchant;
import com.ikkong.vote.model.Task;
import com.ikkong.vote.model.VoteList;
import com.ikkong.vote.service.EventService;
import com.ikkong.vote.service.MerchantService;
import com.ikkong.vote.service.TaskService;
import com.ikkong.vote.service.VoteListService;
import com.ikkong.vote.service.impl.EventServiceImpl;
import com.ikkong.vote.service.impl.MerchantServiceImpl;
import com.ikkong.vote.service.impl.TaskServiceImpl;
import com.ikkong.vote.service.impl.VoteListServiceImpl;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 投票调整任务
 * 1.每1分钟执行一次,通过批次号来排除重复
 */
public class VoteJob implements Job {

    Log logger = Log.getLog(VoteJob.class);
    TaskService taskService = new TaskServiceImpl();
    VoteListService voteListService = new VoteListServiceImpl();
    MerchantService merchantService = new MerchantServiceImpl();
    EventService eventService = new EventServiceImpl();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Record record = Record.create();
        record.set("batchno", sdf.format(new Date()));
        logger.info("开始定时任务，编号：" + record.get("batchno"));
        List<Task> taskList = null;
        Record taskRecord = Record.create();
        while (true) {
            taskList = taskService.queryToDoList(record);
            if (taskList == null || taskList.size() == 0) {
                logger.info("无待执行数据，退出" + record.get("batchno"));
                return;
            }
            for (Task task : taskList) {
                dealTask(task);
                //更新处理状态
                taskRecord.set("id",task.getId());
                taskRecord.set("exectime",new Date());
                taskRecord.set("execbatchno",record.getStr("batchno"));
                taskService.updateExec(taskRecord);
            }
        }
    }

    private void dealTask(Task task) {
        Integer eid = task.getEvent_id();
        Integer mid = task.getMerchantid();
        Integer tickets = 0;
        Integer hits = 0;
        Random random=new Random();
        Record eRecord= Record.create();
        Record mRecord= Record.create();
        if (eid != null) {
            Record record = Record.create();
            record.set("eventid", eid);
            List<Merchant> merchantList = merchantService.queryList(record);
            if (merchantList != null) {
                for (Merchant merchant : merchantList) {
                    tickets = randomAddVoute(task);
                    //插入一条投票记录
                    if (tickets > 0) {
                        //人气额外0-3倍
                        hits=tickets*(random.nextInt(2)+1);
                        //addVouteList(merchant, tickets);
                        //选手票数
                        mRecord.set("id", merchant.getId());
                        mRecord.set("tickets", tickets);
                        eRecord.set("hits",hits);
                        merchantService.updateTicket(mRecord);
                        //活动活动票数
                        eRecord.set("id",eid);
                        eRecord.set("tickets",tickets);
                        eRecord.set("hits",hits);
                        eventService.updateTicket(eRecord);
                    }
                }
            }
        } else if (mid != null) {
            Merchant merchant = merchantService.findById(mid);
            if (merchant != null) {
                tickets = randomAddVoute(task);
                //插入一条投票记录
                if (tickets > 0) {
                    //addVouteList(merchant, tickets);
                    //选手票数
                    mRecord.set("id", merchant.getId());
                    mRecord.set("tickets", tickets);
                    eRecord.set("hits",hits);
                    merchantService.updateTicket(mRecord);
                }
                //人气额外0-3倍
                hits=tickets*(random.nextInt(3));
                mRecord.set("id",merchant.getId());
                mRecord.set("hits",hits);
                merchantService.updateHits(mRecord);
            }
        }
    }

    /**
     * 是否增加投票
     * 2分钟执行一次
     * @param task
     * @return
     */
    private Integer randomAddVoute(Task task) {
        Integer ticketbase=PropKit.getInt("tickets.base",6);
        Integer tickets = 0;
        Random random = new Random();
        if (task.getMaxvalue() <= 60) {
            //60-N作为怎加票数区间
            Integer min = 60 - task.getMaxvalue();
            Integer max = 60 - task.getMinvalue();
            Integer r = random.nextInt(60);
            if (r >= min && r <= max) {
                tickets = 1*(random.nextInt(ticketbase)+1);
            }
        } else {
            //分两段判读，小于60的一段和60到N的一段
            //第一段
            Integer min = 0;
            Integer max = 60 - task.getMinvalue();
            Integer r = random.nextInt(60);
            if (r >= min && r <= max) {
                tickets = 1*(random.nextInt(ticketbase)+1);
            }
            //第二段 60～N
            //超出60的部分
            Integer leave = task.getMaxvalue() - 60;
            tickets = tickets + (Math.round(random.nextInt(leave) * 1.0F / (leave))*(random.nextInt(3)+1));
        }
        return tickets;
    }

    /**
     * 增加投票
     *
     * @param merchant
     */
    private void addVouteList(Merchant merchant, Integer tickets) {
        for (int i = 0; i < tickets; i++) {
            VoteList voteList = new VoteList();
            voteList.setEventid(merchant.getEventid());
            voteList.setMerchantid(merchant.getId());
            voteList.setOpenid(UUID.randomUUID().toString());
            voteList.setStatus("1");
            voteList.setVersion(1);
            voteList.setTickets(1);
            voteList.setVote_time(new Date());
            voteList.setVotetype(1);
            voteListService.save(voteList);
        }
    }


}
