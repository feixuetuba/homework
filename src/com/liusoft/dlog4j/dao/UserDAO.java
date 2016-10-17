/*
 *  UserDAO.java
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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.liusoft.dlog4j.beans.AlbumBean;
import com.liusoft.dlog4j.beans.CatalogBean;
import com.liusoft.dlog4j.beans.DiaryOutlineBean;
import com.liusoft.dlog4j.beans.FriendBean;
import com.liusoft.dlog4j.beans.MusicOutlineBean;
import com.liusoft.dlog4j.beans.MyBlackListBean;
import com.liusoft.dlog4j.beans.PhotoOutlineBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * �û���ص����ݿ���ʽӿ�
 * @author liudong
 */
public class UserDAO extends DAO {
	
	/**
	 * �г����к��ѵ��ռ�
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<DiaryOutlineBean> listDiarysOfFriends(int userid, int from_idx, int count) {		
		return executeNamedQueryCacheable("query.data_of_friends","DIARIES_OF_FRIENDS", from_idx,count, CatalogBean.TYPE_GENERAL, DiaryOutlineBean.STATUS_NORMAL, userid);
	}

	/**
	 * �г����к��ѵ���Ƭ
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<PhotoOutlineBean> listPhotosOfFriends(int userid, int from_idx, int count) {		
		return executeNamedQueryCacheable("query.data_of_friends","PHOTOS_OF_FRIENDS", from_idx,count, AlbumBean.TYPE_PUBLIC, PhotoOutlineBean.STATUS_NORMAL, userid);
	}

	/**
	 * �г����к��ѵĸ���
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<MusicOutlineBean> listSongsOfFriends(int userid, int from_idx, int count) {		
		return executeNamedQueryCacheable("query.data_of_friends","SONGS_OF_FRIENDS", from_idx,count, userid);
	}

	/**
	 * ����ĳ��վ���ע���û���,���û��ָ��վ���򷵻�ע���û�����
	 * @param site
	 * @return
	 */
	public static int getUserCount(int site){
		String hql = "SELECT COUNT(*) FROM UserBean d WHERE d.status=?";
		if(site>0){
			hql += " AND d.site.id=?";
			return executeStatAsInt(hql, UserBean.STATUS_NORMAL, site);
		}
		return executeStatAsInt(hql, UserBean.STATUS_NORMAL);
	}

	/**
	 * ���Ӻ���
	 * @param friend
	 */
	public static boolean addFriend(FriendBean friend){
		if(namedUniqueResult("FRIEND", friend.getOwner(), friend.getFriend().getId())==null){
			save(friend);
			return true;
		}
		delBlackList(friend.getOwner(), friend.getFriend().getId());
		return false;
	}

	/**
	 * ���Ӻ����� 
	 * @param myId
	 * @param otherId
	 * @param type
	 * @return
	 */
	public static boolean addBlackList(int myId, int otherId, int type){
		if(namedUniqueResult("BLACKLIST", myId, otherId)==null){
			MyBlackListBean fbean = new MyBlackListBean();
			fbean.setAddTime(new Date());
			fbean.setMyId(myId);
			fbean.setOther(new UserBean(otherId));
			fbean.setType(type);
			save(fbean);
			return true;
		}
		//ɾ������
		deleteFriend(myId, new String[]{String.valueOf(otherId)});
		return false;
	}
	
	/**
	 * �Ӻ�������ɾ��ĳ���û�
	 * @param myId
	 * @param otherId
	 * @return
	 */
	public static boolean delBlackList(int myId, int otherId){
		return commitNamedUpdate("DELETE_BLACKLIST", myId, otherId)>0;
	}
	
	/**
	 * �ж�ĳ�û��Ƿ�����ĺ�������
	 * @param myId
	 * @param otherId
	 * @return
	 */
	public static boolean isUserInBlackList(int myId, int otherId){
		return namedUniqueResult("BLACKLIST", myId, otherId)!=null;
	}
	
