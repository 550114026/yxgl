package com.ikkong.task;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ikkong.core.aop.AopContext;
import com.ikkong.core.constant.ConstCache;
import com.ikkong.core.dao.Db;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.model.Enterprise;
import com.ikkong.system.model.EnterpriseCredit;
import com.ikkong.system.service.EnterpriseCreditService;
import com.ikkong.system.service.EnterpriseService;
import com.ikkong.system.service.impl.EnterpriseCreditServiceImpl;
import com.ikkong.system.service.impl.EnterpriseServiceImpl;
import com.ikkong.utils.GisPoint;
import com.ikkong.utils.GisUtil;
import com.ikkong.utils.NetUtils;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

public class CollectCreditInformationJob implements Job {

	Log logger = Log.getLog(CollectCreditInformationJob.class);

	final String searchUrl = "http://credit.fuzhou.gov.cn/creditsearch.corlist.dhtml";
	final String detailUrl = "http://credit.fuzhou.gov.cn/creditsearch.detail.dhtml";

	final String baiduUrl = "http://api.map.baidu.com/geocoder/v2/";
	Map<Integer, List<GisPoint>> areamap = new HashMap<Integer, List<GisPoint>>();
	String charset = "UTF-8";

	EnterpriseService service = new EnterpriseServiceImpl();
	EnterpriseCreditService creditService = new EnterpriseCreditServiceImpl();
	Map<String, String> fieldNameMap = new HashMap<String, String>();

