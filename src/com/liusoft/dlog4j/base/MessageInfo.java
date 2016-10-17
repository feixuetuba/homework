package com.liusoft.dlog4j.base;

import java.io.Serializable;

public class MessageInfo implements Serializable{
	public final static int F_NOT_READ  = 0;
	public final static int F_READ = 1;
	public final static int F_REVERT  = 2;
	public final static int S_NOT_READ  = 3;
	public final static int S_READ = 4;
	public final static int S_REVERT  = 5;
	public final static int SYS_NOT_READ  = 0;
	public final static int SYS_READ  = 2;

	
	private int f_not_read;
	private int f_read;
	private int s_not_read;
	private int s_read;
	private int sys_not_read;
	private int sys_read;
	private int my_send;
	private int affiche;
	
	
	public int getF_not_read() {
		return f_not_read;
	}
	public void setF_not_read(int f_not_read) {
		this.f_not_read = f_not_read;
	}
	public int getF_read() {
		return f_read;
	}
	public void setF_read(int f_read) {
		this.f_read = f_read;
	}
	public int getS_not_read() {
		return s_not_read;
	}
	public void setS_not_read(int s_not_read) {
		this.s_not_read = s_not_read;
	}
	public int getS_read() {
		return s_read;
	}
	public void setS_read(int s_read) {
		this.s_read = s_read;
	}
	public int getSys_not_read() {
		return sys_not_read;
	}
	public void setSys_not_read(int sys_not_read) {
		this.sys_not_read = sys_not_read;
	}
	public int getSys_read() {
		return sys_read;
	}
	public void setSys_read(int sys_read) {
		this.sys_read = sys_read;
	}
	public int getMy_send() {
		return my_send;
	}
	public void setMy_send(int my_send) {
		this.my_send = my_send;
	}
	public int getAffiche() {
		return affiche;
	}
	public void setAffiche(int affiche) {
		this.affiche = affiche;
	}

	
	
}
