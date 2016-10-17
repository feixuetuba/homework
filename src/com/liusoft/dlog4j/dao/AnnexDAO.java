package com.liusoft.dlog4j.dao;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.hibernate.hql.ast.HqlASTFactory;

import com.liusoft.dlog4j.beans.AnnexBean;

public class AnnexDAO extends DAO {

	/**
	 * 附件列表
	 */
	public static List nonceAnnex(int id, long vid) {
		String hql = "FROM AnnexBean WHERE userid=? AND valiDate=?";
		return findAll(hql, id, vid);
	}

	/**
	 * 附件列表
	 */
	public static List nonceAnnex(int diaryID) {
		String hql = "FROM AnnexBean WHERE diaryID=?";
		return findAll(hql, diaryID);
	}

	/**
	 * 将附件关联到日志上
	 */
	public static void setDiaryID(int diaryID, String uid, String vid) {
		// TODO Auto-generated method stub
		try {
			// 附件关联日记信息
			String hql = "UPDATE AnnexBean SET diaryID =? WHERE userid=? AND valiDate=?";
			// 日记关联附件信息
			String hql2 = "UPDATE DiaryBean SET annex =1 WHERE diary_id=?";
			commitUpdate(hql, diaryID, Integer.parseInt(uid), Long
					.parseLong(vid));
			commitUpdate(hql2, diaryID);
			//获取未关联的附件信息
			StringBuffer hql3 = new StringBuffer();
			hql3.append(" FROM AnnexBean as ab ");
			hql3.append(" WHERE ab.diaryID=? AND ab.userid=?");
			List annexList = executeQuery(hql3.toString(), -1, -1, 0, Integer
					.parseInt(uid));
			//清除磁盘未关联的附件文件
			for (int i = 0; i < annexList.size(); i++) {
				AnnexBean annexBean = (AnnexBean) annexList.get(i);
				System.out.println(annexBean.getDiskPath());
				remove(annexBean.getDiskPath());
			}
			//清除数据库未关联的附件信息
			StringBuffer hql4 = new StringBuffer();
			hql4.append(" DELETE AnnexBean as ab ");
			hql4.append(" WHERE ab.diaryID=? AND ab.userid=?");
			commitUpdate(hql4.toString(), 0, Integer.parseInt(uid));
		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	/**
	 * 删除上传的文件
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
	 * 修改下载统计
	 */
	public static void statDownload(int anID) {
		AnnexBean bean = (AnnexBean) getBean(AnnexBean.class, anID);
		if(bean!=null)
		{
			bean.setDownloadCount(bean.getDownloadCount() + 1);
			bean.setLastDownload(new Date());
			flush();
		}
	}

	/**
	 * 根据日记 与 用户 获取AnnexBean
	 */
	public static AnnexBean findById(int anID) {
		try {
			String hql = "FROM AnnexBean WHERE id=? ";
			List list = findAll(hql, anID);
			if (list.size() > 0)
				return (AnnexBean) list.get(0);
			return null;
		} catch (Exception ex) {

			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据日记 与 用户 获取AnnexBean
	 */
	public static AnnexBean findByIdAndValidate(int anID, long vid) {
		try {
			String hql = "FROM AnnexBean WHERE id=? AND valiDate=?";
			List list = findAll(hql, anID, vid);
			if (list.size() > 0)
				return (AnnexBean) list.get(0);
			return null;
		} catch (Exception ex) {

			ex.printStackTrace();
			return null;
		}

	}

	public static Object getAnnexBean(Class beanClass, int id) {
		return getBean(beanClass, id);
	}

	public static long annex_validate(int diaryID) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select max(d.valiDate) ");
		hql.append(" from  AnnexBean as d");
		hql.append(" where  d.diaryID=? ");
		hql.append(" GROUP BY d.diaryID ");
		try {
			long validate = Long
					.parseLong(uniqueResult(hql.toString(), diaryID).toString());
			return validate;
		} catch (Exception ex) {

		}
		return 0;
	}

	public static int getdiaryAnnexCount(long validate, int userid) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select count(*) ");
		hql.append(" FROM AnnexBean  ");
		hql.append(" WHERE userid=? AND valiDate=? ");
		return Integer.parseInt(uniqueResult(hql.toString(), userid, validate)
				.toString());

	}

	public static List findByDiaryId(int log_id) {
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM AnnexBean  as d");
		hql.append(" WHERE d.diaryID=? ");
		return executeQuery(hql.toString(), -1, -1, log_id);

	}

}
