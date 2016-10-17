/*
 *  DLOG_ActionServlet.java
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

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;

import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.struts.action.ActionServlet;

import com.liusoft.dlog4j.DLOGSecurityManager;
import com.liusoft.dlog4j.DLOGUserManager;
import com.liusoft.dlog4j.DLOG_LayoutManager;
import com.liusoft.dlog4j.DLOG_VisitorManager;
import com.liusoft.dlog4j.Globals;
import com.liusoft.dlog4j.util.RequestUtils;

/**
 * 对Struts进行扩展，实现Hibernate的初始化以及参数编码的自动处理
 * @author liudong
 */
public class DLOG_ActionServlet extends ActionServlet {

	private String encoding;

	static {
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
	}

	/**
	 * Globals.WEBAPP_PATH变量值对使用access数据库来说非常重要，涉及一个相对路径的问题
	 */
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		if (Globals.WEBAPP_PATH == null)
			Globals.WEBAPP_PATH = context.getRealPath("");
		//初始化系统安全控制
		try {
			DLOGSecurityManager.init(context);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		//初始化用户资料管理接口
		try {
			DLOGUserManager.init(context);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		//初始化布局管理器接口
		DLOG_LayoutManager.init(this);
		DLOG_VisitorManager.init(this);
		//执行Struts的初始化过程
		super.init();
		
		encoding = getInitParameter("encoding");
		if(encoding==null)
			encoding = Globals.ENC_UTF_8;
	}

	/**
	 * 实现对编码的自动转码处理
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @see org.apache.struts.action.ActionServlet#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		HttpServletRequest request;
		if (RequestUtils.isMultipart(req)) {
			//文件表单的编码处理
			request = req;
			request.setCharacterEncoding(encoding);
		} else {
			//自动编码处理
			String enc = req.getCharacterEncoding();
			if (req instanceof RequestProxy)
				request = req;
			else if (encoding.equalsIgnoreCase(enc))
				request = req;
			else
				request = new RequestProxy(req, encoding);
		}
		super.process(request, new DLOG_ServletResponse(res));
	}

	public void destroy() {
		DLOGSecurityManager.destroy();
		DLOGUserManager.destroy();
		//释放Struts
		super.destroy();
	}

	/**
	 * 用于屏蔽几个方法
	 * @author liudong
	 */
	private static class DLOG_ServletResponse extends HttpServletResponseWrapper {
		public DLOG_ServletResponse(HttpServletResponse res){
			super(res);
		}

		public String encodeRedirectUrl(String arg0) {
			return arg0;
		}

		public String encodeRedirectURL(String arg0) {
			return arg0;
		}

		public String encodeUrl(String arg0) {
			return arg0;
		}

		public String encodeURL(String arg0) {
			return arg0;
		}
		
	}
}
