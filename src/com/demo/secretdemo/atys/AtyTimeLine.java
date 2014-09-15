package com.demo.secretdemo.atys;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.demo.secretdemo.Config;
import com.demo.secretdemo.R;
import com.demo.secretdemo.ld.MyContacts;
import com.demo.secretdemo.net.UploadContacts;
import com.demo.secretdemo.tools.MD5Tool;

public class AtyTimeLine extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_timeline);
		
		phone_num = getIntent().getStringExtra(Config.KEY_PHONE_NUM);
		token = getIntent().getStringExtra(Config.KEY_TOKEN);
		phone_md5 = MD5Tool.MD5(phone_num);
		
		new UploadContacts(phone_md5, token, MyContacts.getContactsJSONString(this), new UploadContacts.SuccessCallback() {
			
			@Override
			public void onSuccess() {
				Log.d("Secret: ", "返回成功.");
			}
		}, new UploadContacts.FailCallback() {
			
			@Override
			public void onFail(int errorCode) {
				if(errorCode == Config.RESULT_STATUS_INVALID_TOKEN){
					startActivity(new Intent(AtyTimeLine.this, AtyLogin.class));
					finish();
				}else{
					Log.d("Secret: ", "返回失败!"); 
				}
			}
		});
	}
	
	private String phone_num, token, phone_md5;
}