	public CollectCreditInformationJob() {
		fieldNameMap.put("机构名称", "name");
		fieldNameMap.put("统一社会信用代码", "socialcreditcode");
		fieldNameMap.put("工商注册号", "registrationnumber");
		fieldNameMap.put("法定代表人", "legalrepresentative");
		fieldNameMap.put("注册资本（万元）", "registeredcapital");
		fieldNameMap.put("成立日期", "formatdate");
		fieldNameMap.put("经营期限自", "timelimitfrom");
		fieldNameMap.put("经营期限至", "timelimitto");
		fieldNameMap.put("行业门类", "industrycategory");
		fieldNameMap.put("登记机关", "registrationauthority");
		fieldNameMap.put("经营(业务)范围", "businessscope");
		fieldNameMap.put("地址", "adress");

		ConvertUtils.register(new DateLocaleConverter(), Date.class);
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		logger.info("开始信用数据采集.");
		int start = 0;
		int count = 100;
		int totleSize=0;
		int unGetSize=0;
		int getSize=0;
		List<Record> list = null;
		do {
			// 查询企业数据
			list = service.queryEntToCollectCredit(start, count);
			logger.info("获取企业数：" + list.size());
			totleSize=totleSize+list.size();
			Date now = new Date();
			Integer outRegistType=1;//注册地类型：1园区内注册、2园区外注册、3仓山区外注册
			// 逐个抓取信用数据
			for (Record record : list) {
				try {
					logger.info("开始处理企业：" + JSON.toJSONString(record));
					EnterpriseCredit enterpriseCredit = getCreditInfo(record);
					if (enterpriseCredit == null) {
						unGetSize=unGetSize+1;
						logger.info("获取企业信用信息失败：" + JSON.toJSONString(record));
						if (record.get("cid") == null) {
							enterpriseCredit = new EnterpriseCredit();
							enterpriseCredit.setEntid(record.getLong("id"));
							enterpriseCredit.setUpdatetime(now);
							enterpriseCredit.setName(record.getStr("name"));
							creditService.save(enterpriseCredit);
							//标注未知
							Enterprise enterprise=service.findById(record.getLong("id"));
							enterprise.setOutregist(4);
							//enterprise.setRegistaddress(enterpriseCredit.getAdress());
							enterprise.setCheckstatus(1);
							enterprise.setCheckdate(now);
							enterprise.setVersion(enterprise.getVersion()+1);
							service.update(enterprise);
						}
						continue;
					}
					getSize=getSize+1;
					logger.info("企业信用信息：" + JSON.toJSONString(enterpriseCredit));
					if (!enterpriseCredit.getAdress().equals(record.getStr("address"))) {
						// 地址转百度gps
						getGpsByAddess(enterpriseCredit);
						enterpriseCredit.setEntid(record.getLong("id"));
						// 区域判断
						getMatchnmmmmnkbAreaId(enterpriseCredit);
						outRegistType=1;
						if(enterpriseCredit.getAreaid()!=null){
							//-1 仓山区域外
							if(enterpriseCredit.getAreaid().equals(-1)){
								outRegistType=3;
							}else if(enterpriseCredit.getAreaid().equals(0)){
								//0 仓山区内
								outRegistType=2;
							}
						}
						//修改企业注册地信息
						Enterprise enterprise=service.findById(record.getLong("id"));
						if(enterprise.getOutregist()==null||!enterprise.getOutregist().equals(outRegistType)){
							enterprise.setOutregist(outRegistType);
							enterprise.setRegistaddress(enterpriseCredit.getAdress());
							enterprise.setCheckstatus(1);
							enterprise.setCheckdate(now);
							enterprise.setVersion(enterprise.getVersion()+1);
							service.update(enterprise);
						}else if(!enterprise.getRegistaddress().equals(enterpriseCredit.getAdress())){
							enterprise.setRegistaddress(enterpriseCredit.getAdress());
							enterprise.setVersion(enterprise.getVersion()+1);
							service.update(enterprise);
						}


						// 入库
						if (record.get("cid") != null) {
							enterpriseCredit.setId(record.getLong("cid"));
							enterpriseCredit.setUpdatetime(now);
							creditService.update(enterpriseCredit);
						} else {
							enterpriseCredit.setUpdatetime(now);
							creditService.save(enterpriseCredit);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			start = start + count;
		} while (list.size() == count);
		logger.info("信用数据采集结束：企业总数："+totleSize+"，采集成功数："+getSize+"，未采集到数："+unGetSize);

	}


	/**
	 * 从百度获取GPS
	 *
	 * @param enterpriseCredit
	 */
	private void getGpsByAddess(EnterpriseCredit enterpriseCredit) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("city", "福州市");
		params.put("output", "json");
		try {
			params.put("address", URLEncoder.encode(enterpriseCredit.getAdress(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		params.put("ak", PropKit.get("baidu.map.ak"));
		String json = NetUtils.sendGet(baiduUrl, params, charset);
		JSONObject returnJson = JSON.parseObject(json);
		if (returnJson.getInteger("status").equals(0)) {
			JSONObject location = returnJson.getJSONObject("result").getJSONObject("location");
			BigDecimal lng = location.getBigDecimal("lng");
			BigDecimal lat = location.getBigDecimal("lat");
			enterpriseCredit.setLng(lng);
			enterpriseCredit.setLat(lat);
		}
	}

	private EnterpriseCredit getCreditInfo(Record record) throws UnsupportedEncodingException {
		String name = record.getStr("name").trim();
		name = URLEncoder.encode(name, "UTF-8");
		Record result = Record.create();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kw", name);
		String html = NetUtils.sendGet(searchUrl, params, charset);
		String patternStr = "/creditsearch\\.detail\\.dhtml\\?id=([^\"]*)";
		String id = getMatchStr(patternStr, html);
		if (StringUtils.isNotEmpty(id)) {
			params = new HashMap<String, Object>();
			params.put("id", id);
			html = NetUtils.sendGet(detailUrl, params, charset);
			Document doc = Jsoup.parse(html);
			Element div = doc.getElementById("menu1");
			Elements tables = div.getElementsByTag("table");
			Element detailsTable = null;
			for (Element table : tables) {
				if ("detailsList".equals(table.attr("class"))) {
					detailsTable = table;
					break;
				}
			}
			if (detailsTable != null) {
				Elements trs = div.getElementsByTag("tr");
				for (Element tr : trs) {
					Elements tds = tr.getElementsByTag("td");
					int index = 1;
					while (tds.size() > index) {
						if (fieldNameMap.containsKey(tds.get(index - 1).text())
								&& StringUtils.isNotEmpty(tds.get(index).text())) {
							result.put(fieldNameMap.get(tds.get(index - 1).text()), tds.get(index).text());
						}
						index = index + 2;
					}

				}
			}

		} else {
			return null;
		}
		EnterpriseCredit enterpriseCredit = new EnterpriseCredit();
		logger.info("信用信息：" + JSON.toJSONString(result));
		try {
			org.apache.commons.beanutils.BeanUtils.populate(enterpriseCredit, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enterpriseCredit;
	}

	private String getMatchStr(String patternStr, String sourceStr) {
		Pattern pt = Pattern.compile(patternStr);
		Matcher match = pt.matcher(sourceStr);
		if (match.find()) {
			MatchResult ms = match.toMatchResult();
			return ms.group(1);
		}
		return "";
	}

	/**
	 * 匹配区域
	 *
	 * @param enterpriseCredit
	 */
	private void  getMatchnmmmmnkbAreaId(EnterpriseCredit enterpriseCredit) {
		if(enterpriseCredit.getLng()==null)
			return;
		GisPoint gisPoint=new GisPoint();
		gisPoint.setLat(enterpriseCredit.getLat().doubleValue());
		gisPoint.setLng(enterpriseCredit.getLng().doubleValue());

		if (areamap.size()==0) {
			String CACHE_NAME = ConstCache.AREA_GIS_CACHE;
			final String sql = "select ID,maparea from  TFW_DEPT where isarea=2";
			List<Map<String, Object>> areaList = CacheKit.get(CACHE_NAME, "dict_AREA_GIS_CACHE", new IDataLoader() {
				@Override
				public Object load() {
					return Db.init().selectList(sql, Record.createHashMap(), new AopContext(), null);
				}
			});
			JSONObject point=null;
			GisPoint gp=null;
			List<GisPoint> list=new ArrayList<GisPoint>();
			for (Map<String, Object> map : areaList) {
				if (map.get("maparea") != null) {
					try{
						list=new ArrayList<GisPoint>();
						JSONArray array = JSONArray.parseArray(map.get("maparea").toString());
						for(int i=0;i<array.size();i++){
							point=array.getJSONObject(i);
							gp=new GisPoint();
							gp.setLng(point.getDoubleValue("lng"));
							gp.setLat(point.getDoubleValue("lat"));
							list.add(gp);
						}
						areamap.put(Integer.valueOf(map.get("ID").toString()), list);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		//区域内
		for (Map.Entry<Integer, List<GisPoint>> entry : areamap.entrySet()) {
		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		   if( GisUtil.isInPolygon(gisPoint, entry.getValue())){
			   enterpriseCredit.setAreaid(entry.getKey());
			   break;
		   }
		}

		//区域外处理
		if(enterpriseCredit.getAreaid()==null){
			getDistrictByGPS(enterpriseCredit);
		}


	}


	/**
	 * 从百度获取GPS
	 *
	 * @param enterpriseCredit
	 */
	private void getDistrictByGPS(EnterpriseCredit enterpriseCredit) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("city", "福州市");
		params.put("output", "json");
		params.put("pois", "0");
		params.put("extensions_poi", "null");
		params.put("latest_admin", "1");
		params.put("ak", PropKit.get("baidu.map.ak"));
		params.put("location",enterpriseCredit.getLat().toString()+","+enterpriseCredit.getLng().toString());
		String json = NetUtils.sendGet(baiduUrl, params, charset);
		JSONObject returnJson = JSON.parseObject(json);
		if (returnJson.getInteger("status").equals(0)) {
			JSONObject addressComponent = returnJson.getJSONObject("result").getJSONObject("addressComponent");
			String district=addressComponent.getString("district");
			if("仓山区".equals(district)){
				enterpriseCredit.setAreaid(0);
			}
			else
				enterpriseCredit.setAreaid(-1);
		}
	}

}
