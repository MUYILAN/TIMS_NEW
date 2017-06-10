package com.tims.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class RegisterActivity extends Activity implements OnClickListener{

	private Button btnTeaReg;
	private Button btnStuReg;
	private ImageView ivBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		btnTeaReg = (Button) findViewById(R.id.btn_teareg);
		btnStuReg = (Button) findViewById(R.id.btn_stureg);
		ivBack = (ImageView) findViewById(R.id.iv_left);
		
		btnTeaReg.setOnClickListener(this);
		btnStuReg.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.btn_teareg:
			btnTeaReg.setTextColor(Color.parseColor("#90d7ec"));
			btnStuReg.setTextColor(Color.WHITE);
			btnTeaReg.setBackgroundResource(R.drawable.left_white);
			btnStuReg.setBackgroundResource(R.drawable.right_transparent);
			TeaRegFragment teaFragment = new TeaRegFragment(); 
			FragmentManager teaFragmentManager = getFragmentManager();
			FragmentTransaction teaTransaction = teaFragmentManager.beginTransaction();
			teaTransaction.replace(R.id.fl_regfragment, teaFragment);
			teaTransaction.commit();
			break;
		case R.id.btn_stureg:
			btnTeaReg.setTextColor(Color.WHITE);
			btnStuReg.setTextColor(Color.parseColor("#90d7ec"));
			btnTeaReg.setBackgroundResource(R.drawable.left_transparent);
			btnStuReg.setBackgroundResource(R.drawable.right_white);
			StuRegFragment stuFragment = new StuRegFragment(); 
			FragmentManager stuFragmentManager = getFragmentManager();
			FragmentTransaction stuTransaction = stuFragmentManager.beginTransaction();
			stuTransaction.replace(R.id.fl_regfragment, stuFragment);
			stuTransaction.commit();
			break;
		case R.id.iv_left:
			finish();
			break;
		default:
			break;
		}
	}	
}
