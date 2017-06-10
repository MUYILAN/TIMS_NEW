package com.tims.util;

import java.util.List;

import com.tims.activity.R;
import com.tims.model.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Message> {

	private int resourceId;
	
	public MessageAdapter(Context context, int resource, List<Message> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Message mes = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_mes_content);
		TextView tvDate = (TextView) view.findViewById(R.id.tv_mes_date);
		tvContent.setText(mes.getContent());
		tvDate.setText(mes.getDate());
		return view;
	}
}
