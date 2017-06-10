package com.tims.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tims.model.Subject;
import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.SubjectAdapter;
import com.tims.util.TitleLayout;
import com.tims.util.Utility;

public class HDSQActivity extends Activity implements OnClickListener{

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		querySubjectInfo();
		LogUtil.d("HDSQActivity", "onRestart");
	}

	private int courseId;
	private TextView tvNullSubject;
	private ListView lvSubject;
	private List<Subject> subjectList;
	private LinearLayout llAddSub;
	private EditText etAddSub;
	private Button btnAddSub;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hdsq);
		
		TitleLayout rlTitle = (TitleLayout) findViewById(R.id.titlelayout_hdsq);
		rlTitle.setText("互动社区");
		
		tvNullSubject = (TextView) findViewById(R.id.tv_nullsubject);
		lvSubject = (ListView) findViewById(R.id.list_subject);
		llAddSub = (LinearLayout) findViewById(R.id.ll_addsub);
		etAddSub = (EditText) findViewById(R.id.et_addsub);
		btnAddSub = (Button) findViewById(R.id.btn_addsub);
		
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("course",0);
		courseId = pref.getInt("id", 0);
		pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String type = pref.getString("type", "");
		
		if(type.equals("teacher")){
			llAddSub.setVisibility(View.GONE);
		}else{
			llAddSub.setVisibility(View.VISIBLE);
		}
		btnAddSub.setOnClickListener(this);
		
		querySubjectInfo();
	}

	private void querySubjectInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "SubjectsServlet";
		String message = "id="+courseId;
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("社区主题的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取主题失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lvSubject.setVisibility(View.GONE);
							tvNullSubject.setVisibility(View.VISIBLE);
						}
					});
					
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lvSubject.setVisibility(View.VISIBLE);
							tvNullSubject.setVisibility(View.GONE);
							subjectList = Utility.handleSubjectResponse(HDSQActivity.this, response);
							SubjectAdapter adapter = new SubjectAdapter(HDSQActivity.this, R.layout.item_subject, subjectList);
							lvSubject.setAdapter(adapter);
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
		
		lvSubject.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
				Subject sub = subjectList.get(position);
				Intent intent = new Intent(HDSQActivity.this, ReplyActivity.class);
				intent.putExtra("subject_data", sub);
				startActivity(intent);
			}
			});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		try{
		String strContent = URLEncoder.encode(etAddSub.getText().toString(),"utf-8");
		SimpleDateFormat fm = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		final String strDate = URLEncoder.encode(fm.format(date),"utf-8");
		
		LogUtil.d("Date",strDate);
		LogUtil.d("content",strContent);
		
		if(strContent.equals("")){
		}else{
			SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("user",0);
			String studentId = pref.getString("acc", "");
			String url = HttpUtil.BASE_URL + "AddSubServlet";
			String message = "sql=insert into subject(sub_courseid,sub_studentid,sub_content,sub_date,sub_reply) values ('"+courseId+"','"+studentId+"','"+strContent+"','"+strDate+"','"+0+"')";
			
			HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
				@Override
				public void onFinish(final String response) {
					LogUtil.d("添加主题的返回信息", response);	
					if (response.equals("FALSE")) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(ContextUtil.getContext(),"添加失败!", Toast.LENGTH_SHORT).show();
							}
						});
					}else{
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(ContextUtil.getContext(),"添加成功!", Toast.LENGTH_SHORT).show();
								/*Intent intent = new Intent();
								intent.putExtra("content", strContent);
								intent.putExtra("date", strDate);
								setResult(RESULT_OK, intent);
								finish();*/
								//刷新页面，清空编辑框
								etAddSub.setText("");
								querySubjectInfo();
								//btnAddSub.setEnabled(false);

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
		}	 catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
