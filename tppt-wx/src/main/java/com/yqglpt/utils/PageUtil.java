package com.yqglpt.utils;

import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("unchecked")
public class PageUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private int rowCount;// 数据库一共有多少行
	private int navCount;// 计算出一共有多少页
	private int startRow;// sql语句从哪一行开始查
	private int next;// 下一页
	private int prev;// 上一页
	private int begin;// 导航起始
	private int end;// 导航结束
	private int num;// 当前页
	private int first = 1;// 第一页
	private int last;// 最后一页
	private int navNum = 5;// 始终有5个导航
	private List list;// 分页之后的数据

	public PageUtil(Page page) {
		this.rowCount = page.getTotalRow();
		this.navCount = (int) Math.ceil(this.rowCount * 1.0
				/ page.getPageSize());
		this.last = this.navCount;
		this.num = Math.max(this.first, page.getPageNumber());// 限制当前页范围
		this.num = Math.min(this.last, this.num);
		this.startRow = (this.num - 1) * page.getPageSize();
		this.next = Math.min(this.last, this.num + 1);// 计算下一页
		this.prev = Math.max(this.first, this.num - 1);// 计算上一页
		this.begin = Math.max(this.num - navNum / 2, this.first);
		this.end = Math.min(this.begin + this.navNum - 1, this.last);
		if (end - begin < 4) {
			begin = Math.max(this.first, this.end - this.navNum + 1);// pagetaglib
		}
		this.list = (List) JFinalRender.handleObject(page.getList(),
				JFinalRender.DEPTH);
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getPrev() {
		return prev;
	}

	public void setPrev(int prev) {
		this.prev = prev;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getNavCount() {
		return navCount;
	}

	public void setNavCount(int navCount) {
		this.navCount = navCount;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public int getNavNum() {
		return navNum;
	}

	public void setNavNum(int navNum) {
		this.navNum = navNum;
	}

}
