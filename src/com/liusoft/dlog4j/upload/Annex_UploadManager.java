package com.liusoft.dlog4j.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liusoft.dlog4j.beans.AnnexBean;
import com.liusoft.dlog4j.beans.FckUploadFileBean;

/**
 * 处理附件的servlet
 */
public class Annex_UploadManager extends HttpServlet {
	private final static Log log = LogFactory.getLog(Annex_UploadManager.class);// 日志处理

	private static UploadFileHandler fileHandler;// 接口
	private static int max_annex_size = 5 * 1024 * 1024;// 最大上传限制5M
	private static String path;// 保存路径
	private static String baseURI;// 显示 下载 路径
	protected static String[] extendName;// 文件扩展名

	public static String[] getExtendName() {
		return extendName;
	}

	/**
	 * 文件扩展名集合
	 */
	protected void setExtendName(String names) {
		String[] extendName = null;
		if (names.indexOf(",") > 0 && names.length() > 0)
			extendName = names.split(",");
		this.extendName = extendName;
	}

	/**
	 * 子类可以覆盖该方法以实现判断所上传文件的类型是否被允许
	 * 
	 * @param fileName
	 * @return
	 */
	protected boolean isFileAllowed(String fileName) {
		// 转小写
		fileName = fileName.toLowerCase();
		return isExtendName(fileName);
	}

	/**
	 * 判断文件扩展名
	 */
	private boolean isExtendName(String filename) {
		for (int i = 0; i < extendName.length; i++) {
			if (filename.endsWith(extendName[i]))
				return true;
		}
		return false;
	}

	/**
	 * 初始化 Annex_UploadManager
	 * 
	 * @see 主要设置参数 <br>
	 *      <b>max_annex_size</b> 上传的大小限制<br>
	 *      <b>fileHandler</b> 接口的实现<br>
	 *      <b>path</b> 保存路径<br>
	 *      <b>baseURI</b> 下载路径<br>
	 *      <b>extendName[]</b> 文件扩展名<br>
	 * @throws ServletException
	 */
	public void init() throws ServletException {
		String s_max_upload_size = getInitParameter("max_annex_size");// 初始参数
		if (s_max_upload_size != null) {
			max_annex_size = Integer.parseInt(s_max_upload_size);// 接受参数值
			if (max_annex_size < 0)
				max_annex_size = Integer.MAX_VALUE;
			else
				max_annex_size *= 1024;// K为单位
		}
		// ========================================================================================
		String s_file_handler_class = getInitParameter("file_save_class");// 实现UploadFileHandler类的子类路径
		if (s_file_handler_class == null)// 判断是否有实现类
			fileHandler = new DiskUploadFileHandler();// 初始为空时
		// 用DiskUploadFileHandler()实现类
		else {
			try {
				// 初始参数不为空
				// 实例化初始参数 中的实现类路径
				fileHandler = (UploadFileHandler) Class.forName(
						s_file_handler_class).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		}
		fileHandler.init(getServletConfig());
		// ===========================================================================================
		this.path = getInitParameter("file_saved_path");// 保存路径
		this.baseURI = getInitParameter("file_base_uri");// 下载路径
		// ==========================================================================================
		this.setExtendName(getInitParameter("validate_file_extend_name"));// 文件扩展名

	}

	/**
	 * 处理上传文件
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// // System.out.println(req.getParameter("fromPage"));
		// int errorNum = 0;
		// UploadAnnexInfo uai = null;
		// if (req.getContentLength() < 0) {
		// res.sendError(HttpServletResponse.SC_LENGTH_REQUIRED);
		// return;
		// }
		// //System.out.println(req.getHeader("Accept-Language"));
		// if (isMultipart(req)) {
		//
		//		
		// req.setCharacterEncoding("UTF-8");
		// res.setContentType("text/html; charset=UTF-8");
		//		
		//			
		// String uri = null;
		// String msg = null;
		// Hashtable ht = null;
		// if (max_annex_size == 0 || req.getContentLength() > max_annex_size) {
		// msg = "文件大小不得超过" + (max_annex_size / 1024) + 'K';
		// errorNum = -1;
		// } else {
		// HttpMultiPartParser2 parser2 = new HttpMultiPartParser2();
		// try {
		// int bstart = req.getContentType().lastIndexOf("oundary=");
		// String bound = req.getContentType().substring(bstart + 8);
		// ht = parser2.processData(req.getInputStream(), bound, path);
		// uai = (UploadAnnexInfo) ht.get("fileName");
		// if (uai != null) {
		// // 对文件类型进行判断，不允许上传一些可执行的脚本
		// if (isFileAllowed(uai.clientFileName)) {
		// req.setAttribute("file.size", new Long(uai.file
		// .length()));
		// uri = Annex_UploadManager.getFileHandler().save(
		// req, res, uai.file);// 上传保存文件
		// req.setAttribute("file.uri", uri);
		// msg = "上传成功!";
		// errorNum = 1;
		// // 把附件信息写入数据库
		// AnnexBean annexBean = new AnnexBean();
		// System.out.println(uai.clientFileName);
		// // 每次提交日记检查是否有未关联在日记上的附件
		// } else {
		// msg = "该文件类型不得上传!";
		// errorNum = -1;
		// }
		// }
		// } catch (Exception ex) {
		// msg = "上传失败,请联系管理员!";
		// errorNum = -1;
		// ex.printStackTrace();
		// }
		// }
		// req.setAttribute("upload_annex_uri", uri);// 路径
		// req.setAttribute("upload_annex_num", errorNum);// 错误号
		// req.setAttribute("upload_annex_msg", msg);// 显示消息
		// req.setAttribute("upload_annex_bit", uai.file.length());// 附件名称
		// req.setAttribute("upload_annex_time", new Date());// 提示消息
		// getServletContext().getRequestDispatcher(
		// "/html/diary/_upload_annex.vm").forward(req, res);
		// } else {
		// // String html = generateHtmlResult(uri, uri, msg);
		// // makeOutput(req, res, ht, html);
		// }
		// // }
		// // else {
		// // res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		// // }
	}

	/**
	 * 关闭服务器
	 */
	public void destroy() {
		if (fileHandler != null)
			fileHandler = null;
		// this.destroy();
	}

	public static UploadFileHandler getFileHandler() {
		return fileHandler;
	}

	public static int getMax_annex_size() {
		return max_annex_size;
	}

	public static String getPath() {
		return path;
	}

	public static String getBaseURI() {
		return baseURI;
	}

}

/**
 * 记录每一个上传文件的信息
 * 
 * @author liudong
 */
class UploadAnnexInfo {
	public String name = null;

