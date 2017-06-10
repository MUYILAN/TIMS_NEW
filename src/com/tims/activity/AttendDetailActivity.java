package com.tims.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.tims.model.Attendance;
import com.tims.util.AttendAdapter;
import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.Utility;

public class AttendDetailActivity extends Activity {
	
	private ListView lvDetail;
	private String date;
	private int courseId;
	private String classId;
	private List<Attendance> attendList;
	private int currentStatus = -1;	
	
	private TextView tvTotal;
	private TextView tvOn;
	private TextView tvDelay;
	private TextView tvOff;
	private Spinner spinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_attenddetail);
		
		Intent intent = getIntent();
		try {
			date = URLEncoder.encode(intent.getStringExtra("date"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		courseId = intent.getIntExtra("course_id", 0);
		classId = intent.getStringExtra("class_id");
		
		ImageView btnBack = (ImageView) findViewById(R.id.btn_titleback_attdetail);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) ContextUtil.getContext()).finish();
			}
		});
		

		spinner = (Spinner) findViewById(R.id.spinner_attdetail);
		
		
		tvTotal = (TextView) findViewById(R.id.tv_attdetail_total);
		tvOn = (TextView) findViewById(R.id.tv_attdetail_on);
		tvDelay = (TextView) findViewById(R.id.tv_attdetail_delay);
		tvOff = (TextView) findViewById(R.id.tv_attdetail_off);
		lvDetail = (ListView) findViewById(R.id.list_attenddetail);
		
		queryAttendDetailInfo();
		
		
	}
	protected void modifyStatus() {
		// TODO Auto-generated method stub
		List<Attendance> tempList = new ArrayList<Attendance>();
		for(Attendance att:attendList){
			if(currentStatus == -1){
				tempList.add(att);
			}else if(att.getIsPresent() == currentStatus){
				tempList.add(att);
			}
		}
		Collections.sort(tempList, new Comparator<Attendance>(){

			@Override
			public int compare(Attendance o1, Attendance o2) {
				// TODO Auto-generated method stub
				long l1 = Long.parseLong(o1.getStudentId());
				long l2 = Long.parseLong(o2.getStudentId());
				if(l1 > l2){
					return 1;
				}else{
					return -1;
				}
			}
		}); 
		AttendAdapter adapter = new AttendAdapter(AttendDetailActivity.this, R.layout.item_attenddetail, tempList);
		lvDetail.setAdapter(adapter);
	}
	private void queryAttendDetailInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "AttendDetailServlet";
		String message = "courseid="+courseId+"&classid="+classId+"&date="+date;
		LogUtil.d("attdetail获得的参数", message);
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("考勤详情的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取考勤详情失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"暂时还没有考勤详情!", Toast.LENGTH_SHORT).show();
						}
					});
					
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							attendList = Utility.handleAttendResponse(AttendDetailActivity.this, response);
							Collections.sort(attendList, new Comparator<Attendance>(){

								@Override
								public int compare(Attendance o1, Attendance o2) {
									// TODO Auto-generated method stub
									long l1 = Long.parseLong(o1.getStudentId());
									long l2 = Long.parseLong(o2.getStudentId());
									if(l1 > l2){
										return 1;
									}else{
										return -1;
									}
								}
							}); 
							int totalNum = attendList.size();
							int onNum = 0;
							int delayNum = 0;
							int offNum = 0;
							for(Attendance att:attendList){
								if(att.getIsPresent() == 2){
									onNum++;
								}else if(att.getIsPresent() == 1){
									delayNum++;
								}else{
									offNum++;
								}
							}
							tvTotal.setText("应到:"+totalNum+"人");
							tvOn.setText("已到:"+onNum+"人");
							tvDelay.setText("请假:"+delayNum+"人");
							tvOff.setText("未到:"+offNum+"人");
							
							modifyStatus();
							spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
								//监听事件
								@Override
								public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
									 // TODO Auto-generated method stub
									currentStatus = arg2-1;
									modifyStatus();	 
								}
											
								@Override
								public void onNothingSelected(AdapterView<?> arg0) {
									// TODO Auto-generated method stub
															
								}
							});
//							AttendAdapter adapter = new AttendAdapter(AttendDetailActivity.this, R.layout.item_attenddetail, attendList);
//							lvDetail.setAdapter(adapter);
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
