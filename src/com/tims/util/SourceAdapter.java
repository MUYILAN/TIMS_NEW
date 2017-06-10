package com.tims.util;

import java.util.List;

import com.tims.activity.R;
import com.tims.model.Source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SourceAdapter extends ArrayAdapter<Source> {

	private int resourceId;
	
	public SourceAdapter(Context context, int resource, List<Source> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Source src = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView tvFilename = (TextView) view.findViewById(R.id.tv_sou_filename);
		TextView tvTeachername = (TextView) view.findViewById(R.id.tv_sou_teachername);
		TextView tvDate = (TextView) view.findViewById(R.id.tv_sou_date);
		tvFilename.setText(src.getFileName());
		tvTeachername.setText("上传者:"+src.getTeacherName());
		tvTeachername.setTextColor(R.color.light_gray);
		tvDate.setText("上传日期:"+src.getDate());
		tvDate.setTextColor(R.color.light_gray);
		return view;
	}
}
