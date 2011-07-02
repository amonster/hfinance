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
