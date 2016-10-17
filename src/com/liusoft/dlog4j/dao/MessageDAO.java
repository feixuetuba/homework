/*
 *  MessageDAO.java
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
 *  Author: Winter Lau (javayou@gmail.com)
 *  http://dlog4j.sourceforge.net
 */
package com.liusoft.dlog4j.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.liusoft.dlog4j.base.MessageInfo;
import com.liusoft.dlog4j.beans.AfficheBean;
import com.liusoft.dlog4j.beans.FriendBean;
import com.liusoft.dlog4j.beans.MessageBean;
import com.liusoft.dlog4j.beans.ReceiverMessageBean;
import com.liusoft.dlog4j.beans.SendMessageBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.formbean.MessageForm;
import com.liusoft.dlog4j.util.DateUtils;

/**
 * 站内短消息之数据库访问接口
 * 
 * @author Winter Lau
 */
public class MessageDAO extends DAO {

	/**
	 * 群发留言
	 * 
	 * @param club
	 * @param only_admin
	 * @param content
	 * @param sender
	 */
	public static void SendMsg(UserBean sender, UserBean receiver,
			String content) {
		if (sender != null && receiver != null
				&& StringUtils.isNotBlank(content)) {
			MessageBean msg = new MessageBean();
			msg.setContent(content);
			msg.setSendTime(new Date());
			msg.setFromUser(sender);
			msg.setStatus(MessageBean.STATUS_NEW);
			msg.setToUser(receiver);
			save(msg);
		}
	}

	/**
	 * 群发留言
	 * 
	 * @param club
	 * @param only_admin
	 * @param content
	 * @param sender
	 */
	public static void SendMsgs(UserBean sender, List receivers, String content) {
		try {
			Date newDate = new Date();
			// 批量写入数据库
			Session ssn = getSession();
			for (int i = 0; i < receivers.size(); i++) {
				String nickName = " 错误 ";
				UserBean receiver = (UserBean) receivers.get(i);
				// 发送者是否为好友
				FriendBean friendBane = UserDAO.getFriend(receiver.getId(),
						sender.getId());// (接收者,发送者)
				nickName = ((UserBean) getBean(UserBean.class, receiver.getId()))
						.getNickname();
				// 黑名单
				if (!UserDAO
						.isUserInBlackList(receiver.getId(), sender.getId())) {
					beginTransaction();
					// 构造消息bean
					MessageBean msg = new MessageBean();
					msg.setContent(content);
					msg.setSendTime(newDate);
					msg.setFromUser(sender);
					msg.setStatus(MessageBean.STATUS_NEW);
					msg.setToUser(receiver);
					ssn.save(msg);
					// 发送消息Bean
					SendMessageBean sendMessageBean = new SendMessageBean();
					sendMessageBean.setMsgid(msg.getId());
					sendMessageBean.setReceiverid(receiver.getId());// 接受者
					sendMessageBean.setReceivername(nickName);// 接受者
					sendMessageBean.setSendtime(newDate);
					sendMessageBean.setUserid(sender.getId());// 发送者
					sendMessageBean.setStatus(0);// 关联 回复用到
					ssn.save(sendMessageBean);
					// 接收消息Bean
					ReceiverMessageBean receiverMessageBean = new ReceiverMessageBean();
					receiverMessageBean.setMsgid(msg.getId());
					receiverMessageBean.setSendid(sender.getId());
					receiverMessageBean.setSendname(sender.getNickname());
					receiverMessageBean.setUserid(receiver.getId());
					receiverMessageBean.setReadtime(newDate);
					if (friendBane!=null)
						receiverMessageBean.setStatus(0);// 是好友
					else
						receiverMessageBean.setStatus(3);// 不是好友
					ssn.save(receiverMessageBean);

					if (i % 20 == 0) {
						ssn.flush();
						ssn.clear();
					}
					commit();
				}
			}

		} catch (HibernateException e) {
			e.printStackTrace();
			rollback();
		}
	}

