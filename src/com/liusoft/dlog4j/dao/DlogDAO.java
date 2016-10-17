/*
 *  DlogDAO.java
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

import java.util.List;

import com.liusoft.dlog4j.base.DlogStatInfo;
import com.liusoft.dlog4j.db.HibernateUtils;

/**
 * DLOG4Jƽ̨���������ݿ���ʷ���
 * @author Winter Lau
 */
public class DlogDAO extends DAO {

	/**
	 * �г�����ϵͳ���µ��ռ�
	 * @param fromIdx
	 * @param count
	 * @return
	 */
	public static List listNewArticles(int fromIdx, int count){
		return DiaryDAO.listNewArticles(fromIdx, count);
	}
	/**
	 * ��ȡ����ר����־��Site��days���ڵ������ŵ�����,��������������
	 * @param days
	 * @param count
	 * @return
	 */
	public static List listHotArticles(int days, int count){
		return DiaryDAO.listHotArticles(days, count);
	}

	/**
	 * ��ȡ����ר����־��Site��days���ڵ������ŵ���Ƭ
	 * @param days
	 * @param count
	 * @return
	 */
	public static List listHotPhotos(int days, int count){
		return PhotoDAO.listHotPhotos(days, count);
	}
	
	/**
	 * �г�������������
	 * @param days
	 * @param count
	 * @return
	 */
	public static List listHotTopics(int days, int count){
		return BBSTopicDAO.listHotTopics(0, count, days);
	}
	
	/**
	 * ����ָ����վ��ͳ����Ϣ,���siteֵС��0��ȡ������վ
	 * @param site
	 * @return
	 */
	public static DlogStatInfo getDlogStatInfo(int site){
		DlogStatInfo count = new DlogStatInfo();
		//============== �ռ���
		count.setArticle(DiaryDAO.getDiaryCount(site));
		//============== �ռ�������
		count.setArticleReply(DiaryDAO.getDiaryReplyCount(site));
		//============== ��Ƭ��
		count.setPhoto(PhotoDAO.getPhotoCount(site));
		//============== ��Ƭ������
		count.setPhotoReply(PhotoDAO.getPhotoReplyCount(site));
		//============== ��̳������
		count.setTopic(BBSTopicDAO.getTopicCount(site));
		//============== ��̳������
		count.setTopicReply(BBSReplyDAO.getReplyCount(site));
		//============== ע���û���
		count.setUser(UserDAO.getUserCount(site));
		//============== ע����վ��
		count.setSite(SiteDAO.getSiteCount());
		return count;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HibernateUtils.init();
		try{
			long ct = System.currentTimeMillis();
			listHotArticles(10,10);
			System.out.println("Time used : " + (System.currentTimeMillis() - ct) + " ms.");
		}finally{
			HibernateUtils.destroy();
		}
	}

}
