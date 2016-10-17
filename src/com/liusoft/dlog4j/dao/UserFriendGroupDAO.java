package com.liusoft.dlog4j.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;

import com.liusoft.dlog4j.beans.*;

public class UserFriendGroupDAO extends DAO {

	public static boolean saveUserFriendGroup(UserFriendGroupBean bean) {
		try {
			save(bean);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * 列出分组
	 */
	public static List findByUserId(int userid) {
		String hql = "FROM UserFriendGroupBean where userid=?";
		return UserFriendGroupDAO.findAll(hql, userid);

	}

	public static UserFriendGroupBean findByAGroupId(int groupid) {
		String hql = "FROM UserFriendGroupBean where groupid=?";
		return (UserFriendGroupBean) (UserFriendGroupDAO.findAll(hql, groupid))
				.get(0);

	}

	/**
	 * 列出分组的好友
	 */
	public static List findGroup(int userid, int g_id) {
		String hql = "FROM FriendBean where owner=? and groupid=?";
		return UserFriendGroupDAO.findAll(hql, userid, g_id);

	}

	/**
	 * 删除分组
	 * 
	 * @param UserFriendGroupBean
	 */
	public static void deleteFGroup(UserFriendGroupBean bean) {
		delete(bean);
	}

	public static void deleteFGroup(int groupid, int userid) {
		String sql = "delete from dlog_user_f_group where group_id=?";
		try {
			commitSQLUpdate(sql, groupid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更新分组
	 * 
	 * @param UserFriendGroupBean
	 * @return
	 */
	public static void updateFGroup(UserFriendGroupBean bean) {
		update(bean);
	}

	/**
	 * 统计分组好友总数
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public static int getGroupFriendCount(int userid, int groupid) {
		String hql = "USER_GROUP_FRIENF_COUNT";
		return executeNamedStatAsInt(hql, groupid, userid);
	}

	/**
	 * 获取某人分组的好友
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public static List getGroupFriends(int userid, int groupid) {
		String hql = "FROM FriendBean where owner=? and groupid=?";
		return UserFriendGroupDAO.findAll(hql, userid, groupid);

	}

	/**
	 * 更新统计值
	 * 
	 * @param groupcount
	 * @param groupid
	 * @return
	 */
	public static int UpdateGroupcount(int groupcount, int groupid) {

		String hql = "UPDATE UserFriendGroupBean AS f SET f.groupcount=?  WHERE f.groupid=? ";
		return executeUpdate(hql, groupcount, groupid);

	}

	/**
	 * 过得一组
	 * 
	 * @param id
	 * @return
	 */
	public static UserFriendGroupBean getUserFriendGroupBean(int id) {
		return (UserFriendGroupBean) getBean(UserFriendGroupBean.class, id);
	}

	/**
	 * 转移好友
	 * 
	 * @param userid
	 * @param friendid
	 * @param groupid
	 * @return
	 */
	public static boolean diverFriend(int userid, int friendid, int groupid) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE FriendBean AS fb");
		hql.append(" SET fb.groupid=? ");
		hql.append(" WHERE fb.owner=? AND fb.friend.id=?");
		return commitUpdate(hql.toString(), groupid, userid, friendid) > 0;
	}

	/**
	 * 列出某人分组的好友[分页]
	 * 
	 * @param ownerId
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listFriends(int ownerId, int fromIdx, int count,
			int groupid) {
		return executeNamedQuery("LIST_GROUP_FRIEND", fromIdx, count, ownerId,
				groupid);
	}

	/**
	 * 列出所有好友的日记
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<DiaryOutlineBean> listDiarysOfFriends(int userid,
			int from_idx, int count, int groupid) {
		// TODO Auto-generated method stub
		return executeNamedQueryCacheable("query.data_of_friends",
				"GROUP_DIARIES_OF_FRIENDS", from_idx, count,
				CatalogBean.TYPE_GENERAL, DiaryOutlineBean.STATUS_NORMAL,
				userid, groupid);
	}

	/**
	 * 列出所有好友的照片
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<PhotoOutlineBean> listPhotosOfFriends(int userid,
			int from_idx, int count, int groupid) {
		// TODO Auto-generated method stub
		return executeNamedQueryCacheable("query.data_of_friends",
				"GROUP_PHOTOS_OF_FRIENDS", from_idx, count,
				AlbumBean.TYPE_PUBLIC, PhotoOutlineBean.STATUS_NORMAL, userid,
				groupid);
	}

	/**
	 * 列出所有好友的歌曲
	 * 
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<MusicOutlineBean> listSongsOfFriends(int userid,
			int from_idx, int count, int groupid) {
		// TODO Auto-generated method stub
		return executeNamedQueryCacheable("query.data_of_friends",
				"GROUP_SONGS_OF_FRIENDS", from_idx, count, userid, groupid);
	}

	/**
	 * 用户默认分组id
	 */
	public static UserFriendGroupBean findByDefault(int userid) {
		String hql = "FROM UserFriendGroupBean where userid=? AND grouptype=?";
		return (UserFriendGroupBean) (UserFriendGroupDAO
				.findAll(hql, userid, 2)).get(0);

	}

	/**
	 * 删除分组 转移好友至默认分组
	 */
	public static void divertAllFriend(int userid, int groupid, int g_default) {
		// 过得当前分组
		String hql = "FROM FriendBean where owner=? and groupid=?";
		String sql = "UPDATE dlog_friend AS fb SET fb.group_id=? WHERE fb.user_id=? AND fb.friend_id=?";
		List list = UserFriendGroupDAO.findAll(hql, userid, groupid);
		if (list.size() == 0)
			return;
		int friendid;
		for (int i = 0; i < list.size(); i++) {
			FriendBean bean = (FriendBean) list.get(i);
			friendid = bean.getFriend().getId();
			try {
				commitSQLUpdate(sql, g_default, userid, friendid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取用户好友默认分组
	 */
	public static UserFriendGroupBean getGroupDefault(int uid) {
		String hql = "FROM UserFriendGroupBean where userid=? and grouptype=?";
		return (UserFriendGroupBean) uniqueResult(hql, uid, 2);

	}
}
