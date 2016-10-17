/*
 *  DLOG_StyleManager.java
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
 *  2006-5-20
 */
package com.liusoft.dlog4j;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServlet;

import com.liusoft.dlog4j.base.LayoutInfo;
import com.liusoft.dlog4j.base.LayoutInfo.StyleInfo;
import com.liusoft.dlog4j.beans.StyleBean;
import com.liusoft.dlog4j.dao.AdminDAO;
import com.liusoft.dlog4j.util.StringUtils;
//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 * ���ڴ�����վ���Ի����
 * 
 * @author liudong
 */
public class DLOG_LayoutManager {

	private static String styles_path;

	public static void init(HttpServlet servlet) {
		styles_path = servlet.getInitParameter("styles_base_path");
		if (StringUtils.isEmpty(styles_path))
			styles_path = servlet.getServletContext().getRealPath("/styles");
		else {
			if (styles_path.startsWith(Globals.LOCAL_PATH_PREFIX))
				styles_path = styles_path.substring(Globals.LOCAL_PATH_PREFIX
						.length());
			else if (styles_path.startsWith("/"))
				styles_path = servlet.getServletContext().getRealPath(
						styles_path);
		}
		if (!styles_path.endsWith(File.separator))
			styles_path += File.separator;
	}

	/**
	 * �б����еĲ���
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<LayoutInfo> layouts() {
		List<LayoutInfo> dlog_layouts = (List<LayoutInfo>) DLOG_CacheManager
				.getObjectCached("DLOG4J_layouts", "all");
		if (dlog_layouts == null) {
			File f = new File(styles_path);
			File[] layouts = f.listFiles(new FileFilter() {
				public boolean accept(File f) {
					return !f.getName().startsWith("_")
							&& !f.getName().startsWith(".") && f.isDirectory();
				}
			});
			dlog_layouts = new ArrayList<LayoutInfo>();
			for (int i = 0; layouts != null && i < layouts.length; i++) {
				LayoutInfo layout = new LayoutInfo(styles_path);
				layout.setName(layouts[i].getName());
				layout.setPreviewImg(layout.getName() + '/' + "main.gif");
				layout.setCreateTime(layouts[i].lastModified());
				dlog_layouts.add(layout);
			}
			Collections.sort(dlog_layouts);
			DLOG_CacheManager.putObjectCached("DLOG4J_layouts", "all",
					(Serializable) dlog_layouts);
		}
		return dlog_layouts;
	}

	/**
	 * ��ȡĳ������
	 * 
	 * @param name
	 * @return
	 */
	public static LayoutInfo getLayout(String name) {
		if (!isLayoutExist(name))
			return null;
		LayoutInfo layout = new LayoutInfo(styles_path);
		layout.setName(name);
		layout.setPreviewImg(layout.getName() + '/' + "main.gif");
		return layout;
	}

	/**
	 * �ж���վԤ���岼���Ƿ����
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isLayoutExist(String name) {
		return isStyleExist(name, "main.css");
	}

	/**
	 * �ж�ĳһ����ʽ�����ļ��Ƿ����
	 * 
	 * @param layout
	 * @param css
	 * @return
	 */
	public static boolean isStyleExist(String layout, String css) {
		StringBuffer main_css = new StringBuffer(styles_path);
		main_css.append(layout);
		main_css.append(File.separator);
		main_css.append(css);
		File f = new File(main_css.toString());
		return f.exists() && f.isFile();
	}

