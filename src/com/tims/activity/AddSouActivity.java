package com.tims.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tims.util.ContextUtil;
import com.tims.util.FileAdapter;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddSouActivity extends ListActivity {
	private ProgressDialog progressDialog;
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = Environment.getExternalStorageDirectory()+"/";
	private String curPath = Environment.getExternalStorageDirectory()+"/";
	private String filename = null;
	private TextView selectPath;
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_addsou);
		selectPath = (TextView) findViewById(R.id.tv_addsou_path);
		Button btnOK = (Button) findViewById(R.id.btn_addsou_ok);
		btnOK.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(filename == null){
					Toast.makeText(AddSouActivity.this, "请选择文件!", Toast.LENGTH_LONG).show();
				}else{
					uploadSource();
				}
			}
		});
		Button btnCancle = (Button) findViewById(R.id.btn_addsou_cancle);
		btnCancle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		getFileDir(rootPath);
	}

	protected void uploadSource() {
		// TODO Auto-generated method stub
		showProgressDialog();
		
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("course",0);
		int courseid = pref.getInt("id", 0);
		pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String teacherid = pref.getString("acc", "");
		SimpleDateFormat fm = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String strDate = fm.format(date);
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("courseid", Integer.toString(courseid));
		params.put("teacherid", teacherid);
		params.put("date", strDate);
		params.put("filename", filename);
//		String message = "courseid="+courseid+"&teacherid="+teacherid+"&date="+strDate+"&filename="+filename;
		String url = HttpUtil.BASE_URL + "AddSouServlet";
		HttpUtil.uploadFileRequest(url, curPath, params, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("上传资料的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							Toast.makeText(ContextUtil.getContext(),"上传课程资料失败!", Toast.LENGTH_SHORT).show();
							finish();
						}
					});
				}else if(response.equals("TRUE")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
//							addSourceInfo();
							closeProgressDialog();
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
							finish();
						}
					});
					
				}else{}
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

	private void getFileDir(String filePath) {	

		selectPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();

		if (!filePath.equals(rootPath)) {
			items.add("b1");
			paths.add(rootPath);
			items.add("b2");
			paths.add(f.getParent());
		}
		if(files != null){
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				items.add(file.getName());
				paths.add(file.getPath());
			}
		}
		

		setListAdapter(new FileAdapter(this, items, paths));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = new File(paths.get(position));
		if (file.isDirectory()) {
			curPath = paths.get(position);
			getFileDir(paths.get(position));
		} else {
			filename = file.getName();
//			curPath += "/"+file.getName();
			selectPath.setText(curPath+"/"+file.getName());
//			openFile(file);
		}
	}
	/**
	* 显示进度对话框
	*/
	private void showProgressDialog() {
	if (progressDialog == null) {
	progressDialog = new ProgressDialog(this);
	progressDialog.setMessage("正在上传...");
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


	/*private void addSourceInfo() {
		// TODO Auto-generated method stub
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("course",0);
		int courseid = pref.getInt("id", 0);
		pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String teacherid = pref.getString("acc", "");
		SimpleDateFormat fm = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		final String strDate = fm.format(date);
		
		String message = "courseid='"+courseid+"'&teacherid='"+teacherid+"'&date='"+strDate+"'";
		String url = HttpUtil.BASE_URL + "AddSouServlet";
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("修改上传资料的返回信息", response);	
				if (response.equals("FALSE")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"修改课程资料失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("TRUE")){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
							finish();
						}
					});
					
				}else{}
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
	}*/
}
