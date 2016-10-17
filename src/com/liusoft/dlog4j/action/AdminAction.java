package com.liusoft.dlog4j.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.liusoft.dlog4j.DLOGUserManager;
import com.liusoft.dlog4j.DLOG_LayoutManager;
import com.liusoft.dlog4j.Globals;
import com.liusoft.dlog4j.HttpContext;
import com.liusoft.dlog4j.UserLoginManager;
import com.liusoft.dlog4j.base.FckUploadFileBeanBase;
import com.liusoft.dlog4j.base.LayoutInfo;
import com.liusoft.dlog4j.beans.DiaryBean;
import com.liusoft.dlog4j.beans.FckUploadFileBean;
import com.liusoft.dlog4j.beans.PhotoBean;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.StyleBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.dao.AdminDAO;
import com.liusoft.dlog4j.dao.DAO;
import com.liusoft.dlog4j.dao.FCKUploadFileDAO;
import com.liusoft.dlog4j.dao.PhotoDAO;
import com.liusoft.dlog4j.dao.SiteDAO;
import com.liusoft.dlog4j.dao.UserDAO;

import com.liusoft.dlog4j.formbean.AdminForm;
import com.liusoft.dlog4j.formbean.UserForm;
import com.liusoft.dlog4j.upload.FCK_UploadManager;
import com.liusoft.dlog4j.util.ImageUtils;
import com.liusoft.dlog4j.util.StringUtils;

import dlog.common.search.SearchProxy;

public class AdminAction extends ActionBase {
	private final static Log log = LogFactory.getLog(AdminAction.class);

