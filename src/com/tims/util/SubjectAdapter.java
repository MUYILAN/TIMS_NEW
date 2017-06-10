package com.tims.util;

import java.util.List;

import com.tims.activity.R;
import com.tims.model.Subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SubjectAdapter extends ArrayAdapter<Subject> {

	private int resourceId;
	
	public SubjectAdapter(Context context, int resource, List<Subject> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Subject sub = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		} else {
			view = convertView;
		}
		TextView tvStudentId = (TextView) view.findViewById(R.id.tv_stuid);
		TextView tvStudentName = (TextView) view.findViewById(R.id.tv_stuname);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_sub_content);
		TextView tvReply = (TextView) view.findViewById(R.id.tv_sub_reply);
		TextView tvDate = (TextView) view.findViewById(R.id.tv_sub_date);
		tvStudentId.setText(sub.getStudentId());
		tvStudentName.setText(sub.getStudentName());
		tvContent.setText(sub.getContent());
		tvReply.setText("回复:"+sub.getReply());
		tvDate.setText(sub.getDate());
		return view;
	}
}