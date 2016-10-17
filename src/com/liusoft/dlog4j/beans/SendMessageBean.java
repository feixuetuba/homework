package com.liusoft.dlog4j.beans;

import java.util.Date;

public class SendMessageBean {


	/**发送 SEND>0 回复的是那一条*/
	public static int SEND=0;
	
	private int sendid;//主键
	private int msgid;//短消息id
	private int userid;//发送者id
	private int receiverid;//接受者id
	private String receivername;//接收者名称
	private Date sendtime;//发送时间
	private int status;//状态
	
	public Date getSendtime() {
		return sendtime;
	}
	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getMsgid() {
		return msgid;
	}
	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getSendid() {
		return sendid;
	}
	public void setSendid(int sendid) {
		this.sendid = sendid;
	}
	public int getReceiverid() {
		return receiverid;
	}
	public void setReceiverid(int receiverid) {
		this.receiverid = receiverid;
	}
	public String getReceivername() {
		return receivername;
	}
	public void setReceivername(String receivername) {
		this.receivername = receivername;
	}

	
}
