/*
 *  DLOG_Home_VelocityTool.java
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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.liusoft.dlog4j.DLOG_CacheManager;
import com.liusoft.dlog4j.Globals;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.dao.DiaryDAO;
import com.liusoft.dlog4j.dao.DlogDAO;
import com.liusoft.dlog4j.dao.PhotoDAO;
import com.liusoft.dlog4j.dao.SiteDAO;

/**
 * DLOG4J主页用的Toolbox
 * @author Winter Lau
 */
public class DLOG_Home_VelocityTool{
	
	private final static String CACHE_KEY = "dlog_home_info";
	
	/**
	 * 列出推荐的站点
	 * @param page
	 * @param count
	 * @return
	 */
	public List list_sites_recommend(int page, int count){
		StringBuffer nKey = new StringBuffer("recommend_sites_");
		nKey.append(page);
		nKey.append('_');
		nKey.append(count);
		List sites = (List)DLOG_CacheManager.getObjectCached(CACHE_KEY, nKey.toString());
		if(sites == null){
			int fromIdx = (page - 1) * count;
			sites = SiteDAO.listRecommendSites(fromIdx, count);
			if(page < 10 && sites!=null)
				DLOG_CacheManager.putObjectCached(CACHE_KEY, nKey.toString(), (Serializable)sites);
		}
		return sites;
	}

	/**
	 * 列出不被推荐的站点
	 * @param page
	 * @param count
	 * @return
	 */
	public List list_sites_unrecommend(int page, int count){
		StringBuffer nKey = new StringBuffer("unrecommend_sites_");
		nKey.append(page);
		nKey.append('_');
		nKey.append(count);
		List sites = (List)DLOG_CacheManager.getObjectCached(CACHE_KEY, nKey.toString());
		if(sites == null){
			int fromIdx = (page - 1) * count;
			sites = SiteDAO.listUnrecommendSites(fromIdx, count);
			if(page < 10 && sites!=null)
				DLOG_CacheManager.putObjectCached(CACHE_KEY, nKey.toString(), (Serializable)sites);
		}
		return sites;
	}

	/**
	 * 列出最新发表的专栏文章
	 * @param days
	 * @param count
	 * @return
	 */
	public List list_new_articles(int page, int count){		
		int fromIdx = (page - 1) * count;			
		return DlogDAO.listNewArticles(fromIdx, count);
	}

	/**
	 * 列出照片
	 * @param page
	 * @param count
	 * @return
	 */
	public List list_new_photos(int page, int count){
		int fromIdx = (page-1)*count;
		if(fromIdx < 0)
			fromIdx = 0;
		return PhotoDAO.listPhotos(-1, -1, -1, fromIdx, count);
	}
	
	/**
	 * 列出最热门的专栏文章
	 * @param days
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List list_hot_articles(int days, int count){
		List articles = DlogDAO.listHotArticles(days, count);
		Collections.sort(articles, Globals.random_comparator);
		return articles;
	}
	
	/**
	 * 列出最新推荐的文章
	 * @param page
	 * @param count
	 * @return
	 */
	public List list_recommend_articles(int page, int count){
		int fromIdx = (page - 1) * count;
		return DiaryDAO.listRecommendArticles(fromIdx, count);
	}

	/**
	 * 列出最热门的专栏文章
	 * @param days
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List list_hot_photos(int days, int count){
		List photos = DlogDAO.listHotPhotos(days, count);
		Collections.sort(photos, Globals.random_comparator);
		return photos;
	}

	/**
	 * 列出最热门的帖子
	 * @param days
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List list_hot_topics(int days, int count){
		List topics = DlogDAO.listHotTopics(days * 100, count);
		Collections.sort(topics, Globals.random_comparator);
		return topics;
	}
	
	/**
	 * 列出日记最为活跃的网站
	 * @param count
	 * @return
	 */
	public List list_hot_sites_via_diary(int page, int count){
		int fromIdx = (page - 1) * count;
		return SiteDAO.listHotSitesViaDiary(fromIdx, count);
	}

	/**
	 * 列出相册最为活跃的网站
	 * @param count
	 * @return
	 */
	public List list_hot_sites_via_photo(int page, int count){
		int fromIdx = (page - 1) * count;
		return SiteDAO.listHotSitesViaPhoto(fromIdx, count);
	}

	/**
	 * 列出讨论最为活跃的网站
	 * @param count
	 * @return
	 */
	public List list_hot_sites_via_topic(int page, int count){
		int fromIdx = (page - 1) * count;
		return SiteDAO.listHotSitesViaTopic(fromIdx, count);
	}
	
	/**
	 * 列出最新注册的site
	 * @param count
	 * @return
	 */
	public List list_newest_sites(int page, int count){		
		int fromIdx = (page - 1) * count;		
		return SiteDAO.listNewestSites(fromIdx, count);
	}

	/**
	 * 列出最新注册的site
	 * @param page
	 * @param count
	 * @param ofield
	 * @return
	 */
	public List list_sites_order_by(int page, int count, String ofield){
		int fromIdx = (page - 1) * count;
		if("user".equalsIgnoreCase(ofield))
			return SiteDAO.listSitesOrderByUserCount(fromIdx, count);
		if("diary".equalsIgnoreCase(ofield))
			return SiteDAO.listSitesOrderByDiaryCount(fromIdx, count);
		if("photo".equalsIgnoreCase(ofield))
			return SiteDAO.listSitesOrderByPhotoCount(fromIdx, count);
		if("music".equalsIgnoreCase(ofield))
			return SiteDAO.listSitesOrderByMusicCount(fromIdx, count);
		if("topic".equalsIgnoreCase(ofield))
			return SiteDAO.listSitesOrderByTopicCount(fromIdx, count);
		return list_newest_sites(page, count);
	}
	
	/**
	 * 从缓存中得到某个网站的所有友情链接
	 * @param site_id
	 * @return
	 */
	public List list_links_of_site(int site_id){
		SiteBean site = SiteDAO.getSiteByID(site_id);
		return (site!=null)?site.getLinks():null;
	}
	
}
