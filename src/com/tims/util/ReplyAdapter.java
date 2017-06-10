package com.tims.util;

import java.util.List;

import com.tims.activity.R;
import com.tims.model.Reply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReplyAdapter extends ArrayAdapter<Reply> {

	private int resourceId;
	
	public ReplyAdapter(Context context, int resource, List<Reply> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Reply rep = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		} else {
			view = convertView;
		}
		TextView tvTypeId = (TextView) view.findViewById(R.id.tv_typeid);
		TextView tvTypeName = (TextView) view.findViewById(R.id.tv_typename);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_rep_content);
		ImageView ivIsTea = (ImageView) view.findViewById(R.id.iv_is_tea);
		TextView tvDate = (TextView) view.findViewById(R.id.tv_rep_date);
		tvTypeId.setText(rep.getTypeId());
		tvTypeName.setText(rep.getTypeName());
		if(rep.getType().equals("student")){
			ivIsTea.setVisibility(View.GONE);
		}else{
			ivIsTea.setVisibility(View.VISIBLE);
		}
		tvContent.setText(rep.getContent());
		tvDate.setText(rep.getDate());
		return view;
	}
}