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
 * վ�ڶ���Ϣ��
 * @author liudong
 */
public class MessageForm extends FormBean {

	protected int receiverId; //�����߱��
	protected String content; //��������
	protected Date expiredTime; //���Ե�ʧЧʱ��
	//=====DLOG 3.5========
	protected int msgID; //�ظ����Եı��
	protected int tableID;//������
	protected String receiverName; //����������
	protected String revertName; //����������
	protected int revertId; //�����߱�� �ж��Ƿ�Ϊ���� 
	protected int status; //״̬ ��ϢԴ��� =0���� >1�ظ� ��ϢԴ���[msgid]
	protected String msgContent; //��������
	protected String title; //����

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