	/**
	 * ��ѯ�����û�
	 * @param site_id
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listOnlineUsers(int site_id, int fromIdx, int count){
		return executeNamedQuery("ONLINE_USERS", fromIdx, count, site_id, UserBean.STATUS_ONLINE);
	}
	
	/**
	 * ��ѯ�����û���
	 * @param site_id
	 * @return
	 */
	public static int getOnlineUserCount(int site_id){
		return executeNamedStat("ONLINE_USER_COUNT", site_id, UserBean.STATUS_ONLINE).intValue();
	}
	
	/**
	 * �ж������Ƿ����
	 * @param ownerId
	 * @param friendId
	 * @return
	 */
	public static FriendBean getFriend(int ownerId, int friendId){
		return (FriendBean)namedUniqueResult("FRIEND", ownerId, friendId);
	}
	
	/**
	 * ����ĳ�˵ĺ�����
	 * @param ownerId
	 * @return
	 */
	public static int getFriendCount(int ownerId){
		return executeNamedStat("FRIEND_COUNT", ownerId).intValue();
	}
	
	/**
	 * �г�ĳ�˵ĺ���
	 * @param ownerId
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listFriends(int ownerId, int fromIdx, int count){
		return executeNamedQuery("LIST_FRIEND", fromIdx, count, ownerId);
	}

	/**
	 * ����ĳ�˵ĺ������û���
	 * @param ownerId
	 * @return
	 */
	public static int getBlackUserCount(int ownerId){
		return executeNamedStat("BLACKLIST_COUNT", ownerId).intValue();
	}
	
