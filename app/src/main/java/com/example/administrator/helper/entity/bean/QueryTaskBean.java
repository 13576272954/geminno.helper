package com.example.administrator.helper.entity.bean;

public class QueryTaskBean {
    private String city;
	private Integer orderFlag; 
	private int tasktypeid;
	private Integer pageNo; 
	private Integer pageSize; 
	 
	public Integer getOrderFlag() {
		return orderFlag;
	}
	public void setOrderFlag(Integer orderFlag) {
		this.orderFlag = orderFlag;
	}
	
 
	public QueryTaskBean(String city, Integer orderFlag, int tasktypeid, Integer pageNo, Integer pageSize) {
		super();
		this.city = city;
		this.orderFlag = orderFlag;
		this.tasktypeid = tasktypeid;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getTasktypeid() {
		return tasktypeid;
	}
	public void setTasktypeid(int tasktypeid) {
		this.tasktypeid = tasktypeid;
	}
	public QueryTaskBean(  Integer orderFlag, int tasktypeid, Integer pageNo, Integer pageSize) {
		super();
		 
		this.orderFlag = orderFlag;
		this.tasktypeid = tasktypeid;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	public void setTasktype(int tasktypeid) {
		this.tasktypeid = tasktypeid;
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
	 
	@Override
	public String toString() {
		return "QueryTaskBean [city=" + city + ", tasktypeid=" + tasktypeid + ", pageNo=" + pageNo
				+ ", pageSize=" + pageSize + "]";
	}
	public QueryTaskBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QueryTaskBean(String city, int tasktypeid, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated constructor stub
		this.city = city;
		
		this.tasktypeid = tasktypeid;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
 
	

}
