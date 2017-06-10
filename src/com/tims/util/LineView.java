package com.tims.util;

import com.tims.activity.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class LineView extends LinearLayout {

	    //主题
	    private String subject;

	    //标题
	    private String title;

	    //标题对应的内容
	    private String content;

	    //是否能够跳转
	    private boolean canNav;

	    //是否位于此主题下的最后一行
	    private boolean isBottom;

	    private TextView tv_title;

	    private TextView tv_content;

	    public LineView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        LayoutInflater.from(context).inflate(R.layout.line, this);
	        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineView, 0, 0);
	        try {
	            subject = ta.getString(R.styleable.LineView_subject);
	            title = ta.getString(R.styleable.LineView_heading);
	            content = ta.getString(R.styleable.LineView_content);
	            canNav = ta.getBoolean(R.styleable.LineView_canNav, false);
	            isBottom = ta.getBoolean(R.styleable.LineView_isBottom, false);
	            initView();
	        } finally {
	            ta.recycle();
	        }
	    }

	    private void initView() {
	        TextView tv_subject = (TextView) findViewById(R.id.tv_subject);
	        tv_title = (TextView) findViewById(R.id.tv_title);
	        tv_content = (TextView) findViewById(R.id.tv_content);
	        Switch switch_nav = (Switch) findViewById(R.id.switch_nav);
	        View view_bottomLine = findViewById(R.id.view_bottomLine);
	        if (subject == null || subject.length() == 0) {
	            tv_subject.setVisibility(GONE);
	        } else {
	            tv_subject.setText(subject);
	        }
	        tv_title.setText(title);
	        tv_content.setText(content);
	        switch_nav.setVisibility(canNav ? VISIBLE : INVISIBLE);
	        view_bottomLine.setVisibility(isBottom ? VISIBLE : GONE);
	    }

	    public void setTitle(String title) {
	        this.title = title;
	        tv_title.setText(title);
	    }

	    public void setContent(String content) {
	        this.content = content;
	        tv_content.setText(content);
	    }

	    public String getContent() {
	        return content;
	    }

	}