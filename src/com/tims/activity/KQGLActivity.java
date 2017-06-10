package com.tims.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.TitleLayout;

public class KQGLActivity extends Activity {
	
	private int courseId;
	private ListView lvClass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_kqgl);
		
		TitleLayout rlTitle = (TitleLayout) findViewById(R.id.titlelayout_kqgl);
		rlTitle.setText("考勤管理");
		lvClass = (ListView) findViewById(R.id.list_class);
		
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("course",0);
		courseId = pref.getInt("id", 0);
		pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String type = pref.getString("type", "");
				
		if(type.equals("teacher")){
			queryClassInfo(pref.getString("acc", ""));
			
		}else{
			this.finish();
			Intent intent = new Intent(this,AttendanceActivity.class);
			intent.putExtra("course_id", courseId);
			intent.putExtra("class_id", pref.getString("classid", ""));
			startActivity(intent);
		}
		
		lvClass.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
				String classId = (String) lvClass.getItemAtPosition(position);
				Intent intent = new Intent(KQGLActivity.this,AttendanceActivity.class);
				intent.putExtra("course_id", courseId);
				intent.putExtra("class_id", classId);
				startActivity(intent);
			}
		});
		

	}
	
	private void queryClassInfo(String teacherId) {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "ClassesServlet";
		String message = "teacherid="+teacherId+"&courseid="+courseId;
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("获取班级的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取班级失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
						}
					});
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String[] classes = response.split("\\|");;
							LogUtil.d("class的长度", classes.length+"");
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(KQGLActivity.this, android.R.layout.simple_list_item_1, classes);
							lvClass.setAdapter(adapter);
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

	
}
