package com.tims.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tims.activity.R;
import com.tims.model.Attendance;

public class ClassAdapter extends ArrayAdapter<Attendance> {

	private int resourceId;
	
	public ClassAdapter(Context context, int resource, List<Attendance> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Attendance stu = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView tvStudentId = (TextView) view.findViewById(R.id.tv_addatt_id);
		TextView tvStudentName = (TextView) view.findViewById(R.id.tv_addatt_name);
		
		RadioGroup rgIsPresent = (RadioGroup) view.findViewById(R.id.rg_addatt);
		rgIsPresent.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

					int RadioButtonId = group.getCheckedRadioButtonId();

					if (RadioButtonId == R.id.rb_addatt_0) {
						stu.setIsPresent(0);
					} else if (RadioButtonId == R.id.rb_addatt_1) {
						stu.setIsPresent(1);
					} else if (RadioButtonId == R.id.rb_addatt_2) {
						stu.setIsPresent(2);
					} 
			}
		});
		tvStudentId.setText(stu.getStudentId());
		tvStudentName.setText(stu.getStudentName());
		return view;
	}
}
