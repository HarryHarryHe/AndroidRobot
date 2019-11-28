package com.example.myandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class IndexActivity extends Activity {
	
	int flag = 0;
	ImageView daka = null;
	Button bt = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_main);
		Toast.makeText(getApplicationContext(), "��½�ɹ�", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void close(View view) {
		System.exit(0);
	}

	public void clicked(View view) {
		daka = (ImageView)findViewById(R.id.imageview);
		bt = (Button)findViewById(R.id.button1);
		
		if(flag==0) {
			//������ͼƬ
			daka.setImageResource(R.drawable.dkok);
			bt.setText("�򿨳ɹ�");
		}else if(flag>0) {
			bt.setText("���Ѵ�");
		}
		
		//ȡ��������Activity��Intent����   
	    Intent intent =getIntent();
	    //��ȡ���ݹ����Ĳ���
	    final String uname = intent.getStringExtra("uname");
        final String uid = intent.getStringExtra("uid");
//        new AlertDialog.Builder(this).setTitle("ϵͳ��Ϣ").setMessage("�û���"+uname+"���򿨳ɹ�!")
//		.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		}).show();

		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://192.168.237.129:8080/myweb/action?uname="+uname+"&uid="+uid+"";
				HttpGet httpget = new HttpGet(url);
				HttpClient client = new DefaultHttpClient();

				try {
					HttpResponse res = client.execute(httpget);
					if(res.getStatusLine().getStatusCode()==200) {
						HttpEntity entity = res.getEntity();
						
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf8"));
						
						String success = br.readLine();
						System.out.println("���ǻ�ȡ�������ݣ�"+success);
						
						if(success.equals("�򿨳ɹ�")) {
							flag++;

							Looper.prepare();
							AlertDialog.Builder builder  = new Builder(IndexActivity.this);
							 builder.setTitle("ϵͳ��Ϣ" ) ;
							 builder.setMessage(uname+"���򿨳ɹ���" ) ;
							 builder.setPositiveButton("ȷ��" ,  null );
							 builder.show(); 
							Looper.loop();
							System.out.println("�򿨳ɹ�");
							
						}else {
							flag++;
							
							Looper.prepare();
							AlertDialog.Builder builder  = new Builder(IndexActivity.this);
							 builder.setTitle("ϵͳ��Ϣ" ) ;
							 builder.setMessage("�����Ѿ�������ˣ�������������~" ) ;
							 builder.setPositiveButton("ȷ��" ,  null );
							 builder.show(); 
							Looper.loop();
							System.out.println("�����Ѿ�������");
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	//����������
	public void wechat(View view) {
		Intent i = new Intent(IndexActivity.this,ChatActivity.class);
		startActivity(i);
	}
	
	//��ʾ���մ��б�
	public void view_list(View view) {
		//ȡ��������Activity��Intent����   
	    Intent intent =getIntent();
	    //��ȡ���ݹ����Ĳ���
	    final String uname = intent.getStringExtra("uname");
		Thread t= new Thread() {

			@Override
			public void run() {
				String url = "http://192.168.237.129:8080/myweb/View_list?uname="+uname+"";
				HttpGet httpget = new HttpGet(url);
				HttpClient client = new DefaultHttpClient();

				try {
					HttpResponse res = client.execute(httpget);
					if(res.getStatusLine().getStatusCode()==200) {
						HttpEntity entity = res.getEntity();
						
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf8"));
						
						String data = br.readLine().replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\[", "").replaceAll("\\]", "");
						
						System.out.println(data);
//						.replaceAll("{", "").replaceAll("}", "")
//						System.out.println(data);
//						Daka_list[] array = new Gson().fromJson(data,Daka_list[].class);
//						List<Daka_list> list = Arrays.asList(array);
//						for(Daka_list l:list) {
//							System.out.println(l.getCurrent_time());
//						}
						
						Looper.prepare();
						new AlertDialog.Builder(IndexActivity.this)
						.setTitle("�򿨼�¼")
						.setItems(new String[] {data}, null)
						.setNegativeButton("ȷ��", null)
						.show();
						Looper.loop();
						
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
//		try {
//			t.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}