	public String clientFileName = null;

	public String fileContentType = null;

	private byte[] fileContents = null;

	public File file = null;

	public StringBuffer sb = new StringBuffer(100);

	public void setFileContents(byte[] aByteArray) {
		fileContents = new byte[aByteArray.length];
		System.arraycopy(aByteArray, 0, fileContents, 0, aByteArray.length);
	}
}

class HttpMultiPartParser2 {

	private final int ONE_MB = 1024 * 1024 * 1;

	/**
	 * 文件上传表单的解析
	 * 
	 * @param is
	 * @param boundary
	 * @param saveInDir
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public Hashtable processData(ServletInputStream is, String boundary,
			String saveInDir) throws IllegalArgumentException, IOException {
		if (is == null)//
			throw new IllegalArgumentException("InputStream");
		if (boundary == null || boundary.trim().length() < 1)
			throw new IllegalArgumentException("\"" + boundary
					+ "\" is an illegal boundary indicator");
		boundary = "--" + boundary;
		StringTokenizer stLine = null, stFields = null;
		UploadAnnexInfo annexInfo = null;
		Hashtable<String, Object> dataTable = new Hashtable<String, Object>(5);
		String line = null, field = null, paramName = null;
		boolean saveFiles = (saveInDir != null && saveInDir.trim().length() > 0);
		boolean isFile = false;
		if (saveFiles) { // Create the required directory (including parent
			// dirs)
			File f = new File(saveInDir);
			f.mkdirs();
		}
		line = getLine(is);
		if (line == null || !line.startsWith(boundary))
			throw new IOException("Boundary not found; boundary = " + boundary
					+ ", line = " + line);
		while (line != null) {
			if (line == null || !line.startsWith(boundary))
				return dataTable;
			line = getLine(is);
			if (line == null)
				return dataTable;
			stLine = new StringTokenizer(line, ";\r\n");
			if (stLine.countTokens() < 2)
				throw new IllegalArgumentException("Bad data in second line");
			line = stLine.nextToken().toLowerCase();
			if (line.indexOf("form-data") < 0)
				throw new IllegalArgumentException("Bad data in second line");
			stFields = new StringTokenizer(stLine.nextToken(), "=\"");
			if (stFields.countTokens() < 2)
				throw new IllegalArgumentException("Bad data in second line");
			annexInfo = new UploadAnnexInfo();
			stFields.nextToken();
			paramName = stFields.nextToken();
			isFile = false;
			if (stLine.hasMoreTokens()) {
				field = stLine.nextToken();
				stFields = new StringTokenizer(field, "=\"");
				if (stFields.countTokens() > 1) {
					if (stFields.nextToken().trim()
							.equalsIgnoreCase("filename")) {
						annexInfo.name = paramName;
						String value = stFields.nextToken();
						if (value != null && value.trim().length() > 0) {
							annexInfo.clientFileName = value;
							isFile = true;
						} else {
							line = getLine(is); // Skip "Content-Type:" line
							line = getLine(is); // Skip blank line
							line = getLine(is); // Skip blank line
							line = getLine(is); // Position to boundary line
							continue;
						}
					}
				} else if (field.toLowerCase().indexOf("filename") >= 0) {
					line = getLine(is); // Skip "Content-Type:" line
					line = getLine(is); // Skip blank line
					line = getLine(is); // Skip blank line
					line = getLine(is); // Position to boundary line
					continue;
				}
			}
			boolean skipBlankLine = true;
			if (isFile) {
				line = getLine(is);
				if (line == null)
					return dataTable;
				if (line.trim().length() < 1)
					skipBlankLine = false;
				else {
					stLine = new StringTokenizer(line, ": ");
					if (stLine.countTokens() < 2)
						throw new IllegalArgumentException(
								"Bad data in third line");
					stLine.nextToken(); // Content-Type
					annexInfo.fileContentType = stLine.nextToken();
				}
			}
			if (skipBlankLine) {
				line = getLine(is);
				if (line == null)
					return dataTable;
			}
			if (!isFile) {
				line = getLine(is);
				if (line == null)
					return dataTable;
				dataTable.put(paramName, line);
				// If parameter is dir, change saveInDir to dir
				line = getLine(is);
				continue;
			}
			try {
				OutputStream os = null;
				String path = null;
				if (saveFiles)
					os = new FileOutputStream(path = getFileName(saveInDir,
							annexInfo.clientFileName));
				else
					os = new ByteArrayOutputStream(ONE_MB);
				boolean readingContent = true;
				byte previousLine[] = new byte[2 * ONE_MB];
				byte temp[] = null;
				byte currentLine[] = new byte[2 * ONE_MB];
				int read, read3;
				if ((read = is.readLine(previousLine, 0, previousLine.length)) == -1) {
					line = null;
					break;
				}
				while (readingContent) {
					if ((read3 = is
							.readLine(currentLine, 0, currentLine.length)) == -1) {
						line = null;
						break;
					}
					if (compareBoundary(boundary, currentLine)) {
						os.write(previousLine, 0, read - 2);
						line = new String(currentLine, 0, read3);
						break;
					} else {
						os.write(previousLine, 0, read);
						temp = currentLine;
						currentLine = previousLine;
						previousLine = temp;
						read = read3;
					}// end else
				}// end while
				os.flush();
				os.close();
				if (!saveFiles) {
					ByteArrayOutputStream baos = (ByteArrayOutputStream) os;
					annexInfo.setFileContents(baos.toByteArray());
				} else
					annexInfo.file = new File(path);
				dataTable.put(paramName, annexInfo);
			}// end try
			catch (IOException e) {
				throw e;
			}
		}
		return dataTable;
	}

	public String getFileName(String dir, String fileName)
			throws IllegalArgumentException {
		String path = null;
		if (dir == null || fileName == null)
			throw new IllegalArgumentException("dir or fileName is null");
		int index = fileName.lastIndexOf('/');
		String name = null;
		if (index >= 0)
			name = fileName.substring(index + 1);
		else
			name = fileName;
		index = name.lastIndexOf('\\');
		if (index >= 0)
			fileName = name.substring(index + 1);
		path = dir + File.separator + fileName;
		if (File.separatorChar == '/')
			return path.replace('\\', File.separatorChar);
		else
			return path.replace('/', File.separatorChar);
	}

	/**
	 * Compares boundary string to byte array
	 */
	private boolean compareBoundary(String boundary, byte ba[]) {
		if (boundary == null || ba == null)
			return false;
		for (int i = 0; i < boundary.length(); i++)
			if ((byte) boundary.charAt(i) != ba[i])
				return false;
		return true;
	}

	/**
	 * Convenience method to read HTTP header lines
	 */
	private synchronized String getLine(ServletInputStream sis)
			throws IOException {
		byte b[] = new byte[1024];
		int read = sis.readLine(b, 0, b.length), index;
		String line = null;
		if (read != -1) {
			line = new String(b, 0, read);
			if ((index = line.indexOf('\n')) >= 0)
				line = line.substring(0, index - 1);
		}
		return line;
	}
}// End of class HttpMultiPartParser
