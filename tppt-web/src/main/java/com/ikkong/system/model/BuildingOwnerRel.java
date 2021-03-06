package com.ikkong.system.model;

import org.beetl.sql.core.annotatoin.Table;
import com.ikkong.core.annotation.BindID;
import com.ikkong.core.model.BaseModel;
import java.math.BigDecimal;
import java.util.Date;

import org.beetl.sql.core.annotatoin.AutoID;

/**
 * Generated by Blade.
 * 2017-11-07 18:07:53
 */
@Table(name = "glpt_building_owner_rel")
@BindID(name = "id")
@SuppressWarnings("serial")
public class BuildingOwnerRel extends BaseModel {
    	private Long id; //id
    	private Integer modifyuserid; //修改用户id
    	private Integer status; //status
    	private Integer version=0;
    	private Long adduserid; //添加用户ID
    	private String addusername; //添加用户名
    	private BigDecimal area; //拥有建筑面积
    	private Long buildid; //buildid
    	private Long floor; //floor
    	private String modifyusername; //修改用户
    	private Long ownerid; //ownerid
    	private Date addtime; //添加时间
    	private Date modifytime; //修改时间

    	@AutoID
    	public Long getId() {
    		return id;
    	}

    	public void setId(Long id) {
    		this.id = id;
    	}

    	public Integer getModifyuserid() {
    		return modifyuserid;
    	}

    	public void setModifyuserid(Integer modifyuserid) {
    		this.modifyuserid = modifyuserid;
    	}

    	public Integer getStatus() {
    		return status;
    	}

    	public void setStatus(Integer status) {
    		this.status = status;
    	}

    	public Integer getVersion() {
    		return version;
    	}

    	public void setVersion(Integer version) {
    		this.version = version;
    	}

    	public Long getAdduserid() {
    		return adduserid;
    	}

    	public void setAdduserid(Long adduserid) {
    		this.adduserid = adduserid;
    	}

    	public String getAddusername() {
    		return addusername;
    	}

    	public void setAddusername(String addusername) {
    		this.addusername = addusername;
    	}

    	public BigDecimal getArea() {
    		return area;
    	}

    	public void setArea(BigDecimal area) {
    		this.area = area;
    	}

    	public Long getBuildid() {
    		return buildid;
    	}

    	public void setBuildid(Long buildid) {
    		this.buildid = buildid;
    	}

    	public Long getFloor() {
    		return floor;
    	}

    	public void setFloor(Long floor) {
    		this.floor = floor;
    	}

    	public String getModifyusername() {
    		return modifyusername;
    	}

    	public void setModifyusername(String modifyusername) {
    		this.modifyusername = modifyusername;
    	}

    	public Long getOwnerid() {
    		return ownerid;
    	}

    	public void setOwnerid(Long ownerid) {
    		this.ownerid = ownerid;
    	}

    	public Date getAddtime() {
    		return addtime;
    	}

    	public void setAddtime(Date addtime) {
    		this.addtime = addtime;
    	}

    	public Date getModifytime() {
    		return modifytime;
    	}

    	public void setModifytime(Date modifytime) {
    		this.modifytime = modifytime;
    	}

}
