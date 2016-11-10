package com.liusoft.dlog4j.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.liusoft.dlog4j.base._DiaryBase;
import com.liusoft.dlog4j.dao.DiaryDAO;

public class FLogger {
	public static void doLog(String sth){
		String fpath = "articles/log.txt";
		try{
			File f = new File(fpath);
			if(!f.exists()){
				f.createNewFile();
			}
			
			FileOutputStream out = new FileOutputStream(f, true);
			sth += "\r\n";
			out.write(sth.getBytes("utf-8"));
			out.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
}

}
