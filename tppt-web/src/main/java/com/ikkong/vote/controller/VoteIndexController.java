package com.ikkong.vote.controller;

import com.ikkong.core.base.BaseController;
import com.ikkong.core.dao.Db;
import com.ikkong.core.toolbox.Record;
import com.ikkong.vote.service.EventService;
import com.ikkong.vote.service.VoteListService;
import com.ikkong.vote.service.impl.EventServiceImpl;
import com.ikkong.vote.service.impl.VoteListServiceImpl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Generated by Blade.
 * 2016-10-08 15:59:45
 */
public class VoteIndexController extends BaseController {
    private static String CODE = "vote";
    private static String PERFIX = "vote";
    private static String LIST_SOURCE = "VoteList.list";
    private static String BASE_PATH = "/voteindex/";

    EventService eventService = new EventServiceImpl();
    VoteListService voteListService = new VoteListServiceImpl();

    DecimalFormat decimalFormat = new DecimalFormat("###,###,###.0");



    public void index() {
        setAttr("code", CODE);
        initData();
        countInfo();
        render(BASE_PATH + "list.html");
    }

    /**
     * 取首页数据
     */
    private void initData() {
        Record record = Record.create();
        if(!isAdmin()&&!isStatistics()) {
            record.set("deptid", getDeptId());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Date endDate = null;
        Calendar calendar = Calendar.getInstance();
        //1.取今日交易数
        calendar.setTime(beginDate);
        calendar.add(Calendar.DATE, 1);
        endDate = calendar.getTime();
        record.set("beginDate", sdf.format(beginDate));
        record.set("endDate", sdf.format(endDate));
        Double todayAmount = voteListService.sumAmount(record);
        setAttr("todayAmount", decimalFormat.format(todayAmount));
        //2.取昨日交易数
        endDate = new Date();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, -1);
        beginDate = calendar.getTime();
        record.set("beginDate", sdf.format(beginDate));
        record.set("endDate", sdf.format(endDate));
        Double yesterdayAmount = voteListService.sumAmount(record);
        setAttr("yesterdayAmount", decimalFormat.format(yesterdayAmount));
        //3.取累计交易数
        record = Record.create();
        if(!isAdmin()&&!isStatistics()) {
            record.set("deptid", getDeptId());
        }
        Double totleAmount = voteListService.sumAmount(record);
        setAttr("totleAmount", decimalFormat.format(totleAmount));
        //4.取累计活动数
        Integer totleEvents = eventService.countEvent(record);
        setAttr("totleEvents", totleEvents);
    }

    /**
     * 图标信息
     */
    private void countInfo() {
        Record record = Record.create();
        if(!isAdmin()&&!isStatistics()) {
            record.set("deptid", getDeptId());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, -15);
        beginDate = calendar.getTime();
        record.set("beginDate",beginDate);
        record.set("endDate",endDate);
        List<Record> records = voteListService.countInfo(record);

        String xAxis = "";
        String totletickets = "";
        String viptickets = "";
        String vipamounts = "";
        if (records != null && records.size() > 0) {
            for (Record r : records) {
                xAxis=xAxis+","+r.get("date");
                totletickets=totletickets+","+r.get("totletickets");
                viptickets=viptickets+","+r.get("viptickets");
                vipamounts=vipamounts+","+r.get("vipamount");
            }

            xAxis=xAxis.substring(1);
            totletickets=totletickets.substring(1);
            viptickets=viptickets.substring(1);
            vipamounts=vipamounts.substring(1);
            setAttr("xAxis",xAxis);
            setAttr("totletickets",totletickets);
            setAttr("viptickets",viptickets);
            setAttr("vipamounts",vipamounts);
        }
    }


    /**
     * 取内容
     */
    public void list() {
        Record record = Record.create();
        if(!isAdmin()&&!isStatistics()) {
            record.set("deptid", getDeptId());
        }
      List<Record> list= voteListService.querylastvipList(record);
      setAttr("list",list);
      render(BASE_PATH + "table.html");
    }



}
