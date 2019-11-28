package com.example.myandroid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.harry.bean.ChatMessage;
import com.harry.bean.ChatMessage.Type;
import com.harry.utils.HttpUtils;

import com.baidu.translate.main.*;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity{
	
	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;
	
	private EditText mInputMsg;
	private Button mSendMsg;
	private TextView toUser;
	
	
	//���ڸ���������
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//�ȴ����գ����߳�������ݵķ���
			ChatMessage fromMessage = (ChatMessage) msg.obj;
			mDatas.add(fromMessage);
			mAdapter.notifyDataSetChanged();
			mMsgs.setSelection(mDatas.size()-1);
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		
		initView();
		initDatas();
		// ��ʼ���¼�
		initListener();
	}

	private void initListener(){
		
		mSendMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String toMsg = mInputMsg.getText().toString();
				
				//��ʾ�Է���������
				toUser = (TextView) findViewById(R.id.toUser);
				toUser.setText("�Է���������");
				
				
				//�������߳�ִ��
				final String JpMsg = Main.getJpString(toMsg);    //�������
				
				
				//�����ж�
				if(TextUtils.isEmpty(toMsg)) {
					Toast.makeText(ChatActivity.this, "������Ϣ����Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				
				ChatMessage toMessage = new ChatMessage();
				toMessage.setDate(new Date());
				toMessage.setMsg(JpMsg);		//�������Ľ�����
				toMessage.setType(Type.OUTCOMING);
				mDatas.add(toMessage);
				mAdapter.notifyDataSetChanged();
				mMsgs.setSelection(mDatas.size()-1);
				mInputMsg.setText("");  //�������������
				
				
				new Thread() {
					public void run() {
						ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);  //���������������Ӧ�������̣߳�������Ҫ�����߳�,�����̲߳��ܸ������̵߳Ŀؼ�������Ҫfinal
						Message m = Message.obtain();
						m.obj = fromMessage;
						try {
							sleep(1500);   //ģ������ٶ�
							mHandler.sendMessage(m);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};}.start();
				
			}
		});
	}

	private void initDatas() {
		mDatas = new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("�Τ�̫����������ʲô��",Type.INCOMING,new Date()));
		mAdapter = new ChatMessageAdapter(this, mDatas);
		
		mMsgs.setAdapter(mAdapter);
	}

	private void initView() {
		mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
		
	}
	
	public void fanhui(View view) {
		System.exit(0);
	}
}
