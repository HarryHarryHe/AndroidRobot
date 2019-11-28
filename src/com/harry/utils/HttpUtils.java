package com.harry.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;

import com.google.gson.Gson;
import com.harry.bean.ChatMessage;
import com.harry.bean.ChatMessage.Type;
import com.harry.bean.Result;


public class HttpUtils {

	private static final String URL = "http://www.tuling123.com/openapi/api";
	private static final String API_KEY = "a09669dffb4b475fbee7b3e06156473e";
	
	/***
	 * ����һ����Ϣ���õ����ص���Ϣ
	 * @param msg
	 * @return
	 */
	public static ChatMessage sendMessage(String msg) {
		ChatMessage chatMessage = new ChatMessage();
		
		String jsonRes = doGet(msg);
		Gson gson = new Gson();  //jar�� ����json
		Result result = null;
		try {
			result = gson.fromJson(jsonRes,Result.class);
			chatMessage.setMsg(result.getText());
		} catch (Exception e) {
			chatMessage.setMsg("��������æ�����Ժ�����");
		}
		chatMessage.setDate(new Date());
		chatMessage.setType(Type.INCOMING);
		
		return chatMessage;
	}
	
	//�������ݵ���Ӧ��api���õ���Ӧ��json��Ӧ
	public static String doGet(String msg) {
		String result = "";
		
		String url = setParams(msg);
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		
		
		try {
			java.net.URL urlNet = new java.net.URL(url);
			HttpURLConnection conn = (HttpURLConnection)urlNet.openConnection();
			conn.setReadTimeout(5*1000);
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			
			is = conn.getInputStream();
			int len = -1;
			byte[] buf = new byte[128];
			baos = new ByteArrayOutputStream();
			
			while((len=is.read(buf)) != -1) {
				baos.write(buf,0,len);
			}
			baos.flush();
			result = new String(baos.toByteArray());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(baos!=null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				
		}
		
		
		return result;
	}

	//���õ�ַ����
	private static String setParams(String msg) {
		String url = "";
		try {
			url = URL+"?key="+API_KEY+"&info="+URLEncoder.encode(msg,"utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}