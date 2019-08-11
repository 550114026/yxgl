package com.yqglpt.common;

import java.io.Serializable;
import java.util.List;


public class ResultObj<T> implements Serializable{
	/**
	 * true: 成功  false: 失败
	 */
	private boolean isSuccess;

	/**
	 * 返回编码
	 */
	private String resultCode;

	/**
	 * 返回信息
	 */
	private String resultMessage;

	/**
	 * 数据集合
	 */
	private List<T> list;


	/**
	 * 单个对象
	 */
	private T object;

	public static <T> ResultObj<T> newInstance() {
		ResultObj<T> ResultObj = new ResultObj<T>();
		ResultObj.setSuccess(true);
		return ResultObj;
	}
	
	public static <T> ResultObj<T> newFailInstance() {
		ResultObj<T> ResultObj = new ResultObj<T>();
		ResultObj.setSuccess(false);
		return ResultObj;
	}
	
	public static <T> ResultObj<T> newFailInstance(String resultCode, String resultMessage) {
		ResultObj<T> ResultObj = new ResultObj<T>();
		ResultObj.setSuccess(false);
		ResultObj.setResultCode(resultCode);
		ResultObj.setResultMessage(resultMessage);
		return ResultObj;
	}
	
	public static <T> ResultObj<T> newFailInstance(String resultCode, Exception e) {
		e.printStackTrace();
		return newFailInstance(resultCode, e.getMessage());
	}
	
	
	public static <T> ResultObj<T> newInstance(List<T> list) {
		ResultObj<T> ResultObj = new ResultObj<T>();
		if (list != null) {
			ResultObj.setSuccess(true);
			ResultObj.setList(list);
		}
		return ResultObj;
	}
	
	public static <T> ResultObj<T> newInstance(T object) {
		ResultObj<T> ResultObj = new ResultObj<T>();
		if (object != null) {
			ResultObj.setObject(object);
			ResultObj.setSuccess(true);
		}
		return ResultObj;
	}
	
	/**
	 * true: 成功  false: 失败
	 * @return
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	
	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
}
