package com.ikkong.system.controller;

import com.ikkong.core.constant.Cst;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.interfaces.IMeta;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.modules.beetl.BeetlMaker;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.meta.factory.GenerateFactory;
import com.ikkong.system.model.Generate;
import com.jfinal.kit.StrKit;
import org.beetl.sql.core.JavaType;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.ColDesc;
import org.beetl.sql.core.db.TableDesc;

import java.io.File;
import java.util.*;

public class GenerateController extends CurdController<Generate> {

	@Override
	protected Class<? extends IMeta> metaFactoryClass() {
		return GenerateFactory.class;
	}
	
//	@RequestMapping("/pojo/{table}")
	public void createPojo() {
		String table = getPara(0);
		try {
			Blade.dao().genPojoCodeToConsole(table);
			renderText("[ " + table + " ] pojo生成成功,请查看控制台");
		} catch (Exception e) {
			renderText("[ " + table + " ] pojo生成失败:" + e.getMessage());
		}
	}
	
	public void gencode(){
		String ids = getPara("ids");
		List<Generate> list = Blade.create(Generate.class).findBy("id in (#{join(ids)})", Record.create().set("ids", ids.split(",")));

		for (Generate gen : list) {
			String realPath = gen.getRealpath() + File.separator + "src" + File.separator + "main";
			String packageName = gen.getPackagename();
			String modelName = gen.getModelname();
			String upperModelName = StrKit.firstCharToUpperCase(modelName);
			String lowerModelName = StrKit.firstCharToLowerCase(modelName);
			
			String tableName = gen.getTablename();
			String pkName = gen.getPkname();
			String path = realPath + File.separator + "java" + File.separator + packageName.replace(JStrKit.DOT, File.separator);
			String resourcesPath = realPath + File.separator + "resources";
			String webappPath = realPath + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "view";
			
			//java
			String controllerPath = path + File.separator + "controller" + File.separator + upperModelName + "Controller.java";
			String modelPath = path + File.separator + "model" + File.separator + upperModelName + ".java";
			String servicePath = path + File.separator + "service" + File.separator + upperModelName + "Service.java";
			String serviceimplPath = path + File.separator + "service" + File.separator + "impl" + File.separator + upperModelName + "ServiceImpl.java";
			
			//resources
			String sqlPath = resourcesPath + File.separator + "beetlsql" + File.separator + upperModelName + ".md";
			
			//webapp
			String indexPath = webappPath + File.separator + "platform" + File.separator + lowerModelName + File.separator + lowerModelName + ".html";
			String addPath = webappPath + File.separator + "platform" + File.separator + lowerModelName + File.separator + lowerModelName + "_add.html";
			String editPath = webappPath + File.separator + "platform" + File.separator + lowerModelName + File.separator + lowerModelName + "_edit.html";
			String viewPath = webappPath + File.separator + "platform" + File.separator + lowerModelName + File.separator + lowerModelName + "_view.html";
			
			Map<String, String> pathMap = new HashMap<>();
			pathMap.put("controllerPath", controllerPath);
			pathMap.put("modelPath", modelPath);
			pathMap.put("servicePath", servicePath);
			pathMap.put("serviceimplPath", serviceimplPath);
			pathMap.put("sqlPath", sqlPath);
			pathMap.put("indexPath", indexPath);
			pathMap.put("addPath", addPath);
			pathMap.put("editPath", editPath);
			pathMap.put("viewPath", viewPath);
			
			for (Map.Entry<String, String> entry : pathMap.entrySet()) {  
				File file = new File(entry.getValue());
				if (file.exists()) {
					continue;
				} else {
					file.getParentFile().mkdirs();
				}
			}
			
			//java
			String baseTemplatePath = Cst.me().getRealPath()+ File.separator + "WEB-INF"+ File.separator + "view" + File.separator + "common" + File.separator + "_template" + File.separator;
			String controllerTemplatePath = baseTemplatePath + "_controller" + File.separator + "_controller.bld";
			String modelTemplatePath = baseTemplatePath + "_model" + File.separator +  "_model.bld";
			String serviceTemplatePath = baseTemplatePath + "_service" + File.separator + "_service.bld";
			String serviceimplTemplatePath = baseTemplatePath + "_service" + File.separator + "_impl" + File.separator + "_serviceimpl.bld";
			
			//resources
			String sqlTemplatePath = baseTemplatePath + "_sql" + File.separator + "_sql.bld";
			
			//webapp
			String indexTemplatePath = baseTemplatePath + "_view" + File.separator + "_index.bld";
			String addTemplatePath = baseTemplatePath + "_view" + File.separator + "_add.bld";
			String editTemplatePath = baseTemplatePath + "_view" + File.separator + "_edit.bld";
			String viewTemplatePath = baseTemplatePath + "_view" + File.separator + "_view.bld";
			
			Record maps = Record.create();
			maps.set("realPath", realPath);
			maps.set("packageName", packageName);
			maps.set("modelName", upperModelName);
			maps.set("lowerModelName", lowerModelName);
			maps.set("tableName", tableName);
			maps.set("pkName", pkName);
			
			//java
			BeetlMaker.makeHtml(controllerTemplatePath, maps, controllerPath);
			BeetlMaker.makeHtml(serviceTemplatePath, maps, servicePath);
			BeetlMaker.makeHtml(serviceimplTemplatePath, maps, serviceimplPath);
			setParasAttr(tableName,maps);
			BeetlMaker.makeHtml(modelTemplatePath, maps, modelPath);

			//resources
			BeetlMaker.makeHtml(sqlTemplatePath, maps, sqlPath);
			
			//webapp
			final TableDesc tableDesc = Blade.dao().getMetaDataManager().getTable(tableName);
			Set<String> cols = tableDesc.getCols();
			maps.set("cols", cols);
			maps.set("tableDesc",tableDesc);
			
			
			BeetlMaker.makeHtml(indexTemplatePath, maps, indexPath);
			BeetlMaker.makeHtml(addTemplatePath, maps, addPath);
			BeetlMaker.makeHtml(editTemplatePath, maps, editPath);
			BeetlMaker.makeHtml(viewTemplatePath, maps, viewPath);
			
		}
		
		renderJson(success("生成成功!"));
	}

