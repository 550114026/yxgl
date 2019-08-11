/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ikkong.system.model;

import java.math.BigDecimal;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.Table;

import com.ikkong.core.annotation.BindID;
import com.ikkong.core.model.BaseModel;

@Table(name = "tfw_dept")
@BindID(name = "id")
@SuppressWarnings("serial")
public class Dept extends BaseModel {
	private Integer id; //主键
	private String fullname; //全称
	private Integer num; //排序号
	private int pid; //上级部门
	private String simplename; //简称
	private String tips; //备注
	private int version; //版本号
	private String maparea; //备注
	private int isarea;//

	private BigDecimal latitude; //纬度
	private BigDecimal longitude; //经度
	

	@AutoID
	@SeqID(name = "SEQ_DEPT")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getSimplename() {
		return simplename;
	}

	public void setSimplename(String simplename) {
		this.simplename = simplename;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public int getIsarea() {
		return isarea;
	}

	public void setIsarea(int isarea) {
		this.isarea = isarea;
	}

	public String getMaparea() {
		return maparea;
	}

	public void setMaparea(String maparea) {
		this.maparea = maparea;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

}
