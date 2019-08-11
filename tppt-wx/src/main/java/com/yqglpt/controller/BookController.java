package com.yqglpt.controller;//package com.yqglpt.controller;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.text.MessageFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.springframework.util.StringUtils;
//
//import com.jfinal.aop.Before;
//import com.jfinal.core.Controller;
//import com.jfinal.plugin.activerecord.Page;
//import com.jfinal.plugin.activerecord.tx.Tx;
//import com.jfinal.weixin.sdk.api.JsTicketApi;
//import com.jfinal.weixin.sdk.api.JsTicketApi.JsApiType;
//import com.yqglpt.common.ResultObj;
//import com.yqglpt.interceptor.LoginInterceptor;
//import com.yqglpt.model.Book;
//import com.yqglpt.model.BorrowRecord;
//import com.yqglpt.model.DataDict;
//import com.yqglpt.model.Message;
//import com.yqglpt.model.User;
//import com.yqglpt.utils.Constants;
//import com.yqglpt.utils.DateUtil;
//import com.yqglpt.utils.PageUtil;
//import com.yqglpt.utils.SHA1;
//import com.yqglpt.utils.StrUtils;
//
//@Before(LoginInterceptor.class)
//public class BookController extends Controller {
//
//	private static Logger logger = Logger.getLogger(BookController.class);
//
//	@Before(LoginInterceptor.class)
//	public void mybook() throws UnsupportedEncodingException {
//		String type = "1";
//		String str = "";
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		if (!isParaBlank("t")) {// 类型
//			type = getPara("t");
//		}
//		if (!isParaBlank("s")) {// 查询
//			str=getPara("s");
//			//str = StrUtils.decode(getPara("s"));//new String(getPara("s").getBytes("ISO-8859-1"),"UTF-8");
//		}
//		// 获取所有的工作日志
//		String where = " where status=1 and  user='" + getUserId() + "'";
//		if (!StringUtils.isEmpty(type)) {
//			if (type.equals("s"))
//				where += " and big_type not in(1,2,3)";
//			else
//				where += " and big_type='" + type + "'";
//
//		}
//
//		if (!StringUtils.isEmpty(str))
//			where += " and book_name like '%" + str + "%'";
//
//		Page<Book> page = Book.dao.paginate(pagenum, 20, "select * ",
//				" from bh_book " + where + " order by add_time desc");
//		setAttr("page", new PageUtil(page));
//		setAttr("t", type);
//		setAttr("s", str);
//		setAttr("p", pagenum);
//
//		List<DataDict> borrowStatus = DataDict.dao
//				.queryGroupCode("borrow_status");
//		setAttr("borrowStatus", borrowStatus);
//		if(pagenum==1)
//			render("bookmanage.jsp");
//		else
//			render("bookmanagepage.jsp");
//		// NoteWeek note = getModel(NoteWeek.class, "noteweek");
//		
//	}
//
//	@Before({ LoginInterceptor.class })
//	public void updatebook() {
//		String id = "";
//		Book book = new Book();
//		if (!isParaBlank("id")) {// 书ID
//			id = getPara("id");
//			book = Book.dao.findBookById(id, getUserId(), getUserId());
//		} else
//			book.set("id", "0");
//		setAttr("book", book);
//
//		List<DataDict> bigType = DataDict.dao.queryGroupCode("big_type");
//		setAttr("bigType", bigType);
//
//		List<DataDict> borrowStatus = DataDict.dao
//				.queryGroupCode("borrow_status");
//		setAttr("borrowStatus", borrowStatus);
//
//		List<DataDict> read_status = DataDict.dao
//				.queryGroupCode("read_status");
//		setAttr("read_status", read_status);
//		
////		String noncestr="";
////		String timestamp="";
////		String url="";
////		
////		
////		setAttr("JsTicket", JsTicketApi.getTicket(JsApiType.jsapi));
//		
//
//		render("addbook.jsp");
//
//	}
//	
//	/// <summary>
//	/// 微信权限签名( sha1 算法 )
//	/// 签名用的noncestr和timestamp必须与wx.config中的nonceStr和timestamp相同
//	/// </summary>
//	/// <param name="AppId">第三方用户唯一凭证</param>
//	/// /// <param name="AppSecret">第三方用户唯一凭证密钥，即appsecret</param>
//	/// <param name="noncestr">生成签名的随机串</param>
//	/// <param name="timestamp">生成签名的时间戳</param>
//	/// <param name="url">签名用的url必须是调用JS接口页面的完整URL</param>
//	/// <returns></returns>
////	public String  signatureOut( String noncestr, String timestamp, String url)
////	{
////	
////		String jsapi_ticket = JsTicketApi.getTicket(JsApiType.jsapi).getTicket();
////	
////		String strSha1 = MessageFormat.format("jsapi_ticket={0}&noncestr={1}&timestamp={2}&url={3}", jsapi_ticket, noncestr, timestamp, url);
////		return new SHA1().getDigestOfString(strSha1.getBytes()); 
////	}
//
//	// 保存图书
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void savebook() {
//		Book book = getModel(Book.class, "book");
//		ResultObj<String> result = new ResultObj<String>();
//		try {
//			String id = book.getStr("id");
//			if (!StringUtils.isEmpty(id) && !id.equals("0")) {
//				book.update();
//				//更新阅读记录
//				Book.dao.updateReadRecord(book.getStr("id"),getUserId(),getUserId());
//				
//			} else {
//				book.set("id", Constants.getNewId("book"));
//				book.set("user", getUserId());
//				book.save();
//				id = book.getStr("id");
//				//发送好友图书上线消息
//				Message.dao.sendNewBookMessage(id);
//				
//			}
//			result = ResultObj.newInstance(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result = ResultObj.newFailInstance("", "系统异常");
//		}
//		renderJson(result);
//
//	}
//
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void setbook() {
//		Book book = getModel(Book.class, "book");
//		ResultObj<String> result = new ResultObj<String>();
//		try {
//			String id = book.getStr("id");
//			if (!StringUtils.isEmpty(id) && !id.equals("0")) {
//				Book.dao.setBook(book);
//				result = ResultObj.newInstance(id);
//			} 
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result = ResultObj.newFailInstance("", "系统异常");
//		}
//		renderJson(result);
//
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void deletbook() {
//		Book book = getModel(Book.class, "book");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			String id = book.getStr("id");
//			if (!StringUtils.isEmpty(id) && !id.equals("0")) {
//				Book bookToSave=new Book();
//				bookToSave.set("id", id);
//				bookToSave.set("status", 0);
//				Book.dao.setBook(bookToSave);
//				result = ResultObj.newInstance(true);
//			} 
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result = ResultObj.newFailInstance("", "系统异常");
//		}
//		renderJson(result);
//
//	}
//
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void bookdetail() {
//
//		String id = "";
//		Book book = new Book();
//		if (!isParaBlank("id")) {// 书ID
//			id = getPara("id");
//			book = Book.dao.findBookById(id,getUserId());
//		}
//		setAttr("book", book);
//		render("bookdetail.jsp");
//	}
//
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void borrowmanage() {
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
//
//		String where = " where a.borrow_status=1 and a.user='" + getUserId() + "'";
//		if(!StringUtils.isEmpty(t))
//			where += " and b.return_status="+t;
//		if (!StringUtils.isEmpty(str))
//			where += " and a.book_name like '%" + str + "%'";
//
//		Page<Book> page = Book.dao
//				.paginate(
//						pagenum,
//						20,
//						"select a.*,b.id as bid,b.borrow_time,b.expected_return_time,c.id uid,c.nick_name,b.return_status as breturn_status,"
//						+ "(select 1 from bh_message where busi_type = 23 AND busi_id = b.id and TO_DAYS(NOW())=TO_DAYS(send_time)) ycb",
//						" from bh_book  a "
//								+ "  join ( "
//								+ " select t.* from bh_borrow_record t "
//								+ " where  not exists(select 1 from bh_borrow_record u where u.book_id=t.book_id and u.id>t.id) "
//								+ " ) b on a.id=b.book_id "
//								+ " left join bh_user c on b.user=c.id "
//								+ where 
//								+ " order by b.return_status,if(b.return_status=1,b.add_time,b.expected_return_time) ");
//		setAttr("page", new PageUtil(page));
//		setAttr("s", str);
//		setAttr("p", pagenum);
//		setAttr("t", t);
//		if(pagenum==1)
//			render("borrowmanage.jsp");
//		else
//			render("borrowmanagepage.jsp");
//	}
//
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void borrowdetail() {
//		int pagenum = 1;
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		String id = "";
//		Book book = new Book();
//		if (!isParaBlank("id")) {// 书ID
//			id = getPara("id");
//			book = Book.dao.findBookById(id,getUserId());
//		}
//		setAttr("book", book);
//		Page<BorrowRecord> page = BorrowRecord.dao.queryByBookId(pagenum, 10,id,getUserId());
//		setAttr("page", new PageUtil(page));
//		setAttr("p", pagenum);
//		render("borrowdetail.jsp");
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void returnbook() {
//		BorrowRecord borrowRecord=getModel(BorrowRecord.class, "borrowRecord");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			Integer id = borrowRecord.get("id");
//			if (id!=null && !id.equals("0")) {
//				BorrowRecord borrowRecordToSave=new BorrowRecord();
//				borrowRecordToSave.set("id", id);
//				borrowRecordToSave.set("real_return_time", "now()");
//				borrowRecordToSave.set("return_status", 1);
//				borrowRecordToSave.set("user", getUserId());
//				BorrowRecord.dao.returnbook(borrowRecordToSave);
//				result = ResultObj.newInstance(true);
//			} 
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result = ResultObj.newFailInstance("", "系统异常");
//		}
//		renderJson(result);
//
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void callbook() {
//		BorrowRecord borrowRecord=getModel(BorrowRecord.class, "borrowRecord");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			Integer id = borrowRecord.get("id");
//			if (id!=null && !id.equals("0")) {
//				Message.dao.sendCallBookMsg(id.toString(),getUserId());
//				result = ResultObj.newInstance(true);
//			} 
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result = ResultObj.newFailInstance("", "系统异常");
//		}
//		renderJson(result);
//
//	}
//	
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void borrowbookview() {
//		String bid = "0";//bookId
//		String mid="0";//消息ID
//		if (!isParaBlank("bid")) {
//			bid = getPara("bid");
//			setAttr("book",Book.dao.findBookById(bid,getUserId()));
//		}
//		setAttr("bid", bid);
//		if (!isParaBlank("mid")) {
//			mid = getPara("mid");
//			setAttr("mid", mid);
//			setAttr("book",Book.dao.findBookByMsgId(mid));
//		}
//		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
//		setAttr("today",sFormat.format(new Date()));
//		render("borrowbook.jsp");
//	}
//	
//	@Before({ LoginInterceptor.class, Tx.class })
//	public void borrowbook() {
//		Message message=getModel(Message.class, "message");
//		//=getModel(BorrowRecord.class, "borrowRecord");
//		ResultObj<Boolean> result = new ResultObj<Boolean>();
//		try {
//			Integer mid = message.get("id");//消息ID
//			String busi_id=message.getStr("busi_id");//图书ID
//			message.set("send_user", getUserId());
//			
//			BorrowRecord borrowRecord=new BorrowRecord();
//			borrowRecord.set("borrow_time", getPara("borrowRecord.borrow_time"));
//			borrowRecord.set("expected_return_time",getPara("borrowRecord.borrow_time"));
//			borrowRecord.set("user", getUserId());
//			if (mid!=null &&!mid.equals(0)) {//处理消息
//				//从新书上线提醒消息中获取图书ID
//				Message oldMsg=Message.dao.findById(mid);
//				//插入借阅记录
//				borrowRecord.set("book_id", oldMsg.getStr("busi_id"));
//				borrowRecord.save();
//				//借阅申请记录中，业务ID为借阅记录ID
//				message.set("busi_id", borrowRecord.getInt("id").toString());
//				Message.dao.dealWidthMsg(message,borrowRecord,getUserId());
//			} 
//			else if(!StringUtils.isEmpty(busi_id)){//直接借书
//
//				//插入借阅记录
//				borrowRecord.set("book_id", busi_id);
//				borrowRecord.save();
//				
//
//				//借阅申请记录中，业务ID为借阅记录ID
//				message.set("busi_id", borrowRecord.getInt("id").toString());
//				Message.dao.sendBorrowMsg(message,borrowRecord,getUserId());
//			}
//
//			result = ResultObj.newInstance(true);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			result = ResultObj.newFailInstance("", "系统异常");
//		}
//		renderJson(result);
//
//	}
//	
//
//	private User getUser() {
//		return getSessionAttr("honzh_user");
//	}
//
//	private String getUserId() {
//		return getUser().getStr("id");
//	}
//	private String getNickName() {
//		return getUser().getStr("nick_name");
//	}
//
//	private String getMonth() {
//		return DateUtil.getWeekMonth();
//	}
//}
