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
 * 用户好友分组相关
 * 
 * @author liqiang
 */
public class FriendAction extends ActionBase {
	/**
	 * 验证客户端安全识别码
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
	 * 添加好友分组
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
		// 判断用户是否登录
		SessionUserObject loginUser = super.getLoginUser(request, response,
				false);
		ActionMessages msgs = new ActionMessages();
		// 验证是否为空值 空字符串还没能判断
		while (msgs.isEmpty()) {
			if (StringUtils.isEmpty(request.getParameter("groupname"))) {
				msgs.add("group_name", new ActionMessage(
						"error.empty_not_allowed"));
				break;
			}
			break;
		}
		// 判断message是否有消息
		if (!msgs.isEmpty()) {
			saveMessages(request, msgs);
			return mapping.findForward("show_m");
		}
		// 用户的持久类 取出当前用户的id
		UserBean ubean = UserDAO.getUserByID(friendForm.getId());
		// 持久类 好友分组
		UserFriendGroupBean userFriendGroupBean = new UserFriendGroupBean();
		// 用户好友分组持久类赋值
		userFriendGroupBean.setGroupname(friendForm.getGroupname());
		userFriendGroupBean.setGrouptype(friendForm.getGrouptype());
		userFriendGroupBean.setUserid(friendForm.getId());
		// 是否执行成功
		if (com.liusoft.dlog4j.dao.UserFriendGroupDAO
				.saveUserFriendGroup(userFriendGroupBean)) {
			return makeForward(mapping.findForward("show_m"), friendForm
					.getSid());
		}
		return null;
	}

	/**
	 * 删除分组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @author liqiang
	 */
	protected ActionForward doDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 当前用户信息
		FriendForm friendForm = (FriendForm) form;
		int userid = friendForm.getUserid();
		int groupid = friendForm.getGroupid();
		// 获得用户默认分组
		UserFriendGroupBean bean = UserFriendGroupDAO.findByDefault(friendForm
				.getUserid());

		System.out.println(userid + "  " + groupid + "   " + bean.getGroupid());
		int g_default = bean.getGroupid();
		// 将删除的分组内所有用户转移至默认分组
		UserFriendGroupDAO.divertAllFriend(userid, groupid, g_default);
		// 删除分组
		UserFriendGroupDAO.deleteFGroup(groupid, userid);
		//		
		// return makeForward(mapping.findForward("show_m"),
		// friendForm.getSid());
		return new ActionForward(friendForm.getFromPage(), true);
	}

	/**
	 * 更改分组名称分组
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
		// 当前用户信息
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
	 * 自动创建分组[我的好友]<默认>
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
		bean.setGroupname("我的好友");
		bean.setGrouptype(2);
		bean.setUserid(friendForm.getId());
		UserFriendGroupDAO.save(bean);
		return makeForward(mapping.findForward("show_f"), friendForm.getSid());
	}

	/**
	 * 转移好友
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
			// 好友是否存在
			if (UserDAO.getFriend(friendForm.getUserid(), friendForm
					.getFriendid()) != null) {
				if (UserFriendGroupDAO.diverFriend(friendForm.getUserid(),
						friendForm.getFriendid(), friendForm.getGroupid()))
					return msgbox(mapping, form, request, response, "转移成功;1");
				return msgbox(mapping, form, request, response, "转移失败;-1");
			} else if (UserDAO.delBlackList(friendForm.getUserid(), friendForm
					.getFriendid())) {
				if (!UserDAO.delBlackList(friendForm.getUserid(), friendForm
						.getFriendid())) {
					// 添加好友
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
						"转移成功;1");
					}						
				}
			}
			return msgbox(mapping, form, request, response, "转移失败;-1");
		} else {
			// groupid = -1 将好友转移到黑名单
			String s_id = String.valueOf(friendForm.getFriendid());
			if (UserDAO.isUserInBlackList(loginUser.getId(), friendForm
					.getFriendid())) {
				msg = getMessage(request, null, "error.user_already_in_block",
						s_id)
						+ "-1";
			} else {
				if (UserDAO.addBlackList(loginUser.getId(), friendForm
						.getFriendid(), 0)) {
					// 好友从分组移除
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
