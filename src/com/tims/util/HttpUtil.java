package com.tims.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class HttpUtil {
	public static final String BASE_URL = "http://192.168.42.13:8080/WebProject/";
	public static void sendHttpRequest(final String address,
			final String message,final HttpCallbackListener httpCallbackListener) {
		LogUtil.d("客户端提交", message);
		Context context = ContextUtil.getContext();
		if (!isNetworkAvailable(context)) {
			Toast.makeText(context, "网络不可用!",Toast.LENGTH_SHORT).show();
			return;}
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try{URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setDoInput(true);
					connection.setDoOutput(true);
					DataOutputStream out = new DataOutputStream(connection.getOutputStream()); 
					out.writeBytes(message);
					out.flush();
				    out.close();
				    InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);}
					if (httpCallbackListener != null) {
						httpCallbackListener.onFinish(response.toString());}
				} catch (Exception e) {
					if (httpCallbackListener != null) {
						e.printStackTrace();
						httpCallbackListener.onError(e);}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	 public static boolean isNetworkAvailable(Context context)
	    {
	        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
	        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        
	        if (connectivityManager == null)
	        {
	            return false;
	        }
	        else
	        {
	            // 获取NetworkInfo对象
	            @SuppressWarnings("deprecation")
				NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
	            
	            if (networkInfo != null && networkInfo.length > 0)
	            {
	                for (int i = 0; i < networkInfo.length; i++)
	                {
	                    System.out.println(i + ":状态:" + networkInfo[i].getState());
	                    System.out.println(i + ":类型:" + networkInfo[i].getTypeName());
	                    // 判断当前网络状态是否为连接状态
	                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
	                    {
	                        return true;
	                    }
	                }
	            }
	        }
	        return false;
	    }
	 public static void uploadFileRequest(final String address,final String filepath,final String filename, final HttpCallbackListener listener) {
		 Context context = ContextUtil.getContext();
			if (!isNetworkAvailable(context)) {
				Toast.makeText(context, "网络不可用!",Toast.LENGTH_SHORT).show();
				return;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {

					HttpURLConnection connection = null;
					String BOUNDARY = UUID.randomUUID().toString();
		            String PREFFIX = "--", LINEND = "\r\n";
		            String MULTIPART_FROM_DATA = "multipart/form-data";
		            String CHARSET = "UTF-8";
					try {
						URL url1 = new URL(address);
		            connection = ((HttpURLConnection) url1.openConnection());
					
		            connection.setDoInput(true);
		            connection.setDoOutput(true);
		            connection.setUseCaches(false);
		            connection.setConnectTimeout(120*1000);
		            connection.setRequestMethod("POST");
		            connection.setRequestProperty("keep-alive", "false");
		            connection.setRequestProperty("Charset", "UTF-8");
		            connection.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
		            
		            
		            StringBuilder sb2 = null;
		            StringBuilder sb = new StringBuilder();
		            sb.append(PREFFIX);
	                sb.append(BOUNDARY);
	                sb.append(LINEND);
	                sb.append("Content-Disposition: form-data; name=\"" + filepath + "\"" + LINEND);
	                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
	                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
	                sb.append(LINEND);
	                sb.append(filepath);
	                sb.append(LINEND);
		            OutputStream out = connection.getOutputStream();
//		            DataOutputStream out0 = new DataOutputStream(connection.getOutputStream());
//					((DataOutputStream) out0).writeBytes(message);
//					out0.flush();
//					out0.close();
					
					
		            byte[] buffer = new byte[1024];
		            // 读取文件
		            FileInputStream fileInputStream = new FileInputStream(filepath+"/"+filename);
		            LogUtil.d("wenjian", filepath+"/"+filename);
		            while((fileInputStream.read(buffer, 0, 1024)) != -1){
		                out.write(buffer);
		            }

		            out.flush();
		            out.close();
		            fileInputStream.close();

		            InputStream in = connection.getInputStream();
		            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
			
					if (listener != null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						e.printStackTrace();
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
				}
			}).start();
		}
		
}
