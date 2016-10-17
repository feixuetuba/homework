package com.liusoft.dlog4j.formbean;

public class FriendForm extends FormBean{
	
	protected int groupid;
	protected int userid;// 所属id
	protected int friendid;//好友id
	protected String groupname;// 分组名称
	protected int grouptype;// 分组类型
	protected String verifyCode;//验证码
	
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
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public int getFriendid() {
		return friendid;
	}
	public void setFriendid(int friendid) {
		this.friendid = friendid;
	}

}
