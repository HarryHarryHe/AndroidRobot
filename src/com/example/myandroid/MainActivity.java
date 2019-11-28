package com.example.myandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText et1 = null;
	EditText et2 = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void login(View view) {
		
		et1 = (EditText)findViewById(R.id.editText1);
		et2 = (EditText)findViewById(R.id.editText2);
		
		final String uname = et1.getText().toString();
		final String pwd = et2.getText().toString();
		
		System.out.println(uname);
		System.out.println(pwd);
		final Toast toast = Toast.makeText(getApplicationContext(), "������֤��½...", Toast.LENGTH_SHORT);
		toast.show();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String url = "http://192.168.237.129:8080/myweb/myservlet?uname="+uname+"&pwd="+pwd+"";
				HttpGet httpget = new HttpGet(url);
				HttpClient client = new DefaultHttpClient();
				
				try {
					HttpResponse res = client.execute(httpget);
				
					if(res.getStatusLine().getStatusCode()==200) {
						System.out.println("��������̨����");
						HttpEntity entity = res.getEntity();
						
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf8"));
						
						String success = br.readLine();
						System.out.println("���ǻ�ȡ�������ݣ�"+success);
						String uid = br.readLine();
						System.out.println("�����û�id:"+uid);
						
						if(success.equals("��½�ɹ�")) {
							toast.cancel();
							Intent i = new Intent(MainActivity.this,IndexActivity.class);
							//���ݲ�������һ��activityҳ��
							i.putExtra("uname", uname);
							i.putExtra("uid", uid);
							
							startActivity(i);
						}else {
							toast.cancel();
							//Looper.prepare();Looper.loop(); �ܹؼ����ð�ס�ڶ���toast����Ȼ����ʱ�������Ϊ���̵߳�Ե��
							Looper.prepare();
							Toast.makeText(getApplicationContext(), "��½ʧ��", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}
					}
//					client.execute(httpget);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void close(View view) {
		System.exit(0);
	}
}
