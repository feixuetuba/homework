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
	 * ���շ�����
	 */
	public int today_diary_content() {
		return AdminDAO.today_diary_content();
	}

	/**
	 * ���շ�����
	 */
	public int today_reply() {
		return AdminDAO.today_reply();
	}

	/**
	 * �����û�ע��
	 */
	public int today_create_user() {
		return AdminDAO.today_create_user();
	}

	// =================վ��========================
	/**
	 * վ������
	 */
	public int site_count() {
		return AdminDAO.siteCount();
	}

	/**
	 * ����վ������[�ռ�]
	 */
	public int diary_hot_site_count() {
		return AdminDAO.hot_site_count(DIARY);
	}

	/**
	 * ����վ������[��Ƭ]
	 */
	public int photo_hot_site_count() {
		return AdminDAO.hot_site_count(PHOTO);
	}

	/**
	 * ���¸���վ������[�ռ�]
	 */
	public int new_update_diary_count() {
		return AdminDAO.hot_site_count(DIARY);
	}

	/**
	 * ���¸���վ������[��Ƭ]
	 */
	public int new_update_photo_count() {
		return AdminDAO.hot_site_count(PHOTO);
	}

	/**
	 * ���޸��µ�վ������[�ռ�]
	 */
	public int diary_not_update_cont() {
		return AdminDAO.not_update_cont(DIARY);
	}

	/**
	 * ���޸��µ�վ������[��Ƭ]
	 */
	public int photo_not_update_cont() {
		return AdminDAO.not_update_cont(PHOTO);
	}

	/**
	 * ����վ������[����]
	 */
	public int new_site_count() {
		return AdminDAO.new_site_count();
	}

	/**
	 * ����վ���б�
	 */
	public List new_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_site_list(fromIdx, count);
	}

	/**
	 * ����վ���б�[�ռ�]
	 */
	public List hot_diary_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.hot_site_list(DIARY, fromIdx, count);
	}

	/**
	 * ����վ���б�[��Ƭ]
	 */
	public List hot_photo_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.hot_site_list(PHOTO, fromIdx, count);
	}

	/**
	 * ����վ���б�
	 */
	public List hot_site_list(String Bean, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.hot_site_list(Bean, fromIdx, count);
	}

	/**
	 * ������µ�վ��[�ռ�]
	 */
	public List new_update_diary_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_update_list(DIARY, fromIdx, count);
	}

	/**
	 * ������µ�վ��[��Ƭ]
	 */
	public List new_update_photo_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_update_list(PHOTO, fromIdx, count);
	}

	/**
	 * ��ȡĳ����վ�ķ���ͳ����Ϣ
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
	 * ������µ�վ��
	 */
	public List new_update_list(String Bean, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_update_list(Bean, fromIdx, count);
	}

	/**
	 * ���޸���[�ռ�]
	 */
	public List diary_not_update_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.diary_not_update_list(fromIdx, count);
	}

	/**
	 * ���޸���[��Ƭ]
	 */
	public List photo_not_update_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.photo_not_update_list(fromIdx, count);
	}

	/**
	 * �Ƽ�վ���б�
	 */
	public List site_recommend(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.site_recommend(fromIdx, count);
	}

	/**
	 * �µ��[����]
	 */
	public List month_hot_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return Encapsulation(AdminDAO.visited_hot_list(MONTH, fromIdx, count));
	}

	/**
	 * �ܵ��[����]
	 */
	public List week_hot_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return Encapsulation(AdminDAO.visited_hot_list(WEEK, fromIdx, count));
	}

	/**
	 * �յ��[����]
	 */
	public List day_hot_list(int page, int count) {
		int fromIdx = (page - 1) * count;

		return Encapsulation(AdminDAO.visited_hot_list(DAY, fromIdx, count));
	}

	/**
	 * ȫ�����
	 */
	public List full_visited_hot_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return Encapsulation(AdminDAO.visited_hot_list(FULL, fromIdx, count));
	}

	/**
	 * δ��ͨ����
	 */
	public List not_create_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.not_create_site_list(fromIdx, count);
	}

	/**
	 * �ѿ�ͨ����
	 */
	public List create_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.create_site_list(fromIdx, count);
	}

	/**
	 * ���޵�¼
	 */
	public List month_not_login_site_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.month_not_login_site_list(fromIdx, count);
	}

	// =====================�ռ�============================
	public int diary_count() {
		return AdminDAO.diary_count();
	}

	/**
	 * �����ռ�
	 */
	public List new_diary_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_diary_list(fromIdx, count);
	}

	/**
	 * �����ռ�
	 */
	public List hot_diary_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.hot_diary_list(fromIdx, count);
	}

	/**
	 * �ռ���������
	 */
	public List new_diary_reply_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.new_diary_reply_list(fromIdx, count);
	}

	/**
	 * �����ռ�
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
				return "�˲�ѯ����Ч,����\"��ʼ\"��ť���²�ѯ";
			return "";
		} catch (Exception ex) {

		}

		return "�˲�ѯ����Ч,����\"��ʼ\"��ť���²�ѯ";
	}

	/**
	 * �ռǻظ�����
	 */
	public List diary_reply(int order, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.diary_reply(order, fromIdx, count);

	}

	/**
	 * �ռǻظ�����
	 */
	public List diary_view(int order, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.diary_view(order, fromIdx, count);
	}

	/**
	 * �Ƽ��ռǵ�����
	 */
	public int recommend_diary_count() {

		return AdminDAO.recommend_diary_count();
	}

	/**
	 * �Ƽ��ռ��˱�
	 */
	public List recommend_diary_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.recommend_diary_list(fromIdx, count);
	}

	/**
	 * �ؼ�������
	 */
	public int tags_count() {

		return AdminDAO.tags_count();
	}

	/**
	 * �ؼ����б�
	 */
	public List tags_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.tags_list(fromIdx, count);
	}

	/**
	 * �г��ռ��ϴ�����Ƭ
	 */
	public List diary_list_files(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.diary_list_files(fromIdx, count);
	}

	// =================���==================================

	/**
	 * վ����� ��������[���ݿ�]
	 */
	public int site_style_count() {
		return AdminDAO.site_style_count();
	}

	/**
	 * վ����� ��������б�[���ݿ�]
	 */
	public List site_style_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.site_style_list(fromIdx, count);
	}

	/**
	 * ��������[���ݿ�]
	 */
	public int style_count() {
		return AdminDAO.style_count();
	}

	/**
	 * ��������б�
	 */
	public List style_name_list() {
		return AdminDAO.style_name_list();
	}

	/**
	 * ����б�
	 */
	public List style_main_list(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.style_main_list(fromIdx, count);
	}

	/**
	 * vip����б�
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
	 * �����ʽ��ʾ
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
	 * �����������Ŀ¼�ṹ
	 */
	public List getIdToFileList(int id) {
		StyleBean bean = AdminDAO.getStyleById(id);
		return DLOG_LayoutManager.getFileList(bean.getName(), "");
	}

	/**
	 * ��ȡ����ָ��·���������ļ���
	 */
	public List getNameToFileList() {
		return DLOG_LayoutManager.getFileList_name();
	}

	/** ��ȡ����ָ��·��������.css�ļ� */
	public List getCssToFileList(String name) {
		return DLOG_LayoutManager.getFileList_css(name);
	}

	/** ��ʽ�����б� */
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

	// ===============Ȩ��===============

	/**
	 * Ȩ��:�ɶ�[��ͨ�û�,VIP,Ѳ����,ϵͳ����]���� ��� �޸� ɾ��
	 */
	public boolean popIsSuper(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_SUPER);
	}

	/**
	 * Ȩ��:�޸� ɾ��
	 */
	public boolean popIsOperate(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_OPERATE);
	}

	/**
	 * Ȩ��:�Ƽ� ����
	 */
	public boolean popIsManage(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_MANAGE);
	}

	/**
	 * Ȩ��:�����б�
	 */
	public boolean popIsVisit(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_VISIT);
	}

	/**
	 * Ȩ��:�Ƽ� ���� ɾ�� �޸�
	 */
	public boolean popIsAll(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getPopedom() == bean.POPEDOM_ALL);
	}

	/**
	 * ��ɫ:����Ա
	 */
	public boolean roleIsAdmin(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getRole() == bean.ROLE_ADMINISTRATOR);
	}

	/**
	 * ��ɫ:Ѳ����
	 */
	public boolean roleIsInspector(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getRole() == bean.ROLE_INSPECTOR);
	}

	/**
	 * ��ɫ:����
	 */
	public boolean roleIsStyle(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getRole() == bean.ROLE_STYLE);
	}

	/**
	 * ��ɫ:vip
	 */
	public boolean roleIsVip(UserBean bean) {
		if (bean == null)
			return false;
		return (bean.getRole() == bean.ROLE_VIP);
	}

	/**
	 * ����[��Ȩ�޵�]
	 */
	public int managerCount() {
		return AdminDAO.managerCount();
	}

	/**
	 * �б�[��Ȩ�޵�]
	 */
	public List managerList(int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.managerListint(fromIdx, count);
	}

	/**
	 * �û���[û��Ȩ�޵�]
	 */
	public int userCount(String name) {
		return AdminDAO.userCount(name);
	}

	/**
	 * �û��б�[û��Ȩ�޵�]
	 */
	public List userList(String name, int page, int count) {
		int fromIdx = (page - 1) * count;
		return AdminDAO.userList(name, fromIdx, count);
	}

	/**
	 * �û��Ƿ���isAdministrators�ļ�������
	 * 
	 * @param user
	 * @return
	 */
	public boolean isAdministrators(SessionUserObject user) {
		UserBean bean = UserDAO.getUserByID(user.getId());
		// �Ƿ���isAdministrators�����ó�������Ա��id
		if (UserLoginManager.isSuperior(user)) {
			// �鿴���ݿ��û���Ϣ ��ɫ
			if (bean.getRole() != bean.ROLE_ADMINISTRATOR) {
				bean.setRole(bean.ROLE_ADMINISTRATOR);
			}
			// �鿴Ȩ�� POPEDOM_SUPER
			if (bean.getPopedom() != bean.POPEDOM_SUPER) {
				bean.setPopedom(bean.POPEDOM_SUPER);
			}
			UserDAO.updateUser(bean);
			return true;
		} else {
			// isAdministrators������û��ID
			// ��ԭ�����ù���ID�û�Ȩ�޸�Ϊ����ԱȨ��
			if (bean.getPopedom() == bean.POPEDOM_SUPER) {
				bean.setPopedom(bean.POPEDOM_ALL);
				UserDAO.updateUser(bean);
				return true;
			}
			//�жϽ�ɫ
			if(bean.getRole()>0)
			{
				return true;
			}
		}
		return false;
	}

	// ======================����=============================
	/**
	 * ҳ������
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
	 * ҳ����������
	 * 
	 * @param int
	 *            page ��ǰҳ
	 * @param int
	 *            count ��ҳ��
	 * @param int
	 *            max ��ʾҳ��
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
	 * ��װ����������
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
		String friendlyName; // վ��������
		String uniqueName; // վ��Ӣ����
		String ownerName; // վ��
		int ownerId;// վ��id
		int uvThisMonth; // �·���
		int uvThisWeek;// �ܷ���
		int uvThisDay;// �շ���
		int uvThisFull;// ȫ������
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
