package com.yqglpt.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class Signin extends Model<Signin>{

	public static final Signin dao = new Signin();
	
	/**
	 * 查询签到记录
	 * @param userid
	 * @param date
	 * @return
	 */
	public List<Signin> querySiginList(String userid,String date){
		String sql="select * from glpt_signin where signdate=? and userid=? order by id ";
		return dao.find(sql, new Object[]{date,userid});
	}
	
}

