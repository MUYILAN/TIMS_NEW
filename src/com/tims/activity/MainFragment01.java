package com.tims.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tims.model.Course;
import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;

public class MainFragment01 extends Fragment implements OnClickListener{

	private Spinner spinner;
	private TextView tvNullCourse;
	private String course;
	private int courseId;
	private Button bKCTZ;
	private Button bKCZL;
	private Button bHDSQ;
//	private Button bZYGL;
	private Button bKQGL;
//	private Button bCJGL;
	
	private Course[] courses;
	private String[] name;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.fragment_main01,container, false);
		spinner = (Spinner) view.findViewById(R.id.spinner_mainfrag01);
		tvNullCourse = (TextView) view.findViewById(R.id.tv_nullcourse);
		bKCTZ = (Button) view.findViewById(R.id.ib01);
		bKCZL = (Button) view.findViewById(R.id.ib02);
		bHDSQ = (Button) view.findViewById(R.id.ib03);
//		bZYGL = (Button) view.findViewById(R.id.ib04);
		bKQGL = (Button) view.findViewById(R.id.ib05);
//		bCJGL = (Button) view.findViewById(R.id.ib06);
		
		bKCTZ.setOnClickListener(this);
		bKCZL.setOnClickListener(this);
		bHDSQ.setOnClickListener(this);
//		bZYGL.setOnClickListener(this);
		bKQGL.setOnClickListener(this);
//		bCJGL.setOnClickListener(this);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				courseId = arg2;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		initView();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences pref = ContextUtil.getContext().getSharedPreferences("user",0);
		String type = pref.getString("type", "");
		String acc = pref.getString("acc", "");
		
		String url = HttpUtil.BASE_URL + "CoursesServlet";
		String message = "type="+type+"&acc="+acc;
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("获取课程的返回信息", response);	
				if (response.equals("FALSE")) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取课程失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvNullCourse.setVisibility(View.VISIBLE);
							spinner.setVisibility(View.GONE);
							bKCTZ.setVisibility(View.GONE);
							bKCZL.setVisibility(View.GONE);
							bHDSQ.setVisibility(View.GONE);
//							bCJGL.setVisibility(View.GONE);
							bKQGL.setVisibility(View.GONE);
//							bZYGL.setVisibility(View.GONE);
						}
					});
	
					courses = null;
					name = null;
				}else{
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvNullCourse.setVisibility(View.GONE);
							spinner.setVisibility(View.VISIBLE);
							bKCTZ.setVisibility(View.VISIBLE);
							bKCZL.setVisibility(View.VISIBLE);
							bHDSQ.setVisibility(View.VISIBLE);
//							bCJGL.setVisibility(View.VISIBLE);
							bKQGL.setVisibility(View.VISIBLE);
//							bZYGL.setVisibility(View.VISIBLE);
							
							String[] allCourses = response.split(",");
							courses = new Course[allCourses.length];
							name = new String[allCourses.length];
							for(int i = 0; i < allCourses.length; i++ ){
								String[] array = allCourses[i].split("\\|");
								courses[i] = new Course(Integer.parseInt(array[0]),array[1]);
								name[i] =array[1];
							}
							ArrayAdapter<String> adapter=new ArrayAdapter<String>(ContextUtil.getContext(),
									android.R.layout.simple_spinner_item,name);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							//绑定 Adapter到控件
							spinner .setAdapter(adapter);
						}
					});
	
				}
			}
			@Override
			public void onError(Exception e) {

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ContextUtil.getContext(),"连接服务器失败!", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/////////////////////////////////////////////////
		
		course = spinner.getSelectedItem().toString();
		int id = 0;
		for(Course c:courses){
			if(c.getName().equals(course)){
				id = c.getId();
				break;
			}
		}
		SharedPreferences.Editor editor = ContextUtil.getContext().getSharedPreferences("course",0).edit();
		editor.putInt("id", id);
		editor.commit();

		switch(v.getId()){
		case R.id.ib01:
			Intent intent  = new Intent(ContextUtil.getContext(), KCTZActivity.class);
			startActivity(intent);
			break;
		case R.id.ib02:
			intent  = new Intent(ContextUtil.getContext(), KCZLActivity.class);
			startActivity(intent);
			break;
		case R.id.ib03:
			intent  = new Intent(ContextUtil.getContext(), HDSQActivity.class);
			startActivity(intent);
			break;
		/*case R.id.ib04:
			intent  = new Intent(ContextUtil.getContext(), ZYGLActivity.class);
			startActivity(intent);
			break;*/
		case R.id.ib05:
			intent  = new Intent(ContextUtil.getContext(), KQGLActivity.class);
			startActivity(intent);
			break;
		/*case R.id.ib06:
			intent  = new Intent(ContextUtil.getContext(), CJGLActivity.class);
			startActivity(intent);
			break;*/
		default:
			break;
		}
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		if((courses != null) && (name != null)){
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(ContextUtil.getContext(),android.R.layout.simple_spinner_item,name);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			//绑定 Adapter到控件
			spinner .setAdapter(adapter);
			
			spinner.setSelection(courseId);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	
}
