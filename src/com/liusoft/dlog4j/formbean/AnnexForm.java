package com.liusoft.dlog4j.formbean;

import java.util.Date;

import org.apache.struts.upload.FormFile;

public class AnnexForm extends FormBean {
	private int userid;//�û�id
	//private int tableID;//������
	private int annexID;//
	private Date uploadTime;//�ϴ�ʱ��
	private int downloadCount;//���ش���
	private int status;//״̬
	private String fileType;//�ļ�����
	private String url;//�ļ�·��
	private String fileDesc;//����˵��
	private String annexName;//��������
	private int diaryID=0;//�����ռ�
	private int fileSize;//�ļ���С
	private long valiDate;//��֤
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
