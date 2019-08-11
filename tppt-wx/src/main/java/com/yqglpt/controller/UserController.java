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
//import com.yqglpt.model.BookRead;
//import com.yqglpt.model.BorrowRecord;
//import com.yqglpt.model.DataDict;
//import com.yqglpt.model.Message;
//import com.yqglpt.model.User;
//import com.yqglpt.model.UserRel;
//import com.yqglpt.utils.Constants;
//import com.yqglpt.utils.DateUtil;
//import com.yqglpt.utils.PageUtil;
//import com.yqglpt.utils.StrUtils;
//
////@Before(LoginInterceptor.class)
//public class UserController extends Controller {
//
//	private static Logger logger = Logger.getLogger(UserController.class);
//
//	/**
//	 * 个人中心
//	 */
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void center() {
//		
//		//查询用户信息
//		User user = User.dao.findLoginUser(getUserId());
//		
//		
//		//查询未读消息
//		//Message message=Message.dao.countMessage(getUserId());
//		//查询借阅记录数
//		BorrowRecord borrowRecord=BorrowRecord.dao.countBorrowRecord(getUserId());
//		
//		setAttr("user", user);
//		//setAttr("message",message);
//		setAttr("borrowRecord",borrowRecord);
//		render("persioncenter.jsp");
//	}
//	
//	
//	/**
//	 * 消息中心
//	 */
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void msgcenter() {
//		
//		//查询用户信息
//		List<Message> msgTypeList=Message.dao.countMessage(getUserId());
//		
//		setAttr("messageTypeList",msgTypeList);
//		render("msgcenter.jsp");
//	}
//
//	/*
//	 * 个人信息
//	 */
//	@Before({ LoginInterceptor.class })
//	public void info() {
//
//		//查询用户信息
//		User user = User.dao.findLoginUser(getUserId());
//
//		setAttr("user",user);
//		render("info.jsp");
//
//	}
//	
//	
//	// 保存图书
//		@Before({ LoginInterceptor.class, Tx.class })
//		public void saveinfo() {
//			User user = getModel(User.class, "user");
//			ResultObj<String> result = new ResultObj<String>();
//			try {
//				String id = user.getStr("id");
//				if (!StringUtils.isEmpty(id) && !id.equals("0")) {
//					user.update();
//				}
//				result = ResultObj.newInstance(id);
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error(e.getMessage());
//				result = ResultObj.newFailInstance("", "系统异常");
//			}
//			renderJson(result);
//
//		}
//		//我的消息
//		@Before({ LoginInterceptor.class, Tx.class })
//		public void msgtypelist() {
//			String str = "";
//			String t = "";
//			int pagenum = 1;
//			if (!isParaBlank("p")) {// 页码
//				pagenum = getParaToInt("p");
//			}
//			if (!isParaBlank("s")) {// 查询
//				str = StrUtils.decode(getPara("s"));
//			}
//			Page<Message> page =Message.dao.queryMessage(pagenum, 20, getUserId(),str,t);
//			setAttr("page", new PageUtil(page));
//			setAttr("s", str);
//			setAttr("p", pagenum);
//			render("msglist.jsp");
//			setAttr("p",pagenum);
//			if(pagenum==1)
//				render("msglist.jsp");
//			else
//				render("msglistpage.jsp");
//
//		}
//
//	//我的消息
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void msglist() {
//		String str = "";
//		String t = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		if (!isParaBlank("s")) {// 查询
//			str = StrUtils.decode(getPara("s"));
//		}
//		if (!isParaBlank("t")) {// 查询
//			t = StrUtils.decode(getPara("t"));
//		}
//		Page<Message> page =Message.dao.queryMessage(pagenum, 20, getUserId(),str,t);
//		setAttr("page", new PageUtil(page));
//		setAttr("s", str);
//		setAttr("t", t);
//		render("msglist.jsp");
//		setAttr("p",pagenum);
//		if(pagenum==1)
//			render("msglist.jsp");
//		else
//			render("msglistpage.jsp");
//
//	}
//	
//	
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void msg() {
//		String id = "";
//		String t = "";
//		if (!isParaBlank("id")) {// 查询
//			id = getPara("id");
//		}
//		if (!isParaBlank("t")) {// 查询
//			t = StrUtils.decode(getPara("t"));
//		}
//		Message message=Message.dao.quertInfo(id,getUserId());
//		setAttr("msg",message);
//		setAttr("t",t);
//		render("msg.jsp");
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void dealmsg() {
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			Message message = getModel(Message.class, "message");
//			Message.dao.dealWidthMsg(message,null,getUserId());
//			result = ResultObj.newInstance(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result = ResultObj.newFailInstance("", "系统异常");
//		}
//		renderJson(result);
//	}
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void myborrow() {
//		String str = "";
//		String t = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		if (!isParaBlank("s")) {// 查询
//			str = StrUtils.decode(getPara("s"));
//		}
//
//		if (!isParaBlank("t")) {// 查询
//			t = StrUtils.decode(getPara("t"));
//		}
//		
//		Page<BorrowRecord> page = BorrowRecord.dao.queryMyBorrowList(pagenum, 20, getUserId(), str,t);
//		setAttr("page", new PageUtil(page));
//		setAttr("s",str);
//		setAttr("p",pagenum);
//		setAttr("t",t);
//		if(pagenum==1)
//			render("myborrow.jsp");
//		else
//			render("myborrowpage.jsp");
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void returnbook() {
//		BorrowRecord borrowRecord = getModel(BorrowRecord.class, "borrowRecord");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			BorrowRecord.dao.returnbook(borrowRecord);
//			result=ResultObj.newInstance(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result=ResultObj.newFailInstance("","系统异常");
//		}
//		renderJson(result);
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void likesbook() {
//		BorrowRecord borrowRecord = getModel(BorrowRecord.class, "borrowRecord");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			int id = borrowRecord.getInt("id");
//			if (id>0) {
//				BorrowRecord.dao.likesBook(String.valueOf(id));
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
//	//阅读排名
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void readorder() {
//		String str = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		/*if (!isParaBlank("s")) {// 查询
//			str = StrUtils.decode(getPara("s"));
//		}*/
//		// 获取所有的工作日志
//
//		//String where = " where a.user='" + getUserId() + "'";
//		//查询用户信息
//		Page<User> page = User.dao.queryReadOrder(pagenum, 10,getUserId());
//
//		setAttr("page", new PageUtil(page));
//		setAttr("p",pagenum);
//		render("readorder.jsp");
//	}
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void readlist() {
//		String str = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		String id = "";
//		if (!isParaBlank("id")) {// 查询
//			id = getPara("id");
//		}
//		/*if (!isParaBlank("s")) {// 查询
//			str = StrUtils.decode(getPara("s"));
//		}*/
//		// 获取所有的工作日志
//
//		//String where = " where a.user='" + getUserId() + "'";
//		//查询用户信息
//		Page<BookRead> page = BookRead.dao.queryReadList(pagenum, 10, id);
//
//		setAttr("page", new PageUtil(page));
//		setAttr("p",pagenum);
//		render("readlist.jsp");
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void bookmsg() {
//		String str = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		/*if (!isParaBlank("s")) {// 查询
//			str = StrUtils.decode(getPara("s"));
//		}*/
//		// 获取所有的工作日志
//
//		//String where = " where a.user='" + getUserId() + "'";
//		//查询用户信息
//		Page<Message> page = Message.dao.queryNewBookMessage(pagenum, 10, getUserId());
//
//		setAttr("page", new PageUtil(page));
//		setAttr("p",pagenum);
//		render("bookmsg.jsp");
//	}
//	
//	
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void bookmsgdetail() {
//
//		String id = "";
//		if (!isParaBlank("id")) {// 查询
//			id = getPara("id");
//		}
//		Book book=Book.dao.findBookById(id,getUserId());
//		UserRel userRel=UserRel.dao.checkUserRel(id,getUserId());
//		setAttr("book", book);
//		setAttr("userRel", userRel);
//		render("bookmsgdetail.jsp");
//	}
//	
//	
//	
//
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void bookcountorder() {
//		String str = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		/*if (!isParaBlank("s")) {// 查询
//			str = StrUtils.decode(getPara("s"));
//		}*/
//		// 获取所有的工作日志
//
//		//String where = " where a.user='" + getUserId() + "'";
//		//查询用户信息
//		Page<User> page = User.dao.queryBookCountOrder(pagenum, 20,getUserId());
//
//		setAttr("page", new PageUtil(page));
//		setAttr("p",pagenum);
//		if(pagenum==1)
//			render("bookcountorder.jsp");
//		else
//			render("bookcountorderpage.jsp");
//	}
//	
//	
//
//
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void bookorder() {
//		String str = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		String id = "";
//		if (!isParaBlank("id")) {// 查询
//			id = getPara("id");
//		}
//		Page<Book> page = Book.dao.bookOrderList(pagenum, 20,getUserId());
//
//		setAttr("page", new PageUtil(page));
//		setAttr("p",pagenum);
//		setAttr("userId", getUserId());
//		if(pagenum==1)
//			render("bookorder.jsp");
//		else
//			render("bookorderpage.jsp");
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
//	private User getUser() {
//		return getSessionAttr("honzh_user");
//	}
//
//	private String getUserId() {
//		return getUser().getStr("id");
//	}
//
//	private String getMonth() {
//		return DateUtil.getWeekMonth();
//	}
//}
