package com.tims.util;

public interface HttpCallbackListener {
	void onFinish(String response);
	void onError(Exception e);
}
