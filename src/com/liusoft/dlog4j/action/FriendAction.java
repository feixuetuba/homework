package com.liusoft.dlog4j.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import sun.security.action.GetBooleanAction;

import com.liusoft.dlog4j.SessionUserObject;
import com.liusoft.dlog4j.UserLoginManager;
import com.liusoft.dlog4j.beans.FriendBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.beans.UserFriendGroupBean;
import com.liusoft.dlog4j.dao.UserDAO;
import com.liusoft.dlog4j.dao.UserFriendGroupDAO;
import com.liusoft.dlog4j.formbean.FormBean;
import com.liusoft.dlog4j.formbean.FriendForm;
import com.liusoft.dlog4j.formbean.UserForm;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * �û����ѷ������
 * 
 * @author liqiang
 */
public class FriendAction extends ActionBase {
	/**
	 * ��֤�ͻ��˰�ȫʶ����
	 * 
	 * @param req
	 * @param form
	 * @throws IllegalAccessException
	 */
	protected static void validateClientId(HttpServletRequest req, FormBean form)
			throws IllegalAccessException {
		if (!UserLoginManager.validateClientId(req, form.get__ClientId()))
			throw new IllegalAccessException();
	}

	/**
	 * ��Ӻ��ѷ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @author liqiang
	 */
	protected ActionForward doFriendGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		//
		FriendForm friendForm = (FriendForm) form;
		// �ж��û��Ƿ��¼
		SessionUserObject loginUser = super.getLoginUser(request, response,
				false);
		ActionMessages msgs = new ActionMessages();
		// ��֤�Ƿ�Ϊ��ֵ ���ַ�����û���ж�
		while (msgs.isEmpty()) {
			if (StringUtils.isEmpty(request.getParameter("groupname"))) {
				msgs.add("group_name", new ActionMessage(
						"error.empty_not_allowed"));
				break;
			}
			break;
		}
		// �ж�message�Ƿ�����Ϣ
		if (!msgs.isEmpty()) {
			saveMessages(request, msgs);
			return mapping.findForward("show_m");
		}
		// �û��ĳ־��� ȡ����ǰ�û���id
		UserBean ubean = UserDAO.getUserByID(friendForm.getId());
		// �־��� ���ѷ���
		UserFriendGroupBean userFriendGroupBean = new UserFriendGroupBean();
		// �û����ѷ���־��ำֵ
		userFriendGroupBean.setGroupname(friendForm.getGroupname());
		userFriendGroupBean.setGrouptype(friendForm.getGrouptype());
		userFriendGroupBean.setUserid(friendForm.getId());
		// �Ƿ�ִ�гɹ�
		if (com.liusoft.dlog4j.dao.UserFriendGroupDAO
				.saveUserFriendGroup(userFriendGroupBean)) {
			return makeForward(mapping.findForward("show_m"), friendForm
					.getSid());
		}
		return null;
	}

	/**
	 * ɾ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @author liqiang
	 */
	protected ActionForward doDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// ��ǰ�û���Ϣ
		FriendForm friendForm = (FriendForm) form;
		int userid = friendForm.getUserid();
		int groupid = friendForm.getGroupid();
		// ����û�Ĭ�Ϸ���
		UserFriendGroupBean bean = UserFriendGroupDAO.findByDefault(friendForm
				.getUserid());

		System.out.println(userid + "  " + groupid + "   " + bean.getGroupid());
		int g_default = bean.getGroupid();
		// ��ɾ���ķ����������û�ת����Ĭ�Ϸ���
		UserFriendGroupDAO.divertAllFriend(userid, groupid, g_default);
		// ɾ������
		UserFriendGroupDAO.deleteFGroup(groupid, userid);
		//		
		// return makeForward(mapping.findForward("show_m"),
		// friendForm.getSid());
		return new ActionForward(friendForm.getFromPage(), true);
	}

	/**
	 * ���ķ������Ʒ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @author liqiang
	 */
	protected ActionForward doUpdateGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest hsr,
			HttpServletResponse response) {
		// ��ǰ�û���Ϣ
		FriendForm friendForm = (FriendForm) form;
		UserFriendGroupBean bean = new UserFriendGroupBean();
		bean.setGroupid(friendForm.getGroupid());
		bean.setGroupname(friendForm.getGroupname().trim());
		bean.setGrouptype(friendForm.getGrouptype());
		bean.setUserid(friendForm.getId());
		UserFriendGroupDAO.updateFGroup(bean);
		return makeForward(mapping.findForward("show_m"), friendForm.getSid());
	}

	/**
	 * �Զ���������[�ҵĺ���]<Ĭ��>
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @author liqiang
	 */
	protected ActionForward doCreateGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest hsr,
			HttpServletResponse response) {
		FriendForm friendForm = (FriendForm) form;
		UserFriendGroupBean bean = new UserFriendGroupBean();
		bean.setGroupname("�ҵĺ���");
		bean.setGrouptype(2);
		bean.setUserid(friendForm.getId());
		UserFriendGroupDAO.save(bean);
		return makeForward(mapping.findForward("show_f"), friendForm.getSid());
	}

	/**
	 * ת�ƺ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @author liqiang
	 * @throws IOException
	 */
	protected ActionForward doFriendDivert(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		FriendForm friendForm = (FriendForm) form;
		String msg = null;
		SessionUserObject loginUser = super.getLoginUser(request, response,
				false);
		if (friendForm.getGroupid() > 0) {
			// �����Ƿ����
			if (UserDAO.getFriend(friendForm.getUserid(), friendForm
					.getFriendid()) != null) {
				if (UserFriendGroupDAO.diverFriend(friendForm.getUserid(),
						friendForm.getFriendid(), friendForm.getGroupid()))
					return msgbox(mapping, form, request, response, "ת�Ƴɹ�;1");
				return msgbox(mapping, form, request, response, "ת��ʧ��;-1");
			} else if (UserDAO.delBlackList(friendForm.getUserid(), friendForm
					.getFriendid())) {
				if (!UserDAO.delBlackList(friendForm.getUserid(), friendForm
						.getFriendid())) {
					// ��Ӻ���
					FriendBean friend = new FriendBean();
					UserBean userBean = new UserBean();
					userBean.setId(friendForm.getFriendid());
					friend.setAddTime(new Date());
					friend.setOwner(friendForm.getUserid());
					friend.setGroupid(friendForm.getGroupid());
					friend.setFriend(userBean);
					if (UserDAO.addFriend(friend))
					{
						return msgbox(mapping, form, request, response,
						"ת�Ƴɹ�;1");
					}						
				}
			}
			return msgbox(mapping, form, request, response, "ת��ʧ��;-1");
		} else {
			// groupid = -1 ������ת�Ƶ�������
			String s_id = String.valueOf(friendForm.getFriendid());
			if (UserDAO.isUserInBlackList(loginUser.getId(), friendForm
					.getFriendid())) {
				msg = getMessage(request, null, "error.user_already_in_block",
						s_id)
						+ "-1";
			} else {
				if (UserDAO.addBlackList(loginUser.getId(), friendForm
						.getFriendid(), 0)) {
					// ���Ѵӷ����Ƴ�
					String[] ids = { friendForm.getFriendid() + "" };
					int er = UserDAO.deleteFriend(loginUser.getId(), ids);
				}
				msg = getMessage(request, null, "error.user_added_to_block",
						s_id)
						+ ";1";
			}
			return msgbox(mapping, form, request, response, msg);
		}

	}
}
