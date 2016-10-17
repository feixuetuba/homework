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
 * վ�ڶ���Ϣ֮���ݿ���ʽӿ�
 * 
 * @author Winter Lau
 */
public class MessageDAO extends DAO {

	/**
	 * Ⱥ������
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
	 * Ⱥ������
	 * 
	 * @param club
	 * @param only_admin
	 * @param content
	 * @param sender
	 */
	public static void SendMsgs(UserBean sender, List receivers, String content) {
		try {
			Date newDate = new Date();
			// ����д�����ݿ�
			Session ssn = getSession();
			for (int i = 0; i < receivers.size(); i++) {
				String nickName = " ���� ";
				UserBean receiver = (UserBean) receivers.get(i);
				// �������Ƿ�Ϊ����
				FriendBean friendBane = UserDAO.getFriend(receiver.getId(),
						sender.getId());// (������,������)
				nickName = ((UserBean) getBean(UserBean.class, receiver.getId()))
						.getNickname();
				// ������
				if (!UserDAO
						.isUserInBlackList(receiver.getId(), sender.getId())) {
					beginTransaction();
					// ������Ϣbean
					MessageBean msg = new MessageBean();
					msg.setContent(content);
					msg.setSendTime(newDate);
					msg.setFromUser(sender);
					msg.setStatus(MessageBean.STATUS_NEW);
					msg.setToUser(receiver);
					ssn.save(msg);
					// ������ϢBean
					SendMessageBean sendMessageBean = new SendMessageBean();
					sendMessageBean.setMsgid(msg.getId());
					sendMessageBean.setReceiverid(receiver.getId());// ������
					sendMessageBean.setReceivername(nickName);// ������
					sendMessageBean.setSendtime(newDate);
					sendMessageBean.setUserid(sender.getId());// ������
					sendMessageBean.setStatus(0);// ���� �ظ��õ�
					ssn.save(sendMessageBean);
					// ������ϢBean
					ReceiverMessageBean receiverMessageBean = new ReceiverMessageBean();
					receiverMessageBean.setMsgid(msg.getId());
					receiverMessageBean.setSendid(sender.getId());
					receiverMessageBean.setSendname(sender.getNickname());
					receiverMessageBean.setUserid(receiver.getId());
					receiverMessageBean.setReadtime(newDate);
					if (friendBane!=null)
						receiverMessageBean.setStatus(0);// �Ǻ���
					else
						receiverMessageBean.setStatus(3);// ���Ǻ���
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
	 * �ظ���ɾ�����ظ��Ķ���Ϣ
	 * 
	 * @param old_msg_id
	 * @param msg
	 */
	public static void replyAndDeleteMessage(int old_msg_id, MessageBean msg) {
		try {
			Session ssn = getSession();
			beginTransaction();
			// �ظ�����Ϣ
			ssn.save(msg);
			// ɾ�����ظ��Ķ���Ϣ
			if (old_msg_id > 0)
				executeNamedUpdate("DELETE_MESSAGE", old_msg_id, msg
						.getFromUser().getId());
			commit();
		} catch (HibernateException e) {
			rollback();
		}
	}

	/**
	 * �Ķ�������Ϣ
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
	 * �����¶���Ϣ״̬Ϊ�Ѷ�
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
	 * ��ȡ����Ϣ
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
	 * �ж��Ƿ����¶���Ϣ
	 * 
	 * @param userid
	 * @return
	 */
	public static boolean hasNewMessage(int userid) {
		return getNewMessageCount(userid) > 0;
	}

	/**
	 * ����ĳ���û����¶���Ϣ��(δ������Ϣ) select count(*) as col_0_0_ from dlog_message
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
	 * ����ĳ���û��Ķ���Ϣ��
	 * 
	 * @param userid
	 * @return
	 */
	public static int getMessageCount(int userid) {
		return executeNamedStatAsIntCacheable("query.new_msg_count",
				"MESSAGE_COUNT", userid, MessageBean.STATUS_DELETED);
	}

	/**
	 * �г�ĳ���û����յ��Ķ���Ϣ
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
	 * ɾ��ĳ�����Ϣ
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
	 * ɾ��ĳ������Ϣ
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
	 * ɾ��ĳЩ����Ϣ
	 * 
	 * @param ownerId
	 * @param friendId
	 */
	public static int deleteMsgs(int ownerId, String[] msgIds) {
		if (msgIds == null || msgIds.length == 0)
			return 0;
		// һ�����ɾ����ʮ��
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
	 * ϵͳ��Ϣ�б�
	 */
	public static List getSystem(int uid, int fromIdx, int fetchCount) {
		String hql = "FROM AfficheBean WHERE userid=? ORDER BY sendtime DESC";
		return executeQuery(hql, fromIdx, fetchCount, uid);
	}

	/**
	 * �����б�
	 */
	public static List getAffiche(int fromIdx, int fetchCount) {
		String hql = "FROM AfficheBean WHERE userid=0 ORDER BY sendtime DESC";
		return executeQuery(hql, fromIdx, fetchCount);
	}

	/**
	 * �����б�
	 */
	public static List getMySend(int uid, int fromIdx, int fetchCount) {
		String hql = "FROM SendMessageBean AS s where s.userid=? ORDER BY s.sendtime DESC";
		return executeQuery(hql, fromIdx, fetchCount, uid);
	}

	/**
	 * ������Ϣ�б�
	 */
	public static List getFriend(int uid, int fromIdx, int fetchCount) {
		String hql = "FROM ReceiverMessageBean AS s where s.userid=? AND status>=0 AND status<=2 ORDER BY s.readtime DESC";

		return executeQuery(hql, fromIdx, fetchCount, uid);
	}

	/**
	 * İ�����б�
	 */
	public static List getStranger(int uid, int fromIdx, int fetchCount) {
		String hql = "FROM ReceiverMessageBean AS s where s.userid=? AND status>=3 AND status<=5 ORDER BY s.readtime DESC";
		return executeQuery(hql, fromIdx, fetchCount, uid);
	}

	/**
	 * ��ȡ�ظ�����
	 */
	public static Object getMySendContent(int msgid, int uid, int fuid) {
		try {
			String hql = "";
			if (uid != -1)// �ظ���һ��
			{
				hql = "FROM MessageBean WHERE msgid=? AND toUser.id=?";
				return uniqueResult(hql, msgid, uid);
			}

			if (fuid != -1)// �ظ�����
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
	 * ��ȡ����
	 */
	public static Object getAfficheContent(int msgid, int uid) {
		String hql = "FROM AfficheBean WHERE msgid=? AND userid=?";
		return uniqueResult(hql, msgid, 0);
	}

	/**
	 * ��ȡ ���ѵĶ���Ϣ
	 */
	public static Object getFriendContent(int msgid, int id) {
		try {
			String hql = "FROM MessageBean WHERE msgid=?";
			String hql2 = "FROM ReceiverMessageBean WHERE receiverid=?";
			MessageBean messageBean = (MessageBean) uniqueResult(hql, msgid);
			ReceiverMessageBean receiverMessageBean = (ReceiverMessageBean) uniqueResult(
					hql2, id);// ��ȡ��Ϣ
			if (messageBean != null) {
				if (receiverMessageBean.getStatus() == 0) {
					String hql3 = "UPDATE ReceiverMessageBean AS r SET r.status=? WHERE r.receiverid=?";
					commitUpdate(hql3, 1, id);// �޸�״̬
				}
				if (messageBean.getStatus() == 0) {
					Date newDate = new Date();
					String hql3 = "UPDATE  MessageBean AS m SET m.status=? , m.readTime=? WHERE m.id=?";
					commitUpdate(hql3, 1, newDate, msgid);// �޸�״̬
				}
			}
			return messageBean;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	/**
	 * ��ȡ İ���˵Ķ���
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
					commitUpdate(hql3, 4, id);// �޸Ľ��ܱ�״̬ İ���� �Ѷ�
				}
				if (messageBean.getStatus() == 0) {
					Date newDate = new Date();
					String hql3 = "UPDATE  MessageBean  SET status=? ,readTime=? WHERE id=?";
					commitUpdate(hql3, 1, newDate, msgid);// �޸�����״̬ �Ѷ�
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
	 * ��ȡϵͳ��Ϣ����
	 * */
	public static Object getSystemContent(int msgid, int uid) {
		String hql2 = "FROM AfficheBean WHERE msgid=? and userid=?";
		Object object = uniqueResult(hql2, msgid, uid);
		if (object != null)//��ȡ�ɹ�
		{
			String hql1 = "UPDATE AfficheBean SET status=? WHERE msgid=? and userid=?";
			commitUpdate(hql1, 1, msgid, uid);//�޸�ϵͳ��Ϣ״̬ ϵͳ��Ϣ�Ѷ�
		}

		return object;
	}

	/**
	 * �ظ���Ϣ
	 */
	public static void RevertMsgToAllTable(UserBean sender, UserBean receiver,
			MessageForm form, boolean f) {
		try {
			// System.out.println("SendMsgToAllTable()478");
			Date newDate = new Date();
			// ����д�����ݿ�
			Session ssn = getSession();
			beginTransaction();
			// ������Ϣbean
			MessageBean msg = new MessageBean();
			msg.setContent(form.getMsgContent());
			msg.setSendTime(newDate);
			msg.setFromUser(sender);
			msg.setStatus(MessageBean.STATUS_NEW);
			msg.setToUser(receiver);
			ssn.save(msg);
			// ������ϢBean
			SendMessageBean sendMessageBean = new SendMessageBean();
			sendMessageBean.setMsgid(msg.getId());
			sendMessageBean.setReceiverid(form.getReceiverId());
			sendMessageBean.setReceivername(form.getReceiverName());
			sendMessageBean.setSendtime(newDate);
			sendMessageBean.setUserid(form.getRevertId());
			sendMessageBean.setStatus(form.getStatus());
			ssn.save(sendMessageBean);
			// ������ϢBean
			ReceiverMessageBean receiverMessageBean = new ReceiverMessageBean();
			receiverMessageBean.setMsgid(msg.getId());
			receiverMessageBean.setSendid(form.getRevertId());
			receiverMessageBean.setSendname(form.getRevertName());
			receiverMessageBean.setUserid(form.getReceiverId());
			receiverMessageBean.setReadtime(newDate);

			if (f)// ����״̬
				receiverMessageBean.setStatus(0);// ����δ��
			else
				receiverMessageBean.setStatus(3);// İ����δ��
			ssn.save(receiverMessageBean);
			// ����״̬
			String hql4 = "UPDATE ReceiverMessageBean AS r SET r.status=? WHERE r.receiverid=?";
			Query q = ssn.createQuery(hql4);
			if (f)
				q.setParameter(0, 2);// ���� �ѻ�
			else
				q.setParameter(0, 5);// İ���� �ѻ�
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
	 * д��Ϣ
	 */
	public static void SendMsgToAllTable(UserBean sender, UserBean receiver,
			MessageForm form, boolean f) {
		try {
			// System.out.println("SendMsgToAllTable() row531");
			Date newDate = new Date();
			// ����д�����ݿ�
			Session ssn = getSession();
			beginTransaction();
			// ������Ϣbean
			MessageBean msg = new MessageBean();
			msg.setContent(form.getMsgContent());
			msg.setSendTime(newDate);
			msg.setFromUser(sender);
			msg.setStatus(MessageBean.STATUS_NEW);
			msg.setToUser(receiver);
			ssn.save(msg);
			// ������ϢBean
			SendMessageBean sendMessageBean = new SendMessageBean();
			sendMessageBean.setMsgid(msg.getId());
			sendMessageBean.setReceiverid(form.getReceiverId());
			sendMessageBean.setReceivername(form.getReceiverName());
			sendMessageBean.setSendtime(newDate);
			sendMessageBean.setUserid(form.getRevertId());
			sendMessageBean.setStatus(form.getStatus());
			ssn.save(sendMessageBean);
			// ������ϢBean
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
			// �޸Ľ���״̬
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
	 * ɾ���յ��Ķ���Ϣ
	 * 
	 * @param tableID
	 * @param msgID
	 * 
	 */
	public static boolean deleteFriendMsg(int tableID, int msgID, int userid) {
		try {
			ReceiverMessageBean cbean = new ReceiverMessageBean();
			cbean.setReceiverid(tableID);// ���ձ����� tableID
			cbean.setUserid(userid);
			delete(cbean);
			// Message���Ƿ���send���б�����
			String hql = "FROM SendMessageBean AS s where s.msgid=? or status=? ";
			SendMessageBean obj = (SendMessageBean) uniqueResult(hql, msgID,
					msgID);// ��鷢�ͱ��������Ƿ��й���
			if (obj == null) {
				MessageBean messageBean = new MessageBean();
				messageBean.setId(msgID);// ������dlog_message���� msgID
				delete(messageBean);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}

	/**
	 * ɾ�����͵Ķ���Ϣ
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
			delete(sendMessageBean);// ɾ����������Ϣ tableID
			String hql = "FROM SendMessageBean AS s where s.msgid=? or status=? ";
			SendMessageBean smessageBean = (SendMessageBean) uniqueResult(hql,
					msgID, msgID);// �鷢�ͱ��������Ƿ��й���
			String hql2 = "FROM ReceiverMessageBean AS s where s.msgid=?";
			ReceiverMessageBean rmessageBean = (ReceiverMessageBean) uniqueResult(
					hql2, msgID);// ����ձ��������Ƿ��й���
			if (smessageBean == null && rmessageBean == null) {
				MessageBean messageBean = new MessageBean();
				messageBean.setId(msgID);
				delete(messageBean);// ɾ����Ϣ�� msgID
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}

	/**
	 * ���Ѷ������� ��ҳ��
	 */
	public static int getMsgFriendCount(int uid) {
		String hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS r WHERE r.userid=? AND status>=0 AND status<=2";
		int Count = Integer.parseInt(uniqueResult(hql, uid).toString());
		return Count;
	}

	/**
	 * İ���˶������� ��ҳ��
	 */
	public static int getMsgStrangerCount(int uid) {
		String hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS r WHERE r.userid=? AND status>=3 AND status<=5";
		int Count = Integer.parseInt(uniqueResult(hql, uid).toString());
		return Count;
	}

	/**
	 * ϵͳ�������� ��ҳ��
	 */
	public static int getMsgSystemCount(int uid) {
		String hql = "SELECT COUNT(*) FROM AfficheBean AS a WHERE a.userid=? ";
		int Count = Integer.parseInt(uniqueResult(hql, uid).toString());
		return Count;
	}

	/**
	 * �������� ��ҳ��
	 */
	public static int getMsgAfficheCount(int uid) {
		String hql = "SELECT COUNT(*) FROM AfficheBean AS a WHERE a.userid=? ";
		int Count = Integer.parseInt(uniqueResult(hql, 0).toString());
		return Count;
	}

	/**
	 * ���͵�����Ϣ���� ��ҳ��
	 */
	public static int getMsgMySendCount(int uid) {
		String hql = "SELECT COUNT(*) FROM SendMessageBean AS s WHERE s.userid=? ";
		int Count = Integer.parseInt(uniqueResult(hql, uid).toString());
		return Count;
	}

	/**������Ϣ �Ѷ� δ�� */
	public static MessageInfo getMsgInfo(int uid) {
		// TODO Auto-generated method stub
		MessageInfo info = new MessageInfo();
		String hql;
		hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS s WHERE s.userid=? AND s.status=?";
		info.setF_not_read(Integer.parseInt(uniqueResult(hql, uid,
				info.F_NOT_READ).toString()));// ����δ��
		hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS s WHERE s.userid=? AND s.status>=0 AND s.status<=2";
		info.setF_read(Integer.parseInt(uniqueResult(hql, uid).toString()));// ��������
		hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS s WHERE s.userid=? AND s.status=?";
		info.setS_not_read(Integer.parseInt(uniqueResult(hql, uid,
				info.S_NOT_READ).toString()));// İ����δ��
		hql = "SELECT COUNT(*) FROM ReceiverMessageBean AS s WHERE s.userid=? AND s.status>=3 AND s.status<=5";
		info.setS_read(Integer.parseInt(uniqueResult(hql, uid).toString()));// İ��������

		hql = "SELECT COUNT(*) FROM AfficheBean AS s WHERE s.userid=? AND s.status=? ";
		info.setSys_not_read(Integer.parseInt(uniqueResult(hql, uid,
				info.SYS_NOT_READ).toString()));// ϵͳδ��
		hql = "SELECT COUNT(*) FROM AfficheBean AS s WHERE s.userid=? ";
		info.setSys_read(Integer.parseInt(uniqueResult(hql, uid).toString()));// ϵ����

		hql = "SELECT COUNT(*) FROM SendMessageBean AS s WHERE s.userid=? ";
		info.setMy_send(Integer.parseInt(uniqueResult(hql, uid).toString()));// ����
		hql = "SELECT COUNT(*) FROM AfficheBean AS s WHERE s.userid=? ";
		info.setAffiche(Integer.parseInt(uniqueResult(hql, 0).toString()));// ����

		return info;
	}

	/**
	 * дϵͳ��Ϣ
	 * */
	public static void systemMsg(UserBean sender, UserBean receiver,
			String content) {
		if (sender != null && receiver != null
				&& StringUtils.isNotBlank(content)) {
			AfficheBean afficheBean = new AfficheBean();
			afficheBean.setContent(content);
			afficheBean.setSendtime(new Date());
			afficheBean.setTitle("ϵͳ��Ϣ");
			afficheBean.setStatus(MessageBean.STATUS_NEW);
			afficheBean.setUserid(receiver.getId());
			save(afficheBean);
		}
	}

	/**
	 * ɾ��ϵͳ��Ϣ
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
