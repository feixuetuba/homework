/*
 *  VelocityTextTool.java
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
package com.liusoft.dlog4j.velocity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.tools.struts.MessageTool;
import org.htmlparser.util.ParserException;

import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * 对TextTool的扩展,以支持整数数组
 * 
 * @author Winter Lau
 */
public class VelocityTextTool extends MessageTool {

	/**
	 * HTML输出内容格式转换
	 * @param content
	 * @return
	 */
	public String html(Object content){
		return StringUtils.formatContent((String)content);
	}
	
	private static List<String> tokens_keep;
	private static List<String> tokens_remove;
	static{
		tokens_keep = new ArrayList<String>();
		tokens_keep.add("&nbsp;");
		tokens_keep.add("<br/>");
		tokens_keep.add("<br />");
		tokens_remove = new ArrayList<String>();
		tokens_remove.add("<p>");
		tokens_remove.add("</p>");
	}
	
	/**
	 * 显示友好的格式化文本信息
	 * @param content
	 * @return
	 */
	public static String friendly_text(String content){
        if(content==null)
            return "";
        String html = content;
        for(int i=0;i<tokens_remove.size();i++){
        	html = StringUtils.remove(html, (String)tokens_remove.get(i));
        }
        List<String> randoms = new ArrayList<String>();
        for(int i=0;i<tokens_keep.size();i++){
    		String randomStr = String.valueOf(System.currentTimeMillis())+'_'+i;
    		html = StringUtils.replaceIgnoreCase(html, (String)tokens_keep.get(i), randomStr);
    		randoms.add(randomStr);
        }
		html = StringUtils.replace(html, "&hellip;", "…");
		html = StringUtils.replace(html, "&ldquo;", "“");
		html = StringUtils.replace(html, "&rdquo;", "”");
		html = StringUtils.replace(html, "&mdash;", "—");
		html = StringUtils.replace(html, "&quot;", "\"");
		html = StringUtils.replace(html, "&", "&amp;");
		html = StringUtils.replace(html, "'", "&apos;");
		html = StringUtils.replace(html, "\"", "&quot;");
		html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
		//html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
		html = StringUtils.replace(html, "<", "&lt;");
		html = StringUtils.replace(html, ">", "&gt;");
		for(int i=0;i<tokens_keep.size();i++){
			String randomStr = (String)randoms.get(i);
			html = StringUtils.replaceIgnoreCase(html, randomStr, (String)tokens_keep.get(i));
		}
		return html;
	}

    /**
     * 转换HTML成纯文本，用于显示最新评论或者是日记在首页的预览信息
     * @param html
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParserException 
     */
	public static String plain_text(String html) throws Exception{
		return StringUtils.extractText(html);
	}

	/**
	 * 字符串智能截断
	 * @param str
	 * @param maxWidth
	 * @return
	 */
	public String abbreviate(String str, int maxWidth){
		if(str==null) return null;
		return StringUtils.abbreviate(str,maxWidth);
	}

	/**
	 * 文本替换(login.vm)
	 * 
	 * @param src
	 * @param srcText
	 * @param desText
	 * @return
	 */
	public String replace(String src, String srcText, String desText) {
		return StringUtils.replace(src, srcText, desText);
	}

	/**
	 * 判断是否是一个合法的邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public boolean is_email(String email) {
		return StringUtils.isEmail(email);
	}
	
	public boolean is_empty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public boolean not_empty(String str) {
		return !is_empty(str);
	}

	public static void main(String[] args){
		String html = "hello my name is <form> <bR/>";
		System.out.println(friendly_text(html));
	}
	
	/**
	 * 返回站长自定义的日记栏目名称
	 * 
	 * @param site
	 * @return
	 */
	public String diary(SiteBean site) {
		if (site != null && site.getFunctionName() != null
				&& StringUtils.isNotEmpty(site.getFunctionName().getDiary()))
			return site.getFunctionName().getDiary();
		return get("channel.diary", "ui");
	}

