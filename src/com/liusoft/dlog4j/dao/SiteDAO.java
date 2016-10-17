/*
 *  SiteDAO.java
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

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import com.liusoft.dlog4j.DLOG_CacheManager;
import com.liusoft.dlog4j.DLOG_VisitorManager;
import com.liusoft.dlog4j.base.FunctionStatus;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * ����վ���Ӧ�����ݿ�ӿ� SiteBean��url�ֶα�����Ψһ����,url�ֶβ�Ӧ�����û������ֹ��޸�
 * 
 * @author liudong
 */
public class SiteDAO extends DAO {

	final static String CACHE_NEW_SITES = "query.newest.sites";

	/**
	 * ����ע���û��������������
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listSitesOrderByUserCount(int fromIdx, int count) {
		return listSitesOrderBy(fromIdx, count, "LIST_SITES_ORDER_BY_USERCOUNT");
	}

	/**
	 * �����ռ��������������
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listSitesOrderByDiaryCount(int fromIdx, int count) {
		return listSitesOrderBy(fromIdx, count,
				"LIST_SITES_ORDER_BY_DIARYCOUNT");
	}

	/**
	 * ������Ƭ�������������
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listSitesOrderByPhotoCount(int fromIdx, int count) {
		return listSitesOrderBy(fromIdx, count,
				"LIST_SITES_ORDER_BY_PHOTOCOUNT");
	}

	/**
	 * ���ո����������������
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listSitesOrderByMusicCount(int fromIdx, int count) {
		return listSitesOrderBy(fromIdx, count,
				"LIST_SITES_ORDER_BY_MUSICCOUNT");
	}

	/**
	 * �������ۻ����������������
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listSitesOrderByTopicCount(int fromIdx, int count) {
		return listSitesOrderBy(fromIdx, count,
				"LIST_SITES_ORDER_BY_TOPICCOUNT");
	}

	/**
	 * �����������
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	protected static List listSitesOrderBy(int fromIdx, int count, String hql) {
		Session ssn = getSession();
		Query q = ssn.getNamedQuery(hql);
		if (fromIdx > 0)
			q.setFirstResult(fromIdx);
		if (count > 0)
			q.setMaxResults(count);
		List objs = q.list();
		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			Object[] res = (Object[]) objs.get(i);
			int siteid = ((Number) res[0]).intValue();
			SiteBean site = new SiteBean(siteid);
			site.setUniqueName((String) res[1]);
			site.setFriendlyName((String) res[2]);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * ������������
	 * 
	 * @param key
	 * @return
	 */
	public static List searchSite(String key) {
		String pattern = '%' + key + '%';
		return executeNamedQuery("SEARCH_SITE", -1, 20, SiteBean.STATUS_NORMAL,
				pattern, pattern);
	}

	/**
	 * �г��Ƽ�����վ Ҳ����site_level>1����վ
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listRecommendSites(int fromIdx, int count) {
		return executeNamedQuery("LIST_RECOMMEND_SITES", fromIdx, count);
	}
	
	public static List listUnrecommendSites(int fromIdx, int count) {
		return executeNamedQuery("LIST_UNRECOMMEND_SITES", fromIdx, count);
	}

	/**
	 * �г�����ע���site
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listNewestSites(int fromIdx, int count) {
		return executeNamedQueryCacheable(CACHE_NEW_SITES, "LIST_NEW_SITES",
				fromIdx, count, SiteBean.STATUS_NORMAL);
	}

	/**
	 * �����ռǵĻ�Ծ�̶����г����ŵ�site
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listHotSitesViaDiary(int fromIdx, int count) {
		return listHotSites("DiaryBean", fromIdx, count);
	}

	/**
	 * ������Ƭ�Ļ�Ծ�̶����г����ŵ�site
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listHotSitesViaPhoto(int fromIdx, int count) {
		return listHotSites("PhotoBean", fromIdx, count);
	}

	/**
	 * �������۵Ļ�Ծ�̶����г����ŵ�site
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listHotSitesViaTopic(int fromIdx, int count) {
		return listHotSites("TopicBean", fromIdx, count);
	}

	/**
	 * �г����ŵ�site
	 * 
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	protected static List listHotSites(String bean, int fromIdx, int count) {
		StringBuffer hql = new StringBuffer(
				"SELECT d.site.id,d.site.uniqueName,d.site.friendlyName,(COUNT(d.id)*100+SUM(d.replyCount)*10+SUM(d.viewCount)) FROM ");
		hql.append(bean);
		hql
				.append(" d WHERE d.status=? AND d.site.status=0 GROUP BY d.site.id,d.site.uniqueName,d.site.friendlyName ORDER BY 4 DESC");
		List objs = executeQueryCacheable("dlog_home_info", hql.toString(),
				fromIdx, count, SiteBean.STATUS_NORMAL);
		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			int siteid = ((Number) ((Object[]) objs.get(i))[0]).intValue();
			String name1 = (String) ((Object[]) objs.get(i))[1];
			String name2 = (String) ((Object[]) objs.get(i))[2];
			SiteBean site = new SiteBean(siteid);
			site.setUniqueName(name1);
			site.setFriendlyName(name2);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * ����ע���վ������
	 * 
	 * @param site
	 * @return
	 */
	public static int getSiteCount() {
		return executeNamedStat("SITE_COUNT", SiteBean.STATUS_NORMAL)
				.intValue();
	}

