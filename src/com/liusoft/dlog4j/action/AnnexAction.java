package com.liusoft.dlog4j.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.liusoft.dlog4j.Globals;
import com.liusoft.dlog4j.base.FckUploadFileBeanBase;
import com.liusoft.dlog4j.beans.AnnexBean;
import com.liusoft.dlog4j.beans.DiaryBean;
import com.liusoft.dlog4j.beans.FckUploadFileBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.dao.AnnexDAO;
import com.liusoft.dlog4j.dao.DAO;
import com.liusoft.dlog4j.dao.DiaryDAO;
import com.liusoft.dlog4j.dao.FCKUploadFileDAO;
import com.liusoft.dlog4j.formbean.AnnexForm;
import com.liusoft.dlog4j.formbean.DiaryForm;
import com.liusoft.dlog4j.upload.Annex_UploadManager;
import com.liusoft.dlog4j.upload.FCK_UploadManager;
import com.liusoft.dlog4j.util.RequestUtils;

public class AnnexAction extends ActionBase {

	protected int annex_size = Annex_UploadManager.getMax_annex_size();// �ļ����ƴ�С
	protected String[] extendName = Annex_UploadManager.getExtendName();// �ļ���չ��
	protected String path = Annex_UploadManager.getPath();// ����·��
	protected String baseURI = Annex_UploadManager.getBaseURI();// ��ʾ ���� ·��

