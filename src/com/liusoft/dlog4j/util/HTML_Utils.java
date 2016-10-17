/*
 *  HTML_Utils.java
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
 *  2006-8-17
 */
package com.liusoft.dlog4j.util;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.liusoft.dlog4j.Globals;

/**
 * ���ڸ�ʽ��HTML�Ĺ�����
 * @author liudong
 */
public class HTML_Utils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String html = "<FONT CLASS=\"FrameItemFont\"><A HREF=\"org/htmlparser/lexer/package-frame.html\" target=\"packageFrame\">org.htmlparser.lexer</A></FONT><BR><FONT CLASS=\"FrameItemFont\"><A HREF=\"org/htmlparser/lexerapplications/tabby/package-frame.html\" target=\"packageFrame\">org.htmlparser.lexerapplications.tabby</A></FONT><BR><FONT CLASS=\"FrameItemFont\"><A HREF=\"org/htmlparser/lexerapplications/thumbelina/package-frame.html\" target=\"packageFrame\">org.htmlparser.lexerapplications.thumbelina</A></FONT><BR><FONT CLASS=\"FrameItemFont\"><A HREF=\"org/htmlparser/nodes/package-frame.html\" target=\"packageFrame\">org.htmlparser.nodes</A></FONT>";
		int pre_length = 120;
		String preview = preview(html, pre_length);
		System.out.println(html);
		System.out.println(html.substring(0, pre_length));
		System.out.println(preview);
	}

	private final static NodeFilter nfilter = new NodeFilter(){
		public boolean accept(Node arg0) {
			return true;
		}};
		
	private final static PrototypicalNodeFactory factory = new PrototypicalNodeFactory (){{
		registerTag(new FontTag());
		registerTag(new BoldTag());
		registerTag(new SupTag());
	}};
		
	/**
	 * ����Ԥ������
	 * @param html
	 * @param max_count
	 * @return
	 */
	public static String preview(String html, int max_count){
		if(html.length()<= max_count * 1.1)
			return html;
		Parser parser = new Parser();
		StringBuffer prvContent = new StringBuffer();
		try {
			parser.setEncoding(Globals.ENC_8859_1);
			parser.setInputHTML(html);
			
			parser.setNodeFactory(factory);

			NodeList nodes = parser.extractAllNodesThatMatch(nfilter);
			Node node = null;
			for(int i=0;i<nodes.size();i++){
				if(prvContent.length() >= max_count){
					if(node instanceof TagNode){
						TagNode tmp_node = (TagNode)node;
						boolean isEnd = tmp_node.isEndTag();
						if(!isEnd){
							prvContent.setLength(prvContent.length()-tmp_node.getText().length()-2);
						}
					}
					//��������δ�رյı�ǩ
					Node parent = node;
					//System.out.println("current node is . "+parent.getText());
					do{
						//System.out.println(parent.getClass().getName()+":"+parent.getText());
						parent = parent.getParent();	
						//System.out.println("parent = "+parent);					
						if(parent==null) break;
						if(!(parent instanceof TagNode)) continue;
						//System.out.println("Parent node is no ended. "+parent.getText());
						prvContent.append(((TagNode)parent).getEndTag().toHtml());
					}while(true);
					break;
				}
				node = nodes.elementAt(i);
				if(node instanceof TagNode){
					TagNode tag = (TagNode)node;
					prvContent.append('<');
					prvContent.append(tag.getText());
					prvContent.append('>');
					//System.out.println("TAG: " + '<'+tag.getText()+'>');
				}
				else if(node instanceof TextNode){
					int space = max_count - prvContent.length();
					if(space > 10){
						TextNode text = (TextNode)node;
						if(text.getText().length() < 10)
							prvContent.append(text.getText());
						else
							prvContent.append(StringUtils.abbreviate(text.getText(), max_count - prvContent.length()));
						//System.out.println("TEXT: " + text.getText());
					}
				}
			}
			return prvContent.toString();
		} catch (ParserException e) {
			e.printStackTrace();
		}finally{
			parser = null;
		}
		return html;
	}
	
	/**
	 * �����ǩ
	 * @author liudong
	 */
	public static class FontTag extends CompositeTag {
		private static final String[] mIds = new String[] {"FONT"};
	    private static final String[] mEndTagEnders = new String[] {};
	    public String[] getIds (){
	        return (mIds);
	    }
	    public String[] getEnders (){
	        return (mIds);
	    }
	    public String[] getEndTagEnders (){
	        return (mEndTagEnders);
	    }	   
	}	
	/**
	 * SUP��ǩ
	 * @author liudong
	 */
	public static class SupTag extends CompositeTag {
		private static final String[] mIds = new String[] {"SUP"};
	    private static final String[] mEndTagEnders = new String[] {};
	    public String[] getIds (){
	        return (mIds);
	    }
	    public String[] getEnders (){
	        return (mIds);
	    }
	    public String[] getEndTagEnders (){
	        return (mEndTagEnders);
	    }	   
	}	
	/**
	 * �����ǩ
	 * @author liudong
	 */
	public static class BoldTag extends CompositeTag {
		private static final String[] mIds = new String[] {"B", "U", "I", "STRONG"};
	    private static final String[] mEndTagEnders = new String[] {};
	    public String[] getIds (){
	        return (mIds);
	    }
	    public String[] getEnders (){
	        return (mIds);
	    }
	    public String[] getEndTagEnders (){
	        return (mEndTagEnders);
	    }	   
	}	
}
