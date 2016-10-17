/*
 *  VelocityTextTool.java
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

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.mira.lucene.analysis.MIK_CAnalyzer;

import com.liusoft.dlog4j.Globals;
import com.liusoft.dlog4j.SessionUserObject;
import com.liusoft.dlog4j.base._DiaryBase;
import com.liusoft.dlog4j.beans.DiaryBean;
import com.liusoft.dlog4j.beans.DiaryReplyBean;
import com.liusoft.dlog4j.beans.MusicBean;
import com.liusoft.dlog4j.beans.PhotoBean;
import com.liusoft.dlog4j.beans.PhotoReplyBean;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.TopicBean;
import com.liusoft.dlog4j.beans.TopicReplyBean;
import com.liusoft.dlog4j.dao.SiteDAO;
import com.liusoft.dlog4j.dao.UserDAO;
import dlog.common.search.SearchParameter;
import dlog.common.search.SearchProxy;

import com.liusoft.dlog4j.util.StringUtils;

/**
 * 搜索工具类
 * -Djava.ext.dirs=webapp/WEB-INF/lib;packages -cp webapp/WEB-INF/classes
 * @author Winter Lau
 */
public class DLOG_Search_VelocityTool{

	private final static int SCOPE_DIARY = 10;
	private final static int SCOPE_DIARY_REPLY = 11;
	
	private final static int SCOPE_PHOTO = 20;
	private final static int SCOPE_PHOTO_REPLY = 21;
	
	private final static int SCOPE_TOPIC = 30;
	private final static int SCOPE_TOPIC_REPLY = 31;
	
	private final static int SCOPE_MUSIC = 40;

	/**
	 * 搜索相关的日记
	 * @param diary
	 * @param 是否排除非本站的日记
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List search_rel_diarys(_DiaryBase diary, boolean exclusive,
			boolean random, int count){
		if(diary==null) return null;
		//生成搜索关键字
		String key = diary.getKeyword();
		if(StringUtils.isBlank(key)){
			//分析标题
			StringReader reader = new StringReader(diary.getTitle());
			Analyzer zer = new MIK_CAnalyzer();
			TokenStream ts = zer.tokenStream(null, reader);		
			try{
				StringBuffer tmpKey = new StringBuffer();
				do {
					Token t = ts.next();
					if(t == null) break;
					String tt = t.termText();
					if(tt.length() < 2)
						continue;
					if(tmpKey.length() > 0)
						tmpKey.append(' ');
					tmpKey.append(tt);
				} while(true);
				key = tmpKey.toString();
			}catch(IOException e){}
		}
		if(StringUtils.isBlank(key))
			return null;
		try{
			List logs = search_diary(exclusive?diary.getSite():null, null, key, -1, -1);
			if(logs!=null){
				//删除掉重复的日记
				Iterator iter = logs.iterator();
				while(iter.hasNext()){
					_DiaryBase ttt = (_DiaryBase)iter.next();
					if(ttt.getId()==diary.getId())
						iter.remove();
				}
				Collections.sort(logs, Globals.random_comparator);
				if(count > 0 && count < logs.size())
					return logs.subList(0, count);
				return logs;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 搜索用户
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public List search_user(String searchKey) throws Exception{
		if(StringUtils.isEmpty(searchKey))
			return null;
		long ct = System.currentTimeMillis();
		try{
			return UserDAO.searchUser(searchKey);
		}finally{
			saveSearchTime(ct);
		}
	}
	
	/**
	 * 搜索个人网记
	 * @param searchKey
	 * @return
	 */
	public List search_site(String searchKey){
		if(StringUtils.isEmpty(searchKey))
			return null;
		long ct = System.currentTimeMillis();
		try{
			return SiteDAO.searchSite(searchKey);
		}finally{
			saveSearchTime(ct);
		}
	}
	
