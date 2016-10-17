package com.liusoft.dlog4j.beans;

import java.util.Date;

import com.liusoft.dlog4j.base._BeanBase;

public class AnnexBean extends _BeanBase{
	private int userid;//用户id
	private int siteid;//站点id
	private Date uploadTime;//上传时间
	private int downloadCount;//下载次数
	private int status;//状态
	private long valiDate;//验证码
	private String fileType;//文件类型
	private String url;//文件路径
	private String diskPath;//磁盘路径
	private String fileName;//文件名称
	private String fileDesc;//附件说明
	private String annexName;//附件名称
	private int diaryID;//所属日记
	private int fileSize;//文件大小
	private Date lastDownload;//最后下载时间
	private int extendFiled1;//扩展字段1
	private int extendFiled2;//扩展字段2
	private String extendFiled3;//扩展字段3
	private String extendFiled4;//扩展字段4
	
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
