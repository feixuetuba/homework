/*
 *  Hibernate.java
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
 */
package com.liusoft.util.db;

import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cfg.Configuration;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.stat.Statistics;

/**
 * Hibernate的操作接口
 * @author liudong
 */
public class Hibernate {

	private final static Log log = LogFactory.getLog(Hibernate.class);

	private URL hibernate_cfg;
	private SessionFactoryImpl sessionFactory;
	private ThreadLocal<Session> sessions;
	private ThreadLocal<Transaction> transactions;
	private CacheProvider cacheProvider;
	private Hashtable<String, Cache> caches;
	private Configuration cfg;

	/**
	 * Initialize the hibernate environment
	 * @param hibernate_cfg
	 * @throws MalformedURLException 
	 */
	public final static Hibernate init(URL hibernate_cfg){
		return new Hibernate(hibernate_cfg);
	}

	/**
	 * 根据输入的配置文件初始化Hibernate
	 * @param cfg_path
	 */
	private Hibernate(URL cfg_path){
		this.sessions = new ThreadLocal<Session>();
		this.transactions = new ThreadLocal<Transaction>();
		this.cfg = new Configuration().configure(cfg_path);			
		this.sessionFactory = (SessionFactoryImpl)cfg.buildSessionFactory();		
		this.hibernate_cfg = cfg_path;
		this.cacheProvider = sessionFactory.getSettings().getCacheProvider();
		this.caches = new Hashtable<String, Cache>();
	}
	
	/**
	 * 返回Hibernate的统计数据
	 * @return
	 */
	public Statistics getStatistics(){
		return sessionFactory.getStatistics();
	}

	/**
	 * 返回对应的Hibernate配置文件的路径
	 * @return
	 */
	public URL getHibernateConfig() {
		return hibernate_cfg;
	}
    
	/**
	 * Get a instance of hibernate's session
	 * @return
	 * @throws HibernateException
	 */
	public Session getSession(){
		if(sessions == null)
			return null;

		Session ssn = sessions.get();
		if (ssn == null || !ssn.isOpen()) {
			ssn = sessionFactory.openSession();
			sessions.set(ssn);
		}
		return ssn;
	}

	public Connection getConnection(){
		return getSession().connection();
	}
	
	/**
	 * Closes the Session local to the thread.
	 */
	public void closeSession(){
		if(sessions == null)
			return;
		
		Session ssni = sessions.get();
		if(ssni == null)
			return;
		
		if (ssni.isOpen()) {
			ssni.close();
		}
		sessions.set(null);
	}

	/**
	 * Start a new database transaction.
	 */
	public void beginTransaction(){
		if(transactions == null)
			return;
		// Would be written as a no-op in an EJB container with CMT
		Transaction tx = transactions.get();
		if (tx == null || tx.wasCommitted() || tx.wasRolledBack()) {
			Session ssn = sessions.get();
			if(ssn == null)
				ssn = getSession();
			tx = ssn.beginTransaction();			
			transactions.set(tx);
		}
		else{
			if(tx!=null && log.isWarnEnabled())
				log.warn("Trying to begin a exist transaction, nothing to do.", new Exception());
		}
	}

	/**
	 * Commit the database transaction.
	 */
	public void commit(){
		if(transactions == null)
			return;
		// Would be written as a no-op in an EJB container with CMT
		Transaction tx = transactions.get();
		if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
			tx.commit();
		}
		else{
			if(tx!=null && log.isWarnEnabled())
				log.warn("Trying to commit the uncommitable transaction, nothing to do.");
		}
		if(tx!=null)
			transactions.set(null);
	}

	/**
	 * Rollback the database transaction.
	 */
	public void rollback(){
		if(transactions == null)
			return;
		Transaction tx = (Transaction) transactions.get();
		transactions.set(null);
		if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
			tx.rollback();
		}
		else{
			if(tx!=null && log.isWarnEnabled())
				log.warn("Trying to rollback the unrollbackable transaction, nothing to do.");
		}
	}

	/**
	 * 从缓存中取对象
	 * @return
	 */
	public Object getObjectCached(String cache_name, Object key) {
		Cache cache = caches.get(cache_name);
		if(cache==null){
			synchronized(caches){
				cache = cacheProvider.buildCache(cache_name, cfg.getProperties());
				caches.put(cache_name, cache);
			}
		}
		return (cache!=null)?cache.get(key):null;
	}
	
	/**
	 * 将对象置于缓存中
	 * @param cache_name
	 * @param key
	 * @param value
	 */
	public void setObjectCached(String cache_name, Object key, Object value){
		Cache cache = caches.get(cache_name);
		if(cache==null){
			synchronized(caches){
				cache = cacheProvider.buildCache(cache_name, cfg.getProperties());
				caches.put(cache_name, cache);
			}
		}
		if(cache!=null) cache.put(key, value);
	}
	
	/**
	 * 删除缓存中的对象
	 * @param cache_name
	 * @param key
	 */
	public void evictObjectCached(String cache_name, Object key){
		Cache cache = caches.get(cache_name);
		if(cache!=null){
			cache.remove(key);
		}
	}

	public void evict(Class persistentClass, Serializable id){
		sessionFactory.evict(persistentClass, id);
	}

	public void evict(Class persistentClass){
		sessionFactory.evict(persistentClass);
	}

	public void evictCollection(String roleName, Serializable id){
		sessionFactory.evictCollection(roleName, id);
	}

	public void evictCollection(String roleName){
		sessionFactory.evictCollection(roleName);
	}

	public void evictEntity(String entityName, Serializable id){
		sessionFactory.evictEntity(entityName, id);
	}

	public void evictEntity(String entityName){
		sessionFactory.evictEntity(entityName);
	}

	public void evictQueries(){
		sessionFactory.evictQueries();
	}

	public void evictQueries(String cacheRegion){
		sessionFactory.evictQueries(cacheRegion);
	}

	/**
	 * 释放所有Hibernate占用的资源 
	 * @see com.liusoft.dlog4j.servlet.DLOG_ActionServlet#destroy()
	 */
	public void destroy(){
		if(sessionFactory!=null){
			sessionFactory.close();
			sessionFactory = null;
		}
		sessions = null;
		transactions = null;
		if(log.isWarnEnabled())
			log.warn("Hibernate("+hibernate_cfg+") was destroy successfully, all of sessions were closed.");
	}

}
