package com.baidu.translate.main;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.translate.demo.TransApi;

public class Main extends Thread{

	// 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
	static String result= "";
	@Override
	public void run() {
		final String APP_ID = "20191113000356813";
        final String SECURITY_KEY = "KAKwhf_4uVUt5N3dPkeG";
    	TransApi api = new TransApi(APP_ID, SECURITY_KEY);
    	result = api.getTransResult(result, "auto", "jp");
	}
	
	public Main(String str) {
		   result = str;
	   }
	
	public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(result);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch+"" );
        }
        return str;
    }
	
	 public static String getJpString(String str) {
	    	Main m = new Main(str);
	    	m.start();
	    	try {
				m.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return unicodeToString(m.result);
		}
    
	
	
	
	
    //Unicode转中文
//    public static String GetunicodeToString(String str) {
//    	final String APP_ID = "20191113000356813";
//        final String SECURITY_KEY = "KAKwhf_4uVUt5N3dPkeG";
//    	TransApi api = new TransApi(APP_ID, SECURITY_KEY);
//    	str = api.getTransResult(str, "auto", "jp");
//    	
//        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
//        Matcher matcher = pattern.matcher(str);
//        char ch;
//        while (matcher.find()) {
//            ch = (char) Integer.parseInt(matcher.group(2), 16);
//            str = str.replace(matcher.group(1), ch+"" );
//        }
//        return str;
//    }
}
