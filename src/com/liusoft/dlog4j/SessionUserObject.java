/*
 *  SessionUserObject.java
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
package com.liusoft.dlog4j;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liusoft.dlog4j.base._UserBeanBase;
import com.liusoft.dlog4j.beans.UserBean;
//import com.liusoft.dlog4j.dao.FCKUploadFileDAO;
import com.liusoft.dlog4j.db.HibernateUtils;

/**
 * ��¼�ڻỰ�е��û���������
 * @see com.liusoft.dlog4j.beans.UserBean
 * @author Winter Lau
 */
public class SessionUserObject extends _UserBeanBase implements HttpSessionBindingListener{

	private final static Log log = LogFactory.getLog(SessionUserObject.class);
	
	/* session ��ص���Ϣ,��bean�޹� */
	private String sessionId;

	/**
	 * �ж��û��ǲ��ǳ�������Ա
	 * @param user
	 * @return
	 */
	public boolean isSuperior(){
		return UserLoginManager.isSuperior(this);
	}
	
	/**
	 * ��PO�����и���һ�����ݣ���¡
	 * @param bean
	 * @return
	 */
	public static SessionUserObject copyFrom(UserBean bean){
		SessionUserObject user = new SessionUserObject();
		user.setId(bean.getId());
		user.setName(bean.getName());
		user.setNickname(bean.getNickname());
		user.setSex(bean.getSex());
		user.setBirth(bean.getBirth());
		user.setContactInfo(bean.getContactInfo());
		user.setCount(bean.getCount());
		user.setResume(bean.getResume());
		user.setRegTime(bean.getRegTime());
		user.setLastTime(bean.getLastTime());
		user.setLastAddr(bean.getLastAddr());
		user.setStatus(bean.getStatus());
		user.setKeepDays(bean.getKeepDays());
		user.setOwnSiteId(bean.getOwnSiteId());
		user.setPortrait(bean.getPortrait());
		user.setRole(bean.getRole());
		user.setLevel(bean.getLevel());
		user.setScore(bean.getScore());
		user.setUniqueCode(bean.getUniqueCode());
		user.setPopedom(bean.getPopedom());
		return user;
	}
	
	/**
	 * ����session_id��ֹĳЩӦ�÷������ỰʵЧ���޷���ȡsession_id
	 */
	public void valueBound(HttpSessionBindingEvent e) {
		this.sessionId = e.getSession().getId();
	}

	/**
	 * ִ���û�ע������
	 * ���ڸ÷�������Ӧ�÷��������õģ�������HibernateFilter����˱����ֶ��ر�Session
	 */
	public void valueUnbound(HttpSessionBindingEvent e) {
		SessionUserObject user = (SessionUserObject)e.getValue();
		if(user != null){
			try{
				UserLoginManager.logoutUser(user);
			}catch(Exception excp){
				log.error("Error when logout user, userid="+user.getId(), excp);
			}
			/*
			try{
				//���dlog_fck_upload_file������ļ�
				FCKUploadFileDAO.cleanupOfSession(user.getSessionId(), user.getId());
			}catch(Exception excp){
				log.error("Error when cleanup upload files, userid="+user.getId(), excp);
			}*/
			//�˴����ڲ���HibernateFilter���ƣ������Ҫ��ʽ�����ر����ݿ���Դ
			HibernateUtils.closeSession();			
		}
	}
	
	public String getSessionId() {
		return sessionId;
	}

}
