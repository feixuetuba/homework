/*
 *  DLOG_VelocityServlet.java
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
package com.liusoft.dlog4j.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.view.servlet.VelocityLayoutServlet;

import com.liusoft.dlog4j.DLOGTemplateManager;
import com.liusoft.dlog4j.Globals;
import com.liusoft.dlog4j.SiteNotFoundException;
import com.liusoft.dlog4j.TextCacheManager;
import com.liusoft.dlog4j.dao.SiteDAO;
import com.liusoft.dlog4j.util.RequestUtils;

/**
 * ��VelocityLayoutServlet������չ����Ҫ���б�����Զ�����
 * @author liudong
 */
public class DLOG_VelocityServlet extends VelocityLayoutServlet {
	
	private String encoding;

	public void init() throws ServletException {
		super.init();
		encoding = getInitParameter("encoding");
		if(encoding==null)
			encoding = Globals.ENC_UTF_8;
		//��ʼ�����ı����������
		initTextCacheManager();
	}
	
	private void initTextCacheManager(){
		String dir = getInitParameter("text-cache-dir");
		String cache_dir;
		if(dir==null){
			cache_dir = System.getProperty("java.io.tmpdir");			
		}
		else{
			if(dir.startsWith(Globals.LOCAL_PATH_PREFIX))
				cache_dir = dir.substring(Globals.LOCAL_PATH_PREFIX.length());		
			else if(dir.startsWith("/"))
				cache_dir = getServletContext().getRealPath(dir);
			else
				cache_dir = dir;
		}
		if(!cache_dir.endsWith(File.separator))
			cache_dir += File.separator;
		if(dir==null){
			cache_dir += "dlog";
			cache_dir += File.separator;
			cache_dir += "text";
		}
		TextCacheManager.init(cache_dir);
	}

	protected void doRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		HttpServletRequest request;
		if (RequestUtils.isMultipart(req)) {
			//�ļ����ı��봦��
			request = req;
			request.setCharacterEncoding(encoding);
		} else {
			//�Զ����봦��
			String enc = req.getCharacterEncoding();
			if (req instanceof RequestProxy)
				request = req;
			else if (encoding.equalsIgnoreCase(enc))
				request = req;
			else
				request = new RequestProxy(req, encoding);
		}
		//long begin_time = System.currentTimeMillis();
		int site_id = RequestUtils.getParam(request, "sid", -1);
		if(site_id > 0)
			DLOGTemplateManager.saveSite(SiteDAO.getSiteByID(site_id));
		try{
			super.doRequest(request, res);
		}finally{
			DLOGTemplateManager.clearSite();
			//System.out.println("URL: " + request.getRequestURL()+",TIME:"+(System.currentTimeMillis()-begin_time));
		}
	}

	/**
	 * �Զ���Velocity�Ĵ�����취
	 * @param req
	 * @param res
	 * @param excp	������쳣��Ϣ
	 */
	protected void error(HttpServletRequest req, 
						 HttpServletResponse res,
						 Exception excp) throws ServletException
	{
		Throwable t = excp;
		if(excp instanceof MethodInvocationException)
			t = ((MethodInvocationException)excp).getWrappedThrowable();
		
		try{
			if(t instanceof ResourceNotFoundException){
				super.log("Velocity Template not found: " + req.getRequestURL().toString());
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			if(t instanceof IllegalAccessException){
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			if(!(t instanceof SiteNotFoundException)){
				StringBuffer log = new StringBuffer();
				log.append(req.getRemoteAddr());
				log.append(" => ERROR��Unknown Velocity Error��url=");
				log.append(req.getRequestURL());
				if(req.getQueryString()!=null){
					log.append('?');
					log.append(req.getQueryString());						
				}
				log.append('(');
				log.append(new Date());
				log.append(')');
				super.log(log.toString(), t);
				log = null;
			}
			req.setAttribute(PageContext.EXCEPTION, t);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);		
		}catch(IOException e){
			System.err.println("Exception occured in VelocityServlet.error");
			e.printStackTrace(System.err);
		}
		return;
	}
	
}
