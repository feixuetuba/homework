package com.liusoft.dlog4j.beans;

import java.util.Date;

public class SendMessageBean {


	/**���� SEND>0 �ظ�������һ��*/
	public static int SEND=0;
	
	private int sendid;//����
	private int msgid;//����Ϣid
	private int userid;//������id
	private int receiverid;//������id
	private String receivername;//����������
	private Date sendtime;//����ʱ��
	private int status;//״̬
	
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
