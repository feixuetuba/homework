/*
 *  DataExportServlet.java
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
package com.liusoft.dlog4j.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liusoft.dlog4j.SessionUserObject;
import com.liusoft.dlog4j.UserLoginManager;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.dao.SiteDAO;
import com.liusoft.dlog4j.util.DateUtils;
import com.liusoft.dlog4j.util.RequestUtils;

/**
 * ���ڽ���վ���ݵ����ɴ�HTML�ı������ѹ���ļ�
 * �����û���������
 * ʹ��ʾ���� http://localhost/servlet/export?sid=123
 * �������վ�����ܵ������ݣ�����Ҫ���Ƶ����Ĵ���
 * @author Winter Lau
 */
public class DLOG_DataExportServlet extends HttpServlet {

	/**
	 * TODO: ��������
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		//�ж��û��Ƿ��ѵ�¼
		SessionUserObject loginUser = UserLoginManager.getLoginUser(req, res, true);
		if(loginUser==null){
			res.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		int site_id = RequestUtils.getParam(req, "sid", -1);
		SiteBean site = SiteDAO.getSiteByID(site_id);
		if(site==null||site.getStatus()<0){
			res.sendError(HttpServletResponse.SC_NOT_FOUND, "site #"+site_id+" not found.");
			return;
		}
		//�ж��ϴα���ʱ���Ƿ��Ѿ�����һ��
		if(site.getLastExportTime()!=null && DateUtils.diff_in_date(new Date(), site.getLastExportTime())<7){
			alert(req, res, "Data export must be after 1 week since last export.");
			return;
		}
		//��������ѹ������web����ʱĿ¼�����������ض�������ļ�
		//���ļ���һ��ʱ�����Զ�ɾ��
		
	}
	
	/**
	 * �����������ʾ��Ϣ
	 * 
	 * @param req
	 * @param res
	 * @param msg
	 * @throws IOException
	 */
	protected void alert(HttpServletRequest req, HttpServletResponse res,
			String msg) throws IOException {
		String html = MessageFormat.format(msg_tmp, msg);
		res.getWriter().write(html);
	}

	private final static String msg_tmp = "<script language='javascript'>alert('{0}');</script>";
}
