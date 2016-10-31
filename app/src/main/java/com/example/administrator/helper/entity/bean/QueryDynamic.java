package com.example.administrator.helper.entity.bean;

public class QueryDynamic {
	private int orderFlag;//排序标记
	private Integer pageNo;//第几页；
	private Integer pageSize;//每页大小
	
	
	public QueryDynamic(int orderFlag, Integer pageNo, Integer pageSize) {
		super();
		this.orderFlag = orderFlag;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	public int getOrderFlag() {
		return orderFlag;
	}
	public void setOrderFlag(int orderFlag) {
		this.orderFlag = orderFlag;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
