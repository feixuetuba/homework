/*
 *  DLOG_User_VelocityTool.java
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
package com.liusoft.dlog4j.velocity;

import java.util.List;

import org.hibernate.HibernateException;

import com.liusoft.dlog4j.SessionUserObject;
import com.liusoft.dlog4j.base.MessageInfo;
import com.liusoft.dlog4j.beans.DiaryOutlineBean;
import com.liusoft.dlog4j.beans.MessageBean;
import com.liusoft.dlog4j.beans.MusicOutlineBean;
import com.liusoft.dlog4j.beans.PhotoOutlineBean;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.dao.MessageDAO;
import com.liusoft.dlog4j.dao.TagDAO;
import com.liusoft.dlog4j.dao.UserDAO;
import com.liusoft.dlog4j.dao.UserFriendGroupDAO;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * ���û���ص�Toolbox
 * 
 * @author Winter Lau
 */
public class DLOG_User_VelocityTool {

	/**
	 * �г����к��ѵ��ռ�
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DiaryOutlineBean> list_diarys_of_friends(
			SessionUserObject user, int page, int count) {
		if (user == null)
			return null;
		int from_idx = -1;
		if (page > 1 && count > 0)
			from_idx = (page - 1) * count;
		return UserDAO.listDiarysOfFriends(user.getId(), from_idx, count);
	}

	/**
	 * �г����к��ѵ���Ƭ
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	public List<PhotoOutlineBean> list_photos_of_friends(
			SessionUserObject user, int page, int count) {
		if (user == null)
			return null;
		int from_idx = -1;
		if (page > 1 && count > 0)
			from_idx = (page - 1) * count;
		return UserDAO.listPhotosOfFriends(user.getId(), from_idx, count);
	}

	/**
	 * �г����к��ѵĸ���
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	public List<MusicOutlineBean> list_songs_of_friends(SessionUserObject user,
			int page, int count) {
		if (user == null)
			return null;
		int from_idx = -1;
		if (page > 1 && count > 0)
			from_idx = (page - 1) * count;
		return UserDAO.listSongsOfFriends(user.getId(), from_idx, count);
	}

	/**
	 * �����û���Ż�ȡ�û�����ϸ��Ϣ(_global.vm)
	 * 
	 * @param user_id
	 * @return
	 * @throws HibernateException
	 */
	public UserBean user(int user_id) {
		if (user_id <= 0)
			return null;
		UserBean user = UserDAO.getUserByID(user_id);
		return user;
	}

	public UserBean user(String username) {
		if (StringUtils.isBlank(username))
			return null;
		return UserDAO.getUserByName(username);
	}

	/**
	 * ��ȡע���û�����(users.vm)
	 * 
	 * @param site
	 * @return
	 */
	public int user_count(SiteBean site) {
		if (site == null)
			return -1;
		return UserDAO.getUserCountFromSite(site.getId());
	}

	/**
	 * �г���ĳ����վ��ע����û�(_xxx_top_info.vm)
	 * 
	 * @param site
	 * @param page
	 * @param count
	 * @return
	 */
	public List list_users(SiteBean site, int page, int count) {
		if (site == null)
			return null;
		int fromidx = (page - 1) * count;
		return UserDAO.listUsersFromSite(site.getId(), fromidx, count);
	}

	/**
	 * �г�ĳ����վע��������û�
	 * 
	 * @param site
	 * @param page
	 * @param count
	 * @return
	 */
	public List online_users(SiteBean site, int page, int count) {
		if (site == null)
			return null;
		if (count < 0)
			count = 50;
		int fromidx = (page - 1) * count;
		if (fromidx < 0)
			fromidx = 0;
		return UserDAO.listOnlineUsers(site.getId(), fromidx, count);
	}

	/**
	 * ��ȡ�����û���
	 * 
	 * @param site
	 * @return
	 */
	public int online_user_count(SiteBean site) {
		if (site == null)
			return -1;
		return UserDAO.getOnlineUserCount(site.getId());
	}

	/**
	 * ����ĳ�˵ĺ�����
	 * 
	 * @param user_id
	 * @return
	 */
	public int friend_count(int user_id) {
		if (user_id <= 0)
			return -1;
		return UserDAO.getFriendCount(user_id);
	}

	/**
	 * �г�ĳ�˵ĺ���
	 * 
	 * @param user_id
	 * @param page
	 * @param count
	 * @return
	 */
	public List friends(int user_id, int page, int count) {
		if (user_id <= 0)
			return null;
		int fromIdx = (page - 1) * count;
		if (fromIdx < 0)
			fromIdx = 0;
		return UserDAO.listFriends(user_id, fromIdx, count);
	}

	/**
	 * �г�ĳ�˵ĺ������е������û�
	 * 
	 * @param user_id
	 * @param page
	 * @param count
	 * @return
	 */
	public List black_users(int user_id, int page, int count) {
		if (user_id <= 0)
			return null;
		int fromIdx = (page - 1) * count;
		if (fromIdx < 0)
			fromIdx = 0;
		return UserDAO.listBlackUsers(user_id, fromIdx, count);
	}

	/**
	 * ��ȡĳ�˵ĺ������û���
	 * 
	 * @param user_id
	 * @return
	 */
	public int black_user_count(int user_id) {
		if (user_id <= 0)
			return -1;
		return UserDAO.getBlackUserCount(user_id);
	}

	/**
	 * ��ȡ����Ϣ����
	 * 
	 * @param user
	 * @return
	 */
	public int msg_count(SessionUserObject user) {
		if (user == null)
			return -1;
		return MessageDAO.getMessageCount(user.getId());
	}

