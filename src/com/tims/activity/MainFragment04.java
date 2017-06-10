package com.tims.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tims.util.ContextUtil;
import com.tims.util.LineView;
import com.tims.util.TitleLayout;

public class MainFragment04 extends Fragment implements OnClickListener{

	private LineView lvId;
	private LineView lvName;
	private LineView lvSex;
	private LineView lvPhone;
	private LineView lvSelf;
	private Button btnModify;
	private Button btnLogout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_main04,container, false);
		TitleLayout rlTitle = (TitleLayout) view.findViewById(R.id.titlelayout_mf04);
		rlTitle.setBtnVisible(View.GONE);
		rlTitle.setText("个人中心");
		
		lvId = (LineView) view.findViewById(R.id.line_id);
		lvName = (LineView) view.findViewById(R.id.line_name);
		lvSex = (LineView) view.findViewById(R.id.line_sex);
		lvPhone = (LineView) view.findViewById(R.id.line_phone);
		lvSelf = (LineView) view.findViewById(R.id.line_self);
		btnModify = (Button) view.findViewById(R.id.btn_modifyinfo);
		btnLogout = (Button) view.findViewById(R.id.btn_logout);
		btnModify.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
				
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String type = pref.getString("type", "");
		String id = pref.getString("acc", "");
		String name = pref.getString("name", "");
		String sex = pref.getString("sex", "");
		String phone = pref.getString("phone", "");
		
		lvId.setContent(id);
		lvName.setContent(name);
		lvSex.setContent(sex);
		lvPhone.setContent(phone);
		
		if(type.equals("teacher")){
			String title = pref.getString("title", "");
			lvSelf.setContent(title);
			lvSelf.setTitle("职称");
			lvId.setTitle("工号");
		}else{
			String claName = pref.getString("class", "");
			lvSelf.setContent(claName);
			lvSelf.setTitle("班级");
			lvId.setTitle("学号");
		}
			
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_modifyinfo:
			Intent intent1 = new Intent(getActivity(),ModifyActivity.class);
			startActivity(intent1);
			break;
		case R.id.btn_logout:
			Intent intent2 = new Intent(getActivity(),LoginActivity.class);
			getActivity().finish();
			startActivity(intent2);
			break;
		default:
		}
	}

}
