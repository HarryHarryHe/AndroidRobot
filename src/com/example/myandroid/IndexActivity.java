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
		Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
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
			//设置新图片
			daka.setImageResource(R.drawable.dkok);
			bt.setText("打卡成功");
		}else if(flag>0) {
			bt.setText("今已打卡");
		}
		
		//取得启动该Activity的Intent对象   
	    Intent intent =getIntent();
	    //获取传递过来的参数
	    final String uname = intent.getStringExtra("uname");
        final String uid = intent.getStringExtra("uid");
//        new AlertDialog.Builder(this).setTitle("系统消息").setMessage("用户："+uname+"，打卡成功!")
//		.setNegativeButton("确定", new DialogInterface.OnClickListener() {
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
						System.out.println("这是获取到的内容："+success);
						
						if(success.equals("打卡成功")) {
							flag++;

							Looper.prepare();
							AlertDialog.Builder builder  = new Builder(IndexActivity.this);
							 builder.setTitle("系统消息" ) ;
							 builder.setMessage(uname+"：打卡成功！" ) ;
							 builder.setPositiveButton("确认" ,  null );
							 builder.show(); 
							Looper.loop();
							System.out.println("打卡成功");
							
						}else {
							flag++;
							
							Looper.prepare();
							AlertDialog.Builder builder  = new Builder(IndexActivity.this);
							 builder.setTitle("系统消息" ) ;
							 builder.setMessage("今天已经打过卡了，请明天再来吧~" ) ;
							 builder.setPositiveButton("确认" ,  null );
							 builder.show(); 
							Looper.loop();
							System.out.println("今天已经打卡了噢");
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
	//进入聊天室
	public void wechat(View view) {
		Intent i = new Intent(IndexActivity.this,ChatActivity.class);
		startActivity(i);
	}
	
	//显示今日打卡列表
	public void view_list(View view) {
		//取得启动该Activity的Intent对象   
	    Intent intent =getIntent();
	    //获取传递过来的参数
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
						.setTitle("打卡记录")
						.setItems(new String[] {data}, null)
						.setNegativeButton("确定", null)
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
