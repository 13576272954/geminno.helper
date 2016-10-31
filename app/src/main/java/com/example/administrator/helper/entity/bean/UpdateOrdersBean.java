package com.example.administrator.helper.entity.bean;

 

public class UpdateOrdersBean {
//	update orders,task set receiveUser_id=7,task_status_id=4,taskStatus=3 where orders.id=1 and task.id=1;
Integer Taskid;
Integer userid;
 

public UpdateOrdersBean(Integer Taskid, Integer userid) {
	super();
	this.Taskid = Taskid;
	this.userid = userid;
}


@Override
public String toString() {
	return "UpdateOrdersBean [Taskid=" + Taskid + ", userid=" + userid + "]";
}


public Integer getTaskid() {
	return Taskid;
}


public void setTaskid(Integer Taskid) {
	this.Taskid = Taskid;
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
