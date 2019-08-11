package com.yqglpt.model;

import com.jfinal.plugin.activerecord.Model;

public class Notice extends Model<Notice>{

	public static final Notice dao = new Notice();
	
	public Notice findById(Integer id) {
		return findFirst(
				"select a.F_IT_XL as id,F_IT_LX as type,F_VC_BT as title,F_DT_FBSJ date,F_TX_NR as content,d.name "
				+ " from tb_tfw_tzgg a  left join tfw_dict d on a.F_IT_LX=d.NUM and d.`CODE`=102 where a.F_IT_XL=? ", id);
	}
}
