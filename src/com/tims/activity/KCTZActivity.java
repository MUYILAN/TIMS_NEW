package com.tims.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tims.model.Message;
import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.MessageAdapter;
import com.tims.util.TitleLayout;
import com.tims.util.Utility;

public class KCTZActivity extends Activity {

	private int id;
	private TextView tvNullMessage;
	private ListView listKCTZ;
	private List<Message> messageList = new ArrayList<Message>();
	private MessageAdapter adapter;
	private TitleLayout rlTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_kctz);
		
		rlTitle = (TitleLayout) findViewById(R.id.titlelayout_kctz);
		rlTitle.setText("课程通知");
		
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("course",0);
		id = pref.getInt("id", 0);
		pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String type = pref.getString("type", "");
				
		if(type.equals("teacher")){
			Button btn = new Button(this);
			RelativeLayout.LayoutParams r = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			r.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			btn.setLayoutParams(r);
			btn.setText("添加");
			btn.setTextColor(Color.WHITE);
//			btn.setTextSize(20);
//			btn.setBackgroundColor(R.color.blue);
			btn.getBackground().setAlpha(0);
			rlTitle.addView(btn);
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(KCTZActivity.this,AddMesActivity.class);
					intent.putExtra("id", id);
					startActivityForResult(intent, 1);
				}
				
			});
		}		
		tvNullMessage = (TextView) findViewById(R.id.tv_nullmessage);
		listKCTZ = (ListView) findViewById(R.id.list_message);
		
		queryMessageInfo();
	}


	
	private void queryMessageInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "MessagesServlet";
		String message = "id="+id;
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("课程通知的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取课程通知失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							listKCTZ.setVisibility(View.GONE);
						}
					});
					
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvNullMessage.setVisibility(View.GONE);
							messageList = Utility.handleMessagesResponse(KCTZActivity.this, response);
							adapter = new MessageAdapter(KCTZActivity.this, R.layout.item_message, messageList);
							listKCTZ.setAdapter(adapter);

						}
					});
					
					
				}
			}
			@Override
			public void onError(Exception e) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ContextUtil.getContext(),"连接服务器失败!", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		});
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 1:
				if (resultCode == RESULT_OK) {
					queryMessageInfo();
				}
				break;
			default:
		}
	}

}
