package com.liusoft.dlog4j.beans;

import java.util.Date;

public class AfficheBean {

	private int msgid;//����id
	private int userid;
	private String title;//����
	private String content;//��������
	private Date sendtime;//����ʱ��
	private int status;//״̬
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSendtime() {
		return sendtime;
	}
	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
}
