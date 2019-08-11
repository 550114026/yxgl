package com.yqglpt.controller;//package com.yqglpt.controller;
//
//import org.springframework.util.StringUtils;
//
//import com.jfinal.aop.Before;
//import com.jfinal.plugin.activerecord.Page;
//import com.yqglpt.common.BaseController;
//import com.yqglpt.interceptor.LoginInterceptor;
//import com.yqglpt.model.Notice;
//import com.yqglpt.utils.PageUtil;
//
//@Before(LoginInterceptor.class)
//public class NoticeController extends BaseController{
//
//	public void index() {
//		int pagenum = 1;
//		String str = getSearchText();
//		if (!isParaBlank("p")) {// 页码
//			pagenum = getParaToInt("p");
//		}
//		// 获取所有的工作日志
//		String where = " where (FIND_IN_SET('"+getDeptId()+"',a.deptid) or a.deptid is null or a.deptid='')";
//
//		if (!StringUtils.isEmpty(str))
//			where += " and F_VC_BT like '%" + str + "%'";
//
//		Page<Notice> page = Notice.dao.paginate(pagenum, 20,
//				"select a.F_IT_XL as id,F_IT_LX as type,F_VC_BT as title,F_DT_FBSJ date,d.name ",
//				" from tb_tfw_tzgg a  left join tfw_dict d on a.F_IT_LX=d.NUM and d.`CODE`=102 " + where + " order by F_IT_XL desc");
//		setAttr("page", new PageUtil(page));
//		setAttr("s", str);
//		setAttr("p", pagenum);
//		if(pagenum==1)
//			render("notice.html");
//		else
//			render("noticepage.html");
//	}
//
//
//
//	public void view(){
//		Integer id=getParaToInt("id");
//		Notice notice=Notice.dao.findById(id);
//		setAttr("notice", notice);
//		render("noticeview.html");
//	}
//}