	/**
	 * �г�ĳ�˺������е��û�
	 * @param ownerId
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listBlackUsers(int ownerId, int fromIdx, int count){
		return executeNamedQuery("LIST_BLACKLIST", fromIdx, count, ownerId);
	}
	
	/**
	 * ɾ�������˵ĺ��ѹ�ϵ
	 * @param ownerId
	 * @param friendId
	 */
	public static int deleteFriend(int ownerId, String[] friendIds){
		if(friendIds == null|| friendIds.length == 0)
			return 0;
		StringBuffer hql = new StringBuffer("DELETE FROM FriendBean f WHERE f.owner=? AND f.friend.id IN (");
		for(int i=0;i<friendIds.length;i++){
			hql.append("?,");
		}
		hql.append("?)");
		Session ssn = getSession();
		try{
			beginTransaction();
			Query q = ssn.createQuery(hql.toString());
			q.setInteger(0, ownerId);
			int i=0;
			for(;i<friendIds.length;i++){
				String s_id = (String)friendIds[i];
				int id = -1;
				try{
					id = Integer.parseInt(s_id);
				}catch(Exception e){}
				q.setInteger(i+1, id);
			}
			q.setInteger(i+1, -1);
			int er = q.executeUpdate();
			commit();
			return er;
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}

	/**
	 * ɾ��������
	 * @param ownerId
	 * @param friendId
	 */
	public static int deleteBlacklist(int ownerId, String[] otherIds){
		if(otherIds == null|| otherIds.length == 0)
			return 0;
		StringBuffer hql = new StringBuffer("DELETE FROM MyBlackListBean f WHERE f.myId=? AND f.other.id IN (");
		for(int i=0;i<otherIds.length;i++){
			hql.append("?,");
		}
		hql.append("?)");
		Session ssn = getSession();
		try{
			beginTransaction();
			Query q = ssn.createQuery(hql.toString());
			q.setInteger(0, ownerId);
			int i=0;
			for(;i<otherIds.length;i++){
				String s_id = (String)otherIds[i];
				int id = -1;
				try{
					id = Integer.parseInt(s_id);
				}catch(Exception e){}
				q.setInteger(i+1, id);
			}
			q.setInteger(i+1, -1);
			int er = q.executeUpdate();
			commit();
			return er;
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}
	
	/**
	 * ����ĳ��վ��ע���û�
	 * @param site
	 * @param searchKey
	 * @return
	 */
	public static List searchUser(String searchKey){
		String key = searchKey + '%';
		return executeNamedQuery("SEARCH_USER", -1, 20, key, key);
	}
	
	/**
	 * �������û�
	 * @param user
	 * @param commit
	 * @return
	 */
	public static boolean createUser(UserBean user){
		if(user == null) 
			return false;
		Timestamp ct = new Timestamp(System.currentTimeMillis());
		user.setRegTime(ct);
		user.setLastTime(ct);
		user.setStatus(UserBean.STATUS_NORMAL);
		save(user);
		return true;
	}
	
	/**
	 * �г���ĳ����վ��ע����û�(_xxx_top_info.vm, users.vm)
	 * @param site_id
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listUsersFromSite(int site_id, int fromIdx, int count){
		return executeNamedQueryCacheable("query.new_users","LIST_REGUSERS_OF_SITE", fromIdx, count, site_id);
	}

	/**
	 * �г���ĳ����վ��ע����û���(users.vm)
	 * @param site_id
	 * @return
	 */
	public static int getUserCountFromSite(int site_id){
		return executeNamedStatAsIntCacheable("query.new_users","REGUSER_COUNT_OF_SITE", site_id);
	}
	
	/**
	 * �����û�����
	 * @param user
	 * @param commit
	 * @throws HibernateException
	 */
	public static void updateUser(UserBean user){
		DAO.update(user);
	}

	/**
	 * �����û�Ϊ����
	 * @param user
	 * @return
	 */
	public static int userLogin(UserBean user){
		String uuid = user.getUniqueCode();
		if(StringUtils.isBlank(uuid)){
			uuid = RandomStringUtils.randomAlphanumeric(16);
		}
		return commitNamedUpdate("USER_LOGIN", user.getLastAddr(), user.getLastTime(),
				UserBean.STATUS_ONLINE, user.getKeepDays(), uuid, user.getId());
	}
	
	/**
	 * ���keep_days�ֶ�ֵ,����ע��ʱ�����
	 * @param userid
	 * @param lastLogin ���һ�ε�¼��ʱ��
	 * @param manual_logout �Ƿ��ֹ�ע��
	 * @return
	 */
	public static int userLogout(int userid, Timestamp lastLogin, boolean manual_logout){
		Session ssn = getSession();
		if(ssn == null)
			return -1;
		try{
			beginTransaction();
			Query q = ssn.getNamedQuery(manual_logout?"USER_LOGOUT_1":"USER_LOGOUT_2");
			q.setInteger("online_status", UserBean.STATUS_OFFLINE);
			if(manual_logout)
				q.setInteger("keep_day", 0);
			q.setInteger("user_id", userid);
			q.setTimestamp("last_time", lastLogin);
			int er = q.executeUpdate();
			commit();
			return er;
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}
	
	/**
	 * �����û���Ż�ȡ�û���ϸ��Ϣ
	 * @param user_id
	 * @return
	 * @throws HibernateException
	 */
	public static UserBean getUserByID(int user_id){
		if(user_id < 0)
			return null;
		return (UserBean)getBean(UserBean.class, user_id);
	}
	
	/**
	 * ͨ���û�Ψһʶ�����ȡ�û�����
	 * @param unique_code
	 * @return
	 */
	public static UserBean getUserByUniqueCode(String unique_code){
		return (UserBean)uniqueResult("FROM UserBean u WHERE u.uniqueCode=?", unique_code);
	}

	/**
	 * ͨ���ֻ������ȡ�û�����
	 * @param mobile
	 * @return
	 */
	public static UserBean getUserByMobile(String mobile){
		return (UserBean)uniqueResult("FROM UserBean u WHERE u.mobile=?", mobile);
	}
	
	/**
	 * �����û��������û�����,�����û��ĵ�¼
	 * @param username
	 * @return
	 * @throws HibernateException
	 */
	public static UserBean getUserByName(String username){
		return (UserBean)namedUniqueResult("GET_USER_BY_NAME", username);
	}
	
	/**
	 * �����û��ǳƼ����û�����,����ע��ʱ������ͬ������
	 * @param nickname
	 * @return
	 * @throws HibernateException
	 */
	public static UserBean getUserByNickname(String nickname){
		return (UserBean)namedUniqueResult("GET_USER_BY_NICKNAME", nickname);
	}
	
}