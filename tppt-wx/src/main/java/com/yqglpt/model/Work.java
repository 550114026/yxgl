package com.yqglpt.model;

import org.springframework.util.StringUtils;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Work extends Model<Work>{

	public static final Work dao = new Work();
	
	/**
	 * 
	 * @param userId 用户ID
	 * @param pagenum 页码
	 * @param searchText 查询关键字
	 * @param status 状态 0 未读，1 已读，2 已反馈
	 * @return
	 */
	public Page<Work> queryPage(String userId,Integer pagenum,String searchText,Integer status){
		
		String select="select w.id,w.title,w.publishtime,w.plantdate,"
					+" (select count(0) from glpt_work_feedback wf where wf.status=2 and wf.workid=w.id and wf.userid="+userId+") as hasFeedBack,"
					+" (select count(0) from glpt_work_feedback wf where wf.status=1 and wf.workid=w.id and wf.userid="+userId+") as hasReed";
		
		String from=" from glpt_work w ";
		// 获取所有的工作日志
		String where = " where status=2 and FIND_IN_SET('"+userId+"',w.reciveuserids)>0 ";
		
		if (!StringUtils.isEmpty(searchText))
			where += " and w.title like '%" + searchText + "%'";
		if(status!=null&&status>=0){
			where +=" and EXISTS(select 1 from glpt_work_feedback wf where wf.status="+status+" and wf.workid=w.id and wf.userid="+userId+")";
		}
		
		String order=" order by (select max(status) from glpt_work_feedback wf where wf.workid=w.id and wf.userid="+userId+"),w.publishtime DESC";

		Page<Work> page = paginate(pagenum, 20, select,from+where+order);
		return page;
	}
	
}
