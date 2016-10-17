/*
 *  CommentDAO.java
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
 *  2006-7-22
 */
package com.liusoft.dlog4j.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.liusoft.dlog4j.beans.CommentBean;
import com.liusoft.dlog4j.beans.CommentOutlineBean;

/**
 * 评论数据库操作接口
 * @author liudong
 */
public class CommentDAO extends DAO {

	/**
	 * 删除某个评论
	 * @param cmt_id
	 * @return
	 */
	public static int deleteCommentByID(int cmt_id){
		return delete(CommentBean.class, cmt_id);
	}
	
	public static int getCommentCount(int otype, int oid){
		return executeNamedStatAsInt("COMMENT_TOTAL_COUNT", oid, otype);
	}
	
	/**
	 * 列出某个对象的评论
	 * @param eid
	 * @param etype
	 * @param fromIdx
	 * @param fetchSize
	 * @param reverse
	 * @param withContent
	 * @return
	 */
	public static List listComments(int otype, int oid, int eid, int etype, int fromIdx, int fetchSize, boolean reverse, boolean withContent) {
		Session ssn = getSession();
		Criteria crit = ssn.createCriteria(withContent?CommentBean.class:CommentOutlineBean.class);
		crit.add(Expression.eq("ownerType", otype));
		crit.add(Expression.eq("ownerIdent", oid));
		if(eid > 0)
			crit.add(Expression.eq("eid", eid));
		if(etype >= 0)
			crit.add(Expression.eq("etype", etype));
		crit.addOrder(reverse?Order.desc("id"):Order.asc("id"));
		if(fromIdx > 0)
			crit.setFirstResult(fromIdx);
		if(fetchSize > 0)
			crit.setMaxResults(fetchSize);
		return crit.list();
	}
	
}
