/*
 *  UserLoginManager.java
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
 *  
 */
package com.liusoft.dlog4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.UserBean;
import com.liusoft.dlog4j.dao.UserDAO;
import com.liusoft.dlog4j.util.RequestUtils;
import com.liusoft.dlog4j.util.StringUtils;

/**
 * �û���¼���� ����Cookie��Session��Ͻ����û���¼�Ự���ж� 
 * TODO: ��ֹһ��__ClientIdʹ�ö��
 * 
 * <pre>���㲻����ӵ��ʱ,��Ψһ������,�������Լ���Ҫ����</pre>
 * 
 * @author Winter Lau
 */
public class UserLoginManager {

	private final static Log log = LogFactory.getLog(UserLoginManager.class);
	
	/**
	 * �������ύʱ����������ɱ������0.5��������
	 */
	private final static int MIN_MS_BETWEEN_ACTION = 500;

	/**
	 * ��������Ч����һ��Сʱ
	 */
	private final static int MAX_MS_BETWEEN_ACTION = 3600000;
	private final static String SESSION_USER_KEY = "UserBean";
	private final static String COOKIE_LASTLOGIN_KEY = "LL";//last login key
	private final static String COOKIE_UUID_KEY = "uuid";

	/**
	 * �Զ���¼ ����Velocityҳ��
	 * 
	 * @param request
	 * @param response
	 * @param uuid
	 * @param verify_host
	 * @return
	 * @see com.liusoft.dlog4j.velocity.DLOG_VelocityTool#verify_login_cookie(String,
	 *      boolean)
	 */
	public static SessionUserObject getLoginUser(HttpServletRequest request,
			HttpServletResponse response, boolean verify_host) {
		// ���session�м�¼��ֱ�Ӵ�session�ж�ȡ������
		Cookie uuidCookie = null;		
		HttpSession ssn = request.getSession(false);
		if (ssn != null) {
			SessionUserObject user = (SessionUserObject) ssn
					.getAttribute(SESSION_USER_KEY);
			if (user != null){				
				uuidCookie = getUuidCookie(request);
				//������sessionֵ���ڣ���cookieֵҲ���ڲ���Ч
				//(��Ҫ��Լ�Ⱥ�����£��û���s1ע���ˣ�������������s2ʱ������ʾ��¼״̬)
				if(uuidCookie!=null)
					return user;
				ssn.invalidate();
				return null;
			}
		}
		String uuid = null;
		if(uuidCookie == null)
			uuidCookie = getUuidCookie(request);
		if (uuidCookie != null)
			uuid = uuidCookie.getValue();
		if(StringUtils.isEmpty(uuid))
			return null;
		// session�������û�������ִ���Զ���¼����
		try {
			UUID oUUID = new UUID(uuid);
			String new_host = request.getRemoteAddr();
			if (verify_host && !StringUtils.equals(new_host, oUUID.host))
				return null;
			UserBean user = UserDAO.getUserByID(oUUID.uid);
			// �û�������
			if (user == null || user.getStatus() != UserBean.STATUS_NORMAL
					|| user.getPassword().hashCode() != oUUID.pwdCode) {
				RequestUtils.setCookie(request, response, COOKIE_UUID_KEY, "", 0);
				RequestUtils.setCookie(request, response, COOKIE_LASTLOGIN_KEY, "", 0);
				return null;
			}
			int keep_days = user.getKeepDays();
			if(keep_days < 1) keep_days = 365;
			return loginUser(request, response, user, keep_days);
		} catch (Exception e) {
			log.error("Exception occur when get current user.", e);
		}

		return null;
	}
	
	/**
	 * ͨ���û�Ψһʶ�����ȡ�û�����
	 * @param request
	 * @param response
	 * @param uuid
	 * @return
	 */
	public static UserBean getUserByUUID(HttpServletRequest request,
			HttpServletResponse response, String uuid){
		UserBean user = null;
		if(StringUtils.isNotBlank(uuid)){
			user = UserDAO.getUserByUniqueCode(uuid);
			if(user!=null)
				UserLoginManager.loginUser(request, response, user, 365);
		}
		return user;
	}

	/**
	 * �ж��û��ǲ��ǳ�������Ա
	 * @param user
	 * @return
	 */
	public static boolean isSuperior(SessionUserObject user){
		if(user == null)
			return false;
		return (owner_ids != null && owner_ids
				.contains(String.valueOf(user.getId())));
	}
	