	/**
	 * 搜索日记
	 * 
	 * @param site
	 * @param user
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public List search_diary(final SiteBean site, final SessionUserObject user,
			final String searchKey, int page, int count) throws Exception {
		return search(site, user, searchKey, SCOPE_DIARY, page, count);
	}

	public List search_diary(final SiteBean site, final SessionUserObject user,
			final String searchKey) throws Exception {
		return search(site, user, searchKey, SCOPE_DIARY, -1, -1);
	}
	public int search_diary_count(final SiteBean site, final SessionUserObject user,
			final String searchKey) throws Exception {
		return obj_count(site, user, searchKey, SCOPE_DIARY);
	}

	/**
	 * 搜索音乐
	 * 
	 * @param site
	 * @param user
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public List search_music(final SiteBean site, final SessionUserObject user,
			final String searchKey) throws Exception {
		return search(site, user, searchKey, SCOPE_MUSIC, -1, -1);
	}

	/**
	 * 搜索日记评论
	 * 
	 * @param site
	 * @param user
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public List search_diary_reply(final SiteBean site, final SessionUserObject user,
			final String searchKey) throws Exception {
		return search(site, user, searchKey, SCOPE_DIARY_REPLY, -1, -1);
	}

	/**
	 * 搜索相片
	 * 
	 * @param site
	 * @param user
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public List search_photo(final SiteBean site, final SessionUserObject user,
			final String searchKey) throws Exception {
		return search(site, user, searchKey, SCOPE_PHOTO, -1, -1);
	}

	/**
	 * 搜索相片评论
	 * 
	 * @param site
	 * @param user
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public List search_photo_reply(final SiteBean site, final SessionUserObject user,
			final String searchKey) throws Exception {
		return search(site, user, searchKey, SCOPE_PHOTO_REPLY, -1, -1);
	}

	/**
	 * 搜索帖子
	 * 
	 * @param site
	 * @param user
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public List search_topic(final SiteBean site, final SessionUserObject user,
			final String searchKey) throws Exception {
		return search(site, user, searchKey, SCOPE_TOPIC, -1, -1);
	}

	/**
	 * 搜索帖子评论
	 * 
	 * @param site
	 * @param user
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public List search_topic_reply(final SiteBean site, final SessionUserObject user,
			final String searchKey) throws Exception {
		return search(site, user, searchKey, SCOPE_TOPIC_REPLY, -1, -1);
	}

	/**
	 * 搜索
	 * @param site
	 * @param user
	 * @param searchKey
	 * @param scope
	 * @return
	 * @throws Exception 
	 */
	protected List search(final SiteBean site, final SessionUserObject user,
			final String searchKey, final int scope, int page, int count) throws Exception 
	{
		if(StringUtils.isEmpty(searchKey))
			return null;
		
		long ct = System.currentTimeMillis();
		try{
			if(scope == 6)
				return UserDAO.searchUser(searchKey);

			SearchParameter param = new MySearchParameter(site,searchKey,scope);
			int fromIdx = -1;
			if(page>0&&count>0)
				fromIdx = (page-1)*count;
			return SearchProxy.search(param,fromIdx, count);			
		}catch(IOException e){
			//TODO: 输出错误日志
		}finally{
			saveSearchTime(ct);
		}
		return null;
	}

	/**
	 * 搜索
	 * @param site
	 * @param user
	 * @param searchKey
	 * @param scope
	 * @return
	 * @throws Exception 
	 */
	protected int obj_count(final SiteBean site, final SessionUserObject user,
			final String searchKey, final int scope) throws Exception 
	{
		if(StringUtils.isEmpty(searchKey))
			return 0;
		
		try{
			SearchParameter param = new MySearchParameter(site,searchKey,scope);
			return SearchProxy.objectCount(param);			
		}catch(IOException e){
			//TODO: 输出错误日志
		}
		return -1;
	}

	/**
	 * 返回上次搜索所花的时间
	 * 
	 * @return
	 */
	public long get_search_time() {
		Long sTime = (Long)searchTimes.get();
		return (sTime!=null)?sTime.longValue():-1;
	}

	/**
	 * 在线程本地存储区中保存搜索所用的时间
	 * @param lastTime
	 */
	protected void saveSearchTime(long lastTime){
		long st = System.currentTimeMillis() - lastTime;
		searchTimes.set(st);
	}

	/**
	 * 搜索条件
	 */
	private static class MySearchParameter implements SearchParameter {
		private SiteBean site;
		private String searchKey;
		private int scope;
		public MySearchParameter(SiteBean site, String key, int scope){
			this.site = site;
			this.scope = scope;
			this.searchKey = key;
		}
		
		public String getSearchKey() {
			return searchKey;
		}
		public HashMap getConditions() {
			if (site != null) {
				HashMap<String,Integer> conds = new HashMap<String,Integer>();
				conds.put("site.id", site.getId());
				return conds;
			}
			return null;
		}

		public Class getSearchObject() {
			switch (scope) {
			case SCOPE_DIARY:
				return DiaryBean.class;
			case SCOPE_DIARY_REPLY:
				return DiaryReplyBean.class;
			case SCOPE_PHOTO:
				return PhotoBean.class;
			case SCOPE_PHOTO_REPLY:
				return PhotoReplyBean.class;
			case SCOPE_TOPIC:
				return TopicBean.class;
			case SCOPE_TOPIC_REPLY:
				return TopicReplyBean.class;
			case SCOPE_MUSIC:
				return MusicBean.class;
			}
			return null;
		}
	}
	
	private final static ThreadLocal<Long> searchTimes = new ThreadLocal<Long>();
	
}
