package com.yqglpt.model;



import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class User extends Model<User> {

	public static final User dao = new User();

	public User findLoginUser(String openid) {
		return findFirst("select * from tfw_user where openid=? ", openid);
	}
	

	public User findUserByAccount(String account) {
		return findFirst("select * from tfw_user where account=? ", account);
	}
	
	public Integer bindUser(Integer ID,String openid,String nickname,String headimgurl) {
			String sql="update tfw_user "
					+ "set openid=?,"
					+ "nickname=?,"
					+ "headimgurl=?"
					+ " where ID=?";
			return Db.update(sql, new Object[]{openid,nickname,headimgurl,ID});
	}
	
}
