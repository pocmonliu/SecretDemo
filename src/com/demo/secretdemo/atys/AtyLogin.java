package com.demo.secretdemo.atys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.secretdemo.Config;
import com.demo.secretdemo.R;
import com.demo.secretdemo.net.GetCode;
import com.demo.secretdemo.net.Login;
import com.demo.secretdemo.tools.MD5Tool;

public class AtyLogin extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_login);

		etPhone = (EditText)this.findViewById(R.id.etPhoneNum);
		etCode = (EditText)this.findViewById(R.id.etCode);
		
		findViewById(R.id.btnGetCode).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (TextUtils.isEmpty(etPhone.getText())) {
							Toast.makeText(AtyLogin.this,
									R.string.phone_num_can_not_be_empty,
									Toast.LENGTH_LONG).show();
							return;
						}

						final ProgressDialog pd = ProgressDialog
								.show(AtyLogin.this,
										getResources().getString(
												R.string.Connecting_notice),
										getResources()
												.getString(
														R.string.Connecting_server_please_wait));

						new GetCode(etPhone.getText().toString(),
								new GetCode.SuccessCallback() {

									@Override
									public void onSuccess() {
										pd.dismiss();
										Toast.makeText(AtyLogin.this,
												R.string.success_to_get_code,
												Toast.LENGTH_LONG).show();
									}
								}, new GetCode.FailCallback() {

									@Override
									public void onFail() {
										pd.dismiss();
										Toast.makeText(AtyLogin.this,
												R.string.fail_to_get_code,
												Toast.LENGTH_LONG).show();
									}
								});
					}
				});

		findViewById(R.id.btnLogin).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if(TextUtils.isEmpty(etPhone.getText())){
							Toast.makeText(AtyLogin.this,
									R.string.phone_num_can_not_be_empty,
									Toast.LENGTH_LONG).show();
							return;
						}
						
						if(TextUtils.isEmpty(etCode.getText())){
							Toast.makeText(AtyLogin.this,
									R.string.code_can_not_be_empty,
									Toast.LENGTH_LONG).show();
							return;
						}
						
						new Login(MD5Tool.MD5(etPhone.getText().toString()), etCode.getText().toString(), new Login.SuccessCallback() {
							
							@Override
							public void onSuccess(String token) {
								Config.CacheToken(AtyLogin.this, token);
								Config.CachePhoneNum(AtyLogin.this, etPhone.getText().toString());
								
								Intent i = new Intent(AtyLogin.this, AtyTimeLine.class);
								i.putExtra(Config.KEY_TOKEN, token);
								i.putExtra(Config.KEY_PHONE_NUM, etPhone.getText().toString());
								startActivity(i);
								
								finish();
								
							}
						}, new Login.FailCallback() {
							
							@Override
							public void onFail() {
								Toast.makeText(AtyLogin.this, R.string.fail_to_login, Toast.LENGTH_LONG).show();
							}
						});
					}

				});

	}

	private EditText etPhone = null;
	private EditText etCode = null;
}
