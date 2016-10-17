package com.liusoft.dlog4j.dao;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.methods.GetMethod;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.liusoft.dlog4j.DLOG_CacheManager;
import com.liusoft.dlog4j.base.LayoutInfo;
import com.liusoft.dlog4j.beans.AnnexBean;
import com.liusoft.dlog4j.beans.DiaryBean;
import com.liusoft.dlog4j.beans.PhotoBean;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.StyleBean;
import com.liusoft.dlog4j.beans.TagBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.formbean.AdminForm;

public class AdminDAO extends DAO {

	/**
	 * 后台管理人员
	 */
	public static UserBean getManager(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM UserBean as ub ");
		hql.append(" WHERE ub.popedom <> ? AND ub.id=? ");
		return (UserBean) uniqueResult(hql.toString(), UserBean.POPEDOM_COMMON,
				id);
	}

	/**
	 * 今日发帖数
	 */
	public static int today_diary_content() {
		Date today = getToday();
		String hql = "SELECT COUNT(*) FROM DiaryBean as d WHERE d.writeTime>=?";
		int count = Integer.parseInt(uniqueResult(hql, today).toString());
		return count;
	}

	/**
	 * 今日回帖数
	 */
	public static int today_reply() {
		String hql_diary = "SELECT COUNT(*) FROM DiaryReplyBean as d WHERE d.replyTime>=?";// 日记回复
		String hql_photo = "SELECT COUNT(*) FROM PhotoReplyBean as p WHERE p.replyTime>=?";// 照片回复
		String hql_topic = "SELECT COUNT(*) FROM TopicReplyBean as t WHERE t.replyTime>=?";// 论坛回复
		int diary_count = Integer.parseInt(uniqueResult(hql_diary, getToday())
				.toString());
		int photo_count = Integer.parseInt(uniqueResult(hql_photo, getToday())
				.toString());
		int topic_count = Integer.parseInt(uniqueResult(hql_topic, getToday())
				.toString());
		return diary_count + photo_count + topic_count;
	}

	/**
	 * 今日新注册用户数
	 */
	public static int today_create_user() {
		String hql = "SELECT COUNT(*) FROM UserBean as u WHERE u.regTime>=?";// 用户注册
		int count = Integer.parseInt(uniqueResult(hql, getToday()).toString());
		return count;
	}

	/**
	 * 站点总数
	 */
	public static int siteCount() {
		String hql = "SELECT COUNT(*) FROM UserBean";
		return Integer.parseInt(uniqueResult(hql).toString());
	}

	/**
	 * 最新站点总数
	 */
	public static int new_site_count() {
		// 当天注册的站点总数
		String hql = "SELECT COUNT(*) FROM UserBean WHERE u.regTime>? ";
		return Integer.parseInt(uniqueResult(hql, getToday()).toString());
	}

