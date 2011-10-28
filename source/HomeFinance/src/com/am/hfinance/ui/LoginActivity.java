/*
 * Copyright (C) 2011 Android Monsters
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.am.hfinance.ui;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.am.hfinance.Const;
import com.am.hfinance.R;
import com.am.hfinance.api.ApiClient;
import com.am.hfinance.api.ApiException;
import com.am.hfinance.api.AuthResponse;

public class LoginActivity extends AccountAuthenticatorActivity implements Const, OnClickListener {
    public static final String PARAM_CONFIRMCREDENTIALS = "confirmCredentials";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    
    private String email;
    private String password;
	private EditText editEmail;
	private EditText editPassword;
	private ProgressDialog progress;
	private AccountManager accountManager;
	private ApiClient apiClient;
    
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.login);
		initRefs();
		
		accountManager = AccountManager.get(this);
		apiClient = ApiClient.instance();
	}

	private void initRefs() {
		editEmail = (EditText)findViewById(R.id.edit_email);
		editPassword = (EditText)findViewById(R.id.edit_password);
		findViewById(R.id.btn_login).setOnClickListener(this);
		
		progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage(getString(R.string.progress_signin));
	}

	public void onClick(View v) {
		email = editEmail.getText().toString();
		password = editPassword.getText().toString();
		new LoginTask().execute();
	}
	
	private void showProgress() {
		progress.show();
	}
	
	private void hideProgress() {
		progress.hide();
	}
	
	private void showMessage(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	}
	
	private class LoginTask extends AsyncTask<Void, Void, Runnable> {
		@Override
		protected Runnable doInBackground(Void... params) {
			Runnable result = null;
			
			try {
				AuthResponse AuthResponse = apiClient.authenticate(email, password);
				result = new SuccessRunnable(AuthResponse);
			} catch (ApiException e) {
				if(e.isNetworkDown()) {
					result = new NetworkErrorRunnable();
				} else if(e.isLogicalError()) {
					result = new FailureRunnable(); 
				} else {
					result = new TechnicalIssueRunnable();
					// TODO add ACRA logging
				}
			}
			
			return result;
		}
		
		@Override
		protected void onPreExecute() {
			showProgress();
		}
		
		@Override
		protected void onPostExecute(Runnable result) {
			hideProgress();
			result.run();
		}
	}
	
	private class SuccessRunnable implements Runnable {
		private AuthResponse authResponse;

		public SuccessRunnable(AuthResponse authResponse) {
			this.authResponse = authResponse;
		}

		public void run() {
			// Register new account.
			final Account account = new Account(email, ACCOUNT_TYPE);
			final Bundle params = new Bundle();
			params.putString(USER_ID, authResponse.idUser);
			params.putString(USER_ACCOUNT, authResponse.idAccount);
			params.putString(USER_ROLE, authResponse.role);
			
			accountManager.addAccountExplicitly(account, password, params);
			
			// Send new intent back to the AccountManager.
	        final Intent intent = new Intent();
	        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
	        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, authResponse.token);

            setAccountAuthenticatorResult(intent.getExtras());
	        setResult(RESULT_OK, intent);
	        finish();			
		}
	}
	
	private class FailureRunnable implements Runnable {
		public void run() {
			showMessage(R.string.toast_signin_failure);
		}
	}

	private class NetworkErrorRunnable implements Runnable {
		public void run() {
			showMessage(R.string.toast_signin_network_error);
		}
	}

	private class TechnicalIssueRunnable implements Runnable {
		public void run() {
			showMessage(R.string.toast_signin_technical_issue);
		}
	}
}