	/**
	 * �ϴ��ļ�
	 */
	public ActionForward doUploadAnnex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_status) throws Exception {
		AnnexForm annexForm = (AnnexForm) form;// formBean
		String fromPage=annexForm.getFromPage();
		String url = "";
		String uri = "";		
		long validate=annexForm.getValiDate();
		int userid=annexForm.getUserid();
		int count=AnnexDAO.getdiaryAnnexCount(validate,userid);
		if(count == 3)
			return msgbox(mapping, form, request, response, "���ֻ���ϴ�3���ļ�", fromPage);
		try {

			if (!this.isExtendName(annexForm.getFileName() + ""))// ��չ���ж�
				return msgbox(mapping, form, request, response, "�ļ����Ͳ�����", fromPage);

			if (annexForm.getFileName().getFileSize() > annex_size)// �жϴ�С
			{
				return msgbox(mapping, form, request, response, "�ļ���С���ó���5M",
						fromPage);
			}
			if (annex_size == 0 || request.getContentLength() > annex_size) {

				return msgbox(mapping, form, request, response, "�ļ���С���ó���5M",
						fromPage);
			}
			// �ϴ��ļ� ���ر���·��
			url = Annex_UploadManager.getFileHandler().save(request, response,
					annexForm.getFileName());
			uri = (String) request.getAttribute("file.path");
			// ����Ϣд�����ݿ�
			Date newDate = new Date();
			AnnexBean annexBean = new AnnexBean();
			annexBean.setSiteid(annexForm.getSid());
			annexBean.setDiskPath(uri);// ����·��
			annexBean.setSiteid(annexForm.getSid());// ����·��
			annexBean.setFileName(annexForm.getFileName().toString());// �����ļ�
			annexBean.setUploadTime(newDate);// �ϴ�ʱ��
			annexBean.setFileSize(annexForm.getFileName().getFileSize());// �ļ���С
			annexBean.setUrl(url);// ����·��
			annexBean.setUserid(annexForm.getUserid());// �����û�
			annexBean.setAnnexName(annexForm.getFileName().toString());// ��������
			annexBean.setValiDate(annexForm.getValiDate());// ��֤��
			if (annexForm.getDiaryID()!= 0)
				annexBean.setDiaryID(annexForm.getDiaryID());
			if (annexForm.getSid()!= 0)
				annexBean.setSiteid(annexForm.getSid());
			annexBean.setDownloadCount(0);// ���ش���
			annexBean.setFileType(this.getContentType(annexForm.getFileName()
					.toString()));
			AnnexDAO.save(annexBean);
			if(annexForm.getDiaryID()!=0)
			{
				DiaryBean bean=DiaryDAO.getDiaryByID(annexForm.getDiaryID());
				bean.setAnnex(1);
				DiaryDAO.update(bean, true);
			}
			return new ActionForward(fromPage,true);

		} catch (Exception ex) {
			ex.printStackTrace();
			return msgbox(mapping, form, request, response, "����ʧ��,����ϵ����Ա",fromPage);
		}

	}

	/**
	 * Ĭ�Ϸ��� �ϴ��ļ�̫�� �������Ҳ�������eventSubmit_Xxxx ����ôη���
	 */
	public ActionForward doDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_status) throws Exception {
		String url = request.getParameter("fromPage");
		return null;

	}

	/**
	 * �ж��ļ���չ��
	 */
	private boolean isExtendName(String filename) {
		for (int i = 0; i < extendName.length; i++) {
			if (filename.endsWith(extendName[i]))
				return true;
		}
		return false;
	}

	/**
	 * ɾ������
	 */
	public ActionForward doDeleteAnnex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_status) throws Exception {
		AnnexForm annexForm = (AnnexForm) form;// formBean
		AnnexBean bean;
		String fromPage =annexForm.getFromPage(); // ����·��
		try {
			
			bean = (AnnexBean) AnnexDAO.getAnnexBean(AnnexBean.class, annexForm
					.getAnnexID());
			if (bean.getValiDate() != annexForm.getValiDate()
					|| bean.getUserid() != annexForm.getUserid())
				return msgbox(mapping, form, request, response, "����ʧ��,����ϵ����Ա",
						fromPage);
			remove(bean.getDiskPath());
			AnnexDAO.delete(bean);		
			return new ActionForward(fromPage,true);
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return msgbox(mapping, form, request, response, "����ʧ��,����ϵ����Ա", fromPage);
		}

	}

	/**
	 * ��������ͳ��
	 */
	public ActionForward doStatDownload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_status) throws Exception {
		int anID = Integer.parseInt(request.getParameter("id"));
		AnnexDAO.statDownload(anID);
		// ���ļ������͵��ͻ���
		return this.doDownload(mapping, form, request, response);

	}

	/**
	 * �����ļ�
	 */
	private ActionForward doDownload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AnnexForm annexForm = (AnnexForm) form;
		AnnexBean annexBean = AnnexDAO.findById(annexForm.getId());
		if (annexBean == null)
		{
			response.getWriter().print("");
			return null;
		}	
		BufferedInputStream bis = null;// ������
		InputStream fis = null;// �ļ���
		try {
			response.addHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(annexBean.getFileName(), "utf-8"));
			response.setContentType(annexBean.getFileType());
			// response.setContentType("application/x-download");
			fis = new FileInputStream(annexBean.getDiskPath());
			bis = new BufferedInputStream(fis);
			int bytesRead = 0;
			byte[] buffer = new byte[annexBean.getFileSize()];
			while ((bytesRead = bis.read(buffer)) != -1) {
				// ���ļ����͵��ͻ���
				response.getOutputStream().write(buffer, 0, bytesRead);
			}
			response.flushBuffer();
		} catch (Exception ex) {
			ex.printStackTrace();
			response.reset();
		}
		try {
			if (fis != null) {
				fis.close();
			}
			if (bis != null) {
				bis.close();
			}
		} catch (IOException e) {
			System.err.print(e);
		}

		return null;
	}

	private String getContentType(String fileName) {
		String fileNameTmp = fileName.toLowerCase();
		String ret = "";
		if (fileNameTmp.endsWith("txt")) {
			ret = "text/plain";
		}
		if (fileNameTmp.endsWith("gif")) {
			ret = "image/gif";
		}
		if (fileNameTmp.endsWith("jpg")) {
			ret = "image/jpeg";
		}
		if (fileNameTmp.endsWith("jpeg")) {
			ret = "image/jpeg";
		}
		if (fileNameTmp.endsWith("jpe")) {
			ret = "image/jpeg";
		}
		if (fileNameTmp.endsWith("zip")) {
			ret = "application/zip";
		}
		if (fileNameTmp.endsWith("rar")) {
			ret = "application/rar";
		}
		if (fileNameTmp.endsWith("doc")) {
			ret = "application/msword";
		}
		if (fileNameTmp.endsWith("ppt")) {
			ret = "application/vnd.ms-powerpoint";
		}
		if (fileNameTmp.endsWith("xls")) {
			ret = "application/vnd.ms-excel";
		}
		if (fileNameTmp.endsWith("html")) {
			ret = "text/html";
		}
		if (fileNameTmp.endsWith("htm")) {
			ret = "text/html";
		}
		if (fileNameTmp.endsWith("tif")) {
			ret = "image/tiff";
		}
		if (fileNameTmp.endsWith("tiff")) {
			ret = "image/tiff";
		}
		if (fileNameTmp.endsWith("pdf")) {
			ret = "application/pdf";
		}
		return "application/x-download";
	}

	/**
	 * ɾ���ϴ����ļ�
	 */
	public boolean remove(String diskpath) throws IOException {
		File f = new File(diskpath);
		return f.delete();
	}
}
