package com.tims.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.tims.model.Attendance;
import com.tims.util.ClassAdapter;
import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.TitleLayout;
import com.tims.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AddAttActivity extends Activity {
	private ProgressDialog progressDialog;
	private int courseId;
	private String classId;

	private List<Attendance> studentList;
	private ListView lvStudent;
	private Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addatt);

		TitleLayout rlTitle = (TitleLayout) findViewById(R.id.titlelayout_addatt);
		rlTitle.setText("开始点名");
		
		Intent intent = getIntent();
		courseId = intent.getIntExtra("course_id", 0);
		classId = intent.getStringExtra("class_id");	
		
		lvStudent = (ListView) findViewById(R.id.list_addatt);		
		queryStudentInfo();
		
		btnSubmit = (Button) findViewById(R.id.addatt_ok);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgressDialog();
				try {
					SimpleDateFormat fm = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					final String strDate = URLEncoder.encode(fm.format(date),"utf-8");
								
					String message = "sql=";
					for (int i = 0; i < studentList.size(); i++) {
						message = message + "insert into attendance(att_courseid,att_classid,att_date,att_studentid,att_ispresent) values('"+courseId+"','"+classId+"','"+strDate+"','"+studentList.get(i).getStudentId()+"','"+studentList.get(i).getIsPresent()+"')|";
					}
					String url = HttpUtil.BASE_URL + "AddAttServlet";
					
					HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
						@Override
						public void onFinish(final String response) {
							LogUtil.d("添加点名的返回信息", response);	
							if (response.equals("FALSE")) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										closeProgressDialog();
										Toast.makeText(ContextUtil.getContext(),"添加失败!", Toast.LENGTH_SHORT).show();
									}
								});
							}else{
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										closeProgressDialog();
										Toast.makeText(ContextUtil.getContext(),"添加成功!", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent();
										setResult(RESULT_OK, intent);
										finish();
									}
								});

							}
						}
						@Override
						public void onError(Exception e) {

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									closeProgressDialog();
									Toast.makeText(ContextUtil.getContext(),"连接服务器失败!", Toast.LENGTH_SHORT).show();
								}
								});
							}
						});
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}



	private void queryStudentInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "StudentsServlet";
		String message = "classid='"+classId+"'";
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("所有学生的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取所有学生失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"暂时还没有学生信息!", Toast.LENGTH_SHORT).show();
						}
					});
					
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							studentList = Utility.handleStudentResponse(AddAttActivity.this, response);
							Collections.sort(studentList, new Comparator<Attendance>(){

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
							ClassAdapter adapter = new ClassAdapter(AddAttActivity.this, R.layout.item_addatt, studentList);
							 lvStudent.setAdapter(adapter);
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

	/**
	* 显示进度对话框
	*/
	private void showProgressDialog() {
	if (progressDialog == null) {
	progressDialog = new ProgressDialog(this);
	progressDialog.setMessage("正在上传...");
	progressDialog.setCanceledOnTouchOutside(false);
	}
	progressDialog.show();
	}
	/**
	* 关闭进度对话框
	*/
	private void closeProgressDialog() {
	if (progressDialog != null) {
	progressDialog.dismiss();
	}
	}

}
