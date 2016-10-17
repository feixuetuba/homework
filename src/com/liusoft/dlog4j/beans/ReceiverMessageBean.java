package com.liusoft.dlog4j.beans;

import java.util.Date;

/**
 * 收件表
 */
public class ReceiverMessageBean {
	/**好友未读*/
	public static int STATUS_FRIEND_CLOSE=0;
	/**好友已读*/
	public static int STATUS_FRIEND_OPEN=1;
	/**好友已回*/
	public static int STATUS_FRIEND_REVERT=2;
	
	/**陌生人未读*/
	public static int STATUS_STRANGER_CLOSE=3;
	/**陌生人已读*/
	public static int STATUS_STRANGER_OPEN=4;
	/**陌生人已回*/
	public static int STATUS_STRANGER_REVERT=5;
	
	private int receiverid;// 主键
	private int msgid;// 短信息id
	private int userid;// 接收者id
	private int sendid;// 发送者id
	private String sendname;// 发送者名称
	private Date readtime;// 读取时间
	private int status;// 状态

	public ReceiverMessageBean() {
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

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public Date getReadtime() {
		return readtime;
	}

	public void setReadtime(Date readtime) {
		this.readtime = readtime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReceiverid() {
		return receiverid;
	}

	public void setReceiverid(int receiverid) {
		this.receiverid = receiverid;
	}

}
