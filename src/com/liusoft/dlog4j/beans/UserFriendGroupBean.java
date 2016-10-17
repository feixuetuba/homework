package com.liusoft.dlog4j.beans;

import java.awt.List;
import java.io.Serializable;
import java.util.Date;

/**
 * �û����ѷ�����Ϣ 
 * @author liqiang
 */
public class UserFriendGroupBean {
	
	public static int TYPE_PUBLICITY=0;//��������
	public static int TYPE_PRIVATE=1;//˽�з���
	public static int TYPE_DEFAULT=2;//Ĭ�Ϸ���
	
	protected int groupid;// ������
	protected int userid;// �����û�
	protected String groupname;// ��������
	protected int grouptype;// ��������
	protected int groupcount;
	
	public UserFriendGroupBean() {
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public int getGrouptype() {
		return grouptype;
	}

	public void setGrouptype(int grouptype) {
		this.grouptype = grouptype;
	}

	public int getGroupcount() {
		return groupcount;
	}

	public void setGroupcount(int groupcount) {
		this.groupcount = groupcount;
	}


}