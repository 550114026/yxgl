package com.ikkong.core.constant;

import com.ikkong.common.config.MainConfig;
import com.ikkong.core.intercept.CURDInterceptor;
import com.ikkong.core.intercept.QueryInterceptor;
import com.ikkong.core.intercept.SelectInterceptor;
import com.ikkong.core.interfaces.ICURD;
import com.ikkong.core.interfaces.IGrid;
import com.ikkong.core.interfaces.ILog;
import com.ikkong.core.interfaces.IQuery;
import com.ikkong.core.interfaces.ISelect;
import com.ikkong.core.interfaces.IShiro;
import com.ikkong.core.shiro.DefaultShiroFactroy;
import com.ikkong.core.toolbox.file.DefaultFileProxyFactory;
import com.ikkong.core.toolbox.file.IFileProxy;
import com.ikkong.core.toolbox.grid.JqGridFactory;
import com.ikkong.core.toolbox.log.BladeLogFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cst {

	/**
	 * 默认shirorealm工厂类
	 */
	private IShiro defaultShiroFactory = new DefaultShiroFactroy();
	/**
	 * 默认查询工厂类
	 */
	private IQuery defaultQueryFactory = new QueryInterceptor();
	/**
	 * 默认日志处理工厂类
	 */
	private ILog defaultLogFactory = new BladeLogFactory();
	/**
	 * 默认select查询工厂类
	 */
	private ISelect defaultSelectFactory = new SelectInterceptor();
	/**
	 * 默认grid分页工厂类
	 */
	private IGrid defaultGridFactory = new JqGridFactory();
	/**
	 * 默认分页工厂类
	 */
	private IQuery defaultPageFactory = new QueryInterceptor();
	/**
	 * 默认CURD工厂类
	 */
	private ICURD defaultCURDFactory = new CURDInterceptor();
	/**
	 * 默认文件上传转换工厂类
	 */
	private IFileProxy defaultFileProxyFactory = new DefaultFileProxyFactory();
	
	/**
	 * 项目物理路径
	 */
	private String realPath ;
	/**
	 * 远程上传模式
	 */
	private boolean remoteMode = false;
	/**
	 * 上传下载路径(物理路径)
	 */
	private String remotePath = "D://jblade";

	/**
	 * 上传路径(相对路径)
	 */
	private String uploadPath = "/upload/temp";

	/**
	 * 下载路径
	 */
	private String downloadPath = "/download";

	/**
	 * 项目相对路径
	 */
	private String contextPath ;
	
	

	/**
	 * 密码允许错误次数
	 */
	private int passErrorCount = 6;

	/**
	 * 密码锁定小时数
	 */
	private int passErrorHour = 6;
	
	/**
	 * 是否启用乐观锁
	 */
	private boolean optimisticLock = true;
	
	
	private static final Cst me = new Cst();

	private Cst() {

	}

	public static Cst me() {
		return me;
	}
	
	
	
	public IFileProxy getDefaultFileProxyFactory() {
		return defaultFileProxyFactory;
	}

	public void setDefaultFileProxyFactory(IFileProxy defaultFileProxyFactory) {
		this.defaultFileProxyFactory = defaultFileProxyFactory;
	}

	public String getUploadCtxPath() {


		return getContextPath() + uploadPath;
	}
	
	public String getUploadRealPath() {
		return (remoteMode ? remotePath : getRealPath()) + uploadPath;
	}
	
	public ICURD getDefaultCURDFactory() {
		return defaultCURDFactory;
	}

	public void setDefaultCURDFactory(ICURD defaultCURDFactory) {
		this.defaultCURDFactory = defaultCURDFactory;
	}

	public IQuery getDefaultPageFactory() {
		return defaultPageFactory;
	}

	public void setDefaultPageFactory(IQuery defaultPageFactory) {
		this.defaultPageFactory = defaultPageFactory;
	}

	public IGrid getDefaultGridFactory() {
		return defaultGridFactory;
	}

	public void setDefaultGridFactory(IGrid defaultGridFactory) {
		this.defaultGridFactory = defaultGridFactory;
	}

	public ISelect getDefaultSelectFactory() {
		return defaultSelectFactory;
	}

	public void setDefaultSelectFactory(ISelect defaultSelectFactory) {
		this.defaultSelectFactory = defaultSelectFactory;
	}

	public String getRealPath() {
		return MainConfig.map.get("realPath");
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getContextPath() {
		return MainConfig.map.get("contextPath");
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public IQuery getDefaultQueryFactory() {
		return defaultQueryFactory;
	}

	public void setDefaultQueryFactory(IQuery defaultQueryFactory) {
		this.defaultQueryFactory = defaultQueryFactory;
	}

	public ILog getDefaultLogFactory() {
		return defaultLogFactory;
	}

	public void setDefaultLogFactory(ILog defaultLogFactory) {
		this.defaultLogFactory = defaultLogFactory;
	}

	public int getPassErrorCount() {
		return passErrorCount;
	}

	public void setPassErrorCount(int passErrorCount) {
		this.passErrorCount = passErrorCount;
	}

	public int getPassErrorHour() {
		return passErrorHour;
	}

	public void setPassErrorHour(int passErrorHour) {
		this.passErrorHour = passErrorHour;
	}

	public boolean isOptimisticLock() {
		return optimisticLock;
	}

	public void setOptimisticLock(boolean optimisticLock) {
		this.optimisticLock = optimisticLock;
	}

	public IShiro getDefaultShiroFactory() {
		return defaultShiroFactory;
	}

	public void setDefaultShiroFactory(IShiro defaultShiroFactory) {
		this.defaultShiroFactory = defaultShiroFactory;
	}

	public boolean isRemoteMode() {
		return remoteMode;
	}
	
	
}
