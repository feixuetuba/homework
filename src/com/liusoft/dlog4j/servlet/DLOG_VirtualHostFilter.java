/*
 *  DLOG_VirualHostFilter.java
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.liusoft.dlog4j.DLOG_CacheManager;
import com.liusoft.dlog4j.Globals;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.dao.SiteDAO;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * ����֧���Զ������������Ĺ�����
 * @author Winter Lau
 */
public class DLOG_VirtualHostFilter implements Filter {

	private List<String> domains;
	private List<String> enable_ips;
	private String main_url = "http://www.dlog.cn";
	private final static String cache_name = "DLOG4J_vhosts";
	private boolean disbleIPAccess = false;
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig cfg) throws ServletException {
		this.domains = new ArrayList<String>();
		this.enable_ips = new ArrayList<String>();
		String vhost = cfg.getInitParameter("host-name");
		if(StringUtils.isNotEmpty(vhost)){
			StringTokenizer st = new StringTokenizer(vhost.toLowerCase());
			while(st.hasMoreElements()){
				domains.add(st.nextToken().trim());
			}
		}
		String ips = cfg.getInitParameter("enable-ips");
		if(StringUtils.isNotEmpty(ips)){
			StringTokenizer st = new StringTokenizer(ips);
			while(st.hasMoreElements()){
				String ip = st.nextToken().trim();
				enable_ips.add(ip);
			}
		}
		String mu = cfg.getInitParameter("main-url");
		if(StringUtils.isNotEmpty(mu))
			main_url = mu;
		disbleIPAccess = "true".equals(cfg.getInitParameter("disable-ip-access"));
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException 
	{
		String vhost = req.getServerName().toLowerCase();
		int loop_idx = 0;
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		while(loop_idx<10){			
			//����ʹ���ڲ�IP��ֱַ�ӷ���
			if(isInnerIpAddr(vhost) || enable_ips.contains(req.getRemoteAddr())){
				chain.doFilter(req, res);
				return;
			}
			
			boolean isIpAddr = StringUtils.isIPAddr(vhost);			
			//������ֱ����IP��ַ����
			if(disbleIPAccess && isIpAddr)
				break;
			
			//ʹ��ϵͳĬ��������������ֱ����IP��ַ���ʵĲ������κδ���
			if(isIpAddr || isHostOfDomain(vhost)){
				chain.doFilter(req, res);
				return;
			}

			//�������,1:�������; 2:���治����
			//�������: 1: ֵС��0�򲻶���ɫ; 2: ֵ����0��forward���µ�����
			//���治����: 1: �������ݿ��¼; 2: ���������ݿ��¼  (���»���)
			try {
				int sid = -1;
				Integer nSid = (Integer)DLOG_CacheManager.getObjectCached(cache_name, vhost);
				if(nSid != null){
					sid = nSid.intValue();
					//������ڵ���ֵС����
					if(sid < 0)
						break;
				}
				else{
					//��ѯ���ݿ�
					SiteBean site = SiteDAO.getSiteByVhost(vhost);
					nSid = (site!=null)?site.getId():-1;
					DLOG_CacheManager.putObjectCached(cache_name, vhost, nSid);
					if(site == null)
						break;
					sid = site.getId();
				}
				final String siteId = String.valueOf(sid);				
				String param_sid = req.getParameter(Globals.PARAM_SID);
				if(StringUtils.isNumeric(param_sid) && !StringUtils.equals(siteId, param_sid)) 
					break;
				
				//�����µ��������sid����			
				HttpServletRequest request1 = new HttpServletRequestWrapper((HttpServletRequest)req){
					public String getParameter(String key) {
						if(!Globals.PARAM_SID.equals(key))
							return super.getParameter(key);
						return siteId;
					}

					public Map getParameterMap() {
						Map params = super.getParameterMap();
						params.put(Globals.PARAM_SID, siteId);
						return params;
					}

					public Enumeration getParameterNames() {
						Vector names = new Vector();
						Enumeration params = super.getParameterNames();
						while(params.hasMoreElements()){
							names.add(params.nextElement());
						}
						if(!names.contains(Globals.PARAM_SID))
							names.add(Globals.PARAM_SID);
						return names.elements();
					}
				};
				request1.setAttribute(Globals.PARAM_SID, siteId);
				chain.doFilter(request1, res);
				return;
			} catch (IllegalStateException e) {
				//ɾ��ʧЧ�Ļ��沢����ִ���������
				DLOG_CacheManager.evictObjectCached(cache_name, vhost);
				loop_idx ++;
				continue;
			}
		}

		StringBuffer url = new StringBuffer(main_url);
		url.append(request.getRequestURI());
		String queryStr = request.getQueryString();
		if(StringUtils.isNotEmpty(queryStr)){
			url.append('?');
			url.append(queryStr);
		}
		response.sendRedirect(url.toString());
		
	}
	
	/**
	 * �ж��Ƿ��ڲ�IP��ַ
	 * @param ip
	 * @return
	 */
	private static boolean isInnerIpAddr(String ip){
		return ip.startsWith("192.168.") || ip.startsWith("10.")
				|| ip.equals("127.0.0.1") || ip.equalsIgnoreCase("localhost")
				|| (ip.indexOf('.') == -1);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * �ж�һ��������ַ�Ƿ���ĳ��������һ����
	 * @param domain
	 * @param host
	 * @return
	 */
	protected boolean isHostOfDomain(String host){
		for(int i=0;i<domains.size();i++){
			String domain = (String)domains.get(i);
			if(host.endsWith(domain)){
				if(domain.length()>=host.length())
					return true;
				else{
					int idx = host.length()-domain.length()-1;
					char dot = host.charAt(idx);
					if(dot == '.')
						return true;
				}
			}
		}
		return false;
	}
	
}
