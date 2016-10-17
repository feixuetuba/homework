package com.liusoft.dlog4j.formbean;

import java.util.Date;

import org.apache.struts.upload.FormFile;

public class AnnexForm extends FormBean {
	private int userid;//用户id
	//private int tableID;//表主键
	private int annexID;//
	private Date uploadTime;//上传时间
	private int downloadCount;//下载次数
	private int status;//状态
	private String fileType;//文件类型
	private String url;//文件路径
	private String fileDesc;//附件说明
	private String annexName;//附件名称
	private int diaryID=0;//所属日记
	private int fileSize;//文件大小
	private long valiDate;//验证
	private FormFile fileName;
	
	
//	
//	public int getTableID() {
//		return tableID;
//	}
//	public void setTableID(int tableID) {
//		this.tableID = tableID;
//	}
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
	public FormFile getFileName() {
		return fileName;
	}
	public void setFileName(FormFile fileName) {
		this.fileName = fileName;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public long getValiDate() {
		return valiDate;
	}
	public void setValiDate(long valiDate) {
		this.valiDate = valiDate;
	}
	public int getAnnexID() {
		return annexID;
	}
	public void setAnnexID(int annexID) {
		this.annexID = annexID;
	}
}
