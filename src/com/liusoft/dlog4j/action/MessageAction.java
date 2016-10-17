/*
 *  MessageAction.java
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 *  Author: Winter Lau
 *  http://dlog4j.sourceforge.net
 *  
 */
package com.liusoft.dlog4j.action;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;

import com.liusoft.dlog4j.DLOGSecurityManager;
import com.liusoft.dlog4j.beans.AfficheBean;
import com.liusoft.dlog4j.beans.MessageBean;
import com.liusoft.dlog4j.beans.ReceiverMessageBean;
import com.liusoft.dlog4j.beans.SendMessageBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.dao.MessageDAO;
import com.liusoft.dlog4j.dao.UserDAO;
import com.liusoft.dlog4j.formbean.MessageForm;
import com.liusoft.dlog4j.util.RequestUtils;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * ������ص�Action��
 * 
 * @author Winter Lau
 */
public class MessageAction extends ActionBase {

	/**
	 * ɾ�������Ѷ�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward doDeleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_status) throws Exception {
		MessageForm msg = (MessageForm) form;
		UserBean loginUser = super.getLoginUser(request, response);
		if (loginUser != null) {
			try {
				int status = Integer.parseInt(s_status);
				MessageDAO.deleteMsgs(loginUser.getId(), status);
			} catch (Exception e) {
				context().log(
						"delete message where status is " + s_status
								+ " failed.", e);
			}
		}
		return makeForward(mapping.findForward("msgs"), msg.getSid());
	}

	/**
	 * ɾ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward doDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String s_msg_id) throws Exception {
		MessageForm msg = (MessageForm) form;
		UserBean loginUser = super.getLoginUser(request, response);
		if (loginUser != null) {
			try {
				int msg_id = Integer.parseInt(s_msg_id);
				MessageDAO.deleteMsg(loginUser.getId(), msg_id);
			} catch (Exception e) {
				context().log("delete message #" + s_msg_id + " failed.", e);
			}
		}
		String ext = null;
		int page = RequestUtils.getParam(request, "p", -1);
		if (page > 1) {
			ext = "p=" + page;
		}
		return makeForward(mapping.findForward("msgs"), msg.getSid(), ext);
	}

	/**
	 * ɾ��ѡ�е�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward doDeleteMessages(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm msg = (MessageForm) form;
		UserBean loginUser = super.getLoginUser(request, response);
		if (loginUser != null) {
			String[] mids = request.getParameterValues("mid");
			MessageDAO.deleteMsgs(loginUser.getId(), mids);
		}
		return makeForward(mapping.findForward("msgs"), msg.getSid());
	}

	/**
	 * �������� TODO: ��η�ֹȺ��������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	protected ActionForward doSendMsg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// System.out.println("doSendMsg");
		MessageForm msgform = (MessageForm) form;
		super.validateClientId(request, msgform);
		ActionMessages msgs = new ActionMessages();
		while (true) {
			if (msgform.getExpiredTime() != null
					&& msgform.getExpiredTime().before(new Date())) {
				msgs.add("message", new ActionMessage(
						"error.expired_time_not_available"));
				break;
			}
			if (StringUtils.isEmpty(msgform.getContent())) {
				msgs.add("content", new ActionMessage("error.empty_content"));
				break;
			}
			if (msgform.getReceiverId() == 0 || msgform.getSid() == 0) {
				msgs.add("message", new ActionMessage("error.param"));
				break;
			}
			UserBean loginUser = super.getLoginUser(request, response);// ��ǰ�û�
			if (loginUser == null) {
				msgs.add("message", new ActionMessage("error.user_not_login"));
				break;
			} else if (loginUser.getStatus() != UserBean.STATUS_NORMAL) {
				msgs.add("message", new ActionMessage(
						"error.user_not_available"));
				break;
			}
			final UserBean receiver = UserDAO.getUserByID(msgform
					.getReceiverId());// ������
			if (receiver == null
					|| receiver.getStatus() != UserBean.STATUS_NORMAL) {
				msgs.add("message", new ActionMessage(
						"error.user_not_available"));
				break;
			}
			// �жϽ������Ƿ��Ѿ��������߼�Ϊ������
			String content = super.filterScriptAndStyle(StringUtils.abbreviate(
					super.autoFiltrate(null, msgform.getContent()),
					MAX_MESSAGE_LENGTH));

			ArrayList<UserBean> ccUsers = new ArrayList<UserBean>();
			// �жϷ������Ƿ��ڽ����ߵĺ������� && �ж��Ƿ���Լ���
			if (!UserDAO.isUserInBlackList(receiver.getId(), loginUser.getId())
					&& loginUser.getId() != receiver.getId())
				ccUsers.add(receiver);
			try {
				String[] ccIds = request.getParameterValues("ccId");

				if (ccIds != null) {
					// TODO: �˴������ܸĽ������
					for (String sccId : ccIds) {
						int ccId = NumberUtils.toInt(sccId, -1);
						if (ccId <= 0)
							continue;
						UserBean ub = new UserBean(ccId);
						if (ccUsers.contains(ub))
							continue;
						if (UserDAO.getFriend(loginUser.getId(), ccId) != null
								&& !UserDAO.isUserInBlackList(ccId, loginUser
										.getId())) {
							ccUsers.add(ub);
						}
					}
				}
				if (ccUsers.size() > 0)
					MessageDAO.SendMsgs(loginUser, ccUsers, content);
				msgs.add("message", new ActionMessage("message.sent"));
			} catch (HibernateException e) {
				context().log("undelete diary failed.", e);
				msgs.add("message", new ActionMessage("error.database", e
						.getMessage()));
			} finally {
				if (ccUsers != null) {
					ccUsers.clear();
					ccUsers = null;
				}
			}
			break;
		}

		if (!msgs.isEmpty()) {
			saveMessages(request, msgs);

			if (msgform.getFromPage() != null)
				return msgbox(mapping, form, request, response,
						msgs.toString(), msgform.getFromPage());
			return mapping.findForward("send");
		}
		return mapping.findForward("sendok");
	}

	/**
	 * �ظ����Բ�ɾ�����ظ�����Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward doReplyMsgAndDeleteOld(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm msgform = (MessageForm) form;
		super.validateClientId(request, msgform);
		ActionMessages msgs = new ActionMessages();
		while (true) {
			if (msgform.getExpiredTime() != null
					&& msgform.getExpiredTime().before(new Date())) {
				msgs.add("message", new ActionMessage(
						"error.expired_time_not_available"));
				break;
			}
			if (StringUtils.isEmpty(msgform.getContent())) {
				msgs.add("content", new ActionMessage("error.empty_content"));
				break;
			}
			if (msgform.getReceiverId() == 0 || msgform.getSid() == 0) {
				msgs.add("message", new ActionMessage("error.param"));
				break;
			}
			UserBean loginUser = super.getLoginUser(request, response);
			if (loginUser == null) {
				msgs.add("message", new ActionMessage("error.user_not_login"));
				break;
			} else if (loginUser.getStatus() != UserBean.STATUS_NORMAL) {
				msgs.add("message", new ActionMessage(
						"error.user_not_available"));
				break;
			} else if (DLOGSecurityManager.IllegalGlossary
					.existIllegalWord(msgform.getContent())) {
				msgs
						.add("message", new ActionMessage(
								"error.illegal_glossary"));
				break;
			}
			UserBean receiver = UserDAO.getUserByID(msgform.getReceiverId());
			if (receiver == null
					|| receiver.getStatus() != UserBean.STATUS_NORMAL) {
				msgs.add("message", new ActionMessage(
						"error.user_not_available"));
				break;
			}
			// �жϽ������Ƿ��Ѿ��������߼�Ϊ������
			if (UserDAO.isUserInBlackList(receiver.getId(), loginUser.getId())) {
				msgs.add("message", new ActionMessage("message.sent"));
				break;
			}
			MessageBean msgbean = new MessageBean();
			String content = StringUtils.abbreviate(super.autoFiltrate(null,
					msgform.getContent()), MAX_MESSAGE_LENGTH);
			msgbean.setContent(super.filterScriptAndStyle(content));
			msgbean.setExpiredTime(msgform.getExpiredTime());
			msgbean.setFromUser(loginUser);
			msgbean.setToUser(receiver);
			msgbean.setStatus(MessageBean.STATUS_NEW);
			msgbean.setSendTime(new Date());
			try {
				MessageDAO.replyAndDeleteMessage(msgform.getMsgID(), msgbean);
				msgs.add("message", new ActionMessage("message.sent"));
			} catch (HibernateException e) {
				context().log("undelete diary failed.", e);
				msgs.add("message", new ActionMessage("error.database", e
						.getMessage()));
			}
			break;
		}

		if (!msgs.isEmpty()) {
			saveMessages(request, msgs);
			return mapping.findForward("send");
		}

		return mapping.findForward("sendok");
	}

	// =============DLOG 3.5===============

	/**
	 * �ظ���Ϣ
	 */
	protected ActionForward doRevertMessage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MessageForm messageForm = (MessageForm) form;
		super.validateClientId(request, messageForm);
		UserBean loginUser = super.getLoginUser(request, response);
		ActionMessages msgs = new ActionMessages();
		// ����
		String content = super.filterScriptAndStyle(StringUtils.abbreviate(
				super.autoFiltrate(null, messageForm.getContent()),
				MAX_MESSAGE_LENGTH));
		// messageForm.setContent(content);
		while (true) {
			// �Ƿ�Ϊ����
			if (UserDAO.getFriend(messageForm.getReceiverId(), loginUser
					.getId()) != null
					&& !UserDAO.isUserInBlackList(messageForm.getReceiverId(),
							loginUser.getId())) {
				UserBean receiver = UserDAO.getUserByID(messageForm
						.getReceiverId());// ��ȡ�û�
				MessageDAO.RevertMsgToAllTable(loginUser, receiver,
						messageForm, true);// �Ǻ���
				break;
			} else {
				if (loginUser.getId() == messageForm.getReceiverId()) {

					break;
				} else {

					UserBean receiver = UserDAO.getUserByID(messageForm
							.getReceiverId());// ��ȡ�û�
					MessageDAO.RevertMsgToAllTable(loginUser, receiver,
							messageForm, false);// İ����
					break;
				}
			}

		}
		if (!msgs.isEmpty()) {
			saveMessages(request, msgs);
			return msgbox(mapping, form, request, response,
					"REVERT_MESSAGE_ERROR", messageForm.getFromPage());
		}
		return msgbox(mapping, form, request, response, "REVERT_MESSAGE_OK",
				messageForm.getFromPage());
	}

	/**
	 * ɾ���յ�����Ϣ
	 */
	protected ActionForward doDeleteFriendMsg(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm fbean = (MessageForm) form;
		String msg = null;
		boolean f = MessageDAO.deleteFriendMsg(fbean.getTableID(), fbean
				.getMsgID(), fbean.getId());
		if (f) {
			msg = "FRIEND_MESSAGE_DELETE_OK";
		} else {
			msg = "FRIEND_MESSAGE_DELETE_ERROR";
		}
		return msgbox(mapping, form, request, response, msg, fbean
				.getFromPage());
	}

	/**
	 * ɾ�����͵���Ϣ
	 */
	protected ActionForward doDeleteSendMsg(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm fbean = (MessageForm) form;
		boolean f = MessageDAO.deleteSendMsg(fbean.getTableID(), fbean
				.getMsgID(), fbean.getId());
		String msg = null;
		if (f)
			msg = "SEND_MESSAGE_DELETE_OK";
		else
			msg = "SEND_MESSAGE_DELETE_ERROR";
		return msgbox(mapping, form, request, response, msg, fbean
				.getFromPage());

	}

	/**
	 * ɾ��ϵͳ��Ϣ
	 */
	protected ActionForward doDeleteSystemMsg(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm fbean = (MessageForm) form;
		boolean f = MessageDAO.deleteSystemMsg(fbean.getTableID(), fbean
				.getId());
		if (f)
			return msgbox(mapping, form, request, response,
					"SYSTEM_MESSAGE_DELETE_OK", fbean.getFromPage());
		return msgbox(mapping, form, request, response,
				"SYSTEM_MESSAGE_DELETE_ERROR", fbean.getFromPage());

	}

	/**
	 * ɾ��ϵͳ��Ϣ ����
	 */
	protected ActionForward doDeleteAllSystemMsg(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm fbean = (MessageForm) form;
		int ex = 0;
		String[] mids = request.getParameterValues("mid");
		if (mids != null)
			for (int i = 0; i < mids.length; i++) {
				String str = mids[i];
				String msgID = (str.split(","))[0];
				String tableID = (str.split(","))[1];
				boolean f = MessageDAO.deleteSystemMsg(Integer
						.parseInt(tableID), fbean.getId());
				if (!f)
					ex++;
			}
		if (ex == 0)
			return msgbox(mapping, form, request, response, "ɾ���ɹ�!!!", "");
		return msgbox(mapping, form, request, response, "ɾ��ʧ��" + ex + "��", "");
	}

	/**
	 * ɾ�����͵Ķ��� ����
	 */
	protected ActionForward doDeleteAllSendMsg(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm fbean = (MessageForm) form;
		int ex = 0;
		String[] mids = request.getParameterValues("mid");
		if (mids != null)
			for (int i = 0; i < mids.length; i++) {
				String str = mids[i];
				String msgID = (str.split(","))[0];
				String tableID = (str.split(","))[1];
				boolean f = MessageDAO.deleteSendMsg(Integer.parseInt(tableID),
						Integer.parseInt(msgID), fbean.getId());
				if (!f)
					ex++;
			}
		if (ex == 0)
			return msgbox(mapping, form, request, response, "ɾ���ɹ�!!!", "");
		return msgbox(mapping, form, request, response, "ɾ��ʧ��" + ex + "��", "");

	}

	/**
	 * ɾ�����յ����� ����
	 */
	protected ActionForward doDeleteAllRevertMsg(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm fbean = (MessageForm) form;
		int ex = 0;
		String[] mids = request.getParameterValues("mid");
		if (mids != null)
			for (int i = 0; i < mids.length; i++) {
				String str = mids[i];
				String msgID = (str.split(","))[0];
				String tableID = (str.split(","))[1];
				boolean f = MessageDAO.deleteFriendMsg(Integer
						.parseInt(tableID), Integer.parseInt(msgID), fbean
						.getId());
				if (!f)
					ex++;
			}
		if (ex == 0)
			return msgbox(mapping, form, request, response, "ɾ���ɹ�!!!", "");
		return msgbox(mapping, form, request, response, "ɾ��ʧ��" + ex + "��", "");
	}

	/**
	 * д����
	 */
	protected ActionForward doSendAffiche(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm fbean = (MessageForm) form;
		AfficheBean bean = new AfficheBean();
		Date date = new Date();
		bean.setUserid(0);
		bean.setSendtime(date);
		bean.setStatus(0);
		bean.setTitle(fbean.getTitle());
		String tip = super.getTemplate("/WEB-INF/conf/send_affiche.vm");// ���ģ������
		String notify_content = MessageFormat.format(tip, fbean.getContent());
		bean.setContent(notify_content);// ����Ϣ����
		try {
			MessageDAO.save(bean);
			return msgbox(mapping, form, request, response, "���ͳɹ�", fbean
					.getFromPage());
		} catch (Exception ex) {

		}
		return msgbox(mapping, form, request, response, "����ʧ��", fbean
				.getFromPage());
	}

	/**
	 * д����
	 */
	protected ActionForward doDeleteAffiche(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm fbean = (MessageForm) form;
		AfficheBean bean = new AfficheBean();
		bean.setMsgid(fbean.getMsgID());

		try {
			MessageDAO.delete(bean);
			return msgbox(mapping, form, request, response, "ɾ���ɹ�", fbean
					.getFromPage());
		} catch (Exception ex) {

		}
		return msgbox(mapping, form, request, response, "ɾ��ʧ��", fbean
				.getFromPage());
	}

	/**
	 * ��������(ajax) TODO: ��η�ֹȺ��������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	protected ActionForward doSendMsgAJAX(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MessageForm msgform = (MessageForm) form;
		super.validateClientId(request, msgform);
		MessageResources message = getResources(request);
		String msg = "";
		while (true) {
			if (StringUtils.isEmpty(msgform.getContent())) {
				msg = message.getMessage("error.empty_content");
				break;
			}
			if (msgform.getReceiverId() == 0 || msgform.getSid() == 0) {
				msg = message.getMessage("error.param");
				break;
			}
			UserBean loginUser = super.getLoginUser(request, response);// ������
			UserBean receiver = UserDAO.getUserByID(msgform.getReceiverId());// ������
			if (loginUser == null) {
				msg = message.getMessage("error.user_not_login");
				break;
			} else if (loginUser.getStatus() != UserBean.STATUS_NORMAL) {
				msg = message.getMessage("error.user_not_available");
				break;
			}

			if (receiver == null
					|| receiver.getStatus() != UserBean.STATUS_NORMAL) {
				msg = message.getMessage("error.user_not_available");
				break;
			}
			String content = super.filterScriptAndStyle(StringUtils.abbreviate(
					super.autoFiltrate(null, msgform.getContent()),
					MAX_MESSAGE_LENGTH));
			// �����ߵļ����б�
			ArrayList<UserBean> ccUsers = new ArrayList<UserBean>();
			// �жϷ������Ƿ��ڽ����ߵĺ������� && �ж��Ƿ���Լ���
			if (!UserDAO.isUserInBlackList(receiver.getId(), loginUser.getId())
					&& loginUser.getId() != receiver.getId())
				ccUsers.add(receiver);
			try {
				String[] ccIds = request.getParameterValues("ccId");
				if (ccIds != null) {
					// TODO: �˴������ܸĽ������
					for (String sccId : ccIds) {
						int ccId = NumberUtils.toInt(sccId, -1);
						if (ccId <= 0)
							continue;
						ccUsers.add(new UserBean(ccId));
					}
				}
				if (ccUsers.size() > 0) {
					MessageDAO.SendMsgs(loginUser, ccUsers, content);
				}
				if (msg.trim().equals(""))
					msg = message.getMessage("message.sent");
			} catch (HibernateException e) {
				context().log("undelete diary failed.", e);
				msg = message.getMessage("error.database", e.getMessage());
			} finally {
				if (ccUsers != null) {
					ccUsers.clear();
					ccUsers = null;
				}
			}
			break;
		}
		return msgbox(mapping, form, request, response, msg);
	}
}
