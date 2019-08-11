package com.yqglpt.controller;//package com.yqglpt.controller;
//
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.alibaba.druid.util.StringUtils;
//import com.jfinal.aop.Before;
//import com.jfinal.core.Controller;
//import com.jfinal.plugin.activerecord.Page;
//import com.jfinal.plugin.activerecord.tx.Tx;
//import com.yqglpt.common.ResultObj;
//import com.yqglpt.interceptor.LoginInterceptor;
//import com.yqglpt.model.Book;
//import com.yqglpt.model.BorrowRecord;
//import com.yqglpt.model.DataDict;
//import com.yqglpt.model.Group;
//import com.yqglpt.model.Message;
//import com.yqglpt.model.Note;
//import com.yqglpt.model.NoteWeek;
//import com.yqglpt.model.User;
//import com.yqglpt.model.UserRel;
//import com.yqglpt.utils.PageUtil;
//import com.yqglpt.utils.StrUtils;
//
////@Before(LoginInterceptor.class)
//public class FriendController extends Controller {
//
//	private static Logger logger = Logger.getLogger(FriendController.class);
//
//	/**
//	 * 个人中心
//	 */
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void friendlist() {
//		String str = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		if (!isParaBlank("s")) {// 查询
//			str = StrUtils.decode(getPara("s"));
//		}
//		setAttr("p", pagenum);
//		setAttr("s", str);
//		Page<User> page = User.dao.queryUserFriends(pagenum, 200, getUserId(),str);
//		
//		setAttr("page", new PageUtil(page));
//		List<Group> list=Group.dao.queryGroup(getUserId());
//		setAttr("list", list);
//		render("friendlist.jsp");
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void setfriendnickname() {
//		UserRel uerRel = getModel(UserRel.class, "uerRel");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			String nick_name = uerRel.getStr("nick_name");
//			if (!StringUtils.isEmpty(nick_name) ) {
//				uerRel.set("main_user", getUserId());
//				UserRel.dao.setNickName(uerRel);
//				result = ResultObj.newInstance(true);
//			} 
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result = ResultObj.newFailInstance("", "系统异常");
//		}
//		renderJson(result);
//	}
//	
//	
//
//	
//
//	
//	/*
//	 * 好友藏书
//	 */
//	@Before({ LoginInterceptor.class })
//	public void friendbook() {
//		String id = "";
//		int pagenum=1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		if (!isParaBlank("id")) {// 书ID
//			id = getPara("id");
//			Page<Book> page  = Book.dao.queryUserBooks(pagenum,20,id,getUserId());
//			setAttr("page", new PageUtil(page));
//		}
//
//		setAttr("p", pagenum);
//		setAttr("userId", getUserId());
//		
//		if(pagenum==1)
//			render("friendbooklist.jsp");
//		else
//			render("friendbooklistpage.jsp");
//
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void likesbook() {
//		Book book = getModel(Book.class, "book");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			String id = book.getStr("id");
//			if (!StringUtils.isEmpty(id)) {
//				Book.dao.likesBook(id,getUserId());
//			}
//			result=ResultObj.newInstance(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result=ResultObj.newFailInstance("","系统异常");
//		}
//		renderJson(result);
//	}
//	
//	
//	public void qrcode() {
//		
//		render("qrcode.jsp");
//
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void addfriend() {
//		//查询用户信息
//		User user = User.dao.findLoginUser(getUserId());
//		setAttr("user",user);
//		render("addfriend.jsp");
//	}
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void userlist() {
//		String str = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		if (!isParaBlank("s")) {// 查询
//			str = StrUtils.decode(getPara("s"));
//		}
//		// 获取所有的工作日志
//		Page<User> page=User.dao.queryUserList(pagenum, 20, getUserId(), str);
//		setAttr("page", new PageUtil(page));
//		setAttr("s",str);
//		setAttr("p",pagenum);
//
//		if(pagenum==1)
//			render("userlist.jsp");
//		else
//			render("userlistpage.jsp");
//	}
//
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void sendAddfriend() {
//		Message message = getModel(Message.class, "message");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			message.set("message_type", 1);
//			message.set("busi_type", "11");
//			message.set("recive_user", message.getStr("busi_id"));
//			message.set("title", getNickName()+"向您发起好友申请");
//			message.set("content", getNickName()+"请求加您为站内好友");
//			message.set("send_user", getUserId());
//			message.dao.sendAddfriendMsg(message);
//			result=ResultObj.newInstance(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result=ResultObj.newFailInstance("","系统异常");
//		}
//		renderJson(result);
//	}
//
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void addgroup() {
//		String ids = "";
//		if (!isParaBlank("ids")) {// 查询
//			ids = StrUtils.decode(getPara("ids"));
//		}
//		// 获取所有的工作日志
//		List<User> list=User.dao.queryUserByIds(ids);
//		setAttr("list", list);
//		setAttr("ids", ids);
//		setAttr("optype", "add");
//		render("addgroup.jsp");
//	}
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void movetogroup() {
//		String ids = "";
//		if (!isParaBlank("ids")) {// 查询
//			ids = StrUtils.decode(getPara("ids"));
//		}
//		// 获取所有的工作日志
//		List<User> list=User.dao.queryUserByIds(ids);
//		setAttr("list", list);
//		setAttr("ids", ids);
//		setAttr("optype", "move");
//		
//
//		List<Group> groups=Group.dao.queryGroup(getUserId());
//		setAttr("groups", groups);
//		
//		render("addgroup.jsp");
//	}
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void savegroup() {
//		Group group = getModel(Group.class, "group");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//
//			String ids ="";
//			if (!isParaBlank("ids")) {// 查询
//				ids = StrUtils.decode(getPara("ids"));
//			}
//			//移动到组
//			if(group.getInt("id")!=null){
//				Group.dao.moveToGroup(ids, getUserId(), group);
//			}
//			//新增组
//			else if(!StringUtils.isEmpty(group.getStr("group_name"))){
//				group.set("user", getUserId());
//				group.save();
//				Group.dao.moveToGroup(ids, getUserId(), group);
//			}
//			result=ResultObj.newInstance(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result=ResultObj.newFailInstance("","系统异常");
//		}
//		renderJson(result);
//	}
//	
//
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void deletegroup() {
//		Group group = getModel(Group.class, "group");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			//移动到组
//			if(group.getInt("id")>0){
//				Group.dao.delete(getUserId(), group.getInt("id"));
//			}
//			result=ResultObj.newInstance(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result=ResultObj.newFailInstance("","系统异常");
//		}
//		renderJson(result);
//	}
//	
//	
//	
//	
//	
//
//	private User getUser() {
//		return getSessionAttr("honzh_user");
//	}
//
//	private String getNickName() {
//		return getUser().getStr("nick_name");
//	}
//	private String getUserId() {
//		return getUser().getStr("id");
//	}
//
//}
