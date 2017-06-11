package com.tims.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tims.model.Source;
import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.SourceAdapter;
import com.tims.util.TitleLayout;
import com.tims.util.Utility;

public class KCZLActivity extends Activity {
	private ProgressDialog progressDialog;
	private Source sou;

	private int id;
	private TextView tvNullSource;
	private ListView listKCZL;
	private SourceAdapter adapter;
	private List<Source> sourceList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_kczl);
		
		TitleLayout rlTitle = (TitleLayout) findViewById(R.id.titlelayout_kczl);
		rlTitle.setText("课程资料");
		
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("course",0);
		id = pref.getInt("id", 0);
		pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String type = pref.getString("type", "");
				
		if(type.equals("teacher")){
			Button btn = new Button(this);
			RelativeLayout.LayoutParams r = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			r.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			btn.setLayoutParams(r);
			btn.setText("上传");
			btn.setTextColor(Color.WHITE);
//			btn.setTextSize(20);
//			btn.setBackgroundColor(R.color.blue);
			btn.getBackground().setAlpha(0);
			rlTitle.addView(btn);
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(KCZLActivity.this, AddSouActivity.class);
//					startActivity(intent);
					startActivityForResult(intent,1);
				}
				
			});
		}		
		tvNullSource = (TextView) findViewById(R.id.tv_nullsource);
		listKCZL = (ListView) findViewById(R.id.list_source);
		listKCZL.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				sou = sourceList.get(position);
				downloadSourceInfo();
			}
			});
		
		querySourceInfo();
	}




	protected void downloadSourceInfo() {
		// TODO Auto-generated method stub
		showProgressDialog();
		
		String url = HttpUtil.BASE_URL + "DownSouServlet";
		HttpUtil.downloadFileRequest(url, sou, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("下载资料的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							Toast.makeText(ContextUtil.getContext(),"下载课程资料失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							closeProgressDialog();
							Toast.makeText(ContextUtil.getContext(),"下载完成!文件已保存到\"/TIMSDownload/"+sou.getFileName(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
			@Override
			public void onError(Exception e) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ContextUtil.getContext(),"连接服务器失败!", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		});

	}




	private void querySourceInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "SourcesServlet";
		String message = "id="+id;
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("课程资料的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							Toast.makeText(ContextUtil.getContext(),"获取课程资料失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							listKCZL.setVisibility(View.GONE);
						}
					});
					
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvNullSource.setVisibility(View.GONE);
							sourceList = Utility.handleSourceResponse(KCZLActivity.this, response);
							adapter = new SourceAdapter(KCZLActivity.this, R.layout.item_source, sourceList);
							listKCZL.setAdapter(adapter);
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

	/**
	* 显示进度对话框
	*/
	private void showProgressDialog() {
	if (progressDialog == null) {
	progressDialog = new ProgressDialog(this);
	progressDialog.setMessage("正在下载...");
	progressDialog.setCanceledOnTouchOutside(false);
	}
	progressDialog.show();
	}
	/**
	* 关闭进度对话框
	*/
	private void closeProgressDialog() {
	if (progressDialog != null) {
	progressDialog.dismiss();
	}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 1:
				if (resultCode == RESULT_OK) {
					querySourceInfo();
					LogUtil.d("上传完成后调用", "querySourceInfo");
				}
				break;
			default:
		}
	}
	
}
