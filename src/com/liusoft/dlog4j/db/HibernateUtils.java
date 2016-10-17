/*
 *  HibernateUtils.java
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
package com.liusoft.dlog4j.db;

import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;

import org.hibernate.Session;

import com.liusoft.util.db.Hibernate;

/**
 * DLOG4J本身对Hibernate的操作接口的封装
 * @author liudong
 */
public class HibernateUtils {

	private final static String HIBERNATE_CFG = "/hibernate.cfg.xml";
	private static Hibernate hibernate;
	
	static{
		URL xml = HibernateUtils.class.getResource(HIBERNATE_CFG);
		hibernate = Hibernate.init(xml);
	}
	
	/**
	 * Initialize the hibernate environment
	 * @param context
	 * @throws MalformedURLException 
	 */
	public synchronized final static void init(){
		//Nothing to do
	}

	/**
	 * 释放所有Hibernate占用的资源 
	 * @see com.liusoft.dlog4j.servlet.DLOG_ActionServlet#destroy()
	 */
	public synchronized final static void destroy(){
		if(hibernate != null)
			hibernate.destroy();
	}

	public final static Session getSession() {
		if(hibernate != null)
			return hibernate.getSession();
		return null;
	}

	public final static Connection getConnection() {
		if(hibernate != null)
			return hibernate.getConnection();
		return null;
	}

	public final static void beginTransaction() {
		if(hibernate != null)
			hibernate.beginTransaction();
	}

	public final static void closeSession() {
		if(hibernate != null)
			hibernate.closeSession();
	}

	public final static void commit() {
		if(hibernate != null)
			hibernate.commit();
	}

	public final static void rollback() {
		if(hibernate != null)
			hibernate.rollback();
	}

	public final static Object getObjectCached(String cache_name, Object key) {
		return hibernate.getObjectCached(cache_name, key);
	}

	public final static void setObjectCached(String cache_name, Object key, Object value) {
		hibernate.setObjectCached(cache_name, key, value);
	}

	public final static void evictObjectCached(String cache_name, Object key){
		hibernate.evictObjectCached(cache_name, key);
	}
	
	public final static void evict(Class persistentClass, Serializable id) {
		hibernate.evict(persistentClass, id);
	}

	public final static void evict(Class persistentClass) {
		hibernate.evict(persistentClass);
	}

	public final static void evictCollection(String roleName, Serializable id) {
		hibernate.evictCollection(roleName, id);
	}

	public final static void evictCollection(String roleName) {
		hibernate.evictCollection(roleName);
	}

	public final static void evictEntity(String entityName, Serializable id) {
		hibernate.evictEntity(entityName, id);
	}

	public final static void evictEntity(String entityName) {
		hibernate.evictEntity(entityName);
	}

	public final static void evictQueries() {
		hibernate.evictQueries();
	}

	public final static void evictQueries(String cacheRegion) {
		hibernate.evictQueries(cacheRegion);
	}
    
}
