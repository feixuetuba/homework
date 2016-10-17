/*
 *  WapBrowserServlet.java
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
 *  2006-11-15
 */
package com.liusoft.dlog4j.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Tag;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.liusoft.dlog4j.util.StringUtils;

/**
 * WAP模拟器小服务程序
 * 
 * @author liudong
 */
public class WapBrowserServlet extends HttpServlet {

	private final static PrototypicalNodeFactory factory;
	private static String html_template;
	
	static{
		factory = new PrototypicalNodeFactory ();
		factory.registerTag(new WmlGoTag ());
		factory.registerTag(new CardTag ());
		Parser.getConnectionManager().getRequestProperties().put("User-Agent", "DLOG4J WAP Simulator (http://www.dlog.cn/wap/simulator.html)");
	}	
	
	@Override
	public void init() throws ServletException {
		try {
			html_template = getTemplate();
		} catch (IOException e) {
			log("Exception occur when loading wap browser template.", e);
			html_template = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><meta name=\"Robots\" content=\"noindex,nofollow\"/></head><body>{0}</body></html>";
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String url = req.getQueryString();
		if(StringUtils.isBlank(url))
			return;
		if(!url.toLowerCase().startsWith("http://"))
			return;
		String servlet_path = req.getServletPath();
		res.setContentType("text/html;charset=utf-8");
		String html;
		try {
			html = parseWML(servlet_path, url);
			res.getWriter().print(html);
		} catch (ParserException e) {
			log("", e);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	/**
	 * 解析WML文档
	 * @param servlet_path
	 * @param wml_url
	 * @return
	 * @throws ParserException
	 */
	private static String parseWML(String servlet_path, String wml_url) throws ParserException{
		StringBuffer html = new StringBuffer();
		Parser parser = new Parser(wml_url);
		parser.setNodeFactory(factory);
		NodeList nodes = parser.parse(card_filter);
		if(nodes.size()>0){
			CardTag card = (CardTag)nodes.elementAt(0);
			NodeList childs = card.getChildren();
			for(int i=0;i<childs.size();i++){
				Node child = childs.elementAt(i);
				if(child instanceof Tag)
					dumpNode(servlet_path, wml_url, html, (Tag)child);
				else
					html.append(child.toHtml());
			}
		}
		return StringUtils.replace(html_template, "{0}", html.toString());
	}
	
	/**
	 * 遍历所有节点
	 * @param servlet_path	模拟器的路径
	 * @param wml_url	要浏览的页面url
	 * @param html	返回的文本
	 * @param tag	当前标签
	 */
	private static void dumpNode(String servlet_path, String wml_url, StringBuffer html, Tag tag) {
		if(tag instanceof LinkTag){
			LinkTag link = (LinkTag)tag;
			String slink = link.getLink();
			if(slink.endsWith(".jpg")||slink.endsWith(".gif")||slink.endsWith(".png")){
				if(slink.startsWith("/")){
					int idx = wml_url.indexOf("/", 7);
					String new_url = wml_url.substring(0, idx) + slink;
					link.setLink(new_url);
				}
				else if(!slink.startsWith("http://")){
					int idx = wml_url.lastIndexOf('/');
					String new_url = wml_url.substring(0, idx) + '/' + slink;
					link.setLink(new_url);
				}
			}
			else{
				String new_url = servlet_path + '?' + slink;
				link.setLink(new_url);
			}
		}
		else if(tag instanceof WmlGoTag){
			WmlGoTag link = (WmlGoTag)tag;
			String new_url = servlet_path + '?' + link.getLink();
			link.setLink(new_url);
		}
		else if(tag instanceof ImageTag){
			ImageTag img = (ImageTag)tag;
			String img_url = img.getImageURL();
			
			if(!img_url.startsWith("http://")&&!img_url.startsWith("https://")){	
				if(img_url.startsWith("/")){
					int idx = wml_url.indexOf("/", 7);
					img.setImageURL(wml_url.substring(0, idx) + img_url);
				}
				else{
					int idx = wml_url.lastIndexOf('/');
					img.setImageURL(wml_url.substring(0, idx) + '/' + img_url);
				}
			}
			else
				img.setImageURL(img_url);
		}
		
		html.append('<');
		html.append(tag.getText());
		html.append('>');
	
		//System.out.println(tag.getClass().getName() + ":" + tag.getText());
		NodeList childs = tag.getChildren();
		if(childs!=null && childs.size()>0){
			for(int i=0;i<childs.size();i++){
				Node child = childs.elementAt(i);
				if(child instanceof Tag)
					dumpNode(servlet_path, wml_url, html, (Tag)child);
				else{
					html.append(child.toHtml());
				}
			}
		}
		if(tag.getEndTag()!=null){
			html.append(tag.getEndTag().toHtml());
		}
	}

	/**
	 * 获得内容的模板
	 * @return
	 * @throws IOException
	 */
	protected static String getTemplate() throws IOException{
		InputStream in = WapBrowserServlet.class.getResourceAsStream("wap_browser_template.html");
		StringBuffer template = new StringBuffer(512);
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(in));
			do{
				String line = reader.readLine();
				if(line==null)
					break;
				template.append(line);
				template.append("\r\n");
			}while(true);
		}finally{
			in.close();
		}
		return template.toString();
	}
	
	public static void main(String[] args) throws Exception{
		//String html = parseWML("/servlet/wml2html", "http://wap.dlog.cn");
		//System.out.print("===============================");
		System.out.print(getTemplate());
	}
	
	private final static NodeFilter card_filter = new NodeFilter() {
		public boolean accept(Node nd) {
			return nd instanceof CardTag;
		}		
	};
	
	/**
	 * 解析出所有的链接，包括行为<a>与<go>
	 */
	static NodeFilter lnkFilter = new NodeFilter() {
		public boolean accept(Node node) {
			if(node instanceof WmlGoTag)
				return true;
			if(node instanceof LinkTag)
				return true;
			return false;
		}
	};
	
	/**
	 * WML文档的GO标签解析器
	 * @author Winter Lau
	 */
	private static class WmlGoTag extends CompositeTag {

	    private static final String[] mIds = new String[] {"GO"};
	    private static final String[] mEndTagEnders = new String[] {"ANCHOR"};

	    public String[] getIds (){
	        return (mIds);
	    }
	    public String[] getEnders (){
	        return (mIds);
	    }
	    public String[] getEndTagEnders (){
	        return (mEndTagEnders);
	    }
	    
	    public String getLink(){
	    	return super.getAttribute("href");
	    }

	    public void setLink(String link){	    	
	    	super.setAttribute("href", link);
	    }
	    
	    public String getMethod(){
	    	return super.getAttribute("method");
	    }
	}

	/**
	 * WML文档的Card标签解析器
	 * @author Winter Lau
	 */
	private static class CardTag extends CompositeTag {

	    private static final String[] mIds = new String[] {"card"};
	    private static final String[] mEndTagEnders = new String[] {"card"};

	    public String[] getIds (){
	        return (mIds);
	    }
	    public String[] getEnders (){
	        return (mIds);
	    }
	    public String[] getEndTagEnders (){
	        return (mEndTagEnders);
	    }
	    
	    public String getId(){
	    	return super.getAttribute("id");
	    }
	    
	    public String getTitle(){
	    	return super.getAttribute("title");
	    }
	}

}
