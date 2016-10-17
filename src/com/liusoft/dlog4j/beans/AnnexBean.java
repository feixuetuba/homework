package com.liusoft.dlog4j.beans;

import java.util.Date;

import com.liusoft.dlog4j.base._BeanBase;

public class AnnexBean extends _BeanBase{
	private int userid;//�û�id
	private int siteid;//վ��id
	private Date uploadTime;//�ϴ�ʱ��
	private int downloadCount;//���ش���
	private int status;//״̬
	private long valiDate;//��֤��
	private String fileType;//�ļ�����
	private String url;//�ļ�·��
	private String diskPath;//����·��
	private String fileName;//�ļ�����
	private String fileDesc;//����˵��
	private String annexName;//��������
	private int diaryID;//�����ռ�
	private int fileSize;//�ļ���С
	private Date lastDownload;//�������ʱ��
	private int extendFiled1;//��չ�ֶ�1
	private int extendFiled2;//��չ�ֶ�2
	private String extendFiled3;//��չ�ֶ�3
	private String extendFiled4;//��չ�ֶ�4
	
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public int getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileDesc() {
		return fileDesc;
	}
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
	public String getAnnexName() {
		return annexName;
	}
	public void setAnnexName(String annexName) {
		this.annexName = annexName;
	}
	public int getDiaryID() {
		return diaryID;
	}
	public void setDiaryID(int diaryID) {
		this.diaryID = diaryID;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public Date getLastDownload() {
		return lastDownload;
	}
	public void setLastDownload(Date lastDownload) {
		this.lastDownload = lastDownload;
	}
	public int getExtendFiled1() {
		return extendFiled1;
	}
	public void setExtendFiled1(int extendFiled1) {
		this.extendFiled1 = extendFiled1;
	}
	public int getExtendFiled2() {
		return extendFiled2;
	}
	public void setExtendFiled2(int extendFiled2) {
		this.extendFiled2 = extendFiled2;
	}
	public String getExtendFiled3() {
		return extendFiled3;
	}
	public void setExtendFiled3(String extendFiled3) {
		this.extendFiled3 = extendFiled3;
	}
	public String getExtendFiled4() {
		return extendFiled4;
	}
	public void setExtendFiled4(String extendFiled4) {
		this.extendFiled4 = extendFiled4;
	}
	public long getValiDate() {
		return valiDate;
	}
	public void setValiDate(long valiDate) {
		this.valiDate = valiDate;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getDiskPath() {
		return diskPath;
	}
	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}
	
	public int getSiteid() {
		return siteid;
	}
	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}
	
}
