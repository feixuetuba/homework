/*
 *  TagBean.java
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
package com.liusoft.dlog4j.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.liusoft.dlog4j.base._MultipleSiteEnabledBean;

/**
 * 标签
 * @author Winter Lau
 */
public class TagBean extends _MultipleSiteEnabledBean {

	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private String name;
	private int refId;
	private int refType;
	private int refTime;	//引用的时间，例如 20070417
	
	public TagBean(){
		this.refTime = Integer.parseInt(sdf.format(new Date())); 
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRefId() {
		return refId;
	}
	public void setRefId(int refId) {
		this.refId = refId;
	}
	public int getRefType() {
		return refType;
	}
	public void setRefType(int refType) {
		this.refType = refType;
	}
	public int getRefTime() {
		return refTime;
	}
	public void setRefTime(int refTime) {
		this.refTime = refTime;
	}

	public static void main(String[] args){
		TagBean tb = new TagBean();
		System.out.println(tb.getRefTime());
	}
}
