package com.vote.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

public class Merchant extends Model<Merchant> {

    public static final Merchant dao = new Merchant();



    public void addHits(Integer id){
        String sql="update t_merchant set hits=ifnull(hits,0)+1 where id=?";
        Db.update(sql, new Object[]{id});
    }

    public Integer canEntry(String openId, Integer eId){
        String sql = "select  case when count(0)>3 then 0 else 1 end \n" +
                "from t_merchant m \n" +
                "where m.eventid=? and m.openid=?";
      return Db.queryLong(sql, new Object[]{ eId,openId}).intValue();
    }


    public List<Merchant> queryByOrder(Integer eid){
        String sql="select m.id,m.eventid,m.indexno,m.name,m.totlevotes,m.pictures,\n" +
                "@rownum:=@rownum+1 AS rownum\n" +
                "from t_merchant m,(SELECT @rownum:=0) r\n" +
                "where m.eventid=? and m.status=1  and audit_status=1 \n" +
                "order by totlevotes desc ";
        return find(sql, new Object[]{eid});
    }

    public Merchant  findFirstByIdWithOrder(Integer eid,Integer mid){
        String select="select *,\n" +
                "(select max(totlevotes) from t_merchant m_min where m_min.eventid=m.eventid and m_min.id!=m.id and m_min.totlevotes<=m.totlevotes) next ,\n" +
                "(select min(totlevotes) from t_merchant m_min where m_min.eventid=m.eventid and m_min.id!=m.id and m_min.totlevotes>=m.totlevotes) pre \n" +
                "from (\n" +
                "select m.*,@rownum:=@rownum+1 AS rownum\n" +
                "from t_merchant m,(SELECT @rownum:=0) r\n" +
                "where m.eventid=? and m.status=1  and audit_status=1 order by totlevotes desc) m\n" +
                "where m.id=?;";
        return findFirst(select, new Object[]{eid,mid});
    }

    /**
     *
     * @param eventId 事件ID
     * @param pagenum 页码
     * @param number 选手号
     * @return
     */
    public Page<Merchant> queryPage(Integer eventId, Integer pagenum, Integer number){
        String select="select * ";
        String from=" from t_merchant  ";
        // 获取所有的工作日志
        String where = " where status=1 and audit_status=1 and eventid=?";
        Object[] paramter=new Object[]{eventId};
        if (number!=null) {
            where += " and indexno=?";
            paramter=new Object[]{eventId,number};
        }

        String order=" order by totlevotes desc";

        Page<Merchant> page = paginate(pagenum, 10, select,from+where+order,paramter);
        return page;
    }


    /**
     * 取推荐数据，
     * @param pagenum
     * @param pagesize
     * @param type 1 首页数据、0新闻数据
     * @return
     */
    public Page<Merchant> queryHomeShowPage( Integer pagenum, Integer pagesize,Integer type){
        String select="select e.name ename,e.voteendtime,m.*," +
                "ifnull((select count(0) from t_merchant m1 where m1.eventid=e.id and m1.id!=m.id and m1.totlevotes>m.totlevotes),0)+1 orderIndex ";
        String from=" from  t_event e \n" +
                "join t_merchant  m on e.id=m.eventid  ";
        // 获取所有的工作日志
        String where = " where e.status=1 and m.status=1 and m.homepage=1";
        if(type==1){
            where+="  and e.voteendtime>DATE_SUB(now(),INTERVAL 30 DAY)";
        }

        String order=" order by  voteendtime desc";

        Page<Merchant> page = paginate(pagenum, pagesize, select,from+where+order);
        return page;
    }


    /**
     *
     * @param mid
     * @return
     */
    public Merchant queryMerchantWidthOrder(Integer mid){
        String sql="select m.*,e.name as ename ,ifnull((select count(0) from t_merchant m1 where m1.eventid=m.eventid and m1.id!=m.id and m1.totlevotes>m.totlevotes),0)+1  AS orderIndex\n" +
                "from t_merchant m\n" +
                "join t_event e on m.eventid=e.id\n" +
                "where m.id=? and m.status=1  and audit_status=1  and homepage=1" ;
        return findFirst(sql, new Object[]{mid});
    }


}
