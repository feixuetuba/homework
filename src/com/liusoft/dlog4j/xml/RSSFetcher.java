/*
 *  RSSFetcher.java
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
package com.liusoft.dlog4j.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.rss.Channel;
import org.apache.commons.digester.rss.RSSDigester;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.liusoft.dlog4j.DLOG_CacheManager;

/**
 * RSS数据访问入口
 * 
 * @author Winter Lau
 */
public class RSSFetcher {

	final static HttpClient http_client = new HttpClient();

	final static Log log = LogFactory.getLog(RSSFetcher.class);
	private final static String user_agent = "DLOG4J RSS Viewer (http://www.dlog.cn/rss_viewer.html)";
	private final static String cache_name = "DLOG4J_channels";
	private final static Properties charsets = new Properties();

	static {
		InputStream in = RSSFetcher.class.getResourceAsStream("rss.properties");
		try {
			charsets.load(in);
		} catch (IOException e) {
			log.error("Exception occur when loading rss.properties", e);
		}
	}

	/**
	 * 获取频道内容 该方法首先从缓存中读取频道内容，若缓存为空或者是失效则重新从网站上抓取
	 * 
	 * @param type
	 * @param url
	 * @return
	 */
	public static Channel fetchChannel(int type, String url) {
		Channel can = (Channel) DLOG_CacheManager.getObjectCached(cache_name,
				url);
		if (can == null) {
			try {
				can = fetchChannelViaHTTP_(url);
				if (can != null) {
					DLOG_CacheManager.putObjectCached(cache_name, url, can);
				}
			} catch (Exception e) {
				log.error("Exception occurred when fetch " + url, e);
			}
		}
		return can;
	}

	private static Header getResponseHeader(GetMethod m, String name) {
		Header[] headers = m.getResponseHeaders();
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].getName().equalsIgnoreCase(name))
				return headers[i];
		}
		return null;
	}

	/**
	 * 抓取频道内容
	 * 
	 * @param type
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 * @throws SAXException
	 * @throws ParseException
	 */
	private static Channel fetchChannelViaHTTP(String url)
			throws HttpException, IOException, SAXException {
		Digester parser = new XMLDigester();
		GetMethod get = new GetMethod(url);
		get.setRequestHeader("user-agent", user_agent);
		System.out.println(get.getResponseContentLength());
		try {
			http_client.executeMethod(get);
			if (get.getStatusCode() == HttpServletResponse.SC_OK) {
				Charset cs = null;
				Header header_cs = getResponseHeader(get, "Content-Type");
				if (header_cs == null) {
					cs = Charset.forName(get.getResponseCharSet());
				} else {
					String content_type = header_cs.getValue().toLowerCase();
					try {
						Object[] values = content_type_parser
								.parse(content_type);
						cs = Charset.forName((String) values[1]);
					} catch (ParseException e) {
						URL o_url = new URL(url);
						String host = o_url.getHost();
						Iterator hosts = charsets.keySet().iterator();
						while (hosts.hasNext()) {
							String t_host = (String) hosts.next();
							if (host.toLowerCase().endsWith(t_host)) {
								cs = Charset.forName((String) charsets
										.get(t_host));
								break;
							}
						}
						if (cs == null)
							cs = default_charset;

					}
				}

				BufferedReader rd = new BufferedReader(new InputStreamReader(
						get.getResponseBodyAsStream(), cs));

				char[] cbuf = new char[1];
				int read_idx = 1;
				do {
					rd.mark(read_idx++);
					if (rd.read(cbuf) == -1)
						break;
					if (cbuf[0] != '<')
						continue;
					rd.reset();
					break;
				} while (true);
				//System.out.println(rd);
				return (Channel) parser.parse(rd);
			} else {
				log.error("Fetch RSS from " + url + " failed, code="
						+ get.getStatusCode());
			}
		} finally {
			get.releaseConnection();
			//System.err.println(get.releaseConnection());
			return fetchChannelViaHTTP_(url);
		}
		//return null;
	}

	/**
	 * 抓取频道内容[改] 针对encoding="gb2312"无法解析的rss采用
	 * 
	 * @param type
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 * @throws SAXException
	 * @throws ParseException
	 */
	private static Channel fetchChannelViaHTTP_(String url)
			throws HttpException, IOException, SAXException {
		RSSDigester digester = new RSSDigester();
		Channel obj = null;
		obj = (Channel) digester.parse(url);
		return obj;
	}

	private final static Charset default_charset = Charset.forName("utf-8");

	private final static MessageFormat content_type_parser = new MessageFormat(
			"{0};charset={1}");

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String url = "http://www.flashempire.com/include/rss/rssTop10.php";
		RSSDigester digester = new RSSDigester();
		try {
			Channel obj = (Channel) digester.parse(url);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

}
