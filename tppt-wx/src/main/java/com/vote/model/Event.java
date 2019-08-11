package com.vote.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Event extends Model<Event> {

    public static final Event dao = new Event();

    public Event  findFirstById(Integer id){
        String select="select *,(select count(0) from t_merchant m where status=1 and audit_status=1  and  e.id=m.eventid) as mcount,\n" +
                "case when e.voteendtime>now() then 0 else 1 end as ended\n" +
                "from t_event e where id=?";
        return findFirst(select, id);
    }

    public void addHits(Integer id){
        String sql="update t_event set hits=ifnull(hits,0)+1 where id=?";
        Db.update(sql, new Object[]{id});
    }

//
//
//    /**
//     *
//     * @param eventId 事件ID
//     * @param pagenum 页码
//     * @param number 选手号
//     * @return
//     */
//    public Page<Event> queryPage(Integer eventId, Integer pagenum, Integer number){
//        String select="select * ";
//        String from=" from t_merchant  ";
//        // 获取所有的工作日志
//        String where = " where status=1 audit_status=1 and eventid=?";
//        Object[] paramter=new Object[]{eventId};
//        if (number!=null) {
//            where += " and indexno=?";
//            paramter=new Object[]{eventId,number};
//        }
//
//        String order=" order by totlevotes desc";
//
//        Page<Event> page = paginate(pagenum, 20, select,from+where+order,paramter);
//        return page;
//    }

}
