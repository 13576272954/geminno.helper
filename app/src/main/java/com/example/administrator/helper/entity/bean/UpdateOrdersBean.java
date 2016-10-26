package com.example.administrator.helper.entity.bean;

 

public class UpdateOrdersBean {
//	update orders,task set receiveUser_id=7,task_status_id=4,taskStatus=3 where orders.id=1 and task.id=1;
Integer taskid;
Integer userid;
 

public UpdateOrdersBean(Integer taskid, Integer userid) {
	super();
	this.taskid = taskid;
	this.userid = userid;
}


@Override
public String toString() {
	return "UpdateOrdersBean [orderid=" + taskid + ", userid=" + userid + "]";
}


public Integer getTaskid() {
	return taskid;
}


public void setTaskid(Integer taskid) {
	this.taskid = taskid;
}


public Integer getUserid() {
	return userid;
}


public void setUserid(Integer userid) {
	this.userid = userid;
}


public UpdateOrdersBean() {
	super();
	// TODO Auto-generated constructor stub
}


 
}