	/**
	 * ��ͨ��������
	 * 
	 * @param site
	 */
	public static void createSite(SiteBean site) {
		site.setFunctionStatus(new FunctionStatus());
		Session ssn = getSession();
		try {
			beginTransaction();
			ssn.save(site);
			site.getOwner().setOwnSiteId(site.getId());
			ssn.update(site.getOwner());
			commit();
		} catch (HibernateException e) {
			rollback();
			throw e;
		}
	}

	/**
	 * ������վ����
	 * 
	 * @param site
	 */
	public static void updateSite(SiteBean site) {
		flush();
	}

	/**
	 * ������վ��Ż�ȡ��ϸ��Ϣ
	 * 
	 * @param site_id
	 * @return
	 * @throws HibernateException
	 */
	public static SiteBean getSiteByID(int site_id) {
		if (site_id < 1)
			return null;
		SiteBean site = (SiteBean) getBean(SiteBean.class, site_id);
		return (site != null && site.getStatus() >= 0) ? site : null;
	}

	/**
	 * ������վ��Ψһ��ʶ����ȡ��Ӧ��վ����ϸ��Ϣ
	 * 
	 * @param site_name
	 * @return
	 */
	public static SiteBean getSiteByName(String site_name) {
		if (StringUtils.isBlank(site_name))
			return null;
		SiteBean site = (SiteBean) namedUniqueResult("GET_SITE_BY_NAME",
				site_name);
		return (site != null && site.getStatus() >= 0) ? site : null;
	}

	/**
	 * ������վ������ȡ��Ӧ��վ����ϸ��Ϣ
	 * 
	 * @param site_name
	 * @return
	 */
	public static SiteBean getSiteByFriendlyName(String site_name) {
		if (StringUtils.isBlank(site_name))
			return null;
		SiteBean site = (SiteBean) namedUniqueResult(
				"GET_SITE_BY_FRIENDLYNAME", site_name);
		return (site != null && site.getStatus() >= 0) ? site : null;
	}

	/**
	 * ����վ���Զ�����������в�ѯ
	 * 
	 * @param vhost
	 * @return
	 */
	public static SiteBean getSiteByVhost(String vhost) {
		if (StringUtils.isBlank(vhost))
			return null;
		SiteBean site = (SiteBean) namedUniqueResult("GET_SITE_BY_VHOST", vhost);
		return (site != null && site.getStatus() >= 0) ? site : null;
	}

	// ======DLOG 3.5=================
	
