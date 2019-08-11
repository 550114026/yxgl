package com.ikkong.core.base;

import java.util.Date;
import java.util.Map;

import com.ikkong.common.vo.User;
import com.ikkong.core.constant.Const;
import com.ikkong.core.constant.ConstCache;
import com.ikkong.core.constant.ConstCurd;
import com.ikkong.core.constant.ConstShiro;
import com.ikkong.core.constant.Cst;
import com.ikkong.core.interfaces.IQuery;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.model.AjaxResult;
import com.ikkong.core.modules.support.BeanInjector;
import com.ikkong.core.toolbox.Record;
import com.ikkong.core.toolbox.grid.GridManager;
import com.ikkong.core.toolbox.kit.BeanKit;
import com.jfinal.core.ActionException;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class BaseController extends Controller implements Const, ConstShiro, ConstCurd, ConstCache {

	public boolean isAjax() {
		String header = getRequest().getHeader("X-Requested-With");
		boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(header);
		return isAjax;
	}

	/**
	 * 返回ajaxresult
	 * 
	 * @param data
	 * @return AjaxResult
	 */
	public AjaxResult json(Object data) {
		return new AjaxResult().success(data);
	}

	/**
	 * 返回ajaxresult
	 * 
	 * @param data
	 * @param message
	 * @return AjaxResult
	 */
	public AjaxResult json(Object data, String message) {
		return json(data).setMessage(message);
	}

	/**
	 * 返回ajaxresult
	 * 
	 * @param data
	 * @param message
	 * @param code
	 * @return AjaxResult
	 */
	public AjaxResult json(Object data, String message, int code) {
		return json(data, message).setCode(code);
	}

	/**
	 * 返回ajaxresult
	 * 
	 * @param message
	 * @return AjaxResult
	 */
	public AjaxResult success(String message) {
		return new AjaxResult().addSuccess(message);
	}

	/**
	 * 返回ajaxresult
	 * 
	 * @param message
	 * @return AjaxResult
	 */
	public AjaxResult error(String message) {
		return new AjaxResult().addError(message);
	}

	/**
	 * 返回ajaxresult
	 * 
	 * @param message
	 * @return AjaxResult
	 */
	public AjaxResult warn(String message) {
		return new AjaxResult().addWarn(message);
	}

	/**
	 * 返回ajaxresult
	 * 
	 * @param message
	 * @return AjaxResult
	 */
	public AjaxResult fail(String message) {
		return new AjaxResult().addFail(message);
	}

	/**
	 * ============================ mapping
	 * =================================================
	 */

	/**
	 * 表单值映射为javabean
	 * 
	 * @param beanClass
	 *            javabean.class
	 * @return T
	 */
	public <T> T mapping(Class<T> beanClass) {
		return paraInject(beanClass);
	}

	/**
	 * 表单值映射为javabean
	 * 
	 * @param switchMap
	 *            字段混淆Map map.put("前端字段","数据库字段");
	 * @param beanClass
	 *            javabean.class
	 * @return T
	 */
	public <T> T mapping(Map<String, Object> switchMap, Class<T> beanClass) {
		return paraInject(beanClass, switchMap);
	}

	/**
	 * 表单值映射为javabean
	 * 
	 * @param paraPerfix
	 *            name前缀
	 * @param beanClass
	 *            javabean.class
	 * @return T
	 */
	public <T> T mapping(String paraPerfix, Class<T> beanClass) {
		return paraInject(beanClass, paraPerfix);
	}

	/**
	 * 表单值映射为javabean
	 * 
	 * @param paraPerfix
	 *            name前缀
	 * @param switchMap
	 *            字段混淆Map map.put("前端字段","数据库字段");
	 * @param beanClass
	 *            javabean.class
	 * @return T
	 */
	public <T> T mapping(String paraPerfix, Map<String, Object> switchMap, Class<T> beanClass) {
		return paraInject(beanClass, switchMap, paraPerfix);
	}

	/**
	 * 表单值映射为Maps
	 * 
	 * @return Maps
	 */
	public Record getMaps() {
		return paraMapsInject();
	}

	/**
	 * 表单值映射为Maps
	 * 
	 * @param switchMap
	 *            字段混淆Map map.put("前端字段","数据库字段");
	 * @return Maps
	 */
	public Record getMaps(Map<String, Object> switchMap) {
		return paraMapsInject(switchMap);
	}

	/**
	 * 表单值映射为Maps
	 * 
	 * @param paraPerfix
	 *            name前缀
	 * @return Maps
	 */
	public Record getMaps(String paraPerfix) {
		return paraMapsInject(paraPerfix);
	}

	/**
	 * 表单值映射为Maps
	 * 
	 * @param paraPerfix
	 *            name前缀
	 * @param switchMap
	 *            字段混淆Map map.put("前端字段","数据库字段");
	 * @return Maps
	 */
	public Record getMaps(String paraPerfix, Map<String, Object> switchMap) {
		return paraMapsInject(switchMap, paraPerfix);
	}

	private <T> T paraInject(Class<T> beanClass) {
		return (T) BeanInjector.inject(beanClass, getRequest(), false);
	}

	private <T> T paraInject(Class<T> beanClass, Map<String, Object> switchMap) {
		return (T) BeanInjector.inject(beanClass, switchMap, getRequest(), false);
	}

	private <T> T paraInject(Class<T> beanClass, String paraPerfix) {
		return (T) BeanInjector.inject(beanClass, paraPerfix, getRequest(), false);
	}

	private <T> T paraInject(Class<T> beanClass, Map<String, Object> switchMap, String paraPerfix) {
		return (T) BeanInjector.inject(beanClass, switchMap, paraPerfix, getRequest(), false);
	}

	private Record paraMapsInject() {
		return BeanInjector.injectMaps(getRequest(), false);
	}

	private Record paraMapsInject(Map<String, Object> switchMap) {
		return BeanInjector.injectMaps(switchMap, getRequest(), false);
	}

	private Record paraMapsInject(String paraPerfix) {
		return BeanInjector.injectMaps(paraPerfix, getRequest(), false);
	}

	private Record paraMapsInject(Map<String, Object> switchMap, String paraPerfix) {
		return BeanInjector.injectMaps(switchMap, paraPerfix, getRequest(), false);
	}

	public <T> T reverse(Object model) {
		return paraReverse(null, model);
	}

	public <T> T reverse(Map<String, Object> reverseMap, Object model) {
		return paraReverse(reverseMap, model);
	}

	@SuppressWarnings("unchecked")
	private <T> T paraReverse(Map<String, Object> reverseMap, Object model) {
		return (T) BeanKit.reverse(reverseMap, model);
	}

	/**
	 * ============================ diy
	 * =================================================
	 */

	private Object basepage(String source, IQuery intercept) {
		Integer page = getParaToInt("page", 1);
		Integer rows = getParaToInt("rows", 10);
		String where = getPara("where", "");
		String sidx = getPara("sidx", "");
		String sord = getPara("sord", "");
		String sort = getPara("sort", "");
		String order = getPara("order", "");
		if (StrKit.notBlank(sidx)) {
			sort = sidx + " " + sord + (StrKit.notBlank(sort) ? ("," + sort) : "");
		}
		Object grid = GridManager.paginate(page, rows, source, where, sort, order, intercept, this);
		return grid;
	}
	
	private Object basepage(String source,Record record) {
		Integer page = getParaToInt("page", 1);
		Integer rows = getParaToInt("rows", 10);
		Object grid = Cst.me().getDefaultGridFactory().paginate(page, rows, source,record);
		return grid;
	}

	protected Object paginate(String source,Record record,Integer page) {
		Integer rows = getParaToInt("rows", 10);
		Object grid = Cst.me().getDefaultGridFactory().paginate(page, rows, source,record);
		return grid;
	}

	/**
	 * @param 数据源
	 * @return
	 */
	protected Object paginate(String source) {
		return basepage(source, Cst.me().getDefaultPageFactory());
	}

	/**
	 * @param 数据源
	 * @param 自定义拦截器
	 * @return
	 */
	protected Object paginate(String source, IQuery intercept) {
		return basepage(source, intercept);
	}
	
	/**
	 * @param 数据源
	 * @return
	 */
	protected Object paginate(String source,Record record) {
		return basepage(source, record);
	}

	/**
	 * 用户id
	 * @return
	 */
	protected Integer getUserId() {
		Object userId=ShiroKit.getUser().getId();
		if(userId!=null)
			return Integer.valueOf(userId.toString());
		return null;
	}

	/**
	 * 部门ID
	 * @return
	 */
	protected  Integer getDeptId() {
		Object deptId=ShiroKit.getUser().getDeptId();
		if(deptId!=null) {
			return Integer.valueOf(deptId.toString());
		}
		return null;
	}

	/**
	 * 是否超级管理员
	 * @return
	 */
	protected boolean isAdmin(){
		return ShiroKit.getUser().getRoles().contains("1");
	}

	/**
	 * 是否统计角色
	 * @return
	 */
	protected boolean isStatistics(){
		return ShiroKit.getUser().getRoles().contains("10");
	}

	/**
	 * 用户名
	 * @return
	 */	
	protected String getUserName() {

		Object userName=ShiroKit.getUser().getName();
		if(userName!=null)
			return userName.toString();
		return null;
	}
	
	private Date toDate(String value, Date defaultValue) {
		try {
			if (StrKit.isBlank(value))
				return defaultValue;
			return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
	/**
	 * Get para from url and conver to Integer with default value if it is null.
	 */
	public Date getParaToDate(int index, Date defaultValue) {
		return toDate(getPara(index), defaultValue);
	}
}
