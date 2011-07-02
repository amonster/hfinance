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
