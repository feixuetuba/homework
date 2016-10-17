/*
 *  DAO.java
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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.liusoft.dlog4j.db.HibernateUtils;

/**
 * 所有数据库访问接口的基类
 * 
 * 人之患，在于好为人师
 * 
 * @author liudong
 */
public abstract class DAO extends _DAOBase{

	public final static int MAX_TAG_COUNT = 5;//限制每篇文章的标签最多五个
	public final static int MAX_TAG_LENGTH = 20;//标签最大长度,字节

	/**
	 * 获取数据库的元信息 
	 * @return
	 */
	public static DatabaseMetaData metadata(){
		try{
			return getConnection().getMetaData();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 添加对象
	 * @param cbean
	 */
	public static void save(Object cbean){
		try{
			Session ssn = getSession();
			beginTransaction();
			ssn.save(cbean);
			commit();
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}

	/**
	 * 添加对象
	 * @param cbean
	 */
	public static void saveOrUpdate(Object cbean){
		try{
			Session ssn = getSession();
			beginTransaction();
			ssn.saveOrUpdate(cbean);
			commit();
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}

	/**
	 * 删除对象
	 * @param cbean
	 */
	public static void delete(Object cbean){
		try{
			Session ssn = getSession();
			beginTransaction();
			ssn.delete(cbean);
			commit();
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}
	
	/**
	 * 根据主键删除某个对象
	 * @param objClass
	 * @param key
	 * @return
	 */
	protected static int delete(Class objClass, Serializable key){
		StringBuffer hql = new StringBuffer("DELETE FROM ");
		hql.append(objClass.getName());
		hql.append(" AS t WHERE t.id=?");
		return commitUpdate(hql.toString(), key);
	}

	/**
	 * 写脏数据到数据库
	 */
	public static void flush(){
		try{
			Session ssn = getSession();
			if(ssn.isDirty()){
				beginTransaction();
				ssn.flush();
				commit();
			}
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}

	/**
	 * 写脏数据到数据库
	 */
	protected final static void update(Object obj){
		try{
			beginTransaction();
			getSession().update(obj);
			commit();
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}

	/**
	 * 执行更新语句
	 * @param hql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	protected static int commitSQLUpdate(String sql, Object...args) throws SQLException{
		try{
			beginTransaction();
			int er = executeSQLUpdate(sql, args);
			commit();
			return er;
		}catch(SQLException e){
			rollback();
			throw e;
		}
	}	

	/**
	 * 执行更新语句
	 * @param hql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	protected static int executeSQLUpdate(String sql, Object...args) throws SQLException{
		Connection conn = getSession().connection();
		PreparedStatement ps = conn.prepareStatement(sql);
		try{
			for(int i=0;i<args.length;i++){				
				ps.setObject(i+1, args[i]);
			}
			return ps.executeUpdate();
		}finally{
			close(ps);
		}
	}

	/**
	 * 根据主键加载对象
	 * @param beanClass
	 * @param ident
	 * @return
	 */
	protected static Object getBean(Class beanClass, int id){
		return getSession().get(beanClass, id);
	}

	/**
	 * 执行统计查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static Number executeStat(String hql, Object...args){
		return (Number)uniqueResult(hql, args);
	}

	/**
	 * 执行统计查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static Number executeStatCacheable(String cache_region, String hql, Object...args){
		return (Number)uniqueResultCacheable(cache_region, hql, args);
	}
	
	/**
	 * 执行统计查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static int executeStatAsInt(String hql, Object...args){
		return (executeStat(hql, args)).intValue();
	}

	/**
	 * 执行统计查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static long executeStatAsLong(String hql, Object...args){
		return (executeStat(hql, args)).longValue();
	}

	/**
	 * 执行普通查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static List findAll(String hql, Object...args){
		return executeQuery(hql, -1, -1, args);
	}

	/**
	 * 执行普通查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static List findAllCacheable(String cache_region, String hql, Object...args){
		return executeQueryCacheable(cache_region, hql, -1, -1, args);
	}
	
	/**
	 * 执行普通查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static List executeQuery(String hql, int fromIdx, int fetchCount, Object...args){
		Session ssn = getSession();
		Query q = ssn.createQuery(hql).setReadOnly(true);		
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		if(fromIdx > 0)
			q.setFirstResult(fromIdx);
		if(fetchCount > 0)
			q.setMaxResults(fetchCount);
		return q.list();
	}

	/**
	 * 执行普通查询语句(支持缓存)
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static List executeQueryCacheable(String cache_region, String hql, int fromIdx, int fetchCount, Object...args){
		Session ssn = getSession();
		Query q = ssn.createQuery(hql).setReadOnly(true).setCacheable(true);
		if(cache_region!=null)
			q.setCacheRegion(cache_region);
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		if(fromIdx > 0)
			q.setFirstResult(fromIdx);
		if(fetchCount > 0)
			q.setMaxResults(fetchCount);
		return q.list();
	}

	/**
	 * 执行更新语句
	 * @param hql
	 * @param args
	 * @return
	 */
	public static int executeUpdate(String hql, Object...args){
		Session ssn = getSession();
		Query q = ssn.createQuery(hql);
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		return q.executeUpdate();
	}

	/**
	 * 执行更新语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static int commitUpdate(String hql, Object...args){
		try{
			beginTransaction();
			int er = executeUpdate(hql, args);
			commit();
			return er;
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}

	/**
	 * 执行返回单一结果的查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static Object uniqueResult(String hql, Object...args){
		Session ssn = getSession();
		Query q = ssn.createQuery(hql).setReadOnly(true);		
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		q.setMaxResults(1);
		return q.uniqueResult();
	}

	/**
	 * 执行返回单一结果的查询语句
	 * <b>调用者必须确保返回的结果只有一个！！！</b>
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static Object uniqueResultCacheable(String cache_region, String hql, Object...args){
		Session ssn = getSession();
		Query q = ssn.createQuery(hql).setReadOnly(true).setCacheable(true);
		if(cache_region!=null)
			q.setCacheRegion(cache_region);
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		//q.setMaxResults(1);
		return q.uniqueResult();
	}

	/**
	 * 执行统计查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static Number executeNamedStat(String hql, Object...args){
		return (Number)namedUniqueResult(hql, args);
	}

	protected static Number executeNamedStatCacheable(String cache_region, String hql, Object...args){
		return (Number)namedUniqueResultCacheable(cache_region, hql, args);
	}

	/**
	 * 执行统计查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static int executeNamedStatAsInt(String hql, Object...args){
		return (executeNamedStat(hql, args)).intValue();
	}

	protected static int executeNamedStatAsIntCacheable(String cache_region, String hql, Object...args){
		return ((Number)executeNamedStatCacheable(cache_region, hql, args)).intValue();
	}

	/**
	 * 执行统计查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static long executeNamedStatAsLong(String hql, Object...args){
		return (executeNamedStat(hql, args)).longValue();
	}

	protected static long executeNamedStatAsLongCacheable(String cache_region, String hql, Object...args){
		return ((Number)executeNamedStatCacheable(cache_region, hql, args)).longValue();
	}

	/**
	 * 执行普通查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static List executeNamedQuery(String hql, int fromIdx, int fetchCount, Object...args){
		Session ssn = getSession();
		Query q = ssn.getNamedQuery(hql).setReadOnly(true);		
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		if(fromIdx > 0)
			q.setFirstResult(fromIdx);
		if(fetchCount > 0)
			q.setMaxResults(fetchCount);
		return q.list();
	}

	/**
	 * 执行已命名的查询语句(支持缓存)
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static List executeNamedQueryCacheable(String cache_region, String hql, int fromIdx, int fetchCount, Object...args){
		Session ssn = getSession();
		Query q = ssn.getNamedQuery(hql).setReadOnly(true).setCacheable(true);
		if(cache_region!=null)
			q.setCacheRegion(cache_region);
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		if(fromIdx > 0)
			q.setFirstResult(fromIdx);
		if(fetchCount > 0)
			q.setMaxResults(fetchCount);
		return q.list();
	}

	/**
	 * 执行普通查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static List findNamedAll(String hql, Object...args){
		return executeNamedQuery(hql, -1, -1, args);
	}

	protected static List findNamedAllCacheable(String cache_region, String hql, Object...args){
		return executeNamedQueryCacheable(cache_region, hql, -1, -1, args);
	}
	
	/**
	 * 执行返回单一结果的查询语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static Object namedUniqueResult(String hql, Object...args){
		Session ssn = getSession();
		Query q = ssn.getNamedQuery(hql).setReadOnly(true);		
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		q.setMaxResults(1);
		return q.uniqueResult();
	}

	/**
	 * 执行返回单一结果的查询语句
	 * <b>调用者必须确保返回的结果只有一个！！！</b>
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static Object namedUniqueResultCacheable(String cache_region, String hql, Object...args){
		Session ssn = getSession();
		Query q = ssn.getNamedQuery(hql).setReadOnly(true).setCacheable(true);
		if(cache_region!=null)
			q.setCacheRegion(cache_region);
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		//q.setMaxResults(1);
		return q.uniqueResult();
	}

	/**
	 * 执行更新语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static int executeNamedUpdate(String hql, Object...args){
		Session ssn = getSession();
		Query q = ssn.getNamedQuery(hql);
		for(int i=0;i<args.length;i++){
			q.setParameter(i, args[i]);
		}
		return q.executeUpdate();
	}

	/**
	 * 执行更新语句
	 * @param hql
	 * @param args
	 * @return
	 */
	protected static int commitNamedUpdate(String hql, Object...args){
		try{
			beginTransaction();
			int er = executeNamedUpdate(hql, args);
			commit();
			return er;
		}catch(HibernateException e){
			rollback();
			throw e;
		}
	}
	
}

/**
 * 用于操作Hibernate的一些方法
 * @author Winter Lau
 */
abstract class _DAOBase {

	/**
	 * Get a instance of hibernate's session
	 * @return
	 * @throws HibernateException
	 */
	protected static Session getSession(){
		return HibernateUtils.getSession();
	}

	protected static Connection getConnection(){
		return HibernateUtils.getConnection();
	}

	/**
	 * Start a new database transaction.
	 */
	protected static void beginTransaction(){
		HibernateUtils.beginTransaction();
	}

	/**
	 * Commit the database transaction.
	 */
	protected static void commit(){
		HibernateUtils.commit();
	}

	/**
	 * Rollback the database transaction.
	 */
	protected static void rollback(){
		HibernateUtils.rollback();
	}

	/**
	 * Get a instance of cache provider
	 * @return
	 */
	protected static Object getObjectCached(String cache_name, Object key){
		return HibernateUtils.getObjectCached(cache_name, key);
	}
	
	protected static void setObjectCached(String cache_name, Object key, Object value){
		HibernateUtils.setObjectCached(cache_name, key, value);
	}
	
	protected static void evictObjectCached(String cache_name, Object key){
		HibernateUtils.evictObjectCached(cache_name, key);
	}
	
	protected static void evict(Class persistentClass, Serializable id) {
		HibernateUtils.evict(persistentClass, id);
	}

	protected static void evict(Class persistentClass) {
		HibernateUtils.evict(persistentClass);
	}

	protected static void evictCollection(String roleName, Serializable id) {
		HibernateUtils.evictCollection(roleName, id);
	}

	protected static void evictCollection(String roleName) {
		HibernateUtils.evictCollection(roleName);
	}

	protected static void evictEntity(String entityName, Serializable id) {
		HibernateUtils.evictEntity(entityName, id);
	}

	protected static void evictEntity(String entityName) {
		HibernateUtils.evictEntity(entityName);
	}

	protected static void evictQueries() {
		HibernateUtils.evictQueries();
	}

	protected static void evictQueries(String cacheRegion) {
		HibernateUtils.evictQueries(cacheRegion);
	}
	
	/**
	 * 释放资源
	 * @param objects
	 */
	protected static void close(Object...objects){
		try{
		for(Object o : objects){
			if(o==null) continue;
			if(o instanceof ResultSet){
				((ResultSet)o).close();
			}
			else if(o instanceof Statement){
				((Statement)o).close();
			}
			else if(o instanceof InputStream){
				((InputStream)o).close();
			}
			else if(o instanceof OutputStream){
				((OutputStream)o).close();
			}
			else if(o instanceof Reader){
				((Reader)o).close();
			}
			else if(o instanceof Writer){
				((Writer)o).close();
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}