	/**
	 * 返回站长自定义的相册栏目名称
	 * 
	 * @param site
	 * @return
	 */
	public String photo(SiteBean site) {
		if (site != null && site.getFunctionName() != null
				&& StringUtils.isNotEmpty(site.getFunctionName().getPhoto()))
			return site.getFunctionName().getPhoto();
		return get("channel.photo", "ui");
	}

	/**
	 * 返回站长自定义的音乐栏目名称
	 * 
	 * @param site
	 * @return
	 */
	public String music(SiteBean site) {
		if (site != null && site.getFunctionName() != null
				&& StringUtils.isNotEmpty(site.getFunctionName().getMusic()))
			return site.getFunctionName().getMusic();
		return get("channel.music", "ui");
	}

	/**
	 * 返回站长自定义的讨论栏目名称
	 * 
	 * @param site
	 * @return
	 */
	public String bbs(SiteBean site) {
		if (site != null && site.getFunctionName() != null
				&& StringUtils.isNotEmpty(site.getFunctionName().getForum()))
			return site.getFunctionName().getForum();
		return get("channel.bbs", "ui");
	}

	/**
	 * 返回站长自定义的留言本栏目名称
	 * 
	 * @param site
	 * @return
	 */
	public String guestbook(SiteBean site) {
		if (site != null
				&& site.getFunctionName() != null
				&& StringUtils
						.isNotEmpty(site.getFunctionName().getGuestbook()))
			return site.getFunctionName().getGuestbook();
		return get("channel.guestbook", "ui");
	}
	
	/**
	 * 返回站长自定义的好友本栏目名称
	 * 
	 * @param site
	 * @return
	 * 
	 */
	public String friend(SiteBean site) {
		if (site != null
				&& site.getFunctionName() != null
				&& StringUtils
						.isNotEmpty(site.getFunctionName().getFriend()))
			return site.getFunctionName().getFriend();
		return get("channel.friend", "ui");
	}
	
	public String ui(String key){
		return super.get(key, "ui");
	}

	public String ui(String key, String arg){
		return super.get(key, "ui", new String[]{arg});
	}
	
	public String ui(String arg0, List arg1) {
		return super.get(arg0, "ui", arg1);
	}

	public String ui(String arg0, Object[] arg1) {
		return super.get(arg0, "ui", arg1);
	}

	public String ui(String arg0, int[] arg1) {
		return this.get(arg0, "ui", arg1);
	}

	public String ui(String arg0, int arg1) {
		return this.get(arg0, "ui", new int[]{arg1});
	}

	public String wml(String key){
		return super.get(key, "wml");
	}

	public String wml(String key, String arg){
		return super.get(key, "wml", new String[]{arg});
	}
	
	public String wml(String arg0, List arg1) {
		return super.get(arg0, "wml", arg1);
	}

	public String wml(String arg0, Object[] arg1) {
		return super.get(arg0, "wml", arg1);
	}

	public String wml(String arg0, int[] arg1) {
		return this.get(arg0, "wml", arg1);
	}

	public String wml(String arg0, int arg1) {
		return this.get(arg0, "wml", new int[]{arg1});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.tools.struts.MessageTool#get(java.lang.String,
	 *      java.lang.Object[])
	 */
	public String get(String key, int[] args) {
		if (args == null || args.length == 0)
			return get(key).toString();
		Integer[] nArgs = new Integer[args.length];
		for (int i = 0; i < args.length; i++)
			nArgs[i] = new Integer(args[i]);
		return get(key, nArgs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.tools.struts.MessageTool#get(java.lang.String,
	 *      java.lang.String, java.lang.Object[])
	 */
	public String get(String key, String bundle, int[] args) {
		if (args == null || args.length == 0)
			return get(key, bundle);
		Integer[] nArgs = new Integer[args.length];
		for (int i = 0; i < args.length; i++)
			nArgs[i] = new Integer(args[i]);
		return get(key, bundle, nArgs);
	}
}
