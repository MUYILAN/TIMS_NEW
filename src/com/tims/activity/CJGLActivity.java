package com.tims.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.tims.util.TitleLayout;

public class CJGLActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cjgl);
		
		TitleLayout rlTitle = (TitleLayout) findViewById(R.id.titlelayout_cjgl);
		rlTitle.setText("成绩管理");
	}

	
}
