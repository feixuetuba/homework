package com.liusoft.dlog4j.velocity;

import java.util.List;

import com.liusoft.dlog4j.SessionUserObject;
import com.liusoft.dlog4j.beans.DiaryOutlineBean;
import com.liusoft.dlog4j.beans.MusicOutlineBean;
import com.liusoft.dlog4j.beans.PhotoOutlineBean;
import com.liusoft.dlog4j.beans.UserFriendGroupBean;
import com.liusoft.dlog4j.dao.UserDAO;
import com.liusoft.dlog4j.dao.UserFriendGroupDAO;

public class DLOG_Friend_velocityTool {

	/**
	 * �����б�
	 */
	public List showFriendV(int userid) {
		return UserFriendGroupDAO.findByUserId(userid);
	}

	/**
	 * �������
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public List showGroupFriend(int userid, int g_id) {
		return UserFriendGroupDAO.findGroup(userid, g_id);
	}

	/**
	 * ����ĳ�˺��ѷ���ĺ�����
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public int groupFriend_count(int userid, int g_id) {
		if (userid <= 0)
			return -1;
		return UserFriendGroupDAO.getGroupFriendCount(userid, g_id);
	}

	/**
	 * ����ĳ�˺��ѷ���ĺ���
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public List groupFriends(int userid, int g_id) {
		if (userid <= 0)
			return null;
		return UserFriendGroupDAO.getGroupFriends(userid, g_id);
	}

	/**
	 * �����û�����id ��ȡ����
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public UserFriendGroupBean getGroup(int g_id) {
		return UserFriendGroupDAO.getUserFriendGroupBean(g_id);
	}

	/**
	 * �г�ĳ�˵ĺ���
	 * 
	 * @param user_id
	 * @param page
	 * @param count
	 * @param groupid
	 * @return
	 */
	public List friends(int user_id,int g_id,int page, int count) {
		if (user_id <= 0)
			return null;
		int fromIdx = (page - 1) * count;
		return UserFriendGroupDAO.listFriends(user_id, fromIdx, count,g_id);
	}

	/**
	 * �г����к��ѵ��ռ�
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DiaryOutlineBean> list_diarys_of_friends(int user_id, int page,
			int count, String g_id) {
		if (user_id < 0)
			return null;
		int from_idx = -1;
		if (page > 1 && count > 0)
			from_idx = (page - 1) * count;
		return UserFriendGroupDAO.listDiarysOfFriends(user_id, from_idx, count,
				Integer.parseInt(g_id));
	}

	/**
	 * �г����к��ѵ���Ƭ
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	public List<PhotoOutlineBean> list_photos_of_friends(int user_id, int page,
			int count, String g_id) {
		if (user_id < 0)
			return null;
		int from_idx = -1;
		if (page > 1 && count > 0)
			from_idx = (page - 1) * count;
		return UserFriendGroupDAO.listPhotosOfFriends(user_id, from_idx, count,
				Integer.parseInt(g_id));
	}

	/**
	 * �г����к��ѵĸ���
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	public List<MusicOutlineBean> list_songs_of_friends(int user_id, int page,
			int count, String g_id) {
		if (user_id < 0)
			return null;
		int from_idx = -1;
		if (page > 1 && count > 0)
			from_idx = (page - 1) * count;
		return UserFriendGroupDAO.listSongsOfFriends(user_id, from_idx, count,
				Integer.parseInt(g_id));
	}
	
	public UserFriendGroupBean getGroupDefault (int uid)
	{
		return UserFriendGroupDAO.getGroupDefault(uid);
	}

}
