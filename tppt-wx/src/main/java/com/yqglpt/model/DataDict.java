package com.yqglpt.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;


@SuppressWarnings("serial")
public class DataDict extends Model<DataDict> {

	public static final DataDict dao = new DataDict();
	
	public List<DataDict> queryGroupCode(String groupCode){
		return find("select * from bh_data_dict where group_code='"+groupCode+"' order by order_seq");
	}
}