	/**
	 * �ռ�����
	 */
	public ActionForward doDiaryQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		AdminForm adminForm = (AdminForm) form;
		StringBuffer hql = new StringBuffer();
		Object value[] = new Object[adminForm.getCheck().length];
		setValues(adminForm);
		hql.append(" FROM DiaryBean as d ");
		hql.append(" WHERE d.status=0 ");
		for (int i = 0; i < adminForm.getCheck().length; i++) {
			int checkIndex = Integer.parseInt(adminForm.getCheck()[i]);
			value[i] = values[checkIndex];
			hql.append(spelling(checkIndex));
		}
		hql.append(" ORDER BY d.id DESC ");
		AdminDAO.putCache(adminForm.getCache_key() + "_sql", hql.toString());
		AdminDAO.putCache(adminForm.getCache_key() + "_value", value);
		String path = mapping.findForward("ok").getPath() + "?cache_key="
				+ adminForm.getCache_key();
		return new ActionForward(path, true);
	}

	/**
	 * վ���Ƽ�
	 */
	public ActionForward doSiteRecommend(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.siteRecommend(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ȡ��վ���Ƽ�
	 */
	public ActionForward doCancelSiteRecommend(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.cancelSiteRecommend(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * �ռ��Ƽ�
	 */
	public ActionForward doDiaryRecommend(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.DiaryRecommend(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ȡ���ռ��Ƽ�
	 */
	public ActionForward doCancelDiaryRecommend(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.cancelDiaryRecommend(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * վ������
	 */
	public ActionForward doSiteShield(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.siteShield(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ȡ��վ������
	 */
	public ActionForward doCancelSiteShield(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.cancelSiteShield(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * �ռ�����
	 */
	public ActionForward doDiaryShield(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.diaryShield(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());

	}

	/**
	 * ȡ���ռ�����
	 */
	public ActionForward doCancelDiaryShield(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.cancelDiaryShield(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * �ռ�ɾ��
	 */
	public ActionForward doDiaryDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.diaryDelete(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * վ��ɾ��
	 */
	public ActionForward doSiteDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		if (AdminDAO.siteDelete(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ɾ�������Ƭ
	 */
	public ActionForward doDeleteImg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		HttpContext context = getHttpContext(mapping, form, request, response);

		// ɾ�����
		if (adminForm.getCheck() != null) {
			for (int i = 0; i < adminForm.getCheck().length; i++) {
				PhotoBean pbean = PhotoDAO.getPhotoByID(Integer
						.parseInt(adminForm.getCheck()[i]));
				if (pbean != null) {
					try {

						getPhotoSaver().delete(context, pbean.getImageURL());
						if (!pbean.getPreviewURL().equals(pbean.getImageURL()))
							getPhotoSaver().delete(context,
									pbean.getPreviewURL());
						// ɾ�����ݿ���Ϣ
						PhotoDAO.delete(pbean);
					} catch (Exception e) {
						log.warn("Delete Photo's index failed", e);
						return msgbox(mapping, form, request, response, "ɾ��ʧ��",
								adminForm.getFromPage());
					}
				}
			}
			return msgbox(mapping, form, request, response, "ɾ���ɹ�", adminForm
					.getFromPage());
		}

		PhotoBean pbean = PhotoDAO.getPhotoByID(adminForm.getSid());

		if (pbean != null) {
			try {

				getPhotoSaver().delete(context, pbean.getImageURL());
				if (!pbean.getPreviewURL().equals(pbean.getImageURL()))
					getPhotoSaver().delete(context, pbean.getPreviewURL());
				// ɾ�����ݿ���Ϣ
				PhotoDAO.delete(pbean);
			} catch (Exception e) {
				log.warn("Delete Photo's index failed", e);
				return msgbox(mapping, form, request, response, "ɾ��ʧ��",
						adminForm.getFromPage());
			}
		}
		return msgbox(mapping, form, request, response, "ɾ���ɹ�", adminForm
				.getFromPage());
	}

	/**
	 * ɾ���ռ��е���Ƭ
	 */
	public ActionForward doDeleteDiaryImg(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;
		UserBean loginUser = super.getLoginUser(request, response);
		// ɾ�����
		if (adminForm.getCheck() != null)
			if (FCKUploadFileDAO
					.deleteFileById(adminForm.getCheck(), loginUser)) {
				return msgbox(mapping, form, request, response, "ɾ���ɹ�",
						adminForm.getFromPage());
			}
		if (adminForm.getExtend1() != 0) {
			if (FCKUploadFileDAO.deleteFileById(adminForm.getExtend1(),
					loginUser))
				return msgbox(mapping, form, request, response, "ɾ���ɹ�",
						adminForm.getFromPage());
		}
		return msgbox(mapping, form, request, response, "ɾ��ʧ��", adminForm
				.getFromPage());
	}

	// =================================�޸��û�����=================================
	/**
	 * ����û�ͷ���ļ���·��
	 */
	public final static String PORTRAIT_PATH = "/uploads/portrait/";

	/**
	 * �û�ͷ��������
	 */
	public final static int PORTRAIT_WIDTH = 160;
	/**
	 * ����ͷ��������
	 */
	public final static int REPLY_PORTRAIT_WIDTH = 60;

	/**
	 * �û�ͷ������߶�
	 */
	public final static int PORTRAIT_HEIGHT = 200;
	/**
	 * ����ͷ������߶�
	 */
	public final static int REPLY_PORTRAIT_HEIGHT = 75;
	private static String g_portrait_path;
	private static String g_portrait_uri;

	/**
	 * �û����ϸ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward doUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm user = (AdminForm) form;
		super.validateClientId(request, user);
		ActionMessages msgs = new ActionMessages();
		// ��֤�û����ϱ�
		user.validateUserForm(request, msgs, false);

		if (StringUtils.isNotBlank(user.getMobile())) {
			UserBean uu = UserDAO.getUserByMobile(user.getMobile());
			if (uu != null && uu.getId() != user.getId()) {
				msgs.add("mobile", new ActionMessage("error.mobile_exists"));
			}
		}
		while (msgs.isEmpty()) {
			// ��¼�û�
			UserBean loginUser = super.getLoginUser(request, response);
			if (loginUser == null) {
				msgs.add("user", new ActionMessage("error.user_not_login"));
				break;
			}
			// ��ȡ���޸ĵ��û���Ϣ
			UserBean ubean = UserDAO.getUserByID(user.getId());
			// �жϹ���Ա����
			if (!StringUtils
					.equals(loginUser.getPassword(), user.getPassword())) {
				msgs.add("password",
						new ActionMessage("error.user_auth_failed"));
				break;
			}
			// �����ֹ���
			ubean.setNickname(super.autoFiltrate(null, user.getNickname()));
			// ������ϢBEGIN
			if (user.getBirth() != null && user.getBirth().before(new Date()))
				ubean.setBirth(user.getBirth());
			else if (ubean.getBirth() != null)
				ubean.setBirth(null);
			ubean.setSex(user.getSex());
			if (StringUtils.isNotEmpty(user.getResume()))
				ubean.setResume(super.autoFiltrate(null, StringUtils
						.extractText(user.getResume())));
			else
				ubean.setResume(null);
			if (!StringUtils.equals(ubean.getPassword(), user.getPassword2())
					&& StringUtils.isNotEmpty(user.getPassword2()))
				ubean.setPassword(user.getPassword2());
			if (StringUtils.isNotEmpty(user.getEmail()))
				ubean.setEmail(user.getEmail());
			else
				ubean.setEmail(null);
			if (StringUtils.isNotEmpty(user.getHomePage()))
				ubean.setHomePage(user.getHomePage());
			else
				ubean.setHomePage(null);
			if (StringUtils.isNotEmpty(user.getMobile()))
				ubean.setMobile(user.getMobile());
			else
				ubean.setMobile(null);
			if (StringUtils.isNotEmpty(user.getMsn()))
				ubean.setMsn(user.getMsn());
			else
				ubean.setMsn(null);
			if (StringUtils.isNotEmpty(user.getQq()))
				ubean.setQq(user.getQq());
			else
				ubean.setQq(null);
			if (!StringUtils.equals(ubean.getProvince(), user.getProvince()))
				ubean.setProvince(user.getProvince());

			if (!StringUtils.equals(ubean.getCity(), user.getCity()))
				ubean.setCity(user.getCity());
			// ������ϢEND
			if (user.getRemovePortrait() == 1) {
				// ����ϴ��ļ���Ϣ
				ubean.setPortrait(null);
			} else {
				// ����ͷ��
				String filename[] = handleUserPortrait(ubean.getId(), user
						.getPortrait());
				if (filename != null) {
					String portrait_uri = filename[0];
					String portrait_s_uri = filename[1];
					if (StringUtils.isNotEmpty(portrait_uri)) {
						ubean.setPortrait(portrait_uri);
						ubean.setPortraitIcon(portrait_s_uri);
					}
				}

			}
			try {
				DLOGUserManager.update(ubean);
				// ����session�е��û�����
				// ��ȡ�û����޸��û���session
				// UserLoginManager.updateLoginUser(request, ubean);
				// System.out.println(user.getFromPage());
			} catch (Exception e) {
				msgs.add("result", new ActionMessage("error.database", e
						.getMessage()));
			}
			break;
		}
		if (!msgs.isEmpty()) {
			saveMessages(request, msgs);
			// return new ActionForward(user.getFromPage(), true);
			return msgbox(mapping, form, request, response, msgs.toString(),
					user.getFromPage());
		}
		return msgbox(mapping, form, request, response, "���óɹ�", user
				.getFromPage());
	}

	private synchronized void initPortraitPath() {
		if (g_portrait_uri != null)
			return;
		g_portrait_uri = getServlet().getInitParameter("portrait_base_uri");
		String portrait_path = this.getServlet().getInitParameter(
				"portrait_base_path");

		if (portrait_path.startsWith(Globals.LOCAL_PATH_PREFIX))
			g_portrait_path = portrait_path.substring(Globals.LOCAL_PATH_PREFIX
					.length());
		else if (portrait_path.startsWith("/"))
			g_portrait_path = getServlet().getServletContext().getRealPath(
					portrait_path);
		else
			g_portrait_path = portrait_path;
		if (!g_portrait_path.endsWith(File.separator))
			g_portrait_path += File.separator;
	}

	private String getPortraitPath(String uri) {
		initPortraitPath();
		StringBuffer path = new StringBuffer(g_portrait_path);
		path.append(StringUtils.replace(uri, "/", File.separator));
		return path.toString();
	}

	private final static Object sync_portrait_upload = new Object();

	private String[] handleUserPortrait(int userid, FormFile pFile)
			throws IOException {
		if (pFile == null)
			return null;
		// ��ȡͼ�����չ��
		String extendName = StringUtils.getFileExtend(pFile.getFileName());
		if (StringUtils.isEmpty(extendName))
			return null;
		// �ж��Ƿ�Ϊͼ���ļ�
		if (!ImageUtils.isImage(extendName))
			return null;
		extendName = extendName.toLowerCase();
		String filename[] = new String[2];
		// ����ͼ���ŵ�·�� fileName�û�ͷ�� fileSName����ʱ��ʾ��ͷ��
		StringBuffer fileName = new StringBuffer();
		StringBuffer fileSName = new StringBuffer();
		fileName.append(userid / 10000);
		fileSName.append(userid / 10000);
		fileName.append('/');
		fileSName.append('/');
		fileName.append(userid);
		fileSName.append(userid + "_s");
		fileName.append('.');
		fileSName.append('.');
		fileName.append(extendName);
		fileSName.append(extendName);
		String img_path = getPortraitPath(fileName.toString());
		String img_s_path = getPortraitPath(fileSName.toString());
		File img = new File(img_path);
		File img_s = new File(img_s_path);
		File img_dir = img.getParentFile();
		File img_s_dir = img_s.getParentFile();
		if (!img_dir.exists()) {
			synchronized (sync_portrait_upload) {
				if (!img_dir.mkdirs())
					throw new IOException("Cannot make directory: "
							+ img_dir.getParent());
			}
		}
		if (!img_s_dir.exists()) {
			synchronized (sync_portrait_upload) {
				if (!img_s_dir.mkdirs())
					throw new IOException("Cannot make directory: "
							+ img_s_dir.getParent());
			}
		}
		BufferedImage orig_portrait = (BufferedImage) ImageIO.read(pFile
				.getInputStream());
		int photoSize[] = DlogAction.reckonPhotoSize(orig_portrait.getWidth(),
				orig_portrait.getHeight());
		ImageUtils.createPreviewImage(pFile.getInputStream(), img_path,
				photoSize[0], photoSize[1]);
		ImageUtils.createPreviewImage(pFile.getInputStream(), img_s_path,
				photoSize[2], photoSize[3]);
		filename[0] = fileName.toString();
		filename[1] = fileSName.toString();
		return filename;
	}

	// =================================��ʽ����=================================

	/**
	 * ��ʽ����Ƽ�
	 */
	public ActionForward doRecommendStyle(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;

		if (AdminDAO.styleRecommend(adminForm.getId()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ȡ����ʽ����Ƽ�
	 */
	public ActionForward doCancelRecommendStyle(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;

		if (AdminDAO.cancelRecommendStyle(adminForm.getId()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ������ʽ���
	 */
	public ActionForward doShieldStyle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;

		if (AdminDAO.shieldStyle(adminForm.getId()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ȡ����ʽ�������
	 */
	public ActionForward doCancelShieldStyle(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;

		if (AdminDAO.cancelShieldStyle(adminForm.getId()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * vip
	 */
	public ActionForward doVipStyle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;

		if (AdminDAO.vipStyle(adminForm.getId()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * �����û�
	 */
	public ActionForward doCancelVipStyle(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminForm adminForm = (AdminForm) form;

		if (AdminDAO.cancelVipStyle(adminForm.getId()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ɾ��
	 */
	public ActionForward doDeleteStyle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;

		if (AdminDAO.deleteStyle(adminForm.getId()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * �����ʽ����
	 */
	public ActionForward doCreateStyle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		StyleBean bean = null;
		bean = AdminDAO
				.styleIsPresence(adminForm.getName(), adminForm.getCss());
		if (bean != null) {
			return msgbox(mapping, form, request, response, "�����쳣 : ����Ϣ���ݿ��Ѿ�����");
		}
		// ��鱾�ش�����ʽ�����ļ��Ƿ���Ϲ涨 д�����ݿ�
		if (DLOG_LayoutManager.isStyleExist(adminForm.getName(), adminForm
				.getCss())) {
			// ƥ�� ͼ���ļ�
			if (!DLOG_LayoutManager.isImg(adminForm.getPreview_l_image(),
					adminForm.getName())) {
				return msgbox(mapping, form, request, response, "�����쳣 : ("
						+ adminForm.getPreview_l_image() + ")Ԥ��ͼδ�ҵ�");
			}
			if (!DLOG_LayoutManager.isImg(adminForm.getPreview_s_image(),
					adminForm.getName())) {
				return msgbox(mapping, form, request, response, "�����쳣 : ("
						+ adminForm.getPreview_s_image() + ")Ԥ��ͼδ�ҵ�");
			}
		} else {
			return msgbox(mapping, form, request, response, "δ�ҵ������ļ�("
					+ adminForm.getCss() + ")");
		}

		if (AdminDAO.doCreateStyle(adminForm))
			return msgbox(mapping, form, request, response, "��ӳɹ�");
		return msgbox(mapping, form, request, response, "���ʧ��");
	}

	public ActionForward doEditStyle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		StyleBean bean = null;
		if (!adminForm.getCss().endsWith(".css"))
			adminForm.setCss(adminForm.getCss() + ".css");
		bean = AdminDAO
				.styleIsPresence(adminForm.getName(), adminForm.getCss());
		if (bean != null) {
			if (bean.getId() != adminForm.getId()) {
				adminForm.setStatus(-1);
				adminForm.setExplain("����Ϣ���ݿ��Ѿ�����");
				if (AdminDAO.doEditStyle(adminForm))
					return msgbox(mapping, form, request, response,
							"�����쳣 : ����Ϣ���ݿ��Ѿ�����");
				return msgbox(mapping, form, request, response, "�޸�ʧ��");
			}
		}
		// ��鱾�ش�����ʽ�����ļ��Ƿ���Ϲ涨 д�����ݿ�
		if (DLOG_LayoutManager.isStyleExist(adminForm.getName(), adminForm
				.getCss())) {
			// ƥ�� ͼ���ļ�
			if (!DLOG_LayoutManager.isImg(adminForm.getPreview_l_image(),
					adminForm.getName())) {
				adminForm.setPreview_l_image("Ԥ��ͼδ�ҵ�");
				adminForm.setStatus(-1);
			}
			if (!DLOG_LayoutManager.isImg(adminForm.getPreview_s_image(),
					adminForm.getName())) {
				adminForm.setPreview_s_image("����ͼδ�ҵ�");
				adminForm.setStatus(-1);
			}
		} else {
			adminForm.setStatus(-1);
			adminForm.setExplain("δ�ҵ������ļ�(" + adminForm.getCss() + ")");
			adminForm.setCss(adminForm.getCss());
			adminForm.setPreview_s_image("�޷�ȷ������ͼ");
			adminForm.setPreview_l_image("�޷�ȷ��Ԥ��ͼ");
			if (AdminDAO.doEditStyle(adminForm))
				return msgbox(mapping, form, request, response,
						"�����쳣 : δ�ҵ������ļ�(" + adminForm.getCss() + ")");
		}
		if (AdminDAO.doEditStyle(adminForm))
			return msgbox(mapping, form, request, response, "�޸ĳɹ�");
		return msgbox(mapping, form, request, response, "�޸�ʧ��");
	}

	// ================================Ȩ�޹���============================
	/**
	 * ȡ��Ȩ��
	 */
	public ActionForward doCancelPop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		AdminDAO.moveCache(adminForm.getSid() + "_manager");
		if (AdminDAO.CancelPop(adminForm.getSid()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage());
		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage());
	}

	/**
	 * ����Ȩ��
	 */
	public ActionForward doUpdatePop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminForm adminForm = (AdminForm) form;
		AdminDAO.moveCache(adminForm.getSid() + "_manager");
		if (AdminDAO.UpdatePop(adminForm.getSid(), adminForm.getPopedom(),
				adminForm.getRole()))
			return msgbox(mapping, form, request, response, "���óɹ�", adminForm
					.getFromPage()
					+ "?return=1&pop="
					+ adminForm.getPopedom()
					+ "&role="
					+ adminForm.getRole() + "&rid=" + adminForm.getSid());

		return msgbox(mapping, form, request, response, "����ʧ��", adminForm
				.getFromPage()
				+ "?return=0&rid=" + adminForm.getSid());
	}

	// ===========================================================================
	/**
	 * ƴд��ѯ���
	 */
	protected String spelling(int index) {
		return filed[index];
	}

	/**
	 * �Լ����������ֶ��ֶ�ֵ
	 */
	protected void setValues(AdminForm af) {
		Object obj[] = { getStartDate(af.getStart_create_time()),
				getEndDate(af.getEnd_create_time()),
				getStartDate(af.getStart_reply_time()),
				getEndDate(af.getEnd_reply_time()),
				getStartDate(af.getStart_view_time()),
				getEndDate(af.getEnd_view_time()), af.getTags(), af.getAuthor() };
		values = obj;
	}

	/**
	 * �ռ����������ֶ�����
	 */
	protected String filed[] = {
			" AND d.writeTime>=? ",
			" AND d.writeTime<=? ",
			" AND d.lastReplyTime>=? ",
			" AND d.lastReplyTime<=? ",
			" AND d.lastReadTime>=? ",
			" AND d.lastReadTime<=? ",
			" AND d.id IN (SELECT t.refId FROM TagBean AS t WHERE t.refType=1 AND t.name=?) ",
			" AND d.author=? " };

	/**
	 * ��ѯ�ֶ�
	 */
	protected String sfiled[] = { "d.id", "d.title", "d.writeTime",
			"d.lastReplyTime", "d.lastReadTime", "d.author", "d.friendlyName",
			"d.catalog.name", "d.replyCount", "d.viewCount", "t.name" };

	/**
	 * �ֶ�ֵ����
	 */
	private Object values[];

	/**
	 * �����ַ��� ���ÿ�ʼʱ��
	 */
	protected Date getStartDate(String str) {
		if (str.equals(""))
			return new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date;
		try {
			date = df.parse(str + " 00:00:00");
			return date;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Date();
	}

	/**
	 * �����ַ��� ���ý���ʱ��
	 */
	protected Date getEndDate(String str) {
		if (str.equals(""))
			return new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date;
		try {
			date = df.parse(str + " 23:59:59");
			return date;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Date();
	}

	public static void main(String[] args) {
		DLOG_LayoutManager.getFileList_css("10000");
	}
}
