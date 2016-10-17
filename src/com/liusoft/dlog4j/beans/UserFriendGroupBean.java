package com.liusoft.dlog4j.beans;

import java.awt.List;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户好友分组信息 
 * @author liqiang
 */
public class UserFriendGroupBean {
	
	public static int TYPE_PUBLICITY=0;//公开分组
	public static int TYPE_PRIVATE=1;//私有分组
	public static int TYPE_DEFAULT=2;//默认分组
	
	protected int groupid;// 分组编号
	protected int userid;// 所属用户
	protected String groupname;// 分组名称
	protected int grouptype;// 分组类型
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