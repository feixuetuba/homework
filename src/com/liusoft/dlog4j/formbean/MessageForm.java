/*
 *  MessageForm.java
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
package com.liusoft.dlog4j.formbean;

import java.sql.Date;

/**
 * 站内短消息表单
 * @author liudong
 */
public class MessageForm extends FormBean {

	protected int receiverId; //接收者编号
	protected String content; //留言内容
	protected Date expiredTime; //留言的失效时间
	//=====DLOG 3.5========
	protected int msgID; //回复留言的编号
	protected int tableID;//表主键
	protected String receiverName; //接受者名称
	protected String revertName; //发送者名称
	protected int revertId; //发送者编号 判断是否为好友 
	protected int status; //状态 信息源编号 =0发送 >1回复 消息源编号[msgid]
	protected String msgContent; //留言内容
	protected String title; //标题

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public int getMsgID() {
		return msgID;
	}

	public void setMsgID(int msgID) {
		this.msgID = msgID;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getRevertName() {
		return revertName;
	}

	public void setRevertName(String revertName) {
		this.revertName = revertName;
	}

	public int getRevertId() {
		return revertId;
	}

	public void setRevertId(int revertId) {
		this.revertId = revertId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public int getTableID() {
		return tableID;
	}

	public void setTableID(int tableID) {
		this.tableID = tableID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
