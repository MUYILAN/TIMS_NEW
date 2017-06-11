package com.tims.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import com.tims.model.Source;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

public class HttpUtil {
	public static final String BASE_URL = "http://192.168.42.80:8080/WebProject/";
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

	public static void downloadFileRequest(final String address,
			final Source sou,final HttpCallbackListener httpCallbackListener) {
		Context context = ContextUtil.getContext();
		String filename = null;
		try {
			filename = URLEncoder.encode(sou.getFileName(),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		final String message = "courseid="+sou.getCourseId()+"&filename="+filename;
		
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
				    
				    
				    String path = Environment.getExternalStorageDirectory()+"/TIMSDownload";
					File file = new File(path);
                	if(!(new File(path)).exists()){
                		 System.out.println("TIMS文件夹不存在！");
                         if(file.mkdirs())
                        	 System.out.println("创建TIMSDownload文件夹成功！");
                         else
                        	 System.out.println("创建TIMSDownload文件夹失败！");
                	}
                	/*
                	path = path+"/"+sou.getCourseId();
                	if(!(new File(path)).exists()){
               		 System.out.println(sou.getCourseId()+"文件夹不存在！");
                        if(file.mkdirs())
                        	System.out.println("创建课程文件夹成功！");
                        else
                        	System.out.println("创建课程文件夹失败！");
               	}*/
                	
                		InputStream in = connection.getInputStream();

                 	    int len;
                 	    byte[] buffer = new byte[1024];
                         FileOutputStream outputStream = new FileOutputStream(path+"/"+sou.getFileName());
                         
             	        while ((len = in.read(buffer, 0, 1024)) != -1) {
             	            outputStream.write(buffer, 0, len);
             	        }
             	        outputStream.flush();
             	        outputStream.close();
             	  
         	        if (httpCallbackListener != null) {
						httpCallbackListener.onFinish("TRUE");}
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
	 public static void uploadFileRequest(final String address,final String filepath,final Map<String, String> params, final HttpCallbackListener listener) {
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
		            String CHARSET = "utf-8";
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
		            
		            
//		            StringBuilder sb2 = null;
		            StringBuilder sb = new StringBuilder();
		            for (Map.Entry<String, String> entry : params.entrySet()) {
		                sb.append(PREFFIX);
		                sb.append(BOUNDARY);
		                sb.append(LINEND);
		                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
		                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
		                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
		                sb.append(LINEND);
		                sb.append(entry.getValue());
		                sb.append(LINEND);

		            }
//		            OutputStream out = connection.getOutputStream();
		            DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
		            outStream.write(sb.toString().getBytes(CHARSET));
//					((DataOutputStream) out0).writeBytes(message);
//					out0.flush();
//					out0.close();
		         // 构建发送字符串数据
		                String fileName = params.get("filename");
		                StringBuilder sb1 = new StringBuilder();
		                sb1.append(PREFFIX);
		                sb1.append(BOUNDARY);
		                sb1.append(LINEND);
		                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + LINEND);
		                sb1.append("Content-Type: application/octet-stream;chartset=" + CHARSET + LINEND);
		                sb1.append(LINEND); 
		                // 写入到输出流中
		                outStream.write(sb1.toString().getBytes());
		                // 将文件读入输入流中
		                InputStream is = new FileInputStream(filepath+"/"+fileName);
		                byte[] buffer = new byte[1024];
		                int len = 0;
		                // 写入输出流
		                while ((len = is.read(buffer)) != -1) {
		                    outStream.write(buffer, 0, len);
		                }
		                is.close(); 
		                // 添加换行标志
		                outStream.write(LINEND.getBytes());
		            
		            // 请求结束标志
		            byte[] end_data = (PREFFIX + BOUNDARY + PREFFIX + LINEND).getBytes();
		            outStream.write(end_data);
		            outStream.flush();
		            
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
