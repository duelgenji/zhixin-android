package com.qubaopen.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



//知心 字符串相关 工具类                   by knight  2014-10-29 20:40:40
public class StringFormatUtil {

	public static String formatBreakLine(String content) {
		content=content.replace("\\n", "\n");
		return content;
	}
	
	public static int caculateStringLength(String content){
		int len = 0;
		Pattern numberPattern = Pattern.compile("[0-9]*"); 
		Pattern charPattern = Pattern.compile("[a-zA-Z]");
		Pattern chinesePattern = Pattern.compile("[\u4e00-\u9fa5]");
		for (int i = 0; i < content.length(); i++) {
			String str = content.substring(i, i+1);
			 Matcher numberMatcher = numberPattern.matcher(str); 
			 Matcher charMatcher = charPattern.matcher(str);
			 Matcher chineseMatcher = chinesePattern.matcher(str);
			 if(numberMatcher.matches() || charMatcher.matches()){
		    	 len += 1;
		      } 
		     
		     if(chineseMatcher.matches()){
		    	 len += 2;
		     }
		}
	   
		return len;
		
	}
}
