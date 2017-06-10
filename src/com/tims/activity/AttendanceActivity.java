package com.tims.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.TitleLayout;

public class AttendanceActivity extends Activity {
	
	private ListView lvDate;
	private int courseId;
	private String classId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_attendance);
		
		Intent intent = getIntent();
		courseId = intent.getIntExtra("course_id", 0);
		classId = intent.getStringExtra("class_id");
		
		TitleLayout rlTitle = (TitleLayout) findViewById(R.id.titlelayout_attendance);
		rlTitle.setText("考勤记录");
		lvDate = (ListView) findViewById(R.id.list_attendance);
		
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String type = pref.getString("type", "");
				
		if(type.equals("teacher")){
			Button btn = new Button(this);
			RelativeLayout.LayoutParams r = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			r.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			btn.setLayoutParams(r);
			btn.setText("点名");
			btn.setTextColor(Color.WHITE);
//			btn.setTextSize(20);
//			btn.setBackgroundColor(R.color.blue);
			btn.getBackground().setAlpha(0);
			rlTitle.addView(btn);
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(AttendanceActivity.this, AddAttActivity.class);
					intent.putExtra("course_id", courseId);
					intent.putExtra("class_id", classId);
					startActivityForResult(intent, 1);
				}
				
			});
		}		
		
		queryAttendanceInfo();
		
	}

	private void queryAttendanceInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "AttendancesServlet";
		String message = "courseid="+courseId+"&classid="+classId;
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("考勤日期的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取以往考勤记录失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"暂时还没有考勤记录!", Toast.LENGTH_SHORT).show();
						}
					});
					
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String[] dates = response.split("\\|");;
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttendanceActivity.this, android.R.layout.simple_list_item_1, dates);
							lvDate.setAdapter(adapter);
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
		
		lvDate.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
				String date = (String) lvDate.getItemAtPosition(position);
				Intent intent = new Intent(AttendanceActivity.this,AttendDetailActivity.class);
				intent.putExtra("date", date);
				intent.putExtra("course_id", courseId);
				intent.putExtra("class_id", classId);
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 1:
				if (resultCode == RESULT_OK) {
					queryAttendanceInfo();
				}
				break;
			default:
		}
	}
}
