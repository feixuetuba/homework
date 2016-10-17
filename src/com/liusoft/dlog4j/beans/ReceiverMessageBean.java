package com.liusoft.dlog4j.beans;

import java.util.Date;

/**
 * �ռ���
 */
public class ReceiverMessageBean {
	/**����δ��*/
	public static int STATUS_FRIEND_CLOSE=0;
	/**�����Ѷ�*/
	public static int STATUS_FRIEND_OPEN=1;
	/**�����ѻ�*/
	public static int STATUS_FRIEND_REVERT=2;
	
	/**İ����δ��*/
	public static int STATUS_STRANGER_CLOSE=3;
	/**İ�����Ѷ�*/
	public static int STATUS_STRANGER_OPEN=4;
	/**İ�����ѻ�*/
	public static int STATUS_STRANGER_REVERT=5;
	
	private int receiverid;// ����
	private int msgid;// ����Ϣid
	private int userid;// ������id
	private int sendid;// ������id
	private String sendname;// ����������
	private Date readtime;// ��ȡʱ��
	private int status;// ״̬

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