	/**
	 * 最新站点列表
	 */
	public static List new_site_list(int fromIdx, int fetchCount) {
		try {
			String hql = "FROM SiteBean AS sb  ORDER BY sb.id DESC";
			return executeQuery(hql, fromIdx, fetchCount);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return null;
	}

	/**
	 * 热门站点
	 */
	public static List hot_site_list(String bean, int fromIdx, int count) {
		// StringBuffer hql = new StringBuffer(
		// "SELECT
		// sb.id,sb.uniqueName,sb.friendlyName,sb.owner.name,sb.createTime
		// ,sb.level ,sb.status
		// ,(COUNT(d.id)*100+SUM(d.replyCount)*10+SUM(d.viewCount))
		// as count ,d.site.owner.id FROM ");
		// hql.append(bean);
		//		
		// hql
		// .append(" as d , SiteBean as sb WHERE d.site.id =sb.id GROUP BY sb.id
		// ORDER BY 8 DESC");
		//		
		StringBuffer hql = new StringBuffer();
		hql
				.append("SELECT d.site.id,d.site.uniqueName,d.site.friendlyName,d.site.owner.nickname,d.site.createTime,d.site.level ,d.site.status,d.site.owner.id,(COUNT(d.id)*100+SUM(d.replyCount)*10+SUM(d.viewCount)) FROM ");
		hql.append(bean);
		hql
				.append(" d GROUP BY d.site.id,d.site.uniqueName,d.site.friendlyName ,d.site.owner.nickname,d.site.createTime,d.site.level ,d.site.status ,d.site.owner.id ORDER BY 9 DESC");

		List objs = executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);
		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			int siteid = ((Number) ((Object[]) objs.get(i))[0]).intValue();
			String name1 = (String) ((Object[]) objs.get(i))[1];
			String name2 = (String) ((Object[]) objs.get(i))[2];
			String name3 = (String) ((Object[]) objs.get(i))[3];
			Date name4 = (Date) ((Object[]) objs.get(i))[4];
			int name5 = ((Number) ((Object[]) objs.get(i))[5]).intValue();
			int name6 = ((Number) ((Object[]) objs.get(i))[6]).intValue();
			int name8 = ((Number) ((Object[]) objs.get(i))[8]).intValue();
			SiteBean site = new SiteBean(siteid);
			site.setUniqueName(name1);
			site.setFriendlyName(name2);
			UserBean owner = new UserBean();
			owner.setName(name3);
			site.setOwner(owner);
			site.setCreateTime(name4);
			site.setLevel(name5);
			site.setStatus(name6);
			site.getOwner().setId(name8);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * 最新更新站点列表
	 */
	public static List new_update_list(String bean, int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("");
		if (bean.equals("DiaryBean"))
			hql
					.append("SELECT d.site.id,d.site.uniqueName,d.site.friendlyName,d.site.owner.name,max(d.writeTime),d.site.level,d.status.type,d.site.owner.id ,d.site.status FROM ");
		else if (bean.equals("PhotoBean"))
			hql
					.append("SELECT d.site.id,d.site.uniqueName,d.site.friendlyName,d.site.owner.name,max(d.createTime),d.site.level,d.status.type,d.site.owner.id,d.site.status FROM ");
		else
			return null;
		hql.append(bean);

		hql.append(" as d ");
		hql
				.append(" GROUP BY d.site.id,d.site.uniqueName,d.site.friendlyName,d.site.owner.name,d.site.level,d.status.type,d.site.owner.id ,d.site.status ");
		hql.append(" ORDER BY 5 DESC ");
		List objs = executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);
		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			int siteid = ((Number) ((Object[]) objs.get(i))[0]).intValue();
			String name1 = (String) ((Object[]) objs.get(i))[1];
			String name2 = (String) ((Object[]) objs.get(i))[2];
			String name3 = (String) ((Object[]) objs.get(i))[3];
			Date name4 = (Date) ((Object[]) objs.get(i))[4];
			int name5 = ((Number) ((Object[]) objs.get(i))[5]).intValue();
			int name6 = ((Number) ((Object[]) objs.get(i))[6]).intValue();
			int name7 = ((Number) ((Object[]) objs.get(i))[7]).intValue();
			int name8 = ((Number) ((Object[]) objs.get(i))[8]).intValue();
			SiteBean site = new SiteBean(siteid);
			site.setUniqueName(name1);
			site.setFriendlyName(name2);
			UserBean owner = new UserBean();
			owner.setName(name3);
			site.setOwner(owner);
			site.setCreateTime(name4);
			site.setLevel(name5);
			site.setStatus(name6);
			site.getOwner().setId(name7);
			site.setStatus(name8);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * 月无更新站点[日记]
	 */
	public static List diary_not_update_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer(
				"SELECT d.site.id,d.site.uniqueName,d.site.friendlyName, d.site.owner.name,max(d.writeTime),d.site.type,d.status.type,d.site.owner.id ,d.site.status FROM ");
		hql
				.append("DiaryBean d WHERE d.writeTime<?  GROUP BY d.site.id  ORDER BY 5 DESC");
		List objs = executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count, getLastMonth());

		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			int siteid = ((Number) ((Object[]) objs.get(i))[0]).intValue();
			String name1 = (String) ((Object[]) objs.get(i))[1];
			String name2 = (String) ((Object[]) objs.get(i))[2];
			String name3 = (String) ((Object[]) objs.get(i))[3];
			Date name4 = (Date) ((Object[]) objs.get(i))[4];
			int name5 = ((Number) ((Object[]) objs.get(i))[5]).intValue();
			int name6 = ((Number) ((Object[]) objs.get(i))[6]).intValue();
			int name7 = ((Number) ((Object[]) objs.get(i))[7]).intValue();
			int name8 = ((Number) ((Object[]) objs.get(i))[8]).intValue();
			SiteBean site = new SiteBean(siteid);
			site.setUniqueName(name1);
			site.setFriendlyName(name2);
			UserBean owner = new UserBean();
			owner.setName(name3);
			site.setOwner(owner);
			site.setCreateTime(name4);
			site.setType(name5);
			site.setStatus(name6);
			site.getOwner().setId(name7);
			site.setStatus(name8);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * 月无更新站点[照片]
	 */
	public static List photo_not_update_list(int fromIdx, int count) {

		StringBuffer hql = new StringBuffer(
				"SELECT d.site.id,d.site.uniqueName,d.site.friendlyName, d.site.owner.name,max(d.createTime),d.site.type,d.status.type,d.site.owner.id FROM ");
		hql
				.append("PhotoBean d WHERE (d.status=0 AND d.site.status=0) AND d.createTime<?  GROUP BY d.site.id ORDER BY 5 DESC");
		List objs = executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count, getLastMonth());

		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			int siteid = ((Number) ((Object[]) objs.get(i))[0]).intValue();
			String name1 = (String) ((Object[]) objs.get(i))[1];
			String name2 = (String) ((Object[]) objs.get(i))[2];
			String name3 = (String) ((Object[]) objs.get(i))[3];
			Date name4 = (Date) ((Object[]) objs.get(i))[4];
			int name5 = ((Number) ((Object[]) objs.get(i))[5]).intValue();
			int name6 = ((Number) ((Object[]) objs.get(i))[6]).intValue();
			int name7 = ((Number) ((Object[]) objs.get(i))[7]).intValue();
			SiteBean site = new SiteBean(siteid);
			site.setUniqueName(name1);
			site.setFriendlyName(name2);
			UserBean owner = new UserBean();
			owner.setName(name3);
			site.setOwner(owner);
			site.setCreateTime(name4);
			site.setType(name5);
			site.setStatus(name6);
			site.getOwner().setId(name7);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * 月点击排行
	 */
	public static List month_hot_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("SELECT");
		hql
				.append(" sb.id,sb.uniqueName,sb.friendlyName,sb.owner.name,sb.createTime ,SUM(s.uvCount),sb.type, sb.status,s.owner.id");
		hql.append(" FROM SiteStatBean s,SiteBean sb ");
		hql.append(" WHERE sb.id=s.siteId AND s.statDate>=? ");
		hql.append(" GROUP BY sb.id ");
		hql.append(" ORDER BY 6 DESC ");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		int statDate = getStatDate(cal);
		List objs = executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count, statDate);
		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			int siteid = ((Number) ((Object[]) objs.get(i))[0]).intValue();
			String name1 = (String) ((Object[]) objs.get(i))[1];
			String name2 = (String) ((Object[]) objs.get(i))[2];
			String name3 = (String) ((Object[]) objs.get(i))[3];
			Date name4 = (Date) ((Object[]) objs.get(i))[4];
			int name5 = ((Number) ((Object[]) objs.get(i))[6]).intValue();
			int name7 = ((Number) ((Object[]) objs.get(i))[7]).intValue();
			int name8 = ((Number) ((Object[]) objs.get(i))[8]).intValue();

			SiteBean site = new SiteBean(siteid);
			site.setUniqueName(name1);
			site.setFriendlyName(name2);
			UserBean owner = new UserBean();
			owner.setName(name3);
			site.setOwner(owner);
			site.setCreateTime(name4);
			site.setType(name5);
			site.setStatus(name7);
			site.getOwner().setId(name8);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * 周点击排行
	 */
	public static List week_hot_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("SELECT");
		hql
				.append(" sb.id,sb.uniqueName,sb.friendlyName,sb.owner.name,sb.createTime ,SUM(s.uvCount),sb.type ,sb.status");
		hql.append(" FROM SiteStatBean s,SiteBean sb ");
		hql.append(" WHERE sb.id=s.siteId AND s.statDate>=? ");
		hql.append(" GROUP BY sb.id ");
		hql.append(" ORDER BY 6 DESC ");

		Calendar cal = Calendar.getInstance();
		int week = cal.get(Calendar.DAY_OF_WEEK);
		if (week > 1)
			cal.add(Calendar.DATE, 1 - week);
		int statDate = getStatDate(cal);

		List objs = executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count, statDate);
		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			int siteid = ((Number) ((Object[]) objs.get(i))[0]).intValue();
			String name1 = (String) ((Object[]) objs.get(i))[1];
			String name2 = (String) ((Object[]) objs.get(i))[2];
			String name3 = (String) ((Object[]) objs.get(i))[3];
			Date name4 = (Date) ((Object[]) objs.get(i))[4];
			int name6 = ((Number) ((Object[]) objs.get(i))[6]).intValue();
			int name7 = ((Number) ((Object[]) objs.get(i))[7]).intValue();
			SiteBean site = new SiteBean(siteid);
			site.setUniqueName(name1);
			site.setFriendlyName(name2);
			UserBean owner = new UserBean();
			owner.setName(name3);
			site.setOwner(owner);
			site.setCreateTime(name4);
			site.setType(name6);
			site.setStatus(name7);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * 日点击排行
	 */
	public static List day_hot_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("SELECT");
		hql
				.append(" sb.id,sb.uniqueName,sb.friendlyName,sb.owner.name,sb.createTime ,SUM(s.uvCount),sb.type,sb.status ");
		hql.append(" FROM SiteStatBean s,SiteBean sb ");
		hql.append(" WHERE sb.id=s.siteId AND s.statDate>=? ");
		hql.append(" GROUP BY sb.id ");
		hql.append(" ORDER BY 6 DESC ");

		Calendar cal = Calendar.getInstance();
		int statDate = getStatDate(cal);

		List objs = executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count, statDate);
		List<SiteBean> sites = new ArrayList<SiteBean>();
		for (int i = 0; i < objs.size(); i++) {
			int siteid = ((Number) ((Object[]) objs.get(i))[0]).intValue();
			String name1 = (String) ((Object[]) objs.get(i))[1];
			String name2 = (String) ((Object[]) objs.get(i))[2];
			String name3 = (String) ((Object[]) objs.get(i))[3];
			Date name4 = (Date) ((Object[]) objs.get(i))[4];
			int name6 = ((Number) ((Object[]) objs.get(i))[6]).intValue();
			int name7 = ((Number) ((Object[]) objs.get(i))[6]).intValue();
			SiteBean site = new SiteBean(siteid);
			site.setUniqueName(name1);
			site.setFriendlyName(name2);
			UserBean owner = new UserBean();
			owner.setName(name3);
			site.setOwner(owner);
			site.setCreateTime(name4);
			site.setType(name6);
			site.setStatus(name7);
			sites.add(site);
		}
		return sites;
	}

	/**
	 * 访问排行
	 */
	public static List visited_hot_list(int index, int fromIdx, int count) {
		int values[] = getDates();
		StringBuffer hql = new StringBuffer("SELECT");
		hql
				.append(" sb.id,sb.friendlyName,sb.uniqueName,sb.owner.name,sb.owner.id, ");

		hql
				.append(" (SELECT SUM(d.uvCount) FROM SiteStatBean AS d  WHERE d.siteId=sb.id  AND d.statDate="
						+ values[0] + " GROUP BY d.siteId) as month, ");
		hql
				.append(" (SELECT SUM(d.uvCount) FROM SiteStatBean AS d  WHERE d.siteId=sb.id  AND d.statDate<="
						+ values[1]
						+ " AND d.statDate>="
						+ values[2]
						+ " GROUP BY d.siteId) as week, ");
		hql
				.append(" (SELECT SUM(d.uvCount) FROM SiteStatBean AS d  WHERE d.siteId=sb.id  AND d.statDate<="
						+ values[3]
						+ " AND d.statDate>="
						+ values[4]
						+ " GROUP BY d.siteId) as day, ");
		hql
				.append(" (SELECT SUM(d.uvCount) FROM SiteStatBean AS d  WHERE d.siteId=sb.id  AND d.statDate<="
						+ values[5] + "  GROUP BY d.siteId ) as full, ");
		hql.append(" sb.level,sb.status ");
		hql.append(" FROM SiteBean AS sb");
		if (index == 1)
			hql.append(" ORDER BY 6 DESC ");// 日排序
		else if (index == 2)
			hql.append(" ORDER BY 7 DESC ");// 周排序
		else if (index == 3)
			hql.append(" ORDER BY 8 DESC ");// 月排序
		else if (index == 4)
			hql.append(" ORDER BY 9 DESC ");// 全部排序
		List list = executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);
		return list;

	}

	/**
	 * 今日访问人次
	 */
	public static long getUVToday(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT SUM(d.uvCount) ");
		hql.append(" FROM SiteStatBean AS d  ");
		hql.append(" WHERE d.siteId=? AND d.statDate=? GROUP BY d.siteId");
		return toLong(executeStat(hql.toString(), id, getDates()[0]), 0);
	}

	/**
	 * 本周访问人次
	 */
	public static long getUVThisWeek(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT SUM(d.uvCount) ");
		hql.append(" FROM SiteStatBean AS d  ");
		hql
				.append(" WHERE d.siteId=?  AND d.statDate>=? AND d.statDate<=? GROUP BY d.siteId");
		return toLong(executeStat(hql.toString(), id, getDates()[1],
				getDates()[2]), 0);
	}

	/**
	 * 本月访问人次
	 */
	public static long getUVThisMonth(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT SUM(d.uvCount) ");
		hql.append(" FROM SiteStatBean AS d  ");
		hql
				.append(" WHERE d.siteId=?  AND d.statDate>=? AND d.statDate<=? GROUP BY d.siteId");
		return toLong(executeStat(hql.toString(), id, getDates()[3],
				getDates()[4]), 0);
	}

	/**
	 * 全部访问人次
	 */
	public static long getUVTotal(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT SUM(d.uvCount) ");
		hql.append(" FROM SiteStatBean AS d  ");
		hql.append(" WHERE d.siteId=?  AND d.statDate<=?  GROUP BY d.siteId");
		return toLong(executeStat(hql.toString(), id, getDates()[5]), 0);
	}

	private static long toLong(Number v, int defaultValue) {
		return (v != null) ? v.longValue() : defaultValue;
	}

	private static int[] getDates() {
		Calendar day = Calendar.getInstance();
		Calendar week = Calendar.getInstance();
		Calendar month = Calendar.getInstance();
		week.setFirstDayOfWeek(Calendar.MONDAY);// 设置星期一是第一天
		week.set(week.DAY_OF_WEEK, week.getFirstDayOfWeek());
		month.set(day.DAY_OF_MONTH, 1);
		int statdates[] = { getStatDate(day), getStatDate(day),
				getStatDate(week), getStatDate(day), getStatDate(month),
				getStatDate(day) };
		return statdates;
	}

	private static int getStatDate(Calendar cal) {
		return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1)
				* 100 + cal.get(Calendar.DATE);
	}

	/**
	 * 未开通个人网记
	 */
	public static List not_create_site_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("");
		hql.append("");
		hql.append(" FROM UserBean as u ");
		hql.append(" WHERE u.ownSiteId=0 ");
		return executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);
	}

	/**
	 * 已开通个人网记
	 */
	public static List create_site_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("");
		hql.append(" FROM UserBean as u ");
		hql.append(" WHERE u.ownSiteId>0 ");
		return executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);
	}

	/**
	 * 月无登录
	 */
	public static List month_not_login_site_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer("");
		hql.append("  ");
		hql.append(" FROM UserBean as u ");
		hql.append(" WHERE u.lastTime<? ");
		return executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count, getLastMonth());
	}

	/**
	 * 站点设置为推荐
	 */
	public static boolean siteRecommend(int id) {
		StringBuffer hql_ = new StringBuffer();
		hql_.append(" SELECT max(sb.level)");
		hql_.append(" FROM SiteBean as sb ");
		int level = Integer.parseInt(uniqueResult(hql_.toString()).toString()) + 1;
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE SiteBean as d");
		hql.append(" SET  d.level=? ");
		hql.append(" WHERE d.id=? ");
		return commitUpdate(hql.toString(), level, id) > 0;
	}

	/**
	 * 取消站点推荐
	 */
	public static boolean cancelSiteRecommend(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE SiteBean as d");
		hql.append(" SET  d.level=? ");
		hql.append(" WHERE d.id=? ");
		return commitUpdate(hql.toString(), 1, id) > 0;
	}

	// =====================

	/**
	 * 日记总数
	 */
	public static int diary_count() {
		String hql = "SELECT COUNT(*) FROM DiaryBean as d WHERE d.status=0";// 用户注册
		return Integer.parseInt(uniqueResult(hql).toString());
	}

	/**
	 * 最新日记列表
	 */
	public static List new_diary_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM DiaryBean as d ");
		hql.append(" WHERE  d.status=0");
		hql.append(" ORDER BY  d.writeTime  DESC ");
		// return executeQueryCacheable("dlog_admin_info", hql.toString(),
		// fromIdx, count);
		return executeQuery(hql.toString(), fromIdx, count);
	}

	/**
	 * 热门日记列表
	 */
	public static List hot_diary_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM DiaryBean as d ");
		hql.append(" WHERE  d.status=0");
		hql.append(" ORDER BY  (d.viewCount+d.replyCount)  DESC ");
		// return executeQueryCacheable("dlog_admin_info", hql.toString(),
		// fromIdx, count);
		return executeQuery(hql.toString(), fromIdx, count);
	}

	/**
	 * 最新日记平论
	 */
	public static List new_diary_reply_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM DiaryBean as d ");
		hql.append(" WHERE  d.status=0");
		hql.append(" ORDER BY  d.lastReplyTime  DESC ");
		// return executeQueryCacheable("dlog_admin_info", hql.toString(),
		// fromIdx, count);
		return executeQuery(hql.toString(), fromIdx, count);
	}

	/**
	 * 搜索日记 从缓存中读取HQL语句与相应参数
	 */
	public static List search_diary(String key, int fromIdx, int count) {
		String hql = (String) AdminDAO.getCache(key + "_sql");
		Object value[] = (Object[]) AdminDAO.getCache(key + "_value");

		List diarys = executeQueryCacheable("query.diary_search", hql, fromIdx,
				count, value);
		return diarys;
	}

	/**
	 * 日记回复排行
	 */
	public static List diary_reply(int order, int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql
				.append("FROM DiaryBean as d WHERE d.status=0 ORDER BY d.replyCount DESC");
		return executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);
	}

	/**
	 * 日记阅读排行
	 */
	public static List diary_view(int order, int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql
				.append("FROM DiaryBean as d WHERE d.status=0 ORDER BY d.viewCount DESC");
		hql.append(order_by[order - 1]);
		return executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);
	}

	protected static String order_by[] = { " ORDER BY d.id DESC ",
			" ORDER BY d.author DESC " };

	private static Date getToday() {
		Date date1 = new Date();
		Date date2 = new Date();
		date2.setYear(date1.getYear());
		date2.setMonth(date1.getMonth());
		date2.setHours(0);
		date2.setSeconds(0);
		date2.setMinutes(0);
		return date2;
	}

	private static Date getLastMonth() {
		Date date1 = new Date();
		Date date2 = new Date();
		date2.setYear(date1.getYear());
		date2.setMonth(date1.getMonth() - 1);
		date2.setHours(0);
		date2.setSeconds(0);
		date2.setMinutes(0);
		return date2;
	}

	/**
	 * 推荐日记列表
	 */
	public static List site_recommend(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM SiteBean as d ");
		hql.append(" WHERE d.level>? ");
		hql.append(" ORDER BY  d.level DESC ");
		return executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count, 1);

	}

	/**
	 * 日记推荐
	 */
	public static boolean DiaryRecommend(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE DiaryBean as d");
		hql.append(" SET  d.type=? ");
		hql.append(" WHERE d.id=? ");
		return commitUpdate(hql.toString(), DiaryBean.INFO_TYPE_ELITE, id) > 0;
	}

	/**
	 * 日记取消推荐
	 */
	public static boolean cancelDiaryRecommend(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE DiaryBean as d");
		hql.append(" SET  d.type=? ");
		hql.append(" WHERE d.id=? ");
		return commitUpdate(hql.toString(), 0, id) > 0;
	}

	/**
	 * 站点屏蔽
	 */
	public static boolean siteShield(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE SiteBean as d");
		hql.append(" SET  d.status=? ");
		hql.append(" WHERE d.id=? ");
		return commitUpdate(hql.toString(), -1, id) > 0;
	}

	/**
	 * 取消站点屏蔽
	 */
	public static boolean cancelSiteShield(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE SiteBean as d");
		hql.append(" SET  d.status=? ");
		hql.append(" WHERE d.id=? ");
		return commitUpdate(hql.toString(), 0, id) > 0;
	}

	/**
	 * 日记屏蔽
	 */
	public static boolean diaryShield(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE DiaryBean as d");
		hql.append(" SET  d.type=? ");
		hql.append(" WHERE d.id=? ");
		return commitUpdate(hql.toString(), -1, id) > 0;
	}

	/**
	 * 取消日记屏蔽
	 */
	public static boolean cancelDiaryShield(int id) {

		return cancelDiaryRecommend(id);
	}

	/**
	 * 删除日记
	 */
	public static boolean diaryDelete(int id) {
		// 查存关联附件
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM AnnexBean as ab ");
		hql.append(" WHERE ab.diaryID=? ");
		// 删除关联附件
		StringBuffer hql4 = new StringBuffer();
		hql4.append(" DELETE AnnexBean as ab ");
		hql4.append(" WHERE ab.diaryID=?");
		try {
			DiaryBean bean = (DiaryBean) getBean(DiaryBean.class, id);
			List annexList = executeQuery(hql.toString(), -1, -1, bean.getId());
			// 清除磁盘的附件文件
			for (int i = 0; i < annexList.size(); i++) {
				AnnexBean annexBean = (AnnexBean) annexList.get(i);
				System.out.println(annexBean.getDiskPath());
				remove(annexBean.getDiskPath());
			}
			// 清除数据库附件信息
			commitUpdate(hql4.toString(), bean.getId());
			// 清除数据库日记信息
			DiaryDAO.forceDelete(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除磁盘中上传的文件
	 * 
	 * @return
	 */
	protected static boolean remove(String diskpath) throws IOException {
		File f = new File(diskpath);
		if (f != null)
			return f.delete();
		return false;
	}

	/**
	 * 删除站点
	 */
	public static boolean siteDelete(int id) {
		// SiteBean bean = new SiteBean();
		// bean.setId(id);
		// delete(bean);
		return false;
	}

	/**
	 * 推荐日记的总数
	 */
	public static int recommend_diary_count() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT COUNT(*) ");
		hql.append(" FROM DiaryBean as d");
		hql.append(" WHERE  d.type=1 ");
		return Integer.parseInt(uniqueResult(hql.toString()).toString());
	}

	/**
	 * 推荐日记的总数
	 */
	public static List recommend_diary_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM DiaryBean as d");
		hql.append(" WHERE  d.type=1 ");
		return executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);

	}

	/**
	 * 关键字总数
	 */
	public static int tags_count() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT COUNT(*) ");
		hql.append(" FROM TagBean as t GROUP BY t.name");
		return Integer.parseInt(uniqueResult(hql.toString()).toString());
	}

	/**
	 * 关键字列表
	 */
	public static List tags_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT t.name, ");
		hql
				.append(" (SELECT COUNT(*) FROM TagBean as tb WHERE tb.refType=1 AND tb.name =t.name  ), ");
		hql
				.append(" (SELECT COUNT(*) FROM TagBean as tb WHERE tb.refType=2 AND tb.name =t.name  ) ");

		hql.append(" FROM TagBean AS t ");
		hql
				.append(" WHERE t.refType>0 AND t.refId in (select refId from TagBean where refType=1)");
		hql.append(" GROUP BY t.name");
		return executeQueryCacheable("dlog_admin_info", hql.toString(),
				fromIdx, count);
	}

	/**
	 * 热门站点总数,最新更新的站点总数
	 */
	public static int hot_site_count(String bean) {
		StringBuffer hql = new StringBuffer("SELECT COUNT(*) FROM ");
		hql.append(" SiteBean as sb WHERE sb.id in ( select d.site.id from "
				+ bean + " as d GROUP BY d.site.id)");
		return Integer.parseInt(uniqueResult(hql.toString()).toString());
	}

	/**
	 * 月无更新的站点总数
	 */
	public static int not_update_cont(String bean) {
		StringBuffer hql = new StringBuffer("SELECT COUNT(*) FROM ");
		if (bean.equals("DiaryBean"))
			hql
					.append(" SiteBean as sb WHERE sb.id in ( select d.site.id from "
							+ bean
							+ " as d WHERE d.writeTime<? GROUP BY d.site.id)");
		else if (bean.equals("PhotoBean"))
			hql
					.append(" SiteBean as sb WHERE sb.id in ( select d.site.id from "
							+ bean
							+ " as d WHERE d.createTime<? GROUP BY d.site.id)");
		return Integer.parseInt(uniqueResult(hql.toString(), getLastMonth())
				.toString());
	}

	public static List diary_list_files(int fromIdx, int count) {
		return null;
	}

	// ==============================样式布局===================================

	public static List style_name_list() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT count(d.id),d.layout ");
		hql.append(" FROM SiteBean as d ");
		hql.append(" GROUP BY d.layout ");
		hql.append(" ORDER BY 1 desc ");
		return executeQueryCacheable("dlog_admin_info", hql.toString(), -1, -1);
	}

	public static List style_main_list() {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM StyleBean ");
		hql.append("");
		return executeQuery(hql.toString(), -1, -1);
	}

	public static List style_main_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM StyleBean ");
		hql.append("");
		return executeQuery(hql.toString(), fromIdx, count);
	}

	/**
	 * 样式列表 站点访问
	 */
	public static List site_style_list(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM StyleBean AS sb");
		hql.append(" WHERE sb.status=? ");
		hql.append(" ORDER BY sb.level desc  ");
		return executeQuery(hql.toString(), fromIdx, count,
				StyleBean.STYLE_STATUS_OPEN);
	}

	public static StyleBean getStyleByName(String name, String chileName) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM StyleBean as sb");
		hql.append(" WHERE sb.name=? AND sb.childName=? ");
		return (StyleBean) uniqueResult(hql.toString(), name, chileName);
	}

	/**
	 * 推荐样式风格
	 */
	public static boolean styleRecommend(int id) {
		StringBuffer hql_ = new StringBuffer();
		hql_.append(" SELECT max(sb.level)");
		hql_.append(" FROM StyleBean as sb ");
		int level = Integer.parseInt(uniqueResult(hql_.toString()).toString()) + 1;
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE StyleBean as sb ");
		hql.append(" SET sb.level=? ");
		hql.append(" WHERE sb.id=? ");
		return commitUpdate(hql.toString(), level, id) > 0;
	}

	/**
	 * 取消风格的推荐
	 */
	public static boolean cancelRecommendStyle(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE StyleBean as sb ");
		hql.append(" SET sb.level=? ");
		hql.append(" WHERE sb.id=? ");
		return commitUpdate(hql.toString(), 1, id) > 0;
	}

	public static boolean shieldStyle(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE StyleBean as sb ");
		hql.append(" SET sb.status=? ");
		hql.append(" WHERE sb.id=? ");
		return commitUpdate(hql.toString(), StyleBean.STYLE_STATUS_CLOSE, id) > 0;
	}

	public static boolean cancelShieldStyle(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE StyleBean as sb ");
		hql.append(" SET sb.status=? ");
		hql.append(" WHERE sb.id=? ");
		return commitUpdate(hql.toString(), StyleBean.STYLE_STATUS_OPEN, id) > 0;
	}

	public static boolean vipStyle(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE StyleBean as sb ");
		hql.append(" SET sb.type=? ");
		hql.append(" WHERE sb.id=? ");
		return commitUpdate(hql.toString(), StyleBean.STYLE_TYPE_VIP, id) > 0;
	}

	public static boolean cancelVipStyle(int id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE StyleBean as sb ");
		hql.append(" SET sb.type=? ");
		hql.append(" WHERE sb.id=? ");
		return commitUpdate(hql.toString(), StyleBean.STYLE_TYPE_COMMON, id) > 0;
	}

	/**
	 * 读取数据样式表所有信息
	 */
	public static List getStylas() {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM StyleBean as sb ");
		return executeQuery(hql.toString(), -1, -1);
	}

	/**
	 * 删除
	 */
	public static boolean deleteStyle(int id) {
		try {
			StyleBean cbean = new StyleBean();
			cbean.setId(id);
			delete(cbean);
			return true;
		} catch (Exception ex) {

		}
		return false;
	}

	/**
	 * 根据id 获取样式表信息
	 */
	public static StyleBean getStyleById(int id) {
		return (StyleBean) getBean(StyleBean.class, id);
	}

	public static int style_count(String name, String css) {
		int styleCount = 0;
		Object object = AdminDAO.getCache("style_" + "_" + name + "_" + css);
		if (object != null)
			return new Integer(object.toString());
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT count(*) ");
		hql.append(" FROM SiteBean as sb ");
		hql.append(" WHERE sb.layout=? AND sb.css=?");
		styleCount = Integer.parseInt(uniqueResult(hql.toString(), name, css)
				.toString());
		AdminDAO.putCache("style_" + "_" + name + "_" + css, styleCount);
		return styleCount;
	}

	public static int style_count() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT COUNT(*) ");
		hql.append(" FROM StyleBean  ");
		return Integer.parseInt(uniqueResult(hql.toString()).toString());
	}

	public static int site_style_count() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT COUNT(*) ");
		hql.append(" FROM StyleBean AS sb ");
		hql.append(" WHERE  sb.status=0 ");
		return Integer.parseInt(uniqueResult(hql.toString()).toString());
	}

	public static List getMainStyle() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT sb.name ");
		hql.append(" FROM StyleBean as sb ");
		hql.append(" GROUP BY sb.name ");
		return executeQuery(hql.toString(), -1, -1);
	}

	/**
	 * 添加样式布局
	 */
	public static boolean doCreateStyle(AdminForm form) {
		Date date = new Date();
		StyleBean bean = new StyleBean();
		bean.setName(form.getName());
		bean.setChildName(form.getChildName());
		bean.setCss(form.getCss());
		bean.setPreview_l_image(form.getPreview_l_image());
		bean.setPreview_s_image(form.getPreview_s_image());
		bean.setExplain(form.getExplain());
		bean.setStatus(form.getStatus());
		bean.setType(form.getType());
		bean.setCreateTime(date);
		bean.setLevel(1);
		save(bean);
		return true;
	}

	/**
	 * 修改样式布局
	 */
	public static boolean doEditStyle(AdminForm form) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE StyleBean AS sb");
		hql.append(" SET sb.name=? , sb.childName=? , sb.css=? ,");
		hql
				.append(" sb.preview_l_image=? , sb.preview_s_image=? , sb.explain=? ,");
		hql.append(" sb.type=? , sb.status=? ");
		hql.append(" WHERE sb.id=? ");
		int row = commitUpdate(hql.toString(), form.getName(), form
				.getChildName(), form.getCss(), form.getPreview_l_image(), form
				.getPreview_s_image(), form.getExplain(), form.getType(), form
				.getStatus(), form.getId());

		return row > 0;
	}

	/** 数据库样式信息是否存在 */
	public static StyleBean styleIsPresence(String name, String css) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM StyleBean as sb ");
		hql.append(" WHERE sb.name=? AND sb.css=?");
		return (StyleBean) uniqueResult(hql.toString(), name, css);
	}

	public static List style_rank() {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM StyleBean as sb ");
		hql.append(" ORDER BY sb.count desc  ");
		return executeQuery(hql.toString(), -1, -1);
	}

	public static boolean setStyleCount(int id, int count) {

		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE StyleBean AS sb");
		hql.append(" SET sb.count=? ");
		hql.append(" WHERE sb.id=? ");
		int row = commitUpdate(hql.toString(), count, id);
		return true;
	}

	// ======================权限====================
	public static int managerCount() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT COUNT(*) ");
		hql.append(" FROM UserBean as ub ");
		hql.append(" WHERE ub.popedom <> ? ");
		int count = Integer.parseInt(uniqueResult(hql.toString(),
				UserBean.POPEDOM_COMMON).toString());
		return count;
	}

	public static List managerListint(int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM UserBean as ub ");
		hql.append(" WHERE ub.role <> ? ");
		return executeQuery(hql.toString(), fromIdx, count,
				UserBean.ROLE_COMMON);
	}

	public static int userCount(String name) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT COUNT(*) ");
		hql.append(" FROM UserBean as ub ");
		hql.append(" WHERE ub.role =? AND ub.name like ? ");
		int count = Integer.parseInt(uniqueResult(hql.toString(),
				UserBean.ROLE_COMMON, "%" + name + "%").toString());
		return count;
	}

	public static List userList(String name, int fromIdx, int count) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM UserBean as ub ");
		hql.append(" WHERE ub.role = ? AND ub.name like ? ");
		return executeQuery(hql.toString(), fromIdx, count,
				UserBean.ROLE_COMMON, "%" + name + "%");
	}

	public static boolean CancelPop(int id) {
		UserBean bean = (UserBean) DAO.getBean(UserBean.class, id);
		bean.setPopedom(bean.POPEDOM_COMMON);
		if (bean.getRole() != bean.ROLE_VIP)
			bean.setRole(bean.ROLE_COMMON);
		if (bean == null)
			return false;
		try {
			DAO.update(bean);
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	public static boolean UpdatePop(int id, int popedom, int role) {
		UserBean bean = (UserBean) DAO.getBean(UserBean.class, id);
		bean.setRole(role);
		bean.setPopedom(popedom);
		if (bean == null)
			return false;
		try {
			DAO.update(bean);
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	/**
	 * 写缓存
	 */
	public static void putCache(String key, Object obj) {
		DLOG_CacheManager.putObjectCached("query.diary_search", key, obj);
	}

	/**
	 * 读缓存
	 */
	public static Object getCache(String key) {
		return DLOG_CacheManager.getObjectCached("query.diary_search", key);

	}

	/**
	 * 移除缓存
	 */
	public static void moveCache(String key) {
		DLOG_CacheManager.evictObjectCached("query.diary_search", key);
	}

	public static void main(String[] args) {
		Calendar day = Calendar.getInstance();
		AdminDAO adminDAO = new AdminDAO();
		System.out.println(adminDAO.getStatDate(day));
	}

}