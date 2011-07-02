package com.am.hfinance.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import com.am.hfinance.R;

public class HelpActivity extends Activity {
	
	public static final String EXTRA_HELP_TOPIC = "com.am.hfinance.help_topic";
	public static final String EXTRA_HELP_CONTENT = "com.am.hfinance.help_content";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		Intent intent = getIntent();
		
		if(intent != null) {
			String topic = intent.getStringExtra(EXTRA_HELP_TOPIC);
			String content = intent.getStringExtra(EXTRA_HELP_CONTENT);
			
			TextView topicView = (TextView)findViewById(R.id.text_help_topic);
			TextView contentView = (TextView)findViewById(R.id.text_help_content);
			
			topicView.setText(topic);
			contentView.setText(content);
			
			Linkify.addLinks(contentView, Linkify.ALL);
		}
	}
}
