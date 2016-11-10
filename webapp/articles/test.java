import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test{
	public static void main(String args[]){
		try{
			File f = new File("test_temp.txt");
			FileInputStream inf = new FileInputStream(f);
			byte[] bytes = new byte[4096];
			inf.read(bytes);
			String content = new String(bytes);
			Pattern pattern = Pattern.compile("<img src=\"(/.*[a-z]+)\" ");
			Matcher matcher = pattern.matcher(content);
			while(matcher.find()){
				System.out.println(matcher.group(0));
				System.out.println(matcher.group(1));
				String []strs = matcher.group(1).split("/");
				System.out.println(strs[1]);
			}
			inf.close();
		}catch(Exception e){
			;
		}
	}
}