	/**
	 * �����ѵ�¼�ĻỰ�е��û�����
	 * 
	 * @param req
	 * @param ubean
	 */
	public static void updateLoginUser(HttpServletRequest req, UserBean ubean) {
		HttpSession ssn = req.getSession(true);
		if (ssn != null && ubean != null) {
			ssn.setAttribute(SESSION_USER_KEY, SessionUserObject
					.copyFrom(ubean));
		}
	}

	/**
	 * ���ر����û���¼��Ϣ��Cookie
	 * 
	 * @param request
	 * @return
	 */
	protected static Cookie getUuidCookie(HttpServletRequest request) {
		return RequestUtils.getCookie(request, COOKIE_UUID_KEY);
	}

	/**
	 * ִ���û���¼����
	 * 
	 * @param req
	 * @param res
	 * @param ubean
	 * @param keepDays
	 * @see com.liusoft.dlog4j.action.UserAction#doLogin(ActionMapping,
	 *      ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public static SessionUserObject loginUser(HttpServletRequest req,
			HttpServletResponse res, UserBean ubean, int keepDays) {		
		//System.out.println("keepDays="+keepDays );
		HttpSession ssn = req.getSession(false);
		if (ssn != null) {
			SessionUserObject rub = (SessionUserObject) ssn.getAttribute(SESSION_USER_KEY);
			if (rub != null && rub.getId()==ubean.getId()) {
				return rub;
			}
		}
		
		// �����û����������һ�η���ʱ���Լ����ʵ�ַ,ͬʱ��������״̬
		ubean.setLastAddr(req.getRemoteAddr());
		ubean.setLastTime(new Timestamp(System.currentTimeMillis()));
		ubean.setKeepDays(keepDays);
		ubean.setOnlineStatus(1);
		DLOGUserManager.userLogin(ubean);

		// д��¼��Ϣ��cookie,��ʹ��session�����û�����
		if(getUuidCookie(req)==null){
			UUID uuid = new UUID();
			uuid.uid = ubean.getId();
			uuid.username = ubean.getName();
			uuid.pwdCode = ubean.getPassword().hashCode();
			uuid.host = req.getRemoteAddr();
	
			String value = uuid.toString();
			RequestUtils.setCookie(req, res, COOKIE_UUID_KEY, value,
					(keepDays > 0) ? keepDays * 86400 : -1);
			RequestUtils.setCookie(req, res, COOKIE_LASTLOGIN_KEY, ubean.getLastTime()
					.toString(), -1);
		}
		// �û����ϱ�����Session��
		if (ssn == null)
			ssn = req.getSession(true);
		if (ssn != null && ubean != null) {
			ssn.setAttribute(SESSION_USER_KEY, SessionUserObject
					.copyFrom(ubean));
		}
		return ubean;
	}

	/**
	 * ע���û�
	 * 
	 * @param req
	 * @param res
	 * @see com.liusoft.dlog4j.action.UserAction#doLogout(ActionMapping,
	 *      ActionForm, HttpServletRequest, HttpServletResponse, String)
	 */
	public static void logoutUser(HttpServletRequest req,
			HttpServletResponse res) {
		// ����û����е�keep_days�ֶε�ֵ���û��´β������Զ���¼
		SessionUserObject ubean = getLoginUser(req, res, false);
		if (ubean != null) {
			DLOGUserManager.userLogout(ubean, true);
		}

		// ���Cookie
		RequestUtils.setCookie(req, res, COOKIE_UUID_KEY, "", 0);
		RequestUtils.setCookie(req, res, COOKIE_LASTLOGIN_KEY, "", 0);

		// ���session
		HttpSession ssn = req.getSession(false);
		if (ssn != null) {
			ssn.invalidate();
		}
	}

	/**
	 * ע���û�
	 * 
	 * @param userid
	 * @param lastLogin
	 * @see SessionUserObject#valueUnbound(HttpSessionBindingEvent)
	 */
	public static void logoutUser(SessionUserObject user) {
		DLOGUserManager.userLogout(user, false);
	}

	/**
	 * ��֤�ͻ��˰�ȫʶ����
	 * 
	 * @param req
	 * @param clientId
	 * @return
	 */
	public static boolean validateClientId(HttpServletRequest req,
			String clientId) {
		return ClientID.validate(req, clientId);
	}

	/**
	 * ���ɿͻ��˰�ȫʶ����
	 * 
	 * @param req
	 * @return
	 */
	public static String generateClientId(HttpServletRequest req,
			HttpServletResponse res) {
		return ClientID.generate(req, res);
	}

	/**
	 * �Զ���¼��ʶ
	 * 
	 * @author liudong
	 */
	private static class UUID {

