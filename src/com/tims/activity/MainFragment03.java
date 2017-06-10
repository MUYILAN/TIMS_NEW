package com.tims.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tims.util.TitleLayout;

public class MainFragment03 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_main03,container, false);
		TitleLayout rlTitle = (TitleLayout) view.findViewById(R.id.titlelayout_mf03);
		rlTitle.setBtnVisible(View.GONE);
		rlTitle.setText("我的消息");
		return view;
	}

}
