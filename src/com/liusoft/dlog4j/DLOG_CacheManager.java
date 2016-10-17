/*
 *  DLOG_CacheManager.java
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
 *  Author: Winter Lau
 *  http://dlog4j.sourceforge.net
 *  
 */
package com.liusoft.dlog4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liusoft.dlog4j.dao.DAO;

/**
 * DLOG4J的缓存管理器
 * @author Winter Lau
 */
public class DLOG_CacheManager extends DAO {
	
	final static Log log = LogFactory.getLog(DLOG_CacheManager.class);
	
	/**
	 * 从缓存中获取对象
	 * @param cache_name
	 * @param key
	 * @return
	 */
	public static Object getObjectCached(String cache_name, Object key){
		return DAO.getObjectCached(cache_name, key);
	}
	
	/**
	 * 把对象放入缓存中
	 * @param cache_name
	 * @param key
	 * @param value
	 */
	public static void putObjectCached(String cache_name, Object key, Object value){
		DAO.setObjectCached(cache_name, key, value);
	}

	/**
	 * 删除缓存中的对象
	 * @param cache_name
	 * @param key
	 */
	public static void evictObjectCached(String cache_name, Object key){
		DAO.evictObjectCached(cache_name, key);
	}
}
