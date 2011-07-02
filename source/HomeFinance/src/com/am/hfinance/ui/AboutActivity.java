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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.am.hfinance.R;

public class AboutActivity extends Activity implements OnClickListener {
	private static final Uri URL_TWEET = Uri.parse("http://twitter.com/#!denysnik");
	private static final Uri URL_BLOG = Uri.parse("http://denys-nikolayenko.blogspot.com");
	private static final Uri URL_SITE = Uri.parse("http://home.finance.ua/");	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		findViewById(R.id.btn_site).setOnClickListener(this);
		findViewById(R.id.btn_blog).setOnClickListener(this);
		findViewById(R.id.btn_tweet).setOnClickListener(this);
	}

	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.btn_site:
			startActivity(new Intent(Intent.ACTION_VIEW, URL_SITE));
			break;
		case R.id.btn_blog:
			startActivity(new Intent(Intent.ACTION_VIEW, URL_BLOG));
			break;
		case R.id.btn_tweet:
			startActivity(new Intent(Intent.ACTION_VIEW, URL_TWEET));
			break;
		}
		
	}
}
