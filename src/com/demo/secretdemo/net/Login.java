package com.demo.secretdemo.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.demo.secretdemo.Config;

public class Login {
	public Login(String phone_md5, String code,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject obj = new JSONObject(result);

							switch (obj.getInt(Config.KEY_STATUS)) {
							case Config.RESULT_STATUS_SUCCESS:
								if(successCallback != null){
									successCallback.onSuccess(obj.getString(Config.KEY_TOKEN));
								}
								break;
							default:
								if(failCallback != null){
									failCallback.onFail();
								}
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail() {
						// TODO Auto-generated method stub

					}
				}, Config.KEY_ACTION, Config.ACTION_LOGIN,
				Config.KEY_PHONE_MD5, phone_md5, Config.KEY_CODE, code);
	}

	public static interface SuccessCallback {
		void onSuccess(String token);
	}

	public static interface FailCallback {
		void onFail();
	}
}
