package com.liusoft.dlog4j.beans;

import java.util.Date;

import com.liusoft.dlog4j.base._BeanBase;

public class StyleBean extends _BeanBase{
	public static final int STYLE_STATUS_CLOSE=1;//�ر� ����ʾ
	public static final int STYLE_STATUS_OPEN=0;//���� ��ʾ
	public static final int STYLE_STATUS_ERROR=-1;//�쳣״̬
	
	public static final int STYLE_TYPE_VIP=1;//vip �û� 
	public static final int STYLE_TYPE_COMMON=0;//��ͨ�û�
	
	String name;//��ģ������
	String childName;//��ģ������
	String css;//css�ļ�
	String preview_l_image;//Ԥ��ͼ
	String preview_s_image;//����ͼ
	String explain;//˵��
	int status;//״̬
	int type;//����
	int level;//�Ƽ�
	int count;//ͳ��
	String vip;//vip
	Date createTime;//vip
	int extend1;//��չ�ֶ�1 int
	int extend2;//��չ�ֶ�2 int
	String extend3;//��չ�ֶ�3 varchar(200)
	String extend4;//��չ�ֶ�4 varchar(200)
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}
	public String getPreview_l_image() {
		return preview_l_image;
	}
	public void setPreview_l_image(String preview_l_image) {
		this.preview_l_image = preview_l_image;
	}
	public String getPreview_s_image() {
		return preview_s_image;
	}
	public void setPreview_s_image(String preview_s_image) {
		this.preview_s_image = preview_s_image;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	
	

}
