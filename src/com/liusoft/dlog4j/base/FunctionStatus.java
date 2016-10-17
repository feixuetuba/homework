/*
 *  FunctionStatus.java
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
package com.liusoft.dlog4j.base;

import java.io.Serializable;

/**
 * 网站各个功能的状态
 * @author Winter Lau
 */
public class FunctionStatus implements Serializable {
	
	public final static int STATUS_NORMAL = 0;	//正常
	public final static int STATUS_LOCKED = 1;	//锁定：用户不能增加、修改任何内容
	public final static int STATUS_CLOSED = 2;	//关闭：用户不可见
	
	private final static int EXT1_HIDE_USERS = 0x01;	//是否关闭注册用户的显示
	
	protected int diary;
	protected int photo;
	protected int music;
	protected int forum = STATUS_CLOSED;
	protected int guestbook;
	protected int friend;
	
	protected int ext1;
	protected int ext2;
	protected String ext3;
	protected String ext4;
	
	public boolean isHideUsers(){
		return ((ext1 & EXT1_HIDE_USERS) == EXT1_HIDE_USERS);
	}
	
	public int getDiary() {
		return diary;
	}
	public void setDiary(int diary) {
		this.diary = diary;
	}
	public int getForum() {
		return forum;
	}
	public void setForum(int forum) {
		this.forum = forum;
	}
	public int getGuestbook() {
		return guestbook;
	}
	public void setGuestbook(int guestbook) {
		this.guestbook = guestbook;
	}
	public int getMusic() {
		return music;
	}
	public void setMusic(int music) {
		this.music = music;
	}
	public int getPhoto() {
		return photo;
	}
	public void setPhoto(int photo) {
		this.photo = photo;
	}
	public int getExt1() {
		return ext1;
	}
	public void setExt1(int ext1) {
		this.ext1 = ext1;
	}
	public int getExt2() {
		return ext2;
	}
	public void setExt2(int ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public String getExt4() {
		return ext4;
	}
	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public int getFriend() {
		return friend;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}
		
}
