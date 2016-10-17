package com.liusoft.dlog4j.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.digester.rss.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liusoft.dlog4j.base.ClientInfo;
import com.liusoft.dlog4j.beans.CatalogBean;
import com.liusoft.dlog4j.beans.DiaryBean;
import com.liusoft.dlog4j.beans.MusicBean;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.dao.AdminDAO;
import com.liusoft.dlog4j.dao.CatalogDAO;
import com.liusoft.dlog4j.dao.DiaryDAO;
import com.liusoft.dlog4j.dao.MusicDAO;
import com.liusoft.dlog4j.dao.SiteDAO;
import com.liusoft.dlog4j.formbean.AdminForm;
import com.liusoft.dlog4j.formbean.DlogMoveForm;
import com.liusoft.dlog4j.util.DateUtils;
import com.liusoft.dlog4j.velocity.DLOG_VelocityTool;

public class DlogMoveAction extends ActionBase {
	private final static Log log = LogFactory.getLog(DlogMoveAction.class);

	public ActionForward doPreview(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DlogMoveForm moveForm = (DlogMoveForm) form;
		String url = moveForm.getUrl();
		DLOG_VelocityTool tool = new DLOG_VelocityTool();
		Channel channel = tool.fetch_channel(1, url);
		request.setAttribute("channel", channel);
		return new ActionForward("/html/sitemgr/move_preview.vm",false);
	}

	/**
	 * ���·�� 
	 * */
	public ActionForward doTestURL(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DlogMoveForm moveForm = (DlogMoveForm) form;
		String url = moveForm.getUrl();
		Channel channel = null;
		response.setContentType("text/plain;charset=UTF-8");
		DLOG_VelocityTool tool = new DLOG_VelocityTool();
		try {
			channel = tool.fetch_channel(1, url);
		} catch (Exception ex) {
			response.getWriter().print("��ַ������޷�����;-1");

		}
		if (channel != null)
			response.getWriter().print("��ַ����;1");
		else
			response.getWriter().print("��ַ������޷�����;-1");
		return null;
	}

	/***/
	public ActionForward doMoveDiary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DlogMoveForm moveForm = (DlogMoveForm) form;
		DLOG_VelocityTool tool = new DLOG_VelocityTool();
		UserBean loginUser = super.getLoginUser(request, response);
		Channel channel = tool.fetch_channel(1, moveForm.getUrl());
		List list=new ArrayList<DiaryBean>();;
		DiaryBean bean;
		for(int i=0;i<channel.getItems().length;i++)
		{
			bean=new DiaryBean();
			bean.setAuthor(moveForm.getAuthor());//����
			bean.setAuthorUrl(moveForm.getAuthorUrl());//����
			CatalogBean catalogBean=CatalogDAO.getCatalogByID(moveForm.getCatalogId());//����
			bean.setCatalog(catalogBean);
			bean.setWeather(moveForm.getWeather());//����
			bean.setContent(channel.getItems()[i].getDescription());//����
			bean.setTitle(channel.getItems()[i].getTitle());//����
			bean.setClient(new ClientInfo(request, moveForm.getClientType()));			
			bean.setMoodLevel(moveForm.getMoodLevel());//����
			bean.setRefUrl("/dlog/viewuser.vm?sid="+moveForm.getSid()+"&uid="+loginUser.getId()+"");//���õ�ַ
			bean.setReplyNotify(moveForm.getNotify());//��������
			bean.setStatus(DiaryBean.STATUS_NORMAL);//״̬
			bean.setWriteTime(new Date());	
			SiteBean siteBean=new SiteBean();
			siteBean.setId(moveForm.getSid());//����վ��
			bean.setSite(siteBean);
			bean.setOwner(loginUser);
			try{
				DiaryDAO.create(bean,true);
			}catch(Exception ex){
				response.getWriter().print("ת��ʧ�� д��ʱ�����쳣");
			}
			
		}
		
		response.setContentType("text/plain;charset=UTF-8");
		if(DiaryDAO.MoveDiary(list))
			response.getWriter().print("ת�Ƴɹ�");
		else
			response.getWriter().print("ת��ʧ��");
		return null;
	}

}
