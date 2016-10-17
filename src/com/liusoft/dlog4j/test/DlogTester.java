/*
 *  DlogTester.java
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
 */
package com.liusoft.dlog4j.test;

import java.util.List;

import com.liusoft.dlog4j.beans.DiaryOutlineBean;
import com.liusoft.dlog4j.beans.MusicOutlineBean;
import com.liusoft.dlog4j.beans.PhotoOutlineBean;
import com.liusoft.dlog4j.dao.DAO;
import com.liusoft.dlog4j.dao.UserDAO;

/**
 * DLOG4j的一些测试方法
 * -Djava.ext.dirs=webapp/WEB-INF/lib -cp webapp/WEB-INF/classes
 * @author Winter Lau
 */
public class DlogTester extends DAO {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("====== diarys ======");
		List<DiaryOutlineBean> logs = UserDAO.listDiarysOfFriends(2,-1,20);
		for(DiaryOutlineBean log : logs){
			System.out.println(log.getTitle());
		}
		System.out.println("====== photos ======");
		List<PhotoOutlineBean> photos = UserDAO.listPhotosOfFriends(2,-1,20);
		for(PhotoOutlineBean p : photos){
			System.out.println(p.getTitle());
		}

		System.out.println("====== songs ======");
		List<MusicOutlineBean> songs = UserDAO.listSongsOfFriends(2,-1,20);
		for(MusicOutlineBean song : songs){
			System.out.println(song.getTitle());
		}
	}

}
