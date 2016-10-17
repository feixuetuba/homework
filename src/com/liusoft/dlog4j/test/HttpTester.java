/*
 *  HttpTester.java
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
 *  2006-9-14
 */
package com.liusoft.dlog4j.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * HTTPøÕªß∂À≤‚ ‘¿‡
 * @author liudong
 */
public class HttpTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		HttpClient http = new HttpClient();		
		String url = "http://www.vcmedu.com/vcmplat/vcmLoad.do?method=showHome";
		GetMethod get = new GetMethod(url);
		try{
			System.out.println("fetching url : "+ url);
			get.addRequestHeader("accept-encoding", "gzip,deflate");
			get.addRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; Alexa Toolbar; Maxthon 2.0)");
			int er = http.executeMethod(get);
			if(er==200){
				System.out.println(get.getResponseContentLength());
				String html = get.getResponseBodyAsString();
				System.out.println(html);
				System.out.println(html.getBytes().length);
			}
		}finally{
			get.releaseConnection();
		}
	}

}
