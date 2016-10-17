package com.liusoft.dlog4j;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;

import com.liusoft.dlog4j.util.StringUtils;

/**
 * 对访问用户的操作类
 */
public class DLOG_VisitorManager {

	// 访问者的列表最大数
	public static int MAX_VISITOR_SIZE = 0;

	public static void init(int maxVisitorSize) {
		MAX_VISITOR_SIZE = maxVisitorSize;
	}

	public static void init(String maxVisitorSize) {
		if (!StringUtils.isEmpty(maxVisitorSize)
				&& StringUtils.isNumeric(maxVisitorSize))
			MAX_VISITOR_SIZE = Integer.parseInt(maxVisitorSize);
	}

	public static void init(HttpServlet servlet) {
		String max = servlet.getInitParameter("max_visitor_size");
		if (!StringUtils.isEmpty(max) && StringUtils.isNumeric(max))
			MAX_VISITOR_SIZE = Integer.parseInt(max);
	}

	/**
	 * String 转换成 List
	 * 
	 * @param idstr
	 * @return
	 */
	public static List getVisitorIds(String idstr) {
		if (idstr.trim().equals(""))
			return null;
		List ids = new ArrayList();
		if (idstr.split(",").length > 0)
			for (int i = 0; i < idstr.split(",").length; i++) {
				ids
						.add(Integer.parseInt(idstr.split(",")[i].toString()
								.trim()));
			}
		return ids;
	}

	/**
	 * 是否存在id
	 * 
	 * @param visitors
	 * @param id
	 * @return
	 */
	public static boolean isPresence(List visitors, int id) {
		if (visitors == null)
			return false;
		for (int i = 0; i < visitors.size(); i++) {
			if (id == Integer.parseInt(visitors.get(i).toString()))
				return true;
		}
		return false;
	}

	/**
	 * 把集合中的id转换成字符串
	 * 
	 * @param visitors
	 * @return
	 */
	public static String getIds(List visitors) {
		String idStr = "";
		if (visitors != null)
			if (visitors.size() == 0) {
				return idStr;
			} else {
				for (int i = 0; (i < visitors.size() && i <= MAX_VISITOR_SIZE - 1); i++) {
					idStr = idStr + visitors.get(i).toString() + ",";
				}
				if (idStr.endsWith(","))
					idStr = idStr.substring(0, idStr.length() - 1);
			}

		return idStr;
	}

	public static String userVisitorQueryLink(String idStr) {
		return userVisitorQueryLink(getVisitorIds(idStr));
	}

	public static String userVisitorQueryLink(List vsistorIds) {
		String str = "";
		for (int i = 0; i < vsistorIds.size(); i++) {
			str = str + " ub.id=" + vsistorIds.get(i) + " OR ";
		}
		if (str.endsWith("OR "))
			str = str.substring(0, str.length() - 3);
		return str;
	}

	/**
	 * 
	 */
	public static int[] ListTransformArray(List vsistorIds) {
		int[] visitors = new int[vsistorIds.size()];
		for (int i = 0; i < vsistorIds.size(); i++) {
			visitors[i] = Integer.parseInt(vsistorIds.get(i).toString());
		}
		return visitors;
	}

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		// DLOG_VisitorManager.init("111");
		System.out.println(DLOG_VisitorManager.MAX_VISITOR_SIZE);
	}

}
