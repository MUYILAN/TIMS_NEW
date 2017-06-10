package com.tims.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tims.model.Reply;
import com.tims.model.Subject;
import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.ReplyAdapter;
import com.tims.util.TitleLayout;
import com.tims.util.Utility;

public class ReplyActivity extends Activity implements OnClickListener{

	private TextView tvStuId;
	private TextView tvStuName;
	private TextView tvContent;
	private TextView tvReply;
	private TextView tvDate;
	
	private ListView lvReply;
	private List<Reply> replyList;
	private EditText etAddRep;
	private Button btnAddRep;
	
	private Subject sub;
	private int subjectId;
	private int reply;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hdsq_reply);
		
		TitleLayout rlTitle = (TitleLayout) findViewById(R.id.titlelayout_hdsq_reply);
		rlTitle.setText("主题详情");
		
		sub = (Subject) getIntent().getSerializableExtra("subject_data");
		reply = sub.getReply();
		subjectId = sub.getId();
		
		tvStuId = (TextView) findViewById(R.id.tv_stuid_sub_rep);
		tvStuName = (TextView) findViewById(R.id.tv_stuname_sub_rep);
		tvContent = (TextView) findViewById(R.id.tv_sub_content_rep);
		tvDate = (TextView) findViewById(R.id.tv_sub_date_rep);
		tvReply = (TextView) findViewById(R.id.tv_sub_reply_rep);
		
		tvStuId.setText(sub.getStudentId());
		tvStuName.setText(sub.getStudentName());
		tvContent.setText(sub.getContent());
		tvDate.setText(sub.getDate());
		tvReply.setText("回复:"+reply);
		
		lvReply = (ListView) findViewById(R.id.list_reply);
		etAddRep = (EditText) findViewById(R.id.et_addrep);
		btnAddRep = (Button) findViewById(R.id.btn_addrep);
		btnAddRep.setOnClickListener(this);
	
		//queryReplySubjectInfo();	
		queryReplyInfo();		
	}

/*private void queryReplySubjectInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "ReplySubjectServlet";
		String message = "id="+subjectId;
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("回复的主题的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取回复的主题失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lvReply.setVisibility(View.VISIBLE);
							Subject sub  = Utility.handleReplSubjectyResponse(ReplyActivity.this, response);
							tvStuId.setText(sub.getStudentId());
							tvStuName.setText(sub.getStudentName());
							tvContent.setText(sub.getContent());
							tvDate.setText(sub.getDate());
							tvReply.setText("回复:"+sub.getReply());
						}
					});
				}
			}
			@Override
			public void onError(Exception e) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ContextUtil.getContext(),"连接服务器失败!", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		});
	}*/

		private void queryReplyInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "ReplysServlet";
		String message = "id="+sub.getId();
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("回复的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取回复失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							//lvReply.setVisibility(View.GONE);
						}
					});
					
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lvReply.setVisibility(View.VISIBLE);
							replyList = Utility.handleReplyResponse(ReplyActivity.this, response);
							ReplyAdapter adapter = new ReplyAdapter(ReplyActivity.this, R.layout.item_reply, replyList);;
							lvReply.setAdapter(adapter);;
						}
					});
				}
			}
			@Override
			public void onError(Exception e) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ContextUtil.getContext(),"连接服务器失败!", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		reply++;
		tvReply.setText("回复:"+reply);
		
		try{
		String strContent = URLEncoder.encode(etAddRep.getText().toString(),"utf-8");
		SimpleDateFormat fm = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		final String strDate = URLEncoder.encode(fm.format(date),"utf-8");
		
		LogUtil.d("Date",strDate);
		LogUtil.d("content",strContent);
		
		if(strContent.equals("")){
		}else{
			SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("user",0);
			String type = pref.getString("type", "");
			String typeId = pref.getString("acc", "");
			String url = HttpUtil.BASE_URL + "AddRepServlet";
			String message = "sql=insert into reply(rep_subjectid,rep_type,rep_typeid,rep_content,rep_date) values ('"+subjectId+"','"+type+"','"+typeId+"','"+strContent+"','"+strDate+"')&id="+subjectId;
			
			HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
				@Override
				public void onFinish(final String response) {
					LogUtil.d("添加回复的返回信息", response);	
					if (response.equals("FALSE")) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(ContextUtil.getContext(),"添加失败!", Toast.LENGTH_SHORT).show();
							}
						});
					}else{
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(ContextUtil.getContext(),"添加成功!", Toast.LENGTH_SHORT).show();
								/*Intent intent = new Intent();
								intent.putExtra("content", strContent);
								intent.putExtra("date", strDate);
								setResult(RESULT_OK, intent);
								finish();*/
								//刷新页面，清空编辑框
								etAddRep.setText("");
								//btnAddSub.setEnabled(false);
								//queryReplySubjectInfo();
								queryReplyInfo();
								
							}
						});

					}
				}
				@Override
				public void onError(Exception e) {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"连接服务器失败!", Toast.LENGTH_SHORT).show();
						}
						});
					}
				});
		
		}
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
