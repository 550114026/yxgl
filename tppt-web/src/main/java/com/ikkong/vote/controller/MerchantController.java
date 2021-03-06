package com.ikkong.vote.controller;

import com.google.common.collect.Lists;
import com.ikkong.core.base.BaseController;
import com.ikkong.core.dao.Db;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.jfinal.ext.kit.JsonKit;
import com.ikkong.core.jfinal.ext.render.excel.PoiRender;
import com.ikkong.core.shiro.ShiroDbRealm;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.Record;
import com.ikkong.core.toolbox.kit.DateKit;
import com.ikkong.utils.DownImageUtil;
import com.ikkong.utils.ImportExcelUntil;
import com.ikkong.vote.model.Event;
import com.ikkong.vote.model.Merchant;
import com.ikkong.vote.service.EventService;
import com.ikkong.vote.service.MerchantService;
import com.ikkong.vote.service.impl.EventServiceImpl;
import com.ikkong.vote.service.impl.MerchantServiceImpl;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.upload.UploadFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Generated by Blade.
 * 2016-10-08 15:59:45
 */
	public class MerchantController extends BaseController {
	private static String CODE = "merchant";
	private static String PERFIX = "merchant";
	private static String LIST_SOURCE = "Merchant.list";
	private static String BASE_PATH = "/merchant/";
    private static String RECYCLE_PATH = "/merchantrecycle/";

	MerchantService service = new MerchantServiceImpl();
    EventService eventService = new EventServiceImpl();

    private static Logger log = LoggerFactory.getLogger(MerchantController.class);
	public void index() {
		setAttr("code", CODE);
		Object grid = getPage(null);
		setAttr("grid",grid);
		render(BASE_PATH + "list.html");
	}

	public void add() {
        Integer eventid=getParaToInt(0,0);
        setAttr("code", CODE);
        setAttr("eventid", eventid);
		render(BASE_PATH + "add.html");
	}

	public void edit() {
		Integer id = getParaToInt(0,0);
		Merchant merchant = service.findById(id);
        if(merchant!=null) {
            if (StringUtils.isNotEmpty(merchant.getPictures())) {
                String[] pictures = merchant.getPictures().split(",");
                setAttr("pictures", pictures);
            }
            setAttr("eventid", merchant.getEventid());
        }
		setAttr("model", merchant);
        setAttr("id", id);
		setAttr("id", id);
		setAttr("code", CODE);
		render(BASE_PATH + "add.html");
	}

	public void view() {
		String id = getPara(0);
		Merchant merchant = service.findById(id);
		setAttr("model", JsonKit.toJson(merchant));
		setAttr("id", id);
		setAttr("code", CODE);
		render(BASE_PATH + "merchant_view.html");
	}


	/**
	 * 取分页内容
	 */
	public void page() {
		Object grid = getPage(null);
		setAttr("grid",grid);
		render(BASE_PATH + "table.html");
	}

	/**
	 * 分页取数据
	 * @return
	 */
	private Object getPage(Integer delete_status) {
		Record record = Record.create();
        if(!isAdmin()) {
            record.set("deptid", getDeptId());
        }
		String eventid =getPara("eventid", "").trim();
        setAttr("eventid",eventid);
		String status =getPara("status", "").trim();
		String keystr = getPara("keystr", "").trim();
        record.set("eventid",eventid);
		record.set("audit_status",status);
		record.set("keystr",keystr);

        String sortfield = getPara("sortfield", "");
        String sortDirection = getPara("sortDirection", "");

        if (StringUtils.isNotBlank(sortfield)) {
            switch (sortDirection) {
                case "1":
                    sortDirection = "asc";
                    break;
                case "0":
                    sortDirection = "desc";
                    break;
                default:
                    sortDirection = "";
                    break;
            }
            record.set("sortDirection", sortDirection);
        }
        switch (sortfield) {
            case "totlevotes":
                sortfield="totlevotes";
                break;
            case "todayamount":
                sortfield="todayamount";
                break;
            default:
                sortfield = " totlevotes desc,indexno ";
                break;
        }
        record.set("sortfield", sortfield);

        //删除状态
        if(delete_status!=null){
            record.set("status", delete_status);
        }
		return paginate(LIST_SOURCE,record);
	}

	public void save() {
		Merchant merchant = mapping(PERFIX, Merchant.class);
        initMerchant(merchant);
		boolean temp = service.save(merchant);
		if (temp) {
			renderJson(success(SAVE_SUCCESS_MSG));
		} else {
			renderJson(error(SAVE_FAIL_MSG));
		}
	}

    /**
     * 初始化信息
     * @param merchant
     */
    private void initMerchant(Merchant merchant) {
        merchant.setVersion(1);
        merchant.setCreate_time(new Date());
        merchant.setUpdate_time(new Date());
        merchant.setAudit_status(1);
        merchant.setSource("1");
        merchant.setHomepage(0);
        merchant.setCreatr_user_id(getUserId());
        merchant.setStatus("1");
    }

    public void update() {
		Merchant merchant = mapping(PERFIX, Merchant.class);
        merchant.setVersion(merchant.getVersion()+1);
        merchant.setUpdate_user_id(getUserId());
        merchant.setUpdate_time(new Date());
		boolean temp = service.update(merchant);
		if (temp) {
			renderJson(success(UPDATE_SUCCESS_MSG));
		} else {
			renderJson(error(UPDATE_FAIL_MSG));
		}
	}


    public void push() {
        Integer id = getParaToInt("id",0);
        Integer val = getParaToInt("val",0);
        Merchant merchant = service.findById(id);
        if(merchant!=null) {
            merchant.setVersion(merchant.getVersion()+1);
            merchant.setHomepage(val==0?1:0);
            merchant.setUpdate_time(new Date());
            boolean temp = service.update(merchant);
            if (temp) {
                renderJson(success(OP_SUCCESS_MSG));
            } else {
                renderJson(error(OP_FAIL_MSG));
            }
        }else {
            renderJson(error(OP_FAIL_MSG));
        }

    }

    public void audit() {
        Integer id = getParaToInt("id",0);
        Integer val = getParaToInt("state",0);
        Merchant merchant = service.findById(id);
        if(merchant!=null) {
            merchant.setVersion(merchant.getVersion()+1);
            merchant.setAudit_status(val);
            merchant.setUpdate_time(new Date());
            boolean temp = service.update(merchant);
            if (temp) {
                renderJson(success(OP_SUCCESS_MSG));
            } else {
                renderJson(error(OP_FAIL_MSG));
            }
        }else {
            renderJson(error(OP_FAIL_MSG));
        }
    }

	public void remove() {
        Integer id = getParaToInt("id",0);
        Merchant merchant = service.findById(id);
        if(merchant!=null) {
            Record record = Record.create();
            record.set("id", id);
            record.set("eventid",merchant.getEventid());
            record.set("indexno",merchant.getIndexno());
            merchant.setUpdate_time(new Date());
            Integer temp = service.deleteById(record);

            if (temp>0) {
                renderJson(success(DEL_SUCCESS_MSG));
            } else {
                renderJson(error(DEL_FAIL_MSG));
            }
        }else {
            renderJson(error(DEL_FAIL_MSG));
        }
	}



    public void recycle(){
        setAttr("code", CODE);
        Object grid = getPage(-1);
        setAttr("grid", grid);
        render(RECYCLE_PATH + "list.html");
    }


    /**
     * 取分页内容
     */
    public void recyclepage() {
        Object grid = getPage(-1);
        setAttr("grid", grid);
        render(RECYCLE_PATH + "table.html");
    }


    public void recyclemerchant(){
        Integer id = getParaToInt("id",0);
        Merchant merchant = service.findById(id);
        if(merchant!=null) {
            Record record = Record.create();
            record.set("id", id);
            record.set("eventid",merchant.getEventid());
            merchant.setUpdate_time(new Date());
            Integer temp = service.recycleById(record);
            if (temp>0) {
                renderJson(success(RESTORE_SUCCESS_MSG));
            } else {
                renderJson(error(RESTORE_FAIL_MSG));
            }
        }else {
            renderJson(error(RESTORE_FAIL_MSG));
        }
    }

    public void export(){
        String eid = getPara("eid");
      Event event=  eventService.findById(eid);
      if(event!=null) {
          String sql = Db.init().getSql("Merchant.excel");
          String[] header = new String[]{"选手编号", "选手名称", "联系电话"};
          String[] columns =  new String[]{"indexno", "name", "phone"};
          Record record = Record.create();
          record.set("eventid", eid);
          List dataResult = Db.init().queryListMap(sql, record);
          String fileName=event.getName() + DateKit.getAllTime() + ".xls";
          try {
              fileName=new String(fileName.getBytes(), "ISO-8859-1");
          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          }
          render(PoiRender.me(dataResult).fileName(fileName)
                  .columns(columns).headers(header).sheetName("选手信息").cellWidth(5000).headerRow(1));
      }
      else{
          renderJson(error("参数错误！"));
      }
    }

    public void selectExcel(){
        setAttr("code", CODE);
        Integer eid = getParaToInt("eid",0);
        setAttr("eid", eid);
        render(BASE_PATH + "import.html");
    }

    public void importExcel(){
        Integer eid = getParaToInt("eid",0);
        int totle=0;
        int sucess=0;
        int fail=0;
        try {
            UploadFile fploadFile = getFile();
            String fieldsStr="name,adress,phone,imgpath";
            List<String> fields=Arrays.asList(fieldsStr.split(","));
            List<Map<String, String>> lineList=ImportExcelUntil.importExcel(fploadFile.getFile(),fields);
            totle=lineList.size();
            String image=null;
            String imageUrl=null;
            Merchant merchant=null;
            //逐行处理
            for(Map<String, String> map:lineList){
                try {
                    if(StringUtils.isBlank(map.get("name"))||StringUtils.isBlank(map.get("phone"))){
                        return ;
                    }
                    merchant = new Merchant();
                    merchant.setEventid(eid);
                    merchant.setName(map.get("name"));
                    merchant.setPhone(map.get("phone").replace("[\"","").replace("\"]",""));
                    merchant.setAddress(map.get("adress"));
                    initMerchant(merchant);
                    imageUrl = map.get("imgpath").replace("[\"","").replace("\"]","");
                    if(imageUrl.contains("\",\"")){
                        String[] imgs=imageUrl.split("\",\"");
                        for (String img:imgs){
                            img=img.replaceAll("\"","");
                            if(img.length()>10){
                                imageUrl=img;
                                break;
                            }
                        }
                    }
                    if (imageUrl != null) {
                        image = DownImageUtil.downLoadImage(imageUrl,eid.toString());
                        merchant.setPictures(image);
                    }else{
                        fail++;
                    }
                    service.save(merchant);
                    sucess++;
                }
                catch (Exception e){
                    log.error("导入失败",e);
                }
            }

            String msg="导入完成，Excel总行数"+totle+",导入成功"+sucess+"条";
            if(fail>0){
                msg=msg+"，其中"+fail+"条没有图片！";
            }
            renderJson(success(msg));

        }
        catch (Exception ex){
            ex.printStackTrace();
            log.error(ex.getMessage());
            log.error("导入失败",ex);
            renderJson(error("导入异常！"));
        }

    }




}
