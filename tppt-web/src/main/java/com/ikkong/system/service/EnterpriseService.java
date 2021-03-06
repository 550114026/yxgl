package com.ikkong.system.service;

import java.util.List;

import com.ikkong.core.base.service.IService;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.model.Enterprise;

/**
 * Generated by Blade.
 * 2017-11-07 17:36:57
 */
public interface EnterpriseService extends IService<Enterprise>{

	/**
	 * 股东类型统计
	 * @param record
	 * @return
	 */
	public List<Record> countByEntHolderType(Record record);
	/**
	 * 搬迁意愿统计
	 * @param record
	 * @return
	 */
	public List<Record> countByEntRelocateType(Record record);
	
	
	/**
	 * 查询企业列表
	 * @param record
	 * @return
	 */
	public List<Record> queryEntList(Record record);
	/**
	 * 查询企业列表
	 * @param record
	 * @return
	 */
	public List<Record> queryEntListNoLimit(Record record);
	
	/**
	 * 查询楼宇内的企业信息
	 * @param record
	 * @return
	 */
	public List<Record> queryEntListByBuilding(Record record);
	
	
	/**
	 * 获取经济指标
	 * @param record
	 * @return
	 */
	public List<Record> queryEntEconomicDataList(Record record);
	
	/**
	 * 获取楼宇经济指标
	 * @param record
	 * @return
	 */
	public Record queryEntEconomicByBuildingid(Record record);
	
	/**
	 * 1.2.3 企业规模统计 按营业收入区间进行归类统计
	 * 
	 * @param record
	 * @return
	 */
	public List<Record> statisticEntByInput(Record record);
	
	/**
	 * 1.2.5	行业类型统计
	 * 按经营类型进行归类统计
	 * @param record
	 * @return
	 */
	public List<Record> statisticEntByBusinessType(Record record) ;
	
	/**
	 * 获取企业信息，用于网页爬取数据
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Record> queryEntToCollectCredit(int start, int size);

	/**
	 * 修改区外注册预警状态
	 * @param enterprise
	 */
	public void updateOutRegistType(Enterprise enterprise);
	
	/**
	 * 批量更新企业楼宇信息
	 */
	public void updateBuildingidByBat(String ids,String buildingid);
	
	public List<Record> countByOwnershipType(Record record);

	public List<Record> querylowProdEntListNoLimit(Record record);

}
