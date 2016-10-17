/*
 *  TagDAO.java
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

import org.hibernate.Query;

import com.liusoft.dlog4j.base.MessageInfo;
import com.liusoft.dlog4j.base._BeanBase;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.TagBean;

/**
 * 访问标签的数据库接口
 * 
 * @author Winter Lau
 */
public class TagDAO extends DAO {

	private final static String CACHE_KEY = "DLOG4J_tags";

	/**
	 * 获取标签数
	 * 
	 * @param site
	 * @return
	 */
	public static int getTagCount(SiteBean site) {
		String hql = "SELECT COUNT(*) FROM TagBean t";
		if (site != null) {
			hql += " WHERE t.site.id = ?";
			return executeStatCacheable(CACHE_KEY, hql, site.getId())
					.intValue();
		}

		return executeStatCacheable(CACHE_KEY, hql).intValue();
	}

	/**
	 * 按照热门程度列出标签
	 * 
	 * @param site
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listTags(SiteBean site, int fromIdx, int count) {
		StringBuffer hql = new StringBuffer(
				"SELECT t.name,COUNT(*) FROM TagBean t");
		if (site != null)
			hql.append(" WHERE t.site.id = ?");
		hql.append(" GROUP BY t.name ORDER BY 2 DESC");
		Query query = getSession().createQuery(hql.toString());
		query.setCacheable(true);
		query.setCacheRegion(CACHE_KEY);
		if (site != null)
			query.setInteger(0, site.getId());
		if (fromIdx > 0)
			query.setFirstResult(fromIdx);
		if (count > 0)
			query.setMaxResults(count);
		List<String> tags = new ArrayList<String>();
		List results = query.list();
		for (int i = 0; results != null && i < results.size(); i++) {
			tags.add((String) ((Object[]) results.get(i))[0]);
		}
		return tags;
	}

	/**
	 * 删除某个对象的标签列表
	 * 
	 * @param ref_id
	 * @param ref_type
	 * @see com.liusoft.dlog4j.dao.DiaryDAO#update(DiaryBean, boolean)
	 * @see com.liusoft.dlog4j.dao.PhotoDAO#update(int, PhotoBean, String)
	 * @see com.liusoft.dlog4j.dao.BBSTopicDAO#update(TopicBean)
	 */
	public static void deleteTagByRefId(int ref_id, int ref_type) {
		executeNamedUpdate("DELETE_TAG", ref_id, ref_type);
	}

	/**
	 * 获取指向某个标签的日记数
	 * 
	 * @param site
	 * @param tag
	 * @return
	 */
	public static int getDiaryCountForTag(SiteBean site, String tag) {
		if (site != null)
			return gettObjectCountForTag(site, TagBean.TYPE_DIARY, tag);
		return getObjectCountForTag(TagBean.TYPE_DIARY, tag);
	}

	/**
	 * 获取指向某个标签的照片数
	 * 
	 * @param site
	 * @param tag
	 * @return
	 */
	public static int getPhotoCountForTag(SiteBean site, String tag) {
		if (site != null)
			return gettObjectCountForTag(site, TagBean.TYPE_PHOTO, tag);
		return getObjectCountForTag(TagBean.TYPE_PHOTO, tag);
	}

	/**
	 * 获取指向某个标签的讨论数
	 * 
	 * @param site
	 * @param tag
	 * @return
	 */
	public static int getTopicCountForTag(SiteBean site, String tag) {
		if (site != null)
			return gettObjectCountForTag(site, TagBean.TYPE_BBS, tag);
		return getObjectCountForTag(TagBean.TYPE_BBS, tag);
	}

	/**
	 * 列出指定标签的所有日记
	 * 
	 * @param site
	 * @param tagname
	 * @return
	 */
	public static List listDiaryForTag(SiteBean site, String tagname,
			int fromIdx, int count) {
		return listObjectsForTags(site, "DiaryOutlineBean", TagBean.TYPE_DIARY,
				tagname, fromIdx, count);
	}

	/**
	 * 列出指定标签的所有相片
	 * 
	 * @param site
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listPhotosForTag(SiteBean site, String tagname,
			int fromIdx, int count) {
		return listObjectsForTags(site, "PhotoOutlineBean", TagBean.TYPE_PHOTO,
				tagname, fromIdx, count);
	}

	/**
	 * 列出指定标签的所有论坛帖子
	 * 
	 * @param site
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listTopicsForTag(SiteBean site, String tagname,
			int fromIdx, int count) {
		return listObjectsForTags(site, "TopicOutlineBean", TagBean.TYPE_BBS,
				tagname, fromIdx, count);
	}

	/**
	 * 列出指定标签的所有日记
	 * 
	 * @param site
	 * @param tagname
	 * @return
	 */
	public static List listDiaryForTag(String tagname, int fromIdx, int count) {
		return listObjectsForTags("DiaryOutlineBean", TagBean.TYPE_DIARY,
				tagname, fromIdx, count);
	}

