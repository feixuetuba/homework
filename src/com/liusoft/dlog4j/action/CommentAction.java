/*
 *  CommentAction.java
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
 *  2006-7-26
 */
package com.liusoft.dlog4j.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liusoft.dlog4j.base.AuthorInfo;
import com.liusoft.dlog4j.base.ClientInfo;
import com.liusoft.dlog4j.beans.CommentBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.dao.CommentDAO;
import com.liusoft.dlog4j.formbean.CommentForm;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * 评论的Action
 * @author liudong
 */
public class CommentAction extends ActionBase {

	/**
	 * 发表评论
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward doAddReply(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommentForm cform = (CommentForm)form;
		super.validateClientId(request, cform);
		if(StringUtils.isNotBlank(cform.getContent())){
			CommentBean cbean = new CommentBean();
			cbean.setContent(super.filterScriptAndStyle(super.autoFiltrate(null, cform.getContent())));
			AuthorInfo author = new AuthorInfo();
			if(StringUtils.isNotBlank(cform.getAuthor()))
				author.setName(cform.getAuthor());
			else
				author.setName("匿名");
			if(StringUtils.isNotBlank(cform.getAuthorEmail()))
				author.setEmail(cform.getAuthorEmail());
			if(StringUtils.isNotBlank(cform.getAuthorURL()))
				author.setUrl(cform.getAuthorURL());
			UserBean loginUser = super.getLoginUser(request, response);			
			author.setId((loginUser!=null)?loginUser.getId():-1);
			
			cbean.setAuthor(author);
			cbean.setOwnerIdent(cform.getOwnerId());
			cbean.setOwnerType(cform.getOwnerType());
			cbean.setEid(cform.getEntityId());
			cbean.setEtype(cform.getEntityType());
			cbean.setCreateTime(new Date());
			cbean.setClient(new ClientInfo(request, cform.getClientType()));

			if(StringUtils.isNotEmpty(cform.getTitle()))
				cbean.setTitle(super.autoFiltrate(null, cform.getTitle()));
			else
				cbean.setTitle(StringUtils.abbreviate(StringUtils.extractText(cbean.getContent()), 20));
			CommentDAO.save(cbean);
			//callback
			this.callback(cform.getCallbackURL(), cform.getEntityId(), cform.getEntityType(), "add");
		}
		
		return new ActionForward(cform.getFromPage(), true);
	}
	
	/**
	 * 删除评论
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward doDeleteReply(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommentForm cform = (CommentForm)form;
		CommentDAO.deleteCommentByID(cform.getId());
		this.callback(cform.getCallbackURL(), cform.getEntityId(), cform.getEntityType(), "del");
		return new ActionForward(cform.getFromPage(), true);
	}
	
	/**
	 * 更新评论
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward doUpdateReply(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
	

	/**
	 * 回调评论通知接口
	 * @param url
	 * @param entityId
	 * @param action
	 */
	protected void callback(String url, int eid, int etype, String action){
		PostMethod post = new PostMethod(url);
		post.addParameter("eid", String.valueOf(eid));
		post.addParameter("etype", String.valueOf(etype));
		post.addParameter("act", action);
		try{
			int er = client.executeMethod(post);
			if(er!=HttpServletResponse.SC_OK){
				throw new Exception("Invoke URL:"+url+" failed, CODE: " + er);
			}
			//System.out.println(post.getResponseBodyAsString());
		}catch(Exception e){
			getServlet().log("Failed when invoke url " + url, e);
		}finally{
			post.releaseConnection();
		}
	}

	final static HttpClient client = new HttpClient();
}
