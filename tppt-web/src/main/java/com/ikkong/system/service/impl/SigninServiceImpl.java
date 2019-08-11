
package com.ikkong.system.service.impl;

import com.ikkong.core.base.service.impl.BaseService;
import com.ikkong.core.dao.Db;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.model.Signin;
import com.ikkong.system.service.SigninService;
import java.util.List;

public class SigninServiceImpl extends BaseService<Signin>
		implements SigninService
{

	public SigninServiceImpl()
	{
	}

	public List querySigninList(Record record)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" select s.*,FIND_IN_SET(s.area_id,u.attendareas)>0 isOut");
		sql.append(" from glpt_signin s ");
		sql.append(" join tfw_user u on s.userid=u.ID");
		if(record.containsKey("userid"))
			sql.append(" and s.userid=#{userid} ");
		if(record.containsKey("signdate"))
			sql.append(" and s.signdate=#{signdate} ");
		sql.append(" order by s.id ");
		return Db.init().selectList(sql.toString(), record);
	}

	public List queryByDeptAndDate(Record record)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" select d.id as deptid,d.FULLNAME,leaveusers `leaves`,sum(IFNULL(times,0)) times");
		sql.append(" ,(select count(0) from tfw_user tu where d.ID=tu.DEPTID and tu.`STATUS`=1) users");
		sql.append(" ,sum(IFNULL(amError,0))amError,sum(IFNULL(pmError,0))pmError,sum(IFNULL(qtqq,0)) allDayLost");
		sql.append(" ,(select count(0) from glpt_workday gw where gw.type=0 and gw.day BETWEEN '#{text(signdate_dategt)}' and '#{text(signdate_datelt)}' ) as workdays");
		sql.append("  from tfw_dept d ");
		sql.append("  LEFT JOIN glpt_workday w  on w.type=0 and w.day BETWEEN '#{text(signdate_dategt)}' and '#{text(signdate_datelt)}'");
		sql.append("  left join (");
		sql.append("            select day,DEPTID deptid,");
		sql.append("            sum(times) times,sum(amsb) amsb,sum(amin) amin,sum(amxb) amxb,");
		sql.append("            sum(case when amsb=0  or amxb=0  then 1 else 0 end) amError,");
		sql.append("            sum(pmsb) pmsb,sum(pmin) pmin,sum(pmxb) pmxb,");
		sql.append("            sum(case when pmsb=0 or pmxb=0  then 1 else 0 end) pmError,");
		sql.append("            sum(case when times=0  then 1 else 0 end) qtqq");
		sql.append("             from(");
		sql.append("                    select w.day,u.DEPTID deptid,u.id,");
		sql.append("                    sum(case when s.id is not null then 1 else 0 end)times,");
		sql.append("                    sum(case when DATE_FORMAT(`signtime`,'%H:%i')<='08:30' then 1 else 0 end) amsb,");
		sql.append("                    sum(case when DATE_FORMAT(`signtime`,'%H:%i') BETWEEN '08:31' and '11:59' then 1 else 0 end) amin,");
		sql.append("                    sum(case when DATE_FORMAT(`signtime`,'%H:%i') BETWEEN '12:00' and '13:29' then 1 else 0 end) amxb,");
		sql.append("                    sum(case when DATE_FORMAT(`signtime`,'%H:%i') BETWEEN '13:30' and '15:00' then 1 else 0 end) pmsb,");
		sql.append("                    sum(case when DATE_FORMAT(`signtime`,'%H:%i') BETWEEN '15:01' and '17:59' then 1 else 0 end) pmin,");
		sql.append("                    sum(case when DATE_FORMAT(`signtime`,'%H:%i')>='18:00' then 1 else 0 end) pmxb");
		sql.append("                    from glpt_workday w");
		sql.append("                    join tfw_user u on  u.status=1 ");
		sql.append("                    left join glpt_signin s on w.day=s.signdate and s.userid=u.ID ");
		sql.append("                    where  w.type=0 and  w.day BETWEEN '#{text(signdate_dategt)}' and '#{text(signdate_datelt)}'");
		sql.append("                    group by w.day,u.DEPTID,u.id");
		sql.append("             ) a");
		sql.append("             group by a.day,a.DEPTID");
		sql.append("            ) s  on s.deptid=d.ID   and w.day=s.day");
		sql.append("  left join (");
		sql.append("            select d.ID deptid,sum(case when d.ID is not null then 1 else 0 end) leaveusers");
		sql.append("            from  glpt_workday w  ");
		sql.append("            join glpt_leave l on w.day BETWEEN DATE_FORMAT(l.start_time,'%Y-%m-%d') and DATE_FORMAT(l.end_time,'%Y-%m-%d') and l.audit_status=1");
		sql.append("            join tfw_user u on u.ID=l.submit_user_id  and u.status=1 ");
		sql.append("            join tfw_dept d on u.DEPTID=d.ID");
		sql.append("            where  w.type=0 and w.day BETWEEN '#{text(signdate_dategt)}' and '#{text(signdate_datelt)}'");
		sql.append("            group by d.ID");
		sql.append("           ) l on  l.deptid=d.ID");
		sql.append(" where 1=1 ");
		if(record.containsKey("deptid"))
			sql.append(" and d.id=#{deptid} ");
		sql.append(" group by d.id,d.FULLNAME");
		sql.append(" order by d.NUM");
		return Db.init().selectList(sql.toString(), record);
	}

	public Object queryUserSigin(Record record, int pageIndex, int pageSize)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" select d.id as deptid,d.FULLNAME,u.id as userid,u.`NAME`,w.day, 0 `isdayleave`, 0 `isamleave`, 0 `ispmleave` ");
		sql.append(" ,IFNULL(times,0) times");
		sql.append(" ,case when IFNULL(times,0)=0 then 1 else 0 end  daylost ");
		sql.append(" ,case when IFNULL(amsb,0)>0 and IFNULL(amxb,0)>0  then 1 else 0 end  amnormal ");
		sql.append(" ,case when IFNULL(amsb,0)+IFNULL(amin,0)+IFNULL(amxb,0)=0  then 1 else 0 end  amlost ");
		sql.append(" ,case when IFNULL(amsb,0)=0 and IFNULL(amin,0)+IFNULL(amxb,0)>0 then 1 else 0 end   amlate  ");
		sql.append(" ,case when IFNULL(amxb,0)=0 and IFNULL(amin,0)+IFNULL(amsb,0)>0  then 1 else 0 end   amearly  ");
		sql.append(" ,case when IFNULL(pmsb,0)>0 and IFNULL(pmxb,0)>0  then 1 else 0 end  pmnormal ");
		sql.append(" ,case when IFNULL(pmsb,0)+IFNULL(pmin,0)+IFNULL(pmsb,0)=0  then 1 else 0 end  pmlost  ");
		sql.append(" ,case when  IFNULL(pmsb,0)=0 and IFNULL(pmin,0)+IFNULL(pmxb,0)>0 then 1 else 0 end   pmlate  ");
		sql.append(" ,case when IFNULL(pmxb,0)=0 and IFNULL(pmin,0)+IFNULL(pmsb,0)>0  then 1 else 0 end   pmearly ");
		sql.append(" from tfw_dept d ");
		sql.append(" left join tfw_user u on u.DEPTID=d.id and u.status=1");
		sql.append(" LEFT JOIN glpt_workday w  on w.type=0 and w.day BETWEEN '#{text(signdate_dategt)}' and '#{text(signdate_datelt)}'");
		sql.append(" left join (");
		sql.append("            select w.day,userid,");
		sql.append("            sum(case when s.id is not null then 1 else 0 end)times,");
		sql.append("            sum(case when DATE_FORMAT(`signtime`,'%H:%i')<='08:30' then 1 else 0 end) amsb,");
		sql.append("            sum(case when DATE_FORMAT(`signtime`,'%H:%i') BETWEEN '08:31' and '11:59' then 1 else 0 end) amin,");
		sql.append("            sum(case when DATE_FORMAT(`signtime`,'%H:%i') BETWEEN '12:00' and '13:29' then 1 else 0 end) amxb,");
		sql.append("            sum(case when DATE_FORMAT(`signtime`,'%H:%i') BETWEEN '13:30' and '15:00' then 1 else 0 end) pmsb,");
		sql.append("            sum(case when DATE_FORMAT(`signtime`,'%H:%i') BETWEEN '15:01' and '17:59' then 1 else 0 end) pmin,");
		sql.append("            sum(case when DATE_FORMAT(`signtime`,'%H:%i')>='18:00' then 1 else 0 end) pmxb");
		sql.append("            from glpt_workday w");
		sql.append("            left join glpt_signin s on w.day=s.signdate");
		sql.append("            left join tfw_dept d on s.area_id=d.ID");
		sql.append("            left join tfw_user u on s.userid=u.ID and u.status=1 and FIND_IN_SET(s.area_id,u.attendareas)>0");
		sql.append("            where  w.type=0 and w.day BETWEEN '#{text(signdate_dategt)}' and '#{text(signdate_datelt)}'");
		sql.append("            group by w.day,userid");
		sql.append("            ) s  on s.userid=u.ID   and w.day=s.day");
		sql.append(" where 1=1   ");
		if(record.containsKey("deptid"))
			sql.append(" and d.id=#{deptid} ");
		if(record.containsKey("userid"))
			sql.append(" and u.id=#{userid} ");
		sql.append(" order by d.NUM,d.FULLNAME ,u.`NAME`,w.day");
		return Db.init().paginate(sql.toString(), record, pageIndex, pageSize);
	}
}