	/**
	 * 列出指定标签的所有相片
	 * 
	 * @param site
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listPhotosForTag(String tagname, int fromIdx, int count) {
		return listObjectsForTags("PhotoOutlineBean", TagBean.TYPE_PHOTO,
				tagname, fromIdx, count);
	}

	/**
	 * 列出指定标签的所有论坛帖子
	 * 
	 * @param site
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listTopicsForTag(String tagname, int fromIdx, int count) {
		return listObjectsForTags("TopicOutlineBean", TagBean.TYPE_BBS,
				tagname, fromIdx, count);
	}

	/**
	 * 列出标签所指向的对象
	 * 
	 * @param site
	 * @param beanName
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	protected static List listObjectsForTags(SiteBean site, String beanName,
			int objType, String tagname, int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("FROM ");
		hql.append(beanName);
		hql
				.append(" AS d WHERE d.status=? AND d.site.id=? AND d.id IN (SELECT t.refId FROM TagBean AS t WHERE t.site.id=? AND t.refType=? AND t.name=?) ORDER BY d.id DESC");
		return executeQueryCacheable(CACHE_KEY, hql.toString(), fromIdx, count,
				_BeanBase.STATUS_NORMAL, site.getId(), site.getId(), objType,
				tagname);
	}

	/**
	 * 列出标签所指向的对象
	 * 
	 * @param beanName
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	protected static List listObjectsForTags(String beanName, int objType,
			String tagname, int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("FROM ");
		hql.append(beanName);
		hql
				.append(" AS d WHERE d.status=? AND d.id IN (SELECT t.refId FROM TagBean AS t WHERE t.refType=? AND t.name=?) ORDER BY d.id DESC");
		return executeQueryCacheable(CACHE_KEY, hql.toString(), fromIdx, count,
				_BeanBase.STATUS_NORMAL, objType, tagname);
	}

	/**
	 * 列出标签所指向的对象个数
	 * 
	 * @param site
	 * @param beanName
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	protected static int gettObjectCountForTag(SiteBean site, int objType,
			String tagname) {
		return executeNamedStatAsIntCacheable(CACHE_KEY, "TAG_COUNT_OF_SITE",
				site.getId(), objType, tagname);
	}

	/**
	 * 列出标签所指向的对象个数
	 * 
	 * @param beanName
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	protected static int getObjectCountForTag(int objType, String tagname) {
		return executeNamedStatAsIntCacheable(CACHE_KEY, "TAG_COUNT", objType,
				tagname);
	}

	// ================DLOG 3.5=========================
	/**
	 * 列出站点标签
	 */
	public static List tags(int sid) {

		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT t.id,t.name, ");
		hql
				.append(" (SELECT COUNT(*) FROM TagBean as tb WHERE tb.refType=1 AND tb.name =t.name AND tb.site.id=? ), ");
		hql
				.append(" (SELECT COUNT(*) FROM TagBean as tb WHERE tb.refType=2 AND tb.name =t.name AND tb.site.id=? ) ");

		hql.append(" FROM TagBean AS t ");
		hql.append(" WHERE  t.site.id=? ");
		hql.append(" GROUP BY t.name");
		return executeQuery(hql.toString(), -1, -1, sid,sid,sid);
	}

	/**
	 * 列出站点标签所有日记对象
	 */
	public static List tags_diary(int sid) {

		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT t.id,t.name, ");
		hql
				.append(" (SELECT COUNT(*) FROM TagBean as tb WHERE tb.refType=1 AND tb.name =t.name  ), ");
		hql
				.append(" (SELECT COUNT(*) FROM TagBean as tb WHERE tb.refType=2 AND tb.name =t.name  ) ");

		hql.append(" FROM TagBean AS t ");
		hql.append(" WHERE  t.site.id=? ");
		hql.append(" GROUP BY t.name");
		return executeQuery(hql.toString(), -1, -1, sid);
	}

	/**
	 * 列出站点标签所有图片对象
	 */
	public static List tags_photo(int sid) {

		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT t.id,t.name, ");
		hql
				.append(" (SELECT COUNT(*) FROM TagBean as tb WHERE tb.refType=1 AND tb.name =t.name  ), ");
		hql
				.append(" (SELECT COUNT(*) FROM TagBean as tb WHERE tb.refType=2 AND tb.name =t.name  ) ");

		hql.append(" FROM TagBean AS t ");
		hql.append(" WHERE  t.site.id=? ");
		hql.append(" GROUP BY t.name");
		return executeQuery(hql.toString(), -1, -1, sid);
	}
}