	/**
	 * �г�ĳ�˽��յ��Ķ���Ϣ
	 * 
	 * @param user
	 * @param fromId
	 * @param count
	 * @return
	 */
	public List msgs(SessionUserObject user, int page, int count) {
		if (user == null)
			return null;
		int fromIdx = (page - 1) * count;
		return MessageDAO.listMsgs(user.getId(), fromIdx, count);
	}

	/**
	 * �Ķ���������Ϣ
	 * 
	 * @param msg
	 */
	public void read_msg(MessageBean msg) {
		if (msg != null)
			MessageDAO.readMsg(msg);
	}

	// ======================DLOG 3.5=============================
	/**
	 * ��ȡϵͳ��Ϣ
	 */
	public List read_system(int uid, int fromIdx, int fetchCount) {
		int fromIdx_ = (fromIdx - 1) * fetchCount;
		return MessageDAO.getSystem(uid, fromIdx_, fetchCount);
	}

	/**
	 * ��ȡ����
	 */
	public List read_affiche(int fromIdx, int fetchCount) {
		int fromIdx_ = (fromIdx - 1) * fetchCount;
		return MessageDAO.getAffiche(fromIdx_, fetchCount);

	}

	/**
	 * ��ȡ������Ϣ�б�
	 */
	public List read_friend(int uid, int fromIdx, int fetchCount) {
		int fromIdx_ = (fromIdx - 1) * fetchCount;
		return MessageDAO.getFriend(uid, fromIdx_, fetchCount);

	}

	/**
	 * ��ȡİ������Ϣ�б�
	 */
	public List read_stranger(int uid, int fromIdx, int fetchCount) {
		int fromIdx_ = (fromIdx - 1) * fetchCount;
		return MessageDAO.getStranger(uid, fromIdx_, fetchCount);

	}

	/**
	 * ��ȡ���͵���Ϣ�б�
	 */
	public List read_mysend(int uid, int fromIdx, int fetchCount) {
		int fromIdx_ = (fromIdx - 1) * fetchCount;
		return MessageDAO.getMySend(uid, fromIdx_, fetchCount);

	}

	/**
	 * ��ȡ���͵���Ϣ����
	 */
	public Object read_mysend_content(int msgid, int uid, int fuid) {

		return MessageDAO.getMySendContent(msgid, uid, fuid);

	}

	/**
	 * ��ȡ��������
	 */
	public Object read_affiche_content(int msgid, int uid) {
		return MessageDAO.getAfficheContent(msgid, uid);

	}

	/**
	 * ��ȡ���Ѷ�������
	 */
	public Object read_friend_content(int msgid, int id) {

		return MessageDAO.getFriendContent(msgid, id);

	}

	/**
	 * ��ȡİ���˶�������
	 */
	public Object read_stranger_content(int msgid, int id) {
		return MessageDAO.getStrangerContent(msgid, id);

	}

	/**
	 * ��ȡϵͳ��������
	 */
	public Object read_system_content(int msgid, int uid) {
		return MessageDAO.getSystemContent(msgid, uid);

	}

	/**
	 * ���Ѷ�������
	 */
	public int msg_friend_count(int uid) {
		return MessageDAO.getMsgFriendCount(uid);
	}

	/**
	 * İ���˶�������
	 */
	public int msg_stranger_count(int uid) {
		return MessageDAO.getMsgStrangerCount(uid);
	}

	/**
	 * ϵͳ��������
	 */
	public int msg_system_count(int uid) {
		return MessageDAO.getMsgSystemCount(uid);
	}

	/**
	 * ϵͳ��������
	 */
	public int msg_mysend_count(int uid) {
		return MessageDAO.getMsgMySendCount(uid);
	}

	/**
	 * ϵͳ��������
	 */
	public int msg_affiche_count(int uid) {
		return MessageDAO.getMsgAfficheCount(uid);
	}

	/**
	 * ϵͳ����ͳ��
	 */
	public MessageInfo msg_info(int uid) {
		return MessageDAO.getMsgInfo(uid);
	}

	/**
	 * �б�ǰվ������б�ǩ
	 */
	public List tags(SiteBean bean) {
		return TagDAO.tags(bean.getId());
	}

	/**
	 * ��ȡָ��ĳ����ǩ���ռ���
	 * 
	 * @param site
	 * @param tagname
	 * @return
	 */
	public int diary_count_of_tag(SiteBean site, String tagname) {
		if (StringUtils.isEmpty(tagname))
			return -1;
		return TagDAO.getDiaryCountForTag(site, tagname);
	}

	/**
	 * ��ȡָ��ĳ����ǩ����Ƭ��
	 * 
	 * @param site
	 * @param tagname
	 * @return
	 */
	public int photo_count_of_tag(SiteBean site, String tagname) {
		if (StringUtils.isEmpty(tagname))
			return -1;
		return TagDAO.getPhotoCountForTag(site, tagname);
	}

	/**
	 * �г�ĳ����ǩ����ע�������ռ�
	 * 
	 * @param site
	 * @param tagname
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List diarys_of_tag(SiteBean site, String tagname) {
		if (StringUtils.isEmpty(tagname))
			return null;

		if (site == null)
			return TagDAO.listDiaryForTag(tagname, -1, -1);
		else
			return TagDAO.listDiaryForTag(site, tagname, -1, -1);
	}

	/**
	 * �г�ĳ����ǩ����ע��������Ƭ
	 * 
	 * @param site
	 * @param tagname
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List photos_of_tag(SiteBean site, String tagname) {
		if (StringUtils.isEmpty(tagname))
			return null;
		if (site == null)
			return TagDAO.listPhotosForTag(tagname, -1, -1);
		else
			return TagDAO.listPhotosForTag(site, tagname, -1, -1);
	}

}
