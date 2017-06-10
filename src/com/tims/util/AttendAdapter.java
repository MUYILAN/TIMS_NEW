package com.tims.util;

import java.util.List;

import com.tims.activity.R;
import com.tims.model.Attendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AttendAdapter extends ArrayAdapter<Attendance>{
private int resourceId;
	
	public AttendAdapter(Context context, int resource, List<Attendance> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Attendance att = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView tvId = (TextView) view.findViewById(R.id.tv_detail_id);
		TextView tvName = (TextView) view.findViewById(R.id.tv_detail_name);
		TextView tvDetail = (TextView) view.findViewById(R.id.tv_detail);
		tvId.setText(att.getStudentId());
		tvName.setText(att.getStudentName());
		String detail = "";
		switch(att.getIsPresent()){
		case 0:
			detail = "未到";
			tvDetail.setTextColor(Color.GRAY);
			break;
		case 1:
			detail = "请假";
			tvDetail.setTextColor(0xff90d7ec);
			break;
		case 2:
			detail = "已到";
			tvDetail.setTextColor(Color.BLUE);
			break;
		default:
			break;
		}
		tvDetail.setText(detail);
		
		return view;
	}
}