	// ==========DLOG 3.5==============
	/**
	 * �б����еĲ���_[��]
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<LayoutInfo> layouts_() {
		List<LayoutInfo> dlog_layouts = null;
		if (dlog_layouts == null) {
			File f = new File(styles_path);
			File[] layouts = f.listFiles(new FileFilter() {
				public boolean accept(File f) {
					return !f.getName().startsWith("_")
							&& !f.getName().startsWith(".") && f.isDirectory();
				}
			});

			dlog_layouts = new ArrayList<LayoutInfo>();
			// ��ģ�����Ʊ�
			List layout_child_name = new ArrayList();
			for (int i = 0; layouts != null && i < layouts.length; i++) {
				String layoutPath = styles_path + layouts[i].getName();
				// TODO ���1:xxx.css�ļ������̶� ����д�� ������Ҫ�Ľ�
				File[] layouts_childs = layout_child(layoutPath);
				Hashtable hashtable = new Hashtable();
				// ��ģ���ֵ��
				for (int t = 0; t < layouts_childs.length; t++) {
					if (layouts_childs[t].getName().endsWith(".css")) {
						hashtable.put(layouts_childs[t].getName().substring(0,
								layouts_childs[t].getName().indexOf(".")),
								layouts_childs[t].getName());
						layout_child_name.add(layouts_childs[t].getName()
								.substring(
										0,
										layouts_childs[t].getName()
												.indexOf(".")));
					}

					else
						hashtable.put(layouts_childs[t].getName(),
								layouts_childs[t].getName());
				}

				for (int t = 0; t < layout_child_name.size(); t++) {
					// ��ģ������
					String name = (String) layout_child_name.get(t);
					LayoutInfo layout = new LayoutInfo(styles_path);
					layout.setName(layouts[i].getName());
					layout.setChildName(name);
					if (hashtable.containsKey(name))
						layout.setCss(name + ".css");

					if (hashtable.containsKey(name + ".gif"))
						layout.setPreview_l_Img(name + ".gif");

					if (hashtable.containsKey(name + "_s.gif"))
						layout.setPreview_s_Img(name + "_s.gif");

					layout.setCreateTime(layouts[i].lastModified());
					dlog_layouts.add(layout);
				}
			}

			Collections.sort(dlog_layouts);
			DLOG_CacheManager.putObjectCached("DLOG4J_layouts", "all",
					(Serializable) dlog_layouts);
		}
		return dlog_layouts;
	}

	/**
	 * ��ģ��
	 */
	private static File[] layout_child(String layoutPath) {
		// TODO �Ա��1�ĸĽ�����
		File f = new File(layoutPath);
		File[] layouts = f.listFiles(new FileFilter() {
			public boolean accept(File f) {

				return (f.isFile() && (f.getName().endsWith(".css") || f
						.getName().endsWith(".gif")));
			}
		});
		return layouts;
	}

	/**
	 * ��������Ŀ¼�ṹ
	 * */
	public static List<LayoutInfo> getFileList(String name,String path)
	{
		List<LayoutInfo> list=new ArrayList<LayoutInfo>();	
		String named="";
		named+=name+"\\";//��һ��Ŀ¼
		File f = new File(styles_path+name);
		File[] files = f.listFiles();
		for(int i=1;i<files.length;i++)
		{
			if(files[i].isFile())
			{			
				LayoutInfo info=new LayoutInfo(files[i].getPath());
				info.setType(LayoutInfo.TYPE_FILE);//�ļ�
				info.setName(files[i].getName());
				list.add(info);
				
			}else if (files[i].isDirectory())
			{
				LayoutInfo info=new LayoutInfo(files[i].getPath());
				info.setType(LayoutInfo.TYPE_DIRECTORY);//Ŀ¼
				info.setFlis(getFileList(named+files[i].getName(), path));
				info.setName(files[i].getName());
				list.add(info);				
			}
				
		}
		return  list;
	}

	/**
	 * ֻ��ȡ�����ļ�Ŀ¼����
	 * */
	public static List getFileList_name() {
		List<LayoutInfo> list=new ArrayList<LayoutInfo>();	
		File f = new File(styles_path);
		File[] files = f.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
		for(int i=1;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{			
				LayoutInfo info=new LayoutInfo(files[i].getPath());
				info.setType(LayoutInfo.TYPE_DIRECTORY);//�ļ�
				info.setName(files[i].getName());
				list.add(info);
				
			}
				
		}
		return  list;
	}

	/**
	 * ֻ��ȡ����Ŀ¼.css�ļ�
	 * */
	public static List getFileList_css(String name) {
		List<LayoutInfo> list=new ArrayList<LayoutInfo>();	
		File f = new File(styles_path+name);
		Hashtable temp=styleHas();
		File[] files = f.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isFile();
			}
		});
	
		for(int i=1;i<files.length;i++)
		{			
			if(files[i].isFile()&&files[i].getName().endsWith(".css"))
			{			
				LayoutInfo info=new LayoutInfo(files[i].getPath());
				info.setType(LayoutInfo.TYPE_FILE);//�ļ�
				info.setName(files[i].getName());
				if((temp.get(name+"\\"+info.getName())==null))
				list.add(info);				
			}				
		}
		return  list;
	}

	/**
	 * �ж��Ƿ���Ԥ��ͼ ����ͼ
	 * */
	public static boolean isImg(String preview_image,String name) {
		File f = new File(styles_path+name+"\\"+preview_image);	
		
		return f.isFile();
	}
	/**
	 * 
	 * */
	public static Hashtable styleHas() {
		List list=AdminDAO.getStylas();
		Hashtable stylesHas=new Hashtable();
		for(int i=0;i<list.size();i++)
		{
			StyleBean bean=(StyleBean)list.get(i);
			//  bean.getName()+"\\"+bean.getCss() == 10000\main.css
			stylesHas.put(bean.getName()+"\\"+bean.getCss(), bean);	
		}
		return stylesHas;
	}
}