	/**
	 * 回复并删除所回复的短消息
	 * 
	 * @param old_msg_id
	 * @param msg
	 */
	public static void replyAndDeleteMessage(int old_msg_id, MessageBean msg) {
		try {
			Session ssn = getSession();
			beginTransaction();
			// 回复短消息
			ssn.save(msg);
			// 删除所回复的短消息
			if (old_msg_id > 0)
				executeNamedUpdate("DELETE_MESSAGE", old_msg_id, msg
						.getFromUser().getId());
			commit();
		} catch (HibernateException e) {
			rollback();
		}
	}

	/**
	 * 阅读单独消息
	 * 
	 * @param msg
	 */
	public static void readMsg(MessageBean msg) {
		if (msg != null) {
			commitNamedUpdate("READ_ONE_MESSAGE", MessageBean.STATUS_READ,
					new Date(), msg.getId(), MessageBean.STATUS_NEW);
		}
	}

	/**
	 * 设置新短消息状态为已读
	 * 
	 * @param userid
	 * @return
	 */
	public static int readNewMsgs(int userid) {
		return commitNamedUpdate("READ_MESSAGE", MessageBean.STATUS_READ,
				new Timestamp(System.currentTimeMillis()), userid,
				MessageBean.STATUS_NEW);
	}

	/**
	 * 读取短消息
	 * 
	 * @param msg_id
	 * @return
	 */
	public static MessageBean getMsg(int msg_id) {
		if (msg_id < 0)
			return null;
		return (MessageBean) getBean(MessageBean.class, msg_id);
	}

	/**
	 * 判断是否有新短消息
	 * 
	 * @param userid
	 * @return
	 */
	public static boolean hasNewMessage(int userid) {
		return getNewMessageCount(userid) > 0;
	}

