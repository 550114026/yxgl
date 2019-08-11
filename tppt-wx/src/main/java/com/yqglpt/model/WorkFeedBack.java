package com.yqglpt.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class WorkFeedBack extends Model<WorkFeedBack>{

	public static final WorkFeedBack dao = new WorkFeedBack();
	
	/**
	 * 根据用户ID和工作ID查找记录
	 * @param workid
	 * @param userid
	 * @return
	 */
	public WorkFeedBack findByWorkId(Integer workid,Integer userid){
		String sql="select * from glpt_work_feedback where workid=? and userid=? order by id ";
		return dao.findFirst(sql, new Object[]{workid,userid});
	}
	
	/**
	 * 设置为已读
	 * @param workid
	 * @param userid
	 */
	public void readWorkFeedBack(Integer workid,Integer userid){
		String sql="update glpt_work_feedback set status=1,readtime=now() where workid=? and userid=?";
		Db.update(sql, new Object[]{workid,userid});
	}
	
	/**
	 * 任务反馈
	 * @param workFeedBack
	 */
	public void feedBack(WorkFeedBack workFeedBack){

		String sql="update glpt_work_feedback "
				+ "set status=2, "
				+ "isfinish=1, "
				+ "feekbacktime=now(), "
				+ "feedcontent=? "
				+ "where workid=? "
				+ "and userid=?";
		Db.update(sql, new Object[]{workFeedBack.getStr("feedcontent"),workFeedBack.getLong("workid"),workFeedBack.getInt("userid")});
	}
}