	/**
	 * �û���������վ�㲢��¼
	 * @param uid
	 * @param v_uid
	 */
	public static void visitor(int uid, int v_uid) {
		String idstr = "";
		// ȡ����ķ�����id����
		List visitors = (List) DLOG_CacheManager.getObjectCached(
				"dlog_visitor", "visitor_" + uid);
		if (visitors == null) {
			idstr = getUserVisitor(uid);
			// Stringת��ΪList
			visitors = DLOG_VisitorManager.getVisitorIds(idstr);
			// ��״����List(����������)д�뻺��
			DLOG_CacheManager.putObjectCached("dlog_visitor", "visitor_" + uid,
					visitors);
		}
		// ���÷����ߵ�id
		StringBuffer hql2 = new StringBuffer();
		hql2.append(" UPDATE UserBean as ub ");
		hql2.append(" SET ub.visitor=? ");
		hql2.append(" WHERE ub.id=? ");
		if (visitors == null)
			visitors = new ArrayList();
		// �ж��Ƿ��е�ǰ������id
		if (!DLOG_VisitorManager.isPresence(visitors, v_uid)) {
			visitors.add(0, v_uid);
			// �������ݿ�
			commitUpdate(hql2.toString(), DLOG_VisitorManager.getIds(visitors),
					uid);
			// ���»���[������]
			DLOG_CacheManager.putObjectCached("dlog_visitor", "visitor_" + uid,
					DLOG_VisitorManager.getVisitorIds(idstr));
			// ����վ��ķ����б�
			List userVisitors = (List) DLOG_CacheManager.getObjectCached(
					"dlog_visitor", "user_" + uid);
			if (userVisitors != null) {
				// ��ջ�����վ��ķ����б�
				DLOG_CacheManager.evictObjectCached("dlog_visitor", "user_"
						+ uid);
				// �������û�����վ������б�
				userVisitorList(uid);
			}

		}

	}

	/**
	 * �б������
	 * @param uid
	 * @return
	 */
	public static List userVisitorList(int uid) {
		String idStr = "";
		List list = new ArrayList<UserBean>();
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ub.id, ub.portrait, ub.nickname ,ub.onlineStatus,ub.ownSiteId, ");
		hql.append(" (select d.uniqueName from SiteBean as d where d.owner.id=ub.id ) ");
		hql.append(" FROM UserBean AS ub");
		hql.append(" WHERE ");
		// ȡ����
		List userVisitors = (List) DLOG_CacheManager.getObjectCached(
				"dlog_visitor", "user_" + uid);
		// ����Ϊ��
		if (userVisitors == null) {
			idStr = getUserVisitor(uid);
			if (idStr.trim().equals(""))
				return null;
			// ������id��
			List visitorIds = DLOG_VisitorManager.getVisitorIds(idStr);
			// ͨ��id�� ƴд���
			hql.append(DLOG_VisitorManager.userVisitorQueryLink(visitorIds));
			userVisitors = executeQuery(hql.toString(), -1, -1);
			// ��װ����
			for (int i = 0; i < userVisitors.size(); i++) {
				int id = ((Number) ((Object[]) userVisitors.get(i))[0])
						.intValue();
				String name1 = (String) ((Object[]) userVisitors.get(i))[1];
				String name2 = (String) ((Object[]) userVisitors.get(i))[2];
				int name3 = ((Number) ((Object[]) userVisitors.get(i))[3])
						.intValue();
				int name4 = ((Number) ((Object[]) userVisitors.get(i))[4])
				.intValue();
				String name5 = (String) ((Object[]) userVisitors.get(i))[5];
				UserBean bean = new UserBean();
				bean.setId(id);
				bean.setPortrait(name1);
				bean.setNickname(name2);
				bean.setOnlineStatus(name3);
				bean.setOwnSiteId(name4);
				SiteBean bean2=new SiteBean();
				bean2.setUniqueName(name5);
				bean.setSite(bean2);				
				list.add(bean);
			}
			// д����
			DLOG_CacheManager.putObjectCached("dlog_visitor", "user_" + uid,
					list);
			return list;
		}
		return userVisitors;
	}

	/**
	 * 
	 */
	public static String getUserVisitor(int uid) {
		String idstr = "";
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ub.visitor ");
		hql.append(" FROM UserBean as ub");
		hql.append(" WHERE ub.id=? ");
		Object object = uniqueResult(hql.toString(), uid);
		if (object != null)
			idstr = (String) object;
		return idstr;
	}

}
