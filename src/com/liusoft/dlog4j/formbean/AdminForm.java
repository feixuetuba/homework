package com.liusoft.dlog4j.formbean;

import java.util.Date;

import org.apache.struts.upload.FormFile;

import com.liusoft.dlog4j.beans.UserBean;

public class AdminForm extends UserForm {
	// ==========搜索日记用参数============
	private String start_create_time;
	private String end_create_time;
	private String start_view_time;
	private String end_view_time;
	private String start_reply_time;
	private String end_reply_time;
	private String tags;
	private String author;
	private String check[];
	private long cache_key;
	
	// ========页面用参数===========
	private int p_page;
	private int status;
	private int selected;
	
	// =========添加样式参数=======
	String childName;// 子模板名称
	String css;// css文件
	String preview_l_image = "";// 预览图
	String preview_s_image = "";// 缩略图
	String explain;// 说明
	int type;// 类型
	String vip;// vip
	int extend1;// 扩展字段1 int
	int extend2;// 扩展字段2 int
	String extend3;// 扩展字段3 varchar(200)
	String extend4;// 扩展字段4 varchar(200)
	
	//==========权限=============
	int popedom=UserBean.POPEDOM_COMMON;//权限
	private int role =UserBean.ROLE_COMMON;//角色	
	
	public String getPreview_l_image() {
		if (css.endsWith(".css") && preview_l_image.equals(""))
			return css.substring(0, css.indexOf(".")) + ".gif";
		return preview_l_image;
	}

	public String getPreview_s_image() {
		if (css.endsWith(".css") && preview_s_image.equals(""))
			return css.substring(0, css.indexOf(".")) + "_s.gif";
		return preview_s_image;
	}

	public int getP_page() {
		return p_page;
	}

	public void setP_page(int p_page) {
		this.p_page = p_page;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public int getSid() {
		return super.getSid();
	}

	public void setSid(int sid) {
		super.setSid(sid);
	}

	public long getCache_key() {
		return cache_key;
	}

	public void setCache_key(long cache_key) {
		this.cache_key = cache_key;
	}

	public String getStart_create_time() {
		return start_create_time;
	}

	public void setStart_create_time(String start_create_time) {
		this.start_create_time = start_create_time;
	}

	public String getEnd_create_time() {
		return end_create_time;
	}

	public void setEnd_create_time(String end_create_time) {
		this.end_create_time = end_create_time;
	}

	public String getStart_view_time() {
		return start_view_time;
	}

	public void setStart_view_time(String start_view_time) {
		this.start_view_time = start_view_time;
	}

	public String getEnd_view_time() {
		return end_view_time;
	}

	public void setEnd_view_time(String end_view_time) {
		this.end_view_time = end_view_time;
	}

	public String getStart_reply_time() {
		return start_reply_time;
	}

	public void setStart_reply_time(String start_reply_time) {
		this.start_reply_time = start_reply_time;
	}

	public String getEnd_reply_time() {
		return end_reply_time;
	}

	public void setEnd_reply_time(String end_reply_time) {
		this.end_reply_time = end_reply_time;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String[] getCheck() {
		return check;
	}

	public void setCheck(String[] check) {
		this.check = check;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public int getExtend1() {
		return extend1;
	}

	public void setExtend1(int extend1) {
		this.extend1 = extend1;
	}

	public int getExtend2() {
		return extend2;
	}

	public void setExtend2(int extend2) {
		this.extend2 = extend2;
	}

	public String getExtend3() {
		return extend3;
	}

	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}

	public String getExtend4() {
		return extend4;
	}

	public void setExtend4(String extend4) {
		this.extend4 = extend4;
	}

	public void setPreview_l_image(String preview_l_image) {
		this.preview_l_image = preview_l_image;
	}

	public void setPreview_s_image(String preview_s_image) {
		this.preview_s_image = preview_s_image;
	}

	public int getPopedom() {
		return popedom;
	}

	public void setPopedom(int popedom) {
		this.popedom = popedom;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

}
