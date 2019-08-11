package com.ikkong.common.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.wall.WallFilter;
import com.baidu.ueditor.UeditorConfigKit;
import com.baidu.ueditor.manager.QiniuFileManager;
import com.ikkong.core.constant.Const;
import com.ikkong.core.intercept.DoLogInterceptor;
import com.ikkong.core.jfinal.ext.autoroute.AutoBindRoutes;
import com.ikkong.core.jfinal.ext.shiro.ShiroInterceptor;
import com.ikkong.core.jfinal.ext.shiro.ShiroPlugin;
import com.ikkong.core.modules.beetl.BeetlExt;
import com.ikkong.core.modules.beetl.BeetlRenderFactory;
import com.ikkong.core.modules.beetl.ShiroExt;
import com.ikkong.core.modules.beetl.tag.DropDownTag;
import com.ikkong.core.modules.beetl.tag.FootTag;
import com.ikkong.core.modules.beetl.tag.SelectTag;
import com.ikkong.core.modules.beetl.tag.SideBarTag;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;

import cn.dreampie.quartz.QuartzPlugin;
import net.dreamlu.controller.UeditorApiController;
import org.beetl.core.GroupTemplate;
import org.beetl.sql.ext.jfinal.JFinalBeetlSql;
import org.beetl.sql.ext.jfinal.Trans;
import org.quartz.JobExecutionException;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainConfig extends JFinalConfig implements Const{
	public static final Map<String, String> map = new HashMap<String, String>();

	@Override
	public void configConstant(Constants me) {
		// 配置常量
		PropKit.use("config.properties");
		loadPropertyFile("config.properties");
		map.put("dbType", getProperty("master.dbType", "mysql"));
		me.setDevMode(getPropertyToBoolean("config.devMode", false));
		me.setRenderFactory(new BeetlRenderFactory());
		// 获取GroupTemplate ,可以添加扩展函数，格式化函数，共享变量等，
		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate ;
		Map<String, Object> sharedVars = new HashMap<String, Object>();
		sharedVars.put("startTime", new Date());
		sharedVars.put("basePath", getProperty("config.basePath", "/"));
		sharedVars.put("mapAk", getProperty("baidu.map.ak"));
		sharedVars.put("wxUri", getProperty("wx.uri"));
		sharedVars.put("imageSite", getProperty("imageSite.Url"));
		groupTemplate.setSharedVars(sharedVars);
		groupTemplate.registerTag("select", SelectTag.class);
		groupTemplate.registerTag("sidebar", SideBarTag.class);
		groupTemplate.registerTag("dropdown", DropDownTag.class);
		groupTemplate.registerTag("foot", FootTag.class);
		groupTemplate.registerFunctionPackage("func", new BeetlExt());
		groupTemplate.registerFunctionPackage("shiro", new ShiroExt());

		initErrorView(me);
	}

	private void initErrorView(Constants me) {
		me.setError401View(error401Path);
		me.setError403View(error403Path);
		me.setError404View(error404Path);
		me.setError500View(error500Path);
	}

	@Override
	public void configHandler(Handlers me) {
		DruidStatViewHandler dvh =  new DruidStatViewHandler("/druid");
		me.add(dvh);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// 配置拦截器
		//me.add(new ExceptionHandlerInterceptor());
		me.add(new Trans());//配置事务
		me.add(new DoLogInterceptor());
		me.add(new ShiroInterceptor());
	}

	@Override
	public void configPlugin(Plugins me) {
		// 配置插件
		me.add(new EhCachePlugin());

		DruidPlugin druidPlugin = new DruidPlugin(getProperty("master.url"),
				getProperty("master.username"), getProperty("master.password")
						.trim(), getProperty("master.driver")).set(
				getPropertyToInt("druid.initialSize"),
				getPropertyToInt("druid.minIdle"),
				getPropertyToInt("druid.maxActive")).setMaxWait(
				getPropertyToLong("druid.maxWait"));

        druidPlugin.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType(getProperty("master.dbType"));
		druidPlugin.addFilter(wall);
		me.add(druidPlugin);
		druidPlugin.start();
		JFinalBeetlSql.init(druidPlugin.getDataSource(), null);//主从库在此初始化
	    me.add(new ShiroPlugin(Routes.getRoutesList().get(0)));
	    //任务调度插件
	    QuartzPlugin quartzPlugin = new QuartzPlugin();
	    quartzPlugin.setJobs("quartzJob.properties");
	    me.add(quartzPlugin);

	}

	@Override
	public void configRoute(Routes me) {
		// 配置路由
		me.add(new AutoBindRoutes());
		// ueditor
		me.add("/ueditor/api", UeditorApiController.class);
	}

	@Override
	public void configEngine(Engine engine) {

	}

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 8090, "/");
	}

	@Override
	public void afterJFinalStart() {
		ServletContext sc = JFinal.me().getServletContext();
		// 项目路径
		map.put("realPath", sc.getRealPath("/").replaceFirst("/", ""));
		map.put("contextPath", sc.getContextPath());
		for (Object name : prop.getProperties().keySet()) {
			map.put(name.toString(), prop.getProperties().get(name).toString());
		}

		//ueditor 文件 默认上传到本地，执行以下代码上传到 七牛，示例见 [博客管理]，别忘了七牛配置
		if(map.get("config.devMode").toLowerCase().equals("false")){
			String ak = getProperty("qiniu.ak");
			if(StringUtils.isEmpty(ak))
				return;
			String sk = getProperty("qiniu.sk");
			String bucket = getProperty("qiniu.bucket");
			UeditorConfigKit.setFileManager(new QiniuFileManager(ak, sk, bucket));
		}

//		CollectCreditInformationJob job=new CollectCreditInformationJob();
//		try {
//			job.execute(null);
//		} catch (JobExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}


}
