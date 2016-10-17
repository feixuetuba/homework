package com.liusoft.dlog4j.velocity;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.sql.Update;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.liusoft.dlog4j.DLOGUserManager;
import com.liusoft.dlog4j.DLOG_CacheManager;
import com.liusoft.dlog4j.DLOG_LayoutManager;
import com.liusoft.dlog4j.SessionUserObject;
import com.liusoft.dlog4j.UserLoginManager;
import com.liusoft.dlog4j.base.DlogStatInfo;
import com.liusoft.dlog4j.base.LayoutInfo;
import com.liusoft.dlog4j.base.SiteStatInfo;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.StyleBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.dao.AdminDAO;
import com.liusoft.dlog4j.dao.DAO;
import com.liusoft.dlog4j.dao.DlogDAO;
import com.liusoft.dlog4j.dao.SiteDAO;
import com.liusoft.dlog4j.dao.UserDAO;
import com.liusoft.dlog4j.dao.VisitStatDAO;
//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class DLOG_Admin_velocityTool {
	private final static int DAY = 1;
	private final static int WEEK = 2;
	private final static int MONTH = 3;
	private final static int FULL = 4;
	public final String DIARY = "DiaryBean";
	public final String PHOTO = "PhotoBean";

	/**
	 * 
	 */
	public UserBean getManager(SessionUserObject suo) {
		if (suo == null)
			return null;
		UserBean bean = (UserBean) AdminDAO.getCache(suo.getId() + "_manager");
		if (bean == null) {
			bean = AdminDAO.getManager(suo.getId());
			AdminDAO.putCache(suo.getId() + "_manager", bean);
		}
		return bean;
	}

	/**
	 * 今日发贴数
	 */
	public int today_diary_content() {
		return AdminDAO.today_diary_content();
	}

	/**
	 * 今日发贴数
	 */
	public int today_reply() {
		return AdminDAO.today_reply();
	}

	/**
	 * 今日用户注册
	 */
	public int today_create_user() {
		return AdminDAO.today_create_user();
	}

	// =================站点========================
	/**
	 * 站点总数
	 */
	public int site_count() {
		return AdminDAO.siteCount();
	}

	/**
	 * 热门站点总数[日记]
	 */
	public int diary_hot_site_count() {
		return AdminDAO.hot_site_count(DIARY);
	}

	/**
	 * 热门站点总数[照片]
	 */
	public int photo_hot_site_count() {
		return AdminDAO.hot_site_count(PHOTO);
	}

	/**
	 * 最新更新站点总数[日记]
	 */
	public int new_update_diary_count() {
		return AdminDAO.hot_site_count(DIARY);
	}

	/**
	 * 最新更新站点总数[照片]
	 */
	public int new_update_photo_count() {
		return AdminDAO.hot_site_count(PHOTO);
	}

	/**
	 * 月无更新的站点总数[日记]
	 */
	public int diary_not_update_cont() {
		return AdminDAO.not_update_cont(DIARY);
	}

	/**
	 * 月无更新的站点总数[照片]
	 */
	public int photo_not_update_cont() {
		return AdminDAO.not_update_cont(PHOTO);
	}

	/**
	 * 最新站点总数[当天]
	 */
	public int new_site_count() {
		return AdminDAO.new_site_count();
	}

	/**
	 * 最新站点列表
	 */
	public List new_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_site_list(fromIdx, count);
	}

	/**
	 * 热门站点列表[日记]
	 */
	public List hot_diary_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.hot_site_list(DIARY, fromIdx, count);
	}

	/**
	 * 热门站点列表[照片]
	 */
	public List hot_photo_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.hot_site_list(PHOTO, fromIdx, count);
	}

	/**
	 * 热门站点列表
	 */
	public List hot_site_list(String Bean, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.hot_site_list(Bean, fromIdx, count);
	}

	/**
	 * 最近更新的站点[日记]
	 */
	public List new_update_diary_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_update_list(DIARY, fromIdx, count);
	}

	/**
	 * 最近更新的站点[照片]
	 */
	public List new_update_photo_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_update_list(PHOTO, fromIdx, count);
	}

	/**
	 * 获取某个网站的访问统计信息
	 */
	public SiteStatInfo get_site_stat_info(int sid) {
		SiteStatInfo ssi = new SiteStatInfo();
		ssi.setSite(sid);
		ssi.setUvThisMonth(AdminDAO.getUVThisMonth(sid));
		ssi.setUvThisWeek(AdminDAO.getUVThisWeek(sid));
		ssi.setUvToday(AdminDAO.getUVToday(sid));
		ssi.setUvTotal(AdminDAO.getUVTotal(sid));
		return ssi;
	}

	/**
	 * 最近更新的站点
	 */
	public List new_update_list(String Bean, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_update_list(Bean, fromIdx, count);
	}

	/**
	 * 月无更新[日记]
	 */
	public List diary_not_update_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.diary_not_update_list(fromIdx, count);
	}

	/**
	 * 月无更新[照片]
	 */
	public List photo_not_update_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.photo_not_update_list(fromIdx, count);
	}

	/**
	 * 推荐站点列表
	 */
	public List site_recommend(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.site_recommend(fromIdx, count);
	}

	/**
	 * 月点击[访问]
	 */
	public List month_hot_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return Encapsulation(AdminDAO.visited_hot_list(MONTH, fromIdx, count));
	}

	/**
	 * 周点击[访问]
	 */
	public List week_hot_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return Encapsulation(AdminDAO.visited_hot_list(WEEK, fromIdx, count));
	}

	/**
	 * 日点击[访问]
	 */
	public List day_hot_list(int page, int count) {
		int fromIdx = (page - 1) * count;

		return Encapsulation(AdminDAO.visited_hot_list(DAY, fromIdx, count));
	}

	/**
	 * 全部点击
	 */
	public List full_visited_hot_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return Encapsulation(AdminDAO.visited_hot_list(FULL, fromIdx, count));
	}

	/**
	 * 未开通网记
	 */
	public List not_create_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.not_create_site_list(fromIdx, count);
	}

	/**
	 * 已开通网记
	 */
	public List create_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.create_site_list(fromIdx, count);
	}

	/**
	 * 月无登录
	 */
	public List month_not_login_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.month_not_login_site_list(fromIdx, count);
	}

	// =====================日记============================
	public int diary_count() {
		return AdminDAO.diary_count();
	}

	/**
	 * 最新日记
	 */
	public List new_diary_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_diary_list(fromIdx, count);
	}

	/**
	 * 热门日记
	 */
	public List hot_diary_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.hot_diary_list(fromIdx, count);
	}

	/**
	 * 日记最新评论
	 */
	public List new_diary_reply_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_diary_reply_list(fromIdx, count);
	}

	/**
	 * 搜索日记
	 */
	public List search_diary(String key, int page, int count) {
		int fromIdx = (page - 1) * count;
		String hql;
		Object value[];
		try {
			hql = (String) AdminDAO.getCache(key + "_sql");
			value = (Object[]) AdminDAO.getCache(key + "_value");
			AdminDAO.putCache(key, hql);
			AdminDAO.putCache(key, value);
			return AdminDAO.search_diary(key, fromIdx, count);
		} catch (Exception ex) {

		}

		return null;
	}

	public String get_cache_query(String key) {
		String hql;
		Object value[];
		try {
			hql = (String) AdminDAO.getCache(key + "_sql");
			value = (Object[]) AdminDAO.getCache(key + "_value");
			if (hql == null && value == null)
				return "此查询已无效,请点击\"开始\"按钮重新查询";
			return "";
		} catch (Exception ex) {

		}

		return "此查询已无效,请点击\"开始\"按钮重新查询";
	}

	/**
	 * 日记回复排行
	 */
	public List diary_reply(int order, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.diary_reply(order, fromIdx, count);

	}

	/**
	 * 日记回复排行
	 */
	public List diary_view(int order, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.diary_view(order, fromIdx, count);
	}

	/**
	 * 推荐日记的总数
	 */
	public int recommend_diary_count() {

		return AdminDAO.recommend_diary_count();
	}

	/**
	 * 推荐日记了表
	 */
	public List recommend_diary_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.recommend_diary_list(fromIdx, count);
	}

	/**
	 * 关键字总数
	 */
	public int tags_count() {

		return AdminDAO.tags_count();
	}

	/**
	 * 关键字列表
	 */
	public List tags_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.tags_list(fromIdx, count);
	}

	/**
	 * 列出日记上传的照片
	 */
	public List diary_list_files(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.diary_list_files(fromIdx, count);
	}

	// =================风格==================================

	/**
	 * 站点访问 风格的总数[数据库]
	 */
	public int site_style_count() {
		return AdminDAO.site_style_count();
	}

	/**
	 * 站点访问 风格排行列表[数据库]
	 */
	public List site_style_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.site_style_list(fromIdx, count);
	}

	/**
	 * 风格的总数[数据库]
	 */
	public int style_count() {
		return AdminDAO.style_count();
	}

	/**
	 * 风格排行列表
	 */
	public List style_name_list() {
		return AdminDAO.style_name_list();
	}

	/**
	 * 风格列表
	 */
	public List style_main_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.style_main_list(fromIdx, count);
	}

	/**
	 * vip风格列表
	 */
	public List style_vip_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.style_main_list(fromIdx, count);
	}

	public List getMainStyle() {
		return AdminDAO.getMainStyle();
	}

	public StyleBean getStyleById(int id) {
		return AdminDAO.getStyleById(id);
	}

	/**
	 * 添加样式提示
	 */
	public boolean isNewCreate(Date createDate) {
		Date date = new Date();
		createDate.setDate(date.getDate() + 1);
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
		long temp1 = Long.parseLong(time.format(createDate).toString());
		long temp2 = Long.parseLong(time.format(date).toString());
		return temp1 > temp2;
	}

	/**
	 * 读物磁盘整个目录结构
	 */
	public List getIdToFileList(int id) {
		StyleBean bean = AdminDAO.getStyleById(id);
		return DLOG_LayoutManager.getFileList(bean.getName(), "");
	}

	/**
	 * 读取磁盘指定路径下所有文件加
	 */
	public List getNameToFileList() {
		return DLOG_LayoutManager.getFileList_name();
	}

	/** 读取磁盘指定路径下所有.css文件 */
	public List getCssToFileList(String name) {
		return DLOG_LayoutManager.getFileList_css(name);
	}

	/** 样式排行列表 */
	public List styleRank() {
		List styles = AdminDAO.getStylas();
		StyleBean bean = null;
		for (int i = 0; i < styles.size(); i++) {
			bean = (StyleBean) styles.get(i);
			int count = AdminDAO.style_count(bean.getName(), bean.getCss());
			AdminDAO.setStyleCount(bean.getId(), count);
		}
		return AdminDAO.style_rank();
	}

	// ===============权限===============

	/**
	 * 权限:可对[普通用户,VIP,巡视者,系统管理]操作 添加 修改 删除
	 */
	public boolean popIsSuper(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_SUPER);
	}

	/**
	 * 权限:修改 删除
	 */
	public boolean popIsOperate(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_OPERATE);
	}

	/**
	 * 权限:推荐 屏蔽
	 */
	public boolean popIsManage(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_MANAGE);
	}

	/**
	 * 权限:访问列表
	 */
	public boolean popIsVisit(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_VISIT);
	}

	/**
	 * 权限:推荐 屏蔽 删除 修改
	 */
	public boolean popIsAll(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_ALL);
	}

	/**
	 * 角色:管理员
	 */
	public boolean roleIsAdmin(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getRole() == bean.ROLE_ADMINISTRATOR);
	}

	/**
	 * 角色:巡视者
	 */
	public boolean roleIsInspector(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getRole() == bean.ROLE_INSPECTOR);
	}

	/**
	 * 角色:美工
	 */
	public boolean roleIsStyle(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getRole() == bean.ROLE_STYLE);
	}

	/**
	 * 角色:vip
	 */
	public boolean roleIsVip(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getRole() == bean.ROLE_VIP);
	}

	/**
	 * 总数[有权限的]
	 */
	public int managerCount() {
		return AdminDAO.managerCount();
	}

	/**
	 * 列表[有权限的]
	 */
	public List managerList(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.managerListint(fromIdx, count);
	}

	/**
	 * 用户数[没有权限的]
	 */
	public int userCount(String name) {
		return AdminDAO.userCount(name);
	}

	/**
	 * 用户列表[没有权限的]
	 */
	public List userList(String name, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.userList(name, fromIdx, count);
	}

	/**
	 * 用户是否在isAdministrators文件中描述
	 * 
	 * @param user
	 * @return
	 */
	public boolean isAdministrators(SessionUserObject user) {
		UserBean bean = UserDAO.getUserByID(user.getId());
		// 是否在isAdministrators中配置超级管理员的id
		if (UserLoginManager.isSuperior(user)) {
			// 查看数据库用户信息 角色
			if (bean.getRole() != bean.ROLE_ADMINISTRATOR) {
				bean.setRole(bean.ROLE_ADMINISTRATOR);
			}
			// 查看权限 POPEDOM_SUPER
			if (bean.getPopedom() != bean.POPEDOM_SUPER) {
				bean.setPopedom(bean.POPEDOM_SUPER);
			}
			UserDAO.updateUser(bean);
			return true;
		} else {
			// isAdministrators配置中没有ID
			// 将原来配置过的ID用户权限改为管理员权限
			if (bean.getPopedom() == bean.POPEDOM_SUPER) {
				bean.setPopedom(bean.POPEDOM_ALL);
				UserDAO.updateUser(bean);
				return true;
			}
			//判断角色
			if(bean.getRole()>0)
			{
				return true;
			}
		}
		return false;
	}

	// ======================其他=============================
	/**
	 * 页面索引
	 */
	public List index_page(int page, int count) {
		List index_list = new ArrayList();
		int index = 0;
		if (page >= 4)
			index = page - 4;
		if (page >= count - 4 && (count - 7) > 0)
			index = count - 7;
		else
			index = 0;
		for (int i = 0; i < 7; i++) {
			index += 1;
			if (index > count)
				return index_list;
			index_list.add(i, index);
		}
		return index_list;
	}

	/**
	 * 页面索引长度
	 * 
	 * @param int
	 *            page 当前页
	 * @param int
	 *            count 总页数
	 * @param int
	 *            max 显示页数
	 * @return List
	 */
	public List index_page(int page, int count, int max) {
		List index_list = new ArrayList();
		int dot;
		if (max % 2 == 1)
			dot = (max - 1) / 2;
		else
			dot = max / 2;

		int index = 0;
		if (page >= dot)
			index = page - dot;

		if (page >= count - dot && (count - max) > 0)
			index = count - max;
		else
			index = 0;
		for (int i = 0; i < max; i++) {
			index += 1;
			if (index <= count)
				index_list.add(i, index);
		}
		return index_list;
	}

	// ==================================================
	/**
	 * 封装访问排行类
	 */
	public List Encapsulation(List objs) {
		List<SiteStatCount> sites = new ArrayList<SiteStatCount>();
		for (int i = 0; i < objs.size(); i++) {
			Object obj[] = (Object[]) objs.get(i);
			int name1 = ((Number) obj[0]).intValue();
			String name2 = (String) obj[1];
			String name3 = (String) obj[2];
			String name4 = (String) obj[3];
			int name5 = 0;
			int name6 = 0;
			int name7 = 0;
			int name8 = 0;
			int name9 = 0;
			int name10 = 0;
			int name11 = 0;
			if (obj[4] != null)
				name5 = ((Number) obj[4]).intValue();
			if (obj[5] != null)
				name6 = ((Number) obj[5]).intValue();
			if (obj[6] != null)
				name7 = ((Number) obj[6]).intValue();
			if (obj[7] != null)
				name8 = ((Number) obj[7]).intValue();
			if (obj[8] != null)
				name9 = ((Number) obj[8]).intValue();
			if (obj[9] != null)
				name10 = ((Number) obj[9]).intValue();
			if (obj[10] != null)
				name11 = ((Number) obj[10]).intValue();

			SiteStatCount siteStatCount = new SiteStatCount();
			siteStatCount.setId(name1);
			siteStatCount.setUniqueName(name2);
			siteStatCount.setFriendlyName(name3);
			siteStatCount.setOwnerName(name4);
			siteStatCount.setOwnerId(name5);
			siteStatCount.setUvThisMonth(name6);
			siteStatCount.setUvThisWeek(name7);
			siteStatCount.setUvThisDay(name8);
			siteStatCount.setUvThisFull(name9);
			siteStatCount.setLevel(name10);
			siteStatCount.setStatus(name11);
			sites.add(siteStatCount);
		}

		return sites;
	}

	public class SiteStatCount {
		int id; // id
		String friendlyName; // 站点中文名
		String uniqueName; // 站点英文名
		String ownerName; // 站长
		int ownerId;// 站长id
		int uvThisMonth; // 月访问
		int uvThisWeek;// 周访问
		int uvThisDay;// 日访问
		int uvThisFull;// 全部访问
		int level;
		int status;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getFriendlyName() {
			return friendlyName;
		}

		public void setFriendlyName(String friendlyName) {
			this.friendlyName = friendlyName;
		}

		public String getUniqueName() {
			return uniqueName;
		}

		public void setUniqueName(String uniqueName) {
			this.uniqueName = uniqueName;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}

		public int getUvThisMonth() {
			return uvThisMonth;
		}

		public void setUvThisMonth(int uvThisMonth) {
			this.uvThisMonth = uvThisMonth;
		}

		public int getUvThisWeek() {
			return uvThisWeek;
		}

		public void setUvThisWeek(int uvThisWeek) {
			this.uvThisWeek = uvThisWeek;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getUvThisDay() {
			return uvThisDay;
		}

		public void setUvThisDay(int uvThisToday) {
			this.uvThisDay = uvThisToday;
		}

		public int getUvThisFull() {
			return uvThisFull;
		}

		public void setUvThisFull(int uvThisFull) {
			this.uvThisFull = uvThisFull;
		}

		public int getOwnerId() {
			return ownerId;
		}

		public void setOwnerId(int ownerId) {
			this.ownerId = ownerId;
		}
	}

	public static void main(String[] s) {
		DLOG_Admin_velocityTool tool = new DLOG_Admin_velocityTool();
		List list = tool.index_page(4, 6, 4);
		for (int i = 0; i < list.size(); i++) {
			System.out.print(" " + list.get(i) + " ");
		}

	}
}