	private void setParasAttr(String table, Record ps) {
		SQLManager sm = Blade.dao();
		final TableDesc  tableDesc = sm.getMetaDataManager().getTable(table);
		Set<String> cols = tableDesc.getCols();
		List<Map<String, Object>> attrs = new ArrayList<>();
		boolean tempDouble = false;
		boolean tempDate = false;
		for(String col : cols){
			ColDesc desc = tableDesc.getColDesc(col);
			Map<String, Object> attr = Record.createHashMap();
			attr.put("comment", desc.remark);
			String attrName = sm.getNc().getPropertyName(null, desc.colName);
			attr.put("name", attrName);
			attr.put("methodName", getMethodName(attrName));
			attr.put("type", desc.remark);
			String type = JavaType.getType(desc.sqlType, desc.size, desc.digit);
			if(type.equals("Double")){
				type = "BigDecimal";
				tempDouble = true;
			}
			if(type.equals("Timestamp")){
				type ="Date";
				tempDate = true;
			}
			attr.put("type", type);
			attr.put("desc", desc);
			attrs.add(attr);
		}

		// 主键总是拍在前面，int类型也排在前面，剩下的按照字母顺序排
		Collections.sort(attrs,new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				ColDesc desc1  = (ColDesc) o1.get("desc");
				ColDesc desc2  = (ColDesc) o2.get("desc");
				int score1 = score(desc1);
				int score2 = score(desc2);
				if(score1 == score2){
					return desc1.colName.compareTo(desc2.colName);
				}else{
					return score2 - score1;
				}
			}

			private int score(ColDesc desc){
				if(tableDesc.getIdNames().contains(desc.colName)) {
					return 99;
				}else if(JavaType.isInteger(desc.sqlType)) {
					return 9;
				}else if(JavaType.isDateType(desc.sqlType)) {
					return -9;
				}else{
					return 0;
				}
			}

		});

		String srcHead = "";
		String CR = System.getProperty("line.separator");
		if(tempDate) {
			srcHead += "import java.util.Date;" + CR;
		}
		if(tempDouble) {
			srcHead += "import java.math.BigDecimal;" + CR;
		}

		ps.set("attrs", attrs);
		ps.set("imports", srcHead);
	}

	private String getMethodName(String name) {
		char ch1 = name.charAt(0);
		char ch2 = name.charAt(1);
		if(Character.isLowerCase(ch1) && Character.isUpperCase(ch2)) {
			//aUname---> getaUname();
			return name;
		} else if(Character.isUpperCase(ch1) && Character.isUpperCase(ch2)) {
			//ULR --> getURL();
			return name ;
		} else {
			//general  name --> getName()
			char upper = Character.toUpperCase(ch1);
			return upper + name.substring(1);
		}
	}
}
