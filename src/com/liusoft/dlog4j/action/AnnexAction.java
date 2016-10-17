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

	protected int annex_size = Annex_UploadManager.getMax_annex_size();// 文件限制大小
	protected String[] extendName = Annex_UploadManager.getExtendName();// 文件扩展名
	protected String path = Annex_UploadManager.getPath();// 保存路径
	protected String baseURI = Annex_UploadManager.getBaseURI();// 显示 下载 路径

	/**
	 * 上传文件
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
			return msgbox(mapping, form, request, response, "最多只能上传3个文件", fromPage);
		try {

			if (!this.isExtendName(annexForm.getFileName() + ""))// 扩展名判断
				return msgbox(mapping, form, request, response, "文件类型不符合", fromPage);

			if (annexForm.getFileName().getFileSize() > annex_size)// 判断大小
			{
				return msgbox(mapping, form, request, response, "文件大小不得超过5M",
						fromPage);
			}
			if (annex_size == 0 || request.getContentLength() > annex_size) {

				return msgbox(mapping, form, request, response, "文件大小不得超过5M",
						fromPage);
			}
			// 上传文件 返回保存路径
			url = Annex_UploadManager.getFileHandler().save(request, response,
					annexForm.getFileName());
			uri = (String) request.getAttribute("file.path");
			// 将信息写入数据库
			Date newDate = new Date();
			AnnexBean annexBean = new AnnexBean();
			annexBean.setSiteid(annexForm.getSid());
			annexBean.setDiskPath(uri);// 磁盘路径
			annexBean.setSiteid(annexForm.getSid());// 磁盘路径
			annexBean.setFileName(annexForm.getFileName().toString());// 名称文件
			annexBean.setUploadTime(newDate);// 上传时间
			annexBean.setFileSize(annexForm.getFileName().getFileSize());// 文件大小
			annexBean.setUrl(url);// 下载路径
			annexBean.setUserid(annexForm.getUserid());// 所属用户
			annexBean.setAnnexName(annexForm.getFileName().toString());// 附件名称
			annexBean.setValiDate(annexForm.getValiDate());// 验证码
			if (annexForm.getDiaryID()!= 0)
				annexBean.setDiaryID(annexForm.getDiaryID());
			if (annexForm.getSid()!= 0)
				annexBean.setSiteid(annexForm.getSid());
			annexBean.setDownloadCount(0);// 下载次数
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
			return msgbox(mapping, form, request, response, "操作失败,请联系管理员",fromPage);
		}

	}

	/**
	 * 默认方法 上传文件太大 请求中找不到参数eventSubmit_Xxxx 会调用次方法
	 */
	public ActionForward doDefault(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_status) throws Exception {
		String url = request.getParameter("fromPage");
		return null;

	}

	/**
	 * 判断文件扩展名
	 */
	private boolean isExtendName(String filename) {
		for (int i = 0; i < extendName.length; i++) {
			if (filename.endsWith(extendName[i]))
				return true;
		}
		return false;
	}

	/**
	 * 删除附件
	 */
	public ActionForward doDeleteAnnex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_status) throws Exception {
		AnnexForm annexForm = (AnnexForm) form;// formBean
		AnnexBean bean;
		String fromPage =annexForm.getFromPage(); // 返回路径
		try {
			
			bean = (AnnexBean) AnnexDAO.getAnnexBean(AnnexBean.class, annexForm
					.getAnnexID());
			if (bean.getValiDate() != annexForm.getValiDate()
					|| bean.getUserid() != annexForm.getUserid())
				return msgbox(mapping, form, request, response, "操作失败,请联系管理员",
						fromPage);
			remove(bean.getDiskPath());
			AnnexDAO.delete(bean);		
			return new ActionForward(fromPage,true);
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return msgbox(mapping, form, request, response, "操作失败,请联系管理员", fromPage);
		}

	}

	/**
	 * 更新下载统计
	 */
	public ActionForward doStatDownload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_status) throws Exception {
		int anID = Integer.parseInt(request.getParameter("id"));
		AnnexDAO.statDownload(anID);
		// 将文件流发送到客户端
		return this.doDownload(mapping, form, request, response);

	}

	/**
	 * 下载文件
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
		BufferedInputStream bis = null;// 输入流
		InputStream fis = null;// 文件流
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
				// 将文件发送到客户端
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
	 * 删除上传的文件
	 */
	public boolean remove(String diskpath) throws IOException {
		File f = new File(diskpath);
		return f.delete();
	}
}
