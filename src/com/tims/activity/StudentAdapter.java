package com.tims.activity;

import java.util.List;
import com.tims.model.Attendance;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class StudentAdapter extends BaseAdapter {

	//四个listview的每个对应的getID状态
	private int optionID0, optionID1, optionID2;

	private SparseIntArray checked = new SparseIntArray();//保存哪些radio被选中，相当于hashmap但效率更高

	private LayoutInflater mInflater;
	private List<Attendance> studentList;//数据源

	public StudentAdapter(Context context, List<Attendance> stuList) {
		studentList = stuList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}

	private void init() {
		studentList.clear();
	}

	@Override
	public int getCount() {
		int count = studentList == null ? 0 : studentList.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		/*
		 * Object obj = records != null && records.size() > position ?
		 * records.get(position) : null; return obj;
		 */
		return null;
	}

	@Override
	public long getItemId(int position) {
		//return position;
		return 0;
	}

	private Integer index = -1;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_addatt, null);

			holder = new Holder();
			holder.studentId = (TextView) convertView.findViewById(R.id.tv_addatt_id);
			holder.studentName = (TextView) convertView.findViewById(R.id.tv_addatt_name);
			holder.option = (RadioGroup) convertView.findViewById(R.id.rg_addatt);

			holder.option2 = (RadioButton) convertView.findViewById(R.id.rb_addatt_2);
			optionID2 = holder.option2.getId();
			holder.option1 = (RadioButton) convertView.findViewById(R.id.rb_addatt_1);
			optionID1 = holder.option1.getId();
			holder.option0 = (RadioButton) convertView.findViewById(R.id.rb_addatt_0);
			optionID0 = holder.option0.getId();

			holder.option.setTag(position);

			holder.option.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = (Integer) v.getTag();
					}
					return false;
				}
			});

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
			holder.option.setTag(position);
		}

		//在显示RadioButton值之前，取消对其监听，否则会出现混乱的效果。
		holder.option.setOnCheckedChangeListener(null);
		holder.option.clearCheck();

		if (checked.indexOfKey(position) > -1) {
			holder.option.check(checked.get(position));
		} else {
			holder.option.clearCheck();
		}
		holder.option.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId > -1) {
					checked.put(position, checkedId);

					Attendance item = studentList.get(position);

					int RadioButtonId = group.getCheckedRadioButtonId();

					if (RadioButtonId == optionID0) {
						item.setIsPresent(0);
					} else if (RadioButtonId == optionID1) {
						item.setIsPresent(1);
					} else if (RadioButtonId == optionID2) {
						item.setIsPresent(2);
					} 
				} else {
					if (checked.indexOfKey(position) > -1) {
						checked.removeAt(checked.indexOfKey(position));
					}
				}
			}
		});

		Attendance item = studentList.get(position);
		if (item != null) {
			//防止afterTextChanged自动执行
			holder.studentId.setText(item.getStudentId());
			holder.studentName.setText(item.getStudentName());
			holder.option0.setText("未到");
			holder.option1.setText("请假");
			holder.option2.setText("已到");

		}
		holder.option.clearFocus();
		if (index != -1 && index == position) {
			holder.option.requestFocus();
		}
		return convertView;
	}

	private class Holder {
		private TextView studentId;
		private TextView studentName;
		private RadioGroup option;
		private RadioButton option2;
		private RadioButton option1;
		private RadioButton option0;
	}

}
