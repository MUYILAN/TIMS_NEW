package com.tims.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.TitleLayout;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText etUserAcc;
	private EditText etUserPasswd;
	private RadioGroup rgUserType;
	private RadioButton rbUserTypeTea;
	private RadioButton rbUserTypeStu;
	private CheckBox cbRemPasswd;
	private Button btnLogin;
	private Button btnRegister;
	private TitleLayout rlTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		etUserAcc = (EditText) findViewById(R.id.user_acc);
		etUserPasswd = (EditText) findViewById(R.id.user_passwd);
		rgUserType = (RadioGroup) findViewById(R.id.user_type);
		rbUserTypeTea = (RadioButton) findViewById(R.id.user_type_tea);
		rbUserTypeStu = (RadioButton) findViewById(R.id.user_type_stu);
		cbRemPasswd = (CheckBox) findViewById(R.id.cb_rempasswd);
		btnLogin = (Button) findViewById(R.id.login);
		btnRegister = (Button) findViewById(R.id.register);
		rlTitle = (TitleLayout) findViewById(R.id.titlelayout_login);
		
		SharedPreferences pref = ContextUtil.getContext().getSharedPreferences("rem_user",0);
		boolean isRemember = pref.getBoolean("rempassword", false);
		if (isRemember) {
		// 将账号和密码都设置到文本框中
		String acc = pref.getString("acc", "");
		String password = pref.getString("passwd", "");
		String type = pref.getString("type", "");
		etUserAcc.setText(acc);
		etUserPasswd.setText(password);
		if(type.equals("teacher")){
			rbUserTypeTea.setChecked(true);
			rbUserTypeStu.setChecked(false);
		}else{
			rbUserTypeTea.setChecked(false);
			rbUserTypeStu.setChecked(true);
		}
		cbRemPasswd.setChecked(true);
		}
		
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);

		rlTitle.setBtnVisible(View.GONE);
		rlTitle.setText("登录或注册账号");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.login:
			final String acc = etUserAcc.getText().toString();
			final String passwd = etUserPasswd.getText().toString();
			final String type = rgUserType.getCheckedRadioButtonId() == R.id.user_type_tea?"teacher":"student";
			
			if(!(acc.equals("")) && !(passwd.equals(""))){
				String url = HttpUtil.BASE_URL + "LoginServlet";
				String message;
				if(type.equals("teacher")){
					message = "type=teacher&sql=select * from teacher where tea_id='"+acc+"' and tea_passwd='"+passwd+"';"; 
				}else{
					message = "type=student&sql=select * from student where stu_id='"+acc+"' and stu_passwd='"+passwd+"';";
				}
				
				HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
					@Override
					public void onFinish(String response) {
						LogUtil.d("登录时服务器的返回信息", response);			
						if (response.equals("FALSE")) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ContextUtil.getContext(),"账号或密码错误!", Toast.LENGTH_SHORT).show();
								}
							});
							
						}else{
							String[] array = response.split("\\|");
							SharedPreferences.Editor editor1 = ContextUtil.getContext().getSharedPreferences("user",MODE_PRIVATE).edit();
							editor1.putString("type", type);
							editor1.putString("acc", array[0]);
							editor1.putString("name", array[1]);
							editor1.putString("sex", array[2]);
							editor1.putString("phone", array[3]);
							
							if(type.equals("student")){
								editor1.putString("classid", array[4]);
								editor1.putString("class", array[5]);
							}else{
								editor1.putString("title", array[4]);
							}
							
							editor1.commit();
							
							if (cbRemPasswd.isChecked()){
								SharedPreferences.Editor editor2 = ContextUtil.getContext().getSharedPreferences("rem_user",MODE_PRIVATE).edit();
								editor2.putBoolean("rempassword", true);
								editor2.putString("passwd", passwd);
								editor2.putString("type", type);
								editor2.putString("acc", acc);
								editor2.commit();
							}

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									startActivity(intent);
									LoginActivity.this.finish();
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
			}else{
				Toast.makeText(ContextUtil.getContext(),"账号或密码不能为空!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.register:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
