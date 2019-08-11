package com.yqglpt.controller;
import java.util.Date;

import org.apache.log4j.Logger;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.yqglpt.common.BaseController;
import com.yqglpt.common.ResultObj;
import com.yqglpt.interceptor.LoginInterceptor;
import com.yqglpt.model.Work;
import com.yqglpt.model.WorkFeedBack;
import com.yqglpt.utils.PageUtil;

@Before(LoginInterceptor.class)
public class WorkController extends BaseController{

	private static Logger logger = Logger.getLogger(WorkController.class);
	public void index() {
		int pagenum = 1;
		String str = getSearchText();
		if (!isParaBlank("p")) {// 页码
			pagenum = getParaToInt("p");
		}
		/**
		 * 反馈状态
		 */
		Integer status = getParaToInt("status");

		Page<Work> page = Work.dao.queryPage(getUserId(),pagenum,str,status );
		setAttr("page", new PageUtil(page));
		setAttr("s", str);
		setAttr("p", pagenum);
		if(pagenum==1)
			render("work.html");
		else
			render("workpage.html");
	}
	
	
	/**
	 * 查看工作任务
	 */
//	public void view(){
//		Integer id=getParaToInt("id");
//		Work work=Work.dao.findById(id);
//		setAttr("work", work);
//		render("workview.html");
//	}
	
	
	/**
	 * 打开反馈页面
	 * 根据状态，展示查看、反馈等操作内容
	 */
	public void feedback(){
		
		//任务信息
		Integer id=getParaToInt("id");
		Work work=Work.dao.findById(id);
		setAttr("work", work);
		//反馈信息
		WorkFeedBack workFeedBack=WorkFeedBack.dao.findByWorkId(id,Integer.valueOf(getUserId()) );
		//第一次打开，设置接收时间
		if(workFeedBack.getInt("status")==0){
			workFeedBack.dao.readWorkFeedBack(id,Integer.valueOf(getUserId()));
			workFeedBack.set("readtime", new Date());
			workFeedBack.set("status", 1);
		}
		
		setAttr("workFeedBack", workFeedBack);
		render("workfeed.html");
	}
	
	
	/**
	 * 保存反馈
	 */
	public void save(){

		ResultObj<String> result = ResultObj.newInstance();
		WorkFeedBack workFeedBack = getModel(WorkFeedBack.class, "feedBack");

		try {
			if(workFeedBack!=null){
				workFeedBack.set("userid", Integer.valueOf(getUserId()));
				workFeedBack.set("username", getNickName());
				WorkFeedBack.dao.feedBack(workFeedBack);
			}else{
				result = ResultObj.newFailInstance("", "参数异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			result = ResultObj.newFailInstance("", "系统异常");
		}
		renderJson(result);
	}
	
	/**
	 * 查看反馈
	 */
	public void viewfeedback(){
		Integer workid=getParaToInt("workid");
		WorkFeedBack workFeedBack =WorkFeedBack.dao.findByWorkId(workid,  Integer.valueOf(getUserId()));
		setAttr("workFeedBack", workFeedBack);
		render("workfeedview.html");
	}
	
}