		/**
		 * �Զ���¼��ʶ�ļ������� IMPORTANT: �����޸ĸ�ֵ�����±���ϵͳ�Ա�֤ϵͳ�İ�ȫ�� ����Կ�ĳ��ȱ�����8��������
		 */
		private final static String UUID_ENCRYPT_KEY = "1D2L3O4G546J7V83";
		private final static String PATTERN = "{0}_{1}@{2}_{3}";
		private final static MessageFormat parser = new MessageFormat(PATTERN);

		public int uid;
		public String username;
		public String host;
		public int pwdCode;

		public UUID() {}

		/**
		 * ���л����ַ���
		 */
		public String toString() {
			String uuid = MessageFormat.format(PATTERN, String.valueOf(uid),
					String.valueOf(pwdCode), host, username );
			return StringUtils.encrypt(uuid, UUID_ENCRYPT_KEY);
		}

		/**
		 * ��ԭ
		 * 
		 * @param cookie
		 * @return
		 * @throws ParseException
		 */
		public UUID(String cookie) throws ParseException {
			String uuid = StringUtils.decrypt(cookie, UUID_ENCRYPT_KEY);
			Object[] args = parser.parse(uuid);
			uid = Integer.parseInt((String) args[0]);
			pwdCode = Integer.parseInt((String) args[1]);
			host = (String) args[2];
			username = (String) args[3];
		}
		
		public static void main(String[] args) throws Exception {
			String cookie = "5833C68C30C2755DB8C1505131989698688B358EBC79EEEC78BBA1F6EEC7CEB9";
			UUID uuid = new UUID(cookie);
			System.out.println(uuid.username);
		}

	}

	/**
	 * �ͻ�����֤��
	 * 
	 * @author liudong
	 */
	private static class ClientID {

		private final static String CLIENTID_ENCRYPT_KEY = ".DLOG.4J";
		private final static String PATTERN = "{0}|{1}|{2}";
		private final static MessageFormat parser = new MessageFormat(PATTERN);

		/**
		 * ���ɿͻ��˰�ȫʶ����
		 * 
		 * @param req
		 * @return
		 */
		public static String generate(HttpServletRequest req,
				HttpServletResponse res) {
			long ct = System.currentTimeMillis();
			String user_agent = RequestUtils.getHeader(req, Globals.USER_AGENT);
			String remote_host = req.getRemoteAddr();
			StringBuffer code = new StringBuffer();
			code.append(ct);
			code.append('|');
			code.append(remote_host);
			code.append('|');
			if (user_agent != null)
				code.append(Math.abs(user_agent.hashCode()));
			else
				code.append(ct);
			return StringUtils.encrypt(code.toString(), CLIENTID_ENCRYPT_KEY);
		}

		/**
		 * ��֤�ͻ��˰�ȫʶ����
		 * TODO: ������ͣ��host������֤!
		 * @param req
		 * @param clientId
		 * @return
		 */
		public static boolean validate(HttpServletRequest req, String clientId) {
			String clientCode = StringUtils.decrypt(clientId,
					CLIENTID_ENCRYPT_KEY);
			try {
				Object[] objs = parser.parse(clientCode);
				//String host = req.getRemoteAddr();
				//if (host.equals(objs[1])) {
					String user_agent = RequestUtils.getHeader(req,
							Globals.USER_AGENT);
					String ua = (user_agent!=null)?String.valueOf(Math.abs(user_agent.hashCode())):null;
					if (objs[2].equals(ua) || objs[2].equals(objs[0])) {
						long lt = Long.parseLong((String) objs[0]);
						long ct = System.currentTimeMillis();
						// �ͻ���ʶ�������Ч����һ����ͷ
						long it = ct - lt;
						if (MIN_MS_BETWEEN_ACTION < it
								&& it < MAX_MS_BETWEEN_ACTION) {
							return true;
						}
					}
				//}
			} catch (ParseException e) {
			}
			return false;
		}

	}
	

	final static List<String> owner_ids;
	static{
		owner_ids = new Vector<String>();
		InputStream in = SiteBean.class.getResourceAsStream("/administrators");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try{
			do{
				String ln = br.readLine();
				if(ln==null)
					break;
				ln = ln.trim();
				if(StringUtils.isEmpty(ln))
					continue;
				if(ln.startsWith("#"))
					continue;
				if(!owner_ids.contains(ln))
					owner_ids.add(ln);
			}while(true);
		}catch(IOException e){
			log.error("Error when loading administrators", e);
		}finally{
			if(br!=null)
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}