	/**
	 * 返回某个用户的新短消息数(未读短消息) select count(*) as col_0_0_ from dlog_message
	 * messagebea0_ where messagebea0_.userid=? and messagebea0_.status=? and
	 * (messagebea0_.expire_time>? or messagebea0_.expire_time is null)
	 * 
	 * @param userid
	 * @return
	 */
	public static int getNewMessageCount(int userid) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		DateUtils.resetTime(cal);
		int messageCount = executeNamedStatAsIntCacheable(
				"query.new_msg_count", "NEW_MESSAGE_COUNT_OF_STATUS", userid,
				MessageBean.STATUS_NEW, cal.getTime());
		String hql = "SELECT COUNT(*) FROM AfficheBean WHERE userid=? AND status=?";
		int afficheCount = Integer.parseInt(uniqueResult(hql, userid, 0)
				.toString());
		// System.out.println("messameCount="+messageCount+"
		// afficheCount="+afficheCount+" userid="+userid);
		return messageCount + afficheCount;
	}

	/**
	 * 返回某个用户的短消息数
	 * 
	 * @param userid
	 * @return
	 */
	public static int getMessageCount(int userid) {
		return executeNamedStatAsIntCacheable("query.new_msg_count",
				"MESSAGE_COUNT", userid, MessageBean.STATUS_DELETED);
	}

	/**
	 * 列出某个用户接收到的短消息
	 * 
	 * @param userid
	 * @param fromId
	 * @param count
	 * @return
	 */
	public static List listMsgs(int userid, int fromIdx, int count) {
		return executeNamedQuery("LIST_MESSAGE", fromIdx, count, userid);
	}

	/**
	 * 删除某类短消息
	 * 
	 * @param userid
	 * @param status
	 * @param commit
	 * @return
	 * @throws SQLException
	 */
	public static int deleteMsgs(int userid, int status) {
		return commitNamedUpdate("DELETE_MESSAGE_BY_STATUS", userid, status);
	}

	/**
	 * 删除某条短消息
	 * 
	 * @param userid
	 * @param msgid
	 * @param commit
	 * @return
	 */
	public static int deleteMsg(int userid, int msgid) {
		return commitNamedUpdate("DELETE_MESSAGE", msgid, userid);
	}

	/**
	 * 删除某些短消息
	 * 
	 * @param ownerId
	 * @param friendId
	 */
	public static int deleteMsgs(int ownerId, String[] msgIds) {
		if (msgIds == null || msgIds.length == 0)
			return 0;
		// 一次最多删除五十条
		int max_msg_count = Math.min(msgIds.length, 50);
		StringBuffer hql = new StringBuffer(
				"DELETE FROM MessageBean AS f WHERE f.toUser.id=? AND f.id IN (");
		for (int i = 0; i < max_msg_count; i++) {
			hql.append("?,");
		}
		hql.append("?)");
		Session ssn = getSession();
		try {
			beginTransaction();
			Query q = ssn.createQuery(hql.toString());
			q.setInteger(0, ownerId);
			int i = 0;
			for (; i < max_msg_count; i++) {
				String s_id = (String) msgIds[i];
				int id = -1;
				try {
					id = Integer.parseInt(s_id);
				} catch (Exception e) {
				}
				q.setInteger(i + 1, id);
			}
			q.setInteger(i + 1, -1);
			int er = q.executeUpdate();
			commit();
			return er;
		} catch (HibernateException e) {
			rollback();
			throw e;
		}
	}

	// ====================================================================
	/**
	 * 系统消息列表
	 */
	public static List getSystem(int uid, int fromIdx, int fetchCount) {
		String hql = "FROM AfficheBean WHERE userid=? ORDER BY sendtime DESC";
		return executeQuery(hql, fromIdx, fetchCount, uid);
	}

	/**
	 * 公告列表
	 */
	public static List getAffiche(int fromIdx, int fetchCount) {
		String hql = "FROM AfficheBean WHERE userid=0 ORDER BY sendtime DESC";
		return executeQuery(hql, fromIdx, fetchCount);
	}

	/**
	 * 发送列表
	 */
	public static List getMySend(int uid, int fromIdx, int fetchCount) {
		String hql = "FROM SendMessageBean AS s where s.userid=? ORDER BY s.sendtime DESC";
		return executeQuery(hql, fromIdx, fetchCount, uid);
	}

	/**
	 * 好友消息列表
	 */
	public static List getFriend(int uid, int fromIdx, int fetchCount) {
		String hql = "FROM ReceiverMessageBean AS s where s.userid=? AND status>=0 AND status<=2 ORDER BY s.readtime DESC";

		return executeQuery(hql, fromIdx, fetchCount, uid);
	}

	/**
	 * 陌生人列表
	 */
	public static List getStranger(int uid, int fromIdx, int fetchCount) {
		String hql = "FROM ReceiverMessageBean AS s where s.userid=? AND status>=3 AND status<=5 ORDER BY s.readtime DESC";
		return executeQuery(hql, fromIdx, fetchCount, uid);
	}

	/**
	 * 读取回复内容
	 */
	public static Object getMySendContent(int msgid, int uid, int fuid) {
		try {
			String hql = "";
			if (uid != -1)// 回复那一条
			{
				hql = "FROM MessageBean WHERE msgid=? AND toUser.id=?";
				return uniqueResult(hql, msgid, uid);
			}

			if (fuid != -1)// 回复内容
			{
				hql = "FROM MessageBean WHERE msgid=? AND fromUser.id=?";
				return uniqueResult(hql, msgid, fuid);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	/**
	 * 读取公告
	 */
	public static Object getAfficheContent(int msgid, int uid) {
		String hql = "FROM AfficheBean WHERE msgid=? AND userid=?";
		return uniqueResult(hql, msgid, 0);
	}

	/**
	 * 读取 好友的短消息
	 */
	public static Object getFriendContent(int msgid, int id) {
		try {
			String hql = "FROM MessageBean WHERE msgid=?";
			String hql2 = "FROM ReceiverMessageBean WHERE receiverid=?";
			MessageBean messageBean = (MessageBean) uniqueResult(hql, msgid);
			ReceiverMessageBean receiverMessageBean = (ReceiverMessageBean) uniqueResult(
					hql2, id);// 读取消息
			if (messageBean != null) {
				if (receiverMessageBean.getStatus() == 0) {
					String hql3 = "UPDATE ReceiverMessageBean AS r SET r.status=? WHERE r.receiverid=?";
					commitUpdate(hql3, 1, id);// 修改状态
				}
				if (messageBean.getStatus() == 0) {
					Date newDate = new Date();
					String hql3 = "UPDATE  MessageBean AS m SET m.status=? , m.readTime=? WHERE m.id=?";
					commitUpdate(hql3, 1, newDate, msgid);// 修改状态
				}
			}
			return messageBean;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	/**
	 * 读取 陌生人的短信
	 */
	public static Object getStrangerContent(int msgid, int id) {
		try {
			String hql = "FROM MessageBean WHERE msgid=?";
			String hql2 = "FROM ReceiverMessageBean WHERE receiverid=?";
			MessageBean messageBean = (MessageBean) uniqueResult(hql, msgid);
			ReceiverMessageBean receiverMessageBean = (ReceiverMessageBean) uniqueResult(
					hql2, id);
			if (messageBean != null) {
				if (receiverMessageBean.getStatus() == 3) {
					String hql3 = "UPDATE ReceiverMessageBean AS r SET r.status=? WHERE r.receiverid=?";
					commitUpdate(hql3, 4, id);// 修改接受表状态 陌生人 已读
				}
				if (messageBean.getStatus() == 0) {
					Date newDate = new Date();
					String hql3 = "UPDATE  MessageBean  SET status=? ,readTime=? WHERE id=?";
					commitUpdate(hql3, 1, newDate, msgid);// 修改主表状态 已读
				}
			}
			return messageBean;
		}

		catch (Exception ex) {
			ex.printStackTrace();

		}
		return null;
	}

	/**
	 * 读取系统消息内容
	 * */
	public static Object getSystemContent(int msgid, int uid) {
		String hql2 = "FROM AfficheBean WHERE msgid=? and userid=?";
		Object object = uniqueResult(hql2, msgid, uid);
		if (object != null)//读取成功
		{
			String hql1 = "UPDATE AfficheBean SET status=? WHERE msgid=? and userid=?";
			commitUpdate(hql1, 1, msgid, uid);//修改系统消息状态 系统消息已读
		}

		return object;
	}

	/**
	 * 回复消息
	 */
	public static void RevertMsgToAllTable(UserBean sender, UserBean receiver,
			MessageForm form, boolean f) {
		try {
			// System.out.println("SendMsgToAllTable()478");
			Date newDate = new Date();
			// 批量写入数据库
			Session ssn = getSession();
			beginTransaction();
			// 构造消息bean
			MessageBean msg = new MessageBean();
			msg.setContent(form.getMsgContent());
			msg.setSendTime(newDate);
			msg.setFromUser(sender);
			msg.setStatus(MessageBean.STATUS_NEW);
			msg.setToUser(receiver);
			ssn.save(msg);
			// 发送消息Bean
			SendMessageBean sendMessageBean = new SendMessageBean();
			sendMessageBean.setMsgid(msg.getId());
			sendMessageBean.setReceiverid(form.getReceiverId());
			sendMessageBean.setReceivername(form.getReceiverName());
			sendMessageBean.setSendtime(newDate);
			sendMessageBean.setUserid(form.getRevertId());
			sendMessageBean.setStatus(form.getStatus());
			ssn.save(sendMessageBean);
			// 接受消息Bean
			ReceiverMessageBean receiverMessageBean = new ReceiverMessageBean();
			receiverMessageBean.setMsgid(msg.getId());
			receiverMessageBean.setSendid(form.getRevertId());
			receiverMessageBean.setSendname(form.getRevertName());
			receiverMessageBean.setUserid(form.getReceiverId());
			receiverMessageBean.setReadtime(newDate);

			if (f)// 发送状态
				receiverMessageBean.setStatus(0);// 好友未读
			else
				receiverMessageBean.setStatus(3);// 陌生人未读
			ssn.save(receiverMessageBean);
			// 接收状态
			String hql4 = "UPDATE ReceiverMessageBean AS r SET r.status=? WHERE r.receiverid=?";
			Query q = ssn.createQuery(hql4);
			if (f)
				q.setParameter(0, 2);// 好友 已回
			else
				q.setParameter(0, 5);// 陌生人 已回
			q.setParameter(1, form.getTableID());
			int row = q.executeUpdate();
			if (row > 0)
				commit();
			else
				rollback();
		} catch (HibernateException e) {
			rollback();
		}
	}

	/**
	 * 写消息
	 */
	public static void SendMsgToAllTable(UserBean sender, UserBean receiver,
			MessageForm form, boolean f) {
		try {
			// System.out.println("SendMsgToAllTable() row531");
			Date newDate = new Date();
			// 批量写入数据库
			Session ssn = getSession();
			beginTransaction();
			// 构造消息bean
			MessageBean msg = new MessageBean();
			msg.setContent(form.getMsgContent());
			msg.setSendTime(newDate);
			msg.setFromUser(sender);
			msg.setStatus(MessageBean.STATUS_NEW);
			msg.setToUser(receiver);
			ssn.save(msg);
			// 发送消息Bean
			SendMessageBean sendMessageBean = new SendMessageBean();
			sendMessageBean.setMsgid(msg.getId());
			sendMessageBean.setReceiverid(form.getReceiverId());
			sendMessageBean.setReceivername(form.getReceiverName());
			sendMessageBean.setSendtime(newDate);
			sendMessageBean.setUserid(form.getRevertId());
			sendMessageBean.setStatus(form.getStatus());
			ssn.save(sendMessageBean);
			// 接受消息Bean
			ReceiverMessageBean receiverMessageBean = new ReceiverMessageBean();
			receiverMessageBean.setMsgid(msg.getId());
			receiverMessageBean.setSendid(form.getRevertId());
			receiverMessageBean.setSendname(form.getRevertName());
			receiverMessageBean.setUserid(form.getReceiverId());
			receiverMessageBean.setReadtime(newDate);
			if (f)
				receiverMessageBean.setStatus(0);
			else
				receiverMessageBean.setStatus(3);
			ssn.save(receiverMessageBean);
			// 修改接收状态
			String hql4 = "UPDATE ReceiverMessageBean AS r SET r.status=? WHERE r.receiverid=?";
			Query q = ssn.createQuery(hql4);
			q.setParameter(0, 2);
			q.setParameter(1, form.getTableID());
			int row = q.executeUpdate();
			if (row > 0)
				commit();
			else
				rollback();
		} catch (HibernateException e) {

			rollback();
		}
	}

	/**
	 * 删除收到的短消息
	 * 
	 * @param tableID
	 * @param msgID
	 * 
	 */
	public static boolean deleteFriendMsg(int tableID, int msgID, int userid) {
		try {
			ReceiverMessageBean cbean = new ReceiverMessageBean();
			cbean.setReceiverid(tableID);// 接收表主键 tableID
			cbean.setUserid(userid);
			delete(cbean);
			// Message表是否在send表中被关联
			String hql = "FROM SendMessageBean AS s where s.msgid=? or status=? ";
			SendMessageBean obj = (SendMessageBean) uniqueResult(hql, msgID,
					msgID);// 检查发送表与主表是否有关联
			if (obj == null) {
				MessageBean messageBean = new MessageBean();
				messageBean.setId(msgID);// 关联表dlog_message主键 msgID
				delete(messageBean);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}

	/**
	 * 删除发送的短消息
	 * 
	 * @param tableID
	 * @param msgID
	 */
	public static boolean deleteSendMsg(int tableID, int msgID, int userid) {
		// TODO Auto-generated method stub
		try {
			SendMessageBean sendMessageBean = new SendMessageBean();
			sendMessageBean.setSendid(tableID);
			sendMessageBean.setUserid(userid);
			delete(sendMessageBean);// 删除发出的信息 tableID
			String hql = "FROM SendMessageBean AS s where s.msgid=? or status=? ";
			SendMessageBean smessageBean = (SendMessageBean) uniqueResult(hql,
					msgID, msgID);// 查发送表与主表是否还有关联
			String hql2 = "FROM ReceiverMessageBean AS s where s.msgid=?";
			ReceiverMessageBean rmessageBean = (ReceiverMessageBean) uniqueResult(
					hql2, msgID);// 查接收表与主表是否还有关联
			if (smessageBean == null && rmessageBean == null) {
				MessageBean messageBean = new MessageBean();
				messageBean.setId(msgID);
				delete(messageBean);// 删除信息表 msgID
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}

	/**
	 * 好友短信总数 分页用
	 */
	public static int getMsgFriendCount(int uid) {
		String hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS r WHERE r.userid=? AND status>=0 AND status<=2";
		int Count = Integer.parseInt(uniqueResult(hql, uid).toString());
		return Count;
	}

	/**
	 * 陌生人短信总数 分页用
	 */
	public static int getMsgStrangerCount(int uid) {
		String hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS r WHERE r.userid=? AND status>=3 AND status<=5";
		int Count = Integer.parseInt(uniqueResult(hql, uid).toString());
		return Count;
	}

	/**
	 * 系统短信总数 分页用
	 */
	public static int getMsgSystemCount(int uid) {
		String hql = "SELECT COUNT(*) FROM AfficheBean AS a WHERE a.userid=? ";
		int Count = Integer.parseInt(uniqueResult(hql, uid).toString());
		return Count;
	}

	/**
	 * 公告总数 分页用
	 */
	public static int getMsgAfficheCount(int uid) {
		String hql = "SELECT COUNT(*) FROM AfficheBean AS a WHERE a.userid=? ";
		int Count = Integer.parseInt(uniqueResult(hql, 0).toString());
		return Count;
	}

	/**
	 * 发送到短消息总数 分页用
	 */
	public static int getMsgMySendCount(int uid) {
		String hql = "SELECT COUNT(*) FROM SendMessageBean AS s WHERE s.userid=? ";
		int Count = Integer.parseInt(uniqueResult(hql, uid).toString());
		return Count;
	}

	/**所有信息 已读 未读 */
	public static MessageInfo getMsgInfo(int uid) {
		// TODO Auto-generated method stub
		MessageInfo info = new MessageInfo();
		String hql;
		hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS s WHERE s.userid=? AND s.status=?";
		info.setF_not_read(Integer.parseInt(uniqueResult(hql, uid,
				info.F_NOT_READ).toString()));// 好友未读
		hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS s WHERE s.userid=? AND s.status>=0 AND s.status<=2";
		info.setF_read(Integer.parseInt(uniqueResult(hql, uid).toString()));// 好友总数
		hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS s WHERE s.userid=? AND s.status=?";
		info.setS_not_read(Integer.parseInt(uniqueResult(hql, uid,
				info.S_NOT_READ).toString()));// 陌生人未读
		hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS s WHERE s.userid=? AND s.status>=3 AND s.status<=5";
		info.setS_read(Integer.parseInt(uniqueResult(hql, uid).toString()));// 陌生人总数

		hql = "SELECT COUNT(*) FROM AfficheBean AS s WHERE s.userid=? AND s.status=? ";
		info.setSys_not_read(Integer.parseInt(uniqueResult(hql, uid,
				info.SYS_NOT_READ).toString()));// 系统未读
		hql = "SELECT COUNT(*) FROM AfficheBean AS s WHERE s.userid=? ";
		info.setSys_read(Integer.parseInt(uniqueResult(hql, uid).toString()));// 系总数

		hql = "SELECT COUNT(*) FROM SendMessageBean AS s WHERE s.userid=? ";
		info.setMy_send(Integer.parseInt(uniqueResult(hql, uid).toString()));// 发送
		hql = "SELECT COUNT(*) FROM AfficheBean AS s WHERE s.userid=? ";
		info.setAffiche(Integer.parseInt(uniqueResult(hql, 0).toString()));// 公告

		return info;
	}

	/**
	 * 写系统消息
	 * */
	public static void systemMsg(UserBean sender, UserBean receiver,
			String content) {
		if (sender != null && receiver != null
				&& StringUtils.isNotBlank(content)) {
			AfficheBean afficheBean = new AfficheBean();
			afficheBean.setContent(content);
			afficheBean.setSendtime(new Date());
			afficheBean.setTitle("系统消息");
			afficheBean.setStatus(MessageBean.STATUS_NEW);
			afficheBean.setUserid(receiver.getId());
			save(afficheBean);
		}
	}

	/**
	 * 删除系统消息
	 * @param msgid
	 * @param id
	 */
	public static boolean deleteSystemMsg(int tableID, int id) {

		try {
			AfficheBean afficheBean = new AfficheBean();
			afficheBean.setMsgid(tableID);
			afficheBean.setUserid(id);
			delete(afficheBean);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// protected static List executeQuery(String hql, int fromIdx, int
	// fetchCount, Object...args){
}
